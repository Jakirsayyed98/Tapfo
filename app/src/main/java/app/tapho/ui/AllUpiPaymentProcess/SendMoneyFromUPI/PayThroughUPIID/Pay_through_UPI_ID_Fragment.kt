package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentPayThroughUPIIDBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Adapter.CustomeSendMoneyCategory
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Adapter.CustomeUPIEndPointsAdapter
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.CustomeUPIEndpoints
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment


class Pay_through_UPI_ID_Fragment : BaseFragment<FragmentPayThroughUPIIDBinding>() {
    var isSelected=false
    var UPIidText =""
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
        _binding = FragmentPayThroughUPIIDBinding.inflate(inflater, container, false)


        SetAndEditTextData()

        CustomeUPIEndpointsRV()
        payThroughOtherWays()

        return _binding?.root
    }

    private fun SetAndEditTextData() {
        _binding!!.proceedBtn.isSelected=false
        _binding!!.UPIid.addTextChangedListener {
            if (_binding!!.UPIid.text.toString().contains("@")){
                _binding!!.hintForUPIID.visibility = View.GONE
                _binding!!.afterGettingError.visibility = View.GONE
                _binding!!.extentions.visibility = View.VISIBLE
                _binding!!.proceedBtn.isClickable=true
                _binding!!.proceedBtn.isSelected=true
            }
            if (_binding!!.UPIid.text.toString().isNullOrEmpty()){
                _binding!!.hintForUPIID.visibility = View.VISIBLE
                _binding!!.extentions.visibility = View.GONE
                _binding!!.afterGettingError.visibility = View.GONE
                _binding!!.proceedBtn.isClickable=false
                _binding!!.proceedBtn.isSelected=false
            }


        }

        _binding!!.proceedBtn.setOnClickListener {
            if (_binding!!.UPIid.text.toString().contains("@")){
                UPIContainerActivity.openContainer(requireContext(),"GoForUPIPayment","",false,"",_binding!!.UPIid.text.toString())
            }else{
                _binding!!.afterGettingError.visibility = View.VISIBLE

            }
        }
    }

    private fun CustomeUPIEndpointsRV() {
        val CustomeUPIEndPointsAdapter = CustomeUPIEndPointsAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                UPIidText.removeRange(0,UPIidText.toString().length)
                UPIidText = _binding!!.UPIid.text.toString()
                _binding!!.UPIid.text!!.clear()
                _binding!!.UPIid.setText(UPIidText.replaceAfter("@","").replace("@","") +data.toString())
            }

        }).apply {

            addItem(CustomeUPIEndpoints("1", "@okaxis", "@okaxis"))
            addItem(CustomeUPIEndpoints("2", "@okicici", "@okicici"))
            addItem(CustomeUPIEndpoints("3", "@oksbi", "@oksbi"))
            addItem(CustomeUPIEndpoints("4", "@okkotak", "@okkotak"))
            addItem(CustomeUPIEndpoints("1", "@okaxis", "@okaxis"))
            addItem(CustomeUPIEndpoints("2", "@okicici", "@okicici"))
            addItem(CustomeUPIEndpoints("3", "@oksbi", "@oksbi"))
            addItem(CustomeUPIEndpoints("4", "@okkotak", "@okkotak"))

        }

        _binding!!.UPIExtentions.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CustomeUPIEndPointsAdapter
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
                    "To Mobile Numberr" -> {
                        UPIContainerActivity.openContainer(requireContext(), "PayThroughMobileNumberFragment", "", false, "")
                    }
                    "Self Transfer" -> {
                        UPIContainerActivity.openContainer(requireContext(), "SelfBankTransfer", "", false, "")
                    }
                }

            }

        }).apply {
            addItem(SendMoneyCustomeCategory("2", "Enter Mobile Number", "Pay to direct Bank Account using Mobile Number", R.drawable.mobile_number_upi, "Enter Mobile Number"))
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
            Pay_through_UPI_ID_Fragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}