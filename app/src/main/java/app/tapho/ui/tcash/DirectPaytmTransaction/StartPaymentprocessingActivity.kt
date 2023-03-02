package app.tapho.ui.tcash.DirectPaytmTransaction

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import app.tapho.R
import app.tapho.databinding.ActivityStartPaymentprocessingBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.AddMoneyRes
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class StartPaymentprocessingActivity : BaseActivity<ActivityStartPaymentprocessingBinding>() {

    var OrderId = ""

    var packageNames =""
    var DeepLink = ""
    var operator_icon = ""
    private var AddWalletAmount = ""
    val millisUntilFinished = 60000
    var pspnamedata=""
    private val REQUEST_CODE = 981223
    lateinit var coundown: CountDownTimer

    companion object{
        fun openContainerForPayment(
            context: Context?,
            type: String?,
            DeepLink: String?,
            orderID: String?,
            PackageName: String?,
            PaymentType: String?,
        ) {
            context?.startActivity(Intent(context, StartPaymentprocessingActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("PackageName", PackageName)
                putExtra("DeepLink",DeepLink)
                putExtra("orderID",orderID)
                putExtra("PaymentType",PaymentType)
            })
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPaymentprocessingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarTextWhite()
        statusBarColor(R.color.white)

        OrderId = intent?.getStringExtra("orderID").toString()

        packageNames = intent?.getStringExtra("PackageName").toString()
        DeepLink = intent?.getStringExtra("DeepLink").toString()
        AddWalletAmount =  getSharedPreference().getString("AddWalletAmount").toString()

        binding.mainLayout.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        binding.back.setOnClickListener {
            BackPressAction()
        }
        val pm = this.getPackageManager()
        Glide.with(this).load(pm.getApplicationIcon("app.tapho")).circleCrop().into(binding.operatorIcon)

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable{
            override fun run() {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink))
                intent.data = Uri.parse(DeepLink)
                intent.setPackage(packageNames.toString())
                startActivityForResult(intent, REQUEST_CODE)
            }
        },2000)
        setAllDataAndText()
    }


    private fun setAllDataAndText() {
        binding.AddMoneyAmount.text = withSuffixAmount(AddWalletAmount)

        val icon =this.getPackageManager().getApplicationIcon(packageNames)
        Glide.with(this).load(icon).circleCrop().into(binding.pspIcon)
        Glide.with(this).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.statusMark)
        setTimerData()
    }

    override fun onBackPressed() {
     //   super.onBackPressed()
        coundown.cancel()
    }

    @SuppressLint("InflateParams")
    private fun BackPressAction() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.paymentexit_bottomsheet, null)
        dialog.setCancelable(false)

        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)
        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)


        exit.setOnClickListener {
            coundown.cancel()
            finish()
        }


        app_continue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()


    }

    private fun setTimerData() {
        val pm = this.getPackageManager()
        val pspname = pm.getApplicationInfo(packageNames, PackageManager.GET_META_DATA)
        pspnamedata = pm.getApplicationLabel(pspname).toString()
        coundown = object : CountDownTimer(millisUntilFinished.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timer.text = "from "+pm.getApplicationLabel(pspname)+ "  0" + minute.toString() + ":" + seconds.toString()

            }

            override fun onFinish() {
                // Transaction Failed While Time Out
                PaymentFailedTimeOut()
            }

        }.start()
    }

    private fun PaymentFailedTimeOut() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.timeout_paymentfailed_layout, null)
        dialog.setCancelable(false)

        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)


        exit.setOnClickListener {
            coundown.cancel()
            dialog.dismiss()
           finish()
        }

        dialog.setContentView(view)
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            binding.mainLayout.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
            binding.processing.text = "Verifying Payment "
            getTrasactionStatusPage()
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
                                binding.processing.text = "Payment Verified"
                                Glide.with(this@StartPaymentprocessingActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.statusMark)
                                Glide.with(this@StartPaymentprocessingActivity).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.processingRecharge)
                                coundown.cancel()
                                ProceedToAddMoneyApiCall(it.txnAmount,it.gatewayName)
                            }else{
                                Glide.with(this@StartPaymentprocessingActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.statusMark)
                                coundown.cancel()
                                ContainerActivity.openContainerforPaymentStatus(this@StartPaymentprocessingActivity,"AddMoneyTransactionStatuspreview",it.resultInfo.resultCode,it.txnId,it.resultInfo.resultMsg,"Paytm",pspnamedata,"",null)
                                //     PaymentFailedTimeOut()
                                finish()
                            }
                        }
                    }
                }

            })
    }

    @SuppressLint("SimpleDateFormat")
    private fun ProceedToAddMoneyApiCall(txnAmount: String, payOption : String) {
        val charset =  ('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())
        val txn_id = randomString + currentDate+"T"+getUserId()+"WL"
        val pm = this.getPackageManager()
        val info = pm.getApplicationInfo(packageNames, PackageManager.GET_META_DATA)
        val payoptionName = pm.getApplicationLabel(info)
        viewModel.AddMoneyToWallet(getUserId(),txnAmount,getSharedPreference().getString("wallet_cashback"),txn_id,payoptionName.toString(),this,object :
            ApiListener<AddMoneyRes, Any?> {
            override fun onSuccess(t: AddMoneyRes?, mess: String?) {
                t.let {
                    Glide.with(this@StartPaymentprocessingActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.processingRecharge)
                    Glide.with(this@StartPaymentprocessingActivity).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.rechargeStatus)
                    if (t!!.errorCode.equals("0")){
                        Glide.with(this@StartPaymentprocessingActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.rechargeStatus)
                        setToBlankData()
                        ContainerActivity.openContainerforPaymentStatus(this@StartPaymentprocessingActivity,"AddMoneyTransactionStatuspreview",it!!.errorCode,txn_id,it.errorMsg,"wallet",pspnamedata,"",null)
                        finish()
                    }else{
                        setToBlankData()
                        ContainerActivity.openContainerforPaymentStatus(this@StartPaymentprocessingActivity,"AddMoneyTransactionStatuspreview",it!!.errorCode,txn_id,it.errorMsg,"wallet",pspnamedata,"",null)
                        finish()
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

}