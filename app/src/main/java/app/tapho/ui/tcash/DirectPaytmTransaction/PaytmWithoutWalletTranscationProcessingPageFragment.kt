package app.tapho.ui.tcash.DirectPaytmTransaction

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import app.tapho.R
import app.tapho.databinding.FragmentPaytmWithoutWalletTranscationProcessingPageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.AddMoneyRes
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


class PaytmWithoutWalletTranscationProcessingPageFragment : BaseFragment<FragmentPaytmWithoutWalletTranscationProcessingPageBinding>() {

    var OrderId = ""
    var PackageName = ""
    var DeepLink = ""
    var operator_icon = ""
    var AddWalletAmount = ""
    val millisUntilFinished = 60000
    var pspnamedata=""
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
        _binding = FragmentPaytmWithoutWalletTranscationProcessingPageBinding.inflate(inflater, container, false)
        statusBarTextWhite()
        OrderId = activity?.intent?.getStringExtra("orderID").toString()

        PackageName = activity?.intent?.getStringExtra("PackageName").toString()
        DeepLink = activity?.intent?.getStringExtra("DeepLink").toString()
        AddWalletAmount =  getSharedPreference().getString("AddWalletAmount").toString()

        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE

        onBackPressed()
        val pm = requireContext().getPackageManager()
        Glide.with(requireContext()).load(pm.getApplicationIcon("app.tapho")).circleCrop().into(_binding!!.operatorIcon)

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable{
            override fun run() {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink))
                    intent.data = Uri.parse(DeepLink)
                    intent.setPackage(PackageName.toString())
                    startActivityForResult(intent, REQUEST_CODE)
                BackPressAction()
            }
        },2000)
        setAllDataAndText()

        return _binding?.root
    }

    private fun setAllDataAndText() {
        _binding!!.AddMoneyAmount.text = withSuffixAmount(AddWalletAmount)

        val icon = requireContext().getPackageManager().getApplicationIcon(PackageName)
        Glide.with(requireContext()).load(icon).circleCrop().into(_binding!!.pspIcon)
        Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.statusMark)
        setTimerData()
    }

    fun onBackPressed() {
        _binding!!.back.setOnClickListener {
            BackPressAction()
        }
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                BackPressAction()
                coundown.cancel()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

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
        pspnamedata = pm.getApplicationLabel(pspname).toString()
        coundown = object : CountDownTimer(millisUntilFinished.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
      //          _binding!!.timer.text = "from "+pm.getApplicationLabel(pspname)+ "  0" + minute.toString() + ":" + seconds.toString()

            }

            override fun onFinish() {
                // Transaction Failed While Time Out
                PaymentFailedTimeOut()
            }

        }.start()
    }

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
                                ProceedToAddMoneyApiCall(it.txnAmount,it.gatewayName)
                            }else{
                                Glide.with(requireContext()).load(R.drawable.payment_failed_icon).circleCrop().into(_binding!!.statusMark)
                                coundown.cancel()
                                ContainerActivity.openContainerforPaymentStatus(requireContext(),"AddMoneyTransactionStatuspreview",it.resultInfo.resultCode,it.txnId,it.resultInfo.resultMsg,"Paytm",pspnamedata,"",null)
                           //     PaymentFailedTimeOut()
                                activity?.finish()
                            }
                        }
                    }
                }

            })
    }

    private fun ProceedToAddMoneyApiCall(txnAmount: String,payOption : String) {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = List(7) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())
        val txn_id = randomString + "" + "" + currentDate+"TAPFO"+getUserId()+"WL"
//        Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.processingRecharge)
//        Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.rechargeStatus)
        val pm = requireContext().getPackageManager()
        val info = pm.getApplicationInfo(PackageName, PackageManager.GET_META_DATA)
        val payoptionName = pm.getApplicationLabel(info)
        viewModel.AddMoneyToWallet(getUserId(),txnAmount,getSharedPreference().getString("wallet_cashback"),txn_id,payoptionName.toString(),this,object : ApiListener<AddMoneyRes,Any?>{
            override fun onSuccess(t: AddMoneyRes?, mess: String?) {
                t.let {
                    Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.processingRecharge)
                    Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.rechargeStatus)
                    if (t!!.errorCode.toString().equals("0")){
                        Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.rechargeStatus)
                        requireView().showShortSnack(it!!.errorMsg)
                        setToBlankData()
                        ContainerActivity.openContainerforPaymentStatus(requireContext(),"AddMoneyTransactionStatuspreview",it.errorCode,txn_id,it.errorMsg,"wallet",pspnamedata,"",null)
                        activity?.finish()
                    }else{
                        setToBlankData()
                        ContainerActivity.openContainerforPaymentStatus(requireContext(),"AddMoneyTransactionStatuspreview",it!!.errorCode,txn_id,it.errorMsg,"wallet",pspnamedata,"",null)
                        requireView().showShortSnack(t.errorMsg)
                        activity?.finish()
                    }
                }
            }

        })
    }

    private fun setToBlankData() {
        getSharedPreference().saveString("PackageName","")
        getSharedPreference().saveString("DeepLink","")
        getSharedPreference().saveString("operator_icon","")
        getSharedPreference().saveString("AddWalletAmount","")
        getSharedPreference().saveString("wallet_cashback","0")
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            PaytmWithoutWalletTranscationProcessingPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}