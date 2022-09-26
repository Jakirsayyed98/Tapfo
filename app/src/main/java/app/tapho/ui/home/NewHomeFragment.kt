package app.tapho.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.HttpResponseCache
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.*
import app.tapho.databinding.FragmentNewHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.*
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.MiniCash.UI.MiniCashFragmentContainerActivity
import app.tapho.ui.MiniCash.UI.MiniCashSplashActivity
import app.tapho.ui.Stories.Adapter.StoriesAdapter
import app.tapho.ui.Stories.Model.StoriesResFile
import app.tapho.ui.favourite.FavouriteDialogFragment
import app.tapho.ui.games.adapter.GamesFavListAdapter
import app.tapho.ui.games.adapter.GetRecentlyPlayedGameAdapterList
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import app.tapho.ui.home.NewAdapter.couponCategoriesAdapter
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.ui.home.ShopProduct.ShopAdapter.ShopProducatAdapter
import app.tapho.ui.home.adapter.*
import app.tapho.ui.localbizzUI.LocalBizSplashActivity
import app.tapho.ui.login.model.LoginData
import app.tapho.ui.login.referral_Model.referral_code_res
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.*
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.scanner.scanner
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import app.tapho.viewmodels.FavouriteViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.common.collect.Lists
import com.google.gson.Gson
import com.kakyiretechnologies.appreview.reviewApp
import kotlinx.android.synthetic.main.fragment_fo_cash_data.*
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.forEachIndexed


class NewHomeFragment : BaseFragment<FragmentNewHomeBinding>(), ApiListener<HomeRes, Any?>,
    RecyclerClickListener {

    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    //In App Update
    var moreText="Show more"
    private var inappupdate: AppUpdateManager? = null
    private val inAppUpdateRequest_Code = 1010

    private var partnerCashbackAdapter: HomeCashbackPartnerAdapter<NewCashback>? = null
    private var offersForYouAdapter: offerforyou_Adapter<CashbackMerchant>? = null
    private var shopProducatAdapter: ShopProducatAdapter<Data>? = null
    private var topIconAdapter: TopIconAdapter<NewCashback>? = null
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var bannerAdapter1 = BannerAdapter()
    private val mBannerAdapter = BannerOfferAdapter()
    private val bannerAdapter4 = BannerAdapter()
    private val bannerAdapter5 = BannerAdapter()
    private var cashbackMerchantFragmentAdapter: PagerFragmentAdapter? = null
    private var appCategoryList: ArrayList<AppCategory>? = null
    private var headlinesList: ArrayList<AppCategory>? = null
    private var popularList: ArrayList<Popular>? = null

    private var service: ArrayList<Service>? = null
    private var favViewModel: FavouriteViewModel? = null
    private var inTheSpotLight: InTheSpotLightAdapter<BannerList>? = null
    private var VerticalBannerAdapter: VerticalBannerAdapter<BannerList>? = null
    private var lastTransaction: LasttransactionData<TCashDashboardData>? = null
    private var newPartnerAdapter: NewPartnerAdapter<CashbackMerchant>? = null//
    private var exculsivecashbackAdapter: ExculsivecashbackAdapter<BannerList>? = null//
    private var AdsAdapter: AdsAdapter<BannerList>? = null//
    private var Banner6Adapter: AdsAdapter<BannerList>? = null//
    private var Banner2Adapter: Banner2Adapter<BannerList>? = null//
    private var sponsored: SponsoredAdapter<PromotedApp>? = null//
    private var ShopAllProduct: ShopAllProductAdapter<Data>? = null
    private var StoriesAdapter: StoriesAdapter<app.tapho.ui.Stories.Model.Data>? = null
    private var couponCategoriesAdapter: couponCategoriesAdapter<AppCategory>? = null
    private var CategoriesAdapter: couponCategoriesAdapter<AppCategory>? = null
    private var mAdapter: HomePageOfferListAdapter? = null
    private var HomeMerchantAdapter: HomeMerchantAdapter? = null
    private var HomeMerchantAdapter2: HomeMerchantAdapter? = null

    var customShopCategory : SuperLinkAdapter?=null
    var idCreatedAt = ""
    var appcategoryID = ""
    var backPressedTime: Long = 0

    private var GameFavList: GamesFavListAdapter? = null
    private var RecentPlayed: GetRecentlyPlayedGameAdapterList? = null
    var Storiesdata: ArrayList<app.tapho.ui.Stories.Model.Data> = ArrayList()

    var StoriesCategoryID = 0

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
        _binding1 = FragmentNewHomeBinding.inflate(inflater, container, false)




        // Inflate the layout for this fragment
        statusBarColor(R.color.orange1)
        statusBarTextBlack()
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        callVideoModelClass()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
      //  getCurrentLocationData()
        //In App Review Code
        reviewApp(5)

        createShortCuts()


        //Update Dialog Code
        inappupdate = AppUpdateManagerFactory.create(requireContext())
        InAppUpdate()
        val date = parseDate4(Date().toString())
        if (parseDate4(getSharedPreference().getLoginData()!!.created_at.toString()) == date.toString()) {
            _binding1!!.mainLayout.visibility = View.VISIBLE
        }else{
            _binding1!!.welcome.visibility = View.VISIBLE
            _binding1!!.mainLayout.visibility = View.GONE
            Handler().postDelayed({
                _binding1!!.welcome.visibility = View.GONE
                _binding1!!.mainLayout.visibility = View.VISIBLE
                _binding1!!.welcometext.text = "Welcome back"
            },2000)
        }

        _binding1!!.swipeRefresh.setOnRefreshListener {
            _binding1!!.swipeRefresh.isRefreshing = true
            callVideoModelClass()
            _binding1!!.swipeRefresh.isRefreshing = false
        }


        referralAndEarn()

        return _binding1?.root

    }

    private fun referralAndEarn() {
        _binding1!!.apply {
            invite.setOnClickListener {
                shareApp()
            }
            myrefrrel.text = getRefreelCode()
            codeCopied.setOnClickListener {
                val clipboard =requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label",myrefrrel.text.toString())
                clipboard.setPrimaryClip(clip)

                requireView().showShortSnack("Copied sucessfully")
            }
        }
    }



    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"Hey, I found a great app for you. Download Tapfo, Super app for super people. Earn extra cashback on your shopping from 900+ stores like flipkart, Amazon, Myntra, Swiggy & more. Get ₹30 signup bonus instantly from my Referral Code : *" + getRefreelCode() + "*\n\n\n Download the App now : https://tapfo.onelink.me/k6rU/qmgc2mid")
            type = "text/plain"
        }
        try {
            startActivity(Intent.createChooser(sendIntent, null))
        } catch (e: ActivityNotFoundException) {
            context?.toast("Unable to find market app")
        }
    }


    private fun createShortCuts() {

        val intent : Intent = Intent(requireContext(),scanner::class.java)
        intent.setAction(Intent.ACTION_VIEW)
        val shortcut = ShortcutInfoCompat.Builder(requireContext(), "id1")
            .setShortLabel("Scanner")
            .setLongLabel("Open Scanner")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_scanner_24))
         //   .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mysite.example.com/")))
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(requireContext(), shortcut)
    }

    private fun getCurrentLocationData() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );

            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failed on getting current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(
                        requireContext(), "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun callVideoModelClass() {
        showLoaderFragment()
        superlinks()
        //superlinksSubCategories()
        customShopCategory()
        CustomeFinanceCategories()
        setRecyclerCashbackPartner()
        setOffersForYou()
        getNotificationData()
        //openBottomSheet()
        getShop()
        getShopProduct()
        topicon()
        Banner2()
     //   setRecyclerHeadline()
        getData()
        setMoreClicks()
        exculsiveCashback()
        Banner2()
        getCategories()
        setAppCategoryData()
        setMerchants()
        getCategoryMerchantData("2")
        setoneAppBenefits()
        setStoriesViewModel()
        setStoriesLayout()
        setBannerdata4()
    }

    private fun InAppUpdate() {
        inappupdate!!.appUpdateInfo.addOnSuccessListener { AppUpdateInfo ->

            if (AppUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && AppUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE
                )
            ) {

                inappupdate?.startUpdateFlowForResult(
                    AppUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    requireActivity(),
                    inAppUpdateRequest_Code
                )
            }

        }
    }

    private fun inAppUpdateProgress() {
        inappupdate!!.appUpdateInfo.addOnSuccessListener { AppUpdateInfo ->

            if (AppUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                inappupdate?.startUpdateFlowForResult(
                    AppUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    requireActivity(),
                    inAppUpdateRequest_Code
                )
            }

        }
    }


