package app.tapho.ui.PaytmPaymentGateway

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import app.tapho.R
import app.tapho.databinding.FragmentTranscationProcessingPageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.RechargeService.ModelData.FinalToRecharge.RechargeDoneOrNotRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


class TranscationProcessingPageFragment : BaseFragment<FragmentTranscationProcessingPageBinding>() {

    var OrderId = ""
    var PackageName = ""
    var DeepLink = ""
    var operator_icon = ""
    var mobileNumber = ""
    var servicetype = ""
    var operator_id = ""
    var circle_id = ""
    var Amount = ""
    var WalletAmount = ""
    var PayableAmount = ""
    var PspAppName = ""
    val millisUntilFinished = 60000
    private val REQUEST_CODE = 981223
    lateinit var coundown: CountDownTimer
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
        _binding = FragmentTranscationProcessingPageBinding.inflate(inflater, container, false)
        statusBarTextWhite()
        OrderId = activity?.intent?.getStringExtra("orderID").toString()

        PackageName = activity?.intent?.getStringExtra("PackageName").toString()
        DeepLink = activity?.intent?.getStringExtra("DeepLink").toString()
        operator_icon =  getSharedPreference().getString("operator_icon").toString()
        mobileNumber = getSharedPreference().getString("number").toString()
        servicetype = getSharedPreference().getString("servicetype").toString()
        operator_id = getSharedPreference().getString("operator_id").toString()
        circle_id = getSharedPreference().getString("circle_id").toString()
        Amount = getSharedPreference().getString("Amount").toString()
        WalletAmount = getSharedPreference().getString("WalletAmount").toString()
        PayableAmount = getSharedPreference().getString("PayableAmount").toString()

        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE

        val PaymentType = activity?.intent?.getStringExtra("PaymentType").toString()
        onBackPressed()


