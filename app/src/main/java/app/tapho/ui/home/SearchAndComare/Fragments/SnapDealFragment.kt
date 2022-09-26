package app.tapho.ui.home.SearchAndComare.Fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import app.tapho.R
import app.tapho.databinding.FragmentAmazonBinding
import app.tapho.databinding.FragmentSnapDealBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.SearchAndComare.SearchAndCompareFragment
import app.tapho.ui.home.SearchAndComare.location.GeoWebChromeClientAmazon
import app.tapho.ui.home.SearchAndComare.location.GeoWebChromeClientSnapDeal
import app.tapho.utils.REQUEST_FINE_LOCATION
import java.net.URISyntaxException


class SnapDealFragment : BaseFragment<FragmentSnapDealBinding>() {
    // we will use these when user responds
    @JvmField
    var mGeolocationOrigin: String? = null

    @JvmField
    var mGeolocationCallback: GeolocationPermissions.Callback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSnapDealBinding.inflate(inflater,container,false)
        initWebView()
        return _binding?.root
    }

    fun initWebView() {
        _binding!!.webView.settings.javaScriptEnabled = true
        _binding!!.webView.settings.domStorageEnabled = true
        _binding!!.webView.settings.databaseEnabled = true
        _binding!!.webView.settings.setAppCacheEnabled(true)
        _binding!!.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        _binding!!.webView.settings.allowFileAccess = true
//        binding.webView.settings.builtInZoomControls = true

        _binding!!.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        CookieManager.getInstance().setAcceptThirdPartyCookies(_binding!!.webView, true)
        _binding!!.webView.webChromeClient = GeoWebChromeClientSnapDeal(this)

        _binding!!.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

            }

            override fun onPageCommitVisible(view: WebView?, url1: String?) {
                super.onPageCommitVisible(view, url1)
            }


            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?,
            ) {

                if (errorCode == -2) {
//                    toast("Internet error")
                }
            }


            override fun shouldOverrideUrlLoading(view: WebView, url123: String?): Boolean {
                url123?.let {

                    if (url123.startsWith("tel:")) {

                        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(url123)))
                    } else if (url123.startsWith("http") || url123.startsWith("https")) {
                        return false
                    } else
                        if (url123.startsWith("intent")) {
                            try {
                                val intent = Intent.parseUri(url123, Intent.URI_INTENT_SCHEME)
                                val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                                if (fallbackUrl != null) {
                                    view.loadUrl(fallbackUrl);
                                    return true
                                }
                            } catch (e: URISyntaxException) {
                                e.printStackTrace()
                            }
                        }
                }
                return super.shouldOverrideUrlLoading(view, url123)
            }


        }

        var Url= "https://www.snapdeal.com/search?keyword="+ SearchAndCompareFragment.dataQuery
        _binding!!.webView.loadUrl(Url)

        _binding!!.webView.canGoBack()


    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                if (_binding!!.webView.canGoBack()) {
                    _binding!!.webView.goBack()
                }
                else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_FINE_LOCATION -> {
                var allow = false
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // user has allowed this permission
                    allow = true
                }
                if (mGeolocationCallback != null) {
                    // call back to web chrome client
                    mGeolocationCallback?.invoke(mGeolocationOrigin, allow, false)
                }
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            SnapDealFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}