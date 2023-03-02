package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSelectedVouchersListBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.CategoriesModel.Data
import app.tapho.ui.BuyVoucher.CategoriesModel.VoucherCategoriesViewmodelRes
import app.tapho.ui.BuyVoucher.VoucherListViewModel.CustomeVoucherList
import app.tapho.ui.BuyVoucher.VoucherListViewModel.VoucherListRes
import app.tapho.ui.BuyVoucher.adapter.voucher_list_adapter
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.model.Popular
import app.tapho.utils.DATA
import app.tapho.utils.TITLE
import com.google.gson.Gson


class SelectedVouchersListFragment : BaseFragment<FragmentSelectedVouchersListBinding>(),RecyclerClickListener {

    private var popularList: ArrayList<Popular>? = null
    var customeVoucher : ArrayList<CustomeVoucherList>?=null
    var voucherListAdapter : voucher_list_adapter<CustomeVoucherList>? = null


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
        _binding = FragmentSelectedVouchersListBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        CallAllApis()
        _binding!!.retryinternetButton.setOnClickListener {
            CallAllApis()
        }
        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }

        return _binding?.root
    }

    private fun CallAllApis() {
        progressVISIBLE()
        setLayoutForList()
        setDataForAllLayout()

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

    private fun setDataForAllLayout() {
        if (activity?.intent!!.getStringExtra("reddemtype").toString().equals("0")){
            getSharedPreference().saveString("StartLoadingVoucherslist","0")
            activity?.intent!!.getStringExtra(DATA).let {
                val data =  Gson().fromJson(it, Data::class.java)

                _binding!!.CategoriesName.text = data.name
                val vouchereedemtype = activity?.intent!!.getStringExtra("reddemtype").toString()
                setLayoutData(data,vouchereedemtype)

            }
        }else{
            getSharedPreference().saveString("StartLoadingVoucherslist","0")
            _binding!!.CategoriesName.text =activity?.intent?.getStringExtra(TITLE).toString()
            viewModel.getVoucherCategory(getUserId(),this,object : ApiListener<VoucherCategoriesViewmodelRes,Any?>{
                override fun onSuccess(t: VoucherCategoriesViewmodelRes?, mess: String?) {
                    t.let {
                        val vouchereedemtype = activity?.intent?.getStringExtra("reddemtype").toString()
                        it!!.data.forEach {
                            getVoucherListData(it.id,vouchereedemtype)
                        }
                    }
                }

            })
        }



    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun setLayoutForList() {
        voucherListAdapter = voucher_list_adapter(this)
        _binding!!.vouchersList.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = voucherListAdapter
        }
    }

    private fun setLayoutData(data: Data?,vouchereedemtype : String) {
        data.let {
            getVoucherListData(it!!.id,vouchereedemtype)
        }
    }

    private fun getVoucherListData(id: String,vouchereedemtype:String) {
        viewModel.getVoucherList(getUserId(),id,"0",this,object : ApiListener<VoucherListRes,Any?>{
            override fun onSuccess(t: VoucherListRes?, mess: String?) {
                t.let {
                    it!!.data.let {
                        getSharedPreference().saveString("StartLoadingVoucherslist","1")
                        showHome()
                        it.forEach {voucherdata->
                                if (vouchereedemtype.equals("0")){
                                    if (voucherdata.logo.isNullOrEmpty().not()) {
                                        voucherListAdapter!!.addItem(
                                            CustomeVoucherList(voucherdata.id, voucherdata.logo, voucherdata.name, voucherdata.user_discount, "", "", "50", voucherdata.redemption))
                                    }
                                }else{
                                    if (voucherdata.redemption.equals(vouchereedemtype)){
                                        voucherListAdapter!!.addItem(CustomeVoucherList(voucherdata.id,voucherdata.logo,voucherdata.name,voucherdata.user_discount,"","","50",voucherdata.redemption))
                                    }
                                }
                        }
                    }
                }
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SelectedVouchersListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is CustomeVoucherList){
            ContainerActivity.openContainer(requireContext(),"OpenVoucherDetailForBuy",data)
        }

    }


    fun progressVISIBLE() {
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.VISIBLE
        _binding!!.lowconnection.visibility = View.GONE

    }
    fun lowConnection() {
        _binding!!.lowconnection.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE

    }

    fun showHome() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.VISIBLE

    }


}