/*
    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            text_view_notification = intent.extras?.getString("message").toString()
            val bundle = intent.extras
            if (bundle != null) {
                text_view_notification = bundle.getString("text").toString()
                Log.d("text_view_notification","$text_view_notification")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(messageReceiver, IntentFilter("MyData"))

    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(messageReceiver)

    }
    */

    private fun setStoriesViewModel() {
        viewModel.getStoriesData(getUserId(), this, object : ApiListener<StoriesResFile, Any?> {
            override fun onSuccess(t: StoriesResFile?, mess: String?) {
                t.let {
                    StoriesAdapter!!.addAllItem(it!!.data)
                    //    setStoriesView(it.data)
                }
            }
        })
    }


    fun setStoriesLayout() {
        StoriesAdapter = StoriesAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                //setStoriesView(Storiesdata)
                Storiesdata.clear()
                StoriesCategoryID = data.toString().toInt()
                setStoriesByIDViewmodel(StoriesCategoryID)
//                ContainerActivity.openContainer(requireContext(), "TapfoStoriesFragment", data.toString())
            }

        })
        _binding1!!.Stories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = StoriesAdapter
        }
    }

    private fun setStoriesByIDViewmodel(StoriesId: Int) {
        viewModel.getStoriesData(getUserId(), this, object : ApiListener<StoriesResFile, Any?> {
            override fun onSuccess(t: StoriesResFile?, mess: String?) {
                t!!.let {
                    it.data.forEach {
                        if (it.id == StoriesId.toString()) {
                            Storiesdata.add(it)
                        }
                    }
                    setStoriesView(Storiesdata, StoriesId.toInt())
                }
            }
        })
    }

    internal fun setStoriesView(data: List<app.tapho.ui.Stories.Model.Data>, StoriesId: Int) {
        StoriesCategoryID = StoriesId
        val myStories: ArrayList<MyStory> = ArrayList()
        for (story in data) {
            story.story.forEach {
                myStories.add(MyStory(it.image))
            }
        }

        StoryView.Builder(activity?.getSupportFragmentManager())
            .setStoriesList(myStories) // Required
            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
            .setTitleText(data.get(0).name) // Default is Hidden
//            .setSubtitleText(data.get(0).description.toString()) // Default is Hidden
            .setTitleLogoUrl(data.get(0).image) // Default is Hidden
            .setStoryClickListeners(object : StoryClickListeners {
                override fun onDescriptionClickListener(position: Int) {
                    Toast.makeText(
                        requireContext(),
                        "${data.get(0).story.get(position).url}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onTitleIconClickListener(position: Int) {
                    //your action
                }
            }) // Optional Listeners
            .build() // Must be called before calling show method
            .show()

        // setStoriesByIDViewmodel(StoriesCategoryID++)

    }

    private fun rateApp() {
        val uri: Uri =
            Uri.parse("https://play.google.com/store/apps/details?id=" + activity?.packageName.toString() + "")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            context?.toast("Unable to find market app")
        }
    }

    private fun PopUpBar() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.cashback_rule_popup, null)
        dialog.setCancelable(true)
     //  val AllTransaction: TextView = view.findViewById(R.id.viewAllTransaction)
        val AllTransaction: AppCompatButton = view.findViewById(R.id.viewAllTransaction)
        AllTransaction.setOnClickListener {

            ContainerActivity.openContainer(requireContext(), "transactionHistory", "")

//            val newFragment: Fragment = Fragment_Tcash_HistoryData()
//            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//            transaction.replace(R.id.container, newFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    // Main Category
    private fun getCategories() {
        viewModel.getHomeData("home", getUserId(), this, object : ApiListener<HomeRes, Any?> {
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t!!.app_category!!.get(0).isSelected = true
                CategoriesAdapter!!.addAllItem(t?.app_category!!)
            }

        })

    }

    fun setAppCategoryData() {
        CategoriesAdapter = couponCategoriesAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                getCategoryMerchantData(data.toString())
            }

        })
        _binding1!!.Categories.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoriesAdapter
        }
    }

    private fun getCategoryMerchantData(appId: String) {
        viewModel.getAppCategoryMiniApp(getUserId(), appId, this,
            object : ApiListener<MiniAppRes, Any?> {
                override fun onSuccess(t: MiniAppRes?, mess: String?) {
                    var TempList: ArrayList<MiniApp> = ArrayList()
                    t?.mini_app.let {
                        it!!.forEach {
                            if (it.tcash!!.contains("1")) {
                                TempList.add(it)
                            }

                        }
                    }
                    HomeMerchantAdapter!!.clear()
                    HomeMerchantAdapter!!.addAllItem(/*t!!.mini_app!!*/ TempList)
                    HomeMerchantAdapter!!.notifyDataSetChanged()
                }
            })
    }

    fun setMerchants() {
        HomeMerchantAdapter = HomeMerchantAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//                if (type == "MiniAppFragment")
//                    openAllPopularCashbackMerchants("2")
//                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                if (data is MiniApp)
                    openWebView(data)
//                }
            }

        })
        _binding1!!.Merchants.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeMerchantAdapter
        }
    }

