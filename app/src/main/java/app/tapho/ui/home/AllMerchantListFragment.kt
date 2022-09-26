package app.tapho.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAllMerchantListBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.NewAdapter.couponCategoriesAdapter
import app.tapho.ui.home.adapter.HomeMerchantAdapter
import app.tapho.ui.home.adapter.HomePageOfferListAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular


class AllMerchantListFragment : BaseFragment<FragmentAllMerchantListBinding>() {

    private var CategoriesAdapter: couponCategoriesAdapter<AppCategory>? = null
    private var HomeMerchantAdapter: HomeMerchantAdapter? = null


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
        _binding = FragmentAllMerchantListBinding.inflate(inflater,container,false)
        setClick()
        setAppCategoryData()
        getCategories()
        setMerchants()
        return _binding?.root
    }

    private fun setClick() {
        _binding!!.onback.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        _binding!!.favouritesBtn.setOnClickListener {
          ContainerActivity.openContainer(requireContext(),"favouritesBtn", "", false, "")
        }
    }

    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()
        if (type == "1")
            SearchDialogFragment.showSearch(childFragmentManager, list)

    }
    // Main Category
    private fun getCategories() {
        viewModel.getHomeData("home",getUserId(),this,object : ApiListener<HomeRes, Any?> {
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t!!.app_category!!.get(0).isSelected=true
                getCategoryMerchantData(t!!.app_category!!.get(0).id.toString())

                CategoriesAdapter!!.addAllItem(t?.app_category!!)

            }

        })

    }

    fun setAppCategoryData() {
        CategoriesAdapter = couponCategoriesAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                getCategoryMerchantData(data.toString())
            }

        })
        _binding!!.Categories.apply { layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoriesAdapter
        }
    }

    private fun getCategoryMerchantData(appId: String) {
        viewModel.getAppCategoryMiniApp(getUserId(), appId, this,
            object : ApiListener<MiniAppRes, Any?> {
                override fun onSuccess(t: MiniAppRes?, mess: String?) {
                    var TempList: ArrayList<MiniApp> = ArrayList()
                    t?.mini_app.let {
                        it!!.forEach {
                            if (it.tcash!!.contains("1")){
                                TempList.add(it)
                            }

                        }
                    }
                    HomeMerchantAdapter!!.clear()
                    HomeMerchantAdapter!!.addAllItem(/*t!!.mini_app!!*/ TempList)
                    HomeMerchantAdapter!!.notifyDataSetChanged()
                }
            })
    }

    fun setMerchants() {
        HomeMerchantAdapter = HomeMerchantAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp)
                    openWebView(data)
            }

        })
        _binding!!.Merchants.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = HomeMerchantAdapter
        }
    }

//end

    private fun openWebView(data: MiniApp) {

        WebViewActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AllMerchantListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}