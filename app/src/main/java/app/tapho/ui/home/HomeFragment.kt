package app.tapho.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.BuildConfig
import app.tapho.CamelCaseValue
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.R
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.FragmentHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.BaseRes
import app.tapho.network.Status
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.MiniCash.Adapter.MerchantsDataAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.RechargeService.ModelData.RechargeStatus.checkRechargeStatusRes
import app.tapho.ui.Stories.Adapter.StoriesAdapter
import app.tapho.ui.Stories.Model.StoriesResFile
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.favourite.FavouriteDialogFragment
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import app.tapho.ui.home.adapter.*
import app.tapho.ui.localbizzUI.LocalBizSplashActivity
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.model.*
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.model.UserDetails.getUserDetailRes
import app.tapho.ui.model.mini_app_data.MiniAppsDataRes
import app.tapho.ui.scanner.NewScannerActivity
import app.tapho.ui.scanner.scanner
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import app.tapho.viewmodels.FavouriteViewModel
import app.tapho.viewmodels.MerchantsAllListViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.kakyiretechnologies.appreview.reviewApp
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory
import java.text.SimpleDateFormat


class HomeFragment : BaseFragment<FragmentHomeBinding>(), RecyclerClickListener, ConnectivityListener {



    private val channelID = "BitinfozCoder"

    var checkfav: String? = ""
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var categoryTabAdapter: CategoryTabAdapter<CashbackMerchantCategory>? = null
    var madapterdata : MerchantsDataAdapter?=null
    private var dataList: java.util.ArrayList<CashbackMerchant>? = java.util.ArrayList()

    //In App Update
    var moreText="Show more"
    private var inappupdate: AppUpdateManager? = null
    private val inAppUpdateRequest_Code = 1010
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var partnerCashbackAdapter: HomeCashbackPartnerAdapter<BannerList>? = null
    private var appCategoryList: java.util.ArrayList<AppCategory>? = null
    private var popularList: java.util.ArrayList<Popular>? = null
    private var service: java.util.ArrayList<Service>? = null
    private var favViewModel: FavouriteViewModel? = null
    private var StoriesAdapter: StoriesAdapter<app.tapho.ui.Stories.Model.Data>? = null
    var customShopCategory : SuperLinkAdapter?=null
    private val merchantViewMode: MerchantsAllListViewModel by viewModels()
    var Storiesdata: java.util.ArrayList<app.tapho.ui.Stories.Model.Data> = java.util.ArrayList()
    var StoriesCategoryID = 0
    var tcashdashboard : TCashDasboardRes? = null

//    var handler = Handler(Looper.getMainLooper())


    fun setConnectivityListener(listener: ConnectivityListener) {
        ConnectionReceiver.connectivityListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        statusBarColor(R.color.grey_v_light)
        statusBarTextWhite()
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        checkConnection()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        //In App Review Code
        reviewApp(5)

        val deviceData = requireContext().getSystemDetail()

        createShortCuts()

        //Update Dialog Code
        inappupdate = AppUpdateManagerFactory.create(requireContext())
        InAppUpdate()

        _binding!!.swipeRefresh.setOnRefreshListener {
            _binding!!.swipeRefresh.isRefreshing = true
            callVideoModelClass()
            _binding!!.swipeRefresh.isRefreshing = false
        }
        _binding!!.endtoendencription.setOnClickListener {
            openContainer("EndToEndEncriptionFragment","",false,"")
        }
        _binding!!.androidV.text="App Version - "+ BuildConfig.VERSION_NAME+" 30.12"

        val typenoty = getSharedPreference().getString("type")
        val datanoty =getSharedPreference().getString("TAGS")
        if (typenoty.toString().isNullOrEmpty().not()){
            openNotification(typenoty!!,datanoty!!)
        }

        return _binding?.root
    }

    private fun createShortCuts() {
        val intent : Intent = Intent(requireContext(),scanner::class.java)
        intent.action = Intent.ACTION_VIEW
        val shortcut = ShortcutInfoCompat.Builder(requireContext(), "id1")
            .setShortLabel("Scanner")
            .setLongLabel("Open Scanner")
            .setIcon(IconCompat.createWithResource(requireContext(), R.drawable.ic_qr_code_scanner))
            //   .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mysite.example.com/")))
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(requireContext(), shortcut)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
//                    Toast.makeText(requireContext(), "You need to grant permission to access location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun openNotification(nottype :String,dataNoty :String){

        getSharedPreference().saveString("TAGS","")
        getSharedPreference().saveString("type","")
        when (nottype) {

            "1"->{
                ContainerActivity.openContainer(requireContext(),dataNoty,"")
            }
            "2"->{
                getGamesData(dataNoty)
            }
            "3"->{
                getIntoMiniApps(dataNoty,"3")
            }
            "4"->{
                getIntoMiniApps(dataNoty,"4")
            }
            else->{

            }
        }
    }

    private fun getGamesData(gameId: String?) {
        viewModel.getAllGames(getUserId(),"","","",this,object : ApiListener<Games,Any?>{
            override fun onSuccess(t: Games?, mess: String?) {
                t.let {
                    it!!.data.forEach {
                        if (it.id.equals(gameId)){
                            GameWebViewActivity.openWebView(requireContext(),it.url,it.id,it.name,it.assets.square,"")
                        }
                    }
                }
            }
        })
    }

