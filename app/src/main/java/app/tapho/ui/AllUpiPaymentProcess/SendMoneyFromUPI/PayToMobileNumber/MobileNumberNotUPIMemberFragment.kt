package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMobileNumberNotUPIMemberBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Adapter.CustomeSendMoneyCategory
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment
import app.tapho.utils.toast


class MobileNumberNotUPIMemberFragment : BaseFragment<FragmentMobileNumberNotUPIMemberBinding>() {

    var Number=""

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
        _binding = FragmentMobileNumberNotUPIMemberBinding.inflate(inflater,container,false)

        Number = activity?.intent?.getStringExtra("UPIID").toString()

        setTextOnScreen()
        payThroughOtherWays()
        return _binding?.root
    }

    private fun setTextOnScreen() {
        _binding!!.apply {
            mobileNumber.text=Number
            invite.text=getString(R.string.invite_them_to_go_cashless,Number)
            dontWorry.text=getString(R.string.don_t_worry_you_can_still_send_money_to,Number)
            invite.setOnClickListener {
                shareApp()
            }
            backIv.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=" + activity?.packageName.toString() + ""
            )
            type = "text/plain"
        }
        try {
            startActivity(Intent.createChooser(sendIntent, null))
        } catch (e: ActivityNotFoundException) {
            context?.toast("Unable to find market app")
        }
    }

    private fun payThroughOtherWays() {
        val sendMoneycategory = CustomeSendMoneyCategory(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                when (data) {

                    "Scan QR to Pay" -> {

                    }
                    "Bank Transfer" -> {
                        UPIContainerActivity.openContainer(requireContext(), "DirectToBankAccount", "", false, "")
                    }
                    "To UPI ID" -> {
                        UPIContainerActivity.openContainer(requireContext(), "Pay to UPI ID", "", false, "")
                    }
                    "Self Transfer" -> {
                        UPIContainerActivity.openContainer(requireContext(), "SelfBankTransfer", "", false, "")
                    }
                }

            }

        }).apply {
            addItem(SendMoneyCustomeCategory("1", "Pay to UPI ID", "Pay to direct Bank Account using UPI ID", R.drawable.upi_icon, "Pay to UPI ID"))
            addItem(SendMoneyCustomeCategory("3", "Self Transfer", "Pay to direct Bank Account using UPI ID", R.drawable.selftransfer_icon, "Self Transfer"))
            addItem(SendMoneyCustomeCategory("4", "Bank Transfer", "Pay to direct Bank Account using UPI ID", R.drawable.bank_transfer_icon, "Bank Transfer"))

        }

        _binding!!.OtherPaymentOption.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sendMoneycategory
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MobileNumberNotUPIMemberFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}