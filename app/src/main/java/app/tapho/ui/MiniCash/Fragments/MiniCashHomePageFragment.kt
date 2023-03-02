package app.tapho.ui.MiniCash.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMiniCashHomePageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.*
import app.tapho.ui.MiniCash.Adapter.*
import app.tapho.ui.MiniCash.UI.MiniCashFragmentContainerActivity
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.model.*
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.viewmodels.MerchantsAllListViewModel

class MiniCashHomePageFragment : BaseFragment<FragmentMiniCashHomePageBinding>(),RecyclerClickListener {
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
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.mainLayout.visibility=View.GONE
        _binding!!.progress.visibility=View.VISIBLE

        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
            activity?.finish()
        }



        allviewmodelClasses()



        return _binding?.root
    }


    private fun allviewmodelClasses() {
        HomeViewmodel()
        observeData()

        _binding!!.favouritesBtn.setOnClickListener {
            openContainer2("favouritesBtn", "", false, "")
        }

        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        getData()

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
                    it!!.popular?.let {
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
        ActiveCashbackForWebActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id,
            data.url_type,data.name
        )
    }
}