    private fun getIntoMiniApps(MiniAppId : String, type: String) {
        viewModel.searchMiniApp(getUserId(),MiniAppId,this,object : ApiListener<WebTCashRes,Any?>{
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
             t!!.let {
                 it.data.get(0).let {
//                     if (type.equals("3")){
                         ActiveCashbackForWebActivity.openWebView(requireContext(),it.url,it.id,it.image,it.tcash,it.is_favourite,it.cashback,it.app_category_id,it.url_type,it.name)
//                     }else {
//                         WebViewActivityForOffer.openWebView(requireContext(),it.url,it.id,it.image,it.tcash,it.is_favourite,it.cashback,it.app_category_id)
//                     }
                 }
             }
            }
        })
    }

    private fun callVideoModelClass() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable{
            override fun run() {
                synchronized(this@HomeFragment){
                    handler.postDelayed(object :Runnable{
                        override fun run() {
                            kotlin.runCatching {
                                if (getSharedPreference().getString("StartLoading").equals("0")) {
                                    lowConnection()
                                } else {
                                    handler.removeCallbacksAndMessages(null)
                                    getNotificationData()
                                }
                            }

                        }
                    },35000)

                }

            }
        }
        val thread = Thread(runnable)
        thread.start()

        progressVISIBLE()
        getData()
        superlinks()
        setRecyclerCashbackPartner()
