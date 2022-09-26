package app.tapho.ui.home.CabsContainer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import app.tapho.databinding.FragmentCabsBinding
import app.tapho.databinding.FragmentOlaBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.InternetErrorFragment
import app.tapho.utils.*
import java.net.URISyntaxException


class OlaFragment : BaseFragment<FragmentOlaBinding>() {
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
        OlaBinding = FragmentOlaBinding.inflate(inflater, container, false)
        LoadDataInWebView()

        return OlaBinding?.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun LoadDataInWebView() {
        OlaBinding!!.webView.settings.javaScriptEnabled = true
        OlaBinding!!.webView.settings.domStorageEnabled = true
        OlaBinding!!.webView.settings.databaseEnabled = true
        OlaBinding!!.webView.settings.setAppCacheEnabled(true)
        OlaBinding!!.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        OlaBinding!!.webView.settings.allowFileAccess = true
        OlaBinding!!.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        CookieManager.getInstance().setAcceptThirdPartyCookies(OlaBinding!!.webView, true)
        OlaBinding!!.webView.webChromeClient = GeoWebChromeClient1(this)


        OlaBinding!!.webView.webViewClient = object : WebViewClient() {

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
                    //      InternetErrorFragment.newInstance().show(supportFragmentManager, "")
                }
            }


            /*

//            override fun shouldOverrideUrlLoading(view: WebView, url123: String?): Boolean {
//                url123?.let {
//
//                    if (url123.startsWith("tel:")) {
//
//                        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(url123)))
//                    } else if (url123.startsWith("http") || url123.startsWith("https")) {
//                        return false
//                    } else
//                        if (url123.startsWith("intent")) {
//                            try {
//                                val intent = Intent.parseUri(url123, Intent.URI_INTENT_SCHEME)
//                                val fallbackUrl = intent.getStringExtra("browser_fallback_url")
//                                if (fallbackUrl != null) {
//                                    view.loadUrl(fallbackUrl);
//                                    return true
//                                }
//                            } catch (e: URISyntaxException) {
//                                e.printStackTrace()
//                            }
//                        }
//                }
//                return super.shouldOverrideUrlLoading(view, url123)
//            }



             */


        }
        OlaBinding!!.webView.loadUrl("https://tapfo.in/delta/link/Y0ZlN05zUnBkQjhBQUs4VjZhYng4OW5HUDJ5a2JvYUJlUlFUM3BLTFN5eGY1VGNScEJLQW9WYUQ2d2FhZjV5QTdibVZMZUVSTDc1NVNwN2xBKzFaU2RpQnVubUMyYy9UVEFBQURVSjZkaU15NWJzNTA5dXBObnhNV1RMS3BtS0syVUpDTEFNMEJqZS9mVkZ5a2NnR3dPSHFUajAwa1YwSTNRM1V4NFR0VUpjPQ==")
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


    fun onBackPressed() {
        OlaBinding!!.webView.canGoBack()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    if (OlaBinding!!.webView.canGoBack()) {
                        OlaBinding!!.webView.goBack()
                    } else {
                        activity?.finish()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            OlaFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}