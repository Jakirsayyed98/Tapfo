package app.tapho.ui.BuyVoucher.VoucherPayments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import app.tapho.R
import app.tapho.databinding.FragmentVoucherTransactionBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showLong
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.VoucherBuyingApiRes
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersData
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersDatabase
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.RechargeService.ModelData.FinalToRecharge.RechargeDoneOrNotRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.finalDatewithAMPM
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class VoucherTransactionFragment :BaseFragment<FragmentVoucherTransactionBinding>(){

    var OrderId = ""
    var PackageName = ""
    var DeepLink = ""
    var WalletAmount = ""
    var PspAppName = ""
    val millisUntilFinished = 60000
    private val REQUEST_CODE = 981223
    lateinit var database: VouchersDatabase
    lateinit var coundown: CountDownTimer

    var PaybleAmount = 0.0
    var VoucherData : ArrayList<VouchersData> = ArrayList()

    var VoucherDetailForBuy = ""
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
        _binding = FragmentVoucherTransactionBinding.inflate(inflater,container,false)

        statusBarTextWhite()
        database = VouchersDatabase.getDatabase(requireContext())
        OrderId = activity?.intent?.getStringExtra("orderID").toString()
        PackageName = activity?.intent?.getStringExtra("PackageName").toString()
        DeepLink = activity?.intent?.getStringExtra("DeepLink").toString()
        WalletAmount = getSharedPreference().getString("WalletAmount").toString()



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
        setTimerData()
        database.getVouchersDatabase().getAllVouchersData().observe(requireActivity(), androidx.lifecycle.Observer {
            VoucherData.addAll(it)
            setAllDataAndText()
        })
        return _binding?.root
    }


    @SuppressLint("SetTextI18n")
    private fun setAllDataAndText() {
        _binding!!.voucherbrand.text ="for "+VoucherData.get(0).name
        var VoucherAmount = 0
        VoucherData.forEach {
            VoucherAmount += it.ActualAmount.toInt()
        }

        VoucherData.forEach {
            PaybleAmount += it.PaybleAmount.toInt()
        }

      val MyVoucher : ArrayList<CustomeVoucherDetailForBuy> = ArrayList()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        VoucherData.forEach {
            MyVoucher.add(CustomeVoucherDetailForBuy(it.voucherID,it.denominationId,it.Qty))
        }
        VoucherDetailForBuy =gsonPretty.toJson(MyVoucher)


        _binding!!.voucherAmount.text = withSuffixAmount(VoucherAmount.toString())

        val icon = requireContext().getPackageManager().getApplicationIcon(PackageName)
        Glide.with(requireContext()).load(icon).circleCrop().into(_binding!!.pspIcon)
        Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.statusMark)
        Glide.with(requireContext()).load(VoucherData.get(0).MiniApp_icon).circleCrop().into(_binding!!.operatorIcon)

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
                                VoucherPaymentBaseActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatusForVoucher",it.resultInfo.resultCode,it.txnId,"","",PspAppName)
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
        val simpleDate = SimpleDateFormat("ddMyyyyhhmm")
        val currentDate = simpleDate.format(Date())

        val FinalorderID =randomString + currentDate+"T"+getUserId()+"WL"

        val pm = requireContext().getPackageManager()
        val info = pm.getApplicationInfo(PackageName, PackageManager.GET_META_DATA)
        val payoptionName = pm.getApplicationLabel(info)

        viewModel.addUserTransaction(getUserId(),WalletAmount,PSPtxnAmount,FinalorderID,payoptionName.toString(),"5",this,object :
            ApiListener<addUserTransactionRes, Any?> {
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
                        VoucherPaymentBaseActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatusForVoucher",it.errorCode,it.user_txn_id,"","Recharge",PspAppName)
                        activity?.finish()
                    }
                }
            }

        })
    }

    private fun processForRecharge(user_transactionId : String) {
        Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.processingRecharge)
        Glide.with(requireContext()).asGif().load(R.drawable.loading_progress).circleCrop().into(_binding!!.rechargeStatus)
        viewModel.BuyVouchersApi(getUserId(),user_transactionId,VoucherDetailForBuy,PaybleAmount.toString(),this,object : ApiListener<VoucherBuyingApiRes,Any?>{
            override fun onSuccess(t: VoucherBuyingApiRes?, mess: String?) {
                requireView().showShortSnack(t!!.errorMsg)
                Toast.makeText(requireContext(),t.errorMsg, Toast.LENGTH_LONG).show()
                t.let {

                    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss ")
                    val currentDateAndTime: String = simpleDateFormat.format(Date())


                    if (it.errorCode.equals("0")){
                            _binding!!.rechargeTime.text = currentDateAndTime
                            Glide.with(requireContext()).load(R.drawable.payment_done_icon).circleCrop().into(_binding!!.rechargeStatus)
                            setToBlankData()
                        VoucherPaymentBaseActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatusForVoucher",t.errorCode,user_transactionId,"","Recharge",PspAppName)
                            activity?.finish()
                        }else{
                            Glide.with(requireContext()).load(R.drawable.payment_failed_icon).circleCrop().into(_binding!!.rechargeStatus)
                            setToBlankData()
                        VoucherPaymentBaseActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatusForVoucher",t.errorCode,user_transactionId,"","Recharge",PspAppName)
                            activity?.finish()
                        }


                }
            }

        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setToBlankData() {

        getSharedPreference().saveString("Amount","")
        getSharedPreference().saveString("WalletAmount","")
        GlobalScope.launch {
            database.getVouchersDatabase().deleteAll()
        }

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            VoucherTransactionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}