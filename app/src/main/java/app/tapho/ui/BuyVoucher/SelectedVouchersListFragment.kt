package app.tapho.ui.BuyVoucher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSelectedVouchersListBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.CategoriesModel.Data
import app.tapho.ui.BuyVoucher.VoucherListViewModel.VoucherListRes
import app.tapho.ui.BuyVoucher.adapter.voucher_list_adapter
import app.tapho.ui.ContainerActivity
import app.tapho.utils.DATA
import com.bumptech.glide.Glide
import com.google.gson.Gson


class SelectedVouchersListFragment : BaseFragment<FragmentSelectedVouchersListBinding>(),ApiListener<VoucherListRes,Any?>,RecyclerClickListener {

    var voucherListAdapter : voucher_list_adapter<app.tapho.ui.BuyVoucher.VoucherListViewModel.Data>? = null


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
        setDataForAllLayout()
        _binding!!.swipeRefresh.setOnRefreshListener {
            _binding!!.swipeRefresh.isRefreshing = true
            setDataForAllLayout()
            _binding!!.swipeRefresh.isRefreshing = false
        }
        return _binding?.root
    }

    override fun onResume() {
        super.onResume()
      //  setDataForAllLayout()
    }

    private fun setDataForAllLayout() {
        activity?.intent!!.getStringExtra(DATA).let {
            val data =  Gson().fromJson(it, Data::class.java)

            _binding!!.CategoriesName.text = data.name
            _binding!!.Categoriesdescription.text = data.description
            Glide.with(requireContext()).load(data.image).into(_binding!!.icon)
            setLayoutData(data)
        }

        setLayoutForList()

    }

    private fun setLayoutForList() {
        voucherListAdapter = voucher_list_adapter(this)
        _binding!!.vouchersList.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = voucherListAdapter
        }
    }

    private fun setLayoutData(data: Data?) {
        data.let {
            getVoucherListData(it!!.id)
        }
    }

    private fun getVoucherListData(id: String) {
        viewModel.getVoucherList(getUserId(),id,"0",this,this)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SelectedVouchersListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: VoucherListRes?, mess: String?) {
        t.let {
            it!!.data.let {
                voucherListAdapter!!.addAllItem(it)
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is app.tapho.ui.BuyVoucher.VoucherListViewModel.Data){
            ContainerActivity.openContainer(requireContext(),"OpenVoucherDetailForBuy",data)
        }

    }
}