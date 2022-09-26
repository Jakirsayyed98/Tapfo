package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.SelfTransfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSelfBankTransferBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.Adapter.CustomeSearchBankAdapter
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.SelfTransfer.Adapter.RegisterBankAdapter
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment


class SelfBankTransferFragment : BaseFragment<FragmentSelfBankTransferBinding>(){

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
        _binding = FragmentSelfBankTransferBinding.inflate(inflater,container,false)
        SelecttedBankAccount = ""
        setBankFromDataInRVCustome()
        return _binding?.root
    }

    private fun setBankFromDataInRVCustome() {

        var FromBankDataAdapter = RegisterBankAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                _binding!!.transferToBankList.visibility = View.VISIBLE
                SelecttedBankAccount = data.toString()
                showAvaiLableBankToTransferMoney(data.toString())
            }
        }).apply {
            addItem(SendMoneyCustomeCategory("1", "SBI Bank", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("2", "kotak Bank", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("3", "Union Bank", "", R.drawable.axis_bank, ""))
        }

        _binding!!.bankListFrom.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = FromBankDataAdapter
        }
    }

    private fun showAvaiLableBankToTransferMoney( BankID: String) {
        var ToBankDataAdapter = RegisterBankAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        }).apply {

            addItem(SendMoneyCustomeCategory("1", "SBI Bank", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("2", "kotak Bank", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("3", "Union Bank", "", R.drawable.axis_bank, ""))


        }

        _binding!!.bankListTO.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = ToBankDataAdapter
        }
    }

    companion object {
        var SelecttedBankAccount =""
        @JvmStatic
        fun newInstance() =
            SelfBankTransferFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}