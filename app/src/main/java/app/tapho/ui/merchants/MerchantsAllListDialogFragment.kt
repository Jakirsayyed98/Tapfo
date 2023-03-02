package app.tapho.ui.merchants

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.tapho.network.Status
import app.tapho.R
import app.tapho.databinding.FragmentMerchantsListDialogBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.home.CashbackMerchantsFragment
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.CashbackMerchantCategory
import app.tapho.utils.AppSharedPreference
import app.tapho.viewmodels.MerchantsAllListViewModel

class MerchantsAllListDialogFragment : DialogFragment(), RecyclerClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private var dataList: List<CashbackMerchant>? = null
    private var binding: FragmentMerchantsListDialogBinding? = null
    private val merchantViewMode: MerchantsAllListViewModel by viewModels()
    private var categoryTabAdapter: CategoryTabAdapter<CashbackMerchantCategory>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FAFAFA")))
        binding = FragmentMerchantsListDialogBinding.inflate(inflater, container, false)
        setCategoryRecycler()
        observeData()
        getData()
        return binding?.root
    }

    private fun setCategoryRecycler() {
        binding?.toolbar?.backIv?.setOnClickListener { dismiss() }
        binding?.toolbar?.tvTitle?.text = getString(R.string.cashback_merchants)
        binding?.swipeRefresh?.setOnRefreshListener(this)
        categoryTabAdapter = CategoryTabAdapter(this)
        binding?.recycler?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryTabAdapter
        }
    }

    private fun getData() {
        merchantViewMode.getCashbackMerchants(AppSharedPreference.getInstance(requireContext()).getUserId(), "0")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MerchantsAllListDialogFragment()
    }

    private fun observeData() {
        merchantViewMode.getValue().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding?.shimmerViewContainer?.visibility = View.VISIBLE
                    binding?.shimmerViewContainer?.startShimmer()
                }
                Status.ERROR -> {
                    binding?.swipeRefresh?.isEnabled = false
                    binding?.shimmerViewContainer?.visibility = View.GONE
                    binding?.shimmerViewContainer?.stopShimmer()
                }
                Status.SUCCESS -> {
                    binding?.shimmerViewContainer?.visibility = View.GONE
                    binding?.shimmerViewContainer?.stopShimmer()
                    setData(it.data)
                }
            }
        })
    }

    @SuppressLint("LogConditional")
    private fun setData(t: CashbackMerchantsAllRes?) {
        t?.let {
            binding?.swipeRefresh?.isEnabled = false
            it.cashback_merchant_category?.let { categoryList ->
                categoryTabAdapter?.addItem(CashbackMerchantCategory("", "", "All", "", true))
                categoryTabAdapter?.addAllItem(categoryList)
            }
            dataList = it.data
            dataList?.let { dataList ->
                addFragment(dataList)
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is CashbackMerchantCategory) {
            val list = ArrayList<CashbackMerchant>()
            if (data.id.isNullOrEmpty().not())
                dataList?.forEach {
                    if (it.cashback_merchant_category_id == data.id) {
                        list.add(it)
                    }
                }
            else
                dataList?.let { list.addAll(it) }
            addFragment(list)
        }
    }

    private fun addFragment(list: List<CashbackMerchant>) {
        val fragment = CashbackMerchantsFragment.newInstance(list, false, showBigView = true)
        childFragmentManager.beginTransaction().replace(R.id.containerMerchants, fragment).commit()
    }

    override fun onRefresh() {
        binding?.containerMerchants?.removeAllViews()
        categoryTabAdapter?.clear()
        getData()
        binding?.swipeRefresh?.isRefreshing = false
    }
}