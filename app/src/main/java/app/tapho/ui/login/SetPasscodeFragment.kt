package app.tapho.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import app.tapho.R
import app.tapho.databinding.FragmentSetPasscodeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.RequestViewModel
import app.tapho.showShort
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.profile.SetUpProfileActivity
import app.tapho.utils.PASSCODE_SET
import app.tapho.utils.decrypt
import app.tapho.utils.toast

class SetPasscodeFragment : BaseFragment<FragmentSetPasscodeBinding>(), ApiListener<LoginRes, Any?> {

    var isSelected=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetPasscodeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.tvTitle.text = getString(R.string.set_passcode)
        binding.toolbar.backIv.setOnClickListener { activity?. onBackPressedDispatcher?.onBackPressed() }
        binding.btnVerify.setOnClickListener {
           // binding.animationView.visibility=View.VISIBLE
            validate()
        }
        statusBarColor(R.color.white)
        binding.btnVerify.isSelected=false
        binding.passcodeReEnterView.addTextChangedListener {
            if (binding.passcodeReEnterView.text!!.length==4){
                binding.btnVerify.isClickable=true
                binding.btnVerify.isSelected=true
            }else{
                binding.btnVerify.isClickable=false
                binding.btnVerify.isSelected=false
            }
        }
        setPasscode()
    }

    private fun setPasscode() {
        binding.hintTv.visibility = View.GONE
        binding.passcodeReEnterView.visibility = View.GONE
        //binding.keypad.setTextView(binding.passcodeView)
        binding.passcodeView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 4) {
                    binding.hintTv.visibility = View.VISIBLE
                    binding.passcodeReEnterView.visibility = View.VISIBLE
                 //   binding.keypad.setTextView(binding.passcodeReEnterView)
                }
            }
        })
        binding.passcodeReEnterView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    binding.hintTv.visibility = View.GONE
                    binding.passcodeReEnterView.visibility = View.GONE
                   // binding.keypad.setTextView(binding.passcodeView)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SetPasscodeFragment()
    }

    private fun addFragment() {
        if (activity is VerifyOtpActivity) {
            (activity as VerifyOtpActivity).addFragment(PasscodeLoginFragment.newInstance())
        }
    }

    private fun validate() {
        try {
            when {
                binding.passcodeView.text?.length!! < 4 -> {
      //              binding.animationView.visibility=View.GONE
                    requireView().showShortSnack("Please enter passcode.")
                }
                binding.passcodeReEnterView.text?.length!! < 4 -> {
          //          binding.animationView.visibility=View.GONE
                    requireView().showShortSnack("Please renter passcode.")
                }
                binding.passcodeView.text?.toString()!! != binding.passcodeReEnterView.text.toString() -> {
            //        binding.animationView.visibility=View.GONE
                    requireView().showShortSnack("Passcode does not match.")
                }
                else -> {
                    binding.progress.visibility = View.VISIBLE
                    getSharedPreference().getLoginData()?.id?.let {
                       viewModel.setPasscode(it, binding.passcodeView.text.toString(), this, object : ApiListener<LoginRes,Any?>{
                                override fun onSuccess(t: LoginRes?, mess: String?) {
                                    getSharedPreference().saveBoolean(PASSCODE_SET, true)
                                    // binding.animationView.visibility=View.GONE
                                    getSharedPreference().getLoginData()?.let {
                                        if (it.email.isNullOrEmpty()) {
                                            binding.progress.visibility = View.GONE
                                            startActivity(Intent(context, SetUpProfileActivity::class.java).apply {
                                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                                            })
                                        } else{
                                            binding.progress.visibility = View.GONE
                                            startActivity(Intent(context, HomeActivity::class.java).apply {
                                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                                            })
                                        }

                                    }
                                }
                           override fun onError(mess: String?) {
                               super.onError(mess)

                           }

                            })

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSuccess(t: LoginRes?, mess: String?) {
        //showMess(mess)

        getSharedPreference().saveBoolean(PASSCODE_SET, true)
       // binding.animationView.visibility=View.GONE
        getSharedPreference().getLoginData()?.let {
            if (it.email.isNullOrEmpty()) {
                binding.progress.visibility = View.GONE
                startActivity(Intent(context, SetUpProfileActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                })
            } else{
                binding.progress.visibility = View.GONE
                startActivity(Intent(context, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }

        }
    }

    override fun onError(mess: String?) {
        super.onError(mess)

    }
}