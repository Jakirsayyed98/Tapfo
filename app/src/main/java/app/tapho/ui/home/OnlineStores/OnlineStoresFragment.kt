package app.tapho.ui.home.OnlineStores

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentOnlineStoresBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.MiniCash.Fragments.MiniCashActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.home.OnlineStoresAdapter.OnlineHeadlineAdapter
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.*
import app.tapho.ui.merchants.model.CustomeModel
import app.tapho.ui.model.*
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.utils.OPEN_WEB_VIEW
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat

class OnlineStoresFragment : BaseFragment<FragmentOnlineStoresBinding>(),ApiListener<HomeRes,Any?> ,RecyclerClickListener{

    private var popularList: ArrayList<Popular>? = null
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var appCategoryList: ArrayList<AppCategory>? = null
    private var headlineAdapter: OnlineHeadlineAdapter? = null


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
        _binding = FragmentOnlineStoresBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()


        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        callVideoModelClass()

      _binding!!.rupee.setOnClickListener {
          ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","0","Pending Rewards",null)
        }

        _binding!!.favouritesBtn.setOnClickListener {
            openContainer("favouritesBtn", "", false, "")
        }

        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }

        backpressedbtn()
        return _binding?.root
    }

    private fun backpressedbtn() {
        val  OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), OnBackPressedCallback)
    }

    private fun callVideoModelClass() {
        _binding!!.progressBar.visibility = View.VISIBLE
        _binding!!.mainLayout.visibility = View.GONE
        setRecyclerHeadline()
        setRecyclerBrand()
        getHomeViewmodel()
        getSuperTabData()
        setStaticLayout()
    }

    private fun setRecyclerHeadline() {
        headlineAdapter = OnlineHeadlineAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is AppCategory)
                    openContainer("MiniAppFragment", data, false, data.name)
                else if (data is MiniApp)
                    openWebView(data)
            }
        })
        binding.recyclerHeadline.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = headlineAdapter
        }
    }

    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()
        appCategoryList?.let { appCategory ->
//            if (appCategory.size > 10 && moreText == getString(R.string.more))
//                itemTypeAdapter?.addAllItem(appCategory.subList(0, 9).toList())
//            else
                itemTypeAdapter?.addAllItem(appCategory)
//                itemTypeAdapter?.addItem(AppCategory("", "", "", "", "", "", "", "", moreText, "", false, null))
        }
    }

    private fun getSuperTabData() {
        val topTabAdapter = TopTabAdapterLinkAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                when(data){
                    "Store Categories" -> {
                        startActivity(Intent(requireContext(), MiniCashActivity::class.java))
                    }
                    "Populars" -> {
                        openContainer("PopularPagesFragment", "", false, "")
                    }
                    "Daily Deals" -> {
                        ContainerActivity.openContainer(context, "DailyDealsFragment", "")
                    }
                    "News" -> {
                        ContainerActivity.openContainer(context, "News", "")
                    }
                    "Freebies" -> {
                        ContainerActivity.openContainer(requireContext(), "FreebiesFragment", "")
                    }
                    "Offers" -> {
                        ContainerActivity.openContainer(requireContext(), "OffersFragment", "")

                    }

                }
            }

        }).apply {
            addItem(CustomeSuperCategoryModel(R.drawable.online_popular_icon, "Popular Cashback ", "Populars",""))
            addItem(CustomeSuperCategoryModel(R.drawable.online_store_categories, "Store Categories", "Store Categories",""))
//            addItem(CustomeSuperCategoryModel(R.drawable.daily_product_icon, "Marketplace", "AllProductWithCategory","Deals"))
//            addItem(CustomeSuperCategoryModel(R.drawable.daily_product_icon, "News", "News",""))
//            addItem(CustomeSuperCategoryModel(R.drawable.daily_product_icon, "Daily Deals", "Daily Deals",""))
            addItem(CustomeSuperCategoryModel(R.drawable.offer_and_coupons_icon, "Offers", "Offers",""))
//            addItem(CustomeSuperCategoryModel(R.drawable.online_freebies_icon, "Freebies", "Freebies",""))


        }
        _binding!!.topTab.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = topTabAdapter
        }
    }

    private fun setRecyclerBrand() {
        itemTypeAdapter = ItemTypeAdapter(this)
        _binding!!.recyclerAppCategory.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = itemTypeAdapter
        }
    }

    private fun setStaticLayout() {
        val blockData = CustomeHowCBRedeemBloksAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {


            }
        }).apply {
            addItem(CustomeModel(1,"",R.drawable.new_step_1))
            addItem(CustomeModel(2,"",R.drawable.new_step_2))
            addItem(CustomeModel(3,"",R.drawable.new_step_3))
            addItem(CustomeModel(4,"",R.drawable.new_step_4))
            addItem(CustomeModel(5,"",R.drawable.new_step_5))
        }
        _binding!!.stepsRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter =blockData
        }
    }

    private fun getHomeViewmodel() {
        viewModel.getHomeData("home",getUserId(),this,this)
    }

    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()
        popularList?.let {
            list.addAll(it)
        }
        if (type == "1")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        else
            openContainer(getString(R.string.popular_merchants), list, true, getString(R.string.popular_merchants))
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun setPopularMerchants(list: ArrayList<Popular>) {
//        val mAdapter = homePagePopularStoreAdapter<Popular>(object : RecyclerClickListener {
        val mAdapter = homePagePopularStoreAdapter1<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                if (type == "MiniAppFragment")
                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is Popular) {
                    data.mini_app!!.let {
                        ActiveCashbackForWebActivity.openWebView(context, it.url, it.id, it.image, it.tcash, it.is_favourite,
                            data.cashback, it.app_category_id, it.url_type,it.name)
                    }
                }


            }
        })
        _binding!!.recommended.apply {
            layoutManager= GridLayoutManager(requireContext(),3,GridLayoutManager.HORIZONTAL,false)
//            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        mAdapter.clear()
        mAdapter.addAllItem(list)
//        mAdapter.addAllItem(if (list.size > 16) { list.subList(0, 16) } else { list })


    }

    private fun setPopularMerchants1(list: ArrayList<CashbackMerchant>) {
        val mAdapter = OnlinePagePopularStoreAdapter<CashbackMerchant>(this)
        _binding!!.popularApps.apply {
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = mAdapter
        }
        mAdapter.clear()
        mAdapter.addAllItem(if (list.size > 12) { list.subList(0, 12) } else { list })

    }

    private fun openWebView(data: MiniApp) {
        ActiveCashbackForWebActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id, data.url_type,data.name)
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {

        t!!.let {
            appCategoryList = it.app_category
            setAppCategory(getString(R.string.more))


            headlineAdapter!!.clear()
            it.headlines.let {
                headlineAdapter?.addAllItem(it!!)
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
                                val currentdate =TimePeriodDialog.getCurrentDate() //: Date = sdf.parse(Calendar.DAY_OF_MONTH.toString())
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

            it.popular?.let { popular ->
                setPopularMerchants2(popular)
            }

            it.banner_list2?.let { bannerList ->
                if (bannerList.isNullOrEmpty()){
                    _binding!!.banner2.visibility = View.GONE
                    _binding!!.tabLayout1.visibility = View.GONE
                }else{
                    val banner : ArrayList<BannerList2> = ArrayList()
                    banner.addAll(bannerList.shuffled())
                    setBannerAuto(bannerList)
                }
            }

            it.new_cashback_merchant.let {
                setPopularMerchants1(it!!)
            }


            _binding!!.progressBar.visibility = View.GONE
            _binding!!.mainLayout.visibility = View.VISIBLE

            it.popular!!.let {
                popularList = it
                setPopularMerchants(it)



            }
            it.popular!!.shuffled().let {
                setBannerLayouta1(it)
            }
            it.popular!!.shuffled().let {
                setBannerLayouta(it)
            }

        }
    }

    private fun setPopularMerchants2(list: ArrayList<Popular>) {
//        val mAdapter = popularitemAdapter<Popular>(object : RecyclerClickListener {
        val mAdapter = homePagePopularStoreAdapter1<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is Popular) {
                    data.mini_app!!.let {
                        ActiveCashbackForWebActivity.openWebView(context, it.url, it.id, it.image, it.tcash, it.is_favourite,
                            data.cashback, it.app_category_id, it.url_type,it.name)
                    }
                }
            }
        })
        _binding!!.recyclerPopularMerchants.apply {
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
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

    private fun setOnCustomeCrome(url: String, color: String) {
        val customIntent = CustomTabsIntent.Builder()
        customIntent.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
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

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
                    setAppCategory(getString(R.string.less))
                //    setFavourites(getString(R.string.less))
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
                //    setFavourites(getString(R.string.more))
                }
                else -> openContainer("SelectedCategoriesMiniAppsFragment", data, false, data.name)

                // else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp){
                openWebView(data)
            }

        }
    }

    private fun setBannerAuto(banners: ArrayList<BannerList2>) {

        if (banners.isNullOrEmpty()) {
            _binding!!.banner2.visibility = View.GONE
            _binding!!.tabLayout1.visibility = View.GONE
        } else {
            _binding!!.banner2.visibility = View.VISIBLE
            _binding!!.tabLayout1.visibility = View.VISIBLE
        }
        val imageList = ArrayList<SliderModelMain>()

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(x.image, x.url, x.id,x.type, "", ""))
            }

        }

        _binding!!.banner2.adapter = NewBannerDataAdapter(imageList, _binding!!.banner2, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if(data.toString().contains("http")){
                        setOnCustomeCrome(data.toString(), "")
                    }else{
                        getWebUrlData2(data.toString(),"2")
                    }
                }
            })
        _binding!!.banner2.clipToPadding = false
        _binding!!.banner2.clipChildren = false
        _binding!!.banner2.offscreenPageLimit = 3
        _binding!!.banner2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.banner2.setPadding(10, 0, 10, 0)

        //Handler Start
