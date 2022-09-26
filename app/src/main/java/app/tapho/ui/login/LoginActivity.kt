package app.tapho.ui.login

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.graphics.toColor
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.ActivityLoginBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.network.RequestViewModel
import app.tapho.ui.BaseActivity
import app.tapho.ui.LoaderFragment
import app.tapho.ui.PrivacyPolicyActivity
import app.tapho.ui.intro.IntroActivity
import app.tapho.ui.login.model.LoginData
import app.tapho.ui.login.model.LoginRes
import app.tapho.utils.*
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.gson.Gson


class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoaderListener, ApiListener<LoginRes, Any?> {
    val Credintial_Request=100;
    var isSelected=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTextWhite()
        statusBarColor(R.color.status_bar)
        window.statusBarColor=Color.WHITE
        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnVerify.isSelected=false
        binding.MobileNumber.addTextChangedListener {
            if (binding.MobileNumber.text!!.length == 10) {
                binding.btnVerify.isClickable=true
                binding.btnVerify.isSelected=true
            }else{
                binding.btnVerify.isClickable=false
                binding.btnVerify.isSelected=false
            }
        }

//        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
//            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        }

        //mobile Number Hint Code start
        getPhoneNumber()

        //binding.keypad.setTextView(binding.MobileNumber)
        binding.btnVerify.setOnClickListener {
           // binding.animationView.visibility=View.VISIBLE
            binding.progress.visibility = View.VISIBLE
            callApi()

        }
        setTerms()
    }

    private fun getPhoneNumber() {
        //mobile Number Hint Code start
        val hintRequest=HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val option = CredentialsOptions.Builder()
            .forceEnableSaveDialog().build()
        val crential_mobile=Credentials.getClient(applicationContext,option)

        val intent=crential_mobile.getHintPickerIntent(hintRequest)
        startIntentSenderForResult(intent.intentSender,Credintial_Request,null,0,0,0,Bundle())
//Mobile Hint Code End
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Credintial_Request && resultCode == RESULT_OK){
            val cred :Credential?=data?.getParcelableExtra(Credential.EXTRA_KEY)
            cred.apply { cred!!.id }

            val data1=cred!!.id

            if (data1.contains("+91")){
                val number=data1.removeRange(0,3)
                binding.MobileNumber.setText(number)
            }else
            {
                binding.MobileNumber.setText(data1)
            }
        }else{
            Toast.makeText(applicationContext,"No Mobile Number Found",Toast.LENGTH_SHORT).show()
        }
    }


    private fun setTerms() {
        val spannablecontent = SpannableString(getString(R.string.by_proceeding_you_are))

        with(spannablecontent) {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    openPrivacy("2")
                }
            }, 39, 55, 0)
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    openPrivacy("1")
                }
            }, 58, length, 0)
        }
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTerms.text = spannablecontent
    }


    private fun callApi() {
        if (binding.MobileNumber.length() < 10) {
            showMess("Please enter 10 mobile number.")
            return
        }
        if (intent.getBooleanExtra(FORGOT_PASSCODE, false))
            loginWithOtp()
        else

            viewModel.loginCheckPasscode(
                binding.MobileNumber.text.toString(), "check_passcode", this, object : ApiListener<BaseRes, Any?> {
                    override fun onSuccess(t: BaseRes?, mess: String?) {
                        isPasscodeSet()
                        binding.progress.visibility = View.GONE
                     //   binding.animationView.visibility=View.INVISIBLE
                    }

                    override fun onError(mess: String?) {
                        super.onError(mess)
                        loginWithOtp()
                     //   binding.animationView.visibility=View.INVISIBLE
                    }
                })
    }

    private fun loginWithOtp() {
        viewModel.loginWithOtp(binding.MobileNumber.text.toString(), getUniqueCode(), this@LoginActivity, this@LoginActivity)
    }

    private fun isPasscodeSet() {
        getSharedPreference().apply {
            saveBoolean(PASSCODE_SET, true)
            saveString(LOGIN_DATA, Gson().toJson(LoginData(binding.MobileNumber.text.toString())))
        }
        startActivity(Intent(this@LoginActivity, VerifyOtpActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    override fun showLoader() {
        LoaderFragment.showLoader(supportFragmentManager)
    }

    override fun dismissLoader() {
        LoaderFragment.dismissLoader(supportFragmentManager)
    }

    override fun showMess(mess: String?) {
        toast(mess)
    }

    override fun onSuccess(t: LoginRes?, mess: String?) {
        t?.let {
            if (it.data.isNullOrEmpty().not()) {
                binding.progress.visibility = View.GONE
                getSharedPreference().saveString(LOGIN_DATA, Gson().toJson(it.data?.get(0)))
                getSharedPreference().saveBoolean(VERIFY_OTP, true)
                startActivity(Intent(this, VerifyOtpActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(FORGOT_PASSCODE, intent.getBooleanExtra(FORGOT_PASSCODE, false))
                })
            }
        }
    }

    private fun openPrivacy(type: String) {
        startActivity(Intent(this, PrivacyPolicyActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("TYPE", type)
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this,IntroActivity::class.java))
        super.onBackPressed()
    }
}