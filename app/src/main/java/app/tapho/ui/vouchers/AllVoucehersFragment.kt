package app.tapho.ui.vouchers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAllVoucehersBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VoucherListViewModel.VoucherListRes
import app.tapho.ui.merchants.adapter.SubCategoryTabsAdapter
import app.tapho.ui.model.AppSubCategory
import app.tapho.ui.vouchers.adapter.VoucherListAdapter
import app.tapho.ui.vouchers.model.VoucherListData
import app.tapho.utils.DATA

class AllVoucehersFragment : BaseFragment<FragmentAllVoucehersBinding>(), RecyclerClickListener,
    ApiListener<VoucherListRes, Any?> {
    private var subCatTabsAdapter: SubCategoryTabsAdapter? = null
    private var res: VoucherListRes? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllVoucehersBinding.inflate(layoutInflater, container, false)
        init()
        return _binding?.root
    }

    private fun init() {
        subCatTabsAdapter = SubCategoryTabsAdapter(this)
        binding.recyclerSubCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = subCatTabsAdapter
        }
        if (res == null) {
            subCatTabsAdapter?.addItem(AppSubCategory("", "", "", "", "", "", "", true))
            subCatTabsAdapter?.addItem(AppSubCategory("", "", "", "", "", "", "", false))
        }

        if (res == null)
            getData("0")
        else
            setData()
    }

    private fun addVouchers(list: List<VoucherListData>) {
        binding.recyclerVouchers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = VoucherListAdapter(this@AllVoucehersFragment).apply {
                addAllItem(list)
            }
        }
    }

    private fun getData(subCatId: String) {
        viewModel.getVoucherList(getSharedPreference().getUserId(),
            arguments?.getString(DATA),
            subCatId,
            this,
            this)
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "sub_cat_click") {

        } else {
            if (parentFragment is NavHostFragment) {
                (parentFragment as NavHostFragment).navController.navigate(R.id.action_allVoucehersFragment_to_voucherDetailFragment)
            }
        }
    }

    override fun onSuccess(t: VoucherListRes?, mess: String?) {
        res = t
        t?.let {
            setData()
        }
    }

    private fun setData() {
        res?.let {
//            it.voucher_sub_category?.let { it1 -> subCatTabsAdapter?.addAllItem(it1) }
//            it.data?.let { it1 -> addVouchers(it1) }
        }
    }

}