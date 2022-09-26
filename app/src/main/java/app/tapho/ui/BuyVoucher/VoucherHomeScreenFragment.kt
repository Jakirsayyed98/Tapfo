package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentVoucherHomeScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.CategoriesModel.Data
import app.tapho.ui.BuyVoucher.CategoriesModel.VoucherCategoriesViewmodelRes
import app.tapho.ui.BuyVoucher.adapter.voucher_categories_adapter
import app.tapho.ui.ContainerActivity


class VoucherHomeScreenFragment : BaseFragment<FragmentVoucherHomeScreenBinding>(), ApiListener<VoucherCategoriesViewmodelRes,Any?>,RecyclerClickListener{

    var voucherCategoryiesAdapter : voucher_categories_adapter<Data>? = null
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
        CallAllApis()

        _binding!!.swipeRefresh.setOnRefreshListener {
            _binding!!.swipeRefresh.isRefreshing = true
            CallAllApis()
            _binding!!.swipeRefresh.isRefreshing = false
        }
        return _binding?.root
    }

    private fun CallAllApis() {
        getVoucherCategoriesData()
        setLayoutDataForCategory()
    }

    private fun setLayoutDataForCategory() {
        voucherCategoryiesAdapter = voucher_categories_adapter(this)
        _binding!!.categories.apply {
            layoutManager = GridLayoutManager(requireContext(),4)

        //    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = voucherCategoryiesAdapter
        }
    }

    private fun getVoucherCategoriesData() {
        viewModel.getVoucherCategory(getUserId(),this,this)
    }


    override fun onResume() {
        super.onResume()
//        CallAllApis()
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
                voucherCategoryiesAdapter!!.addAllItem(it)
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if(data is Data){
            ContainerActivity.openContainer(requireContext(),"OpenVoucherList",data)
        }
    }
}