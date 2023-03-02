package app.tapho.ui.ScanAndPay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import app.tapho.R
import app.tapho.databinding.ActivityScanAndPayPaymentProcessingBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShort
import app.tapho.ui.BaseActivity
import app.tapho.ui.PaytmPaymentGateway.TransactionStatus.TransactionStatusRes
import app.tapho.ui.tcash.AddMoneyPopup.AddMoneyModel.AddMoneyRes
import app.tapho.ui.walletTransactionData.addUserTransactionRes
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.DATA
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class ScanAndPayPaymentProcessingActivity : BaseActivity<ActivityScanAndPayPaymentProcessingBinding>() {

    companion object{
        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            DeepLink: String?,
            packagesname: String?,
            orderID: String?,
        ) {
            context?.startActivity(Intent(context, ScanAndPayPaymentProcessingActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("DeepLink", DeepLink)
                putExtra("packagesname", packagesname)
                putExtra("orderID", orderID)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }
    }
    var pspPayAmount =""
    var walletPayAmount =""
    var Notes =""
    var DeepLink =""
    var packagesname =""
    var orderID =""
    val REQUEST_CODE = 17102000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanAndPayPaymentProcessingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        pspPayAmount = getSharedPreference().getString("PspPayAmount").toString()
        walletPayAmount = getSharedPreference().getString("WalletPayAmount").toString()
        Notes= getSharedPreference().getString("TransactionNote").toString()
        DeepLink = intent?.getStringExtra("DeepLink").toString()
        packagesname = intent?.getStringExtra("packagesname").toString()
        orderID = intent?.getStringExtra("orderID").toString()
        binding.progress.visibility = View.VISIBLE
        binding.statusGIF.visibility = View.GONE

//        if (pspPayAmount.toDouble()<=0){
//            walletDeductAmount(pspPayAmount,walletPayAmount)
//        }else{
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink))
//            intent.data = Uri.parse(DeepLink)
//            intent.setPackage(packagesname)
//            startActivityForResult(intent, REQUEST_CODE)
//        }

        if (pspPayAmount.isNullOrEmpty()){

            walletDeductAmount(pspPayAmount,walletPayAmount)

        }else{
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeepLink))
//            intent.data = Uri.parse(DeepLink)
//            intent.setPackage(packagesname)
//            startActivityForResult(intent, REQUEST_CODE)
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            getTrasactionStatusPage()
        }else{
            binding.progress.visibility = View.GONE
            binding.statusGIF.visibility = View.VISIBLE
            setDataToBlank()
            Glide.with(this).asGif().load(R.drawable.new_failed_icon).into(binding.statusGIF)
        }

    }

    private fun getTrasactionStatusPage() {
        viewModel.getTransactionStatus(getUserId(), orderID, this,
            object : ApiListener<TransactionStatusRes, Any?> {
                override fun onSuccess(t: TransactionStatusRes?, mess: String?) {
                    t!!.data.let {
                        it.body.let {
                            val result_status = it.resultInfo.resultStatus
                            if (result_status == "TXN_SUCCESS") {
                                walletDeductAmount(it.txnAmount,walletPayAmount)
                            }else{
                                binding.progress.visibility = View.GONE
                                binding.statusGIF.visibility = View.VISIBLE
                                setDataToBlank()
                                Glide.with(this@ScanAndPayPaymentProcessingActivity).asGif().load(R.drawable.new_failed_icon).into(binding.statusGIF)
                            }
                        }
                    }
                }

            })
    }

    private fun walletDeductAmount(pspPayAmount: String, walletPayAmount: String) {
        val charset = ('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())
        val FinalorderID =randomString + currentDate+"TAPFO"+getUserId()+"WL"
        val pm = this.getPackageManager()
        val info = pm.getApplicationInfo(packagesname, PackageManager.GET_META_DATA)
        val payoptionName = pm.getApplicationLabel(info)
        var pspAmount = ""
        if (pspPayAmount.isNullOrEmpty()){
            pspAmount ="0"
        }else{
            pspAmount = pspPayAmount
        }
        viewModel.addUserTransaction(getUserId(),walletPayAmount,pspAmount,FinalorderID,payoptionName.toString(),"5",this,object : ApiListener<addUserTransactionRes,Any?>{
            override fun onSuccess(t: addUserTransactionRes?, mess: String?) {
                t!!.let {
                    if(it.user_txn_id.isNullOrEmpty().not()){

                        val amount = pspAmount.toDouble()+walletPayAmount.toDouble()
                        sendToUserAccount(amount.toString())
                    }else{
                        binding.progress.visibility = View.GONE
                        binding.statusGIF.visibility = View.VISIBLE
                        setDataToBlank()
                        Glide.with(this@ScanAndPayPaymentProcessingActivity).asGif().load(R.drawable.new_failed_icon).into(binding.statusGIF)
                    }
                }
            }

        })
    }

    private fun sendToUserAccount(amount : String) {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = List(7) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())
        val txn_id = randomString + currentDate+"TAPFO"+getUserId()+"WL"
        val reciver_userid = getSharedPreference().getString("payingToUser").toString().replace("tapfo0","")
        val name =getSharedPreference().getLoginData()!!.name.toString()
        viewModel.AddMoneyToWallet(reciver_userid,amount,"",txn_id,"Recived from "+name,this,object : ApiListener<AddMoneyRes,Any?>{
            override fun onSuccess(t: AddMoneyRes?, mess: String?) {
             t!!.let {
                 if (it.errorCode.equals("0")){
                     setDataToBlank()
                     this@ScanAndPayPaymentProcessingActivity.showShort("Money added "+it.errorMsg)
                     binding.progress.visibility = View.GONE
                     binding.statusGIF.visibility = View.VISIBLE
                     Glide.with(this@ScanAndPayPaymentProcessingActivity).asGif().load(R.drawable.sucess_icon).into(binding.statusGIF)
                 }else{
                     binding.progress.visibility = View.GONE
                     binding.statusGIF.visibility = View.VISIBLE
                     Glide.with(this@ScanAndPayPaymentProcessingActivity).asGif().load(R.drawable.new_failed_icon).into(binding.statusGIF)

                 }
             }
            }
        })
    }

    private fun setDataToBlank() {
        getSharedPreference().let {
            it.saveString("payingToUser","")
            it.saveString("WalletPayAmount","")
            it.saveString("TransactionNote","")
            it.saveString("PspPayAmount","")
        }
    }


}