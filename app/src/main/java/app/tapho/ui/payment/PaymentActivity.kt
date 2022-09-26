package app.tapho.ui.payment

import android.os.Bundle
import app.tapho.databinding.ActivityPaymentBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.payment.model.TransactionTokenRes
import app.tapho.utils.appLog
import com.google.gson.Gson
import net.one97.paytm.nativesdk.PaytmSDK
import net.one97.paytm.nativesdk.Utils.Server
import net.one97.paytm.nativesdk.app.PaytmSDKCallbackListener
import net.one97.paytm.nativesdk.dataSource.models.UpiCollectRequestModel
import net.one97.paytm.nativesdk.dataSource.models.UpiIntentRequestModel
import net.one97.paytm.nativesdk.instruments.upicollect.models.UpiOptionsModel
import net.one97.paytm.nativesdk.transcation.model.TransactionInfo
import java.util.*


class PaymentActivity : BaseActivity<ActivityPaymentBinding>(), PaytmSDKCallbackListener {
    private val clientId = "g7vCrf7%1QPDUaPE"
    private val mid = "mflHPl34649927055038"
    private var amount: Double = 0.0
    private var orderId: String = ""
    private var paytmSDK: PaytmSDK? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    /**
     * @see https://developer.paytm.com/docs/account-linking/
     */
    private fun init() {
        val paymentsUtilRepository = PaytmSDK.getPaymentsUtilRepository()
        val isInstalled = paymentsUtilRepository.isPaytmAppInstalled(this)
        val authCode = paymentsUtilRepository.fetchAuthCode(this, clientId, mid)

        amount = 100.0
        orderId = System.currentTimeMillis().toString()

        gettxnToken()
    }


//    https://developer.paytm.com/docs/transaction-processing/

    private fun makePayment(transactionToken: String) {
        val builder = PaytmSDK.Builder(this, mid, orderId, transactionToken, amount, this)
        PaytmSDK.setServer(Server.STAGING)
        builder.setMerchantCallbackUrl("https://merchant.com/callback")
        paytmSDK = builder.build()
        val upiApps = PaytmSDK.getPaymentsHelper().getUpiAppsInstalled(this)

        startTransaction(null)
//        SelectUpiAppBottomSheetDialog.newInstance(upiApps, amount).show(supportFragmentManager, "")
    }

    fun startTransaction(upiIntentModel: UpiOptionsModel?) {
        if (upiIntentModel != null) {
            val upiCollectRequestModel = UpiIntentRequestModel("NONE",
                upiIntentModel.appName,
                upiIntentModel.resolveInfo.activityInfo)
            paytmSDK?.startTransaction(this, upiCollectRequestModel)
        } else {
            val upiCollectRequestModel = UpiCollectRequestModel("NONE", "7777777777@paytm")
            paytmSDK?.startTransaction(this, upiCollectRequestModel)
        }
    }

    override fun onTransactionResponse(p0: TransactionInfo?) {
        appLog(Gson().toJson(p0))
    }

    override fun networkError() {
        showMess("Network error")
    }

    override fun onBackPressedCancelTransaction() {

    }

    override fun onGenericError(p0: Int, p1: String?) {
        showMess(p1)
    }

    private fun gettxnToken() {
        viewModel.getTransactionToken(getUserId(), amount, orderId, this,
            object : ApiListener<TransactionTokenRes, Any?> {
                override fun onSuccess(t: TransactionTokenRes?, mess: String?) {
                    t?.let {
                        t.body?.resultInfo?.let {
                            if (it.resultStatus == "S") {
                                t.body?.txnToken?.let { it1 ->
                                    makePayment(it1) }
                            } else
                                showMess(it.resultMsg)
                        }
                    }
                }
            })
    }
}