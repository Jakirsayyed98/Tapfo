package app.tapho.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentGameSearchBinding
import app.tapho.databinding.FragmentSearch2Binding
import app.tapho.databinding.FragmentSearchBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.games.adapter.SearchGames
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.home.adapter.Universaladapter2
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : BaseFragment<FragmentSearch2Binding>(),
    RecyclerClickListener {
    var MiniAppID = 1

    private var MiniAppSearch: Universaladapter2<app.tapho.ui.model.MiniApp>? = null//
    private var MiniAppSearchre: UniversalAdapter<app.tapho.ui.model.MiniApp>? = null//
    private lateinit var itemList: List<app.tapho.ui.model.MiniApp>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingForGames = FragmentSearch2Binding.inflate(inflater, container, false)
        viewmodeldataset()
        initdata()
        AllGamesAdapter()
        SearchResult()
        searchViewModeldata("")
        return bindingForGames?.root
    }

    private fun viewmodeldataset() {
        viewModel.getAppCategoryMiniApp(
            AppSharedPreference.getInstance(requireContext()).getUserId(),
            MiniAppID.toString(), this, object : ApiListener<MiniAppRes, Any?> {
                override fun onSuccess(t: MiniAppRes?, mess: String?) {
                    var tempList: ArrayList<MiniApp> = ArrayList()
               //     itemList = t!!.mini_app
                    t.let {
                        it?.data?.forEach {
                            Log.d("NewCashbackMiniApp","$it")
                        }
                        t?.mini_app?.forEach {

                            MiniAppSearch!!.addItem(it)
                            tempList.add(it)
                        }
//                        MiniAppID++
//                        viewmodeldataset()
                    }
                }

            })
    }

    private fun initdata() {
        bindingForGames!!.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                searchViewModeldata(p0.toString())
             filterListData(p0.toString())
            }
        })
    }

    private fun searchViewModeldata(stringdata: String) {
        viewModel.searchMiniApp(AppSharedPreference.getInstance(requireContext()).getUserId(),stringdata,this,object : ApiListener<WebTCashRes,Any?>{
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                t.let {it1->
                    it1?.mini_app.let {
                        MiniAppSearchre?.addAllItem(it!!)
                        itemList=it!!
                    }
                    it1?.mini_app?.forEach {
                      //  MiniAppSearchre?.addItem(it)

                    }

                }
            }

        })
    }

    internal fun filterListData(searchName: String) {
        var tempList: ArrayList<app.tapho.ui.model.MiniApp> = ArrayList()
        for (x in itemList) {
            if (searchName.lowercase(Locale.getDefault()) in x.name!!.lowercase(Locale.getDefault())) {
                tempList.add(x)
            }
        }
        MiniAppSearch!!.updatelist(tempList)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun AllGamesAdapter() {
        MiniAppSearch = Universaladapter2(this)
        bindingForGames!!.MiniApps.apply {
            layoutManager = GridLayoutManager(activity, 4)
            adapter = MiniAppSearch
            adapter?.notifyDataSetChanged()
        }
        bindingForGames!!.MiniApps.setNestedScrollingEnabled(false);
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun SearchResult() {
        MiniAppSearchre = UniversalAdapter(this)
        bindingForGames!!.recycler.apply {
            layoutManager = GridLayoutManager(activity, 4)
            adapter = MiniAppSearchre
            adapter?.notifyDataSetChanged()
        }
        bindingForGames!!.recycler.setNestedScrollingEnabled(false);
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW)
            if (data is MiniApp)
                WebViewActivity.openWebView(
                    context,
                    data.url,
                    data.id,
                    data.image,
                    data.tcash.toString(),
                    data.is_favourite.toString(), data.cashback, data.app_category_id
                )
    }

}