/*
        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                   kotlin.runCatching {
                       var i = _binding!!.banner9.currentItem

                       if (i == banners.size - 1) {
                           i = -1//0
                           _binding!!.banner9.currentItem = i
                       }
                       i++
                       _binding!!.banner9.setCurrentItem(i, true)
                       postDelayed(this, 2000)
                   }
                }
            }
            postDelayed(runnable, 2000)
        }
*/

        TabLayoutMediator(_binding!!.tabLayout1,_binding!!.banner2,false) { _,_ -> }.attach()
    }

    private fun setBanner1Auto(banners: ArrayList<BannerList>) {

        if (banners.isNullOrEmpty()) {
            _binding!!.banner1.visibility = View.GONE
            _binding!!.tabLayout.visibility = View.GONE
        } else {
            _binding!!.banner1.visibility = View.VISIBLE
            _binding!!.tabLayout.visibility = View.VISIBLE
        }
        val imageList = ArrayList<SliderModelMain>()

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(x.image, x.url, x.id,x.type, "", ""))
            }

        }

        _binding!!.banner1.adapter =
            NewBannerDataAdapter(imageList, _binding!!.banner1, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if(data.toString().contains("http")){
                        setOnCustomeCrome(data.toString(), "")
                    }else{
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

        TabLayoutMediator(_binding!!.tabLayout,_binding!!.banner1,false) { _,_ -> }.attach()
    }

    private fun setBannerLayouta(listofCB: List<Popular>) {
        if (listofCB.isNullOrEmpty()) {
            _binding!!.MiniAppBannerlayout.visibility = View.GONE
        } else {
            _binding!!.MiniAppBannerlayout.visibility = View.VISIBLE
        }
        val list : ArrayList<Popular> =ArrayList()
        list.addAll(if (listofCB.size>=10) listofCB.subList(0,8) else listofCB)
        val imageList = ArrayList<SliderModelMain>()
        for (x in list) {
            val image = x.image
            if (image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(image, x.mini_app!!.url, x.mini_app!!.id,x.mini_app!!.tcash, "", ""))
            }
        }


        _binding!!.MiniAppbanner.adapter = NewBannerDataAdapter1(imageList, _binding!!.MiniAppbanner, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is SliderModelMain){
                    ActiveCashbackForWebActivity.openWebView(requireContext(),data.url,data.id,"",data.tcash,"","","","","")
                }

            }
        })

        _binding!!.MiniAppbanner.clipToPadding = false
        _binding!!.MiniAppbanner.clipChildren = false
        _binding!!.MiniAppbanner.offscreenPageLimit = 3
        _binding!!.MiniAppbanner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.MiniAppbanner.setPadding(40, 0, 40, 0)

        //Handler Start


        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.MiniAppbanner.currentItem

                        if (i == imageList.size - 1) {
                            i = -1//0
                            _binding!!.MiniAppbanner.currentItem = i
                        }
                        i++
                        _binding!!.MiniAppbanner.setCurrentItem(i, true)
                        postDelayed(this, 5000)
                    }
                }
            }
            postDelayed(runnable, 5000)
        }

        TabLayoutMediator(_binding!!.MiniAppbannertabLayout,_binding!!.MiniAppbanner,false) { _,_ -> }.attach()
    }

    private fun setBannerLayouta1(listofCB: List<Popular>) {
        if (listofCB.isNullOrEmpty()) {
            _binding!!.MiniAppBannerlayout1.visibility = View.GONE
        } else {
            _binding!!.MiniAppBannerlayout1.visibility = View.VISIBLE
        }
        val list : ArrayList<Popular> =ArrayList()
        list.addAll(if (listofCB.size>=10) listofCB.subList(0,10) else listofCB)
        val imageList = ArrayList<SliderModelMain>()
        for (x in list) {
            val image = x.image
            if (image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(image, x.mini_app!!.url, x.mini_app!!.id,x.mini_app!!.tcash, "", ""))
            }
        }


        _binding!!.MiniAppbanner1.adapter = NewBannerDataAdapter1(imageList, _binding!!.MiniAppbanner1, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is SliderModelMain){
                    ActiveCashbackForWebActivity.openWebView(requireContext(),data.url,data.id,"",data.tcash,"","","","","")
                }
            }
        })

        _binding!!.MiniAppbanner1.clipToPadding = false
        _binding!!.MiniAppbanner1.clipChildren = false
        _binding!!.MiniAppbanner1.offscreenPageLimit = 3
//        _binding!!.banner9.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//        _binding!!.banner9.setPadding(10, 0, 10, 0)

        _binding!!.MiniAppbanner1.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.MiniAppbanner1.setPadding(40, 0, 40, 0)

        //Handler Start

        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.MiniAppbanner1.currentItem

                        if (i == imageList.size - 1) {
                            i = -1//0
                            _binding!!.MiniAppbanner1.currentItem = i
                        }
                        i++
                        _binding!!.MiniAppbanner1.setCurrentItem(i, true)
                        postDelayed(this, 6000)
                    }
                }
            }
            postDelayed(runnable, 6000)
        }

        TabLayoutMediator(_binding!!.MiniAppbannertabLayout1,_binding!!.MiniAppbanner1,false) { _,_ -> }.attach()
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            OnlineStoresFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}