//end
    private fun setMoreTextSuperLinks(moreTex: String) {
    var CustomeShopCategory: ArrayList<CustomeShopCategoryModel> = ArrayList()
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.new_miniearn_icon, "Minicash", "Minicash"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.order_food_icon, "order food", "Order Foods"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.gamezon_icon, "900+ Games", "900+ Games"))

    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.new_billpay_icon, "bills&Recharge", "BillsAndRecharge"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.appfo_icon, "All Mini Apps", "All Mini Apps"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.travel_and_tour_icon, "Travel", "Travel"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.book_tickets_icon, "Book tickets", "Book tickets"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.voucher_home_icon, "gift voucher", "Gift Voucher"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.local_deals_new_icons, "Nearbuy deals", "Nearbuy deals"))
//    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.pinbiz_home_icon, "localbiz", "localbiz"))
    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.whats_next_new_icon, "what_next", "what_next"))


//    CustomeShopCategory.add(CustomeShopCategoryModel(R.drawable.electro_mall_icon, "electro", "electro"))




        if (moreTex == "Show more") {
            customShopCategory!!.clear()
            customShopCategory!!.addAllItem(CustomeShopCategory)
            moreText ="Show less"
            _binding1!!.showMoreBtn1.text = moreText
            Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding1!!.arrow)

        }else{
            customShopCategory!!.clear()
            customShopCategory!!.addAllItem(if (CustomeShopCategory.size>=3) CustomeShopCategory.subList(0,6) else CustomeShopCategory)
            moreText ="Show more"
            _binding1!!.showMoreBtn1.text = moreText
            Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding1!!.arrow)
        }
    }

    private fun superlinks() {



        customShopCategory = SuperLinkAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Mini Apps" -> {
                        ContainerActivity.openContainer(requireContext(), "Mini Apps", "")
                    }
                    "Travel" -> {
                        setDataCategoryIDAndName("54", "Tour & Travel")
//                        ContainerActivity.openContainer(requireContext(), "Travel", "")
                    }
                    "All Mini Apps" -> {
                        ContainerActivity.openContainer(requireContext(), "All Mini Apps", "")
                    }
                    "Gift Voucher" -> {
                        getWebUrlData("Gyftr")
                    }
                    "BillsAndRecharge" -> {
                        RechargeServiceActivity.openRechargeService(requireContext())
                    }
                    "Order Foods" -> {
                        setDataCategoryIDAndName("2", "Order Food")
                    }
                    "Compare Flights" -> {
                        getWebUrlData("Skyscanner")
                    }
                    "Book Cabs" -> {
                        ContainerActivity.openContainer(requireContext(), "Cabs", "")
                    }
                    "Nearbuy deals" -> {
                        getWebUrlData("Nearbuy")
                    }
                    "Sports" -> {
                        getWebUrlData("FanCode")
                    }
                    "Book tickets" -> {
                        getWebUrlData("bookmyshow")
                    }
                    "News" -> {
                        ContainerActivity.openContainer(requireContext(), "News", "")
                    }
                    "Deals & Coupons" -> {
                        ContainerActivity.openContainer(requireContext(), "Deals & Coupons", "")
                    }
                    "900+ Games" -> {
                        if (getSharedPreference().getString("gameIntro").isNullOrEmpty()) {
                            ContainerActivity.openContainer(requireContext(), "GamesOneTimeSplash", "")
                        } else {
                            ContainerActivity.openContainer(requireContext(), "Games", "")
                        }
                    }
                    "mx player" -> {
                        getWebUrlData("Mxplayer")
                    }
                    "what_next" -> {
                        ContainerActivity.openContainer(requireContext(), "BuyGiftCard", "")
                    }
                    "AllProductWithCategory" -> {
                        ContainerActivity.openContainer(
                            requireContext(),
                            "AllProductWithCategory",
                            ""
                        )
                    }
                    "compare&Shop" -> {
                        ContainerActivity.openContainer(requireContext(), "compare&Shop", "")
                    }
                    "Minicash" -> {
                        if (getSharedPreference().getString("MiniEarnOnBordingStatus").toString().isNullOrEmpty()) {
                            ContainerActivity.openContainer(requireContext(), "MiniEarnOnBoardingScreen", "")
                        } else {
                            startActivity(Intent(requireContext(), MiniCashSplashActivity::class.java))
                        }
                    }
                    "localbiz" -> {
                        if (getSharedPreference().getString("localbizOnBoarding").isNullOrEmpty()) {
                            ContainerActivity.openContainer(requireContext(), "localbizOnBoarding", "")
                        } else {
                            startActivity(Intent(requireContext(), LocalBizSplashActivity::class.java))
                        }

                  //      startActivity(Intent(requireContext(), LocalBizSplashActivity::class.java))
                    }
                    "electro" -> {
                        if (getSharedPreference().getString("electroIntro").isNullOrEmpty()) {
                            ContainerActivity.openContainer(requireContext(), "ElectroSplashFragment", "")
                        } else {
                            ContainerActivity.openContainer(requireContext(), "ElectroFragment", "")
                        }
                    }



                }
            }

        })/*.apply {
//            addItem(CustomeShopCategoryModel(R.drawable.mini_earn_icon, "Minicash", "Minicash"))
//            addItem(CustomeShopCategoryModel(R.drawable.buy_gift_voucher_icon, "gift voucher", "Gift Voucher"))
//            addItem(CustomeShopCategoryModel(R.drawable.bill_pay_icon, "bills&Recharge", "BillsAndRecharge"))
//            addItem(CustomeShopCategoryModel(R.drawable.electro_mall_icon, "electro", "electro"))
//            addItem(CustomeShopCategoryModel(R.drawable.gamezon_icon, "900+ Games", "900+ Games"))
//            addItem(CustomeShopCategoryModel(R.drawable.book_tickets_icon, "Book tickets", "Book tickets"))
//            addItem(CustomeShopCategoryModel(R.drawable.order_food_icon, "order food", "Order Foods"))
//            addItem(CustomeShopCategoryModel(R.drawable.local_deals_new_icons, "Nearbuy deals", "Nearbuy deals"))
//            addItem(CustomeShopCategoryModel(R.drawable.travel_and_tour_icon, "Travel", "Travel"))
//            addItem(CustomeShopCategoryModel(R.drawable.book_tickets_icon, "Book", "localbiz"))
//            addItem(CustomeShopCategoryModel(R.drawable.whats_next_new_icon, "what_next", "what_next"))


/*
            addItem(CustomeShopCategoryModel(R.drawable.shop_and_earn_icon, "Mini Apps", "Mini Apps"))
            addItem(CustomeShopCategoryModel(R.drawable.tapbazar, "AllProductWithCategory", "AllProductWithCategory"))
                   addItem(
                       CustomeShopCategoryModel(
                           R.drawable.new_foprice_icon,
                           "compare",
                           "compare&Shop"
                       )
                   )







                   addItem(CustomeShopCategoryModel(R.drawable.latest_news_icon_2, "News", "News"))
                   addItem(CustomeShopCategoryModel(R.drawable.sports_new_icon12, "Sports", "Sports"))

                   addItem(CustomeShopCategoryModel(R.drawable.mx_player_icon, "mx player", "mx player"))
                   addItem(CustomeShopCategoryModel(R.drawable.book_cabs_icon, "Book Cabs", "Book Cabs"))
                   addItem(CustomeShopCategoryModel(R.drawable.dealsandcoupons_icon, "Deals & Coupons", "Deals & Coupons"))
                   addItem(CustomeShopCategoryModel(R.drawable.compare_new_icon, "Compare Flights", "Compare Flights"))
                   addItem(CustomeShopCategoryModel(R.drawable.all_miniapps, "All Mini Apps", "All Mini Apps"))
                */
        }
        */


        _binding1!!.SuperLinksRV.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = customShopCategory
        }
    }



    private fun customShopCategory() {
        val customShopCategory = CustomeShopCategoryAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "AllCategories" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "",
                            "",
                            "",
                            "All Product"
                        )
                    }
                    "Mobiles" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "1",
                            "3",
                            "",
                            "Mobiles"
                        )
                    }
                    "Fashion" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "3",
                            "",
                            "",
                            "Fashion"
                        )
                    }
                    "Beauty" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "2",
                            "",
                            "",
                            "Beauty"
                        )
                    }
                    "Laptop" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "1",
                            "4",
                            "",
                            "Laptop"
                        )
                    }
                    "Television" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "1",
                            "6",
                            "",
                            "Television"
                        )
                    }
                    "Audio" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "1",
                            "2",
                            "",
                            "Audio"
                        )
                    }
                    "Refrigerators" -> {
                        Toast.makeText(requireContext(), "Refrigerators", Toast.LENGTH_SHORT).show()
                    }
                    "Washing Machines" -> {
                        Toast.makeText(requireContext(), "Washing Machines", Toast.LENGTH_SHORT)
                            .show()
                    }
                    "Kitchen Appliances " -> {
                        Toast.makeText(requireContext(), "Kitchen Appliances ", Toast.LENGTH_SHORT)
                            .show()
                    }
                    "Wearables" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "1",
                            "1",
                            "",
                            "Wearables"
                        )
                    }
                    "Camera" -> {
                        ContainerActivity.openCustomeCategory(
                            requireContext(),
                            "CustomeShopCategory",
                            "1",
                            "8",
                            "",
                            "Camera"
                        )
                    }
                }
            }

        }).apply {
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Mobiles", "Mobiles"))
            addItem(CustomeShopCategoryModel(R.drawable.fashion_new_icon, "Fashion", "Fashion"))
            addItem(CustomeShopCategoryModel(R.drawable.beauty_new_icon, "Beauty", "Beauty"))
            addItem(CustomeShopCategoryModel(R.drawable.laptop_new_icon2, "Laptop", "Laptop"))
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.mobile_new_icon,
                    "Television",
                    "Television"
                )
            )
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Audio", "Audio"))
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.mobile_new_icon,
                    "Refrigerators",
                    "Refrigerators"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.mobile_new_icon,
                    "Washing\nMachines",
                    "Washing Machines"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.mobile_new_icon,
                    "Kitchen\nAppliances ",
                    "Kitchen Appliances "
                )
            )
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Wearables", "Wearables"))
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Camera", "Camera"))
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.mobile_new_icon,
                    "All Categories",
                    "AllCategories"
                )
            )
        }
        _binding1!!.ShopProductCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = customShopCategory
        }
    }

    private fun CustomeFinanceCategories() {
        val customFinaceCategory = CustomeFinanceCategoryAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Scan QR" -> {
                        startActivity(Intent(requireContext(), scanner::class.java))
                    }
                    "Send Money" -> {
                        UPIContainerActivity.openContainer(
                            context,
                            "Send Money",
                            "Send Money",
                            false,
                            ""
                        )
                    }
                    "RecivedMoney" -> {
                        UPIContainerActivity.openContainer(
                            context,
                            "RecivedMoney",
                            "RecivedMoney",
                            false,
                            "RecivedMoney"
                        )
                    }
                    "recharge" -> {
                        RechargeServiceActivity.openRechargeService(requireContext())
                    }

                }
            }

        }).apply {

            addItem(
                CustomeShopCategoryModel(
                    R.drawable.new_qr_reader_icon,
                    "Scan &\nPay",
                    "Scan QR"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.sendmoney_new_icon,
                    "Send\nMoney",
                    "Send Money"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.recivedmoney_new_icon,
                    "Recived\nMoney",
                    "RecivedMoney"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.balance_check_icon,
                    "Topup\nCard",
                    "Wallet Popup"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.balance_check_icon,
                    "Check\nBalance",
                    "Check Balance"
                )
            )
        }
        _binding1!!.customFinance.apply {

            layoutManager = GridLayoutManager(context, 5)
            //layoutManager = LinearLayoutManager(context)
            adapter = customFinaceCategory
        }
    }

    private fun getWebUrlFromService(s: String) {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(),
            this, object : ApiListener<HomeRes, Any?> {
                override fun onSuccess(t: HomeRes?, mess: String?) {
                    t!!.services.let {
                        it!!.forEach {
                            if (it.id == s) {
                                WebViewActivityForOffer.openWebView(
                                    context,
                                    it.url,
                                    id.toString(), "", "", "", "", ""
                                )
                            }
                        }
                    }
                }

            })
    }

    private fun setDataCategoryIDAndName(id: String, Name: String) {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(),
            this, object : ApiListener<HomeRes, Any?> {
                override fun onSuccess(t: HomeRes?, mess: String?) {
                    t!!.app_category.let {
                        it!!.forEach {
                            if (it.id == id) {
                                MiniCashFragmentContainerActivity.openContainer(context, "MiniCashStorePage", it, false, it.name)
//                                openContainer("MiniAppFragmentNewCategories", it, false, Name)
                            }
                        }
                    }
                }

            })
    }

    fun getNotificationData() {
        viewModel.getAllNotification(
            getUserId(),
            this,
            object : ApiListener<AllNotificationRes, Any?> {
                override fun onSuccess(t: AllNotificationRes?, mess: String?) {
                    var datalist: ArrayList<app.tapho.ui.model.AllNotification.Data> = ArrayList()
                    t.let {
                        it!!.data.forEachIndexed { index, data ->
                            if (data.noti_type.toString() == "1") {
                                datalist.add(data)
                            }
                        }
                        if (datalist.isNullOrEmpty().not()) {
                            _binding1!!.lasttransaction.text = getString(
                                R.string.congratulations_you_recieved_29_cashback_on_swiggy_order,
                                withSuffixAmount(datalist.get(0).merchant_postback_value.get(0).user_commision),
                                datalist.get(0).merchant_postback_value.get(0).offer_name.toString()
                            )
                            _binding1!!.lasttransactiondate.text =
                                parseAgoDate(datalist.get(0).created_at)
                            _binding1!!.lasttransactiondate.visibility = View.VISIBLE
                            Glide.with(requireContext())
                                .load(datalist.get(0).offer_data.get(0).image).circleCrop()
                                .into(_binding1!!.transactionicon)

                        } else {
                            _binding1!!.lasttransaction.text =
                                "you don't have any transactions in last 7 days. shop and earn cashback now"
                            _binding1!!.lasttransactiondate.text = ""
                            _binding1!!.lasttransactiondate.visibility = View.GONE
                            Glide.with(requireContext()).load(R.drawable.notification_icon)
                                .circleCrop().into(_binding1!!.transactionicon)

                        }

                    }
                }

            })
    }

    private fun tcashhistoryDataViewModel1() {
        getSharedPreference().getUserId().let {
            viewModel.getTCashDashboard(getUserId(),
                TimePeriodDialog.getDate(1, -12),
                TimePeriodDialog.getCurrentDate(),
                this,
                object : ApiListener<TCashDasboardRes, Any?> {
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                        _binding1!!.availbalance.text = withSuffixAmount(t!!.cash_available)
                        _binding1!!.pendingbalance.text = withSuffixAmount(t.pending)

                        var TempList: ArrayList<Int> = ArrayList()
                        var transaction = 0
                        t?.data.let {

                            if (it != null) {
                                it.forEach {
                                    TempList.add(it.sale_amount!!.toInt())
                                }
                            }
                        }

                        for (x in TempList) {
                            transaction += x
                        }

                        t?.data.let {
                            lastTransaction?.addAllItem(
                                if (it!!.size > 4) it.subList(0, 4) else it
                            )

                        }
                    }
                })
        }
    }


    private fun topicon() {
        topIconAdapter = TopIconAdapter(this)
    }

    private fun setMoreClicks() {
        setMoreTextSuperLinks("Show less")
        _binding1!!.showMoreBtn.setOnClickListener {

            setMoreTextSuperLinks(moreText)
        }


        _binding1!!.pendingLayout.setOnClickListener {
            PopUpBar()
        }
        _binding1!!.leaveFeedback.setOnClickListener {
            rateApp()
        }

        _binding1!!.notificationIv.setOnClickListener {
//            ContainerActivity.openContainer(context, ContainerType.NOTIFICATION.name, "")
            ContainerActivity.openContainer(context, "AllNotification", "")
        }
        _binding1!!.notificationRe.setOnClickListener {
//            ContainerActivity.openContainer(context, ContainerType.NOTIFICATION.name, "")
            ContainerActivity.openContainer(context, "AllNotification", "")
        }
        _binding1!!.searchTv.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        _binding1!!.searchTool.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        _binding1!!.search.setOnClickListener {
//            ContainerActivity.openContainer(context, "search", "")
            openAllPopularCashbackMerchants("1")
        }
        _binding1!!.cashbackCard.setOnClickListener {
            // Cashback Verifed History And Rewards Page
            if (getSharedPreference().getString("WalletOnBordingStatus").toString().isNullOrEmpty()) {
                ContainerActivity.openContainer(requireContext(), "WalletOnBoardingScreen", "")
            } else {
                ContainerActivity.openContainer(context, "cashbackcard", "")
            }

        }
 _binding1!!.verifiedLayout.setOnClickListener {
     ContainerActivity.openContainer(requireContext(), "AvailableBalance", "")

        }

        _binding1!!.recommendedAll.setOnClickListener {
            if (getSharedPreference().getString("MiniEarnOnBordingStatus").toString().isNullOrEmpty()) {
                ContainerActivity.openContainer(requireContext(), "MiniEarnOnBoardingScreen", "")
            } else {
                startActivity(Intent(requireContext(), MiniCashSplashActivity::class.java))
            }
//            ContainerActivity.openContainer(requireContext(), "Mini Apps", "")
//            ContainerActivity.openContainer(context, "RecommendedItem", "")
        }


        _binding1!!.favouritesBtn.setOnClickListener {
            openContainer("favouritesBtn", "", false, "")
        }

        _binding1!!.reProfile.setOnClickListener {
            ContainerActivity.openContainer(context, ContainerType.PROFILE_EDIT.name, "")
        }
        _binding1!!.shopproductseeall.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "AllProductWithCategory", "")
        }

        _binding1!!.AddMoreFavTV.setOnClickListener {
            FavouriteDialogFragment.newInstance(appCategoryList).show(childFragmentManager, null)
        }
        _binding1!!.scanner.setOnClickListener {
            startActivity(Intent(requireContext(), scanner::class.java))
        }

    }



    private fun getWebUrlData(s: String) {
        viewModel.searchMiniApp(getUserId(), s, this, object : ApiListener<WebTCashRes, Any?> {
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                val url = t?.data?.get(0)?.url
                val id = t?.data?.get(0)?.id
                WebViewActivityForOffer.openWebView(context, url, id, "", "", "", "", "")
            }

        })
    }


    private fun getData() {
        viewModel.getHomeData(
            "home",
            AppSharedPreference.getInstance(requireContext()).getUserId(),
            this,
            this
        )
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {

        t?.let {

            dissmissLoader()
            appcategoryID = it.app_category!!.get(1).id.toString()
            tcashhistoryDataViewModel1()
            it.banner_list1?.let { bannerList1 ->

                if (bannerList1.isNullOrEmpty()) {
                    _binding1!!.banner1.visibility = View.GONE
                } else {
                    exculsivecashbackAdapter?.addAllItem(bannerList1)
                }

            }
            it.banner_list3?.let { bannerList3 ->
                if (bannerList3.isNullOrEmpty()) {
                    _binding1!!.oneBanifitApp.visibility = View.GONE
                } else {
                    inTheSpotLight?.addAllItem(bannerList3)
                }

            }
            it.banner_list2?.let { bannerList2 ->

                if (bannerList2.isNullOrEmpty()) {
                    _binding1!!.banner2.visibility = View.GONE
                } else {
                    Banner2Adapter?.addAllItem(bannerList2)
                }

            }
            it.banner_list4?.let { bannerList4 ->
                VerticalBannerAdapter!!.addAllItem(bannerList4)
//                setBannerdata4(bannerList4)
            }


            it.banner_list5?.let { bannerList5 ->
                AdsAdapter?.addAllItem(bannerList5)
                bannerAdapter5.addAllData(bannerList5)
            }
            it.banner_list6?.let {
                Banner6Adapter?.addAllItem(it)
            }
            it.banner_list7?.let {
                Banner2Adapter?.addAllItem(it)
            }
            it.new_cashback_merchant?.let { newCashBack ->
                partnerCashbackAdapter?.addAllItem(
                    if (newCashBack.size > 12) newCashBack.subList(0, 11) else newCashBack
                )
            }

            it.popular?.let {
                popularList = it

            }
            it.promoted_app?.let {
                sponsored?.addAllItem(it)
            }

            it.new_cashback?.let { newCashBack ->
                //
                topIconAdapter?.addAllItem(
                    if (newCashBack.size > 7) newCashBack.subList(0, 4) else newCashBack
                )
            }
            appCategoryList = it.app_category
            setFavourites(getString(R.string.more))

            it.services?.let {
                recommendedDatagett(it)
            }

            it.popular?.let { popular ->
                setPopularMerchants(popular)

            }

            it.cashback_merchant?.let { cashBackM ->
                if (cashBackM.isNotEmpty()) {
                    val partitionedList: List<List<CashbackMerchant>> =
                        Lists.partition(cashBackM, 12)
                    cashbackMerchantFragmentAdapter?.addFragment(
                        CashbackMerchantsFragment.newInstance(
                            partitionedList[0], true, false
                        ),
                        "Popular Merchants"
                    )
                }
            }

            it.cashback_merchant?.let { newCashBackM ->
                var list: List<CashbackMerchant>
                list = newCashBackM.shuffled()
                if (newCashBackM.isNullOrEmpty().not()) {
                    if (newCashBackM.size > 12) {
                        list.subList(0, 12)
                    } else {
                        offersForYouAdapter?.addAllItem(list.shuffled())
                    }
                }
            }

            it.new_cashback_merchant?.let { newCashBackM ->
                if (newCashBackM.isNotEmpty()) {
                    val partitionedList: List<List<CashbackMerchant>> =
                        Lists.partition(newCashBackM, 12)
                    newPartnerAdapter!!.addAllItem(newCashBackM)
                    cashbackMerchantFragmentAdapter?.addFragment(
                        CashbackMerchantsFragment.newInstance(
                            partitionedList[0],
                            true,
                            showBigView = false
                        ), "New Partners"
                    )
                }
            }

         //   dissmissLoader()
        }

        val listData = t?.new_cashback
        var i = 0
        while (i <= listData!!.size) {
            SetDataForSearchcashback(i, t)
            i++
        }

    }


    fun setoneAppBenefits() {
        inTheSpotLight = InTheSpotLightAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                setOnCustomeCrome(data.toString(), "")
            }

        })
        _binding1!!.oneBanifitApp.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = inTheSpotLight
        }
    }

    /*
        private fun setBannerdata4(bannerList4: ArrayList<BannerList>) {
            if (bannerList4.isNullOrEmpty()) {
                _binding1!!.banner4.visibility = View.GONE
            } else {
                _binding1!!.banner4.visibility = View.VISIBLE
            }

            val imageList = ArrayList<SliderModelMain>()
            bannerlist4 = bannerList4

            for (x in bannerList4) {
                imageList.add(SliderModelMain(x.image, x.url, x.id, x.type, "", ""))
            }
            _binding1!!.banner4.adapter =
                NewBannerDataAdapter(imageList, _binding1!!.banner4, object : RecyclerClickListener {
                    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                        if (data.toString().contains("https://")) {
                            //   WebViewActivity.openWebView(context, data.toString(), "", "", "", "", "", "" )
                            NewWebViewActivity.openWebView(
                                context,
                                data.toString(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                            )

                        } else if (data.toString().contains("http://")) {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(data.toString()))
                            startActivity(browserIntent)
                        }
                    }

                })


            //   TabLayoutMediator(_binding1!!.tabLayout1, _binding1!!.banner1) { _, _ -> }.attach()

            _binding1!!.banner4.clipToPadding = false
            _binding1!!.banner4.clipChildren = false
            _binding1!!.banner4.offscreenPageLimit = 3
            _binding1!!.banner4.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            //   _binding1!!.banner3.setPadding(30, 0, 30, 0)

            //Handler Start

            Handler().apply {
                val runnable = object : Runnable {
                    override fun run() {
                        var i = _binding1!!.banner4.currentItem

                        if (i == bannerList4.size - 1) {
                            i = -1//0
                            _binding1!!.banner4.currentItem = i
                        }
                        i++
                        _binding1!!.banner4.setCurrentItem(i, true)
                        postDelayed(this, 4000)
                    }
                }
                postDelayed(runnable, 4000)
            }

        }
    */
    private fun setBannerdata4() {
        VerticalBannerAdapter = VerticalBannerAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })

        _binding1!!.banner4.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = VerticalBannerAdapter
        }
    }


    private fun exculsiveCashback() {
        exculsivecashbackAdapter = ExculsivecashbackAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                setOnCustomeCrome(data.toString(), "")
            }
        })
        _binding1!!.banner1.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = exculsivecashbackAdapter
        }
    }

    private fun Banner2() {
        Banner2Adapter = Banner2Adapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                setOnCustomeCrome(data.toString(), "")
            }

        })
        _binding1!!.banner2.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = Banner2Adapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun SetDataForSearchcashback(i: Int, t: HomeRes) {

        val cashbackData =
            t.new_cashback!!.get(i).cashback + " on " + t.new_cashback!!.get(i).mini_app?.name

        //binding.searchTv.hint = cashbackData
    }

    /**
     * @param moreText show more icon or show less icon
     */


    private fun setOffersForYou() {
        offersForYouAdapter = offerforyou_Adapter(this)
        _binding1!!.offersForYou.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = offersForYouAdapter
        }
    }

    private fun setRecyclerCashbackPartner() {
        partnerCashbackAdapter = HomeCashbackPartnerAdapter(this)
        _binding1!!.recyclerCashbackpartner.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = partnerCashbackAdapter
        }
    }

    private fun getShop() {
        getSharedPreference().getUserId().let {
            viewModel.getShopProduct(getUserId(), "", "", "", this,
                object : ApiListener<ShopProductData, Any?> {
                    override fun onSuccess(t: ShopProductData?, mess: String?) {
                        t!!.data.let {
                            shopProducatAdapter!!.addAllItem(
                                if (it.size > 8) it.shuffled().subList(0, 8) else it
                            )

                        }
                    }
                })
        }
    }

    private fun getShopProduct() {
        shopProducatAdapter = ShopProducatAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                setOnCustomeCrome(data.toString(), "")
            }

        })
        _binding1!!.ShopProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = shopProducatAdapter
        }


    }

    private fun setOnCustomeCrome(url: String, color: String) {
        val customIntent = CustomTabsIntent.Builder()
        customIntent.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        customIntent.setShowTitle(true);
        openCustomTab(requireContext(), customIntent.build(), Uri.parse(url))
    }

    private fun openCustomTab(
        requireContext: Context,
        customTabsIntent: CustomTabsIntent,
        Url: Uri
    ) {
        val packageName = "com.android.chrome"
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(requireContext, Url)
        } else {
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Url))
        }
    }

    private fun recommendedDatagett(it: ArrayList<Service>) {
        service = it
        var myadapter = RecommendedAdapter<Service>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    openWebView(data)
                }
            }
        })
        myadapter.setShowRupee(true)
        myadapter.setMoreImagePos(16)