        Handler(Looper.getMainLooper()).postDelayed(object : Runnable{
            @SuppressLint("SetTextI18n")
            override fun run() {

                if (PaymentType.equals("1")){
                    _binding!!.mainLayout.visibility = View.VISIBLE
                    _binding!!.progress.visibility = View.GONE
                    _binding!!.processing.text = "Verifying Payment "
                    ProceedForUserAddUserTransaction("0",PackageName)
                }else{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink))
                    intent.data = Uri.parse(DeepLink)
                    intent.setPackage(PackageName)
                    startActivityForResult(intent, REQUEST_CODE)
                }
            }
        },2000)
        setAllDataAndText()

        return _binding?.root
    }

    @SuppressLint("SetTextI18n")
    private fun setAllDataAndText() {
        _binding!!.mobileNumber.text ="for "+mobileNumber
        _binding!!.rechargeAmount.text = withSuffixAmount(Amount)

        val icon = requireContext().getPackageManager().getApplicationIcon(PackageName)
        Glide.with(requireContext()).load(icon).circleCrop().into(_binding!!.pspIcon)
        Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.statusMark)
        Glide.with(requireContext()).load(operator_icon).circleCrop().into(_binding!!.operatorIcon)
        setTimerData()
    }

    fun onBackPressed() {
        _binding!!.back.setOnClickListener {
            BackPressAction()
        }
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                BackPressAction()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback)
    }

    @SuppressLint("InflateParams")
    private fun BackPressAction() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.paymentexit_bottomsheet, null)
        dialog.setCancelable(false)

        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)
        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)


        exit.setOnClickListener {
            coundown.cancel()
            activity?.finish()
        }


        app_continue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()


    }

    private fun setTimerData() {
        val pm = requireContext().getPackageManager()
        val pspname = pm.getApplicationInfo(PackageName, PackageManager.GET_META_DATA)
        PspAppName = pm.getApplicationLabel(pspname).toString()
        coundown = object : CountDownTimer(millisUntilFinished.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                _binding!!.timer.text =getString(R.string.from_phonepe_0_59,pm.getApplicationLabel(pspname),minute.toString() + ":" + seconds.toString())// "from "+pm.getApplicationLabel(pspname)+ "  0" + minute.toString() + ":" + seconds.toString()
            }
            override fun onFinish() {
                // Transaction Failed While Time Out
                PaymentFailedTimeOut()
            }

        }.start()
    }

    @SuppressLint("InflateParams")
    private fun PaymentFailedTimeOut() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.timeout_paymentfailed_layout, null)
        dialog.setCancelable(false)

        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)


        exit.setOnClickListener {
            coundown.cancel()
            dialog.dismiss()
            activity?.finish()
        }

        dialog.setContentView(view)
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE) {
                _binding!!.mainLayout.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
                _binding!!.processing.text = "Verifying Payment "
                getTrasactionStatusPage()
                onBackPressed()
            }
    }

    private fun getTrasactionStatusPage() {
        viewModel.getTransactionStatus(getUserId(), OrderId, this,
            object : ApiListener<TransactionStatusRes, Any?> {
                override fun onSuccess(t: TransactionStatusRes?, mess: String?) {
                    t!!.data.let {
                        it.body.let {
                            val result_status = it.resultInfo.resultStatus
                            if (result_status == "TXN_SUCCESS") {
                                _binding!!.processing.text = "Payment Verified"
                                Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.statusMark)
                                Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.processingRecharge)
                                coundown.cancel()
                                 ProceedForUserAddUserTransaction(it.txnAmount,"Wallet")
                                //processForRecharge()
                            }else{
                                Glide.with(requireContext()).load(R.drawable.payment_failed_icon).circleCrop().into(_binding!!.statusMark)
                                coundown.cancel()
                                ContainerActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatus",it.resultInfo.resultCode,it.txnId,it.resultInfo.resultMsg,"Paytm",PspAppName,"",null)
                                activity?.finish()
                               // PaymentFailedTimeOut()
                            }
                        }
                    }
                }

            })
    }

    private fun ProceedForUserAddUserTransaction( PSPtxnAmount: String,paymentOption : String) {

        val charset = ('A'..'Z') + ('0'..'9')

        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())

        val FinalorderID =randomString + currentDate+"T"+getUserId()+"WL"

        val pm = requireContext().getPackageManager()
        val info = pm.getApplicationInfo(PackageName, PackageManager.GET_META_DATA)
        val payoptionName = pm.getApplicationLabel(info)

        viewModel.addUserTransaction(getUserId(),WalletAmount,PSPtxnAmount,FinalorderID,payoptionName.toString(),"3",this,object : ApiListener<addUserTransactionRes,Any?>{
            override fun onSuccess(t: addUserTransactionRes?, mess: String?) {
                t.let {

                    if (it!!.errorCode.toString().equals("0")){
                        _binding!!.processing.text = "Payment Verified"
                        Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.statusMark)
                        Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.processingRecharge)
                        coundown.cancel()
                        requireView().showShortSnack(it.errorMsg)
                        processForRecharge(it.user_txn_id)
                    }else{
                        Glide.with(requireContext()).load(R.drawable.payment_failed_icon).circleCrop().into(_binding!!.statusMark)
                        requireView().showShortSnack(it.errorMsg)
                        setToBlankData()
                        ContainerActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatus",it.errorCode,it.user_txn_id,it.errorMsg,"Wallet",PspAppName,"",null)
                        activity?.finish()
                    }
                }
            }

        })
    }

    private fun processForRecharge(user_transactionId : String) {
        Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.processingRecharge)
        Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.rechargeStatus)
        viewModel.rechargeprocess(getUserId(),servicetype,mobileNumber,operator_id,circle_id,Amount,user_transactionId,this,object : ApiListener<RechargeDoneOrNotRes,Any?>{
            override fun onSuccess(t: RechargeDoneOrNotRes?, mess: String?) {
                requireView().showShortSnack(t!!.errorMsg)
                t.let {
                    it!!.data.let {
                        if (it.Status.equals("Success")){
                            _binding!!.rechargeTime.text = finalDatewithAMPM(it.TransactionDate)
                            Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.rechargeStatus)
                            setToBlankData()
                            ContainerActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatus",t.errorCode,user_transactionId,it.Status,"Recharge",PspAppName,"",null)
                            activity?.finish()
                        }else{
                            Glide.with(requireContext()).load(R.drawable.payment_failed_icon).circleCrop().into(_binding!!.rechargeStatus)
                            setToBlankData()
                            ContainerActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatus",t.errorCode,user_transactionId,it.Status,"Recharge",PspAppName,"",null)
                            activity?.finish()
                        }
                    }

                }
            }
        })
    }

    private fun setToBlankData() {
        getSharedPreference().saveString("Operator_Code","")
        getSharedPreference().saveString("Circle_Code","")
        getSharedPreference().saveString("number","")
        getSharedPreference().saveString("servicetype","")
        getSharedPreference().saveString("discription","")
        getSharedPreference().saveString("Amount","")
        getSharedPreference().saveString("operator_name","")
        getSharedPreference().saveString("circle_name","")
        getSharedPreference().saveString("operator_icon","")
        getSharedPreference().saveString("circle_id","")
        getSharedPreference().saveString("operator_id","")
        getSharedPreference().saveString("WalletAmount","")
        getSharedPreference().saveString("PayableAmount","")
        getSharedPreference().saveString("min_recharge","")
        getSharedPreference().saveString("user_commission","")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TranscationProcessingPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}