//        observeData()
//        getMerchantData()


        setMoreClicks()
        exculsiveCashback()
        setRecyclerBrand()
        setStoriesLayout()

        _binding!!.Greeting.text ="Hey, "+ getGreetingMessage()
        setTabLayoutdata()
        setDatainAllMerchant()

    }

    private fun getNotificationData() {
        viewModel.getAllNotification(getUserId(),this,object : ApiListener<AllNotificationRes,Any?>{
            override fun onSuccess(t: AllNotificationRes?, mess: String?) {
                val data : java.util.ArrayList<app.tapho.ui.model.AllNotification.Data> =
                    java.util.ArrayList()
                t.let {
                    it!!.data.forEach {
                        if (it.noti_type.equals("1")){
                            data.add(it)
                        }
                    }
                    if (data.isNullOrEmpty().not()) {
                        PopUpBar(data)
                    }
                }
            }

        })
    }

    private fun PopUpBar(data: java.util.ArrayList<app.tapho.ui.model.AllNotification.Data>) {
        if(getSharedPreference().getString("NotificationStatusID").toString().equals(data.get(0).id).not()){
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.cashback_rule_popup, null)
            dialog.setCancelable(true)
            //  val AllTransaction: TextView = view.findViewById(R.id.viewAllTransaction)
            val AllTransaction: AppCompatButton = view.findViewById(R.id.viewAllTransaction)
            val iconS: ImageView = view.findViewById(R.id.iconS)
            val imageIv: ImageView = view.findViewById(R.id.imageIv)
            val discription: TextView = view.findViewById(R.id.discription)
            val cashback: TextView = view.findViewById(R.id.cashback)
            getSharedPreference().saveString("NotificationStatusID",data.get(0).id)
            Glide.with(requireContext()).load(data.get(0).offer_data.get(0).image).circleCrop().into(iconS)
            Glide.with(requireContext()).load(R.drawable.app_icon).circleCrop().into(imageIv)
            discription.text = "Your Cashback has been tracked\nfrom "+data.get(0).offer_data.get(0).name
            val commision = data.get(0).merchant_postback_value.get(0).user_commision
            cashback.text = "+"+withSuffixAmount(commision)
            dialog.setContentView(view)
            dialog.show()
            AllTransaction.setOnClickListener {
                ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","0","Pending Rewards",tcashdashboard)
                dialog.dismiss()
            }
        }else{

        }
    }
    /*
        private fun getMerchantData() {
            merchantViewMode.getCashbackMerchants(AppSharedPreference.getInstance(requireContext()).getUserId(), "0")
        }

        private fun observeData() {
            merchantViewMode.getValue().observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                    }
                    Status.SUCCESS -> {
                        setData(it.data)
                    }
                }
            })
        }

        private fun setData(data: CashbackMerchantsAllRes?) {
            madapterdata!!.clear()
            var category = ""
            data?.let {
                it.cashback_merchant_category?.let { categoryList ->
                    category = categoryList.get(0).id.toString()
                    //    categoryTabAdapter?.addItem(CashbackMerchantCategory("", "", "All", "", true))
                    categoryList.forEach {
                        if (category.equals(it.id)){
                            categoryTabAdapter?.addItem(CashbackMerchantCategory(it.created_at, it.id, it.name, it.status, true))
                        }else{
                            categoryTabAdapter?.addItem(CashbackMerchantCategory(it.created_at, it.id, it.name, it.status, false))
                        }
                    }
    //                categoryTabAdapter?.addAllItem(categoryList)
                }
                val list : ArrayList<CashbackMerchant> = ArrayList()
                it.data!!.forEach {
                    if (it.cashback_merchant_category_id!!.equals(category)){
                        list.add(it)
                    }
                }

                madapterdata!!.addAllItem(list)
                it.data!!.forEach {
                    if (it.mini_app!=null){
                        dataList!!.add(it)
                    }

                }

            }
        }
    */
    private fun setTabLayoutdata() {
        val list = java.util.ArrayList<CashbackMerchant>()
        categoryTabAdapter = CategoryTabAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                madapterdata!!.clear()
                list.clear()
                if (data is CashbackMerchantCategory) {
                    if (data.id.isNullOrEmpty().not()){
                        dataList?.forEach {
                            if (it.cashback_merchant_category_id == data.id) {
                                list.add(it)
                            }
                        }
                    } else{
                        dataList?.let { list.addAll(it) }

                    }
                    madapterdata!!.addAllItem(list)
                }
            }

        })
        _binding!!.recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryTabAdapter
        }
    }

    private fun setDatainAllMerchant() {
        madapterdata = MerchantsDataAdapter( this)
        _binding!!.merchantdata.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = madapterdata
        }
    }

    private fun checkConnection() {
        val isConnected = ConnectionReceiver.isConnected(requireContext())

        if (isConnected) {
            callVideoModelClass()
        } else{
            noInternetConnection()
        }

        _binding!!.retryinternetButton.setOnClickListener {
            checkConnection()
        }

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            callVideoModelClass()
        } else{
            noInternetConnection()
        }
    }

    private fun InAppUpdate() {
        inappupdate!!.appUpdateInfo.addOnSuccessListener { AppUpdateInfo ->
            kotlin.runCatching {
                if (AppUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && AppUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    inappupdate?.startUpdateFlowForResult(AppUpdateInfo, AppUpdateType.IMMEDIATE, requireActivity(), inAppUpdateRequest_Code)
                }
            }
        }
    }

    private fun inAppUpdateProgress() {
        inappupdate!!.appUpdateInfo.addOnSuccessListener { AppUpdateInfo ->
            kotlin.runCatching {
                if (AppUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    inappupdate?.startUpdateFlowForResult(AppUpdateInfo, AppUpdateType.IMMEDIATE, requireActivity(), inAppUpdateRequest_Code)
                }
            }
        }
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
        _binding!!.Stories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = StoriesAdapter
        }
    }

    private fun setStoriesByIDViewmodel(StoriesId: Int) {
        viewModel.getStoriesData(getUserId(), this, object : ApiListener<StoriesResFile, Any?> {
            override fun onSuccess(t: StoriesResFile?, mess: String?) {
                t!!.let {
                    StoriesAdapter!!.addAllItem(it!!.data)
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
        val myStories: java.util.ArrayList<MyStory> = java.util.ArrayList()
        for (story in data) {
            story.story.forEach {
                myStories.add(MyStory(it.image))
            }
        }

        StoryView.Builder(activity?.supportFragmentManager)
            .setStoriesList(myStories) // Required
            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
            .setTitleText(data.get(0).name) // Default is Hidden
//            .setSubtitleText(data.get(0).description.toString()) // Default is Hidden
            .setTitleLogoUrl(data.get(0).image) // Default is Hidden
            .setStoryClickListeners(object : StoryClickListeners {
                override fun onDescriptionClickListener(position: Int) {
//                    Toast.makeText(requireContext(), "${data.get(0).story.get(position).url}", Toast.LENGTH_LONG).show()
                }

                override fun onTitleIconClickListener(position: Int) {
                    //your action
                }
            }) // Optional Listeners
            .build() // Must be called before calling show method
            .show()
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

    private fun tcashhistoryDataViewModel1() {

        getSharedPreference().getUserId().let {
            viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),"2", this, object : ApiListener<TCashDasboardRes, Any?> {
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    _binding!!.pendingbalance.text = withSuffixAmount(t!!.pending.toString())
                    _binding!!.availbalance.text = withSuffixAmount(t.cash_available.toString())
                    _binding!!.lifetimeearning.text = withSuffixAmount(t.lifetime_earning.toString())

                    tcashdashboard = t

                    val TempList: java.util.ArrayList<Double> = java.util.ArrayList()
                    var transaction = 0.0
                    t.data.let {
                        if (it != null) {
                            it.forEach {
                                TempList.add(it.sale_amount!!.toDouble())
                            }
                        }
                    }

                    for (x in TempList) {
                        transaction += x
                    }
                    }
                })
        }

     //   viewModel.getTCashDashboard1(getUserId(),TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),"2")

    }

    private fun setRecyclerBrand() {
        itemTypeAdapter = ItemTypeAdapter(this)
//        NewHomecategoryAdapter = NewHomecategoryAdapter(this)
        _binding!!.recyclerAppCategory.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context, 5)
            adapter = itemTypeAdapter
        }
    }

    private fun setMoreClicks() {
        setMoreTextSuperLinks("Show less")
        _binding!!.pendingLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","0","Pending Rewards",tcashdashboard)
        }



        _binding!!.leaveFeedback.setOnClickListener {
            rateApp()
        }
        _binding!!.notificationRe.setOnClickListener {
            ContainerActivity.openContainer(context, "AllNotification", "")
        }
        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        _binding!!.cashbackCard.setOnClickListener {
            ContainerActivity.openContainer(context, "cashbackcard", tcashdashboard)
        }
        _binding!!.verifiedLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","1","Total Rewards",tcashdashboard)
        }

        _binding!!.recommendedAll.setOnClickListener {
//            startActivity(Intent(requireContext(), MiniCashSplashActivity::class.java))
            ContainerActivity.openContainer(requireContext(), "OnlineStores", "")
        }

        _binding!!.favouritesBtn.setOnClickListener {
            openContainer("favouritesBtn", "", false, "")
        }
        _binding!!.seeAll.setOnClickListener {
            openContainer("favouritesBtn", "", false, "")
        }

        _binding!!.reProfile.setOnClickListener {
            ContainerActivity.openContainer(context,"ProfileDetailFragment", tcashdashboard)
        }

        _binding!!.AddMoreFavTV.setOnClickListener {
            FavouriteDialogFragment.newInstance(appCategoryList).show(childFragmentManager, null)
        }
        _binding!!.scanner.setOnClickListener {
//            startActivity(Intent(requireContext(), scanner::class.java))
            permissionTaking()
        }



    }

    private fun blockUser() {
        val builder = AlertDialog.Builder(requireContext())
        val dialog =builder.create()
        dialog.setTitle("Your Account has been suspended ")
        dialog.setMessage("Contact our support team for feather process .")

        dialog.setCancelable(false)
        dialog.show()

    }

    private fun updateRechargeStatus() {
        viewModel.checkRechargeStatus(getUserId(),this,object :ApiListener<checkRechargeStatusRes,Any?>{
            override fun onSuccess(t: checkRechargeStatusRes?, mess: String?) {

            }
        })
    }

    private fun permissionTaking() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 100)
        } else {
//            ScanAndPayContainerActivity.openContainer(requireContext(),"EnterAmountForSend","textData","","")
            startActivity(Intent(requireContext(), NewScannerActivity::class.java))
        }
    }


    private fun getData() {
        getSharedPreference().saveString("StartLoading","0")
        viewModel.getHomeData("home", getUserId(), this, object :ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t?.let {
                    showHome()
                    getSharedPreference().saveString("StartLoading","1")
                    appCategoryList = it.app_category
                    setAppCategory(getString(R.string.more))

                    tcashhistoryDataViewModel1()

                    it.popular?.let {
                        popularList = it
                    }



                    it.services?.let {
                        recommendedDatagett(it)
                    }

                    it.popular?.let { popular ->
                        setPopularMerchants(popular)
                    }
                    it.profile_detail!!.get(0).let {
                        if (it.status.equals("0")){
                            blockUser()
                        }
                    }
                    it.profile_detail!!.get(0).let {
                        if (it.recharge_request_pending.toDouble()>0){
                            updateRechargeStatus()
                        }
                    }

                    it.banner_list1?.let { bannerList1 ->

                        if (bannerList1.isNullOrEmpty()) {
                            _binding!!.banner1.visibility = View.GONE
                            _binding!!.tabLayout.visibility = View.GONE
                        } else {
                            val banners : java.util.ArrayList<BannerList> = java.util.ArrayList()
                            bannerList1.forEach {
                                if (it.image.isNullOrEmpty().not()){
                                    if (it.expiry_date.toString().isNullOrEmpty().not()){
                                        val sdf = SimpleDateFormat("MM/dd/yyyy")
                                        val currentdate = TimePeriodDialog.getCurrentDate() //: Date = sdf.parse(Calendar.DAY_OF_MONTH.toString())
                                        val expiredate =it.expiry_date // : Date = sdf.parse(it.expiry_date.toString())
                                        if (expiredate != null) {
                                            if (expiredate>currentdate){
                                                banners.add(it)
                                            }else{

                                            }
                                        }

                                    }else{
                                        banners.add(it)
                                    }
                                }
                            }

                            if (banners.isNullOrEmpty()){
                                _binding!!.banner1.visibility = View.GONE
                                _binding!!.tabLayout.visibility = View.GONE
                            }else{
                                setBanner1Auto(banners)
                            }

                        }

                    }

                    it.banner_list10.let {bannerList1->
                        if (bannerList1.isNullOrEmpty()) {
                            _binding!!.promotedBanner.visibility = View.GONE
                        } else {
                            val banners : java.util.ArrayList<BannerList> = java.util.ArrayList()
                            bannerList1.forEach {
                                if (it.image.isNullOrEmpty().not()){
                                    if (it.expiry_date.toString().isNullOrEmpty().not()){
                                        val sdf = SimpleDateFormat("MM/dd/yyyy")
                                        val currentdate = TimePeriodDialog.getCurrentDate() //: Date = sdf.parse(Calendar.DAY_OF_MONTH.toString())
                                        val expiredate =it.expiry_date // : Date = sdf.parse(it.expiry_date.toString())
                                        if (expiredate != null) {
                                            if (expiredate>currentdate){
                                                banners.add(it)
                                            }else{
                                            }
                                        }

                                    }else{
                                        banners.add(it)
                                    }

                                }
                            }

                            if (banners.isNullOrEmpty()){
                                _binding!!.promotedBanner.visibility = View.GONE
                            }else{
                                setPromotedBanners(banners)
                            }

                        }
                    }

                    it.banner_list9?.let {
                        if (it.isNullOrEmpty()){
                            _binding!!.explorelayout.visibility = View.GONE
                        }else{
                            _binding!!.explorelayout.visibility = View.VISIBLE
                        }
                        partnerCashbackAdapter?.addAllItem(it)
                    }

                    it.popular?.let {
                        val miniapps : java.util.ArrayList<Popular> = java.util.ArrayList()
                        val miniapps2 : java.util.ArrayList<Popular> = java.util.ArrayList()

                        it.forEach {
                            if (it.image.isNullOrEmpty().not()){
                                miniapps.add(it)
                            }
                        }


                        if (miniapps.size>=4){
                            miniapps2.addAll(miniapps.subList(0,4))
                            setpopularMiniBanner(miniapps2)
                        }else{
                            setpopularMiniBanner(miniapps)
                        }

                    }
//            showHome()
                }
            }

        })
    }



    private fun CustomNotification(popular: Popular) {
        val name = getSharedPreference().getLoginData()!!.name!!.replaceAfter(" ","").replace(" ","")
        //     val remoteViews = RemoteViews(requireContext().getPackageName(), R.layout.customnotification)
        val titleForNotification = "Hii "+name+" shop on "+popular.mini_app!!.name
        val discription = "You will get assured "+popular.cashback
        val intent = Intent(requireContext(),ActiveCashbackForWebActivity::class.java)

        intent.apply {
            putExtra(WEB_VIEW_URL, popular.mini_app!!.url)
            putExtra(MINI_APP_ID, popular.mini_app!!.id)
            putExtra(ICON_URL, popular.mini_app!!.image)
            putExtra(TCASH_TYPE,  popular.mini_app!!.tcash)
            putExtra(IS_FAVOURITE, popular.mini_app!!.is_favourite)
            putExtra(CASHBACK, popular.cashback)
            putExtra(APPCATEGORYID, popular.mini_app!!.app_category_id)
            putExtra("UrlType", popular.mini_app!!.url_type)
            putExtra("Name", name)
        }

        val pIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(requireContext(),channelID)
            .setSmallIcon(R.drawable.app_icon)
            .setAutoCancel(true)
            .setContentIntent(pIntent)
            .setContentTitle(titleForNotification)
            .setContentText(discription)
        kotlin.runCatching {
            notification.setLargeIcon(Glide.with(requireContext()).asBitmap().load(popular.image).submit().get())
            notification.setStyle(NotificationCompat.BigPictureStyle().bigPicture(Glide.with(requireContext()).asBitmap().load(popular.mini_app!!.image).submit().get()))
        }

        val notificationmanager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        //   createnotificationMagnager(notificationmanager as NotificationManager)

        notificationmanager!!.notify(0, notification.build())
    }
    fun createnotificationMagnager(manager: NotificationManager){
        val channel = NotificationChannel(channelID,"BitinfozCoder", NotificationManager.IMPORTANCE_HIGH)

        channel.description = "Tapfo App"
        channel.enableLights(true)
        manager.createNotificationChannel(channel)
    }

    private fun setpopularMiniBanner(banners: java.util.ArrayList<Popular>) {

        if (banners.isNullOrEmpty()) {
            _binding!!.popularMinibanner.visibility = View.GONE
            _binding!!.popularMinitabLayout.visibility = View.GONE
        } else {
            _binding!!.popularMinibanner.visibility = View.VISIBLE
            _binding!!.popularMinitabLayout.visibility = View.VISIBLE
        }

        val imageList = java.util.ArrayList<SliderModelMain2>()
        //   bannerlist4 = bannerList4

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain2(x.image,x.mini_app!!.image, x.mini_app!!.url, x.mini_app!!.id,x.mini_app!!.tcash, x.mini_app!!.is_favourite, x.cashback))
            }
        }


        _binding!!.popularMinibanner.adapter = PopularBannerDataAdapter(imageList, _binding!!.popularMinibanner, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if (data is SliderModelMain2){
                        data.let {
                            ActiveCashbackForWebActivity.openWebView(requireContext(),it.url,it.id,it.icon,it.tcash,it.fav,it.cashback,"","","")
                        }

                    }else{
                        if(data.toString().contains("http")){
                            setOnCustomeCrome(data.toString(), "")
                        }else{
                            getWebUrlData2(data.toString(),"2")
                        }
                    }


                }
            })

        _binding!!.popularMinibanner.clipToPadding = false
        _binding!!.popularMinibanner.clipChildren = false
        _binding!!.popularMinibanner.offscreenPageLimit = 3
        _binding!!.popularMinibanner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.popularMinibanner.setPadding(20, 0, 20, 0)

        //Handler Start

        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.popularMinibanner.currentItem

                        if (i == banners.size - 1) {
                            i = -1//0
                            _binding!!.popularMinibanner.currentItem = i
                        }
                        i++
                        _binding!!.popularMinibanner.setCurrentItem(i, true)
                        postDelayed(this, 6000)
                    }
                }
            }
            postDelayed(runnable, 6000)
        }


        TabLayoutMediator(_binding!!.popularMinitabLayout, _binding!!.popularMinibanner,false) { _,_ -> }.attach()
    }

    private fun setMoreTextSuperLinks(moreTex: String) {
        val CustomeShopCategory: java.util.ArrayList<CustomeSuperCategoryModel> = ArrayList()
        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.online2_gif, "Mini Save", "OnlineStores","Cashback"))
        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.new_recharge_icon, "Mobile Recharge", "MobileRecharge",""))
        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.walletshare_icon, "Refer & Earn", "ReferAndEarn",""))
        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.food_bowl, "Offer", "Offers",""))
