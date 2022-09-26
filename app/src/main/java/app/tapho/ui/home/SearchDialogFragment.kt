package app.tapho.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.FragmentSearchBinding
import app.tapho.focuschange
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity

import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.home.adapter.SuperLinksSubCategories
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.home.adapter.searchUniversalAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.toast
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchDialogFragment : DialogFragment(), ApiListener<WebTCashRes, Any?>, LoaderListener,
    RecyclerClickListener {


    private var binding: FragmentSearchBinding? = null
    private val handler = Handler(Looper.getMainLooper())
    private val receiver = Runnable {
        search()
    }
    private var viewModel: RequestViewModel? = null
    private var userId: String? = ""
    private var serviceAdapter: searchUniversalAdapter<MiniApp>? = null
    private var popularList: ArrayList<MiniApp> = ArrayList()
    private var job: Job? = null

    companion object {
        fun newInstance(type: String) =
            SearchDialogFragment().apply {
                arguments = Bundle().apply { putString("TYPE", type) }
            }
        fun showSearch(fragmentManager: FragmentManager, list: List<Popular>) {
            SearchDialogFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(DATA, Gson().toJson(list))
                    }
                }
                .show(fragmentManager, "SearchDialogFragment")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        dialog?.window?.statusBarColor =  ContextCompat.getColor(requireContext(), R.color.white)
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding!!.searchEt.setText("")
        init()
        superlinksSubCategories()
        return binding?.root
    }

    private fun init() {
        dismissLoader()
        binding?.backIv?.setOnClickListener { dismiss() }
        serviceAdapter = searchUniversalAdapter(this)
        binding?.recycler?.apply {
            layoutManager = GridLayoutManager(context, 4)
//            layoutManager = GridLayoutManager(context, 3 ,LinearLayoutManager.HORIZONTAL,false)
            adapter = serviceAdapter
        }

        Gson().fromJson<List<Popular>>(arguments?.getString(DATA),
            object : TypeToken<List<Popular>>() {}.type)?.let {
            it.forEach { pop ->
                pop.mini_app?.let { it1 -> popularList.add(it1) }
            }
        }

        popularList.let {
            serviceAdapter?.addAllItem(if (it.size >12) it.subList(0,12) else it)
        }
        userId = AppSharedPreference.getInstance(requireContext()).getUserId()
        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)
        binding?.searchEt?.doAfterTextChanged {
            handler.removeCallbacks(receiver)
            handler.postDelayed(receiver, 700)
        }
    }

    private fun superlinksSubCategories() {
        val customShopCategory = SuperLinksSubCategories(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "All Mini Apps" -> {
                        ContainerActivity.openContainer(requireContext(), "All Mini Apps", "")
                    }

                    "Gift Voucher" -> {
                        getWebUrlData("Gyftr")
                    }
                    "BillsAndRecharge" -> {
                        RechargeServiceActivity.openRechargeService(requireContext())
                    }

                    "Compare Flights" -> {
                        getWebUrlData("Skyscanner")
                    }
                    "Book Cabs" -> {
                        ContainerActivity.openContainer(requireContext(), "Cabs", "")
                    }

                    "Sports" -> {
                        getWebUrlData("FanCode")
                    }
                    "Book tickets" -> {
                        getWebUrlData("bookmyshow")
                    }
                    "News" -> {
                        ContainerActivity.openContainer(requireContext(), "News", "")
                    }
                    "Deals & Coupons" -> {
                        ContainerActivity.openContainer(requireContext(), "Deals & Coupons", "")
                    }
                    "mx player" -> {
                        getWebUrlData("Mxplayer")
                    }

                }
            }

        }).apply {

            //     addItem(CustomeShopCategoryModel(R.drawable.daily_news_icon, "Daily News", "News"))

            addItem(CustomeShopCategoryModel(R.drawable.new_gift_card_icon, "Gift Vouchers", "Gift Voucher"))
//            addItem(CustomeShopCategoryModel(R.drawable.deals_and_coupons, "Offers & Deals", "Deals & Coupons"))
            addItem(CustomeShopCategoryModel(R.drawable.live_score, "Live Scores", "Sports"))
            addItem(CustomeShopCategoryModel(R.drawable.new_mini_apps, "Mini Apps ", "All Mini Apps"))
            addItem(CustomeShopCategoryModel(R.drawable.new_book_cabs_icon, "Book Cabs", "Book Cabs"))
            addItem(CustomeShopCategoryModel(R.drawable.entertainment_icon, "Entertainment", "mx player"))
            addItem(CustomeShopCategoryModel(R.drawable.cheapest_flights, "Cheapest Flights", "Compare Flights"))


        }
        binding!!.SuperLinksSUBRV.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//            layoutManager = GridLayoutManager(context, 3)
//            layoutManager = StaggeredGridLayoutManager(context,LinearLayoutManager.HORIZONTAL)
            layoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
            adapter = customShopCategory
        }
    }
    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(requireContext())
    }

    private fun getWebUrlData(s: String) {
        job = viewModel!!.searchMiniApp(getSharedPreference().getUserId(), s, this, object : ApiListener<WebTCashRes, Any?> {
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                val url = t?.data?.get(0)?.url
                val id = t?.data?.get(0)?.id
                WebViewActivityForOffer.openWebView(context, url, id, "", "", "", "", "")
            }

        })
    }

    private fun search() {
        kotlin.runCatching {
            serviceAdapter?.clear()
            if (binding?.searchEt?.text.isNullOrEmpty()) {
                binding?.trendingIv?.visibility = View.VISIBLE
                binding?.merchantsTv?.text = getString(R.string.trending_merchants)
                popularList.let {

                    serviceAdapter?.addAllItem(it)
                }
            } else {
                job?.cancel()
                job = viewModel?.searchMiniApp(userId, binding?.searchEt?.text.toString(), this, this)
            }
        }
    }





    override fun onSuccess(t: WebTCashRes?, mess: String?) {
        t?.let {
            serviceAdapter?.clear()
            binding?.trendingIv?.visibility = View.INVISIBLE
            binding?.merchantsTv?.text = getString(R.string.related_search)
            serviceAdapter?.addAllItem(it.data)
        }
    }

    override fun showLoader() {
        binding?.progress?.visibility = View.VISIBLE
    }

    override fun dismissLoader() {
        binding?.progress?.visibility = View.GONE
    }

    override fun showMess(mess: String?) {
        context?.toast(mess)
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW)
            if (data is MiniApp)
                WebViewActivity.openWebView(context,
                    data.url,
                    data.id,
                    data.image,
                    data.tcash,
                    data.is_favourite,data.cashback,data.app_category_id) }
}