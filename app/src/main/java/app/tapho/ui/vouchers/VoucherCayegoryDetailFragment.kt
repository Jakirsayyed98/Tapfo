package app.tapho.ui.vouchers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentVoucherCategoryDetailBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VoucherListViewModel.VoucherListRes
import app.tapho.ui.merchants.adapter.SubCategoryTabsAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.AppSubCategory
import app.tapho.ui.vouchers.adapter.ProductVoucherListAdapter
import app.tapho.ui.vouchers.model.VoucherListData
import app.tapho.utils.DATA
import com.google.gson.Gson

class VoucherCayegoryDetailFragment : BaseFragment<FragmentVoucherCategoryDetailBinding>(),
    RecyclerClickListener, ApiListener<VoucherListRes, Any?> {
    private var res: VoucherListRes? = null
    private var subCatTabsAdapter: SubCategoryTabsAdapter? = null
    private var voucherId: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVoucherCategoryDetailBinding.inflate(inflater, container, false)

        init()
        setSubCategories()
        return _binding?.root
    }

    private fun init() {
        Gson().fromJson(arguments?.getString(DATA), AppCategory::class.java)?.let {
            voucherId = it.id
            binding.brandNameTv.text = it.name
        }
    }

    private fun setSubCategories() {
        if (res == null)
            subCatTabsAdapter = SubCategoryTabsAdapter(this)
        binding.recyclerSubCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = subCatTabsAdapter
        }
        if (res == null) {
            subCatTabsAdapter?.addItem(AppSubCategory("", "", "", "", "", "", "", true))
            subCatTabsAdapter?.addItem(AppSubCategory("", "", "", "", "", "", "", false))
            getData("0")
        } else
            setData()
    }

    private fun addVouchers(list: List<VoucherListData>) {
        binding.recyclerVouchers.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ProductVoucherListAdapter(this@VoucherCayegoryDetailFragment).apply {
                addAllItem(list)
            }
        }
    }

    private fun getData(subCatId: String) {
        viewModel.getVoucherList(getSharedPreference().getUserId(),
            voucherId,
            subCatId,
            this,
            this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VoucherCayegoryDetailFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "detail") {
            if (parentFragment is NavHostFragment) {
                (parentFragment as NavHostFragment).navController.navigate(R.id.action_voucherCategoryDetailFragment_to_voucherDetailFragment,
                    Bundle().apply { putString(DATA, Gson().toJson(data)) })
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