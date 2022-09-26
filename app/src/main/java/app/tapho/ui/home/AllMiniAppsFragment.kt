package app.tapho.ui.home

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAllMiniAppsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.home.NewAdapter.couponCategoriesAdapter
import app.tapho.ui.home.adapter.AllMiniAppSearchAdapter
import app.tapho.ui.home.adapter.FavouriteHomeAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import java.util.*
import kotlin.collections.ArrayList


class AllMiniAppsFragment : BaseFragment<FragmentAllMiniAppsBinding>() {

    var MiniAppList : ArrayList<MiniApp> = ArrayList()
    var categoryAdapter : couponCategoriesAdapter<AppCategory>? = null
    var Merchantadapter : AllMiniAppSearchAdapter<MiniApp>?=null
    private var popularList: ArrayList<Popular>? = null

    private lateinit var itemList:List<MiniApp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       AllMiniApps = FragmentAllMiniAppsBinding.inflate(inflater,container,false)

        statusBarColor(R.color.appfobackground)
        gethomeViewModel()
        setCategoryAdapter()
        getSearch()
        progressVisible()
//        AllMiniApps!!.searchTool.setOnClickListener {
//            openAllPopularCashbackMerchants("1")
//        }

        AllMiniApps!!.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        return AllMiniApps?.root
    }

    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()
        popularList?.let {
            list.addAll(it)
        }
        if (type == "1")
        //  openContainer("MiniAppSearch", list, false, "data.name")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        //    SearchFragment.showSearch(childFragmentManager, list)
        else
            openContainer(
                getString(R.string.popular_merchants),
                list,
                true,
                getString(R.string.popular_merchants)
            )
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun getSearch() {
        AllMiniApps!!.searchTv.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterListData(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
//                filterListData(p0.toString())
            }

        })
    }
    private fun filterListData(searchName: String) {
        if (itemList.isNullOrEmpty().not()){
            var tempList: ArrayList<MiniApp> = ArrayList()
            for (x in itemList){
                if (searchName.lowercase(Locale.getDefault()) in x.name.toString().lowercase(Locale.getDefault())){
                    tempList.add(x)
                }
            }
            Merchantadapter!!.updatelist(tempList)
        }
    //   AllGames!!.updatelist(tempList)
    }

    private fun gethomeViewModel() {
        viewModel.getHomeData("home",getUserId(),this,object:ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t.let {
                    categoryAdapter!!.addItem(AppCategory("","","","","","","0","","All","",true,null))
                    categoryAdapter!!.addAllItem(it!!.app_category!!)
                    it!!.app_category?.forEach {
                        getthisCategoryMiniApp(it.id)
                    }
                }
            }
        })
    }

    private fun getthisCategoryMiniApp(categoryid: String?) {
        viewModel.getAppCategoryMiniApp(getUserId(),categoryid,this,object : ApiListener<MiniAppRes,Any?>{
            override fun onSuccess(t: MiniAppRes?, mess: String?) {
                t.let {
                    MiniAppList.addAll(it!!.mini_app!!.shuffled())
                }
                setAllAppList()

            }

        })
    }

    private fun setCategoryAdapter() {
        categoryAdapter = couponCategoriesAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                MiniAppList.clear()
                if (data.toString().equals("0")){
                    gethomeViewModel()
                }else
                getthisCategoryMiniApp(data.toString())
            }
        })
        AllMiniApps!!.category.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = categoryAdapter
        }
    }

    private fun setAllAppList() {
        Merchantadapter = AllMiniAppSearchAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp){
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

            }

        })

        Merchantadapter!!.addAllItem(MiniAppList)
        itemList = MiniAppList
        progressInVisible()
        AllMiniApps!!.merchant.apply {
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = Merchantadapter
        }

    }

    fun progressVisible(){
        AllMiniApps!!.progress.visibility = View.VISIBLE
    }


    fun progressInVisible() {
        Handler().postDelayed(Runnable {
            AllMiniApps!!.progress.visibility = View.INVISIBLE
            AllMiniApps!!.merchant.visibility = View.VISIBLE

        },1000)


    }


    companion object {
        @JvmStatic
        fun newInstance() =
            AllMiniAppsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}