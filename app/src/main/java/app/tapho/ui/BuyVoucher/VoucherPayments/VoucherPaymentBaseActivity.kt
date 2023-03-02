package app.tapho.ui.BuyVoucher.VoucherPayments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityVoucherPaymentBaseBinding
import app.tapho.ui.BaseActivity

import app.tapho.utils.*
import com.google.gson.Gson

class VoucherPaymentBaseActivity : BaseActivity<ActivityVoucherPaymentBaseBinding>()  {
    var type: String?=""
    companion object {

        fun openContainerForPayment(
            context: Context?,
            type: String?,
            DeepLink: String?,
            orderID: String?,
            PackageName: String?,
            PaymentType: String?,
        ) {
            context?.startActivity(Intent(context, VoucherPaymentBaseActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("PackageName", PackageName)
                putExtra("DeepLink",DeepLink)
                putExtra("orderID",orderID)
                putExtra("PaymentType",PaymentType)
            })
        }

        fun openContainerforPaymentStatus(
            context: Context?,
            type: String?,
            errorcode: String?,
            txn_Id: String,
            status: String?,
            statusType: String?,
            pspnamedata: String?,
        ) {
            context?.startActivity(Intent(context, VoucherPaymentBaseActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("txn_Id", txn_Id)
                putExtra("errorcode", errorcode)
                putExtra("status", status)
                putExtra("statusType", statusType)
                putExtra("pspnamedata", pspnamedata)
            })
        }


        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,

            ) {
            context?.startActivity(Intent(context, VoucherPaymentBaseActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)

                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoucherPaymentBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //    statusBarTextWhite()

        statusBarColor(R.color.status_bar)
        window.statusBarColor = Color.WHITE

        init()
    }


    private fun init() {
        val type = intent.getStringExtra(CONTAINER_TYPE_KEY)
        when (type) {
            "InitiatePaymentForVoucher" -> {
                addFragment(InitiatePaymentForVoucherFragment.newInstance())
            }

            "VoucherProcessTransaction"->{
                addFragment(VoucherTransactionFragment.newInstance())
            }
            "TransactionStatusPreview"->{
                addFragment(VoucherStatusPreviewFragment.newInstance())
            }
            "TransactionStatusForVoucher"->{
                addFragment(VoucherPaymentStatusFragment.newInstance())
            }
        }
    }



    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

    }

}