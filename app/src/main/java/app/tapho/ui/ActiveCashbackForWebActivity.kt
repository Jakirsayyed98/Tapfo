package app.tapho.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.GeolocationPermissions
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import app.tapho.ContactOnWhatsapp
import app.tapho.HtmlToText
import app.tapho.R
import app.tapho.databinding.ActivityActiveCashbackForWebBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.ui.help.model.CustomeServicelist
import app.tapho.ui.home.WebviewLoadingDialog
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog


class ActiveCashbackForWebActivity : BaseActivity<ActivityActiveCashbackForWebBinding>() {

    private var loadingDialog: WebviewLoadingDialog? = null
    private var tcash: WebTCashRes? = null
    private var res: WebTCashRes? = null
    var storeDeals: StoreDeals? = null
    var webViewUrl: String? = ""
    var miniAppId: String? = ""
    var iconUrl: String? = ""
    var tCashType: String? = ""
    var favourite: String? = ""
    var cashback: String? = ""
    var textCashback: String? = null
    var UrlType: String? = null
    var checkfav: String? = ""
    var name: String? = ""


    val chrome = "com.android.chrome"

    @JvmField
    var mGeolocationOrigin: String? = null

    @JvmField
    var mGeolocationCallback: GeolocationPermissions.Callback? = null


