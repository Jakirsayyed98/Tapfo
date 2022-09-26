package app.tapho.NavSheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentForYouBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.CashbackPartnerAdapter
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.*
import app.tapho.ui.scanner.scanner
import app.tapho.ui.tcash.TCashDashboardFragment
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW


class Fragment_forYou : BaseFragment<FragmentForYouBinding>(), ApiListener<HomeRes, Any?>,
    RecyclerClickListener {
    private var appCategoryList: ArrayList<AppCategory>? = null
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var partnerCashbackAdapter: CashbackPartnerAdapter<NewCashback>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingForYou = FragmentForYouBinding.inflate(inflater, container, false)
        getData()
        setRecyclerBrand()
        setRecyclerCashbackPartner()

        return bindingForYou?.root
    }

    private fun getData() {
        bindingForYou!!.searchEt.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        viewModel.getHomeData(
            "home",
            AppSharedPreference.getInstance(requireContext()).getUserId(),
            this,
            this
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            Fragment_forYou()

    }
    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()

        if (type == "1")
            SearchDialogFragment.showSearch(childFragmentManager, list)

    }

    private fun setRecyclerBrand() {
        itemTypeAdapter = ItemTypeAdapter(this)
        bindingForYou!!.recyclerAppCategory.apply {
            layoutManager = GridLayoutManager(
                context,
                4
            )
            adapter = itemTypeAdapter
        }
    }

    private fun setRecyclerCashbackPartner() {
        partnerCashbackAdapter = CashbackPartnerAdapter(this)
        bindingForYou!!.recyclerCashbackpartner.apply {
                // layoutManager = GridLayoutManager(context, 4)
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = partnerCashbackAdapter
        }
    }
    private fun addFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {
        t?.let {

            it.new_cashback?.let { newCashBack ->
                partnerCashbackAdapter?.addAllItem(newCashBack)
            }
            appCategoryList = it.app_category
            setAppCategory(getString(R.string.more))

        }
    }
//    Glide.with(context).load(R.drawable.ic_more_right)
//    .into(binding.ivPartner)

    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()
        appCategoryList?.let { appCategory ->

            itemTypeAdapter?.addItem(AppCategory("", "", "", "", "", "", "", "", "scan", "", false, null))
            itemTypeAdapter?.addItem(AppCategory("", "", "", "", "", "", "", "", "wallet", "", false, null))

            if (appCategory.size > 8 && moreText == getString(R.string.more))
                itemTypeAdapter?.addAllItem(
                    appCategory.subList(0, 9).toList()
                )
            else

            itemTypeAdapter?.addAllItem(appCategory)



            itemTypeAdapter?.addItem(AppCategory("", "", "", "", "",
                    "", "", "", moreText, "",
                    false, null
                )
            )

        }
    }


    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                "wallet"->{

                    addFragment(TCashDashboardFragment.newInstance(""))

                }
                "scan"->{
                    startActivity(Intent(context,scanner::class.java))
                    Toast.makeText(context,"scan",Toast.LENGTH_SHORT).show()
                }
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
            data.cashback,
            data.app_category_id
        )
    }
}