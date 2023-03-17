package app.tapho.ui.tcash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.FragmentHomeCardToTcashPageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.home.adapter.*
import app.tapho.ui.localbizzUI.LocalBizSplashActivity

import app.tapho.ui.tcash.adapter.*
import app.tapho.ui.tcash.model.AddMoneyVoucers.Data
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.custome_quickrechargemodel
import app.tapho.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.util.ArrayList


class HomeCardToTcashPageFragment : BaseFragment<FragmentHomeCardToTcashPageBinding>() {

    var customShopCategory: SuperLinkAdapterFotTcash? = null
    var walletAdapter: TcashbackOnlyWalletTransaction_Adapter? = null
    var walletVoucherAdapter: WalletVoucherAdapter<Data>? = null
    var tcashdashboard : TCashDasboardRes? = null
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
        _binding = FragmentHomeCardToTcashPageBinding.inflate(inflater, container, false)
        statusBarColor(R.color.black)
        statusBarTextBlack()

        init()
        _binding!!.secure.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "EndToEndEncriptionFragment", "", false, "")
        }

        _binding!!.back.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        return _binding?.root
    }


    private fun init() {
        progressVISIBLE()


             getTcashdashboard()



        QuickRecharges()
        details()
        setClicks()
    }

    fun progressVISIBLE() {
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.VISIBLE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }

    fun lowConnection() {
        _binding!!.lowconnection.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }

    fun showHome() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.VISIBLE


    }


    private fun setClicks() {
        _binding!!.pendingLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(requireContext(), "TcashrewardsFragment", "0", "Pending Rewards",tcashdashboard)
        }


        _binding!!.verifiedLayout.setOnClickListener {
            ContainerActivity.openContainerforPointScreen(requireContext(), "TcashrewardsFragment", "1", "Total Rewards",tcashdashboard)
        }

        _binding!!.AddBalance.setOnClickListener {
            getSharedPreference().saveString("wallet_cashback", "0")
            ContainerActivity.openContainerForVoucher(context, "addtopup", "", "", "",tcashdashboard)
        }
    }

    private fun getTcashdashboard() {

        val data = activity?.intent?.getStringExtra(DATA)
        if (data.isNullOrEmpty().not()) {
            val tcash = Gson().fromJson(data,TCashDasboardRes::class.java)
            showHome()
            tcashdashboard = tcash
            _binding!!.pending.text = withSuffixAmount(tcash.pending.toString())
            _binding!!.cashAvailable.text = withSuffixAmount(tcash.cash_available.toString())
            _binding!!.cashAvailable1.text = withSuffixAmount(tcash.cash_available.toString())
            _binding!!.lifetimeEarning.text = withSuffixAmount(tcash.lifetime_earning.toString())
            _binding!!.alltransaction.setOnClickListener {click->
                ContainerActivity.openContainer(requireContext(), "HistoryTabFragment", tcash ,false, "")
            }
        }else {


                viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getCurrentDate(), TimePeriodDialog.getCurrentDate(), "2", this, object :
                    ApiListener<TCashDasboardRes, Any?> {
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                        t!!.let {
                            showHome()
                            _binding!!.pending.text = withSuffixAmount(it.pending.toString())
                            _binding!!.cashAvailable.text = withSuffixAmount(it.cash_available.toString())
                            _binding!!.cashAvailable1.text = withSuffixAmount(it.cash_available.toString())
                            _binding!!.lifetimeEarning.text =withSuffixAmount(it.lifetime_earning.toString())
                            _binding!!.alltransaction.setOnClickListener {click->
                                ContainerActivity.openContainer(requireContext(), "HistoryTabFragment", it ,false, "")
                            }
                        }
                    }
                })


        }

    }

    private fun QuickRecharges() {
        val detailsAdapter = quickrecharegAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Custom" -> {
                        getSharedPreference().saveString("wallet_cashback", "0")
                        ContainerActivity.openContainerForVoucher(context, "addtopup", "", "", "",tcashdashboard)
                    }
                    else -> {
                        getSharedPreference().saveString("wallet_cashback", "0")
                        ContainerActivity.openContainerForVoucher(context, "addtopup", data.toString(), "", "",tcashdashboard)
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
                        ContainerActivity.openContainerForVoucher(context, "addtopup", "200", "", "",tcashdashboard)
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

            addItem(CustomeShopCategoryModel(R.drawable.recharge_mobile, "Mobile\nRecharge", "Rechargedata"))
            addItem(CustomeShopCategoryModel(R.drawable.add_cash, "Add Cash", "Add balance"))

//            addItem(CustomeShopCategoryModel(R.drawable.mobile_recharge_icon, "Vouchers", "Buy Voucher"))

        }
        _binding!!.allfeature.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = detailsAdapter
        }
    }


    private fun screenredirection(data: String) {
        when (data) {

            "TapfoFood" -> {
                TapfoFoodContainerActivity.openContainer(
                    requireContext(),
                    "TapfoFoodLocationFragment",
                    ""
                )
            }
            "MyMini" -> {
                ContainerActivity.openContainer(requireContext(), "NewHomeFragment", "")
            }

            "Pending Rewards" -> {
                ContainerActivity.openContainerforPointScreen(requireContext(), "TcashrewardsFragment", "0", "Pending Rewards",tcashdashboard)
            }
            "Total Rewards" -> {
                ContainerActivity.openContainerforPointScreen(requireContext(), "TcashrewardsFragment", "1", "Total Rewards",tcashdashboard)
            }

            "addtopup" -> {
                getSharedPreference().saveString("wallet_cashback", "0")
                ContainerActivity.openContainerForVoucher(context, "addtopup", "", "", "",tcashdashboard)
            }

            "OnlineStores" -> {
                ContainerActivity.openContainer(requireContext(), "OnlineStores", "")
            }
            "Offers" -> {
                ContainerActivity.openContainer(requireContext(), "OffersFragment", "")
            }

            "ReferAndEarn" -> {
                ContainerActivity.openContainer(requireContext(), "referandearnscreen", tcashdashboard)
            }

            "Scan & Pay" -> {
                ContainerActivity.openContainer(requireContext(), "ScanAndPayIntroFragment", "")
            }

            "BillsAndRecharge" -> {
                RechargeServiceActivity.openRechargeService(requireContext())
            }
            "MobileRecharge" -> {
                getSharedPreference().saveString("servicetype", "1")
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
            HomeCardToTcashPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}