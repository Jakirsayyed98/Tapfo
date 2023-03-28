package app.tapho.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.fragment.app.DialogFragment
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
import app.tapho.databinding.FragmentHomeTabBinding
import app.tapho.databinding.FragmentWelcomebackscreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.BaseRes
import app.tapho.network.Status
import app.tapho.ui.*
import app.tapho.ui.Cabs.CabsParentPageFragment
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.RechargeService.ModelData.RechargeStatus.checkRechargeStatusRes
import app.tapho.ui.ScanAndPay.ScanAndPayContainerActivity
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.favourite.FavouriteDialogFragment
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter2
import app.tapho.ui.home.adapter.*
import app.tapho.ui.localbizzUI.LocalBizSplashActivity
import app.tapho.ui.model.*
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.scanner.NewScannerActivity
import app.tapho.ui.scanner.ScanCart.ContainerForProductActivity
import app.tapho.ui.scanner.scanner
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import app.tapho.viewmodels.FavouriteViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.erkutaras.showcaseview.ShowcaseManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.kakyiretechnologies.appreview.reviewApp
import java.text.SimpleDateFormat
import java.util.*


class HomeTabFragment : BaseFragment<FragmentHomeTabBinding>(), RecyclerClickListener,
    ConnectivityListener {

    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CODE_SPEECH_INPUT = 1
    var tcashdashboard: TCashDasboardRes? = null

    //In App Update
    var moreText = "Show more"
    private var inappupdate: AppUpdateManager? = null
    private val inAppUpdateRequest_Code = 1010
    private var itemTypeAdapter: NewItemTypeAdapter<AppCategory>? = null
    private var itemTypeAdapter1: NewItemTypeAdapter<AppCategory>? = null
    private var Superlinks: NewItemTypeAdapter<SuperCategory>? = null

    private var appCategoryList: ArrayList<AppCategory>? = null
    private var popularList: ArrayList<Popular>? = null
    private var favViewModel: FavouriteViewModel? = null
    var customShopCategory: SuperLinkAdapter2? = null
    lateinit var tcash: TCashDasboardRes

    fun setConnectivityListener(listener: ConnectivityListener) {
        ConnectionReceiver.connectivityListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeTabBinding.inflate(inflater, container, false)

//        welcomebackscreenFragment.newInstance(appCategoryList).show(childFragmentManager, null)

        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        statusBarColor(R.color.lightblack)
        statusBarTextBlack()

        checkConnection()

        //In App Review Code
        reviewApp(5)
        inappupdate = AppUpdateManagerFactory.create(requireContext())
        InAppUpdate()
        _binding!!.androidV.text = "App Version - " + BuildConfig.VERSION_NAME + " 30.12"


        _binding!!.swipeRefresh.setOnRefreshListener {
            _binding!!.swipeRefresh.isRefreshing = true
            callVideoModelClass()
            _binding!!.swipeRefresh.isRefreshing = false
        }


        setHomeTabUpLayout()
        getRecentMiniappList()
        val deviceData = requireContext().getSystemDetail()

        return _binding?.root
    }

    private fun setHomeTabUpLayout() {
        createShortCuts()
        _binding!!.Greeting.text = "Hey, " + getGreetingMessage()

        _binding!!.favouritesBtn.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "favouritesBtn", "", false, "")
        }

        _binding!!.reProfile.setOnClickListener {
            ContainerActivity.openContainer(context, "ProfileDetailFragment", tcashdashboard)
        }

        _binding!!.scanner.setOnClickListener {
//            startActivity(Intent(requireContext(), scanner::class.java))
            permissionTaking()
        }
    }

    private fun createShortCuts() {
        val intent: Intent = Intent(requireContext(), scanner::class.java)
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


    }

    fun noInternetConnection() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.noconnection.visibility = View.VISIBLE
    }

    private fun setMoreClicks() {
//        setMoreTextSuperLinks("Show less")
        _binding!!.TapfoPlay.setOnClickListener {
            if (getSharedPreference().getString("gameIntro").isNullOrEmpty()) {
                ContainerActivity.openContainer(requireContext(), "GamesOneTimeSplash", "")
            } else {
                ContainerActivity.openContainer(requireContext(), "Games", "")
            }
        }

        _binding!!.TapfoFi.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "TapfoFiHomeFragment", "")
        }

        _binding!!.pendingLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(
                requireContext(),
                "TcashrewardsFragment",
                "0",
                "Pending Rewards",
                tcashdashboard
            )
        }

        _binding!!.endtoendencription.setOnClickListener {
            openContainer("EndToEndEncriptionFragment", "", false, "")
        }


        _binding!!.verifiedLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(
                requireContext(),
                "TcashrewardsFragment",
                "1",
                "Total Rewards",
                tcashdashboard
            )
        }

        _binding!!.mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast.makeText(context, " " + e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                when (result!!.get(0)) {
                    "mobile recharge" -> {
                        screenredirection("MobileRecharge")
                    }
                    "recharge" -> {
                        screenredirection("MobileRecharge")
                    }
                    "games" -> {
                        screenredirection("900+ Games")
                    }
                    else -> {
                        viewModel.searchMiniApp(
                            getUserId(),
                            result.get(0).toString(),
                            this,
                            object : ApiListener<WebTCashRes, Any?> {
                                override fun onSuccess(t: WebTCashRes?, mess: String?) {
                                    t!!.data.let {
                                        if (it.isNullOrEmpty().not()) {
                                            it.get(0).let {
                                                ActiveCashbackForWebActivity.openWebView(
                                                    requireContext(),
                                                    it.url, it.id, it.image,
                                                    it.tcash,
                                                    it.is_favourite,
                                                    it.cashback,
                                                    it.app_category_id,
                                                    it.url_type,
                                                    it.name
                                                )
                                            }
                                        } else {
                                            requireContext().customToast("No App Found", true)

                                        }
                                    }

                                }

                            })
                    }
                }
            }
        }
    }

    private fun permissionTaking() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        } else {
//            ScanAndPayContainerActivity.openContainer(requireContext(),"EnterAmountForSend","textData","","")
//            startActivity(Intent(requireContext(), NewScannerActivity::class.java))
            startActivity(Intent(requireContext(), scanner::class.java))
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    fun openNotification(app_id: String, tag: String, screen_url: String) {
        getSharedPreference().saveString("app_id", "")
        getSharedPreference().saveString("tag", "")
        getSharedPreference().saveString("screen_url", "")

        if (screen_url.isNullOrEmpty().not()) {
            when (screen_url) {
                "com.tapfo.redirectScreen.Games" -> {
                    getGamesData(app_id)
                }
                "com.tapfo.ui.WebviewActivity" -> {
                    getIntoMiniApps(app_id, "3")
                }
                "com.tapfo.ui.ContainerActivity" -> {
                    ContainerActivity.openContainer(requireContext(), tag, "")
                }
                "com.tapfo.ui.home.WebViewActivityForOffer" -> {
                    getIntoMiniApps(app_id, "4")
                }
                else -> {

                }
            }
        }


    }

    private fun getGamesData(gameId: String?) {
        viewModel.getAllGames(getUserId(), "", "", "", this, object : ApiListener<Games, Any?> {
            override fun onSuccess(t: Games?, mess: String?) {
                t.let {
                    it!!.data.forEach {
                        if (it.id.equals(gameId)) {
                            GameWebViewActivity.openWebView(
                                requireContext(),
                                it.url,
                                it.id,
                                it.name,
                                it.assets.square,
                                ""
                            )
                        }
                    }
                }
            }
        })
    }

    private fun getIntoMiniApps(MiniAppId: String, type: String) {
        viewModel.searchMiniApp(
            getUserId(),
            MiniAppId,
            this,
            object : ApiListener<WebTCashRes, Any?> {
                override fun onSuccess(t: WebTCashRes?, mess: String?) {
                    t!!.let {
                        it.data.get(0).let {
                            if (type.equals("3")) {
                                ActiveCashbackForWebActivity.openWebView(
                                    requireContext(),
                                    it.url,
                                    it.id,
                                    it.image,
                                    it.tcash,
                                    it.is_favourite,
                                    it.cashback,
                                    it.app_category_id,
                                    it.url_type,
                                    it.name
                                )
                            } else {
                                WebViewActivityForOffer.openWebView(
                                    requireContext(),
                                    it.url,
                                    it.id,
                                    it.image,
                                    it.tcash,
                                    it.is_favourite,
                                    it.cashback,
                                    it.app_category_id
                                )
                            }
                        }
                    }
                }
            })
    }

    private fun callVideoModelClass() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                synchronized(this@HomeTabFragment) {
                    handler.postDelayed(object : Runnable {
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
                    }, 35000)

                }

            }
        }
        val thread = Thread(runnable)
        thread.start()
        progressVISIBLE()
        getData()

        setRecyclerBrand()
        setRecyclerForSuperLinks()
        setFavourites(getString(R.string.more))
        setMoreClicks()
