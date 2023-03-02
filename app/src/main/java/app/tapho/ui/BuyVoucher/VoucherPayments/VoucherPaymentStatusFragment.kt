package app.tapho.ui.BuyVoucher.VoucherPayments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentVoucherPaymentStatusBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.AllVoucher_data
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.BuyVoucher.adapter.purchased_vouchers_list_adapter
import app.tapho.ui.ContainerActivity
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.*
import app.tapho.utils.parseDateWithFullFormate
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide


class VoucherPaymentStatusFragment :BaseFragment<FragmentVoucherPaymentStatusBinding>() {
    var paymentStatus = ""

    var AllvouchersAdapter : purchased_vouchers_list_adapter<AllVoucher_data>?= null
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
        _binding = FragmentVoucherPaymentStatusBinding.inflate(inflater,container,false)

        val status = activity?.intent?.getStringExtra("status")
        val errorcode = activity?.intent?.getStringExtra("errorcode")
        val txn_Id = activity?.intent?.getStringExtra("txn_Id")
        val statusType = activity?.intent?.getStringExtra("statusType")
        val pspnamedata = activity?.intent?.getStringExtra("pspnamedata")

        setPurchasedVoucherLayout()

        _binding!!.viewAllVouchers.setOnClickListener {
            ContainerActivity.openContainer(requireContext(),"AllPurchasedVouchers","")
            activity?.finish()
        }


        _binding!!.PaymentMode.text = pspnamedata
        setData(status, errorcode, txn_Id, statusType)

        getTcashdata(txn_Id, statusType, errorcode)
        onBackPressedMethod()
        VisiblityLayout()


        return _binding?.root
    }

    private fun setPurchasedVoucherLayout() {
        AllvouchersAdapter = purchased_vouchers_list_adapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        _binding!!.VoucherList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = AllvouchersAdapter
        }
    }

    private fun getVouchersDetails(vPullVouchersResult: VPullVouchersResult) {
        if (vPullVouchersResult.PullVouchers.isNullOrEmpty().not()){
            vPullVouchersResult.PullVouchers.forEach {
                setVouchersData(it)
            }
        }
    }

    private fun setVouchersData(pullvoucher: PullVoucher) {
        val name = pullvoucher.ProductName
        var icon = ""
        viewModel.searchMiniApp(getUserId(),name,this,object : ApiListener<WebTCashRes,Any?>{
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                icon = t!!.data.get(0).image.toString()
                Glide.with(requireContext()).load(icon).placeholder(R.drawable.loding_app_icon).circleCrop().into(_binding!!.icon)
                pullvoucher.Vouchers.forEach {
                    AllvouchersAdapter!!.addItem(AllVoucher_data(icon,pullvoucher.ProductName,it.VoucherNo,it.EndDate,it.Voucherpin,it.Value,it.VoucherGCcode))
                }
            }

        })

    }


    private fun VisiblityLayout() {
        _binding!!.walletDetails.setOnClickListener {
            if (_binding!!.walletDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).placeholder(R.drawable.loding_app_icon).into(_binding!!.imagedown1)
                _binding!!.walletDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).placeholder(R.drawable.loding_app_icon).into(_binding!!.imagedown1)
                _binding!!.walletDetailsform.visibility = View.VISIBLE
            }
        }

        _binding!!.sucessbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
            activity?.finish()
        }
        _binding!!.retrybtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
            activity?.finish()
        }
        _binding!!.donbtnAfterFailed.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
            activity?.finish()
        }


        _binding!!.PaymentMethodDetails.setOnClickListener {
            if (_binding!!.PaymentDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).placeholder(R.drawable.loding_app_icon).into(_binding!!.imagedown3)
                _binding!!.PaymentDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).placeholder(R.drawable.loding_app_icon).into(_binding!!.imagedown3)
                _binding!!.PaymentDetailsform.visibility = View.VISIBLE
            }
        }
    }
    private fun onBackPressedMethod() {
        _binding!!.back.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.back1.setOnClickListener {
            SendToHomeScreen()
        }
    }

    private fun SendToHomeScreen() {
        activity?. onBackPressedDispatcher?.onBackPressed()
    }

    private fun getTcashdata(txnId: String?, statusType: String?, errorcode: String?) {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),"2", this, object :
            ApiListener<TCashDasboardRes, Any?> {
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                t.let {
                    it!!.txn.forEach {
                        if (it.id.equals(txnId)) {
                            setAllData(it, statusType, errorcode)
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setAllData(it: Txn, statusType: String?, errorcode: String?) {
        _binding!!.WalletOrderId.text = it.txn_id
        _binding!!.txnid.text = "TXN ID " + it.txn_id
        _binding!!.paymentDebitAmount.text = withSuffixAmount(it.amount)
        _binding!!.paybleAmount.text = withSuffixAmount(it.payment_amount)
//        _binding!!.names.text = it.voucher_detail.response_json.vPullVouchersResult.PullVouchers.get(0).ProductName
//
//            getVouchersDetails(it.voucher_detail.response_json.vPullVouchersResult)


        if (errorcode.toString().equals("0")) {
            _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(it.amount), " Sucessfull")
        } else {
            _binding!!.paymentForTitle.text = getString(
                R.string.payment_of_299_sucessful,
                withSuffixAmount(it.amount),
                " Failed"
            )
        }
        _binding!!.idCreatedAt.text = parseDateWithFullFormate(it.created_at)
        _binding!!.PaymentOrderId.text = it.txn_id
        _binding!!.amount.text = withSuffixAmount((it.payment_amount.toDouble()+it.amount.toDouble()).toString())

    }

    private fun setData(status: String?, errorcode: String?, txnId: String?, statusType: String?) {

        if (errorcode.toString().equals("0")) {
            //            _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
            _binding!!.sucessScreen.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
            statusBarColor(R.color.GreenWalletBackgroundDark)
            _binding!!.SuccessScreen.visibility = View.VISIBLE
            _binding!!.failedScreen.visibility = View.GONE
            _binding!!.failedbtnlayout.visibility = View.GONE
            statusBarTextBlack()
        } else {
            //          _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.red))
            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
            _binding!!.SuccessScreen.visibility = View.GONE
            _binding!!.failedScreen.visibility = View.VISIBLE
            _binding!!.failedbtnlayout.visibility = View.VISIBLE
            statusBarColor(R.color.red)
            statusBarTextBlack()
        }

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            VoucherPaymentStatusFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}