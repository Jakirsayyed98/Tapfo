package app.tapho.ui.tcash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentHistoryTabBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.adapter.CustomeTcashCategoryModel
import app.tapho.ui.tcash.adapter.customeTransactionCategoriesAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.DATA
import com.google.gson.Gson


class HistoryTabFragment : BaseFragment<FragmentHistoryTabBinding>() {


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
        _binding = FragmentHistoryTabBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        statusBarColor(R.color.white)
        activity?.intent?.getStringExtra(DATA).let {
            val tcash = Gson().fromJson(it,TCashDasboardRes::class.java)
            getTcashdashboard(tcash)
        }

        _binding!!.back.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }


        return _binding?.root
    }

    private fun getTcashdashboard(tcash: TCashDasboardRes?) {

        tcash!!.let {
            _binding!!.progress.visibility = View.GONE
            setCategoryeslayout(it)
        }

    }

    private fun setCategoryeslayout(tcash: TCashDasboardRes) {
        val categoryList = customeTransactionCategoriesAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when(data){
                    "Recharge & bill payments" -> {
                        ContainerActivity.openContainerForTransaction(requireContext(),"AllTransactionFragment","Recharge",tcash,false,"Recharge & bill")
                    }
                    "Wallet balance" -> {
                        ContainerActivity.openContainerForTransaction(requireContext(),"AllTransactionFragment","walletTransaction",tcash,false,"Wallet balance")
                    }
                    "Merchants ( Mini Save )" -> {
                        ContainerActivity.openContainerForTransaction(requireContext(),"AllTransactionFragment","Minisave",tcash,false,"Merchants ( Mini Save )")
                    }
                    "Cashback & Rewards" -> {
                        ContainerActivity.openContainerForTransaction(requireContext(),"AllTransactionFragment","Cashback",tcash,false,"Cashback & Rewards")
                    }

                    "ShopGoHistoryFragment" -> {
                        ContainerActivity.openContainerForTransaction(requireContext(),"ShopGoHistoryFragment","ShopGo",tcash,false,"Cashback & Rewards")
                    }
                    else->{
                        ContainerActivity.openContainerForTransaction(requireContext(),"BuyGiftCard","Cashback",tcash,false,"Cashback & Rewards")
                    }
                }
            }

        }).apply {
                addItem(CustomeTcashCategoryModel(R.drawable.recharge_and_bills_icon,"Recharge & bill payments","includes recharge & bill payment history, Cashback, rewards, refund or more","Recharge & bill payments"))
                addItem(CustomeTcashCategoryModel(R.drawable.cashback_rewards_icon,"Cashback & Rewards","Includes Wallet top up, referral bonus, Recharge, Bill Payments, Merchants or more","Cashback & Rewards"))
                addItem(CustomeTcashCategoryModel(R.drawable.merchant_icons,"Merchants ( Mini Save )","includes Cashback from merchants store like flipkart, myntra, Ajio or more","Merchants ( Mini Save )"))
                addItem(CustomeTcashCategoryModel(R.drawable.wallet_balance_icon,"Wallet balance","Includes Wallet top up, refunds, payments history, cashback or more","Wallet balance"))
                addItem(CustomeTcashCategoryModel(R.drawable.wallet_balance_icon,"ShopGo","Get quick check-out with ShopGo","ShopGoHistoryFragment"))
        }
        _binding!!.categories.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = categoryList
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HistoryTabFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}