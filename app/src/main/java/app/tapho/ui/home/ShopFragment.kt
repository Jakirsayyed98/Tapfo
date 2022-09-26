package app.tapho.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentShopBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.FavouriteHomeAdapter
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.*
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.viewmodels.FavouriteViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel

class ShopFragment : BaseFragment<FragmentShopBinding>() , ApiListener<HomeRes, Any?>,RecyclerClickListener{

    private var popularList: ArrayList<Popular>? = null
    private var itemTypeAdapter: ItemTypeAdapter? = null

    private var appCategoryList: ArrayList<AppCategory>? = null
    private var popularbanner: ArrayList<CashbackMerchant>? = null
    private var newcashback: ArrayList<NewCashback>? = null
    private var favViewModel: FavouriteViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        statusBarColor(R.color.white)
        shopBinding=FragmentShopBinding.inflate(inflater, container, false)
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        shopBinding!!.searchTv.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        shopBinding!!.offertab.setOnClickListener {
                if (activity is HomeActivity)
                    (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.offerTab)
        }
        getData()
        setFavourites()
        setRecyclerBrand()
        return shopBinding!!.root
    }

    private fun getData() {
        viewModel.getHomeData("home", AppSharedPreference.getInstance( requireContext() ) .getUserId(), this, this )
    }
    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()
        popularList?.let { list.addAll(it) }
        if (type == "1")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        else
            openContainer(getString(R.string.popular_merchants), list, true, getString(R.string.popular_merchants))
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShopFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {
        t?.let {
            appCategoryList = it.app_category
            setAppCategory(getString(R.string.more))
            it.new_cashback.let { NewCashback->
                setPopularbannersetdata(NewCashback)
            }

            it.cashback_merchant.let {popularbanner1->
             //   setPopularbannerset(popularbanner1)
            }

        }
    }

    private fun setPopularbannersetdata(newCashback: java.util.ArrayList<NewCashback>?) {
        val imageList = ArrayList<SlideModel>()

        var data1=newCashback!!.get(1).mini_app!!.image
        Log.d("Banner","$data1")

        newcashback=newCashback
        if (newCashback != null) {
            for (x in newCashback) {
                Log.d("cashback_merchant_category_id","${x.cashback_merchant_category_id}")

                Log.d("data","${x.status}")

                if (x.cashback_merchant_category_id!!.contains("1")){
                if(x.cashback_merchant_category_id!!.toInt()==1) {
                    shopBinding!!.popularbanner.visibility=View.VISIBLE
                    imageList.add(SlideModel(x.image, x.cashback, ScaleTypes.CENTER_CROP))
                }
                }else{
                    shopBinding!!.popularbanner.visibility=View.GONE
                }
            }
        }else{
            Toast.makeText(context,"Refresh now",Toast.LENGTH_SHORT).show()
        }

        shopBinding!!.popularbanner.setImageList(imageList)

        shopBinding!!.popularbanner.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {

                if (newCashback!!.get(position).mini_app!!.url.toString().contains("https://")){
                    WebViewActivity.openWebView(context,newCashback.get(position).mini_app!!.url,"","","","","","")
                }else if(newCashback.get(position).mini_app!!.url.toString().contains("http://")){
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newCashback.get(position).mini_app!!.url.toString()))
                    startActivity(browserIntent)
                }
            }
        })
    }
/*
//    private fun setPopularbannerset(popularbanner1: java.util.ArrayList<CashbackMerchant>?) {
//        val imageList = ArrayList<SlideModel>()
//
//        var data1=popularbanner1!!.get(1).mini_app!!.image
//        Log.d("Banner","$data1")
//
//        popularbanner=popularbanner1
//        if (popularbanner1 != null) {
//            for (x in popularbanner1) {
//                Log.d("cashback_merchant_category_id","${x.cashback_merchant_category_id}")
//                if(x.cashback_merchant_category_id!!.toInt()==1) {
//                    //imageList.add(SlideModel(x.cashback_merchant_category_id?.get(1)))
//                    imageList.add(SlideModel(x.image, x.cashback, ScaleTypes.CENTER_CROP))
//                }
//            }
//        }else{
//            Toast.makeText(context,"Refresh now",Toast.LENGTH_SHORT).show()
//        }
//
//        shopBinding!!.popularbanner.setImageList(imageList)
//
//        shopBinding!!.popularbanner.setItemClickListener(object : ItemClickListener {
//            override fun onItemSelected(position: Int) {
//
//                if (popularbanner1!!.get(position).mini_app!!.url.toString().contains("https://")){
//                    WebViewActivity.openWebView(context,popularbanner1.get(position).mini_app!!.url,"","","","","")
//                }else if(popularbanner1.get(position).mini_app!!.url.toString().contains("http://")){
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(popularbanner1.get(position).mini_app!!.url.toString()))
//                    startActivity(browserIntent)
//                }
//            }
//        })
//
//    }

//    private fun setPopularbannerset(popularbanner1: ArrayList<CashbackMerchant>?) {
//        val imageList = ArrayList<SlideModel>()
//
//        var data1=popularbanner1!!.get(1).mini_app!!.image
//        Log.d("Banner","$data1")
//
//        popularbanner=popularbanner1
//        if (popularbanner1 != null) {
//            for (x in popularbanner1) {
//                imageList.add(SlideModel(x.image,x.cashback, ScaleTypes.CENTER_CROP))
//
//            }
//        }else{
//            Toast.makeText(context,"Refresh now",Toast.LENGTH_SHORT).show()
//        }
//
//        shopBinding!!.popularbanner.setImageList(imageList)
//
//        shopBinding!!.popularbanner.setItemClickListener(object : ItemClickListener {
//            override fun onItemSelected(position: Int) {
//
//                if (popularbanner1!!.get(position).mini_app!!.url.toString().contains("https://")){
//                    WebViewActivity.openWebView(context,popularbanner1.get(position).mini_app!!.url,"","","","","")
//                }else if(popularbanner1.get(position).mini_app!!.url.toString().contains("http://")){
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(popularbanner1.get(position).mini_app!!.url.toString()))
//                    startActivity(browserIntent)
//                }
//            }
//        })
//    }
*/

    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()
        appCategoryList?.let { appCategory ->
                itemTypeAdapter?.addAllItem(appCategory)
        }
    }
    private fun setRecyclerBrand() {
        itemTypeAdapter = ItemTypeAdapter(this)
        shopBinding!!.recyclerAppCategory.apply {
            layoutManager = GridLayoutManager(
                context,
                5
            )
            adapter = itemTypeAdapter
        }
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
                else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                openWebView(data)
        }
    }
    private fun openWebView(data: MiniApp) {
        WebViewActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,data.app_category_id
        )
    }

    private fun setFavourites() {
        val mAdapter = FavouriteHomeAdapter(this)
        shopBinding!!.recyclerFavourites.apply {
            layoutManager = GridLayoutManager(context, 4)
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
}