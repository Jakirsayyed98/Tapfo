package app.tapho.ui.home.Recommended

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentHomeBinding
import app.tapho.databinding.FragmentRecommendedBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.home.adapter.RecommendedAppCategoryAdapter
import app.tapho.ui.home.adapter.RecommendedUniversalAdapter
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.merchants.adapter.SubCategoryTabsAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.AppSubCategory
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW


class RecommendedFragment : BaseFragment<FragmentRecommendedBinding>(), ApiListener<HomeRes, Any?>,
    RecyclerClickListener {
    var categoryId="0"
    var SubcategoryId="0"

    private var itemTypeAdapter: RecommendedAppCategoryAdapter? = null
    private var appCategoryList: ArrayList<AppCategory>? = null
    private var subCatTabsAdapter: SubCategoryTabsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         recommendedbinding = FragmentRecommendedBinding.inflate(inflater, container, false)
        statusBarColor(R.color.orange1)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        mainCategoryViewmodel()
        setRecyclerBrand()
        recommendedbinding!!.backiv.setOnClickListener {
            activity?.onBackPressed()
        }

   //     subCatViewModelData(categoryId)
        return recommendedbinding?.root
    }

    private fun mainCategoryViewmodel() {
        viewModel.getHomeData("home",AppSharedPreference.getInstance(requireContext()).getUserId(),this,
            object : ApiListener<HomeRes, Any?> {
                override fun onSuccess(t: HomeRes?, mess: String?) {
                    t?.app_category.let {
                        appCategoryList?.let {

                        }
                        subCatViewModelData(it!!.get(0).id.toString())
                        itemTypeAdapter?.addAllItem(it)
                    }
                }
            })
    }


    private fun subCatViewModelData(categoryId: String) {
        viewModel.getAppCategoryMiniApp(AppSharedPreference.getInstance(requireContext()).getUserId(),categoryId,this,object :ApiListener<MiniAppRes,Any?>{
            override fun onSuccess(t: MiniAppRes?, mess: String?) {
                var filterdList = ArrayList<MiniApp>()
                t?.mini_app.let {MiniAppData->
                    MiniAppData?.forEach {
                        if (it.cashback.isNullOrEmpty().not()){
                            filterdList.add(it)
                        }
                    }
                    setRecyclerAllApp(filterdList)

                }

            }

        })
    }

    private fun setRecyclerBrand() {
        itemTypeAdapter = RecommendedAppCategoryAdapter(object :RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                subCatViewModelData(data.toString())
            }

        })
        recommendedbinding!!.recyclerAppCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            //    layoutManager = GridLayoutManager(context, 5)
            adapter = itemTypeAdapter
        }
    }


    private fun setRecyclerAllApp(it: List<MiniApp>?) {
        recommendedbinding!!.allMerchant.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = RecommendedUniversalAdapter<MiniApp>(this@RecommendedFragment).apply {
                val filterdList1 = ArrayList<MiniApp>()
                it?.forEachIndexed { index, miniApp ->
                    filterdList1.add(miniApp)
                }
                addAllItem(filterdList1)
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            RecommendedFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                WebViewActivity.openWebView(context,
                    data.url,
                    data.id,
                    data.image,
                    data.tcash,
                    data.is_favourite,
                    data.cashback,data.app_category_id)
        }
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {



    }
}