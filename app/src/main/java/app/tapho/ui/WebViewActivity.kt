package app.tapho.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.transition.TransitionManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.AppCompatButton
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.ActivityWebViewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.ui.activecashback.ActiveCashbackDialogFragment
import app.tapho.ui.home.WebviewLoadingDialog
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.URISyntaxException


class WebViewActivity : BaseActivity<ActivityWebViewBinding>(), ApiListener<WebTCashRes, Any?> {
    private var loadingDialog: WebviewLoadingDialog? = null
    private var tCash: WebTCashRes? = null
    private var tcash2: TCashDasboardRes? = null
    var checkfav: String? =""
    private var fullData: Boolean = false
    private var onceDialogVisible: Boolean = false
    private val TAG = "WEB_VIEW_ERROR"
    private var res: WebTCashRes? = null
    var CurrentUrl: String? = ""
    var MerchantName: String? = ""
    var cashbackOnExit: String? = ""

    var storeDeals: StoreDeals? = null

    var webViewUrl: String?=""
    var miniAppId: String?=""
    var iconUrl: String?=""
    var tCashType: String?=""
    var favourite: String?=""
    var cashback: String?=""

    // we will use these when user responds
    @JvmField
    var mGeolocationOrigin: String? = null

    @JvmField
    var mGeolocationCallback: GeolocationPermissions.Callback? = null


    private val IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002


