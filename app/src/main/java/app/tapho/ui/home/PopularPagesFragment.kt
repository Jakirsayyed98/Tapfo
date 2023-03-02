package app.tapho.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentPopularPagesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.adapter.PagePopularStoreAdapter
import app.tapho.ui.home.adapter.PopularsStoresAdapterWithBanner
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.OPEN_WEB_VIEW


class PopularPagesFragment : BaseFragment<FragmentPopularPagesBinding>(),ApiListener<HomeRes,Any?> ,RecyclerClickListener{

    private var popularList: ArrayList<Popular>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPopularPagesBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.progressbar.visibility = View.VISIBLE
        _binding!!.mainLayout.visibility = View.GONE

        _binding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }

        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }


        _binding!!.favouritesBtn.setOnClickListener {
            openContainer("favouritesBtn", "", false, "")
        }

        VMcallsFunctions()
        return _binding?.root
    }

    private fun VMcallsFunctions() {
        viewModel.getHomeData("home",getUserId(),this,this)
    }

    private fun setPopularMerchantswithBanner(list: ArrayList<Popular>) {
        val mAdapter = PopularsStoresAdapterWithBanner<Popular>(this)
        _binding!!.popularWithbanner.apply {
            layoutManager= LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = mAdapter
        }
        mAdapter.clear()
        mAdapter.addAllItem(if (list.size > 12) { list.subList(0, 12).shuffled() } else { list.shuffled() })

    }
    private fun setPopularMerchants(list: ArrayList<Popular>) {
        val mAdapter = PagePopularStoreAdapter<Popular>(this)
        _binding!!.popularstores.apply {
//            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = mAdapter
        }
        mAdapter.clear()
//        mAdapter.addAllItem(if (list.size > 9) { list.subList(0, 9) } else { list })
        mAdapter.addAllItem(list.shuffled())

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PopularPagesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {
       t.let {
           it!!.popular!!.let {
               popularList = it
               setPopularMerchants(it)
               setPopularMerchantswithBanner(it)
           }


           _binding!!.progressbar.visibility = View.GONE
           _binding!!.mainLayout.visibility = View.VISIBLE
       }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
//                    setAppCategory(getString(R.string.less))
                    //    setFavourites(getString(R.string.less))
                }
                getString(R.string.ShopCompair) -> {
                    openContainer("Shop & Compair", data, false, data.name)
                }
                getString(R.string.All) -> {
                    if (activity is HomeActivity)
                        (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.ShopNow)
                }
                getString(R.string.Offers) -> {
                    if (activity is HomeActivity)
                        (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.offerTab)
                }
                getString(R.string.less) -> {
//                    setAppCategory(getString(R.string.more))
                    //    setFavourites(getString(R.string.more))
                }
                else -> openContainer("SelectedCategoriesMiniAppsFragment", data, false, data.name)

                // else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp){
                openWebView(data)
            }

        }
    }


    private fun openWebView(data: MiniApp) {
        ActiveCashbackForWebActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id, data.url_type,data.name)
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
}