    companion object {

        var banner = ""
        fun openwebView(
            context: Context?,
            webViewUrl: String?,
            UrlType: String?,
        ) {
            context?.startActivity(Intent(context, ActiveCashbackForWebActivity::class.java).apply {
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra("UrlType", UrlType)
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
            UrlType: String?,
            name: String?,
        ) {
            context?.startActivity(Intent(context, ActiveCashbackForWebActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra(MINI_APP_ID, miniAppId)
                putExtra(ICON_URL, iconUrl)
                putExtra(TCASH_TYPE, tCashType)
                putExtra(IS_FAVOURITE, favourite)
                putExtra(CASHBACK, cashback)
                putExtra(APPCATEGORYID, appcategoryid)
                putExtra("UrlType", UrlType)
                putExtra("Name", name)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityActiveCashbackForWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.black)
        statusBarTextblack()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.mainLayout.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE

        webViewUrl = intent.getStringExtra(WEB_VIEW_URL)
        miniAppId = intent.getStringExtra(MINI_APP_ID)
        iconUrl = intent.getStringExtra(ICON_URL)
        tCashType = intent.getStringExtra(TCASH_TYPE)
        favourite = intent.getStringExtra(IS_FAVOURITE)
        cashback = intent.getStringExtra(CASHBACK)
        UrlType = intent.getStringExtra("UrlType")
        name = intent.getStringExtra("Name")


        if (miniAppId.isNullOrEmpty() /*|| tCashType != "1"*/) {
            activate()
        }else if(tCashType != "1"){
            activate()
        } else {
            getSearchMiniApp()
        }

        binding.backbtn.setOnClickListener {
            finish()
        }

    }



    private fun setAlldata() {
        res!!.data.get(0).let {
            binding.apply{
                Glide.with(applicationContext).load(it.logo).into(logo)
                Glide.with(applicationContext).load(it.image).circleCrop().into(productIv)
                Glide.with(applicationContext).load(it.image).circleCrop().into(merchantIcon)
                val cashbackTV = decodeCashback(it.merchant_payout!!.cashback)
                textCashback = cashbackTV
                cashback.text = cashbackTV!!.split(" ")[1]
                cashback1.text = "to activate "+cashbackTV
                description.text = it.description
                merchantName.text = it.name
                tandc.text = getString(R.string.by_proceeding_to_freshtohome,it.name)
                nameTv2.text ="Open "+ it.name
                report.text = it.merchant_payout!!.report

                if (it.merchant_payout!!.terms.isNullOrEmpty().not()){
                    aboutCashback.visibility = View.VISIBLE
                    aboutCashbackT.visibility = View.VISIBLE
                    aboutCashback.text =this@ActiveCashbackForWebActivity.HtmlToText(it.merchant_payout!!.terms)
                }else{
                    aboutCashback.visibility = View.GONE
                    aboutCashbackT.visibility = View.GONE
                }
                T1.text = getString(R.string.takes_24_48_hrs_to_track_the_transaction_status,it.merchant_payout!!.report, it.name)
                T2.text = getString(R.string.to_avail_this_cashback_open_swiggy_via_tapfo_app, it.name)
                T3.text = getString(R.string.cashback_offer_is_active_for_a_limited_time, it.name)
                T4.text = getString(R.string.the_cashback_offer_will_be_calculated, it.name)
                T5.text = getString(R.string.all_purchases_made_by_the_customer, it.name)


                term1.text = getString(R.string.makemytrip_takes_24_hrs_to_validate_the_order_status, name, it.report)
                term2.text = getString(R.string.makemytrip_holds_cashback_up_to_45_to_60_days, it.name)
                term3.text = getString(R.string.makemytrip_cashback_is_applicable_only_on, it.name)
                term7.text = getString(R.string.tapfo_s_reserves_the_rights, it.name)
                term5.text = getString(R.string.all_user_agreement_and_privacy, it.name)
                term6.text = getString(R.string.if_the_user_cancels_returns_refunds, it.name)
                term8.text = getString(R.string.for_any_queries_related_to_the_cashback, it.name)


                continueLi.setOnClickListener {
                    activate()
                }

                binding.howitwork.setOnClickListener {
                    ContainerActivity.openContainer(this@ActiveCashbackForWebActivity, "CashbackHowItsWorksFragment", "", false, "")
                }


                binding.favouriteIv.setOnClickListener {
                    checkFavOrNot()
                }


            }

        }
    }

    private fun checkFavOrNot() {
        if (miniAppId.isNullOrEmpty().not())
            viewModel.getMiniAppTCash(AppSharedPreference.getInstance(this).getUserId(),
                miniAppId, this, object : ApiListener<WebTCashRes, Any?> {
                    override fun onSuccess(t: WebTCashRes?, mess: String?) {
                        t?.let {
                            checkfav = it.mini_app?.get(0)?.is_favourite
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

        Glide.with(this).load(R.drawable.favicon2).circleCrop().placeholder(R.drawable.loding_app_icon).into(iv_partner)

        if (checkfav == "1") {
            favourite.visibility = View.GONE
            unfavourite.visibility = View.VISIBLE
        } else if (checkfav == "0") {
            favourite.visibility = View.VISIBLE
            unfavourite.visibility = View.GONE
        }

        favourite.setOnClickListener {
            makeFav()
            favourite.visibility = View.GONE
            unfavourite.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)
        }

        unfavourite.setOnClickListener {
            makeUnFav()
            favourite.visibility = View.VISIBLE
            unfavourite.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 2000)

        }

        dialog.setContentView(view)
        dialog.show()
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

    private fun getSearchMiniApp() {
        viewModel.searchMiniApp(getUserId(),miniAppId,this,object : ApiListener<WebTCashRes,Any?>{
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                t.let {
                    res = it
                    setAlldata()
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    getTcashData(it!!.data.get(0).name!!)

                }
            }
        })
    }

    private fun getTcashData(name: String) {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),"2", this, object : ApiListener<TCashDasboardRes, Any?> {
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    Tcashdata(t!!, name)
                    binding.alltransaction.setOnClickListener { click ->
                        ContainerActivity.openContainer(this@ActiveCashbackForWebActivity,"CashbackOrderFragment",t,false,miniAppId,"")
                    }

                }
            })
    }

    private fun Tcashdata(data: TCashDasboardRes, name: String) {
        val tcashdatalist: ArrayList<TCashDashboardData> = ArrayList()
        val tcashlist: ArrayList<TCashDashboardData> = ArrayList()
        var verified = 0.0
        var pending = 0.0
        data.data!!.forEachIndexed { index, d ->
            if (d.offer_name == name) {
                tcashlist.add(d)
            }
        }
        data.data?.forEach { tcashdata ->
            if (tcashdata.offer_name == name) {
                tcashdatalist.add(tcashdata)
            }
        }

        tcashdatalist.forEach {
            if (it.status!!.uppercase().equals("PENDING")) {
                pending = pending + it.user_commision!!.toDouble()
            } else if (it.status!!.uppercase().equals("VERIFIED")) {
                verified = verified + it.user_commision!!.toDouble()
            } else if (it.status!!.uppercase().equals("VALIDATED")) {
                verified = verified + it.user_commision!!.toDouble()
            }
        }

        binding.verifiedAmount.text = withSuffixAmount(verified.toString())
        binding.verifiedAmt.text = withSuffixAmount(verified.toString())
        binding.pendingcashback.text =
            withSuffixAmount(pending.toString())!!.replaceAfter(".", "").replace(".", "")


    }

    private fun activate() {
        addToRecent(miniAppId.toString())
        if (UrlType.equals("1")) {
            if (tCashType.equals("1")) {
                WebViewActivity.openWebView(this, webViewUrl, miniAppId, iconUrl, tCashType, favourite, textCashback,res)
                finish()
            } else {
                WebViewActivity.openWebView(this, webViewUrl, miniAppId, iconUrl, tCashType, favourite, textCashback,res)
                finish()
            }
        } else {
            loadingDialog = WebviewLoadingDialog.showDialog(supportFragmentManager, iconUrl, textCashback)
            kotlin.runCatching {
                Handler(Looper.getMainLooper()).postDelayed({
                    kotlin.runCatching {
                        setOnCustomeCrome(webViewUrl!!)
                        Handler(Looper.getMainLooper()).postDelayed({
                            kotlin.runCatching {
                                loadingDialog!!.dismiss()
                                finish()
                            }
                        }, 500)
                    }
                }, 2000)
            }
        }
    }

    private fun addToRecent(id:String) {
        viewModel.addMiniAppToRecent(getUserId(),id,this,object : ApiListener<BaseRes,Any?>{
            override fun onSuccess(t: BaseRes?, mess: String?) {

            }
        })
    }


    private fun setOnCustomeCrome(url: String) {
        if (isAppInstalled("com.android.chrome")==true && isAppReady("com.android.chrome")){
            val customIntent = CustomTabsIntent.Builder()
            customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.white))
            customIntent.setDefaultShareMenuItemEnabled(false)
            customIntent.build()
            openCustomTab(this, customIntent.build(), Uri.parse(url))
        }else{
            WebViewActivity.openWebView(this, webViewUrl, miniAppId, iconUrl, tCashType, favourite, textCashback,res)
            finish()

        }
    }

    fun isAppInstalled(packageName: String): Boolean {
        val pm = this.getPackageManager()
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()

        }
        return false;
    }

