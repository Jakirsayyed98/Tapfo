package app.tapho.ui.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.ActivitySetUpProfileBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShort
import app.tapho.showShortSnack
import app.tapho.ui.BaseActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.login.LoginActivity
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.login.referral_Model.referral_code_res
import app.tapho.utils.LOGIN_DATA

import app.tapho.utils.isValidEmail
import com.google.android.material.snackbar.Snackbar

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class SetUpProfileActivity : BaseActivity<ActivitySetUpProfileBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nicknameEt.requestFocus()
        statusBarColor(R.color.white)
        statusBarTextWhite()

        statusBarColor(R.color.status_bar)
        window.statusBarColor = Color.WHITE
//        binding.toolbar.backIv.setOnClickListener {
//            onBackPressed()
//        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@SetUpProfileActivity,LoginActivity::class.java))
                finish()
            }

        })

        if (binding.nicknameEt.text!!.isEmpty()) {
            binding.btnSetProfile.isSelected=false
            binding.btnSetProfile.isClickable = false
        } else if (binding.emailEt.text!!.isBlank()) {
            binding.btnSetProfile.isSelected=false
            binding.btnSetProfile.isClickable = true
        } else  if (binding.emailEt.text.toString().isValidEmail().not()) {
            binding.btnSetProfile.isSelected=false
            binding.btnSetProfile.isClickable = false
        } else{
            binding.btnSetProfile.isSelected=true
            binding.btnSetProfile.isClickable = true
        }

        init()
    }

    private fun init() {
        binding.emailEt.addTextChangedListener {
            if (binding.emailEt.text.toString().isValidEmail()){
                binding.btnSetProfile.isSelected = true
                binding.btnSetProfile.isClickable = true
            }else{
                binding.btnSetProfile.isClickable = false
                binding.btnSetProfile.isSelected = false
            }
        }
        binding.informationpage.visibility = View.VISIBLE

        binding.btnSetProfile.setOnClickListener {
            setProfile()
        }
    }

    private fun setProfile() {
        if (binding.nicknameEt.text!!.isEmpty()) {
            binding.nicknameEt.setError("Please enter your name.")
        } else if (binding.emailEt.text!!.isBlank()) {
                binding.emailEt.setError("Please enter your Email id!.")
                Toast.makeText(applicationContext, "Enter Your Email ID", Toast.LENGTH_SHORT).show()
            } else  if (binding.emailEt.text.toString().isValidEmail().not()) {
                binding.emailEt.setError("Please enter your correct Email id!.")
                showMess("Please enter valid email address.")
            } else {
                //server work
                // binding.animationView.visibility=View.VISIBLE
                viewModel.updateProfile(getUserId(), binding.emailEt.text.toString(), binding.nicknameEt.text.toString(),
                    "", "","", this, object : ApiListener<LoginRes,Any?>{
                        override fun onSuccess(t: LoginRes?, mess: String?) {
                            Toast.makeText(this@SetUpProfileActivity,"0 "+t!!.errorMsg,Toast.LENGTH_LONG).show()
                          if (t.errorCode=="0"){
                              getSharedPreference().saveString(LOGIN_DATA, Gson().toJson(t.data!!.get(0)))

                              startActivity(Intent(this@SetUpProfileActivity, VerifyReferralCodeActivity::class.java).apply {
                                  //  flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                              })
                          }else{
                              Toast.makeText(this@SetUpProfileActivity,"1 "+t.errorMsg,Toast.LENGTH_LONG).show()
                          }
                        }
                    })
            }
    }

}
