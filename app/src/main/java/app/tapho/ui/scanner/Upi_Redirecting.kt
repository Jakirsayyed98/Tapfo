package app.tapho.ui.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import app.tapho.databinding.ActivityUpiRedirectingBinding
import app.tapho.ui.BaseActivity
import com.airbnb.lottie.parser.IntegerParser
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import java.text.SimpleDateFormat
import java.util.*


class Upi_Redirecting : BaseActivity<ActivityUpiRedirectingBinding>()  {

    var UPI_PAYMENT=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUpiRedirectingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.idEdtName.text=scanner.UPI_paying_name.toString()
        binding.idEdtUpi.text=scanner.main_upi_id.toString()

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
        val transcId = df.format(c)

        binding.idBtnMakePayment.setOnClickListener {
            var amountdata:String?=null
            if (!binding.idEdtAmount.text.contains(".")){
                amountdata=binding.idEdtAmount.text.toString()+".00"
            }else{
              amountdata=binding.idEdtAmount.text.toString()
            }

           // var namedata=binding.idEdtName.text.toString()
            var namedata=scanner.UPI_paying_name.toString()
//            var upi_data=binding.idEdtUpi.text.toString()
            var upi_data=scanner.main_upi_id.toString()
            var upi_dis=binding.idEdtDescription.text.toString()
            if (amountdata.isNullOrEmpty() && namedata.isNullOrEmpty() && upi_data.isNullOrEmpty() && upi_dis.isNullOrEmpty()){
                Toast.makeText(applicationContext, "Please enter all the details..", Toast.LENGTH_SHORT).show();
            }else{
                makepaymentupi(amountdata,namedata,upi_data,upi_dis,transcId)
            //    makepaymentupidata(amountdata,namedata,upi_data,upi_dis,transcId)
            }
        }


    }

    private fun makepaymentupi(
        amountdata: String, namedata: String, upiData: String, upiDis: String, transcId: String) {
        var uri:Uri=Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa",upiData)
            .appendQueryParameter("pn",namedata)
            .appendQueryParameter("tn",upiDis)
            .appendQueryParameter("am",amountdata)
            .appendQueryParameter("cu","INR")
            .build()

        val upiIntent:Intent=Intent(Intent.ACTION_VIEW)
        upiIntent.data=uri

        val chosser=Intent.createChooser(upiIntent,"Pay Using")

        if (null != chosser.resolveActivity(packageManager)){
            startActivityForResult(chosser,UPI_PAYMENT)
        }else{
            Toast.makeText(applicationContext,"UPI App not found",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    if (trxt != null) {
                        dataList.add(trxt)
                    }
//                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null") //when user simply back without payment
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(this)) {
            var str: String? = data[0]
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }

            if (status == "success") {
                //Code to handle successful transaction here.
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show()
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {

        fun isConnectionAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected
                    && netInfo.isConnectedOrConnecting
                    && netInfo.isAvailable) {
                    return true
                }
            }
            return false
        }
    }
/*
    private fun makepaymentupidata(
        amountdata: String,
        namedata: String,
        upiData: String,
        upiDis: String,
        transcId: String
    ) {
       // var myamountdata = amountdata.toFloat()
        var easyUpiPayment:EasyUpiPayment=EasyUpiPayment.Builder()
            .with(this).setPayeeVpa(upiData)
            .setPayeeName(namedata)
            .setTransactionId(transcId)
            .setTransactionRefId(transcId)
            .setDescription(upiDis)
            .setAmount(amountdata)
            .build()

        easyUpiPayment.startPayment()
        easyUpiPayment.setPaymentStatusListener(this)
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        // on below line we are getting details about transaction when completed.
        var transactiondetail=transactionDetails!!.status.toString()+ "\n"+"Transaction ID: "+ transactionDetails.transactionId
        binding.idTVTransactionDetails.visibility=View.VISIBLE
        binding.idTVTransactionDetails.text= transactionDetails.toString()
    }

    override fun onTransactionSuccess() {
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
    }

    override fun onTransactionSubmitted() {
        Log.d("TAG", "TRANSACTION SUBMIT");
    }

    override fun onTransactionFailed() {
        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show();
    }

    override fun onTransactionCancelled() {
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
    }

    override fun onAppNotFound() {
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show();
    }
*/

}