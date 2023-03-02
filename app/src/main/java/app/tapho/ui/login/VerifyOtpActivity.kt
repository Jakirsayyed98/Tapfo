package app.tapho.ui.login

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityVerifyOtpBinding
import app.tapho.ui.BaseActivity
import app.tapho.utils.PASSCODE_SET
import app.tapho.utils.VERIFY_OTP

class VerifyOtpActivity : BaseActivity<ActivityVerifyOtpBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTextWhite()
        statusBarColor(R.color.status_bar)
        window.statusBarColor=Color.WHITE
        setContentView(R.layout.activity_verify_otp)

        val loginData = getSharedPreference().getLoginData()
        val isPassCode = getSharedPreference().getBoolean(PASSCODE_SET)
        val verifyOtp = getSharedPreference().getBoolean(VERIFY_OTP)

        if (verifyOtp) {
            addFragment(VerifyOtpFragment.newInstance())
        } else {
            if (isPassCode)
                addFragment(PasscodeLoginFragment.newInstance())
            else
                addFragment(SetPasscodeFragment.newInstance())
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else
            super.onBackPressed()
//        onBackPressedDispatcher.onBackPressed()
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.container, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }
}