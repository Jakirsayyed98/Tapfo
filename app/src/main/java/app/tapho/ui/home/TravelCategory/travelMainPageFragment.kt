package app.tapho.ui.home.TravelCategory

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTravelMainPageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import app.tapho.ui.home.WebViewActivityForOffer
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.home.adapter.RecommendedAdapter
import app.tapho.ui.home.adapter.TravelCategoryAdapter
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.OPEN_WEB_VIEW
import net.one97.paytm.nativesdk.common.Requester.APIReqtGenerator


class travelMainPageFragment : BaseFragment<FragmentTravelMainPageBinding>() {

    var miniappAdapter : RecommendedAdapter<MiniApp>? = null
    var noncashback : RecommendedAdapter<MiniApp>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
       
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       travelbinding = FragmentTravelMainPageBinding.inflate(inflater,container,false)
        statusBarColor(R.color.homebackground)
        setCategories()
        getCategoryID()
        setMiniAppData()
        setMiniAppDataStore()
        return travelbinding!!.root
    }



    private fun getCategoryID() {
        viewModel.getHomeData("home",getUserId(),this,object : ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t.let {
                    it!!.app_category!!.forEach {
                        if (it.name.toString().equals("Travel")){
                            getTravelCategory(it.id)
                        }
                    }
                }
            }

        })
    }

    private fun getTravelCategory(CategoriId: String?) {
        viewModel.getAppCategoryMiniApp(getUserId(),CategoriId,this,object : ApiListener<MiniAppRes,Any?>{
            override fun onSuccess(t: MiniAppRes?, mess: String?) {
                var CbList : ArrayList<MiniApp> = ArrayList()
                var NonCbList : ArrayList<MiniApp> = ArrayList()
                t.let {
                    it!!.mini_app.let {
                        it!!.forEach {
                            if (it.cashback.isNullOrEmpty()){
                                NonCbList.add(it)
                            }else{
                                CbList.add(it)
                            }
                        }
                        miniappAdapter!!.addAllItem(CbList)
                        noncashback!!.addAllItem(NonCbList)
                    }
                }
            }

        })

    }

    private fun setMiniAppData() {
        miniappAdapter = RecommendedAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp) {
                    openWebView(data)
                }            }

        })
        travelbinding!!.primiumStore.apply {
            layoutManager =GridLayoutManager(requireContext(),4)
            adapter = miniappAdapter
        }
    }
    private fun setMiniAppDataStore() {
        noncashback = RecommendedAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is MiniApp) {
                    openWebView(data)
                }
            }
        })
        travelbinding!!.Store.apply {
            layoutManager =GridLayoutManager(requireContext(),4)
            adapter = noncashback
        }
    }
    private fun setCategories() {
        val customShopCategory = TravelCategoryAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {

                    "Flight" -> {
                        getWebUrlData("makemytrip")
                    }
                    "bus" -> {
                        getWebUrlData("Redbus")
                    }
                    "Train" -> {
                        getWebUrlData("confirmtkt")
                    }
                    "Cabs" -> {
                        getWebUrlData("Ola")
                    }
                    "Hotel" -> {
                        getWebUrlData("OYO")
                    }

                }
            }

        }).apply {

            addItem(CustomeShopCategoryModel(R.drawable.flight_icon2, "Flight", "Flight"))
            addItem(CustomeShopCategoryModel(R.drawable.bus_icon2, "Bus", "bus"))
            addItem(CustomeShopCategoryModel(R.drawable.train_icon2, "Train", "Train"))
            addItem(CustomeShopCategoryModel(R.drawable.cab_icon2, "Cabs", "Cabs"))
            addItem(CustomeShopCategoryModel(R.drawable.bus_icon2, "Hotel", "Hotel"))

        }
        travelbinding!!.Categories.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = customShopCategory
        }
    }

    private fun getCashbackMerchantListData(CategoriId: String, Title: String) {

    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(requireContext(), type, data, isOldData, title)
    }

    private fun openWebView(data: MiniApp) {
        //    ActiveCashbackActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id)
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

    private fun getWebUrlData(s: String) {
        viewModel.searchMiniApp(getUserId(), s, this, object : ApiListener<WebTCashRes, Any?> {
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                val url = t?.data?.get(0)?.url
                val id = t?.data?.get(0)?.id
                WebViewActivityForOffer.openWebView(context, url, id, "", "", "", "", "")
            }

        })
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            travelMainPageFragment().apply {
                arguments = Bundle().apply {
                  
                }
            }
    }

}