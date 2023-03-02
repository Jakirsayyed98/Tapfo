package app.tapho.ui.MiniCash.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMiniCashMiniAppListBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.MiniCash.Adapter.MiniCashNewCategoryUniversaladapter
import app.tapho.ui.MiniCash.UI.MiniCashFragmentContainerActivity
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.MiniApp
import app.tapho.utils.DATA
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class MiniCashMiniAppListFragment : BaseFragment<FragmentMiniCashMiniAppListBinding>(),RecyclerClickListener {


    private var miniAppRes: MiniAppRes? = null
    var MiniCashNewCategoryUniversaladapter : MiniCashNewCategoryUniversaladapter<MiniApp>?=null

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
        // Inflate the layout for this fragment
        _binding = FragmentMiniCashMiniAppListBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        activity?.intent?.getStringExtra(DATA).let {
            val category = Gson().fromJson(it, AppCategory::class.java)
            Glide.with(requireContext()).load(category.image).into(_binding!!.iconCard)
            _binding!!.category.text=category.name
            _binding!!.description.text=category.description
            val AppCategoryID = category.id!!
            getCategoryData(AppCategoryID)
        }

        _binding!!.backbutton.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.partenrwithUs.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "BuyGiftCard", "")
        }
//        activity?.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

        return _binding?.root
    }





    private fun getCategoryData(AppCategoryID: String) {
        val categoryID = AppCategoryID
        viewModel.getAppCategoryMiniApp(getUserId(), categoryID, "",this, object : ApiListener<MiniAppRes, Any?> {
            override fun onSuccess(t: MiniAppRes?, mess: String?) {
                    miniAppRes = t
                    t!!.let {

                        setRecyclerAppData(it.mini_app)


                    }
                }
            })
    }

    private fun getSearch() {
        _binding!!.searchEt.addTextChangedListener(object : TextWatcher {
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
            val tempList: ArrayList<MiniApp> = ArrayList()
            for (x in itemList){
                if (searchName.lowercase(Locale.getDefault()) in x.name.toString().lowercase(Locale.getDefault())){
                    tempList.add(x)
                }
            }
            MiniCashNewCategoryUniversaladapter!!.updatelist(tempList)
        }
    }


    private fun setRecyclerAppData(miniApp: List<MiniApp>?) {
        val withCashback = ArrayList<MiniApp>()
        _binding!!.Stores.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            MiniCashNewCategoryUniversaladapter = MiniCashNewCategoryUniversaladapter<MiniApp>(this@MiniCashMiniAppListFragment).apply {

            //    val withoutCashback = ArrayList<MiniApp>()

                miniAppRes?.mini_app?.forEachIndexed { index, miniApp ->
                    if (miniApp.cashback.isNullOrEmpty().not()) {
                        withCashback.add(miniApp)
                    }else{
                 //       withoutCashback.add(miniApp)
                    }
                }


                if (withCashback.isNullOrEmpty()){
                    addAllItem( miniAppRes?.mini_app!!)
                    itemList = miniAppRes?.mini_app!!
                }else{

                //    withCashback.addAll(withoutCashback)
                    addAllItem(withCashback)
                    itemList = withCashback
                    getSearch()
                }
            }
            _binding!!.storecount.text = getString(R.string._29_minicash_activated,withCashback.size.toString())
            adapter = MiniCashNewCategoryUniversaladapter
        }
    }




    companion object {
        @JvmStatic
        fun newInstance() =
            MiniCashMiniAppListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                openWebView(data)
        }

    }
    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        MiniCashFragmentContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun openWebView(data: MiniApp) {
//            ActiveCashbackActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id)
        ActiveCashbackForWebActivity.openWebView(context, data.url, data.id, data.image, data.tcash.toString(), data.is_favourite.toString(), data.cashback, data.app_category_id,data.url_type,data.name)
    }
}