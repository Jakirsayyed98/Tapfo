package app.tapho.ui.activecashback.ActiveCashbackScreenNew

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityActiveCashbackBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.activecashback.OrdersFragment
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.*

class ActiveCashbackActivity : BaseActivity<ActivityActiveCashbackBinding>(){
    private var res: WebTCashRes? = null
    companion object{
        fun openWebView(
            context: Context?,
            webViewUrl: String?,
            miniAppId: String?,

        ) {
            context?.startActivity(Intent(context, ActiveCashbackActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra(MINI_APP_ID, miniAppId)

            })
        }


        fun openWebView(
            context: Context?,
            webViewUrl: String?,
            miniAppId: String?,
            iconUrl: String?,
            tCashType: String?,
            favourite: String?,
            cashback: String?,
            appcategoryid: String?,
        ) {
            context?.startActivity(Intent(context, ActiveCashbackActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra(MINI_APP_ID, miniAppId)
                putExtra(ICON_URL, iconUrl)
                putExtra(TCASH_TYPE, tCashType)
                putExtra(IS_FAVOURITE, favourite)
                putExtra(CASHBACK, cashback)
                putExtra(APPCATEGORYID, appcategoryid)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_cashback)
        window.statusBarColor=Color.WHITE
        statusBarTextWhite()
        viewModel.getMiniAppTCash(AppSharedPreference.getInstance(this).getUserId(),
            intent?.getStringExtra(MINI_APP_ID), this,
            object : ApiListener<WebTCashRes, Any?> {
                override fun onSuccess(t: WebTCashRes?, mess: String?) {
                    t?.let {
                        res=it
                    }
                }
            })
      //  addFragment(ActiveCashbackFragment.newInstance())
        addFragment(CashbackOrderFragment.newInstance(res,true))
    }
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.viewpagerActiveCashback, fragment).commit()
    }
}