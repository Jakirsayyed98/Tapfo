package app.tapho.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.TypefaceSpan
import androidx.core.content.res.ResourcesCompat
import app.tapho.BuildConfig
import app.tapho.R
import app.tapho.SpannableStringS1
import app.tapho.databinding.ActivitySplashBinding
import app.tapho.ui.games.GamesHomeActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.intro.IntroActivity
import app.tapho.ui.login.VerifyOtpActivity
import app.tapho.utils.REACHED_HOME
import app.tapho.utils.VERIFY_OTP

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        belowStatusBar()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.orange1)

        val verifyOtp = getSharedPreference().getBoolean(VERIFY_OTP)
        if (verifyOtp && getSharedPreference().getBoolean(REACHED_HOME).not())
            getSharedPreference().clear()



        Handler(Looper.getMainLooper()).postDelayed({
            val loginData = getSharedPreference().getLoginData()
            if (loginData == null) {
                startActivity(Intent(this, IntroActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            } else {
                if (getSharedPreference().getBoolean(REACHED_HOME)) {
                    startActivity(Intent(this,HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                    finish()
                } else
                    startActivity(Intent(this, VerifyOtpActivity::class.java))

            }
        }, 2000)

    }

}