package app.tapho.ui.home.Categorys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentCategoryMerchantsBinding
import app.tapho.databinding.FragmentMiniAppBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.BaseRes
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.merchants.MiniAppFragment
import app.tapho.ui.merchants.adapter.SubCategoryAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.AppSubCategory
import app.tapho.ui.model.MiniApp
import app.tapho.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CategoryMerchantsFragment :  BaseFragment<FragmentCategoryMerchantsBinding>(),
    ApiListener<MiniAppRes, Any?>,
    RecyclerClickListener {
    private var miniAppRes: MiniAppRes? = null
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
        CategoryMerchantsbinding = FragmentCategoryMerchantsBinding.inflate(inflater, container, false)
        getData()
        return CategoryMerchantsbinding?.root
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            CategoryMerchantsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun getData() {
        val oldData = activity?.intent?.getBooleanExtra(SET_OLD_DATA, false)

        activity?.intent?.getStringExtra(DATA)?.let {
            if (oldData == false) {
                val category = Gson().fromJson(it, AppCategory::class.java)
                viewModel.getAppCategoryMiniApp(
                    AppSharedPreference.getInstance(requireContext()).getUserId(),
                    category.id,"0",
                    this,
                    this
                )
            } else {
                Gson().fromJson<ArrayList<MiniApp>>(it, object : TypeToken<ArrayList<MiniApp>>() {}.type)?.let {
                    setRecyclerAllApp(it, true)
                }
            }
        }
    }

    private fun setRecyclerAllApp(miniApp1: List<MiniApp>, showTopMerchants: Boolean) {
        CategoryMerchantsbinding!!.allMerchant.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = UniversalAdapter<MiniApp>(this@CategoryMerchantsFragment).apply {
                val filterdList1 = ArrayList<MiniApp>()
                miniAppRes?.mini_app?.forEachIndexed { index, miniApp ->
                        filterdList1.add(miniApp)
                }
                addAllItem(filterdList1)
            }
        }

    }

    override fun onSuccess(t: MiniAppRes?, mess: String?) {
        t?.let {
            miniAppRes = t
            it.mini_app?.let { it1 -> setRecyclerAllApp(it1, true) }

        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                ActiveCashbackForWebActivity.openWebView(
                    context,
                    data.url,
                    data.id,
                    data.image,
                    data.tcash,
                    data.is_favourite,
                    data.cashback,
                    data.app_category_id,data.url_type,data.name)
        }

    }
}