    fun isAppReady(packageName: String): Boolean {
        var appUpiReady = false
        val upiIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webViewUrl))
        val pm = this.getPackageManager()
        val upiActivities: List<ResolveInfo> = pm.queryIntentActivities(upiIntent, 0)
        for (a in upiActivities){
            if (a.activityInfo.packageName == packageName) appUpiReady = true
        }
        return appUpiReady
    }

    private fun openCustomTab(context: Context, customTabsIntent: CustomTabsIntent, Url: Uri) {
        val packageName = "com.android.chrome"
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.launchUrl(context, Url)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.howitwork -> {

                ContainerActivity.openContainer(this, "HowOnlineCashbackWorksFragment", "", false, "")
            }
            R.id.help -> {
                val data  =CustomeServicelist("4",R.drawable.ic_call, "Request Call Back", "Our team will get back to you as soon as possible.","Request Call Back","3")
                ContainerActivity.openContainerForSupport(this,"NeedSupportFragment",data)
            }
            R.id.feedback -> {
                val data  =CustomeServicelist("3",R.drawable.share, "Share Feedback", "Please share your valuable feedback with us","Share Feedback","2")
                ContainerActivity.openContainerForSupport(this,"NeedSupportFragment",data)
            }
            R.id.faq -> {
                this.ContactOnWhatsapp("8369197342")

            }
        }
        return super.onOptionsItemSelected(item)
    }



}
