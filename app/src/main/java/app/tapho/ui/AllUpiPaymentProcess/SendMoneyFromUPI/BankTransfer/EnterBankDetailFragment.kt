package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import app.tapho.R
import app.tapho.databinding.FragmentEnterBankDetailBinding
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment
import app.tapho.utils.DATA
import app.tapho.utils.TITLE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class EnterBankDetailFragment : BaseFragment<FragmentEnterBankDetailBinding>() {
    var isSelected=false
    var bankName =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEnterBankDetailBinding.inflate(inflater,container,false)
        bankName = activity?.intent?.getStringExtra(TITLE)  .toString()

        settextAndClickEvent()
        return _binding?.root
    }

    private fun settextAndClickEvent() {
        _binding!!.proceedBtn.isSelected=false
        if (_binding!!.AccountNumber.text.toString().isNullOrEmpty()){
            _binding!!.proceedBtn.isClickable=false
            _binding!!.proceedBtn.isSelected=false
        }else{
            _binding!!.proceedBtn.isClickable=true
            _binding!!.proceedBtn.isSelected=true
        }

        if (_binding!!.ReBankAccountNumber.text.toString().isNullOrEmpty()){
            _binding!!.proceedBtn.isClickable=false
            _binding!!.proceedBtn.isSelected=false
        }else{
            _binding!!.proceedBtn.isClickable=true
            _binding!!.proceedBtn.isSelected=true
        }

        if (_binding!!.IFSCCODE.text.toString().isNullOrEmpty()){
            _binding!!.proceedBtn.isClickable=false
            _binding!!.proceedBtn.isSelected=false
        }else{
            _binding!!.proceedBtn.isClickable=true
            _binding!!.proceedBtn.isSelected=true
        }
        if (_binding!!.branchName.text.toString().isNullOrEmpty()){
            _binding!!.proceedBtn.isClickable=false
            _binding!!.proceedBtn.isSelected=false
        }else{
            _binding!!.proceedBtn.isClickable=true
            _binding!!.proceedBtn.isSelected=true
        }
        if (getSharedPreference().getLoginData()?.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = bankName

        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(getSharedPreference().getLoginData()?.image).apply(
                RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
            ).into(_binding!!.profileIv)
        }

        _binding!!.apply {
            findIFSCCode.setOnClickListener {
                UPIContainerActivity.openContainer(requireContext(),"FindIFSCCODE","",false,"",_binding!!.AccountNumber.text.toString())
            }
            ReBankAccountNumber.doAfterTextChanged {
                if (!AccountNumber.text.toString().equals(ReBankAccountNumber.text.toString())){
                    ReBankAccountNumber.setError("Account Number And Re- Account Number Should be ")
                }
            }

            IFSCCODE.setText(FindIFSCCodeFragment.ifscCode)

            title.text= getString(R.string.send_money_to_000_bank_account,bankName)

            proceedBtn.setOnClickListener {
                UPIContainerActivity.openContainer(requireContext(),"GoForUPIPayment","",false,"",_binding!!.AccountNumber.text.toString())
            }
        }
    }

    companion object {
      
        @JvmStatic
        fun newInstance() =
            EnterBankDetailFragment().apply {
                arguments = Bundle().apply {
                   
                }
            }
    }
}