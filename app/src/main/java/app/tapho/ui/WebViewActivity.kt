package app.tapho.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.transition.TransitionManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.ActivityWebViewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.ui.home.WebviewLoadingDialog
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_news_web_view.*
import java.net.URISyntaxException


class WebViewActivity : BaseActivity<ActivityWebViewBinding>(){

    private var loadingDialog: WebviewLoadingDialog? = null
    var tcash=""
    private var tcash2: TCashDasboardRes? = null
    var checkfav: String? =""
    private var fullData: Boolean = false
    private var onceDialogVisible: Boolean = false
    private val TAG = "WEB_VIEW_ERROR"
    private var res: WebTCashRes? = null
    var CurrentUrl: String? = ""
    var MerchantName: String? = ""
    var cashbackOnExit: String? = ""



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

    companion object {

        fun openWebView(
            context: Context?,
            webViewUrl: String?,
            miniAppId: String?,
            iconUrl: String?,
            tCashType: String?,
            favourite: String?,
            cashback: String?,
            data: Any?,
        ) {
            context?.startActivity(Intent(context, WebViewActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra(MINI_APP_ID, miniAppId)
                putExtra(ICON_URL, iconUrl)
                putExtra(TCASH_TYPE, tCashType)
                putExtra(IS_FAVOURITE, favourite)
                putExtra(CASHBACK, cashback)

                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.black)

        webViewUrl=intent.getStringExtra(WEB_VIEW_URL)
        miniAppId =intent.getStringExtra(MINI_APP_ID)
        iconUrl=intent.getStringExtra(ICON_URL)
        tCashType=intent.getStringExtra(TCASH_TYPE)
        favourite=intent.getStringExtra(IS_FAVOURITE)
        cashback=intent.getStringExtra(CASHBACK)
        binding.favouriteIv.isSelected = favourite == "1"

        intent.getStringExtra(DATA).let {
            if (it.isNullOrEmpty().not()){
                Gson().fromJson(it,WebTCashRes::class.java).let {
                    res = it
                    setTermsData(it)
                }
            }else{
                viewModel.searchMiniApp(getUserId(),miniAppId,this,object :ApiListener<WebTCashRes,Any?>{
                    override fun onSuccess(t: WebTCashRes?, mess: String?) {
                        t!!.let {
                            res = it
                            setTermsData(it)
                        }
                    }

                })
            }
        }

        openPageActivate()

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
        binding.favouriteIv.setOnClickListener {
            checkFavOrNot(binding.favouriteIv)
        }
        binding.backIv.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openPageActivate() {
        loadingDialog = WebviewLoadingDialog.showDialog(supportFragmentManager, iconUrl, cashback)
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.toolbar.visibility = View.VISIBLE
        }, 500)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)
        binding.webView.webChromeClient = GeoWebChromeClient(this)
        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (onceDialogVisible.not() /*&& binding.tcashbackLi.visibility == View.VISIBLE*/){

                }

            }

            override fun onPageCommitVisible(view: WebView?, url1: String?) {
                super.onPageCommitVisible(view, url1)
                dismissLoadingDialog()
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?, ) {
                if (errorCode == -2) {
                    InternetErrorFragment.newInstance().show(supportFragmentManager, "")
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


        webViewUrl?.let {
            binding.cashbacklay.visibility = if (tCashType.equals("1")) View.VISIBLE else View.GONE
            binding.cashback1234.text = if(cashback.isNullOrEmpty().not()) cashback!!.replace("Upto ","Upto\n") else ""
            binding.cashbackinTerms.text = if (cashback.isNullOrEmpty().not()) cashback else ""
            cashbackOnExit =if (cashback.isNullOrEmpty().not()) cashback else ""
           binding.webView.loadUrl(it)
        }

    }


    private fun checkFavOrNot(favouriteIv: ImageView) {
        if (miniAppId.isNullOrEmpty().not())
            viewModel.searchMiniApp(getUserId(), miniAppId, this, object : ApiListener<WebTCashRes, Any?> {
                    override fun onSuccess(t: WebTCashRes?, mess: String?) {
                        t?.let {
                            checkfav =it.data.get(0).is_favourite.toString()
                            favourite = it.data.get(0).is_favourite.toString()
                            makeorUnMakeFav(checkfav,favouriteIv)
                        }
                    }
                })
    }

    private fun makeorUnMakeFav(checkfav: String?,favouriteIv: ImageView) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.miniapp_fav_bottomsheet, null)

        dialog.setCancelable(true)

        val favourite: TextView = view.findViewById(R.id.favourite)
        val unfavourite: TextView = view.findViewById(R.id.unfavourite)
        val iv_partner: ImageView = view.findViewById(R.id.iv_partner)

