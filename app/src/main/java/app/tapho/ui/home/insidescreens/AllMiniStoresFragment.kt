package app.tapho.ui.home.insidescreens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAllMiniStoresBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.home.adapter.*
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.MiniApp
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.viewmodels.FavouriteViewModel
import java.util.ArrayList


class AllMiniStoresFragment : BaseFragment<FragmentAllMiniStoresBinding>(), RecyclerClickListener {
    private var favViewModel: FavouriteViewModel? = null
    var miniAppAdapter:FavouriteHomeAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ministorebinding = FragmentAllMiniStoresBinding.inflate(inflater, container, false)

        statusBarColor(R.color.orange1)
        homeviewmodel()
        //Fav ViewModel
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        staticLayout()
        setFavourites()


        return ministorebinding!!.root
    }

    private fun homeviewmodel() {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(), this, object:ApiListener<HomeRes,Any?>{
                override fun onSuccess(t: HomeRes?, mess: String?) {
                    t!!.app_category!!.forEach {
                   Toast.makeText(requireContext(),"${it.mini_app!!.get(0).name}",Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun staticLayout() {
        val customShopCategory = AllminiAppStoresScreenAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {

                }
            }

        }).apply {

            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img1,
                    "Save battery,\nspace & memory",
                    ""
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img2,
                    "No need to\nDownload it",
                    ""
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img3,
                    "Plus earn extra\ncashback",
                    ""
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img4,
                    "Enjoy 90,000+\nBrands & Stores",
                    ""
                )
            )
        }
        ministorebinding!!.staticLayout.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = customShopCategory
        }
    }

    private fun setFavourites() {
        val mAdapter = FavouriteHomeAdapter(this)
        ministorebinding!!.myfavRV.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = mAdapter
        }
        favViewModel?.getData()?.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                mAdapter.clear()
                it.data?.let { it1 -> mAdapter.addAllItem(it1) }
            }
        })
        getFevouriteData()
    }

    private fun getFevouriteData() {
        favViewModel?.getFavouriteList(getUserId())
    }

    fun refreshFavData() {
        getFevouriteData()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AllMiniStoresFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
                    //      setAppCategory(getString(R.string.less))
                }
                getString(R.string.less) -> {
                    //       setAppCategory(getString(R.string.more))
                }
                else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                openWebView(data)
        }
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun openWebView(data: MiniApp) {
        WebViewActivity.openWebView(
            context, data.url, data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id
        )
    }

    private fun setUpAdapter(it: List<MiniApp>?) {
        val mAdapter = UniversalAdapter<MiniApp>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                //    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    openWebView(data)
                }
            }
        })


        mAdapter.setShowRupee(true)
        //mAdapter.setMoreImagePos(8)

        binding.AllMiniApp.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = mAdapter
        }
        mAdapter.addAllItem(it!!)
    }


}