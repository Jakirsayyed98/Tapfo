package app.tapho.ui.AllUpiPaymentProcess

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityUpicontainerBinding
import app.tapho.ui.AllUpiPaymentProcess.RecivedMoneyFromUPI.GetMoneyFromOtherFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.EnterBankDetailFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.FindIFSCCodeFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.SearchBankAccountFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.TransferThroughBankToBankFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber.MobileNumberNotUPIMemberFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber.PayThroughMobileNumberFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Pay_through_UPI_ID_Fragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber.SerachNumberAndShowFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.SelfTransfer.SelfBankTransferFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.SendMoneyFragment
import app.tapho.ui.BaseActivity
import app.tapho.ui.home.*
import app.tapho.ui.merchants.*
import app.tapho.ui.tcash.*
import app.tapho.utils.*
import com.google.gson.Gson

class UPIContainerActivity :BaseActivity<ActivityUpicontainerBinding>() {


    companion object {

        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, UPIContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }
        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
            UPIID: String?,
        ) {
            context?.startActivity(Intent(context, UPIContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                putExtra("UPIID", UPIID)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpicontainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarTextWhite()
        statusBarColor(R.color.status_bar)
        window.statusBarColor = Color.WHITE
        binding.toolbar.backIv.setOnClickListener { onBackPressed() }
        init()
    }

    private fun init() {
        val type = intent.getStringExtra(CONTAINER_TYPE_KEY)
        when (type) {
            "Send Money" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SendMoneyFragment.newInstance())
            }
            "RecivedMoney" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(GetMoneyFromOtherFragment.newInstance())
            }
            "GoForUPIPayment" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(UPIPaymentScreenFragment.newInstance())
            }

            "Pay to UPI ID" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(Pay_through_UPI_ID_Fragment.newInstance())
            }
            "PayThroughMobileNumberFragment" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(PayThroughMobileNumberFragment.newInstance())
            }
            "NumberNotAvailInUPIID" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(MobileNumberNotUPIMemberFragment.newInstance())
            }
            "NumberAvailInUPIIDSearchPage" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SerachNumberAndShowFragment.newInstance())
            }
            "DirectToBankAccount" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(TransferThroughBankToBankFragment.newInstance())
            }
            "SearchBankAccount" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SearchBankAccountFragment.newInstance())
            }
            "EnterBankDetail" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(EnterBankDetailFragment.newInstance())
            }
            "FindIFSCCODE" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(FindIFSCCodeFragment.newInstance())
            }
            "SelfBankTransfer" -> {
                setTitlec(intent.getStringExtra(TITLE))
                addFragment(SelfBankTransferFragment.newInstance())
            }
        }
    }

    private fun setCashbackMerchants() {
        addFragment(CashbackMerchantsFragment.newInstance(null, true, false))
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    fun setTitlec(title: String?) {
        binding.toolbar.tvTitle.text = title
    }
}