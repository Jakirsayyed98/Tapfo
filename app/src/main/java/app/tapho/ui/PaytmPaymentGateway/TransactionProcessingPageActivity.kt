package app.tapho.ui.PaytmPaymentGateway

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import app.tapho.R
import app.tapho.databinding.ActivityTransactionProcessingPage2Binding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.RechargeService.ModelData.FinalToRecharge.RechargeDoneOrNotRes
import app.tapho.ui.RechargeService.ModelData.RechargeStatus.checkRechargeStatusRes
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class TransactionProcessingPageActivity: BaseActivity<ActivityTransactionProcessingPage2Binding>() {    private val channelID = "BitinfozCoder"


    var OrderId = ""
    var packagesname = ""
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
    val millisUntilFinished1= 300000
    val millisUntilFinished = 60000
    private val REQUEST_CODE = 981223
    lateinit var coundown: CountDownTimer
    lateinit var coundown1: CountDownTimer


    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    companion object{
        fun openContainerForPayment(
            context: Context?,
            type: String?,
            DeepLink: String?,
            orderID: String?,
            PackageName: String?,
            PaymentType: String?,
        ) {
            context?.startActivity(Intent(context, TransactionProcessingPageActivity::class.java).apply {
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
        binding = ActivityTransactionProcessingPage2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarTextWhite()
        statusBarColor(R.color.white)

        OrderId = intent?.getStringExtra("orderID").toString()

        packagesname = intent?.getStringExtra("PackageName").toString()
        DeepLink = intent?.getStringExtra("DeepLink").toString()
        operator_icon =  getSharedPreference().getString("operator_icon").toString()
        mobileNumber = getSharedPreference().getString("number").toString()
        servicetype = getSharedPreference().getString("servicetype").toString()
        operator_id = getSharedPreference().getString("operator_id").toString()
        circle_id = getSharedPreference().getString("circle_id").toString()
        Amount = getSharedPreference().getString("Amount").toString()
        WalletAmount = getSharedPreference().getString("WalletAmount").toString()
        PayableAmount = getSharedPreference().getString("PayableAmount").toString()

        binding.mainLayout.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        binding.rechargeTimer.visibility = View.GONE
        val PaymentType = intent?.getStringExtra("PaymentType").toString()


        Handler(Looper.getMainLooper()).postDelayed(object : Runnable{
            @SuppressLint("SetTextI18n")
            override fun run() {

                if (PaymentType.equals("1")){
                    binding.mainLayout.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.processing.text = "Verifying Payment "
                    ProceedForUserAddUserTransaction("0",packagesname)
                }else{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink))
                    intent.data = Uri.parse(DeepLink)
                    intent.setPackage(packagesname)
                    startActivityForResult(intent, REQUEST_CODE)
                }
            }
        },2000)
        setAllDataAndText()
    }

    @SuppressLint("SetTextI18n")
    private fun setAllDataAndText() {
        binding.mobileNumber.text ="for "+mobileNumber
        binding.rechargeAmount.text = withSuffixAmount(Amount)

        if (packagesname.equals("app.tapho")){
            val icon = R.drawable.profile_wallet_icon
            Glide.with(this).load(icon).into(binding.pspIcon)
        }else{
            val icon = this.getPackageManager().getApplicationIcon(packagesname)
            Glide.with(this).load(icon).circleCrop().into(binding.pspIcon)
        }



        Glide.with(this).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.statusMark)
        Glide.with(this).load(operator_icon).circleCrop().into(binding.operatorIcon)
        setTimerData()
    }


    override fun onBackPressed() {
 //       super.onBackPressed()
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
            coundown1.cancel()
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
        val pspname = pm.getApplicationInfo(packagesname, PackageManager.GET_META_DATA)
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

    private fun setTimerData2() {
        binding.rechargeTimer.visibility = View.VISIBLE
        coundown1 = object : CountDownTimer(millisUntilFinished1.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timer1.text =minute.toString() + ":" + seconds.toString()
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
            coundown1.cancel()
            dialog.dismiss()
            finish()
        }

        dialog.setContentView(view)
        dialog.show()

    }

    @Deprecated("Deprecated in Java")
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
                @SuppressLint("SetTextI18n")
                override fun onSuccess(t: TransactionStatusRes?, mess: String?) {
                    t!!.data.let {
                        it.body.let {
                            val result_status = it.resultInfo.resultStatus
                            if (result_status == "TXN_SUCCESS") {
                                binding.processing.text = "Payment Verified"
                                Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.statusMark)
                                Glide.with(this@TransactionProcessingPageActivity).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.processingRecharge)
                                coundown.cancel()
                                ProceedForUserAddUserTransaction(it.txnAmount,"Wallet")
                            }else{
                                Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.statusMark)
                                coundown.cancel()
                                ContainerActivity.openContainerforPaymentStatus(this@TransactionProcessingPageActivity,"TransactionStatus",it.resultInfo.resultCode,it.txnId,it.resultInfo.resultMsg,"Paytm",PspAppName,"0",null)
                                finish()
                            }
                        }
                    }
                }
            })
    }

    @SuppressLint("SimpleDateFormat")
    private fun ProceedForUserAddUserTransaction(PSPtxnAmount: String, paymentOption : String) {
        val charset = ('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())
        val FinalorderID =randomString + currentDate+"T"+getUserId()+"WL"
        val pm = this.getPackageManager()
        val info = pm.getApplicationInfo(packagesname, PackageManager.GET_META_DATA)
        val payoptionName = pm.getApplicationLabel(info)


        viewModel.addUserTransaction(getUserId(),WalletAmount,PSPtxnAmount,FinalorderID,payoptionName.toString(),"3",this,object :
            ApiListener<addUserTransactionRes, Any?> {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(t: addUserTransactionRes?, mess: String?) {
                t.let {
                    if (it!!.errorCode.toString().equals("0")){
                        binding.processing.text = "Payment Verified"
                        Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.statusMark)
                        Glide.with(this@TransactionProcessingPageActivity).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.processingRecharge)
                        coundown.cancel()
                        processForRecharge(it.user_txn_id)

                    }else{
                        Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.statusMark)
                        setToBlankData()
                        ContainerActivity.openContainerforPaymentStatus(this@TransactionProcessingPageActivity,"TransactionStatus",it.errorCode,it.user_txn_id,it.errorMsg,"Wallet",PspAppName,"0",null)
                        finish()
                    }
                }
            }
        })
    }

    private fun processForRecharge(user_transactionId : String) {
        Glide.with(this).load(R.drawable.payment_done_icon).circleCrop().into(binding.processingRecharge)
        Glide.with(this).asGif().load(R.drawable.loading_progress).circleCrop().into(binding.rechargeStatus)
        setTimerData2()
        viewModel.rechargeprocess(getUserId(),servicetype,mobileNumber,operator_id,circle_id,Amount,user_transactionId,this,object :
            ApiListener<RechargeDoneOrNotRes, Any?> {
            override fun onSuccess(t: RechargeDoneOrNotRes?, mess: String?) {
                t!!.data.let {
                    Log.d("RechargeData",it.toString())
                    when(it.Status.uppercase()){

                        "SUCCESS"->{
                            binding.rechargeTime.text = finalDatewithAMPM(it.TransactionDate)
                            Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.payment_done_icon).circleCrop().into(binding.rechargeStatus)
                            setToBlankData()
                            coundown1.cancel()
                            Notificationsend("Successfull")
                            ContainerActivity.openContainerforPaymentStatus(this@TransactionProcessingPageActivity,"TransactionStatus",t.errorCode,user_transactionId,it.Status,"Recharge",PspAppName,"2",null)
                            finish()
                        }

                        "PENDING"->{
                            binding.rechargeTime.text = finalDatewithAMPM(it.TransactionDate)
                            Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.new_failed_icon).circleCrop().into(binding.rechargeStatus)
                            setToBlankData()
                            coundown1.cancel()
                            Notificationsend("processing")
                            ContainerActivity.openContainerforPaymentStatus(this@TransactionProcessingPageActivity,"TransactionStatus",t.errorCode,user_transactionId,it.Status,"Recharge",PspAppName,"0",null)
                            finish()
//                            getTrasactionStatusLog(user_transactionId)
                        }

                        "FAILURE"->{
                            Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.rechargeStatus)
                            setToBlankData()
                            coundown1.cancel()
                            Notificationsend("Failed")
                            ContainerActivity.openContainerforPaymentStatus(this@TransactionProcessingPageActivity,"TransactionStatus",t.errorCode,user_transactionId,it.Status,"Recharge",PspAppName,"1",null)
                            finish()
                        }

                        else ->{
                            Glide.with(this@TransactionProcessingPageActivity).load(R.drawable.payment_failed_icon).circleCrop().into(binding.rechargeStatus)
                            setToBlankData()
                            coundown1.cancel()
                            ContainerActivity.openContainerforPaymentStatus(this@TransactionProcessingPageActivity,"TransactionStatus",t.errorCode,user_transactionId,it.Status,"Recharge",PspAppName,"1",null)
                            finish()
                        }

                    }
                }

            }
        })
    }



//    Recharge for ₹10 is under processing
    fun Notificationsend(status:String) {
        val strtitle = "Recharge for ${withSuffixAmount(Amount)} is $status"
        val strtext = "No extra charges for mobile recharge"
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val intent:Intent= Intent(this,HomeActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

//        val contentView = RemoteViews(packageName, R.layout.activity_after_notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(strtitle)
                .setContentText(strtext)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.app_icon))
//                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(strtitle)
                .setContentText(strtext)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.app_icon))
//                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())

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


}