        Glide.with(this).load(R.drawable.favicon2).circleCrop().placeholder(R.drawable.loding_app_icon).into(iv_partner)

        if (checkfav == "1"){
            favourite.visibility =View.GONE
            unfavourite.visibility =View.VISIBLE
        }else if (checkfav == "0"){
            favourite.visibility =View.VISIBLE
            unfavourite.visibility =View.GONE
        }

        favourite.setOnClickListener {
            makeFav(favouriteIv)
            favourite.visibility =View.GONE
            unfavourite.visibility =View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)

        }
        unfavourite.setOnClickListener {
            makeUnFav(favouriteIv)
            favourite.visibility =View.VISIBLE
            unfavourite.visibility =View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)

        }



        dialog.setContentView(view)
        dialog.show()
    }

    private fun makeFav(favouriteIv: ImageView) {
        viewModel.miniAppFev(AppSharedPreference.getInstance(this).getUserId(), miniAppId, this,
            object : ApiListener<BaseRes, Any?> {
                override fun onSuccess(t: BaseRes?, mess: String?) {
                    t?.let {
                        if (it.errorCode == "0") {
                            favourite = "1"
                            favouriteIv.isSelected = true
                            customToast(getString(R.string.added_in_fev), false)
                        } else toast(mess)
                    }
                }
            })
    }

    private fun makeUnFav(favouriteIv: ImageView) {
        viewModel.miniAppUnFev(AppSharedPreference.getInstance(this).getUserId(),
            miniAppId,
            this,
            object : ApiListener<BaseRes, Any?> {
                override fun onSuccess(t: BaseRes?, mess: String?) {
                    t?.let {
                        if (it.errorCode == "0") {
                            favouriteIv.isSelected = false
                            favourite = "0"
                            customToast(getString(R.string.removed_from_fav), true)
                        } else toast(mess)
                    }
                }
            })
    }

    private fun setTermsData(it: WebTCashRes) {
        Log.d("@@@@@",it.data.get(0).name.toString())

        MerchantName = it.data.get(0).name
        binding.terms1.text = getString(R.string.terms_condition_popup, MerchantName)
        binding.terms2.text = getString(R.string.cashback_credit_as_pending_,MerchantName)
        binding.terms6.text = getString(R.string.decline_cashback, MerchantName)
        binding.secureLine1.text = getString(R.string.this_browsing_is_,MerchantName)
        binding.secureLine2.text = getString(R.string.this_browsing, MerchantName)
        binding.creditHours.text = it.data.get(0).merchant_payout!!.report
//        binding.favouriteIv.isSelected = it.data.get(0).is_favourite == "1"
        MerchantName = MerchantName.toString()
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

    @SuppressLint("MissingInflatedId")
    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.exitbutton, null)

        dialog.setCancelable(true)
        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)
        val merchant_icon_lay: CardView = view.findViewById(R.id.merchant_icon_lay)
        val icon: ImageView = view.findViewById(R.id.merchant_icon)
        val favouriteIv2: ImageView = view.findViewById(R.id.favouriteIv2)
        val name: TextView = view.findViewById(R.id.name)
        val exit_tag_text: TextView = view.findViewById(R.id.exit_tag_text)
        val exitlayout: LinearLayout = view.findViewById(R.id.exit)
        val cashback_on_Exit: TextView = view.findViewById(R.id.cashback_on_Exit)


        merchant_icon_lay.visibility = if (iconUrl.isNullOrEmpty().not()) View.VISIBLE else View.GONE
        favouriteIv2.setOnClickListener {
            checkFavOrNot(favouriteIv2)
        }
        favouriteIv2.isSelected = favourite == "1"
        Glide.with(this).load(iconUrl).circleCrop().into(icon)



        if (tCashType.equals("1")){
            exitlayout.visibility = View.VISIBLE
            name.text = res!!.data.get(0).name
            if (cashbackOnExit.toString().contains("null")) {
                cashback_on_Exit.visibility = View.GONE
                exit_tag_text.visibility = View.GONE
                exit.text = "Exit"
            } else {
                cashback_on_Exit.text = cashbackOnExit
            }
            exit_tag_text.text = getString(R.string.you_might_miss_out, MerchantName)
        }else{
            exitlayout.visibility = View.GONE
        }






        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)
        exit.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra(MINI_APP_ID, miniAppId)
                putExtra(IS_FAVOURITE, binding.favouriteIv.isSelected)
            })
            dialog.dismiss()
            finish()
            super.getOnBackPressedDispatcher().onBackPressed()
        }


        app_continue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()


    }

}