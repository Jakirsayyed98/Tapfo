package app.tapho.ui.home.ShopProduct

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.AppCompatButton
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.ActivityNewWebViewBinding
import app.tapho.databinding.ActivityWebViewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.ui.BaseActivity
import app.tapho.ui.InternetErrorFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.WebviewLoadingDialog
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.URISyntaxException

class NewWebViewActivity : BaseActivity<ActivityNewWebViewBinding>(), ApiListener<WebTCashRes, Any?> {
    private var loadingDialog: WebviewLoadingDialog? = null
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

        var banner=""
        fun openwebView(
            context: Context?,
            webViewUrl: String?,
        ) {
            context?.startActivity(Intent(context, NewWebViewActivity::class.java).apply {

                putExtra(WEB_VIEW_URL, webViewUrl)
            })
        }

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
            context?.startActivity(Intent(context, NewWebViewActivity::class.java).apply {
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


        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= 21) {

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

        binding = ActivityNewWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVisibility()
        getTCashData1()

        if (intent.getStringExtra(MINI_APP_ID).isNullOrEmpty() ||
            intent.getStringExtra(TCASH_TYPE) != "1"
        )
            initWebView()
        else {
            binding.toolbar.visibility = View.INVISIBLE
        }

        binding.cashback1234.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.termsAndCondition)
            binding.termsAndCondition.visibility = View.VISIBLE


        }

        binding.gotit1.setOnClickListener {

            binding.termsAndCondition.visibility = View.GONE
            TransitionManager.beginDelayedTransition(binding.termsAndCondition)
        }

        binding.secure.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.termsAndCondition)
            binding.securety.visibility = View.VISIBLE
            binding.cardView.isClickable = false
            binding.webView.isClickable = false
        }
        binding.gotit.setOnClickListener {
            binding.securety.visibility = View.GONE
            TransitionManager.beginDelayedTransition(binding.termsAndCondition)
            binding.cardView.isClickable = true
            binding.webView.isClickable = true
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
        binding.backIv.setOnClickListener {

            onBackPressed()
        }
        binding.favouriteIv.isSelected = intent.getStringExtra(IS_FAVOURITE) == "1"
        binding.favouriteIv.setOnClickListener {
            if (it.isSelected) makeUnFav() else makeFav()
        }
    }

    fun initWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
//        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)

        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
        binding.webView.webChromeClient = GeoWebChromeClientNewWebView(this)

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (onceDialogVisible.not() /*&& binding.tcashbackLi.visibility == View.VISIBLE*/){

                }
            }

            override fun onPageCommitVisible(view: WebView?, url1: String?) {
                super.onPageCommitVisible(view, url1)
                dismissLoadingDialog()
//                appLog("WEBVIEW onPageCommitVisible")
            }


            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?,
            ) {
                if (errorCode == -2) {
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

            if (data.toString().equals("null")) {
                //   binding.cashback1234.visibility = View.GONE
                binding.cashback1234.text = intent.getStringExtra(CASHBACK)
                binding.cashbackinTerms.text = intent.getStringExtra(CASHBACK)
                cashbackOnExit = intent.getStringExtra(CASHBACK)
                binding.webView.loadUrl(it)
                CurrentUrl = it
            }
            else {
                binding.cashback1234.visibility = View.VISIBLE
                cashbackOnExit = data
                binding.cashbackinTerms.text = cutdata.toString()
                binding.cashback1234.text = data //tCash?.data?.get(0)?.cashback.toString()
                binding.webView.loadUrl(it)
                CurrentUrl = it
            }
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

    private fun dismissLoadingDialog() {
        kotlin.runCatching {
            if (loadingDialog?.isVisible == true)
                loadingDialog?.dismiss()
        }
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

                            setTermsData(it)
                            if (it.errorCode == "0") {
                                tCash = it
                                binding.favouriteIv.isSelected =
                                    it.mini_app?.get(0)?.is_favourite == "1"
                                if (intent.getStringExtra(MINI_APP_ID).isNullOrEmpty().not() && intent.getStringExtra(TCASH_TYPE) == "1"){

                                }


                            }
                        }
                    }
                })
    }

    private fun setTermsData(it: WebTCashRes) {
        MerchantName = it.mini_app!!.get(0).name
        binding.terms1.text = getString(R.string.terms_condition_popup, it.mini_app!!.get(0).name)
        binding.terms2.text = getString(R.string.cashback_credit_as_pending_, it.mini_app!!.get(0).name)
        binding.terms6.text = getString(R.string.decline_cashback, it.mini_app!!.get(0).name)
        binding.secureLine1.text = getString(R.string.this_browsing_is_, it.mini_app!!.get(0).name)
        binding.secureLine2.text = getString(R.string.this_browsing, it.mini_app!!.get(0).name)
        binding.creditHours.text = it.data[0].report
        MerchantName = it.mini_app!!.get(0).name.toString()


    }
    override fun onSuccess(t: WebTCashRes?, mess: String?) {
        onceDialogVisible = true
        t?.let {

            if (it.errorCode == "0") {
                tCash = it
                kotlin.runCatching {
                    binding.favouriteIv.isSelected = it.mini_app?.get(0)?.is_favourite == "1"
                }
            }
        }
    }

    private fun makeFav() {
        viewModel.miniAppFev(AppSharedPreference.getInstance(this).getUserId(),
            intent.getStringExtra(MINI_APP_ID),
            this,
            object : ApiListener<BaseRes, Any?> {
                override fun onSuccess(t: BaseRes?, mess: String?) {
                    t?.let {
                        if (it.errorCode == "0") {
                            binding.favouriteIv.isSelected = true
                            customToast(getString(R.string.added_in_fev), false)
                        } else toast(mess)
                    }
                }
            })
    }

    private fun makeUnFav() {
        viewModel.miniAppUnFev(AppSharedPreference.getInstance(this).getUserId(),
            intent.getStringExtra(MINI_APP_ID),
            this,
            object : ApiListener<BaseRes, Any?> {
                override fun onSuccess(t: BaseRes?, mess: String?) {
                    t?.let {
                        if (it.errorCode == "0") {
                            binding.favouriteIv.isSelected = false
                            customToast(getString(R.string.removed_from_fav), true)
                        } else toast(mess)
                    }
                }
            })
    }

    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.exitbutton, null)

        dialog.setCancelable(false)
        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)

        exit.text = getString(R.string.leave_on, MerchantName)


        val exit_tag_text: TextView = view.findViewById(R.id.exit_tag_text)

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
                putExtra(IS_FAVOURITE, binding.favouriteIv.isSelected)
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