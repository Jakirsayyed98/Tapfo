package app.tapho.ui.Cabs

import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.*
import app.tapho.R
import app.tapho.databinding.FragmentUberCabsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.GeoWebChromeClientForUber
import app.tapho.utils.RC_SIGN_IN
import app.tapho.utils.REQUEST_FINE_LOCATION
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.net.URISyntaxException


class UberCabsFragment :BaseFragment<FragmentUberCabsBinding>(){

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
        // Inflate the layout for this fragment
        _binding = FragmentUberCabsBinding.inflate(inflater,container,false)



        getSearhAPI()
        return _binding?.root
    }

    private fun getSearhAPI() {
        viewModel.searchMiniApp(getUserId(),"uber",this,object : ApiListener<WebTCashRes, Any?> {
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                t!!.let {
                    initWebView(it)
                }
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(it: WebTCashRes) {

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
        binding.webView.webChromeClient = GeoWebChromeClientForUber(this)
        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)


            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progress.visibility =  View.GONE

            }

            override fun onPageCommitVisible(view: WebView?, url1: String?) {
                super.onPageCommitVisible(view, url1)
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                if (errorCode == -2) {

                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url123: String?): Boolean {
                url123?.let {
                    if (url123.startsWith("truecallersdk:")){
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
                    } else if (url123.startsWith("tel:")) {

                        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(url123)))
                    } else if (url123.startsWith("http") || url123.startsWith("https")) {
                        return false
                    } else if (url123.startsWith("intent")) {
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

        binding.webView.loadUrl(it.data.get(0).url!!)
        binding.webView.canGoBack()
        binding.webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && binding.webView.canGoBack()) {
                binding.webView.goBack()
                return@OnKeyListener true
            }
            false
        })
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
            UberCabsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}