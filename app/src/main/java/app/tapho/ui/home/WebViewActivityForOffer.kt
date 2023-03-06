package app.tapho.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.ActivityWebViewForOfferBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShort
import app.tapho.ui.BaseActivity
import app.tapho.ui.InternetErrorFragment
import app.tapho.ui.OfferWebViewSplashScreenFragment
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.one97.paytm.nativesdk.BasePaytmSDK.getApplicationContext
import java.net.URISyntaxException


class WebViewActivityForOffer :  BaseActivity<ActivityWebViewForOfferBinding>(),
    ApiListener<WebTCashRes, Any?> {


    private var loadingDialog: OfferWebViewSplashScreenFragment? = null
    private var tCash: WebTCashRes? = null
    private var fullData: Boolean = false
    private var onceDialogVisible: Boolean = false
    private val TAG = "WEB_VIEW_ERROR"
    private var res: WebTCashRes? = null
    var CurrentUrl: String? = ""
    var MerchantName: String? = ""
    var cashbackOnExit: String? = ""


    var storeDeals: StoreDeals? = null

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
            appcategoryid: String?,
        ) {
            context?.startActivity(Intent(context, WebViewActivityForOffer::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra(MINI_APP_ID, miniAppId)
                putExtra(ICON_URL, iconUrl)
                putExtra(TCASH_TYPE, tCashType)
                putExtra(IS_FAVOURITE, favourite)
                putExtra(CASHBACK, cashback)
                putExtra(APPCATEGORYID, appcategoryid)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewForOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.backIv.setOnClickListener {
            onBackPressed()
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )




        intent!!.getStringExtra(MINI_APP_ID).let {
            when (it) {
                "582"->{
                    Glide.with(applicationContext).load(R.drawable.near_buy_ig).into(binding.Icon)
                }
                "325"->{
                    Glide.with(applicationContext).load(R.drawable.fancode_img).into(binding.Icon)
                }
                 "81"->{
                     Glide.with(applicationContext).load(R.drawable.new_bookmyshow).into(binding.Icon)

                 }
                  "573"->{
                      Glide.with(applicationContext).load(R.drawable.skyscanner_icon).into(binding.Icon)

                 }
               "585"->{
                   Glide.with(applicationContext).load(R.drawable.olx_icon).centerCrop().into(binding.Icon)
                 }
                 "24"->{
                     Glide.with(applicationContext).load(R.drawable.olx_icon).centerCrop().into(binding.Icon)
                 }


                else -> {}
            }
        }
            initWebView()

        binding.refresh.setOnClickListener {
            initWebView()
        }

    }



    fun initWebView() {
        loadingDialog = OfferWebViewSplashScreenFragment.showDialog(supportFragmentManager,intent.getStringExtra(MINI_APP_ID).toString())
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
        binding.webView.webChromeClient = GeoWebChromeClientWebViewActivityForOffer(this)


        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)


                if (onceDialogVisible.not())
                    getTCashDataAfterSomeTime()


            }

            override fun onPageCommitVisible(view: WebView?, url1: String?) {
                super.onPageCommitVisible(view, url1)
                dismissLoadingDialog()
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
            val cutdata = data.removeRange(0, 4)
            binding.webView.loadUrl("javascript:ub=function(){"+"document.getElementsById('app')[0].style.display ='none';" + "})()")



            if (data.toString().equals("null")) {
                    //   binding.cashback1234.visibility = View.GONE
                    cashbackOnExit = intent.getStringExtra(CASHBACK)
                    binding.webView.loadUrl(it)
                    CurrentUrl = it

                }
                else {
                    cashbackOnExit = data
                    binding.webView.loadUrl(it)
                    CurrentUrl = it
                }


        }


    }



    private fun dismissLoadingDialog() {
        kotlin.runCatching {
            if (loadingDialog?.isVisible == true)
                loadingDialog?.dismiss()
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


    private fun getTCashDataAfterSomeTime() {
        onceDialogVisible = true
        Handler(Looper.getMainLooper()).postDelayed({
            getTCashData(null)
        }, 10000)
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

    override fun onSuccess(t: WebTCashRes?, mess: String?) {
        onceDialogVisible = true
        t?.let {

            if (it.errorCode == "0") {
                tCash = it
                kotlin.runCatching {
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.exitbutton, null)

        dialog.setCancelable(false)
        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)

        exit.text = getString(R.string.leave_on, MerchantName)


        val exit_tag_text: TextView = view.findViewById(R.id.exit_tag_text)
        exit_tag_text.text = getString(R.string.you_might_miss_out, MerchantName)
        exit_tag_text.visibility=View.GONE
        val cashback_on_Exit: TextView = view.findViewById(R.id.cashback_on_Exit)
        if (cashbackOnExit.toString().contains("null")) {
            cashback_on_Exit.visibility = View.GONE
            exit_tag_text.visibility = View.GONE
            exit.text = "Exit"
        } else {
            cashback_on_Exit.text = cashbackOnExit
        }
        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)
        exit.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra(MINI_APP_ID, intent.getStringExtra(MINI_APP_ID))

            })
            finish()
            super.onBackPressed()
        }
        app_continue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }





}