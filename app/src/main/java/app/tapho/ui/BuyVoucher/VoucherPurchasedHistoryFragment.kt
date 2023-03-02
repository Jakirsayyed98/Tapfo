package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentVoucherPurchasedHistoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.CategoriesModel.Custome_voucher_history
import app.tapho.ui.BuyVoucher.VoucherPayments.VoucherPaymentBaseActivity
import app.tapho.ui.BuyVoucher.adapter.voucher_purchased_history_list_adapter
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.ui.tcash.model.VPullVouchersResult


class VoucherPurchasedHistoryFragment : BaseFragment<FragmentVoucherPurchasedHistoryBinding>() {

    var vouchersHistory : voucher_purchased_history_list_adapter<Custome_voucher_history>? =null

    var txndata : ArrayList<Txn> = ArrayList()
    var history: ArrayList<Custome_voucher_history> = ArrayList()
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
        _binding = FragmentVoucherPurchasedHistoryBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        setLayoutAdapter()

        getViewmodelDataFromTcash()

        return _binding?.root
    }

    private fun getViewmodelDataFromTcash() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),"2",
            this, object : ApiListener<TCashDasboardRes, Any?> {
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t.let {
                        it!!.txn.forEach {
                            if (it.type.equals("5")) {
                                txndata.add(it)
                            }
                        }
                        if (txndata.isNullOrEmpty().not()){
                            getVouchersTxnData(txndata)
                        }
                    }
                }
            })
    }

    private fun getVouchersTxnData(txndata: ArrayList<Txn>) {
        txndata.forEach {
//            getVouchersDetails(it.voucher_detail.response_json.vPullVouchersResult,it)
        }
    }


    private fun getVouchersDetails(vPullVouchersResult: VPullVouchersResult, txn: Txn) {
        var MiniAppIcon = ""
        var price = 0.0
        var errorCode = "0"
//        if (txn.voucher_detail.reponse_status.toString().equals("SUCCESS")){
//            errorCode = "0"
//        }else{
//            errorCode = "1"
//        }
        val name = vPullVouchersResult.PullVouchers.get(0).ProductName
        viewModel.searchMiniApp(getUserId(),name,this,object :ApiListener<WebTCashRes,Any?>{
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                t.let {
                    MiniAppIcon = it!!.data.get(0).image.toString()
                    if (vPullVouchersResult.PullVouchers.isNullOrEmpty().not()){
                        vPullVouchersResult.PullVouchers.forEach {
                            it.Vouchers.forEach {
                                price += it.Value.toDouble()
                            }
                        }
                        vouchersHistory!!.addItem(Custome_voucher_history(MiniAppIcon,vPullVouchersResult.PullVouchers.get(0).ProductName,price.toString(),txn.created_at,vPullVouchersResult.ResultType,errorCode,txn.id,txn.pay_option))
                    }
                }
            }

        })



    }



    private fun setLayoutAdapter() {
        vouchersHistory = voucher_purchased_history_list_adapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Custome_voucher_history){
                    VoucherPaymentBaseActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatusForVoucher",data.errorCode,data.user_transaction,"","Recharge",data.pspname)
                }
            }
        })
        _binding!!.VoucherHistory.apply {
            layoutManager =LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = vouchersHistory
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            VoucherPurchasedHistoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}