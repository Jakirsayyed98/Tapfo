package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSearchBankAccountBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.Adapter.CustomeSearchBankAdapter
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment


class SearchBankAccountFragment : BaseFragment<FragmentSearchBankAccountBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =FragmentSearchBankAccountBinding.inflate(inflater,container,false)
        searchBanks()
        return _binding?.root
    }
    private fun searchBanks() {
        val sendMoneycategory = CustomeSearchBankAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                UPIContainerActivity.openContainer(requireContext(), "EnterBankDetail", "", false, data.toString())
            }
        }).apply {
            addItem(SendMoneyCustomeCategory("1", "SBI Bank", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("2", "kotak Bank", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("3", "Union Bank", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("4", "Bank Of Barodra ", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("5", "PNB", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("6", "Bank Of Maharashtra", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("7", "Central Bank Of India", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("8", "Hindustna Bank", "", R.drawable.axis_bank, ""))
            addItem(SendMoneyCustomeCategory("9", "Jankalyan Bank", "", R.drawable.sbi_bank, ""))
            addItem(SendMoneyCustomeCategory("10", "AU Bank", "",R.drawable.axis_bank, ""))
        }

        _binding!!.BankData.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sendMoneycategory
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            SearchBankAccountFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}