//        _binding1!!.recommended.apply {
//            layoutManager = GridLayoutManager(context, 4)
//            adapter = myadapter
//        }
        myadapter.addAllItem(if (it.size > 8) it.subList(0, 7) else it)
    }

    private fun setPopularMerchants(list: ArrayList<Popular>) {
//        val mAdapter = popularitemAdapter<Popular>(object : RecyclerClickListener {
        val mAdapter = homePagePopularStoreAdapter<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    openWebView(data)
                }
            }
        })
        _binding1!!.recyclerPopularMerchants.apply {
//            layoutManager= GridLayoutManager(requireContext(),3,GridLayoutManager.HORIZONTAL,false)
            layoutManager = GridLayoutManager(context, 3)
            adapter = mAdapter
        }
        mAdapter.addAllItem(
            if (list.size > 9) {
                list.subList(0, 9)
            } else {
                list
            }
        )

    }


    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()
        popularList?.let {
            list.addAll(it)
        }
        if (type == "1")
        //  openContainer("MiniAppSearch", list, false, "data.name")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        //    SearchFragment.showSearch(childFragmentManager, list)
        else
            openContainer(
                getString(R.string.popular_merchants),
                list,
                true,
                getString(R.string.popular_merchants)
            )
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            NewHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
                    //     setAppCategory(getString(R.string.less))
                    setFavourites(getString(R.string.less))
                }
                getString(R.string.ShopCompair) -> {
                    openContainer("Shop & Compair", data, false, data.name)
                }
                getString(R.string.All) -> {
                    if (activity is HomeActivity)
                        (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.ShopNow)
                }
                getString(R.string.Offers) -> {
                    if (activity is HomeActivity)
                        (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.offerTab)
                }
                getString(R.string.less) -> {
                    //     setAppCategory(getString(R.string.more))
                    setFavourites(getString(R.string.more))
                }
                else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                openWebView(data)
        }
    }


    private fun openWebView(data: MiniApp) {
        //    ActiveCashbackActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id)
        WebViewActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id
        )
    }

    override fun onResume() {
        super.onResume()
        inAppUpdateProgress()
//        callVideoModelClass()
        val loginData = getSharedPreference().getLoginData()
        HomeFragment.UserId = loginData?.unique_user_id.toString()

        if (loginData?.image.isNullOrEmpty()) {
            _binding1!!.profileName.visibility = View.VISIBLE
            _binding1!!.profileIv.visibility = View.GONE
            _binding1!!.profileName.text = loginData!!.name!!.replaceAfter(" ", "")

        } else {
            _binding1!!.profileName.visibility = View.GONE
            Glide.with(this).load(loginData?.image).apply(RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)).into(_binding1!!.profileIv)
        }
        //   ("Hi," + loginData?.name).also { binding.nameTv.text = it }