//        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.food_bowl, "Food Duniya", "TapfoFood",""))
        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.games_icon_new, "MyMini", "MyMini",""))
        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.games_icon_new, "Games", "900+ Games",""))
        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.new_mobile_recharge_icon, "Recharge & Paybills", "BillsAndRecharge",""))
//        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.pinbiz_icon, "Pinbiz", "localbiz","Beta"))
//        CustomeShopCategory.add(CustomeSuperCategoryModel(R.drawable.voucher_icon_new, "Gift Voucher", "Gift Voucher",""))


        if (CustomeShopCategory.size<=6){
            customShopCategory!!.addAllItem(CustomeShopCategory)
        }else{
            if (moreTex == "Show more") {
                customShopCategory!!.clear()
                customShopCategory!!.addAllItem(CustomeShopCategory)
                customShopCategory!!.addItem(CustomeSuperCategoryModel(R.drawable.superlinks_upicon, "Less", "Show less",""))
                moreText ="Show less"
            }else{
                customShopCategory!!.clear()
                customShopCategory!!.addAllItem(if (CustomeShopCategory.size>=6) CustomeShopCategory.subList(0,5) else CustomeShopCategory)
                customShopCategory!!.addItem(CustomeSuperCategoryModel(R.drawable.down_2_icon, "More", "Show more",""))
                moreText ="Show more"
            }
        }


    }

    private fun superlinks() {
        customShopCategory = SuperLinkAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                screenredirection(data.toString())

            }

        })


        _binding!!.SuperLinksRV.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = customShopCategory
        }
    }

    private fun screenredirection(data: String) {
        when (data) {

            "TapfoFood" -> {
                TapfoFoodContainerActivity.openContainer(requireContext(),"TapfoFoodLocationFragment","")
            }
            "MyMini" -> {
                ContainerActivity.openContainer(requireContext(),"NewHomeFragment","")
            }

            "Pending Rewards"->{
                ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","0","Pending Rewards",tcashdashboard)
            }
            "Total Rewards"->{
                ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","1","Total Rewards",tcashdashboard)
            }

            "addtopup"->{
                getSharedPreference().saveString("wallet_cashback", "0")
                ContainerActivity.openContainerForVoucher(context, "addtopup", "", "", "",tcashdashboard)
            }

            "OnlineStores" -> {
                ContainerActivity.openContainer(requireContext(), "OnlineStores", "")
            }
            "Offers" -> {
                ContainerActivity.openContainer(requireContext(), "OffersFragment", "")
            }
            "Show less" -> {
                setMoreTextSuperLinks("Show less")
            }
            "ReferAndEarn" -> {
                ContainerActivity.openContainer(requireContext(), "referandearnscreen", tcashdashboard)
            }
            "Show more" -> {
                setMoreTextSuperLinks("Show more")
            }
            "Scan & Pay" -> {
                ContainerActivity.openContainer(requireContext(), "ScanAndPayIntroFragment", "")
            }

            "Gift Voucher" -> {
                startActivity(Intent(requireContext(), VouchersActivity::class.java))
            }
            "BillsAndRecharge" -> {
//                getSharedPreference().saveString("servicetype","1")
//                ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
                        RechargeServiceActivity.openRechargeService(requireContext())
            }
            "MobileRecharge" -> {
                getSharedPreference().saveString("servicetype","1")
                ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
//                        RechargeServiceActivity.openRechargeService(requireContext())
            }

            "900+ Games" -> {
                if (getSharedPreference().getString("gameIntro").isNullOrEmpty()) {
                    ContainerActivity.openContainer(requireContext(), "GamesOneTimeSplash", "")
                } else {
                    ContainerActivity.openContainer(requireContext(), "Games", "")
                }
            }


//            "AllProductWithCategory" -> {
//                ContainerActivity.openContainer(requireContext(), "AllProductWithCategory", "")
//            }

            "localbiz" -> {
//                        if (getSharedPreference().getString("localbizOnBoarding").isNullOrEmpty()) {
//                            ContainerActivity.openContainer(requireContext(), "localbizOnBoarding", "")
//                        } else {
                startActivity(Intent(requireContext(), LocalBizSplashActivity::class.java))
//                        }

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

    private fun setBanner1Auto(banners: java.util.ArrayList<BannerList>) {

        if (banners.isNullOrEmpty()) {
            _binding!!.banner1.visibility = View.GONE
            _binding!!.tabLayout.visibility = View.GONE
        } else {
            _binding!!.banner1.visibility = View.VISIBLE
            _binding!!.tabLayout.visibility = View.VISIBLE
        }

        val imageList = java.util.ArrayList<SliderModelMain>()

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(x.image, x.url, x.id,x.type, "", ""))
            }

        }
        _binding!!.banner1.adapter = NewBannerDataAdapter(imageList, _binding!!.banner1, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data.toString().equals("mobile recharge")){
                    screenredirection(data.toString())
                } else {
                    getWebUrlData2(data.toString(),"2")
                }


            }
        })
        _binding!!.banner1.clipToPadding = false
        _binding!!.banner1.clipChildren = false
        _binding!!.banner1.offscreenPageLimit = 3
        _binding!!.banner1.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.banner1.setPadding(10, 0, 10, 0)

        //Handler Start
        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.banner1.currentItem

                        if (i == banners.size - 1) {
                            i = -1//0
                            _binding!!.banner1.currentItem = i
                        }
                        i++
                        _binding!!.banner1.setCurrentItem(i, true)
                        postDelayed(this, 4000)
                    }
                }
            }
            postDelayed(runnable, 4000)
        }


        TabLayoutMediator(_binding!!.tabLayout, _binding!!.banner1,false) { _,_ -> }.attach()
    }

    private fun setPromotedBanners(promotedbanners: java.util.ArrayList<BannerList>) {
        if (promotedbanners.isNullOrEmpty()) {
            _binding!!.promotedBanner.visibility = View.GONE
        } else {
            _binding!!.promotedBanner.visibility = View.VISIBLE
        }

        val imageList = java.util.ArrayList<SliderModelMain>()
        //   bannerlist4 = bannerList4

        for (x in promotedbanners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(x.image, x.url, x.id,x.type, "", ""))
            }
        }
        _binding!!.promotedBanner.adapter = NewBannerDataAdapter(imageList, _binding!!.promotedBanner, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//                if(data.toString().contains("http")){
                setOnCustomeCrome(data.toString(), "")
//                } else{
//                    getWebUrlData2(data.toString(),"2")
//                }
            }
        })
        _binding!!.promotedBanner.clipToPadding = false
        _binding!!.promotedBanner.clipChildren = false
        _binding!!.promotedBanner.offscreenPageLimit = 3
        _binding!!.promotedBanner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.promotedBanner.setPadding(30, 0, 30, 0)

        //Handler Start

        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.promotedBanner.currentItem

                        if (i == promotedbanners.size - 1) {
                            i = -1//0
                            _binding!!.promotedBanner.currentItem = i
                        }
                        i++
                        _binding!!.promotedBanner.setCurrentItem(i, true)
                        postDelayed(this, 4000)
                    }
                }
            }
            postDelayed(runnable, 4000)
        }


    }

    private fun exculsiveCashback() {
//        exculsivecashbackAdapter = ExculsivecashbackAdapter(object : RecyclerClickListener {
//            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//                if(data.toString().contains("http")){
//                    setOnCustomeCrome(data.toString(), "")
//                }else{
//                    getWebUrlData2(data.toString(),"2")
//                }
//
//            }
//        })
//        _binding!!.banner1.apply {
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//            adapter = exculsivecashbackAdapter
//        }
    }

    private fun getWebUrlData2(s: String,type: String) {
        viewModel.searchMiniApp(getUserId(), s, this, object : ApiListener<WebTCashRes, Any?> {
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                val url = t?.data?.get(0)?.url
                val id = t?.data?.get(0)?.id
                val name = t?.data?.get(0)?.name
                val icon = t?.data?.get(0)?.image
                val tcash = t?.data?.get(0)?.tcash
                val fav = t?.data?.get(0)?.is_favourite
                val cashback = t?.data?.get(0)?.cashback
                val appcategory = t?.data?.get(0)?.app_category_id
                val type = t?.data?.get(0)?.url_type
                ActiveCashbackForWebActivity.openWebView(requireContext(),url,id,icon,tcash,fav,cashback,appcategory,type,name)
            }

        })
    }

    private fun setRecyclerCashbackPartner() {
        partnerCashbackAdapter = HomeCashbackPartnerAdapter(this)
        _binding!!.recyclerCashbackpartner.apply {
            layoutManager = GridLayoutManager(requireContext(),1)
            adapter = partnerCashbackAdapter
        }
    }

    private fun setOnCustomeCrome(url: String, color: String) {
        val customIntent = CustomTabsIntent.Builder()
        customIntent.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.white))
        customIntent.setShowTitle(true)
        customIntent.setDefaultShareMenuItemEnabled(false)
        openCustomTab(requireContext(), customIntent.build(), Uri.parse(url))
    }

    private fun openCustomTab(requireContext: Context, customTabsIntent: CustomTabsIntent, Url: Uri) {
        val packageName = "com.android.chrome"
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(requireContext, Url)
        } else {
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Url))
        }
    }

    private fun recommendedDatagett(it: java.util.ArrayList<Service>) {
        service = it
        val myadapter = RecommendedAdapter<Service>(object : RecyclerClickListener {
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
//        _binding!!.recommended.apply {
//            layoutManager = GridLayoutManager(context, 4)
//            adapter = myadapter
//        }
        myadapter.addAllItem(if (it.size > 8) it.subList(0, 7) else it)
    }

    private fun setPopularMerchants(list: java.util.ArrayList<Popular>) {
//        val mAdapter = popularitemAdapter<Popular>(object : RecyclerClickListener {
        val mAdapter = homePagePopularStoreAdapter<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (pos.equals(1)){
                    if (data is Popular){
                        checkFavOrNot(data)
                    }

                }else{
                    if (type == "MiniAppFragment")
                        openAllPopularCashbackMerchants("2")
                    else if (type == OPEN_WEB_VIEW && data is Popular) {
                        data.mini_app!!.let {
                            ActiveCashbackForWebActivity.openWebView(context, it.url, it.id, it.image, it.tcash, it.is_favourite,
                                data.cashback, it.app_category_id, it.url_type,it.name)
                        }
                    }
                }
            }
        })
        _binding!!.recyclerPopularMerchants.apply {
//            layoutManager= GridLayoutManager(requireContext(),3,GridLayoutManager.HORIZONTAL,false)
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        mAdapter.clear()
        mAdapter.addAllItem(
            if (list.size > 12) {
                list.subList(0, 12)
            } else {
                list
            }
        )
    }

    private fun checkFavOrNot(data: Popular) {
        val miniAppId = data.mini_app!!.id
        if (miniAppId.isNullOrEmpty().not())
            viewModel.getMiniAppTCash(AppSharedPreference.getInstance(requireContext()).getUserId(),
                miniAppId, this, object : ApiListener<WebTCashRes, Any?> {
                    override fun onSuccess(t: WebTCashRes?, mess: String?) {
                        t?.let {
                            checkfav = it.mini_app?.get(0)?.is_favourite
                            makeorUnMakeFav(checkfav,miniAppId!!)
                        }
                    }
                })
    }

    private fun makeorUnMakeFav(checkfav: String?,miniAppId:String) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.miniapp_fav_bottomsheet, null)

        dialog.setCancelable(true)

        val favourite: TextView = view.findViewById(R.id.favourite)
        val unfavourite: TextView = view.findViewById(R.id.unfavourite)
        val iv_partner: ImageView = view.findViewById(R.id.iv_partner)

        Glide.with(this).load(R.drawable.favicon2).circleCrop()
            .placeholder(R.drawable.loding_app_icon).into(iv_partner)

        if (checkfav == "1") {
            favourite.visibility = View.GONE
            unfavourite.visibility = View.VISIBLE
        } else if (checkfav == "0") {
            favourite.visibility = View.VISIBLE
            unfavourite.visibility = View.GONE
        }

        favourite.setOnClickListener {
            makeFav(miniAppId)
            favourite.visibility = View.GONE
            unfavourite.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 1500)

        }
        unfavourite.setOnClickListener {
            makeUnFav(miniAppId)
            favourite.visibility = View.VISIBLE
            unfavourite.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 1500)

        }



        dialog.setContentView(view)
        dialog.show()
    }

    private fun makeFav(miniAppId:String) {
        viewModel.miniAppFev(AppSharedPreference.getInstance(requireContext()).getUserId(),
            miniAppId, this, object : ApiListener<BaseRes, Any?> {
                override fun onSuccess(t: BaseRes?, mess: String?) {
                    t?.let {
                        if (it.errorCode == "0") {
                            requireContext().customToast(getString(R.string.added_in_fev), false)

                        } else requireContext().toast(mess)
                    }
                }
            })
    }

    private fun makeUnFav(miniAppId:String) {
        viewModel.miniAppUnFev(AppSharedPreference.getInstance(requireContext()).getUserId(),
            miniAppId, this, object : ApiListener<BaseRes, Any?> { override fun onSuccess(t: BaseRes?, mess: String?) {
                t?.let {
                    if (it.errorCode == "0") {
                        requireContext().customToast(getString(R.string.removed_from_fav), true)

                    } else {
                        requireContext().toast(mess)
                    }
                }
            }
            })
    }

    private fun openAllPopularCashbackMerchants(type: String) {
        val list = java.util.ArrayList<Popular>()
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

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
                    setAppCategory(getString(R.string.less))
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
                    setAppCategory(getString(R.string.more))
                    setFavourites(getString(R.string.more))
                }
                else -> openContainer("SelectedCategoriesMiniAppsFragment", data, false, data.name)

