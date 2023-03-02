package app.tapho.ui.ScanAndPay

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityScanAndPayContainerBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.*
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.DATA
import app.tapho.utils.SET_OLD_DATA
import app.tapho.utils.TITLE
import com.google.gson.Gson

class ScanAndPayContainerActivity : BaseActivity<ActivityScanAndPayContainerBinding>() {

    companion object{
        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            Amount: String?,
            Notes : String?
        ) {
            context?.startActivity(Intent(context, ScanAndPayContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra("Amount", Amount)
                putExtra("Notes", Notes)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanAndPayContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.status_bar)
        window.statusBarColor = Color.WHITE
        init()

    }


    private fun init() {
        val type = intent.getStringExtra(CONTAINER_TYPE_KEY)
        when (type) {
            "EnterAmountForSend" -> {
                addFragment(GetAmountDataFragment.newInstance())
            }
            "TapfoPayPinVerifyFragment" -> {
                addFragment(TapfoPayPinVerifyFragment.newInstance())
            }
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

    }

}