package app.tapho.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import app.tapho.R
import app.tapho.databinding.FragmentNewHomeBinding
import app.tapho.databinding.FragmentOnlyCashbackStoresBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.News.HeadlinesActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.favourite.FavouriteDialogFragment
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.home.ShopProduct.ShopAdapter.ShopProducatAdapter
import app.tapho.ui.home.adapter.*
import app.tapho.ui.merchants.MerchantOfferDetailFragment
import app.tapho.ui.merchants.MerchantsAllListDialogFragment
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.*
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.scanner.scanner
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.ContainerType
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.withSuffixAmount
import app.tapho.viewmodels.FavouriteViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.common.collect.Lists
import com.kakyiretechnologies.appreview.reviewApp
import java.net.URLDecoder
import kotlin.random.Random


class OnlyCashbackStoresFragment : BaseFragment<FragmentOnlyCashbackStoresBinding>(),
    ApiListener<HomeRes, Any?>,
    RecyclerClickListener {

    private var partnerCashbackAdapter: CashbackPartnerAdapter<NewCashback>? = null
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var bannerlist1: ArrayList<BannerList>? = null
    var CB_MiniApps : ArrayList<MiniApp> = ArrayList()

    private var appCategoryList: ArrayList<AppCategory>? = null
    private var favViewModel: FavouriteViewModel? = null

    val REQUEST_CODE = 11

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
        _binding1 = FragmentOnlyCashbackStoresBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        statusBarColor(R.color.light_orange)
         statusBarTextWhite()
//        statusBarTextBlack()
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        _binding1!!.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        getCashbackMerchantLink()


        showLoaderFragment()
        setRecyclerCashbackPartner()
        getData()
        setMoreClicks()




        setRecyclerBrand()
        return _binding1?.root

    }

    private fun getCashbackMerchantLink() {
        viewModel.getHomeData("home",getUserId(),this,object : ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t!!.let {
                   it.app_category!!.forEach {
                       getMiniAppList(it.id)
                   }
                }
            }

        })

    }

    private fun getMiniAppList(categoryid: String?) {
        viewModel.getAppCategoryMiniApp(getUserId(),categoryid,this,object : ApiListener<MiniAppRes,Any?>{
            override fun onSuccess(t: MiniAppRes?, mess: String?) {
                t.let {
                    it!!.mini_app!!.forEach {
                        if (it.tcash.equals("1")){
                            CB_MiniApps.add(it)
                        }

                    }
//                    CB_MiniApps!!.addAll(it!!.mini_app!!.shuffled())
                }
                setListData(CB_MiniApps!!)

            }

        })
    }



    private fun setListData(list: ArrayList<MiniApp>) {
        val mAdapter = CardPopularStore<MiniApp>(this)
        _binding1!!.recyclerPopularMerchants.apply {

//            layoutManager= GridLayoutManager(requireContext(),2,GridLayoutManager.HORIZONTAL,false)
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        list.shuffled()
        mAdapter.addAllItem(if (list.size > 12) { list.subList(0, 12) } else { list }
        )

    }


    private fun setRecyclerBrand() {
        itemTypeAdapter = ItemTypeAdapter(this)
        _binding1!!.recyclerAppCategory.apply {
         //   layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                layoutManager = GridLayoutManager(context, 4)
            adapter = itemTypeAdapter
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            OnlyCashbackStoresFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(requireContext(), "Downloading Start", Toast.LENGTH_SHORT).show()
            if (resultCode != Activity.RESULT_OK) {
                Log.d("Error", "Update Failed: $resultCode")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun setMoreClicks() {
        _binding1!!.recommendedAll.setOnClickListener {
            ContainerActivity.openContainer(
                context,
                "RecommendedItem", ""
            )
        }
        _binding1!!.AddMoreFavTV.setOnClickListener {
            FavouriteDialogFragment.newInstance(appCategoryList).show(childFragmentManager, null)
        }

    }


    private fun getData() {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(), this, this)
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {

        t?.let {
            it.banner_list1?.let { bannerList1 ->
                setBannerdata1(bannerList1)
            }

            it.new_cashback?.let { newCashBack ->
                partnerCashbackAdapter?.addAllItem(newCashBack)
            }
            setFavourites(getString(R.string.more))
            appCategoryList = it.app_category

            itemTypeAdapter?.addAllItem(appCategoryList!!)
            it.popular?.let { popular ->
                //setPopularMerchants(popular)
            }
            dissmissLoader()
        }
        val listData = t?.new_cashback
        var i = 0
        while (i <= listData!!.size) {
            i++
        }

    }


    private fun setNewBannerdata1(bannerList1: java.util.ArrayList<BannerList>,
        viewPager: ViewPager,
        sliderDotspanel: LinearLayout
    ) {
        var dotscount = 0
        var counter = 0
        sliderDotspanel.removeAllViews()
        // dotscount = AdapterShopViewPager.objCategory.exercisesInCategory.size
    }

    private fun setRecyclerCashbackPartner() {
        partnerCashbackAdapter = CashbackPartnerAdapter(this)
        _binding1!!.recyclerCashbackpartner.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = partnerCashbackAdapter
        }
    }

    private fun setBannerdata1(bannerList1: ArrayList<BannerList>) {
        if (bannerList1.isNullOrEmpty()) {
            _binding1!!.banner1.visibility = View.GONE
        } else {
            _binding1!!.banner1.visibility = View.VISIBLE
        }

        val imageList = ArrayList<SliderModelMain>()
        bannerlist1 = bannerList1

        for (x in bannerList1) {
            imageList.add(SliderModelMain(x.image, x.url, x.id, x.type, "", ""))
        }
        _binding1!!.banner1.adapter =
            NewBannerDataAdapter(imageList, _binding1!!.banner1, object : RecyclerClickListener {
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


        TabLayoutMediator(_binding1!!.tabLayout1, _binding1!!.banner1) { _, _ -> }.attach()

        _binding1!!.banner1.clipToPadding = false
        _binding1!!.banner1.clipChildren = false
        _binding1!!.banner1.offscreenPageLimit = 3
        _binding1!!.banner1.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        //Handler Start
        Handler().apply {
            val runnable = object : Runnable {
                override fun run() {
                    var i = _binding1!!.banner1.currentItem

                    if (i == bannerList1.size - 1) {
                        i = -1//0
                        _binding1!!.banner1.currentItem = i
                    }
                    i++
                    _binding1!!.banner1.setCurrentItem(i, true)
                    postDelayed(this, 4000)
                }
            }
            postDelayed(runnable, 4000)
        }

    }

    /*
    private fun setPopularMerchants(list: ArrayList<Popular>) {
        val mAdapter = popularitemAdapter<Popular>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    openWebView(data)
                }
            }
        })


        mAdapter.setShowRupee(false)
      _binding1!!.recyclerPopularMerchants.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = mAdapter
        }
        mAdapter.addAllItem(if (list.size > 15) {
            list.subList(0, 15)
        } else {
                list
            }
        )
    }
*/

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }


    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
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
                    setFavourites(getString(R.string.more))
                }
//                else -> openContainer("MiniAppFragment", data, false, data.name)
                else ->     openContainer("MiniAppFragmentNewCategories", data, false, data.name)
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
        val loginData = getSharedPreference().getLoginData()
        HomeFragment.UserId = loginData?.unique_user_id.toString()

        if (loginData?.image.isNullOrEmpty()) {


        } else {


        }
        //   ("Hi," + loginData?.name).also { binding.nameTv.text = it }
        //binding.coinsTv.text = withSuffixAmount(loginData?.cash_available)


    }

    @SuppressLint("LogConditional")
    private fun setFavourites(moreText: String) {
        val mAdapter = FavouriteHomeAdapter(this)
    //    val mAdapter = HomeFavAdapter(this)
        _binding1!!.recyclerFavourites.apply {
            //   layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            layoutManager = GridLayoutManager(context, 4)
        //    layoutManager = GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL,false)
            adapter = mAdapter
        }
        favViewModel?.getData()?.observe(viewLifecycleOwner, {
            if (it.data?.size == 0) {
//                _binding1!!.favouritescard.visibility = View.VISIBLE
//                _binding1!!.favourites.visibility = View.GONE
            } else {
//                _binding1!!.favouritescard.visibility = View.GONE
 //               _binding1!!.favourites.visibility = View.VISIBLE
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

    override fun onPause() {
        super.onPause()
    }

    private fun dissmissLoader() {
      //  _binding1!!.loadingbar.visibility=View.GONE
    }


    private fun showLoaderFragment() {
    //    _binding1!!.loadingbar.visibility=View.VISIBLE
    }

}