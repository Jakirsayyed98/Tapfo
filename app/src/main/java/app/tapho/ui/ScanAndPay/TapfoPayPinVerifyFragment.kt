package app.tapho.ui.ScanAndPay

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentTapfoPayPinVerifyBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShort
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.login.model.LoginRes
import app.tapho.utils.withSuffixAmount


class TapfoPayPinVerifyFragment : BaseFragment<FragmentTapfoPayPinVerifyBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTapfoPayPinVerifyBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        getPinAndVerify()

        return _binding?.root
    }

    private fun getPinAndVerify() {
        _binding!!.titledis.text = getString(R.string.enter_your_tapfo_pay_pin, withSuffixAmount(getSharedPreference().getString("WalletPayAmount")))

        _binding!!.pinView.addTextChangedListener {
            if ( _binding!!.pinView.text!!.length==4){
                _binding!!.conformbtn.isClickable=true
                _binding!!.conformbtn.isSelected=true
            }else{
                _binding!!.conformbtn.isClickable=false
                _binding!!.conformbtn.isSelected=false
            }
        }

        _binding!!.conformbtn.setOnClickListener {
            validatefunction()
        }
    }

    private fun validatefunction() {

        _binding!!.pinView.text?.toString()?.let { passcode ->
            if (passcode.length != 4) {
                requireView().showShortSnack("Please enter passcode")
            } else {
                getSharedPreference().getLoginData()?.let { loginData ->
                    viewModel.loginWithPasscode(passcode, loginData.country_code.toString(), loginData.mobile_no.toString(), this, object : ApiListener<LoginRes,Any?>{
                        override fun onSuccess(t: LoginRes?, mess: String?) {
                                t.let {

                                    if (it!!.errorCode.equals("0")){
                                        ScanAndPayPaymentProcessingActivity.openContainer(requireContext(),"ScanAndPayPaymentProcessingActivity","","","app.tapho","")
                                    }else{
                                        requireContext().showShort("Passcode was incorrect")
                                    }

                                }
                        }
                    })
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TapfoPayPinVerifyFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}