//        _binding!!.Greeting.text ="Hey, "+ getGreetingMessage()

    }

    private fun checkConnection() {
        val isConnected = ConnectionReceiver.isConnected(requireContext())

        if (isConnected) {
            callVideoModelClass()
        } else {
            noInternetConnection()
        }

        _binding!!.retryinternetButton.setOnClickListener {
            checkConnection()
        }

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            callVideoModelClass()
        } else {
            noInternetConnection()
        }
    }

    private fun tcashhistoryDataViewModel1() {

        viewModel.getTCashDashboard(
            getUserId(),
            TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),
            "2",
            this,
            object : ApiListener<TCashDasboardRes, Any?> {
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t!!.let {
                        _binding!!.pendingbalance.text = withSuffixAmount(it.pending.toString())
                        _binding!!.availbalance.text =
                            withSuffixAmount(it.cash_available.toString())
                        _binding!!.lifetimeearning.text =
                            withSuffixAmount(it.lifetime_earning.toString())
                        tcashdashboard = it
                        _binding!!.cashbackCard.setOnClickListener { click ->
                            ContainerActivity.openContainer(context, "cashbackcard", it, false, "")
                        }

                    }


                }
            })

    }

    private fun InAppUpdate() {
        inappupdate!!.appUpdateInfo.addOnSuccessListener { AppUpdateInfo ->
            kotlin.runCatching {
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
    }

    private fun inAppUpdateProgress() {
        inappupdate!!.appUpdateInfo.addOnSuccessListener { AppUpdateInfo ->
            kotlin.runCatching {
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
    }

    private fun blockUser() {
        BlockUserDialogFragment.newInstance(null).show(childFragmentManager, null)
    }

    private fun updateRechargeStatus() {
        viewModel.checkRechargeStatus(
            getUserId(),
            this,
            object : ApiListener<checkRechargeStatusRes, Any?> {
                override fun onSuccess(t: checkRechargeStatusRes?, mess: String?) {

                }
            })
    }

    private fun getNotificationData() {
        viewModel.getAllNotification(
            getUserId(),
            this,
            object : ApiListener<AllNotificationRes, Any?> {
                override fun onSuccess(t: AllNotificationRes?, mess: String?) {
                    val data: java.util.ArrayList<app.tapho.ui.model.AllNotification.Data> =
                        java.util.ArrayList()
                    t.let {
                        it!!.data.forEach {
                            if (it.noti_type.equals("1")) {
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
        if (getSharedPreference().getString("NotificationStatusID").toString()
                .equals(data.get(0).id).not()
        ) {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.cashback_rule_popup, null)
            dialog.setCancelable(true)
            //  val AllTransaction: TextView = view.findViewById(R.id.viewAllTransaction)
            val AllTransaction: AppCompatButton = view.findViewById(R.id.viewAllTransaction)
            val iconS: ImageView = view.findViewById(R.id.iconS)
            val imageIv: ImageView = view.findViewById(R.id.imageIv)
            val discription: TextView = view.findViewById(R.id.discription)
            val cashback: TextView = view.findViewById(R.id.cashback)
            getSharedPreference().saveString("NotificationStatusID", data.get(0).id)
            Glide.with(requireContext()).load(data.get(0).offer_data.get(0).image).circleCrop()
                .into(iconS)
            Glide.with(requireContext()).load(R.drawable.app_icon).circleCrop().into(imageIv)
            discription.text =
                "Your Cashback has been tracked\nfrom " + data.get(0).offer_data.get(0).name
            val commision = data.get(0).merchant_postback_value.get(0).user_commision
            cashback.text = "+" + withSuffixAmount(commision)
            dialog.setContentView(view)
            dialog.show()
            AllTransaction.setOnClickListener {
                ContainerActivity.openContainerforPointScreen(
                    requireContext(),
                    "TcashrewardsFragment",
                    "0",
                    "Pending Rewards",
                    tcashdashboard
                )
                dialog.dismiss()
            }
        } else {

        }
    }

    private fun getData() {
        getSharedPreference().saveString("StartLoading", "0")
        viewModel.getHomeData("home", getUserId(), this, object : ApiListener<HomeRes, Any?> {
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t?.let {
                    getSharedPreference().saveString("StartLoading", "1")
                    showHome()

                    val app_id = getSharedPreference().getString("app_id")
                    val tag = getSharedPreference().getString("tag")
                    val screen_url = getSharedPreference().getString("screen_url")
                    openNotification(app_id!!.toString(), tag.toString(), screen_url.toString())

                    popularList = it.popular

                    _binding!!.search.setOnClickListener {click->
                        openAllPopularCashbackMerchants("1",it.popular!!)
                    }

                    _binding!!.AddMoreFavTV.setOnClickListener {click->
                        FavouriteDialogFragment.newInstance(it.app_category).show(childFragmentManager, null)
                    }


                    Superlinks!!.addAllItem(it.super_category!!)
                    if (it.super_category.isNullOrEmpty().not()) {
                        _binding!!.superlinkLayout.visibility = View.VISIBLE
                    } else {
                        _binding!!.superlinkLayout.visibility = View.GONE
                    }

                    appCategoryList = it.app_category
                    setAppCategory(getString(R.string.more))

                    it.banner_list2.let { bannerList ->
                        val banners: java.util.ArrayList<BannerList2> = java.util.ArrayList()
                        bannerList!!.forEach {
                            if (it.image.isNullOrEmpty().not()) {
                                if (it.expiry_date.toString().isNullOrEmpty().not()) {
                                    val sdf = SimpleDateFormat("MM/dd/yyyy")
                                    val currentdate =
                                        TimePeriodDialog.getCurrentDate() //: Date = sdf.parse(Calendar.DAY_OF_MONTH.toString())
                                    val expiredate =
                                        it.expiry_date // : Date = sdf.parse(it.expiry_date.toString())
                                    if (expiredate != null) {
                                        if (expiredate > currentdate) {
                                            banners.add(it)
                                        } else {

                                        }
                                    }

                                } else {
                                    banners.add(it)
                                }
                            }
                        }

                        if (banners.isNullOrEmpty()) {
                            _binding!!.bannerPromoted.visibility = View.GONE
//                            _binding!!.tabLayout1.visibility = View.GONE
                        } else {
                            setPromotedBannerAuto(banners)
                        }

                    }

                    it.popular?.let {
                        val miniapps: java.util.ArrayList<Popular> = java.util.ArrayList()
                        val miniapps2: java.util.ArrayList<Popular> = java.util.ArrayList()

                        it.forEach {
                            if (it.image.isNullOrEmpty().not()) {
                                miniapps.add(it)
                            }
                        }


                        if (miniapps.size >= 4) {
                            miniapps2.addAll(miniapps.subList(0, 4))
                            setpopularMiniBanner(miniapps2)
                        } else {
                            setpopularMiniBanner(miniapps)
                        }

                    }

                    it.popular?.let { popular ->
                        setPopularMerchants(popular)
                    }

                    it.promoted_app.let {
                        setPromotedApps(it!!)
                    }

                    it.profile_detail!!.get(0).let {
                        if (it.recharge_request_pending.toDouble() > 0) {
                            updateRechargeStatus()
                        }
                    }

                    it.profile_detail!!.get(0).let {
                        if (it.status.equals("0")) {
                            blockUser()
                        }
                    }

                }

            }
        })
    }

    //Recent Miniapp
    private fun getRecentMiniappList() {
        viewModel.getMiniRecentData().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it!!.data.let { t ->
                        t!!.let {
                            setRecentMinisMerchants(it.data)
                        }
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }

            }
        })
        getMiniappRecentList()
    }

    private fun getMiniappRecentList() {
        viewModel.getMiniRecentData(getUserId())
    }

    fun refreshGameRecentData() {
        getMiniappRecentList()
    }

    private fun setRecentMinisMerchants(list: ArrayList<MiniApp>) {
        val mAdapter = RecentMinisAdapter<MiniApp>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                   // openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    data.let {
                        ActiveCashbackForWebActivity.openWebView(
                            context,
                            it.url,
                            it.id,
                            it.image,
                            it.tcash,
                            it.is_favourite,
                            data.cashback,
                            it.app_category_id,
                            it.url_type,
                            it.name
                        )
                    }
                }
            }
        })
        _binding!!.RecentRV.apply {
            layoutManager = GridLayoutManager(context, 4)
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = mAdapter
        }
        mAdapter.clear()
        mAdapter.addAllItem(
            if (list.size > 8) {
                list.subList(0, 8)
            } else {
                list
            }
        )

        if (list.isNullOrEmpty().not()) {
            _binding!!.recentLayout.visibility = View.VISIBLE
        } else {
            _binding!!.recentLayout.visibility = View.GONE
        }
    }

    fun openAllPopularCashbackMerchants(type: String,list : ArrayList<Popular>) {
//        val list = java.util.ArrayList<Popular>()
//        popularList?.let {
//            list.addAll(it)
//        }
        if (type == "1")
        //  openContainer("MiniAppSearch", list, false, "data.name")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        //    SearchFragment.showSearch(childFragmentManager, list)
        else
            openContainer(getString(R.string.popular_merchants), list, true, getString(R.string.popular_merchants))
    }

    private fun setPromotedApps(list: ArrayList<PromotedApp>) {
        val mAdapter = SponsoredAdapter<PromotedApp>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                if (data.toString().contains("http")) {
                    setOnCustomeCrome(data.toString())
                } else {
                    screenredirection(data.toString())
                }
            }
        })
        _binding!!.recyclerPromotedApps.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = mAdapter
        }
        mAdapter.clear()
        if (list.isNullOrEmpty().not()) {
            _binding!!.promotedsection.visibility = View.VISIBLE
        } else {
            _binding!!.promotedsection.visibility = View.GONE
        }
        mAdapter.addAllItem(
            if (list.size > 12) {
                list.subList(0, 12)
            } else {
                list
            }
        )
    }

    private fun setOnCustomeCrome(
        url: String
    ) {
        val customIntent = CustomTabsIntent.Builder()
        customIntent.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.white))
        customIntent.setShowTitle(true)
        customIntent.setDefaultShareMenuItemEnabled(false)
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

    private fun setPopularMerchants(list: java.util.ArrayList<Popular>) {
        val mAdapter = popularitemAdapter<Popular>(object : RecyclerClickListener {
            //        val mAdapter = homePagePopularStoreAdapter<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//                if (pos.equals(1)){
//                    if (data is Popular){
//                        checkFavOrNot(data)
//                    }
//
//                }else{
                if (type == "MiniAppFragment")
//                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    data.let {
                        ActiveCashbackForWebActivity.openWebView(
                            context, it.url, it.id, it.image, it.tcash, it.is_favourite,
                            data.cashback, it.app_category_id, it.url_type, it.name
                        )
                    }
                }
//                }
            }
        })
        _binding!!.recyclerPopularMerchants.apply {
//            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            layoutManager = GridLayoutManager(requireContext(), 4)
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

    private fun setpopularMiniBanner(banners: java.util.ArrayList<Popular>) {

        if (banners.isNullOrEmpty()) {
            _binding!!.popularMinibanner.visibility = View.GONE
//            _binding!!.popularMinitabLayout.visibility = View.GONE
        } else {
            _binding!!.popularMinibanner.visibility = View.VISIBLE
//            _binding!!.popularMinitabLayout.visibility = View.VISIBLE
        }

        val imageList = java.util.ArrayList<SliderModelMain2>()
        //   bannerlist4 = bannerList4

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()) {
                imageList.add(
                    SliderModelMain2(
                        x.image,
                        x.mini_app!!.image,
                        x.mini_app!!.url,
                        x.mini_app!!.id,
                        x.mini_app!!.tcash,
                        x.mini_app!!.is_favourite,
                        x.cashback
                    )
                )
            }
        }


        _binding!!.popularMinibanner.adapter = PopularBannerDataAdapter(
            imageList,
            _binding!!.popularMinibanner,
            object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if (data is SliderModelMain2) {
                        data.let {
                            ActiveCashbackForWebActivity.openWebView(
                                requireContext(),
                                it.url,
                                it.id,
                                it.icon,
                                it.tcash,
                                it.fav,
                                it.cashback,
                                "",
                                "1",
                                ""
                            )
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


        TabLayoutMediator(
            _binding!!.popularMinitabLayout,
            _binding!!.popularMinibanner,
            false
        ) { _, _ -> }.attach()
    }

    private fun setPromotedBannerAuto(banners: java.util.ArrayList<BannerList2>) {

        if (banners.isNullOrEmpty()) {
            _binding!!.bannerPromoted.visibility = View.GONE
//            _binding!!.tabLayout1.visibility = View.GONE
        } else {
            _binding!!.bannerPromoted.visibility = View.VISIBLE
//            _binding!!.tabLayout1.visibility = View.VISIBLE
        }

        val imageList = java.util.ArrayList<SliderModelMain>()

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()) {
                imageList.add(SliderModelMain(x.image, x.url, x.id, x.type, "", ""))
            }

        }
        _binding!!.bannerPromoted.adapter =
            NewBannerDataAdapter(
                imageList,
                _binding!!.bannerPromoted,
                object : RecyclerClickListener {
                    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                        if (data.toString().equals("mobile recharge")) {
                            screenredirection(data.toString())
                        } else {
                            getWebUrlData2(data.toString(), "2")
                        }


                    }
                })


        _binding!!.bannerPromoted.clipToPadding = false
        _binding!!.bannerPromoted.clipChildren = false
        _binding!!.bannerPromoted.offscreenPageLimit = 3
        _binding!!.bannerPromoted.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.bannerPromoted.setPadding(10, 0, 10, 0)

        //Handler Start
        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.bannerPromoted.currentItem

                        if (i == banners.size - 1) {
                            i = -1//0
                            _binding!!.bannerPromoted.currentItem = i
                        }
                        i++
                        _binding!!.bannerPromoted.setCurrentItem(i, true)
                        postDelayed(this, 4000)
                    }
                }
            }
            postDelayed(runnable, 4000)
        }


        TabLayoutMediator(
            _binding!!.tabLayout1,
            _binding!!.bannerPromoted,
            false
        ) { _, _ -> }.attach()
    }

    private fun getWebUrlData2(s: String, type: String) {
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
                ActiveCashbackForWebActivity.openWebView(
                    requireContext(),
                    url,
                    id,
                    icon,
                    tcash,
                    fav,
                    cashback,
                    appcategory,
                    type,
                    name
                )
            }

        })
    }

    override fun onResume() {
        super.onResume()
        setConnectivityListener(this)
        tcashhistoryDataViewModel1()
        refreshGameRecentData()
        inAppUpdateProgress()


        val loginData = getSharedPreference().getLoginData()
//        HomeFragment.UserId = loginData?.unique_user_id.toString()
        if (loginData?.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = loginData!!.name!!.replaceAfter(" ", "")
        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(loginData?.image)
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon))
                .into(_binding!!.profileIv)
        }

        _binding!!.name.text =
            requireContext().CamelCaseValue(loginData?.name.toString()).replaceAfter(" ", "")
    }

    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()

        appCategoryList?.let { appCategory ->
            if (appCategory.size > 8 && moreText == getString(R.string.more))
                itemTypeAdapter?.addAllItem(appCategory.subList(0, 7).toList())
            else
                itemTypeAdapter?.addAllItem(appCategory)
            itemTypeAdapter?.addItem(
                AppCategory(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    moreText,
                    "",
                    false,
                    null
                )
            )


        }
    }

    private fun setRecyclerBrand() {
        itemTypeAdapter = NewItemTypeAdapter(this)
        _binding!!.recyclerAppCategory.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = itemTypeAdapter
        }
    }

    private fun setRecyclerForSuperLinks() {
        Superlinks = NewItemTypeAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is SuperCategory) {
                    if (data.screen_url.isNullOrEmpty().not()) {
                        screenredirection(data.screen_url.toString())
                    }

                }

            }
        })
        _binding!!.SuperLinksRV.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = Superlinks
        }
    }

    private fun screenredirection(data: String) {
        when (data) {

            "CabsParentPageFragment" -> {
                ContainerActivity.openContainer(requireContext(), "CabsParentPageFragment", "")
            }

            "QRScanner" -> {
                permissionTaking()
            }

            "TapfoFood" -> {
                TapfoFoodContainerActivity.openContainer(
                    requireContext(),
                    "TapfoFoodLocationFragment",
                    ""
                )
            }
            "MyMini" -> {
                ContainerActivity.openContainer(requireContext(), "NewHomeFragment", "")
            }

            "Pending Rewards" -> {
                ContainerActivity.openContainerforPointScreen(
                    requireContext(),
                    "TcashrewardsFragment",
                    "0",
                    "Pending Rewards",
                    tcashdashboard
                )
            }

            "Total Rewards" -> {
                ContainerActivity.openContainerforPointScreen(
                    requireContext(),
                    "TcashrewardsFragment",
                    "1",
                    "Total Rewards",
                    tcashdashboard
                )
            }

            "addtopup" -> {
                getSharedPreference().saveString("wallet_cashback", "0")
                ContainerActivity.openContainerForVoucher(
                    context,
                    "addtopup",
                    "",
                    "",
                    "",
                    tcashdashboard
                )
            }

            "OnlineStores" -> {
                ContainerActivity.openContainer(requireContext(), "OnlineStores", "")
            }
            "Offers" -> {
                ContainerActivity.openContainer(requireContext(), "OffersFragment", "")
            }
            "MovieTicket" -> {
                getIntoMiniApps("81", "2")
            }

            "LiveScore" -> {
                getIntoMiniApps("325", "2")
            }

            "ReferAndEarn" -> {
                ContainerActivity.openContainer(
                    requireContext(),
                    "referandearnscreen",
                    tcashdashboard
                )

            }

            "Scan & Pay" -> {
                ContainerActivity.openContainer(requireContext(), "ScanAndPayIntroFragment", "")
            }

            "BillsAndRecharge" -> {
//                getSharedPreference().saveString("servicetype","1")
//                ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
                RechargeServiceActivity.openRechargeService(requireContext())
            }
            "MobileRecharge" -> {
                getSharedPreference().saveString("servicetype", "1")
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

            else -> {

            }

        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
//                    setAppCategory(getString(R.string.less))
                    setBottomSheetForCategories(appCategoryList)

//                    setFavourites(getString(R.string.less))
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
//                    setFavourites(getString(R.string.more))
                }
                else -> openContainer("SelectedCategoriesMiniAppsFragment", data, false, data.name)

//                 else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp) {
                openWebView(data)
            }

        }
    }

    @SuppressLint("MissingInflatedId")
    private fun setBottomSheetForCategories(appCategoryList: ArrayList<AppCategory>?) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.full_category_layout, null)
        dialog.setCancelable(true)

        val recyclerAppCategory: RecyclerView = view.findViewById(R.id.recyclerAppCategorybottom)
        val down: ImageView = view.findViewById(R.id.down)
        down.setOnClickListener {
            dialog.dismiss()
        }

        itemTypeAdapter1 = NewItemTypeAdapter(this)
        recyclerAppCategory.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = itemTypeAdapter1
        }
        itemTypeAdapter1!!.addAllItem(appCategoryList!!)




        dialog.setContentView(view)
        dialog.show()
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)

    }

    private fun openWebView(data: MiniApp) {
        ActiveCashbackForWebActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id,
            data.url_type,
            data.name
        )
    }

    @SuppressLint("LogConditional")
    private fun setFavourites(moreText: String) {
        val mAdapter = HomeFavAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp) {
                    openWebView(data)
                }
            }
        })
        _binding!!.recyclerFavourites.apply {
//            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            layoutManager = GridLayoutManager(context, 4)
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
                it.data?.let { it1 -> mAdapter.addAllItem(it1) }

//                it.data?.let { it1 ->
//                    if (it1.size > 8 && moreText == getString(R.string.more)){
//                        mAdapter.addAllItem(it1.subList(0, 9).toList())
//                    } else {
//                        mAdapter.addAllItem(it1)
//                    }
//                }

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


    companion object {
        @JvmStatic
        fun newInstance() =
            HomeTabFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}