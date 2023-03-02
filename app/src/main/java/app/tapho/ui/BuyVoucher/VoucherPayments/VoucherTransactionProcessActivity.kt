package app.tapho.ui.BuyVoucher.VoucherPayments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import app.tapho.R
import app.tapho.databinding.ActivityVoucherTransactionProcessBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.VoucherBuyingApiRes
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersData
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersDatabase
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VoucherTransactionProcessActivity : BaseActivity<ActivityVoucherTransactionProcessBinding>() {

    var OrderId = ""
    var packagesName = ""
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

   companion object{
       fun openContainerForPayment(
           context: Context?,
           type: String?,
           DeepLink: String?,
           orderID: String?,
           PackageName: String?,
           PaymentType: String?,
       ) {
           context?.startActivity(Intent(context, VoucherTransactionProcessActivity::class.java).apply {
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
        binding = ActivityVoucherTransactionProcessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        database = VouchersDatabase.getDatabase(this)
        OrderId = intent?.getStringExtra("orderID").toString()
        packagesName = intent?.getStringExtra("PackageName").toString()
        DeepLink = intent?.getStringExtra("DeepLink").toString()
        WalletAmount = getSharedPreference().getString("WalletAmount").toString()

        binding.mainLayout.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE

        val PaymentType = intent?.getStringExtra("PaymentType").toString()
        binding.back.setOnClickListener {
            BackPressAction()
        }

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable{
            @SuppressLint("SetTextI18n")
            override fun run() {

                if (PaymentType.equals("1")){
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.processing.text = "Verifying Payment "
                    ProceedForUserAddUserTransaction("0",packagesName)
                }else{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink))
                    intent.data = Uri.parse(DeepLink)
                    intent.setPackage(packagesName)
                    startActivityForResult(intent, REQUEST_CODE)
                }
            }
        },2000)
        setTimerData()
        database.getVouchersDatabase().getAllVouchersData().observe(this, androidx.lifecycle.Observer {
            VoucherData.addAll(it)
            setAllDataAndText()
        })
    }



    @SuppressLint("SetTextI18n")
    private fun setAllDataAndText() {
        binding.voucherbrand.text ="for "+VoucherData.get(0).name
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


        binding.voucherAmount.text = withSuffixAmount(VoucherAmount.toString())

        val icon = this.getPackageManager().getApplicationIcon(packagesName)
        Glide.with(this).load(icon).circleCrop().into(binding.pspIcon)
        Glide.with(this).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.statusMark)
        Glide.with(this).load(VoucherData.get(0).MiniApp_icon).circleCrop().into(binding.operatorIcon)

    }

    override fun onBackPressed() {
      //  super.onBackPressed()
        BackPressAction()
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
        val pspname = pm.getApplicationInfo(packagesName, PackageManager.GET_META_DATA)
        PspAppName = pm.getApplicationLabel(pspname).toString()
        coundown = object : CountDownTimer(millisUntilFinished.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timer.text =getString(R.string.from_phonepe_0_59,pm.getApplicationLabel(pspname),minute.toString() + ":" + seconds.toString())// "from "+pm.getApplicationLabel(pspname)+ "  0" + minute.toString() + ":" + seconds.toString()
            }
            override fun onFinish() {
                // Transaction Failed While Time Out
                PaymentFailedTimeOut()
            }

        }.start()
    }

    @SuppressLint("InflateParams")
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
                                Glide.with(this@VoucherTransactionProcessActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.statusMark)
                                Glide.with(this@VoucherTransactionProcessActivity).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.processingRecharge)
                                coundown.cancel()
                                ProceedForUserAddUserTransaction(it.txnAmount,"Wallet")
                                //processForRecharge()
                            }else{
                                Glide.with(this@VoucherTransactionProcessActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.statusMark)
                                coundown.cancel()
                                VoucherPaymentBaseActivity.openContainerforPaymentStatus(this@VoucherTransactionProcessActivity,"TransactionStatusPreview",it.resultInfo.resultCode,it.txnId,"","",PspAppName)
                                finish()
                                // PaymentFailedTimeOut()
                            }
                        }
                    }
                }

            })
    }

    private fun ProceedForUserAddUserTransaction( PSPtxnAmount: String,paymentOption : String) {

        val charset =  ('A'..'Z') + ('0'..'9')

        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())

        val FinalorderID =randomString  + currentDate+"T"+getUserId()+"WL"

        val pm = this.getPackageManager()
        val info = pm.getApplicationInfo(packagesName, PackageManager.GET_META_DATA)
        val payoptionName = pm.getApplicationLabel(info)

        viewModel.addUserTransaction(getUserId(),WalletAmount,PSPtxnAmount,FinalorderID,payoptionName.toString(),"5",this,object :
            ApiListener<addUserTransactionRes, Any?> {
            override fun onSuccess(t: addUserTransactionRes?, mess: String?) {
                t.let {

                    if (it!!.errorCode.toString().equals("0")){
                        binding.processing.text = "Payment Verified"
                        Glide.with(this@VoucherTransactionProcessActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.statusMark)
                        Glide.with(this@VoucherTransactionProcessActivity).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.processingRecharge)
                        coundown.cancel()
                        processForRecharge(it.user_txn_id)
                    }else{
                        Glide.with(this@VoucherTransactionProcessActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.statusMark)

                        setToBlankData()
                        VoucherPaymentBaseActivity.openContainerforPaymentStatus(this@VoucherTransactionProcessActivity,"TransactionStatusPreview",it.errorCode,it.user_txn_id,"","Recharge",PspAppName)
                        finish()
                    }
                }
            }

        })
    }

    private fun processForRecharge(user_transactionId : String) {
        Glide.with(this).load(R.drawable.payment_done_icon).circleCrop().into(binding.processingRecharge)
        Glide.with(this).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.rechargeStatus)
        viewModel.BuyVouchersApi(getUserId(),user_transactionId,VoucherDetailForBuy,PaybleAmount.toString(),this,object :
            ApiListener<VoucherBuyingApiRes, Any?> {
            override fun onSuccess(t: VoucherBuyingApiRes?, mess: String?) {
                Toast.makeText(this@VoucherTransactionProcessActivity,t!!.errorMsg, Toast.LENGTH_LONG).show()
                t.let {

                    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss ")
                    val currentDateAndTime: String = simpleDateFormat.format(Date())


                    if (it.errorCode.equals("0")){
                        binding.rechargeTime.text = currentDateAndTime
                        Glide.with(this@VoucherTransactionProcessActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.rechargeStatus)
                        setToBlankData()
                        VoucherPaymentBaseActivity.openContainerforPaymentStatus(this@VoucherTransactionProcessActivity,"TransactionStatusPreview",t.errorCode,user_transactionId,"","Recharge",PspAppName)
                        finish()
                    }else{
                        Glide.with(this@VoucherTransactionProcessActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.rechargeStatus)
                        setToBlankData()
                        VoucherPaymentBaseActivity.openContainerforPaymentStatus(this@VoucherTransactionProcessActivity,"TransactionStatusPreview",t.errorCode,user_transactionId,"","Recharge",PspAppName)
                        finish()
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


}