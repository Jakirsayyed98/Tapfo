package app.tapho.ui.home.Categorys

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentCategoryCashbackPartnerBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.Categorys.adapter.CategoryCardAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.MiniApp
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.SET_OLD_DATA
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryCashbackPartnerFragment : BaseFragment<FragmentCategoryCashbackPartnerBinding>(),
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
        CategoryCashbackMerchantsbinding = FragmentCategoryCashbackPartnerBinding.inflate(inflater, container, false)
        getData()

        return CategoryCashbackMerchantsbinding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CategoryCashbackPartnerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun getData() {
        val oldData = activity?.intent?.getBooleanExtra(SET_OLD_DATA, false)

        activity?.intent?.getStringExtra(DATA)?.let {
            if (oldData == false) {
                val category = Gson().fromJson(it, AppCategory::class.java)
//                viewModel.getAppCategoryMiniAppwithBanner(AppSharedPreference.getInstance(requireContext()).getUserId(), category.id, this, this)
                viewModel.getAppCategoryMiniApp(getUserId(), category.id, "",this, this)
            } else {
                Gson().fromJson<ArrayList<MiniApp>>(it, object : TypeToken<ArrayList<MiniApp>>() {}.type
                )?.let {
                    setRecyclerAllApp(it, true)
                }
            }
        }
    }

    private fun setRecyclerAllApp(miniApp1: List<MiniApp>, showTopMerchants: Boolean) {

        CategoryCashbackMerchantsbinding!!.allcashbackmerchant.apply {
            //  layoutManager = GridLayoutManager(context, 4)
            layoutManager = LinearLayoutManager(context)

            adapter = CategoryCardAdapter<MiniApp>(this@CategoryCashbackPartnerFragment).apply {
                val filterdList1 = ArrayList<MiniApp>()
                miniAppRes?.mini_app?.forEachIndexed { index, miniApp ->
                    if (miniApp.cashback.isNullOrEmpty().not()) {
                        filterdList1.add(miniApp)
                    }
                }
                addAllItem(filterdList1)
            }
        }

    }

    override fun onSuccess(t: MiniAppRes?, mess: String?) {
        t?.let {
            miniAppRes = t
            it.mini_app.let { it1 -> setRecyclerAllApp(it1!!, true) }

        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                ActiveCashbackForWebActivity.openWebView(context, data.url, data.id, data.image, data.tcash.toString(), data.is_favourite.toString(), data.cashback,
                    data.app_category_id,data.url_type,data.name)
        }
    }
}