//                 else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp){
                openWebView(data)
            }

        }
    }

    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()
        appCategoryList?.let { appCategory ->
            if (appCategory.size > 10 && moreText == getString(R.string.more))
                itemTypeAdapter?.addAllItem(appCategory.subList(0, 9).toList())
            else
                itemTypeAdapter?.addAllItem(appCategory)
            itemTypeAdapter?.addItem(AppCategory("", "", "", "", "", "", "", "", moreText, "", false, null))


        }
    }

    private fun openWebView(data: MiniApp) {
        ActiveCashbackForWebActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id, data.url_type,data.name)
    }

    override fun onResume() {
        super.onResume()
        setConnectivityListener(this)
        inAppUpdateProgress()
        tcashhistoryDataViewModel1()

        val loginData = getSharedPreference().getLoginData()
     //   HomeFragment.UserId = loginData?.unique_user_id.toString()
        if (loginData?.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = loginData!!.name!!.replaceAfter(" ", "")
        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(loginData?.image).apply(RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon)).into(_binding!!.profileIv)
        }

        _binding!!.name.text =requireContext().CamelCaseValue(loginData?.name.toString()).replaceAfter(" ","")

    }

    @SuppressLint("LogConditional")
    private fun setFavourites(moreText: String) {
        val mAdapter = HomeFavAdapter(this)
        _binding!!.recyclerFavourites.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }

        favViewModel?.getData()?.observe(viewLifecycleOwner, {
            if (it.data?.size == 0) {
//                _binding!!.favouritescard.visibility = View.VISIBLE
                _binding!!.favourites.visibility = View.GONE
            } else {
//                _binding!!.favouritescard.visibility = View.GONE
                _binding!!.favourites.visibility = View.VISIBLE
            }


            if (it.status == Status.SUCCESS) {
                mAdapter.clear()
                //    it.data?.let { it1 -> mAdapter.addAllItem(it1) }

                it.data?.let { it1 ->
//                    if (it1.size > 8 && moreText == getString(R.string.more)){
//                        mAdapter.addAllItem(it1.subList(0, 9).toList())
//                    }
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

    fun progressVISIBLE() {
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.VISIBLE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }

    fun lowConnection() {
        _binding!!.lowconnection.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }

    fun showHome() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.VISIBLE


//        val typenoty = getSharedPreference().getString("type")
//        val datanoty =getSharedPreference().getString("TAGS")
//        if (typenoty.toString().isNullOrEmpty().not()){
//            openNotification(typenoty!!,datanoty!!)
//        }

    }

    fun noInternetConnection() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.noconnection.visibility = View.VISIBLE
    }



    companion object {


        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }
}