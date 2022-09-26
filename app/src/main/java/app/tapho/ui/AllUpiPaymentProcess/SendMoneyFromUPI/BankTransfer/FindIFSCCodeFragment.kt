package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentFindIFSCCodeBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.Adapter.CustomeSearchBankAdapter
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import app.tapho.ui.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialog


class FindIFSCCodeFragment : BaseFragment<FragmentFindIFSCCodeBinding>() {


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
        _binding = FragmentFindIFSCCodeBinding.inflate(inflater,container,false)
        setTextAndClickEvent()
        return _binding?.root
    }

    private fun setTextAndClickEvent() {
        _binding!!.apply {
            selectBank.setOnClickListener {
                SelectBankBottomSheet()
            }
            proceedBtn.setOnClickListener {
                ifscCode = selectBank.text.toString()
                activity?.onBackPressed()
            }
        }
    }

    private fun SelectBankBottomSheet() {
        val dialogAllBank = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.rechargeserviceoperator, null)

        val BankData = view.findViewById<RecyclerView>(R.id.orperator)

        val sendMoneycategory = CustomeSearchBankAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
               _binding!!.selectBank.text = data.toString()
                _binding!!.proceedBtn.isSelected = true
                _binding!!.proceedBtn.isClickable = true
                dialogAllBank.dismiss()
            }
        }).apply {
            addItem(SendMoneyCustomeCategory("1", "SBI Bank", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("2", "kotak Bank", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("3", "Union Bank", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("4", "Bank Of Barodra ", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("5", "PNB", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("6", "Bank Of Maharashtra", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("7", "Central Bank Of India", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("8", "Hindustna Bank", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("9", "Jankalyan Bank", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("10", "AU Bank", "", R.drawable.sbi_bank, ""))
        }

        BankData.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sendMoneycategory
        }


        dialogAllBank.setContentView(view)
        dialogAllBank.show()
    }


    companion object {
       var  ifscCode =""
        @JvmStatic
        fun newInstance() =
            FindIFSCCodeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}