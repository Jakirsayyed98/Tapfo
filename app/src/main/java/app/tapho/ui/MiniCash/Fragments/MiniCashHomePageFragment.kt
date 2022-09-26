package app.tapho.ui.MiniCash.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentMiniCashHomePageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.MiniCash.Adapter.*
import app.tapho.ui.MiniCash.Adapter.BannerAdapter
import app.tapho.ui.MiniCash.Adapter.CategorywithMiniAppsRecyler.HeadlineAdapterForCategories
import app.tapho.ui.MiniCash.UI.MiniCashFragmentContainerActivity
import app.tapho.ui.MiniCash.model.BannerModel
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.*
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.*
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcashback_detail_Activity
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.withSuffixAmount
import app.tapho.viewmodels.FavouriteViewModel
import app.tapho.viewmodels.MerchantsAllListViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson


class MiniCashHomePageFragment : BaseFragment<FragmentMiniCashHomePageBinding>(),RecyclerClickListener {
    private var MinicashappcategoryAdapter: MinicashappcategoryAdapter? = null
    private var popularList: ArrayList<Popular>? = null
    private var categoryTabAdapter: CategoryTabAdapter<CashbackMerchantCategory>? = null
    var madapterdata : MerchantsDataAdapter?=null
    private val merchantViewMode: MerchantsAllListViewModel by viewModels()
    private var dataList: ArrayList<CashbackMerchant>? = ArrayList()
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
        _binding = FragmentMiniCashHomePageBinding.inflate(inflater, container, false)
        statusBarColor(R.color.Minicash)
        statusBarTextBlack()
        _binding!!.mainLayout.visibility=View.GONE
        _binding!!.progress.visibility=View.VISIBLE

//        Handler(Looper.getMainLooper()).postDelayed({
//
//            _binding!!.mainLayout.visibility = View.VISIBLE
//            _binding!!.progress.visibility=View.GONE
//        },2000)


        allviewmodelClasses()



        return _binding?.root
    }


    private fun allviewmodelClasses() {
        observeData()

        _binding!!.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.searchEt.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        getData()
        setRecyclerBrand()
        HomeViewmodel()
        setTabLayoutdata()
        setDatainAllMerchant()

    }

    private fun setTabLayoutdata() {
        val list = ArrayList<CashbackMerchant>()
        categoryTabAdapter = CategoryTabAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                madapterdata!!.clear()
                list.clear()
                if (data is CashbackMerchantCategory) {


                    if (data.id.isNullOrEmpty().not())
                        dataList?.forEach {
                            if (it.cashback_merchant_category_id == data.id) {
                                list.add(it)
                            }
                        }
                    else
                        dataList?.let { list.addAll(it) }

                    madapterdata!!.addAllItem(list)
                    list.forEach {
                        Log.d("MyDataImage",it.image.toString())
                    }

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

    private fun HomeViewmodel() {
        viewModel.getHomeData("home",getUserId(),this,object : ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t.let{
                    it!!.app_category.let {
                        MinicashappcategoryAdapter!!.addAllItem(it!!)
                    }
                    it.popular?.let {
                        popularList = it
                    }

                    _binding!!.mainLayout.visibility = View.VISIBLE
                    _binding!!.progress.visibility=View.GONE
                }
            }

        })
    }


    private fun getData() {
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
        data?.let {
            it.cashback_merchant_category?.let { categoryList ->
                categoryTabAdapter?.addItem(CashbackMerchantCategory("", "", "All", "", true))
                categoryTabAdapter?.addAllItem(categoryList)
            }
            var list : ArrayList<CashbackMerchant> = ArrayList()
            it.data!!.forEach {
                if (it.mini_app!=null){
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
            openContainer2(
                getString(R.string.popular_merchants),
                list,
                true,
                getString(R.string.popular_merchants)
            )
    }
    private fun openContainer2(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }




    private fun setRecyclerBrand() {
        MinicashappcategoryAdapter = MinicashappcategoryAdapter(this)
        _binding!!.recyclerAppCategory.apply {
               layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = MinicashappcategoryAdapter
        }
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            MiniCashHomePageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
         //   when (data.name) {
            //    else ->
                    openContainer("MiniCashStorePage", data, false, data.name)
      //      }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                openWebView(data)
        }
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        MiniCashFragmentContainerActivity.openContainer(context, type, data, isOldData, title)
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
}