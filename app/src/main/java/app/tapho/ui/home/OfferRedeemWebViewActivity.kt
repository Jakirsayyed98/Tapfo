package app.tapho.ui.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.ActivityOfferRedeemWebViewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.InternetErrorFragment
import app.tapho.ui.merchants.MerchantOfferDetailFragment
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.*
import java.net.URISyntaxException

class OfferRedeemWebViewActivity : BaseActivity<ActivityOfferRedeemWebViewBinding>(),
    ApiListener<WebTCashRes, Any?> {
    private var tCash: WebTCashRes? = null

    private var fullData: Boolean = false
    var CurrentUrl: String? = null
    // we will use these when user responds
    @JvmField
    var mGeolocationOrigin: String? = null

    @JvmField
    var mGeolocationCallback: GeolocationPermissions.Callback? = null

    companion object {
        fun openWebView(
            context: Context?,
            webViewUrl: String?,
            miniAppId: String?,
            iconUrl: String?,
            tCashType: String?,
            favourite: String?,
            cashback: String?,
        ) {
            context?.startActivity(Intent(context, OfferRedeemWebViewActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra(MINI_APP_ID, miniAppId)
                putExtra(ICON_URL, iconUrl)
                putExtra(TCASH_TYPE, tCashType)
                putExtra(IS_FAVOURITE, favourite)
                putExtra(CASHBACK, cashback)
            })
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_redeem_web_view)
        supportActionBar?.hide()
//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
//        }
        if (Build.VERSION.SDK_INT >= 21) {
            //setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            //window.statusBarColor(Color.TRANSPARENT)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.BLACK
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

        binding = ActivityOfferRedeemWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initwebViewOffer()
        setVisibility()
        getTCashData1()
    }
    private fun setWindowFlag(
        mainActivity: OfferRedeemWebViewActivity,
        flagTranslucentStatus: Int,
        b: Boolean
    ) {
        val window: Window = this.window
        val winlayout: WindowManager.LayoutParams = window.attributes
        if (b) {
            winlayout.flags != flagTranslucentStatus
        } else {
            winlayout.flags = flagTranslucentStatus
        }
        window.attributes = winlayout
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
    private fun setVisibility() {
        binding.share.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            // intent.setPackage("com.whatsapp")
            val appLink: String = "https://play.google.com/store/apps/details?id=app.tapho"
            val message: String =
                "I found an amazing product for you, hope you will like it checkout here"
            val newMessage: String =
                "also I’m saving on every shopping over 200+ stores like Flipkart, Myntra, Ajio, Mamaearth & more with Tapfo, Just download the app to save more"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey\n" + message + "\n\n" + CurrentUrl.toString() + "\n\n\n" + newMessage + "\n\n" + appLink
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
        binding.name.text=MerchantOfferDetailFragment.merchantName
//        intent.getStringExtra(MiniApp).let {
//                binding.terms1.text=tCash?.data?.get(0)?.name.toString()
//        }
        binding.backIv.setOnClickListener {

            onBackPressed()
        }
        // binding.favouriteIv.visibility = if (intent.getStringExtra(MINI_APP_ID).isNullOrEmpty()) View.GONE else View.VISIBLE
    }
    private fun getTCashData(loaderListener: LoaderListener?) {
        fullData = loaderListener != null
        viewModel.getMiniAppTCash(
            AppSharedPreference.getInstance(this).getUserId(),
            intent.getStringExtra(MINI_APP_ID),
            loaderListener,
            this
        )
    }
    private fun getTCashData1() {
        if (intent.getStringExtra(MINI_APP_ID).isNullOrEmpty().not())
            viewModel.getMiniAppTCash(AppSharedPreference.getInstance(this).getUserId(),
                intent.getStringExtra(MINI_APP_ID),
                this,
                object : ApiListener<WebTCashRes, Any?> {
                    override fun onSuccess(t: WebTCashRes?, mess: String?) {
                        t?.let {


                            if (it.errorCode == "0") {
                                tCash = it
                            }
                        }
                    }
                })
    }
    private fun initwebViewOffer() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
//        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)

        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.allowFileAccess = true
//        binding.webView.settings.builtInZoomControls = true

        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
        binding.webView.webChromeClient = GeoWebChromeClientOfferReddem(this)
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
            val data = tCash?.data?.get(0)?.cashback.toString()
            if (data.equals("null")) {
                binding.webView.loadUrl(it)
                CurrentUrl=it
              //  binding.name.text=tCash!!.data.get(0).name

            } else {
             //   binding.name.text=tCash!!.data.get(0).name
                binding.webView.loadUrl(it)
                CurrentUrl=it

            }
        }
    }

    override fun onSuccess(t: WebTCashRes?, mess: String?) {
        t?.let {
            if (it.errorCode == "0") {
                tCash = it
                kotlin.runCatching {

                   // binding.favouriteIv.isSelected = it.mini_app?.get(0)?.is_favourite == "1"
                }
            }
        }
    }
}