package app.tapho.ui.profile


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.ActivitySetUpProfileBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShort
import app.tapho.ui.BaseActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.login.LoginActivity
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.login.referral_Model.referral_code_res
import app.tapho.utils.LOGIN_DATA
import app.tapho.utils.isValidEmail
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson


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



        binding.havearefercode.setOnClickListener {
            openReferPopupDialog()
        }

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





    private fun openReferPopupDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.referral_id_bottomsheet, null)
        dialog.setCancelable(true)
        val refercode: TextInputEditText = view.findViewById(R.id.refercode)
        val referCodebtn: AppCompatButton = view.findViewById(R.id.referCode_btn)
        refercode.addTextChangedListener {
            if (refercode.text!!.length>=6){
                referCodebtn.isClickable = true
                referCodebtn.isSelected = true
            }else{
                referCodebtn.isClickable = false
                referCodebtn.isSelected = false
            }
        }
        referCodebtn.setOnClickListener {click->

            val code = refercode.text.toString()
            if (code.isNullOrEmpty()){
                this@SetUpProfileActivity.showMess("Please enter a valid Refer code")
            }
            else{
                viewModel.verifyReferralCode(getUserId(),code,this,object : ApiListener<referral_code_res,Any?>{
                    override fun onSuccess(t: referral_code_res?, mess: String?) {
                        t!!.let {
                            binding.havearefercode.visibility = View.VISIBLE
                            if (it.errorCode.equals("0")){
                                binding.errorcodeTV.text ="Refer code applied sucessfully"
                                binding.havearefercode.visibility = View.GONE
                                binding.errorcodeTV.visibility = View.VISIBLE
                                binding.errorcodeTV.setTextColor(Color.parseColor("#008D3A"))
                                binding.havearefercode.isClickable = false
                                startNewModel()
                                dialog.dismiss()
                            }else{
                                binding.errorcodeTV.text ="Please enter the correct referral code! "
                                binding.errorcodeTV.setTextColor(Color.parseColor("#EF5350"))
                                binding.errorcodeTV.visibility = View.VISIBLE
                                refercode.setError("Please enter the correct referral code! ")
                                dialog.dismiss()
                            }
                        }
                    }

                    override fun onError(mess: String?) {
                        super.onError(mess)
                        if (mess.equals("Referral User Blocked")){
                            binding.errorcodeTV.text ="Account has been suspended on this referral code"
                            binding.errorcodeTV.setTextColor(Color.parseColor("#EF5350"))
                            binding.errorcodeTV.visibility = View.VISIBLE
                            refercode.setError("Account has been suspended on this referral code")
                            dialog.dismiss()
                        }else{
                            binding.errorcodeTV.text ="Please enter the correct referral code! "
                            binding.errorcodeTV.setTextColor(Color.parseColor("#EF5350"))
                            binding.errorcodeTV.visibility = View.VISIBLE
                            refercode.setError("Please enter the correct referral code! ")
                            dialog.dismiss()
                        }
                    }
                })
            }
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun startNewModel() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.referal_bounus_layout, null)
        val image : ImageView = view.findViewById(R.id.aapliedicon)

        Glide.with(this).asGif().load(R.drawable.applied_succesfully_icon).into(image)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        },2000)
        dialog.setContentView(view)
        dialog.show()
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

            } else  if (binding.emailEt.text.toString().isValidEmail().not()) {
                binding.emailEt.setError("Please enter your correct Email id!.")

            } else {
                //server work
                // binding.animationView.visibility=View.VISIBLE
                binding.progress.visibility = View.VISIBLE
                viewModel.updateProfile(getUserId(), binding.emailEt.text.toString(), binding.nicknameEt.text.toString(),
                    "", "","", this, object : ApiListener<LoginRes,Any?>{
                        override fun onSuccess(t: LoginRes?, mess: String?) {
                            t.let {

                                if (it!!.errorCode.equals("0")){
                                    binding.progress.visibility = View.GONE
                                    getSharedPreference().saveString(LOGIN_DATA, Gson().toJson(it.data!!.get(0)))
                                    startActivity(Intent(this@SetUpProfileActivity, HomeActivity::class.java))
                                    finish()
                                }else{

                                    binding.errorcodeTVEmail.setText("This "+ it.errorMsg+" "+ it.errorCode)
                                    binding.errorcodeTVEmail.visibility = View.VISIBLE
                                    binding.errorcodeTVEmail.setTextColor(Color.parseColor("#EF5350"))
                                    binding.progress.visibility = View.GONE
                                    binding.emailEt.setError(it.errorMsg)
                                }
                            }
                        }

                        override fun onError(mess: String?) {
                            super.onError(mess)

                            binding.progress.visibility = View.GONE
                            binding.errorcodeTVEmail.visibility = View.VISIBLE
                            binding.errorcodeTVEmail.setText("This "+ mess.toString())
                            binding.errorcodeTVEmail.setTextColor(Color.parseColor("#EF5350"))
                        }

                    })
            }
    }

}
