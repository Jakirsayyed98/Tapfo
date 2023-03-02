package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.R
import app.tapho.databinding.FragmentVoucherHomeScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.CategoriesModel.Data
import app.tapho.ui.BuyVoucher.CategoriesModel.VoucherCategoriesViewmodelRes
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersDatabase
import app.tapho.ui.BuyVoucher.adapter.voucher_categories_adapter
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.CustomeHowCBRedeemBloksAdapter
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import app.tapho.ui.home.adapter.TopTabAdapterLinkAdapter
import app.tapho.ui.merchants.model.CustomeModel
import app.tapho.ui.model.Popular
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VoucherHomeScreenFragment : BaseFragment<FragmentVoucherHomeScreenBinding>(), ApiListener<VoucherCategoriesViewmodelRes,Any?>,RecyclerClickListener,
    ConnectivityListener {
    private var popularList: ArrayList<Popular>? = null
    var voucherCategoryiesAdapter : voucher_categories_adapter<Data>? = null
    lateinit var database: VouchersDatabase

    private var appCategoryList: ArrayList<Data>? = null

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
        _binding = FragmentVoucherHomeScreenBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        database = VouchersDatabase.getDatabase(requireContext())

        checkConnection()
        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.swipeRefresh.setOnRefreshListener {
            _binding!!.swipeRefresh.isRefreshing = true
            CallAllApis()
            _binding!!.swipeRefresh.isRefreshing = false
        }

        _binding!!.supportpage.setOnClickListener {
                ContainerActivity.openContainer(requireContext(),"OpenServiceList","")
            }

        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        return _binding?.root
    }

    private fun CallAllApis() {
        progressVISIBLE()

        setLayoutDataForCategory()
        getVoucherCategoriesData()
        getSuperTabData()
        setStaticLayout()


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


    private fun getSuperTabData() {
        val topTabAdapter = TopTabAdapterLinkAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when(data){
                    "My Vouchers"->{
                            openContainer("AllPurchasedVouchers","",false,"")
                    }
                    "History"->{
                            openContainer("VoucherPurchasedHistoryFragment","",false,"")
                        }
                    "Online Vouchers"->{
                            ContainerActivity.openContainerforvoucher(requireContext(),"OpenVoucherList","","1","Online Vouchers")
                        }
                    "Offline Vouchers"->{
                            ContainerActivity.openContainerforvoucher(requireContext(),"OpenVoucherList","","2","Offline Vouchers")
                        }
                    "Favourites Vouchers"->{
                            openContainer("FavouritesVoucherFragment","",false,"")
                        }
                    "Popular Brands"->{
                            openContainer("PopularVoucherFragment","",false,"")
                        }
                }
            }

        }).apply {
            addItem(CustomeSuperCategoryModel(R.drawable.online_fav_icon, "My Favourites", "Favourites Vouchers",""))
            addItem(CustomeSuperCategoryModel(R.drawable.offer_and_coupons_icon, "Popular Brands", "Popular Brands",""))
            addItem(CustomeSuperCategoryModel(R.drawable.offer_and_coupons_icon, "Online Vouchers", "Online Vouchers","Online Vouchers"))
            addItem(CustomeSuperCategoryModel(R.drawable.offer_and_coupons_icon, "Offline Vouchers", "Offline Vouchers",""))
            addItem(CustomeSuperCategoryModel(R.drawable.offer_and_coupons_icon, "My Vouchers", "My Vouchers",""))
            addItem(CustomeSuperCategoryModel(R.drawable.offer_and_coupons_icon, "History", "History",""))
        }
        _binding!!.topTab.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = topTabAdapter
        }
    }

    private fun setLayoutDataForCategory() {
        voucherCategoryiesAdapter = voucher_categories_adapter(this)
        _binding!!.recyclerAppCategory.apply {
            layoutManager = GridLayoutManager(requireContext(),5)
            adapter = voucherCategoryiesAdapter
        }
    }

    private fun setStaticLayout() {
        val blockData = CustomeHowCBRedeemBloksAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {


            }
        }).apply {
            addItem(CustomeModel(1,"",R.drawable.voucherstep_1))
            addItem(CustomeModel(2,"",R.drawable.voucherstep_2))
            addItem(CustomeModel(3,"",R.drawable.voucherstep_3))
            addItem(CustomeModel(4,"",R.drawable.voucherstep_4))
        }
        _binding!!.stepsRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter =blockData
        }
    }

    private fun getVoucherCategoriesData() {
        getSharedPreference().saveString("StartLoading","0")
        viewModel.getVoucherCategory(getUserId(),this,this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VoucherHomeScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: VoucherCategoriesViewmodelRes?, mess: String?) {
        t.let {
            it!!.data.let{
//                voucherCategoryiesAdapter!!.addAllItem(it)
                getSharedPreference().saveString("StartLoading","1")
                appCategoryList = it
                showHome()
                setAppCategory(getString(R.string.more))
            }
        }
    }

    private fun setAppCategory(moreText: String) {
        voucherCategoryiesAdapter?.clear()
        appCategoryList?.let { appCategory ->
            voucherCategoryiesAdapter?.addAllItem(appCategory)
        }
    }



    @OptIn(DelicateCoroutinesApi::class)
    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        GlobalScope.launch {
            database.getVouchersDatabase().deleteAll()
        }

        if (data is Data) {
            when (data.name) {
                getString(R.string.more) -> {
                    setAppCategory(getString(R.string.less))
                }
                getString(R.string.less) -> {
                    setAppCategory(getString(R.string.more))
                }
                else -> {
                    ContainerActivity.openContainerforvoucher(requireContext(),"OpenVoucherList",data,"0","")
//                    ContainerActivity.openContainer(requireContext(), "OpenVoucherList", data)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        setConnectivityListener(this)
    }


    private fun checkConnection() {
        val isConnected = ConnectionReceiver.isConnected(requireContext())

        if (isConnected) {
            CallAllApis()
        } else{
            noInternetConnection()
        }

        _binding!!.retryinternetButton.setOnClickListener {
            checkConnection()
        }

    }



    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {

            CallAllApis()
        } else{
            requireContext().showShort("Sorry! Not connected to internet")
        }
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


}