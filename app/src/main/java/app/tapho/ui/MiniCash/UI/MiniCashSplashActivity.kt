package app.tapho.ui.MiniCash.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import app.tapho.R
import app.tapho.databinding.ActivityMiniCashSplashBinding
import app.tapho.databinding.ActivitySplashBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.MiniCash.Fragments.MiniCashActivity


class MiniCashSplashActivity : BaseActivity<ActivityMiniCashSplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        belowStatusBar()
        binding = ActivityMiniCashSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.orange1)
        statusBarTextblack()
        setScreenTimeHandler()
    }

    private fun setScreenTimeHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MiniCashActivity::class.java))
            finish()
        }, 1000)
    }
}