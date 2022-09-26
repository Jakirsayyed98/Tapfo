package app.tapho.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.databinding.FragmentWalletOfferDialogBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.LoaderFragment
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.wallet.adapter.CashbackCouponsAdapter
import app.tapho.ui.wallet.model.CashbackVouchersRes
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * A simple [Fragment] subclass.
 * Use the [WalletOfferDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletOfferDialogFragment : BottomSheetDialogFragment(), LoaderListener,
    ApiListener<AddWalletVoucherRes, Any?> {
    private var binding: FragmentWalletOfferDialogBinding? = null
    private var mAdapter: CashbackCouponsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWalletOfferDialogBinding.inflate(inflater, container, false)
        init()
        return binding?.root
    }

    private fun init() {
        mAdapter = CashbackCouponsAdapter()
        binding?.recyclerCashbackOffers?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        getVouchers()
    }

    private fun getVouchers() {
        ViewModelProvider(this).get(RequestViewModel::class.java).getWalletOfferVouchers(
            AppSharedPreference.getInstance(requireContext()).getUserId(),
            this,
            this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WalletOfferDialogFragment()
    }

    override fun showLoader() {
        kotlin.runCatching {
            LoaderFragment.showLoader(childFragmentManager)
        }
    }

    override fun dismissLoader() {
        kotlin.runCatching {
            LoaderFragment.dismissLoader(childFragmentManager)
        }
    }

    override fun showMess(mess: String?) {
        context?.toast(mess)
    }

    override fun onSuccess(t: AddWalletVoucherRes?, mess: String?) {
        t?.let {
           // it.data?.let { it1 -> mAdapter?.addAllItem(it1) }
        }
    }
}