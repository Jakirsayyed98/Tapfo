package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentSerachNumberAndShowBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber.Adapter.CustomeSearchPeopleAdapter
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber.Adapter.SearchPeopleCustomeDataModel
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment


class SerachNumberAndShowFragment : BaseFragment<FragmentSerachNumberAndShowBinding>(){

    var MobileNumber = ""

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
        _binding = FragmentSerachNumberAndShowBinding.inflate(inflater,container,false)
        MobileNumber = activity?.intent?.getStringExtra("UPIID").toString()
        setTextAndClick()
        SearchData()
        return _binding?.root
    }

    private fun setTextAndClick() {

        _binding!!.number.addTextChangedListener {
            if (_binding!!.number.text.toString().length==10){
                _binding!!.proceedBtn.isClickable=true
                _binding!!.proceedBtn.isSelected=true
            }else{
                _binding!!.proceedBtn.isClickable=false
                _binding!!.proceedBtn.isSelected=false
            }
        }
        _binding!!.apply {
            backIv.setOnClickListener {
                activity?.onBackPressed()
            }
            if (number.text.isNullOrEmpty()){
                _binding!!.proceedBtn.isClickable=false
                _binding!!.proceedBtn.isSelected=false
            }else{
                _binding!!.proceedBtn.isClickable=true
                _binding!!.proceedBtn.isSelected=true
            }
            number.setText(MobileNumber)
        }
    }

    private fun SearchData() {
        val CustomeSearchPeopleAdapter = CustomeSearchPeopleAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                UPIContainerActivity.openContainer(requireContext(),"GoForUPIPayment","",false,"",_binding!!.number.text.toString())


            }

        }).apply {
            addItem(SearchPeopleCustomeDataModel("1", "Javed", "8108893686", ""))
            addItem(SearchPeopleCustomeDataModel("2", "Ravi", "8655158963", ""))
            addItem(SearchPeopleCustomeDataModel("3", "Anil", "7854893056", ""))
        }

        _binding!!.searchData.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = CustomeSearchPeopleAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SerachNumberAndShowFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}