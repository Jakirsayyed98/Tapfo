package app.tapho.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.FragmentPasscodeLoginBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.profile.SetUpProfileActivity
import app.tapho.utils.FORGOT_PASSCODE
import app.tapho.utils.LOGIN_DATA
import app.tapho.utils.getUniqueCode
import com.google.gson.Gson


class PasscodeLoginFragment : BaseFragment<FragmentPasscodeLoginBinding>(), ApiListener<LoginRes, Any?> {
    var isSelected=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPasscodeLoginBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.toolbar.tvTitle.text = getString(R.string.welcome_back)
        binding.toolbar.backIv.setOnClickListener {
            //activity?. onBackPressedDispatcher?.onBackPressed()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
        statusBarColor(R.color.white)
        binding.pinView.isPasswordHidden = true
        binding.btnProceed.isSelected=false
        binding.btnProceed.setOnClickListener {
            /*addFragment()*/
            binding.linearError.visibility=View.GONE
            validate()
        }
        setForgotPass()
        setNumber()
        setTerms()
        binding.pinView.addTextChangedListener {
            if (binding.pinView.text!!.length==4){
                binding.btnProceed.isClickable=true
                binding.btnProceed.isSelected=true
            }else{
                binding.btnProceed.isClickable=false
                binding.btnProceed.isSelected=false
            }
        }
    }


    private fun setForgotPass() {
       binding.loginwithOTP.setOnClickListener(View.OnClickListener { loginWithOtp() })
       binding.loginwithOTP1.setOnClickListener(View.OnClickListener { loginWithOtp() })
    }

    private fun setNumber() {
        getSharedPreference().getLoginData()?.let {
          //  binding.numberTv.text = getString(R.string.enter_the_4_digit_2fa, it.mobile_no)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PasscodeLoginFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addFragment(fragment: Fragment) {
        if (activity is VerifyOtpActivity) {
            (activity as VerifyOtpActivity).addFragment(fragment)
//            (activity as VerifyOtpActivity).addFragment(VerifyErrorFragment.newInstance())
        }
    }

    private fun setTerms() {
        val spannablecontent = SpannableString(getString(R.string.passcode_was_incorrect_otp_login))

        with(spannablecontent) {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
              //      loginWithOtp()
                }
            }, 24, length, 0)
        }
        binding.loginwithOTP.movementMethod = LinkMovementMethod.getInstance()
        binding.loginwithOTP.text = spannablecontent
    }

    private fun validate() {
        binding.pinView.text?.toString()?.let { passcode ->
            if (passcode.length != 4) {
                requireView().showShortSnack("Please enter passcode")
            } else {
               // binding.animationView.visibility=View.VISIBLE
                binding.progress.visibility = View.VISIBLE
                getSharedPreference().getLoginData()?.let { loginData ->
                    viewModel.loginWithPasscode(passcode, loginData.country_code, loginData.mobile_no, this, this)
                }
            }
        }
    }

    override fun onSuccess(t: LoginRes?, mess: String?) {
        t?.data?.let {
            if (it.isNotEmpty()) {
                getSharedPreference().saveString(LOGIN_DATA, Gson().toJson(it[0]))
                binding.progress.visibility = View.GONE
                if (it[0].email.isNullOrEmpty()) {
                    startActivity(Intent(context, SetUpProfileActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                } else
                    startActivity(Intent(context, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                    })
            }

        }
    }



    override fun onError(mess: String?) {
        super.onError(mess)
        binding.progress.visibility=View.GONE
        binding.linearError.visibility=View.VISIBLE
        binding.pinView.text=null
       // addFragment(VerifyErrorFragment.newInstance())
    }

    private fun loginWithOtp() {
        binding.progress.visibility = View.VISIBLE
        getSharedPreference().getLoginData()?.let {
            it.mobile_no?.let { it1 ->
                getSharedPreference().saveString("LoginMobileNumber",it1)
                activity?.intent?.putExtra(FORGOT_PASSCODE, true)
                binding.progress.visibility = View.GONE
                addFragment(VerifyOtpFragment.newInstance())

                /*
                viewModel.loginWithOtp(it1, getUniqueCode(), this, object : ApiListener<LoginRes, Any?> {
                        override fun onSuccess(t: LoginRes?, mess: String?) {
                            t?.let {
                                t.data?.let {
                                    if (it.isNotEmpty()) {
                                        getSharedPreference().saveString(LOGIN_DATA, Gson().toJson(it[0]))
                                        activity?.intent?.putExtra(FORGOT_PASSCODE, true)
                                        binding.progress.visibility = View.GONE
                                        addFragment(VerifyOtpFragment.newInstance())
                                    }
                                }
                            }
                        }
                    })
            */
            }
        }
    }
}