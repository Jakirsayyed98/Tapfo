package app.tapho.ui.PaytmPaymentGateway

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentPaymentStatusScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.PaytmPaymentGateway.Adapter.PopularPartnerAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.Popular
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.RechargeDetail
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.DATA
import app.tapho.utils.parseDateWithFullFormate
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class paymentStatusScreenFragment : BaseFragment<FragmentPaymentStatusScreenBinding>() {

    var paymentStatus = ""
    var result = ""
    var PopularPartnerAdapter: PopularPartnerAdapter<Popular>? = null
    var TXNdata : ArrayList<Txn> = ArrayList()
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
        _binding = FragmentPaymentStatusScreenBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextBlack()
        val status = activity?.intent?.getStringExtra("status")
        val errorcode = activity?.intent?.getStringExtra("errorcode")
        val txn_Id = activity?.intent?.getStringExtra("txn_Id")
        val statusType = activity?.intent?.getStringExtra("statusType")
        val pspnamedata = activity?.intent?.getStringExtra("pspnamedata")
        result = activity?.intent?.getStringExtra("result").toString()


        _binding!!.PaymentMode.text = pspnamedata
        setData(status, errorcode, txn_Id, statusType)

        activity?.intent?.getStringExtra(DATA).let {
            val txn = Gson().fromJson(it,Txn::class.java)
            setAllData(txn, statusType, errorcode)
        }

        onBackPressedMethod()
        VisiblityLayout()
        setRecyclerViewData()
        getPopularMiniApps()
        _binding!!.paymentdetail.visibility = View.VISIBLE

        return _binding?.root
    }

    private fun VisiblityLayout() {
        _binding!!.walletDetails.setOnClickListener {
            if (_binding!!.walletDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.imagedown1)
                _binding!!.walletDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.imagedown1)
                _binding!!.walletDetailsform.visibility = View.VISIBLE
            }
        }

        _binding!!.refunddetail.setOnClickListener {
            if (_binding!!.refunddetailform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.refunddown)
                _binding!!.refunddetailform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.refunddown)
                _binding!!.refunddetailform.visibility = View.VISIBLE
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
//            startActivity(Intent(requireContext(),HomeActivity::class.java))
            activity?.finish()
        }

        _binding!!.RechargeDetails.setOnClickListener {
            if (_binding!!.RechargeDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.imagedown2)
                _binding!!.RechargeDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.imagedown2)
                _binding!!.RechargeDetailsform.visibility = View.VISIBLE
            }
        }
        _binding!!.PaymentMethodDetails.setOnClickListener {
            if (_binding!!.PaymentDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.imagedown3)
                _binding!!.PaymentDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.imagedown3)
                _binding!!.PaymentDetailsform.visibility = View.VISIBLE
            }
        }

        _binding!!.cashbackdetaild.setOnClickListener {
            if (_binding!!.cashbackdetailform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.cashbackdown)
                _binding!!.cashbackdetailform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.cashbackdown)
                _binding!!.cashbackdetailform.visibility = View.VISIBLE
            }
        }
    }

    private fun setRecyclerViewData() {
        PopularPartnerAdapter = PopularPartnerAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })
        _binding!!.popularMiniApps.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = PopularPartnerAdapter
        }
    }

    private fun getPopularMiniApps() {
        val popularList: ArrayList<Popular> = ArrayList()
        viewModel.getHomeData("home", getUserId(), this, object : ApiListener<HomeRes, Any?> {
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t.let {
                    it!!.popular.let {
                        popularList.addAll(it!!)
                    }
                    popularList.shuffle()
                    PopularPartnerAdapter!!.addAllItem(if (popularList.size > 3) popularList.subList(0, 3) else popularList)
                }
            }
        })

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

    private fun getValidationPeriod(data: String): String {
        try {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(data)?.let {
                return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(
                    Calendar.getInstance()
                        .apply {
                            time = it
                            add(Calendar.DATE, 1)
                        }.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun setAllData(it: Txn, statusType: String?, errorcode: String?) {

        if (it.recharge_detail.get(0).user_refund_status.equals("1")){
            _binding!!.refundstatus.text = "Completed"
            _binding!!.refundstatus.setTextColor(R.color.green_dark)
            _binding!!.refundtime.text ="Credited to Wallet | "+ parseDateWithFullFormate(it.created_at)
            _binding!!.refundtime.visibility = View.VISIBLE
        }else{
            _binding!!.refundtime.visibility = View.GONE
        }


        _binding!!.WalletOrderId.text = it.txn_id
        _binding!!.txnid.text = "TXN ID " + it.txn_id
        _binding!!.PaymentOrderId.text = it.txn_id
        _binding!!.refundTXNID.text = it.txn_id


        _binding!!.paymentDebitAmount.text = "-" + withSuffixAmount(it.amount)
        _binding!!.paybleAmount.text = "-" + withSuffixAmount(it.payment_amount)


        _binding!!.idCreatedAt.text = parseDateWithFullFormate(it.created_at)
        _binding!!.RechargeOrderId.text = it.recharge_detail.get(0).usertx

        if (it.payment_amount.isNullOrEmpty().not()){
            if (it.payment_amount.equals("0")){
                _binding!!.paymentMethod.visibility = View.GONE
                _binding!!.paymentMethod1.visibility = View.GONE
            }else{
                _binding!!.paymentMethod.visibility = View.VISIBLE
                _binding!!.paymentMethod1.visibility = View.VISIBLE
            }
        }else{
            _binding!!.paymentMethod.visibility = View.GONE
            _binding!!.paymentMethod1.visibility = View.GONE
        }

        if (it.amount.isNullOrEmpty().not()){
            if (it.amount.equals("0")){
                _binding!!.walletpaymentmethod.visibility = View.GONE
            }else{
                _binding!!.walletpaymentmethod.visibility = View.VISIBLE
            }
        }else{
            _binding!!.walletpaymentmethod.visibility = View.GONE
        }
        if (it.status.equals("0") ){
            _binding!!.cashbackstatusd.text =  "Pending (Verified on next recharge)"
        }  else {
            _binding!!.cashbackstatusd.text =  "Verified (Credited to wallet)"
            _binding!!.cashbackstatusd.setTextColor(R.color.green_dark)
        }
        _binding!!.refundDateAndTime.text =getValidationPeriod(it.created_at.toString())

        var transactionAmount = ""
        var cashback = ""
        it.recharge_detail.get(0).let {
            transactionAmount = it.amount
            cashback = it.user_commission_amount
            _binding!!.cashbackAmountAmount.text = withSuffixAmount(cashback)
            _binding!!.cashbackAmountAmount.setTextColor(R.color.green_dark)
            _binding!!.refundAmount.text = withSuffixAmount(transactionAmount)
            _binding!!.Rechargeamount.text = withSuffixAmount(it.amount)
            _binding!!.opretorName.text = it.operator_detail.name
            _binding!!.mobileNumber.text = it.number
            Glide.with(requireContext()).load( it.operator_detail.image).centerCrop().circleCrop().into(_binding!!.opretorIcon)
        }


        when (statusType) {
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
                    _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(transactionAmount), " is Successful")
                } else {
                    when(result){
                        "0"->{
                            _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(transactionAmount), " is Failed")
                        }
                        "1"->{
                            _binding!!.paymentForTitle.text = "Recharge of "+ withSuffixAmount(transactionAmount)+ " is Failed"
                        }
                        "2"->{
                            _binding!!.paymentForTitle.text = "Recharge of "+ withSuffixAmount(transactionAmount)+ " is Successful"
                        }
                    }

                }
            }
            "Paytm" -> {
                if (errorcode.toString().equals("01")) {
                    _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(transactionAmount), " is Successful")
                } else {
                    when(result){
                        "0"->{
                            _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(transactionAmount), " is Failed")
                        }
                        "1"->{
                            _binding!!.paymentForTitle.text = "Recharge of "+ withSuffixAmount(transactionAmount)+ " is Failed"
                        }
                        "2"->{
                            _binding!!.paymentForTitle.text = "Recharge of "+ withSuffixAmount(transactionAmount)+ " is Successful"
                        }
                    }
                }
            }

            else->{
                when(result){
                    "0"->{
                        _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(transactionAmount), " is Failed")
                    }
                    "1"->{
                        _binding!!.paymentForTitle.text = "Recharge of "+ withSuffixAmount(transactionAmount)+ " is Failed"
                    }
                    "2"->{
                        _binding!!.paymentForTitle.text = "Recharge of "+ withSuffixAmount(transactionAmount)+ " is Successful"
                    }
                }
            }
        }



        _binding!!.paymentOption1.text = it.pay_option


        val recharge = it.recharge_detail.get(0).api_response
        getData(recharge)

        if (it.cashback.isNullOrEmpty().not()){
            if (it.cashback.equals("0")) {
                _binding!!.cashbacklayout.visibility = View.GONE
            } else {
                _binding!!.cashbacklayout.visibility = View.VISIBLE
            }
        }

    }

    private fun getData(map: String) {
        val objectd = JSONObject(map)
        val OperatorRef = objectd.getString("OperatorRef")
        _binding!!.RechargeRef.text =OperatorRef
        if (OperatorRef.isNullOrEmpty().not()){
            _binding!!.RechargeReflay.visibility = View.VISIBLE
        }else{
            _binding!!.RechargeReflay.visibility = View.GONE
        }
    }


    private fun setData(status: String?, errorcode: String?, txnId: String?, statusType: String?) {

        when (statusType) {
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.refunddetail.visibility = View.GONE
                    _binding!!.refundview.visibility = View.GONE
                    _binding!!.refundStatuslayout.visibility = View.GONE
                    statusBarTextBlack()

                } else {
                    when(result){
                        "0"->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.GONE
                            _binding!!.failedScreen.visibility = View.VISIBLE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.GONE
                            _binding!!.refundview.visibility = View.GONE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                        "1"->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.VISIBLE
                            _binding!!.failedScreen.visibility = View.GONE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.VISIBLE
                            _binding!!.refundview.visibility = View.VISIBLE
                            _binding!!.refundStatuslayout.visibility = View.VISIBLE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                        else->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.GONE
                            _binding!!.failedScreen.visibility = View.VISIBLE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.GONE
                            _binding!!.refundview.visibility = View.GONE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                    }

                }
            }
            "Paytm" -> {
                if (errorcode.toString().equals("01")) {
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.refundStatuslayout.visibility = View.GONE
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()

                } else {
                    when(result){
                        "0"->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.GONE
                            _binding!!.failedScreen.visibility = View.VISIBLE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.GONE
                            _binding!!.refundview.visibility = View.GONE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                        "1"->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.VISIBLE
                            _binding!!.failedScreen.visibility = View.GONE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.VISIBLE
                            _binding!!.refundview.visibility = View.VISIBLE
                            _binding!!.refundStatuslayout.visibility = View.VISIBLE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                        else->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.GONE
                            _binding!!.failedScreen.visibility = View.VISIBLE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.GONE
                            _binding!!.refundview.visibility = View.GONE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                    }

                }
            }
            "Recharge" -> {
                if (status.toString().equals("Success")) {
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.refundStatuslayout.visibility = View.GONE
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    _binding!!.refunddetail.visibility = View.GONE
                    _binding!!.refundview.visibility = View.GONE
                    statusBarTextBlack()
                } else {
                    when(result){
                        "0"->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.GONE
                            _binding!!.failedScreen.visibility = View.VISIBLE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.GONE
                            _binding!!.refundview.visibility = View.GONE
                            _binding!!.cashbacklayout.visibility = View.GONE
                            _binding!!.cashbackview.visibility = View.GONE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                        "1"->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.VISIBLE
                            _binding!!.failedScreen.visibility = View.GONE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.cashbacklayout.visibility = View.GONE
                            _binding!!.cashbackview.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.VISIBLE
                            _binding!!.refundview.visibility = View.VISIBLE
                            _binding!!.refundStatuslayout.visibility = View.VISIBLE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                        "2"->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                            _binding!!.SuccessScreen.visibility = View.VISIBLE
                            _binding!!.failedScreen.visibility = View.GONE
                            _binding!!.refundStatuslayout.visibility = View.GONE
                            statusBarColor(R.color.GreenWalletBackgroundDark)
                            _binding!!.refunddetail.visibility = View.GONE
                            _binding!!.refundview.visibility = View.GONE
                            statusBarTextBlack()
                        }
                        else->{
                            _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                            _binding!!.SuccessScreen.visibility = View.GONE
                            _binding!!.failedScreen.visibility = View.VISIBLE
                            _binding!!.rewardSection.visibility = View.GONE
                            _binding!!.refunddetail.visibility = View.GONE
                            _binding!!.refundview.visibility = View.GONE
                            statusBarColor(R.color.red)
                            statusBarTextBlack()
                        }
                    }

                }
            }


        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            paymentStatusScreenFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}