//        _binding1!!.coinsTv.text = withSuffixAmount(loginData?.cash_available)

//        _binding1!!.name.text =loginData?.name!!.get(0).uppercaseChar().toString().replaceAfter(" ","")
        _binding1!!.name.text =requireContext().CamelCaseValue(loginData?.name.toString()).replaceAfter(" ","")
        _binding1!!.welcomename.text ="Hi "+ requireContext().CamelCaseValue(loginData?.name.toString()).replaceAfter(" ","")+","

        Log.d("Created at ",loginData!!.created_at.toString())
        //  binding.startearningTV.text = getString(R.string.startearning, loginData?.name)
//        _binding1!!.coinsTv.text = withSuffixAmount(loginData?.cash_available)


    }

    @SuppressLint("LogConditional")
    private fun setFavourites(moreText: String) {
        val mAdapter = HomeFavAdapter(this)
        _binding1!!.recyclerFavourites.apply {
            //   layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//            layoutManager = GridLayoutManager(context, 4)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
        favViewModel?.getData()?.observe(viewLifecycleOwner, {
            if (it.data?.size == 0) {
//                _binding1!!.favouritescard.visibility = View.VISIBLE
                _binding1!!.favourites.visibility = View.GONE
            } else {
//                _binding1!!.favouritescard.visibility = View.GONE
                _binding1!!.favourites.visibility = View.VISIBLE
            }


            if (it.status == Status.SUCCESS) {
                mAdapter.clear()
                //    it.data?.let { it1 -> mAdapter.addAllItem(it1) }

                it.data?.let { it1 ->
//                    if (it1.size > 10 && moreText == getString(R.string.more))
//                        mAdapter.addAllItem(it1.subList(0, 9).toList())
//                    else
                    mAdapter.addAllItem(it1)

                }
            }
        })
        getFevouriteData()
    }

    private fun getFevouriteData() {
        favViewModel?.getFavouriteList(getUserId())
    }

    fun refreshFavData() {
        getFevouriteData()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun dissmissLoader() {
//        _binding1!!.loadingbar.visibility = View.GONE
//        _binding1!!.mainLayout.visibility = View.VISIBLE
    }


    private fun showLoaderFragment() {
//        _binding1!!.loadingbar.visibility = View.VISIBLE
//        _binding1!!.mainLayout.visibility = View.GONE
    }

}