    companion object {

        var banner=""
        fun openwebView(
            context: Context?,
            webViewUrl: String?,
        ) {
            context?.startActivity(Intent(context, WebViewActivity::class.java).apply {

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
            context?.startActivity(Intent(context, WebViewActivity::class.java).apply {
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


        fun openWebView2(
            launcher: ActivityResultLauncher<Intent>?,
            context: Context?,
            webViewUrl: String?,
            miniAppId: String?,
            iconUrl: String?,
            tCashType: String?,
            favourite: String?,
            cashback: String?,
        ) {
            launcher?.launch(Intent(context, WebViewActivity::class.java).apply {
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



        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= 21) {

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
         //   window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.BLACK
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.getStringExtra(WEB_VIEW_URL).isNullOrEmpty().not()){
            webViewUrl=intent.getStringExtra(WEB_VIEW_URL)
            miniAppId =intent.getStringExtra(MINI_APP_ID)
            iconUrl=intent.getStringExtra(ICON_URL)
            tCashType=intent.getStringExtra(TCASH_TYPE)
            favourite=intent.getStringExtra(IS_FAVOURITE)
            cashback=intent.getStringExtra(CASHBACK)
        }else if (intent.getStringExtra(WEB_VIEW_URL).equals("")){
            viewModel.getMiniAppTCash(getUserId(),intent.getStringExtra(MINI_APP_ID),this,object:ApiListener<WebTCashRes,Any?>{
                override fun onSuccess(t: WebTCashRes?, mess: String?) {
                    t!!.let {
                        Log.d("DataMiniID","${it.mini_app!!.get(0).name}")
                        it!!.mini_app!!.get(0).let {
                            webViewUrl=it.url
                            miniAppId =it.id
                            iconUrl=it.image
                            tCashType=it.tcash
                            favourite=it.is_favourite
                            cashback=it.cashback
                        }
                    }
                }
            })


        }

//        getTcashDataForEarning()
        getTCashData1()

    //    initWebView()
        if (miniAppId.isNullOrEmpty() || tCashType != "1")
            initWebView()
        else {
            binding.toolbar.visibility = View.INVISIBLE
        }

        setVisibility()
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

 // 29 July /2022

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
//            intent.data = Uri.parse("package:$packageName")
//            startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST)
//        } else {
//
//        }

        // 29 July /2022 end

    }

    private fun getTcashDataForEarning() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),this,object :ApiListener<TCashDasboardRes,Any?>{
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    tcash2 = t
                    openTcashbackFullDialog(true)
                }

            })
    }
//    Log.d("Tcashdata","${tcash2!!.data!!.get(0).offer_name }}")

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
//        intent.getStringExtra(MiniApp).let {
//                binding.terms1.text=tCash?.data?.get(0)?.name.toString()
//        }
        binding.backIv.setOnClickListener {

            onBackPressed()
        }
        // binding.favouriteIv.visibility = if (intent.getStringExtra(MINI_APP_ID).isNullOrEmpty()) View.GONE else View.VISIBLE
        binding.favouriteIv.isSelected = favourite == "1"
        binding.favouriteIv.setOnClickListener {
            //makeorUnMakeFav()
            checkFavOrNot()
//            if (it.isSelected) makeUnFav() else makeFav()
        }
    }

    private fun checkFavOrNot() {
        if (miniAppId.isNullOrEmpty().not())
            viewModel.getMiniAppTCash(AppSharedPreference.getInstance(this).getUserId(),
                miniAppId, this, object : ApiListener<WebTCashRes, Any?> {
                    override fun onSuccess(t: WebTCashRes?, mess: String?) {
                        t?.let {
                            checkfav =it.mini_app?.get(0)?.is_favourite
                            makeorUnMakeFav(checkfav)
                            }
                        }
                })
    }

    private fun makeorUnMakeFav(checkfav: String?) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.miniapp_fav_bottomsheet, null)

        dialog.setCancelable(true)

        val favourite: TextView = view.findViewById(R.id.favourite)
        val unfavourite: TextView = view.findViewById(R.id.unfavourite)
        val iv_partner: ImageView = view.findViewById(R.id.iv_partner)

        Glide.with(this).load(R.drawable.favicon2).circleCrop().into(iv_partner)

        if (checkfav == "1"){
            favourite.visibility =View.GONE
            unfavourite.visibility =View.VISIBLE
        }else if (checkfav == "0"){
            favourite.visibility =View.VISIBLE
            unfavourite.visibility =View.GONE
        }

        favourite.setOnClickListener {
            makeFav()
            favourite.visibility =View.GONE
            unfavourite.visibility =View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)

        }
        unfavourite.setOnClickListener {
            makeUnFav()
            favourite.visibility =View.VISIBLE
            unfavourite.visibility =View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)

        }



        dialog.setContentView(view)
        dialog.show()
    }

    fun initWebView() {
        //this one
        loadingDialog = WebviewLoadingDialog.showDialog(supportFragmentManager, iconUrl, cashback)

//this one
        Handler(Looper.getMainLooper()).postDelayed({
            binding.toolbar.visibility = View.VISIBLE
            //  binding.line.visibility = View.VISIBLE
        }, 500)

        //impliment google and facebook authantication start
     //   binding.webView.settings.userAgentString="Chrome/56.0.0 Mobile"
        //impliment google and facebook authantication end
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.allowFileAccess = true
//        binding.webView.settings.builtInZoomControls = true

        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
        binding.webView.webChromeClient = GeoWebChromeClient(this)

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (onceDialogVisible.not() /*&& binding.tcashbackLi.visibility == View.VISIBLE*/)

                    getTCashDataAfterSomeTime()
//                appLog("WEBVIEW onPageFinished")
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
                Log.d(TAG, "Error Description: $description")
                Log.d(TAG, "failingUrl: $failingUrl")
                Log.d(TAG, "errorCode: $errorCode")
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
/*
//            private val loadedUrls: HashMap<String?, Boolean> = HashMap()
            /* override fun shouldInterceptRequest(
                 view: WebView?,
                 request: WebResourceRequest?,
             ): WebResourceResponse? {
                 val url = request?.url.toString()
                 var ad = false
                 if (!loadedUrls.containsKey(url)) {
                     ad = AdBlocker.isAd(url)
                     loadedUrls.put(url, ad)
                 } else {
 //                    ad = mLoadedUrls.get(url)
                 }
                 return if (ad) AdBlocker.createEmptyResource() else super.shouldInterceptRequest(
                     view,
                     request)
             }*/
            */

        }


        webViewUrl?.let {
            val data = tCash?.data?.get(0)?.cashback.toString()
            val cutdata = data.removeRange(0, 4)

            if (data.toString().equals("null")) {
                //   binding.cashback1234.visibility = View.GONE
                binding.cashback1234.text = cashback
                binding.cashbackinTerms.text = cashback
                cashbackOnExit =cashback
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

    private fun openTcashbackDialog() {

        //Jakir 16/02/2022 WebView Cashback Dialog

//        tCash?.let {
//            TCashbackDialogFragment.newInstance(it, false)
//                .show(supportFragmentManager, "tcashbackdialog")
//        }
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
           miniAppId,
            loaderListener,
            this
        )
    }

    private fun getTCashData1() {
        if (miniAppId.isNullOrEmpty().not())
            viewModel.getMiniAppTCash(AppSharedPreference.getInstance(this).getUserId(),
                miniAppId,
                this,
                object : ApiListener<WebTCashRes, Any?> {
                    override fun onSuccess(t: WebTCashRes?, mess: String?) {
                        t?.let {

                            setTermsData(it)
                            if (it.errorCode == "0") {
                                tCash = it

                                binding.favouriteIv.isSelected = it.mini_app?.get(0)?.is_favourite == "1"
                                if (miniAppId.isNullOrEmpty().not() && tCashType == "1"){
                                    getTcashDataForEarning()
//                                    openTcashbackFullDialog(true)
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

    private fun openTcashbackFullDialog(showBack: Boolean) {
//        TCashbackDialogFullFragment.newInstance(tCash, showBack)
//            .show(supportFragmentManager, "")
        //Jakir 16/02/2022

//        //11th April Active Cashback Screencomment

         ActiveCashbackDialogFragment.newInstance(tCash, tcash2,true).show(supportFragmentManager, "")
//         ActiveCashbackdialogwithTabFragment.newInstance().show(supportFragmentManager, "")





    }

    override fun onSuccess(t: WebTCashRes?, mess: String?) {
        onceDialogVisible = true
        t?.let {

            if (it.errorCode == "0") {
                // tCash = it
                kotlin.runCatching {
                    openTcashbackDialog()
                    binding.favouriteIv.isSelected = it.mini_app?.get(0)?.is_favourite == "1"
                }
            }
        }
    }

    private fun makeFav() {
        viewModel.miniAppFev(AppSharedPreference.getInstance(this).getUserId(),
            miniAppId,
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
            miniAppId,
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

        dialog.setCancelable(true)
        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)

        exit.text = getString(R.string.leave_on, MerchantName)


        val exit_tag_text: TextView = view.findViewById(R.id.exit_tag_text)
        exit_tag_text.text = getString(R.string.you_might_miss_out, MerchantName)

        val cashback_on_Exit: TextView = view.findViewById(R.id.cashback_on_Exit)
        if (cashbackOnExit.toString().contains("null")) {
            cashback_on_Exit.visibility = View.GONE
            exit_tag_text.visibility = View.GONE
            exit.text = "Leave"
        } else {
            cashback_on_Exit.text = cashbackOnExit
        }
        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)
        exit.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra(MINI_APP_ID, miniAppId)
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