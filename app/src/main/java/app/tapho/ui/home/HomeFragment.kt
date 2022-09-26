package app.tapho.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.databinding.FragmentHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.favourite.FavouriteDialogFragment
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.home.ShopProduct.ShopAdapter.ShopProducatAdapter
import app.tapho.ui.home.adapter.*
import app.tapho.ui.merchants.MerchantOfferDetailFragment
import app.tapho.ui.merchants.MerchantsAllListDialogFragment
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.*
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.scanner.scanner
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import app.tapho.viewmodels.FavouriteViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.common.collect.Lists
import com.kakyiretechnologies.appreview.reviewApp
import java.net.URLDecoder
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment<FragmentHomeBinding>(), ApiListener<HomeRes, Any?>,
    RecyclerClickListener {
    private var partnerCashbackAdapter: CashbackPartnerAdapter<NewCashback>? = null
    private var shopProducatAdapter: ShopProducatAdapter<Data>? = null
    private var topIconAdapter: TopIconAdapter<NewCashback>? = null
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var headlineAdapter: HeadlineAdapter? = null
    private val bannerAdapter1 = BannerAdapter()
    private val bannerAdapter2 = BannerAdapter()
    private val mBannerAdapter = BannerOfferAdapter()
    private val bannerAdapter4 = BannerAdapter()
    private val bannerAdapter5 = BannerAdapter()
    private var cashbackMerchantFragmentAdapter: PagerFragmentAdapter? = null
    private var appCategoryList: ArrayList<AppCategory>? = null
    private var headlinesList: ArrayList<AppCategory>? = null
    private var popularList: ArrayList<Popular>? = null
    private var miniApp: ArrayList<MiniApp>? = null
    private var bannerlist1: ArrayList<BannerList>? = null
    private var bannerlist2: ArrayList<BannerList>? = null
    private var bannerlist3: ArrayList<BannerList>? = null
    private var bannerlist4: ArrayList<BannerList>? = null
    private var service: ArrayList<Service>? = null
    private var favViewModel: FavouriteViewModel? = null
    private var inTheSpotLight: InTheSpotLightAdapter<BannerList>? = null
    private var lastTransaction: LasttransactionData<TCashDashboardData>? = null
    private var newPartnerAdapter: NewPartnerAdapter<CashbackMerchant>? = null//
    private var exculsivecashbackAdapter: ExculsivecashbackAdapter<Popular>? = null//
    private var AdsAdapter: AdsAdapter<BannerList>? = null//
    private var Banner6Adapter: AdsAdapter<BannerList>? = null//
    private var Banner2Adapter: Banner2Adapter<BannerList>? = null//
    private var sponsored: SponsoredAdapter<PromotedApp>? = null//
    private var ShopAllProduct: ShopAllProductAdapter<Data>? = null
    private var mAdapter: HomePageOfferListAdapter? = null
    var idCreatedAt = ""

    val REQUEST_CODE = 11
    val sliderHandler = Handler()
    val sliderHandler2 = Handler()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        statusBarColor(R.color.orange1)
        // statusBarTextWhite()
        statusBarTextBlack()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        //setBanner1()

        //   activity?.onBackPressed()


        setRecyclerCashbackPartner()
        //ShopProduct()
        getShop()
        topicon()
        setRecyclerBrand()
        setRecyclerOffer()
//        setCashBackBanner()
//        setCashBackBanner4()
        setRecyclerHeadline()
        //    setCashBackBanner5()
        //  addFragment(Fragment_forYou.newInstance())
        setCashbackMerchantsPager()
        getData()
        inTheSpotLight()
        LastTransaction()
        //  setFavourites(getString(R.string.more))
        setMoreClicks()
        ShopProduct()
        AllShopProduct()
        exculsiveCashback()
        Banner5()
        Banner6()
        Banner7()
        promotedApps()
        getShopAllProduct()
        NewPartner()
        showcaseBuilder()
        offerforYou()
        getNewHomeResData()
        //In App Review Code
        reviewApp(1)


        //Update Dialog Code
        val appUpdateManager = AppUpdateManagerFactory.create(requireContext())
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    FLEXIBLE,
                    requireActivity(),
                    REQUEST_CODE
                )
            }
        }

        //   tcashhistoryDataViewModel(TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(), getString(R.string.this_month))


        binding.allMerchantsTv.setOnClickListener {
            MerchantsAllListDialogFragment().show(childFragmentManager, null)
        }
        return _binding?.root
    }

    private fun tcashhistoryDataViewModel1(idCreatedAt: String, s: String, string: String) {
        getSharedPreference().getUserId().let {
            viewModel.getTCashDashboard(
                getUserId(),
                idCreatedAt,
                s,
                this,
                object : ApiListener<TCashDasboardRes, Any?> {
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                        val transaction = t?.data
                        t?.data.let {

                            lastTransaction?.addAllItem(
                                if (it!!.size > 4) it.subList(0, 4) else it
                            )

                            //     lastTransaction?.addAllItem(it!!)
                        }
                        if (transaction!!.isEmpty()) {
                            binding.noTransaction.visibility = View.VISIBLE
                        }
                    }
                })
        }
    }

    private fun getNewHomeResData() {
        viewModel.getHomeDataNew(
            "home",
            getUserId(),
            this,
            object : ApiListener<HomeResData, Any?> {
                override fun onSuccess(t: HomeResData?, mess: String?) {
                    t?.profile_detail?.get(0).let {
                        idCreatedAt = it!!.created_at
                        tcashhistoryDataViewModel1(
                            idCreatedAt,
                            "2022-04-15",
                            getString(R.string.this_month)
                        )
                    }
                    t!!.headlines.let {
                        headlineAdapter?.addAllItem(it)
                    }

                }

            })
    }

    private fun offerforYou() {
        mAdapter = HomePageOfferListAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is OfferData)
                    openDialogredem(data)
            }

        })
        binding.offers.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            //  layoutManager=GridLayoutManager(context,2)
            //   layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun openDialogredem(data: OfferData) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.offerbottom_sheet, null)
        val partnerIv: ImageView = view.findViewById(R.id.partnerIV);
        val name: TextView = view.findViewById(R.id.name);

        val couponsAndOffer: TextView = view.findViewById(R.id.couponsTv);
        val ExpireDateTV: TextView = view.findViewById(R.id.ExpireDateTV);
        val grabTV: TextView = view.findViewById(R.id.grabTV);
        val grabAdditional: TextView = view.findViewById(R.id.grabAdditional);
        val coupons: TextView = view.findViewById(R.id.coupons);
        val copybtn: TextView = view.findViewById(R.id.copybtn);
        val peopleUsed: TextView = view.findViewById(R.id.peopleUsed);
        val validitydaysleft: TextView = view.findViewById(R.id.validitydaysleft);
        val details: TextView = view.findViewById(R.id.details);
        val redeem: AppCompatButton = view.findViewById(R.id.redeem);
        val couponsCode: CardView = view.findViewById(R.id.couponsCode);
        val hintCoupn: TextView = view.findViewById(R.id.hintCoupn);

        if (data.type == "2") {
            couponsCode.visibility = View.GONE
            hintCoupn.visibility = View.GONE
            couponsAndOffer.text = "Offers"
        } else {
            couponsCode.visibility = View.VISIBLE
            hintCoupn.visibility = View.VISIBLE
            couponsAndOffer.text = "Coupon"
        }

        Glide.with(requireContext()).load(data.mini_app.get(0).image).circleCrop()
            .into(partnerIv)
        ExpireDateTV.text = data.end_date
        grabTV.text = "Grab! " + data.label + " " + data.name
        grabAdditional.text = "+ Get " + URLDecoder.decode(data.cashback, "UTF-8") + "*"
        MerchantOfferDetailFragment.merchantName = data.mini_app.get(0).name
        name.text = data.mini_app.get(0).name

        coupons.text = data.code
        copybtn.setOnClickListener {
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)?.apply {
                setPrimaryClip(
                    ClipData.newPlainText(
                        coupons.text,
                        coupons.text
                    )
                )
                copybtn.text = "Coppied"
//                context?.customToast("code coppied", false)
            }
        }
        peopleUsed.text = data.visited + " people used this"
        validitydaysleft.text = data.end + " days left"
        details.text = data.description
        redeem.setOnClickListener {
            OfferRedeemWebViewActivity.openWebView(
                context,
                data.url,
                data.mini_app[0].id,
                data.mini_app[0].image,
                data.tcash,
                data.is_favourite,
                data.cashback
            )
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showcaseBuilder() {
//        ShowcaseView.Builder(requireActivity())
//           //.setTarget(ActionViewTarget(requireActivity(), ActionViewTarget.Type.HOME))
//            .setContentTitle("This is the title")
//            .setContentText("This is a the text pointing to an x,y point in the screen")
//            .hideOnTouchOutside()
//            .setOnClickListener {
//                Log.wtf("MainActivity", "do nothing, hehe")
//            }
//            .build()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(requireContext(), "Downloading Start", Toast.LENGTH_SHORT).show()
            if (resultCode != RESULT_OK) {
                Log.d("Error", "Update Failed: $resultCode")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun topicon() {
        topIconAdapter = TopIconAdapter(this)
    }

    private fun setMoreClicks() {
        binding.newCashbackPartnerMoreIv.setOnClickListener {
            //   openContainer(getString(R.string.cashback_merchants), null, false, getString(R.string.cashback_merchants))
            if (activity is HomeActivity)
                (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.offerTab)
        }

        binding.notificationIv.setOnClickListener {

            ContainerActivity.openContainer(context, ContainerType.NOTIFICATION.name, "")
        }
        binding.seeall.setOnClickListener {
            ContainerActivity.openContainer(context, "ShopProduct", "")
        }
        binding.searchTv1.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        binding.cardMerchantStoreDetails.setOnClickListener {
            ContainerActivity.openContainer(
                context,
                ContainerType.MERCHANT_STORE.name, ""
            )
        }
        binding.recommendedAll.setOnClickListener {
            ContainerActivity.openContainer(
                context,
                "RecommendedItem", ""
            )
        }

        binding.favouritescard.setOnClickListener {
            FavouriteDialogFragment.newInstance(appCategoryList).show(childFragmentManager, null)
        }
        binding.newPartnerAll.setOnClickListener {
            ContainerActivity.openContainer(
                context,
                "Cashback Merchants",
                "",
                false,
                getString(R.string.cashback_merchants)
            )
        }
//        binding.AllCategories.setOnClickListener {
//            if (activity is HomeActivity)
//                (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.ShopNow)
//        }
        binding.cardOffer.setOnClickListener {
            if (activity is HomeActivity)
                (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.offerTab)

        }
        binding.scan.setOnClickListener {

            startActivity(Intent(requireContext(), scanner::class.java))

        }

//        binding.seeAllfavourites.setOnClickListener {
//            if (activity is HomeActivity)
//                (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.favouriteTab)
//
//        }
        binding.gamestab.setOnClickListener {
            if (activity is HomeActivity)
                (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.Games)

        }
        binding.reProfile.setOnClickListener {
            ContainerActivity.openContainer(context, ContainerType.PROFILE_EDIT.name, "")
        }
        binding.AddMoreFavTV.setOnClickListener {
            FavouriteDialogFragment.newInstance(appCategoryList).show(childFragmentManager, null)
        }

    }

    private fun getData() {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(), this, this )
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {

        t?.let {
            it.banner_list1?.let { bannerList1 ->

                /*

                //     Setbanner1data(bannerList1)
                //    setNewBannerdata1(bannerList1)

                 */

                setBannerdata1(bannerList1)

                bannerAdapter1.addAllData(bannerList1)
            }
            it.banner_list3?.let { bannerList3 ->
                inTheSpotLight?.addAllItem(bannerList3)
                mBannerAdapter.addAllItem(bannerList3)
            }
            it.banner_list2?.let { bannerList2 ->
                SetBanner2Data(bannerList2)
                bannerAdapter2.addAllData(bannerList2)
            }
            it.banner_list4?.let { bannerList4 ->
                setBannerdata4(bannerList4)
                bannerAdapter4.addAllData(bannerList4)
            }
            it.app_category?.forEach {
                getOffrsViewmodel1(it.id)
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
            it.new_cashback?.let { newCashBack ->
                partnerCashbackAdapter?.addAllItem(newCashBack)
                //     shopProducatAdapter?.addAllItem(newCashBack)
            }
            it.popular?.let {
                exculsivecashbackAdapter?.addAllItem(it)
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

            setAppCategory(getString(R.string.more))
            setFavourites(getString(R.string.more))

            it.services?.let {
                recommendedDatagett(it)
            }

            it.popular?.let { popular ->
                setPopularMerchants(popular)

            }

//            headlinesList = it.headlines
//            it.headlines?.let { headline ->
//                headlineAdapter?.addAllItem(headline)
//            }

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


        }

        val listData = t?.new_cashback
        var i = 0
        while (i <= listData!!.size) {
            SetDataForSearchcashback(i, t)
            i++
        }

        hideViewIfEmpty()


    }

    private fun setNewBannerdata1(
        bannerList1: java.util.ArrayList<BannerList>,
        viewPager: ViewPager,
        sliderDotspanel: LinearLayout
    ) {
        var dotscount = 0
        var counter = 0
        sliderDotspanel.removeAllViews()
        // dotscount = AdapterShopViewPager.objCategory.exercisesInCategory.size
    }

    private fun getOffrsViewmodel1(id: String?) {
        viewModel.getOfferDetail(getUserId(), "1", id, "0", "0", this,
            object : ApiListener<OfferDetailRes, Any?> {
                override fun onSuccess(t: OfferDetailRes?, mess: String?) {
                    t!!.app_data.let {
                        mAdapter?.addAllItem(it!!)
                    }

                }

            })
    }

    private fun setBannerdata4(bannerList4: ArrayList<BannerList>) {
        val imageList = ArrayList<SliderModelMain>()
        bannerlist4 = bannerList4

        for (x in bannerList4) {
            imageList.add(SliderModelMain(x.image, x.url, x.id, x.type, "", ""))
        }
        _binding!!.banner4.adapter =
            NewBannerDataAdapter(imageList, _binding!!.banner4, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if (data.toString().contains("https://")) {
                        WebViewActivity.openWebView(
                            context,
                            data.toString(), "",
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

        _binding!!.banner4.clipToPadding = false
        _binding!!.banner4.clipChildren = false
        _binding!!.banner4.offscreenPageLimit = 3
        _binding!!.banner4.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
/*
        //slider look start
//
//        val compositPagerTransformation= CompositePageTransformer()
//        compositPagerTransformation.addTransformer(MarginPageTransformer(30))
//        compositPagerTransformation.addTransformer { page, position ->
//            val slide=1- abs(position)
//            page.scaleY= 0.85f+slide+0.25f
//        }
//        _binding!!.banner1.setPageTransformer(compositPagerTransformation)

        //slider look end

 */
        _binding!!.banner4.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler2.removeCallbacks(sliderRunnable)
                sliderHandler2.postDelayed(sliderRunnable, 5000)
            }
        })

    }

    private fun setBannerdata1(bannerList1: ArrayList<BannerList>) {

        val imageList = ArrayList<SliderModelMain>()
        bannerlist1 = bannerList1

        for (x in bannerList1) {
            imageList.add(SliderModelMain(x.image, x.url, x.id, x.type, "", ""))
        }
        _binding!!.banner1.adapter =
            NewBannerDataAdapter(imageList, _binding!!.banner1, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if (data.toString().contains("https://")) {
                     //   WebViewActivity.openWebView(context, data.toString(), "", "", "", "", "", "" )
                        NewWebViewActivity.openWebView(context, data.toString(), "", "", "", "", "", "" )
                    } else if (data.toString().contains("http://")) {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(data.toString()))
                        startActivity(browserIntent)
                    }
                }

            })
        TabLayoutMediator(binding.tabLayout1, binding.banner1) { _, _ -> }.attach()

        _binding!!.banner1.clipToPadding = false
        _binding!!.banner1.clipChildren = false
        _binding!!.banner1.offscreenPageLimit = 3
        _binding!!.banner1.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        //slider look start

        val compositPagerTransformation = CompositePageTransformer()
        compositPagerTransformation.addTransformer(MarginPageTransformer(30))
//        compositPagerTransformation.addTransformer { page, position ->
//            val slide=1- abs(position)
//            page.scaleY= 0.85f+slide+0.25f
//        }
//        _binding!!.banner1.setPageTransformer(compositPagerTransformation)

        //slider look end


        _binding!!.banner1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 4000)
            }
        })


    }

    private fun inTheSpotLight() {
        inTheSpotLight = InTheSpotLightAdapter(this)
        binding.spotLight.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = inTheSpotLight
        }
    }

    private fun LastTransaction() {
        lastTransaction = LasttransactionData(this)
        binding.lasttransaction.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = lastTransaction
        }
    }

    private fun Banner5() {
        AdsAdapter = AdsAdapter(this)
        binding.Banner5.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = AdsAdapter
        }
    }

    private fun Banner6() {
        Banner6Adapter = AdsAdapter(this)
        binding.Banner6.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = Banner6Adapter
        }
    }

    private fun Banner7() {
        Banner2Adapter = Banner2Adapter(this)
        binding.Banner7.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = Banner2Adapter
        }
    }

    private fun exculsiveCashback() {
        exculsivecashbackAdapter = ExculsivecashbackAdapter(this)
        binding.exculsiveCashback.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = exculsivecashbackAdapter
        }
    }

    private fun promotedApps() {
        sponsored = SponsoredAdapter(this)
        binding.sponsored.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = sponsored
        }
    }

    private fun NewPartner() {
        newPartnerAdapter = NewPartnerAdapter(this)
        binding.newPartner.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = newPartnerAdapter
        }
    }


    //   15/03/2022
    private fun SetBanner2Data(bannerList2: ArrayList<BannerList>) {
        val imageList = ArrayList<SliderModelMain>()
        bannerlist2 = bannerList2
        for (x in bannerList2) {
            imageList.add(SliderModelMain(x.image, x.url, x.id, x.type, "", ""))
        }
        _binding!!.banner2.adapter =
            NewBannerDataAdapter(imageList, _binding!!.banner2, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if (data.toString().contains("https://")) {
                        WebViewActivity.openWebView(
                            context, data.toString(), "", "",
                            "", "", "", ""
                        )
                    } else if (data.toString().contains("http://")) {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(data.toString()))
                        startActivity(browserIntent)
                    }
                }

            })
        _binding!!.banner2.clipToPadding = false
        _binding!!.banner2.clipChildren = false
        _binding!!.banner2.offscreenPageLimit = 3
        //    _binding!!.banner2.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER


        //slider look start
        /*
        val compositPagerTransformation= CompositePageTransformer()
        compositPagerTransformation.addTransformer(MarginPageTransformer(30))
        compositPagerTransformation.addTransformer { page, position ->
//            val slide=1- abs(position)
//            page.scaleY= 0.85f+slide+0.25f
        }
        _binding!!.banner2.setPageTransformer(compositPagerTransformation)

         */
        //slider look end

        _binding!!.banner2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //        sliderHandler2.removeCallbacks(sliderRunnable)
                //     sliderHandler2.postDelayed(sliderRunnable,3000)
            }
        })

    }

    //   15/03/2022
    /*
    private fun Setbanner1data(bannerList1: ArrayList<BannerList>) {
        val imageList = ArrayList<SlideModel>()
        bannerlist1 = bannerList1
        for (x in bannerList1) {
            imageList.add(SlideModel(x.image, ScaleTypes.CENTER_CROP))
        }
        binding.banner1.setImageList(imageList)
        binding.banner1.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {

                if (bannerList1[position].url.toString().contains("https://")) {
                    WebViewActivity.openWebView(
                        context,
                        bannerList1[position].url,
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    )
                } else if (bannerList1[position].url.toString().contains("http://")) {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(bannerList1[position].url.toString()))
                    startActivity(browserIntent)
                }
            }

        })
    }
*/

    @SuppressLint("SetTextI18n")
    private fun SetDataForSearchcashback(i: Int, t: HomeRes) {

        val cashbackData =
            t.new_cashback!!.get(i).cashback + " on " + t.new_cashback!!.get(i).mini_app?.name

        //binding.searchTv.hint = cashbackData
    }

    /**
     * @param moreText show more icon or show less icon
     */
    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()
        appCategoryList?.let { appCategory ->
//            if (appCategory.size > 10 && moreText == getString(R.string.more))
//                itemTypeAdapter?.addAllItem(
//                    appCategory.subList(0, 9).toList()
//                )
//            else
            itemTypeAdapter?.addItem(AppCategory("", "", "", "", "", "", "", "", "All", "", false, null))

            itemTypeAdapter?.addItem(
                AppCategory("", "", "", "", "", "", "", "", "Shop&Compair", "", false, null)
            )


            itemTypeAdapter?.addItem(
                AppCategory("", "", "", "", "", "", "", "", "Offers", "", false, null)
            )

            itemTypeAdapter?.addAllItem(appCategory)
//            itemTypeAdapter?.addItem(AppCategory("", "", "",
//                    "", "", "", "", "", moreText, "", false, null))

        }
    }

    private fun setRecyclerCashbackPartner() {
        partnerCashbackAdapter = CashbackPartnerAdapter(this)
        binding.recyclerCashbackpartner.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = partnerCashbackAdapter
        }
    }

    private fun getShop() {
        getSharedPreference().getUserId().let {
            viewModel.getShopProduct(getUserId(), "1", "1", "1", this,
                object : ApiListener<ShopProductData, Any?> {
                    override fun onSuccess(t: ShopProductData?, mess: String?) {
                        t!!.data.let {
                            shopProducatAdapter!!.addAllItem(
                                if (it.size > 8) it.subList(
                                    0,
                                    8
                                ) else it
                            )
                        }
                    }
                })
        }
    }

    private fun getShopAllProduct() {
        getSharedPreference().getUserId().let {
            viewModel.getShopProduct(getUserId(), "", "", "", this,
                object : ApiListener<ShopProductData, Any?> {
                    override fun onSuccess(t: ShopProductData?, mess: String?) {

                        t!!.data.let {
                            var data=it.shuffled()
                            ShopAllProduct?.addAllItem(if (data.size > 20) data.subList(
                                0, 20 ) else data.shuffled())

                            // shopProducatAdapter!!.addAllItem(if (it.size > 8) it.subList(0, 8) else it)
                        }
                    }
                })
        }
    }

    private fun ShopProduct() {
        shopProducatAdapter = ShopProducatAdapter(this)
        binding.shopProduct.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            // layoutManager = GridLayoutManager(context, 2)
            adapter = shopProducatAdapter
        }
    }

    private fun AllShopProduct() {
        ShopAllProduct = ShopAllProductAdapter(this)
        binding.AllshopProduct.apply {
            //   layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter = ShopAllProduct
        }
    }

    /*
        private fun addFragment(fragment: Fragment) {
            //supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            Log.d("Data", "$fragment")
            //   val newFragment: Fragment = Fragment_notification_nav()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentframe, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    */

    private fun setRecyclerBrand() {
        itemTypeAdapter = ItemTypeAdapter(this)
        binding.recyclerAppCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            //    layoutManager = GridLayoutManager(context, 5)
            adapter = itemTypeAdapter
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
        binding.recommended.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = myadapter
        }
        myadapter.addAllItem(if (it.size > 8) it.subList(0, 7) else it)
    }


    private fun setPopularMerchants(list: ArrayList<Popular>) {
        val mAdapter = popularitemAdapter<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    openWebView(data)
                }
            }
        })


        mAdapter.setShowRupee(false)
        mAdapter.setMoreImagePos(8)

        binding.recyclerPopularMerchants.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        mAdapter.addAllItem(if (list.size > 12) { list.subList(0, 12) } else { list }
        )
    }

    //popular Merchant
    /*
    private fun setPopularMerchants(list: ArrayList<Popular>) {
        val mAdapter = UniversalAdapter<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    openWebView(data)
                }
            }
        })


        mAdapter.setShowRupee(true)
        mAdapter.setMoreImagePos(8)

        binding.recyclerPopularMerchants.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        mAdapter.addAllItem(if (list.size > 7) list.subList(0, 8) else list)
    }
*/
    private fun setRecyclerOffer() {
//        binding.recyclerOffer.apply {
//            layoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.HORIZONTAL, false
//            )
//            adapter = mBannerAdapter
//        }
    }

    /*
        private fun setCashBackBanner() {
            binding.pagerCashbackBanner.adapter = bannerAdapter2
            binding.pagerCashbackBanner.pageMargin = 20
            binding.bannerIndicator.setupWithViewPager(binding.pagerCashbackBanner)
        }
        private fun setCashBackBanner4() {
            binding.pagerCashbackBanner4.adapter = bannerAdapter4
            binding.pagerCashbackBanner4.pageMargin = 20
            binding.bannerIndicator.setupWithViewPager(binding.pagerCashbackBanner4)
        }
        private fun setCashBackBanner5() {
            binding.pagerCashbackBanner5.adapter = bannerAdapter5
            binding.pagerCashbackBanner5.pageMargin = 20
        }
    */
    private fun setRecyclerHeadline() {
        headlineAdapter = HeadlineAdapter(object : RecyclerClickListener {
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

    private fun setCashbackMerchantsPager() {
        cashbackMerchantFragmentAdapter = PagerFragmentAdapter(this)
        binding.pagerCashbackMerchantsFragment.adapter = cashbackMerchantFragmentAdapter

        TabLayoutMediator(
            binding.tabLayout,
            binding.pagerCashbackMerchantsFragment
        ) { tab, position ->
            tab.customView =
                getCustomTab(context, cashbackMerchantFragmentAdapter?.getTitle(position))
        }.attach()
    }

    private fun hideViewIfEmpty() {
/*
//        if (bannerAdapter2.count == 0) {
//            binding.pagerCashbackBanner.visibility = View.GONE
//            binding.bannerIndicator.visibility = View.GONE
//        }
//
//        if (bannerAdapter4.count == 0) {
//            binding.pagerCashbackBanner4.visibility = View.GONE
//            binding.bannerIndicator.visibility = View.GONE
//        }
//        if (bannerAdapter5.count == 0) {
//            binding.pagerCashbackBanner5.visibility = View.GONE
//        }
//        if (partnerCashbackAdapter?.itemCount == 0) {
//            binding.cardCashbackPartner.visibility = View.GONE
//        }

 */

        if (itemTypeAdapter?.itemCount == 0) {
            // binding.cardAppCategory.visibility = View.GONE
        }
        if (headlineAdapter?.itemCount == 0) {
            binding.recyclerHeadline.visibility = View.GONE
        }
        if (cashbackMerchantFragmentAdapter?.itemCount == 0) {
            binding.cardCashbackMerchantsFragment.visibility = View.GONE
        }
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
        var UserId = ""

        @JvmStatic
        fun newInstance() =
            HomeFragment()
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
        sliderHandler.postDelayed(sliderRunnable, 4000)
        sliderHandler2.postDelayed(sliderRunnable, 5000)
        val loginData = getSharedPreference().getLoginData()
        UserId = loginData?.unique_user_id.toString()

        if (loginData?.image.isNullOrEmpty()) {
            binding.profileName.visibility = View.VISIBLE
            binding.profileIv.visibility = View.GONE
            binding.profileName.text = loginData?.name

        } else {
            binding.profileName.visibility = View.GONE
            Glide.with(this).load(loginData?.image).apply(
                RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
            ).into(binding.profileIv)
        }
        //   ("Hi," + loginData?.name).also { binding.nameTv.text = it }
        //binding.coinsTv.text = withSuffixAmount(loginData?.cash_available)
        binding.name.text = loginData?.name
        binding.startearningTV.text = getString(R.string.startearning, loginData?.name)
        binding.coinsTv.text = withSuffixAmount(loginData?.cash_available)


    }

    @SuppressLint("LogConditional")
    private fun setFavourites(moreText: String) {
        val mAdapter = FavouriteHomeAdapter(this)
        binding.recyclerFavourites.apply {
            //layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        favViewModel?.getData()?.observe(viewLifecycleOwner, {
            if (it.data?.size == 0) {
                binding.favouritescard.visibility = View.VISIBLE
                binding.favourites.visibility = View.GONE
            } else {
                binding.favouritescard.visibility = View.GONE
                binding.favourites.visibility = View.VISIBLE
            }


            if (it.status == Status.SUCCESS) {
                mAdapter.clear()
                //    it.data?.let { it1 -> mAdapter.addAllItem(it1) }

                it.data?.let { it1 ->
                    if (it1.size > 10 && moreText == getString(R.string.more))
                        mAdapter.addAllItem(it1.subList(0, 9).toList())
                    else
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


    private val sliderRunnable = Runnable {
        try {
            _binding!!.banner1.currentItem = _binding!!.banner1.currentItem + 1
            _binding!!.banner4.currentItem = _binding!!.banner4.currentItem + 1
            //    _binding!!.banner2.currentItem=_binding!!.banner2.currentItem+1
        } catch (e: Exception) {
            Log.d("DataException", "$e")
        }

    }

    override fun onPause() {
        super.onPause()
        sliderHandler.postDelayed(sliderRunnable, 4000)
        sliderHandler2.postDelayed(sliderRunnable, 5000)
        //     sliderHandler2.postDelayed(sliderRunnable,3000)
    }
}