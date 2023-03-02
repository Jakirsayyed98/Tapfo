package app.tapho.ui.merchants

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentNewMerchantCategoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.merchants.adapter.NewCategoryUniversaladapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.MiniApp
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList


class NewMerchantCategoryFragment : BaseFragment<FragmentNewMerchantCategoryBinding>(),
    ApiListener<MiniAppRes, Any?>,
    RecyclerClickListener {
    private var miniAppRes: MiniAppRes? = null
    var categoryid=""
    var NewCategoryUniversaladapter : NewCategoryUniversaladapter<MiniApp>?=null

    private lateinit var itemList: List<MiniApp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingNewCat = FragmentNewMerchantCategoryBinding.inflate(inflater, container, false)
        getData()
//        activity?.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        statusBarColor(R.color.grey_v_light)

        initdata()
        bindingNewCat!!.backbutton.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return bindingNewCat?.root
    }


    private fun initdata() {
        bindingNewCat!!.searchEt.addTextChangedListener(object : TextWatcher {
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
        if (!itemList.isNullOrEmpty()){

            var tempList: ArrayList<MiniApp> = ArrayList()
            for (x in itemList) {
                if (searchName.lowercase(Locale.getDefault()) in x.name.toString()
                        .lowercase(Locale.getDefault())
                ) {
                    tempList.add(x)
                }
            }
            NewCategoryUniversaladapter!!.updatelist(tempList)

        }

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            NewMerchantCategoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun getData() {
        val oldData = activity?.intent?.getBooleanExtra(SET_OLD_DATA, false)

        activity?.intent?.getStringExtra(DATA)?.let {
            if (oldData == false) {
                val category = Gson().fromJson(it, AppCategory::class.java)
                categoryid=category.id!!
                Glide.with(requireContext()).load(category.image).placeholder(R.drawable.loding_app_icon).circleCrop().into(bindingNewCat!!.iconCard)
                bindingNewCat!!.Title.text=activity?.intent?.getStringExtra(TITLE)
                viewModel.getAppCategoryMiniApp(AppSharedPreference.getInstance(requireContext()).getUserId(),"0", category.id, this, this)
            } else {
                Gson().fromJson<ArrayList<MiniApp>>(
                    it,
                    object : TypeToken<ArrayList<MiniApp>>() {}.type
                )?.let {
                    setRecyclerAllApp(it, true)
                }
            }
        }
    }

    private fun setRecyclerAllApp(miniApp1: List<MiniApp>, showTopMerchants: Boolean) {
        itemList = miniApp1
        bindingNewCat!!.allMerchant.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            NewCategoryUniversaladapter = NewCategoryUniversaladapter<MiniApp>(this@NewMerchantCategoryFragment).apply {
                val withCashback = ArrayList<MiniApp>()
                val withoutCashback = ArrayList<MiniApp>()
                miniAppRes?.mini_app?.forEachIndexed { index, miniApp ->
                    if (miniApp.cashback.isNullOrEmpty().not()) {
                        withCashback.add(miniApp)
                    }else{
                        withoutCashback.add(miniApp)
                    }
                }
                if (withCashback.isNullOrEmpty()){
                    addAllItem( miniAppRes?.mini_app!!)
                }else{
                    bindingNewCat!!.totalStore.text = getString(R.string.currently_20_partners_offer_extra_cashback,withCashback.size.toString())
                    withCashback.addAll(withoutCashback)
                    bindingNewCat!!.totalMerchants.text = getString(R.string.total_219_merchants,withCashback.size.toString())
                    addAllItem(withCashback)
                }

            }
            adapter = NewCategoryUniversaladapter
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
                    data.app_category_id,data.url_type,data.name
                )
        }

    }
}