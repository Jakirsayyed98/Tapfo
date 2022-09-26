package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentTransferThroughBankToBankBinding
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment


class TransferThroughBankToBankFragment : BaseFragment<FragmentTransferThroughBankToBankBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransferThroughBankToBankBinding.inflate(inflater,container,false)
        setAndClickEvent()
        return _binding?.root
    }

    private fun setAndClickEvent() {
        _binding!!.apply {
            outlinedTextField.setOnClickListener {
                UPIContainerActivity.openContainer(requireContext(), "SearchBankAccount", "", false, "")
            }
            bankname.setOnClickListener {
                UPIContainerActivity.openContainer(requireContext(), "SearchBankAccount", "", false, "")
            }
            SearchBank.setOnClickListener {
                UPIContainerActivity.openContainer(requireContext(), "SearchBankAccount", "", false, "")
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TransferThroughBankToBankFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}