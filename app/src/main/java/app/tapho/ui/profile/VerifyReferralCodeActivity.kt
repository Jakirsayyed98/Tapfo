package app.tapho.ui.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import app.tapho.R
import app.tapho.databinding.ActivityVerifyReferralCodeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShort
import app.tapho.showShortSnack
import app.tapho.ui.BaseActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.login.referral_Model.referral_code_res

class VerifyReferralCodeActivity : BaseActivity<ActivityVerifyReferralCodeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyReferralCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        binding.getStarted.isClickable = true
        binding.getStarted.isSelected = true
        binding.getStarted.setOnClickListener {
            if (binding.referralCode.text.toString().isNullOrEmpty()){
              this.showShort("please enter referral code")
            }else{
                getReffrelData(binding.referralCode.text.toString())
            }
        }
        binding.skip.setOnClickListener {

        startActivity(Intent(this@VerifyReferralCodeActivity, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
        })

        }
    }

    private fun getReffrelData(code: String) {
        viewModel.verifyReferralCode(getUserId(),code,this,object :
            ApiListener<referral_code_res, Any?> {
            override fun onSuccess(t: referral_code_res?, mess: String?) {
                t.let {
                    if (it!!.errorCode.equals("0")){
                       Toast.makeText(this@VerifyReferralCodeActivity,it.errorMsg,Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@VerifyReferralCodeActivity, HomeActivity::class.java).apply {
                              flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }else{
                        Toast.makeText(this@VerifyReferralCodeActivity,it.errorMsg,Toast.LENGTH_SHORT).show()
                        View(this@VerifyReferralCodeActivity).showShortSnack(it.errorMsg)

                    }
                }
            }

        })
    }
}