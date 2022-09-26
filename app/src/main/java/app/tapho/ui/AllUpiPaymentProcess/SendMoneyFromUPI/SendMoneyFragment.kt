package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSendMoneyBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Adapter.CustomeSendMoneyCategory
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity

class SendMoneyFragment : BaseFragment<FragmentSendMoneyBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendMoneyBinding = FragmentSendMoneyBinding.inflate(inflater, container, false)

        setSendMoneyButtons()
        sendMoneyBinding!!.backIv.setOnClickListener {
            activity?.onBackPressed()
        }

        return sendMoneyBinding?.root
    }

    private fun setSendMoneyButtons() {
        val sendMoneycategory = CustomeSendMoneyCategory(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                when (data) {
                    "Pay to UPI ID" -> {
                        UPIContainerActivity.openContainer(requireContext(), "Pay to UPI ID", "", false, "")
                    }
                    "Enter Mobile Number" -> {
                        UPIContainerActivity.openContainer(requireContext(), "PayThroughMobileNumberFragment", "", false, "")
                    }
                    "Self Transfer" -> {
                        UPIContainerActivity.openContainer(requireContext(), "SelfBankTransfer", "", false, "")
                    }
                    "Bank Transfer" -> {
                        UPIContainerActivity.openContainer(requireContext(), "DirectToBankAccount", "", false, "")
                    }
                }

            }

        }).apply {
            addItem(
                SendMoneyCustomeCategory(
                    "1",
                    "Pay to UPI ID",
                    "Pay to direct Bank Account using UPI ID",
                    R.drawable.upi_icon,
                    "Pay to UPI ID"
                )
            )
            addItem(
                SendMoneyCustomeCategory(
                    "2",
                    "Enter Mobile Number",
                    "Pay to direct Bank Account using Mobile Number",
                    R.drawable.mobile_number_upi,
                    "Enter Mobile Number"
                )
            )
            addItem(
                SendMoneyCustomeCategory(
                    "3",
                    "Self Transfer",
                    "Pay to direct Bank Account using UPI ID",
                    R.drawable.selftransfer_icon,
                    "Self Transfer"
                )
            )
            addItem(
                SendMoneyCustomeCategory(
                    "4",
                    "Bank Transfer",
                    "Pay to direct Bank Account using UPI ID",
                    R.drawable.bank_transfer_icon,
                    "Bank Transfer"
                )
            )
        }

        sendMoneyBinding?.senddMoneyRV?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sendMoneycategory
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SendMoneyFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}