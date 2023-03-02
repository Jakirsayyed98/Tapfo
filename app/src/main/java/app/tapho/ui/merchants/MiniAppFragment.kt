package app.tapho.ui.merchants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMiniAppBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.BaseRes
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.Categorys.CategoryCashbackPartnerFragment
import app.tapho.ui.home.Categorys.CategoryMerchantsFragment
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.BannerAdapter
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.merchants.adapter.SubCategoryAdapter
import app.tapho.ui.merchants.adapter.SubCategoryTabsAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.AppSubCategory
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

class MiniAppFragment : BaseFragment<FragmentMiniAppBinding>(), ApiListener<MiniAppRes, Any?>,
    RecyclerClickListener {
    private var popularList: ArrayList<Popular>? = null
    private var madapter: PagerFragmentAdapter? = null
    private val bannerAdapter1 = BannerAdapter()
    private var subCatTabsAdapter: SubCategoryTabsAdapter? = null
    private var miniAppRes: MiniAppRes? = null
    var filterdList = ArrayList<MiniApp>()
    var listdata =ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMiniAppBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        _binding!!.favouritesBtn.setOnClickListener {
            openContainer("favouritesBtn", "", false, "")
        }

        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }

        init()
        getData()
        setTab()
        return _binding?.root
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


    private fun init() {
        binding.titleTv.text = activity?.intent?.getStringExtra(TITLE)
        subCatTabsAdapter = SubCategoryTabsAdapter(this)
        binding.recyclerSubCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = subCatTabsAdapter

        }
        subCatTabsAdapter?.addItem(AppSubCategory("", "", "", "", "", "", "", true))
        subCatTabsAdapter?.addItem(AppSubCategory("", "", "", "", "", "", "", false))

//        binding.allAllIv.setOnClickListener { select(it) }
//        binding.staredIv.setOnClickListener { select(it) }
        binding.bannerPager.adapter = bannerAdapter1
        binding.bannerPager.pageMargin = 20
      //  binding.animationView.visibility=View.VISIBLE
    }

    private fun setTab() {
        madapter = PagerFragmentAdapter(this)
        _binding!!.categoriesTab.adapter = madapter
        madapter!!.addFragment(CategoryMerchantsFragment.newInstance(), getString(R.string.Merchants))
        madapter!!.addFragment(CategoryCashbackPartnerFragment.newInstance(), getString(R.string.Cashback))
        //bindingForGames!!.tabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_on_primary))
        _binding!!.tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.white), ContextCompat.getColor(requireContext(), R.color.white))
        TabLayoutMediator(
            _binding!!.tabLayout,
            _binding!!.categoriesTab
        ) { tab, position ->
            tab.customView = getCustomTab(context, madapter?.getTitle(position))
        }.attach()
    }
    private fun getData() {
        val oldData = activity?.intent?.getBooleanExtra(SET_OLD_DATA, false)

        activity?.intent?.getStringExtra(DATA)?.let {
            if (oldData == false) {
                val category = Gson().fromJson(it, AppCategory::class.java)
//                if (activity is ContainerActivity)
//                    (activity as ContainerActivity).setTitlec(category.name)

                _binding!!.categoryName.text=activity?.intent?.getStringExtra(TITLE)//category.name
                viewModel.getAppCategoryMiniApp(AppSharedPreference.getInstance(requireContext()).getUserId(),"0", category.id, this, this)
            } else {
                //  binding.animationView.visibility=View.GONE
                //       binding.recyclerSubCategories.visibility = View.GONE
                Gson().fromJson<ArrayList<MiniApp>>(it,
                    object : TypeToken<ArrayList<MiniApp>>() {}.type)?.let {
                    binding.bannerPager.visibility = View.GONE
                    setRecyclerAllApp(it, true)
                }
            }
        }
    }

    private fun setRecyclerAllApp(miniApp1: List<MiniApp>, showTopMerchants: Boolean) {

        binding.recyclerMiniApp.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = UniversalAdapter<MiniApp>(this@MiniAppFragment).apply {
                val filterdList1 = ArrayList<MiniApp>()
                miniAppRes?.mini_app?.forEachIndexed { index, miniApp ->
                    if (miniApp.cashback.isNullOrEmpty()) {
//                        binding.animationView.visibility=View.GONE
//                        binding.line.visibility=View.VISIBLE
//                        binding.recyclerSubCategories.visibility=View.VISIBLE
//                        binding.bestPick1.visibility=View.VISIBLE
                            filterdList1.add(miniApp)
                    }
                }
                addAllItem(filterdList1)
            }
    }


    if (miniApp1.size > 4) {
        topMerchantsRecycler(miniApp1.subList(miniApp1.size - 4, miniApp1.size), showTopMerchants)
    } else {
        topMerchantsRecycler(miniApp1, showTopMerchants)
    }
}

private fun setRecyclerSubCategories(miniApp: List<MiniApp>) {
    binding.recyclerMiniApp.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = SubCategoryAdapter(this@MiniAppFragment).apply {
            //     binding.animationView.visibility=View.GONE
            binding.line.visibility=View.GONE
            //  addItem(miniApp[1])
            addAllItem(miniApp)
        }
    }
    topMerchantsRecycler(emptyList(), false)
}

