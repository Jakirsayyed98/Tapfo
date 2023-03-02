package app.tapho.ui.merchants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.tapho.network.Status
import app.tapho.R
import app.tapho.databinding.FragmentMerchantsListBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.CashbackMerchantsFragment
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.CashbackMerchantCategory
import app.tapho.utils.AppSharedPreference
import app.tapho.viewmodels.MerchantsAllListViewModel

class MerchantsAllListFragment : BaseFragment<FragmentMerchantsListBinding>(), RecyclerClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private var dataList: List<CashbackMerchant>? = null

    private var categoryTabAdapter: CategoryTabAdapter<CashbackMerchantCategory>? = null
   private  val merchantViewMode: MerchantsAllListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMerchantsListBinding.inflate(inflater, container, false)

        setCategoryRecycler()
        observeData()
        getData()
        return _binding?.root
    }

    private fun setCategoryRecycler() {
        binding.swipeRefresh.setOnRefreshListener(this)
        categoryTabAdapter = CategoryTabAdapter(this)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryTabAdapter
        }
    }

    private fun getData() {
       /* viewModel.getCashbackMerchantAll(AppSharedPreference.getInstance(requireContext())
            .getUserId(), "0", this, this)*/
        merchantViewMode.getCashbackMerchants(AppSharedPreference.getInstance(requireContext()).getUserId(), "0")
    }

    private fun observeData(){
        merchantViewMode.getValue().observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    showLoader()
                }
                Status.ERROR -> {
                    binding.swipeRefresh.isEnabled = false
                    dismissLoader()
                    showMess(it.message)
                }
                Status.SUCCESS -> {
                    dismissLoader()
                    setData(it.data)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MerchantsAllListFragment()
    }

    /*override fun onSuccess(t: CashbackMerchantsAllRes?, mess: String?) {
        t?.let {
            binding.swipeRefresh.isEnabled = false
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
*/

    private fun setData(t: CashbackMerchantsAllRes?){
        t?.let {
            binding.swipeRefresh.isEnabled = false
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
        val fragment = CashbackMerchantsFragment.newInstance(list, false,false)
        childFragmentManager.beginTransaction().replace(R.id.containerMerchants, fragment).commit()
    }

    override fun onRefresh() {
        binding.containerMerchants.removeAllViews()
        categoryTabAdapter?.clear()
        getData()
        binding.swipeRefresh.isRefreshing = false
    }
}