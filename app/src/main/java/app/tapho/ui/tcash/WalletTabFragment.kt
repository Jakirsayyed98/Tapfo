package app.tapho.ui.tcash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.FragmentWalletTabBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import app.tapho.ui.localbizzUI.LocalBizSplashActivity
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.HomeRes
import app.tapho.ui.tcash.adapter.SuperLinkAdapterFotTcash
import app.tapho.ui.tcash.adapter.TcashBannerAdapter
import app.tapho.ui.tcash.adapter.customeCardDetailsAdapter
import app.tapho.ui.tcash.adapter.quickrecharegAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.custome_quickrechargemodel
import app.tapho.utils.withSuffixAmount
import kotlinx.android.synthetic.main.dishes_layout.*
import java.util.ArrayList


class WalletTabFragment : BaseFragment<FragmentWalletTabBinding>() {


    var customShopCategory : SuperLinkAdapterFotTcash?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletTabBinding.inflate(inflater,container,false)

        if (parentFragment is HomeCardToTcashPageFragment)
            (parentFragment as HomeCardToTcashPageFragment).progressVISIBLE()
        getTcashdashboard()
        getHomeScreen()
        QuickRecharges()
        details()
        setClicks()
        superlinks()
        return _binding?.root
    }



    private fun setClicks() {
        _binding!!.pendingLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","0","Pending Rewards")
        }
        _binding!!.verifiedLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","1","Total Rewards")
        }

        _binding!!.AddBalance.setOnClickListener {
            getSharedPreference().saveString("wallet_cashback", "0")
            ContainerActivity.openContainerForVoucher(context, "addtopup", "", "", "")
        }
    }

    private fun getHomeScreen() {

        viewModel.getHomeData("home",getUserId(),this,object : ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t!!.let {

                    setBanners(it.banner_list10)

                    if (parentFragment is HomeCardToTcashPageFragment)
                        (parentFragment as HomeCardToTcashPageFragment).showHome()


                }
            }

        })

    }

    private fun setBanners(bannerList1: ArrayList<BannerList>?) {
        val bannerAdapter = TcashBannerAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        _binding!!.allbanners.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = bannerAdapter
        }

        bannerAdapter.addAllItem(bannerList1!!)
    }

    private fun getTcashdashboard() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getCurrentDate(), TimePeriodDialog.getCurrentDate(),"2",this,object : ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                t!!.let {
                    _binding!!.pending.text = withSuffixAmount(it.pending.toString())
                    _binding!!.cashAvailable.text = withSuffixAmount(it.cash_available.toString())
                    _binding!!.lifetimeEarning.text = withSuffixAmount(it.lifetime_earning.toString())
                    if (it.cash_available.toString().toDouble()<=100){
                        _binding!!.lowBalance.visibility = View.VISIBLE
                    }else{
                        _binding!!.lowBalance.visibility = View.GONE
                    }

                }
            }

        })
    }

    private fun QuickRecharges() {
        val detailsAdapter = quickrecharegAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Custom" -> {
                        getSharedPreference().saveString("wallet_cashback", "0")
                        ContainerActivity.openContainerForVoucher(context, "addtopup", "", "", "")
                    }
                    else -> {
                        getSharedPreference().saveString("wallet_cashback", "0")
                        ContainerActivity.openContainerForVoucher(context, "addtopup", data.toString(), "", "")
                    }

                }
            }

        }).apply {

            addItem(custome_quickrechargemodel("1", "999", "999"))
            addItem(custome_quickrechargemodel("2", "1499", "1499"))
            addItem(custome_quickrechargemodel("3", "1999", "1999"))
            addItem(custome_quickrechargemodel("4", "Custom", "Custom"))


        }
        _binding!!.quickRecharges.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = detailsAdapter
        }
    }

    private fun details() {
        val detailsAdapter = customeCardDetailsAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Add balance" -> {
                        getSharedPreference().saveString("wallet_cashback", "0")
                        ContainerActivity.openContainerForVoucher(context, "addtopup", "200", "", "")
                    }
                    "Buy Voucher" -> {
                        startActivity(Intent(requireContext(), VouchersActivity::class.java))
                    }
                    "Wallet Cashback" -> {
                        ContainerActivity.openContainer(requireContext(), "AddMoneyCardOffers", "")
                    }
                    "Rechargedata" -> {
                        getSharedPreference().saveString("servicetype", "1")
                        ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
                    }
                }
            }

        }).apply {
            addItem(CustomeShopCategoryModel(R.drawable.wallet_topup_icon, "Top Up\nwallet", "Add balance"))
            addItem(CustomeShopCategoryModel(R.drawable.mobile_recharge_icon, "Mobile\nRecharge", "Rechargedata"))

        }
        _binding!!.allfeature.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = detailsAdapter
        }
    }

    private fun superlinks() {
        customShopCategory = SuperLinkAdapterFotTcash(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                screenredirection(data.toString())
            }
        }).apply {
            addItem(CustomeSuperCategoryModel(R.drawable.refer_and_earn_icon, "Refer & Earn", "ReferAndEarn",""))
            addItem(CustomeSuperCategoryModel(R.drawable.mini_save_icon, "Mini Save", "OnlineStores","Cashback"))
        }
        _binding!!.SuperLinksRV.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = customShopCategory
        }
    }

    private fun screenredirection(data: String) {
        when (data) {

            "TapfoFood" -> {
                TapfoFoodContainerActivity.openContainer(requireContext(),"TapfoFoodLocationFragment","")
            }
            "MyMini" -> {
                ContainerActivity.openContainer(requireContext(),"NewHomeFragment","")
            }

            "Pending Rewards"->{
                ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","0","Pending Rewards")
            }
            "Total Rewards"->{
                ContainerActivity.openContainerforPointScreen(requireContext(),"TcashrewardsFragment","1","Total Rewards")
            }

            "addtopup"->{
                getSharedPreference().saveString("wallet_cashback", "0")
                ContainerActivity.openContainerForVoucher(context, "addtopup", "", "", "")
            }

            "OnlineStores" -> {
                ContainerActivity.openContainer(requireContext(), "OnlineStores", "")
            }
            "Offers" -> {
                ContainerActivity.openContainer(requireContext(), "OffersFragment", "")
            }

            "ReferAndEarn" -> {
                ContainerActivity.openContainer(requireContext(), "referandearnscreen", "")
            }

            "Scan & Pay" -> {
                ContainerActivity.openContainer(requireContext(), "ScanAndPayIntroFragment", "")
            }

            "Gift Voucher" -> {
                startActivity(Intent(requireContext(), VouchersActivity::class.java))
            }
            "BillsAndRecharge" -> {
//                getSharedPreference().saveString("servicetype","1")
//                ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
                RechargeServiceActivity.openRechargeService(requireContext())
            }
            "MobileRecharge" -> {
                getSharedPreference().saveString("servicetype","1")
                ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
//                        RechargeServiceActivity.openRechargeService(requireContext())
            }

            "900+ Games" -> {
                if (getSharedPreference().getString("gameIntro").isNullOrEmpty()) {
                    ContainerActivity.openContainer(requireContext(), "GamesOneTimeSplash", "")
                } else {
                    ContainerActivity.openContainer(requireContext(), "Games", "")
                }
            }


//            "AllProductWithCategory" -> {
//                ContainerActivity.openContainer(requireContext(), "AllProductWithCategory", "")
//            }

            "localbiz" -> {
//                        if (getSharedPreference().getString("localbizOnBoarding").isNullOrEmpty()) {
//                            ContainerActivity.openContainer(requireContext(), "localbizOnBoarding", "")
//                        } else {
                startActivity(Intent(requireContext(), LocalBizSplashActivity::class.java))
//                        }

            }
            "electro" -> {
                if (getSharedPreference().getString("electroIntro").isNullOrEmpty()) {
                    ContainerActivity.openContainer(requireContext(), "ElectroSplashFragment", "")
                } else {
                    ContainerActivity.openContainer(requireContext(), "ElectroFragment", "")
                }
            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WalletTabFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}