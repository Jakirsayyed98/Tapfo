package app.tapho.ui.tcash

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAddMoneyCardOffersBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.adapter.WalletVoucherAdapter
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.tcash.model.AddMoneyVoucers.Data

class AddMoneyCardOffersFragment : BaseFragment<FragmentAddMoneyCardOffersBinding>(),
    ApiListener<AddWalletVoucherRes, Any?> {

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
        _binding = FragmentAddMoneyCardOffersBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        getDataFromList()
        setRecylerData()
        _binding!!.backBtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return _binding?.root
    }

    private fun setRecylerData() {
        walletVoucherAdapter = WalletVoucherAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data){
                    getSharedPreference().saveString("wallet_cashback",data.cashback)
                    ContainerActivity.openContainerForVoucher(context, "addtopup", data.amount,"CouponApply",data.cashback)
                }
            }
        })
        _binding!!.voucherlist.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = walletVoucherAdapter
        }
    }

    private fun getDataFromList() {
        viewModel.getWalletOfferVouchers(getUserId(), this, this )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddMoneyCardOffersFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: AddWalletVoucherRes?, mess: String?) {
        t.let {
            t.let {
                walletVoucherAdapter!!.addAllItem(it!!.data)
            }

        }
    }
}