package app.tapho.ui.vouchers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentVouchersCategoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.CategoriesModel.VoucherCategoriesViewmodelRes
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.vouchers.model.VoucherCategoryRes
import app.tapho.utils.DATA
import com.google.gson.Gson

class VouchersCategoryFragment : BaseFragment<FragmentVouchersCategoryBinding>(), RecyclerClickListener, ApiListener<VoucherCategoriesViewmodelRes, Any?> {
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var appCategoryList: List<AppCategory>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVouchersCategoryBinding.inflate(layoutInflater, container, false)
        init()
        return _binding?.root
    }

    private fun init() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
        setRecyclerBrand()
    }

    private fun setRecyclerBrand() {
        itemTypeAdapter = ItemTypeAdapter(this)
        binding.recyclerVouchers.apply {
            layoutManager = GridLayoutManager(
                context,
                4
            )
            adapter = itemTypeAdapter
        }
        if (appCategoryList == null)
            getCategory()
        else
            setAppCategory(getString(R.string.more))
    }

    private fun getCategory() {
        viewModel.getVoucherCategory(getSharedPreference().getUserId(), this, this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VouchersCategoryFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
                    setAppCategory(getString(R.string.less))
                }
                getString(R.string.less) -> {
                    setAppCategory(getString(R.string.more))
                }
                else -> {
                    if (parentFragment is NavHostFragment)
                        (parentFragment as NavHostFragment).navController
                            .navigate(R.id.action_allVoucehersFragment_to_voucherDetailFragment,
                                Bundle().apply {
                                    putString(DATA, Gson().toJson(data))
                                })
                }
            }
        }
    }

    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()
        appCategoryList?.let { appCategory ->
            if (appCategory.size > 8 && moreText == getString(R.string.more))
                itemTypeAdapter?.addAllItem(
                    appCategory.subList(0, 7).toList()
                )
            else
                itemTypeAdapter?.addAllItem(appCategory)
            itemTypeAdapter?.addItem(AppCategory("", "", "", "", "", "", "", "", moreText, "",false,null))
        }
    }

    override fun onSuccess(t: VoucherCategoriesViewmodelRes?, mess: String?) {
        t?.let {
//            appCategoryList = t.data.
//            setAppCategory(getString(R.string.more))
        }
    }
}