package app.tapho.ui.News

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.activity.result.ActivityResultLauncher
import app.tapho.R
import app.tapho.databinding.ActivityNewsWebViewBinding
import app.tapho.databinding.ActivityWebViewBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.InternetErrorFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.WebviewLoadingDialog
import app.tapho.utils.*
import java.net.URISyntaxException

class NewsWebViewActivity : BaseActivity<ActivityNewsWebViewBinding>() {
    @JvmField
    var mGeolocationOrigin: String? = null

    @JvmField
    var mGeolocationCallback: GeolocationPermissions.Callback? = null

    companion object {
        fun openwebView(
            context: Context?,
            webViewUrl: String?,
        ) {
            context?.startActivity(Intent(context, NewsWebViewActivity::class.java).apply {

                putExtra(WEB_VIEW_URL, webViewUrl)
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor=Color.BLACK
        binding.backIv.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        initWebView()
    }

    fun initWebView() {

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
//        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)

        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)

        binding.webView.webViewClient = object : WebViewClient() {

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
                    InternetErrorFragment.newInstance().show(supportFragmentManager, "")
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

        intent.getStringExtra(WEB_VIEW_URL)?.let {
            binding.webView.loadUrl(it)
            binding.url.text = it

        }


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
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

}