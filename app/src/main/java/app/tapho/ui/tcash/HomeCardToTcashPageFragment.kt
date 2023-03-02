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
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentHomeCardToTcashPageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.BuyGiftCardFragment
import app.tapho.ui.home.HomeTabFragment
import app.tapho.ui.home.adapter.*
import app.tapho.ui.model.*
import app.tapho.ui.tcash.adapter.TcashbackOnlyWalletTransaction_Adapter
import app.tapho.ui.tcash.adapter.WalletVoucherAdapter
import app.tapho.ui.tcash.adapter.customeCardDetailsAdapter
import app.tapho.ui.tcash.adapter.quickrecharegAdapter
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.tcash.model.AddMoneyVoucers.Data
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.custome_quickrechargemodel
import app.tapho.utils.*
import com.google.android.material.tabs.TabLayoutMediator


class HomeCardToTcashPageFragment : BaseFragment<FragmentHomeCardToTcashPageBinding>(){


    var walletAdapter: TcashbackOnlyWalletTransaction_Adapter? = null
    var walletVoucherAdapter: WalletVoucherAdapter<Data>? = null

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
            ContainerActivity.openContainer(requireContext(),"EndToEndEncriptionFragment","",false,"")
        }

        _binding!!.back.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        return _binding?.root
    }



    private fun init() {
        val mAdapter = PagerFragmentAdapter(this)
        mAdapter.addFragment(WalletTabFragment.newInstance(), "Wallet")
        mAdapter.addFragment(HistoryTabFragment.newInstance(), "History")
        binding.viewPager.adapter = mAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.customView = getCustomTab2(context, mAdapter.getTitle(pos))
        }.attach()
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

    fun noInternetConnection() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.noconnection.visibility = View.VISIBLE
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