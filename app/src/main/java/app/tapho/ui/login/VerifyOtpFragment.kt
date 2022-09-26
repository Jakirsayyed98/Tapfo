package app.tapho.ui.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import app.tapho.R
import app.tapho.ServiceData.SmsBroadCastReciver
import app.tapho.databinding.FragmentVerifyOtpBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.login.model.LoginData
import app.tapho.ui.login.model.LoginRes
import app.tapho.utils.FORGOT_PASSCODE
import app.tapho.utils.LOGIN_DATA
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.gson.Gson
import java.util.regex.Matcher
import java.util.regex.Pattern

class VerifyOtpFragment : BaseFragment<FragmentVerifyOtpBinding>(), ApiListener<LoginRes, Any?> {
    private var loginData: LoginData? = null
    private var timer: CountDownTimer? = null
    var isSelected=false


    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadCastReciver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)

        startSmsUserConsent()
        return _binding?.root
    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(requireActivity())
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(object : OnSuccessListener<Void?> {
            override fun onSuccess(p0: Void?) {
                Toast.makeText(requireContext(), "On Success", Toast.LENGTH_LONG).show()
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                Toast.makeText(requireContext(), "On OnFailure", Toast.LENGTH_LONG).show()
            }
        })
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMessage(message)
            }
        }
    }

    private fun getOtpFromMessage(message: String?) {
        // This will match any 4 digit number in the message
        val pattern = Pattern.compile("(|^)\\d{4}")
        val matcher: Matcher = pattern.matcher(message)
        if (matcher.find()) {
            binding.pinView.setText(matcher.group(0))
            Log.d("MyOTPData2",matcher.group(0).toString())

        }
    }


    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadCastReciver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadCastReciver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
                }

                override fun onFailure() {

                }

            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver,intentFilter)

    }


    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(smsBroadcastReceiver)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData = getSharedPreference().getLoginData()

        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)
//        binding.toolbar.tvTitle.text = getString(R.string.verify_number)
        binding.toolbar.backIv.setOnClickListener {
            //activity?.onBackPressed()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
        //binding.pinView.isPasswordHidden = true
        //binding.keypad.setTextView(binding.pinView)
        binding.btnProceed.isSelected=false
        binding.btnProceed.setOnClickListener {
       //     binding.animationView.visibility=View.VISIBLE
            verifyOtp()
        }
      //  setResendbtn()

        binding.pinView.addTextChangedListener {
            if (binding.pinView.text!!.length==4){
                binding.btnProceed.isSelected=true
                binding.btnProceed.isClickable=true
            }else{
                binding.btnProceed.isClickable=false
                binding.btnProceed.isSelected=false
            }
        }
        setNumber()
        setTimer()
    }

    private fun setResendbtn() {
      //  binding.animationView.visibility=View.GONE
            binding.resendBtn.visibility=View.VISIBLE
            binding.resendBtn.setOnClickListener( View.OnClickListener {
                resendOtp()
           //     binding.animationView.visibility=View.GONE
                binding.resendBtn.visibility=View.INVISIBLE
            })
    }

    private fun setNumber() {

        val spannablecontent = SpannableString("change")

        with(spannablecontent) {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    startActivity(Intent(requireContext(),LoginActivity::class.java))
                }
            }, 0, length, 0)
        }
        binding.numberTv1.movementMethod = LinkMovementMethod.getInstance()
        val spannable = SpannableString("${loginData?.mobile_no} ").apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    activity?.onBackPressed ()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    //Jakir 10/02/2022
                    ds.color = resources.getColor(R.color.transparent)
                    //10/02/2022
                }
            }, 11, length, 0)
        }
        binding.numberTv1.movementMethod = LinkMovementMethod.getInstance()
        val dataText:String="we have sent you 4 digit \n OTP to "

        //binding.numberTv.setText("we have sent you \n4 digit OTP to "+spannable)
        binding.numberTv1.text = dataText+spannable+" "+spannablecontent
    }

    private fun setTimer() {
        val reciviedOTPData:String="Didn't receive OTP? 00:"
        timer = object : CountDownTimer(59000, 1000) {
            override fun onTick(p0: Long) {
                binding.otpTimerTv.text = (p0 / 1000).toString()
            }
            override fun onFinish() {
                setResendbtn()
             //   binding.animationView.visibility=View.INVISIBLE
            }
        }
        timer?.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VerifyOtpFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        _binding = null
    }

    private fun addFragment() {
//        if (activity?.intent?.getBooleanExtra(FORGOT_PASSCODE, false) == true) {
//         //   binding.animationView.visibility=View.GONE
//            startActivity(Intent(context, HomeActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
//            })
//        } else {
            if (activity is VerifyOtpActivity) {
           //     binding.animationView.visibility=View.GONE
                (activity as VerifyOtpActivity).addFragment(SetPasscodeFragment.newInstance())
            }
//        }
    }

    private fun resendOtp() {
        timer?.cancel()
        loginData?.mobile_no?.let {
            viewModel.resendOtp(it, this, object : ApiListener<LoginRes, Any?> {
                override fun onSuccess(t: LoginRes?, mess: String?) {
                    showMess(mess)
                    setTimer()
                }
            })
        }
    }

    private fun verifyOtp() {
        if (binding.pinView.text?.length ?: false != 4) {
            showMess("Please enter 4 digit OTP.")
            return
        }
        loginData?.id?.let {
            viewModel.verifyOtp(it, binding.pinView.text.toString(), this, this)
        }
    }

    override fun onSuccess(t: LoginRes?, mess: String?) {
        t?.let {
            t.data?.let {
                if (it.isNotEmpty()) {
             //       binding.animationView.visibility=View.INVISIBLE
                    getSharedPreference().saveString(LOGIN_DATA, Gson().toJson(it[0]))
                    addFragment()
                }
            }
        }
    }
}