companion object {
    @JvmStatic
    fun newInstance() =
        MiniAppFragment()
}

override fun onSuccess(t: MiniAppRes?, mess: String?) {
    t?.let {
        miniAppRes = t
        if (it.banner_list.isNullOrEmpty())
            binding.bannerPager.visibility = View.GONE
        it.banner_list?.let { banner ->
            bannerAdapter1.addAllData(banner)
        }
        it.mini_app?.let { it1 -> setRecyclerAllApp(it1, true) }

        it.app_sub_category?.let { it1 -> subCatTabsAdapter?.addAllItem(it1) }
    }
}

override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
    if (type == OPEN_WEB_VIEW) {
        if (data is MiniApp)
            ActiveCashbackForWebActivity.openWebView(context,
                data.url,
                data.id,
                data.image,
                data.tcash,
                data.is_favourite,
                data.cashback,data.app_category_id,data.url_type,data.name)
    } else if (type == "sub_cat_click") {
        when (pos) {
            0 -> miniAppRes?.mini_app?.let { setRecyclerAllApp(it, true) }
            1 -> setFavouriteList()
            else -> {
                if (data is AppSubCategory) {
                    listdata
                    val list = ArrayList<MiniApp>()
                    miniAppRes?.mini_app?.forEach {
                        if (it.app_sub_category_id == data.id) {
                            list.add(it)
                        }
                    }
                    setRecyclerSubCategories(list)
                }
            }
        }
    } else if (type == "favourite") {
        if (data is MiniApp) {
            if (data.is_favourite == "1")
                makeUnFav(data)
            else
                makeFav(data)
        }
    }
}

private fun setFavouriteList() {
    miniAppRes?.mini_app?.let {
        val list = ArrayList<MiniApp>()
        miniAppRes?.mini_app?.forEach {
            if (it.is_favourite == "1") {
                list.add(it)
            }
        }
        setRecyclerSubCategories(list)
    }
}

private fun makeFav(miniApp: MiniApp) {
    viewModel.miniAppFev(AppSharedPreference.getInstance(requireContext()).getUserId(),
        miniApp.id,
        this,
        object : ApiListener<BaseRes, Any?> {
            override fun onSuccess(t: BaseRes?, mess: String?) {
                t?.let {
                    if (it.errorCode == "0") {
                        miniApp.is_favourite = "1"
                        binding.recyclerMiniApp.adapter?.notifyDataSetChanged()
                        if (subCatTabsAdapter?.getSelectedPos() == 1)
                            setFavouriteList()
                        context?.customToast(getString(R.string.added_in_fev), false)
                    } else context?.toast(mess)
                }
            }
        })
}

private fun makeUnFav(miniApp: MiniApp) {
    viewModel.miniAppUnFev(AppSharedPreference.getInstance(requireContext()).getUserId(),
        miniApp.id,
        this,
        object : ApiListener<BaseRes, Any?> {
            override fun onSuccess(t: BaseRes?, mess: String?) {
                t?.let {
                    if (it.errorCode == "0") {
                        miniApp.is_favourite = "0"
                        binding.recyclerMiniApp.adapter?.notifyDataSetChanged()
                        if (subCatTabsAdapter?.getSelectedPos() == 1)
                            setFavouriteList()
                        context?.customToast(getString(R.string.removed_from_fav), true)
                    } else context?.toast(mess)
                }
            }
        })
}


private fun topCashbackRecycler(miniApp: List<MiniApp>) {
    binding.recyclerBestPicks.apply {
        layoutManager = GridLayoutManager(context, 3)
        adapter = UniversalAdapter<MiniApp>(this@MiniAppFragment).apply {

            //         binding.animationView.visibility=View.GONE
            binding.line.visibility=View.GONE
            binding.recyclerSubCategories.visibility=View.VISIBLE
            //  binding.bestPick1.visibility=View.VISIBLE
            addAllItem(miniApp)
        }
    }
}


private fun topMerchantsRecycler(miniApp: List<MiniApp>, showTopMerchants: Boolean) {
    if (showTopMerchants) {
        //      binding.animationView.visibility=View.GONE
        binding.line.visibility=View.VISIBLE
        //      binding.bestPick1.visibility = View.VISIBLE
        binding.recyclerBestPicks.visibility = View.VISIBLE
    } else {
        //  binding.animationView.visibility=View.GONE
        binding.line.visibility=View.VISIBLE
        //binding.bestPick1.visibility = View.GONE
        binding.recyclerBestPicks1.visibility = View.GONE
        binding.recyclerBestPicks.visibility = View.GONE
    }
    if (showTopMerchants) {
        topCashbackRecycler(getTopCashbackApps())
    }
}

private fun getTopCashbackApps(): MutableList<MiniApp> {
    // filterdList = ArrayList<MiniApp>()
    miniAppRes?.mini_app?.forEachIndexed { index, miniApp ->
        if (miniApp.cashback.isNullOrEmpty().not()) {
            if (miniApp.getCashback().contains("%") || miniApp.getCashback().contains("₹")) {
                filterdList.add(miniApp)
            }
        }
    }

    return filterdList
}

}