package app.tapho.ui.localbizzUI

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import app.tapho.databinding.ActivityLocalBizSplashBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.localbizzUI.LocalbizContainerActivity

class LocalBizSplashActivity : BaseActivity<ActivityLocalBizSplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        belowStatusBar()
        binding = ActivityLocalBizSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setScreenTimeHandler()
    }

    private fun setScreenTimeHandler() {
        Handler(Looper.getMainLooper()).postDelayed({

            LocalbizContainerActivity.openContainer(this,"HomePage")
            finish()

        }, 1500)
    }
}