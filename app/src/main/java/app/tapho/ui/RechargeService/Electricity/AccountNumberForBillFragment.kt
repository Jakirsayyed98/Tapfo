package app.tapho.ui.RechargeService.Electricity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAccountNumberForBillBinding
import app.tapho.databinding.FragmentCustomerIdForDTHRechargeBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class AccountNumberForBillFragment : BaseFragment<FragmentAccountNumberForBillBinding>() {

    var ServiceTypeID=""
    var Name=""
    var icon=""
    var CustomerIDType=""
    var discription=""
    var AmountRange=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountNumberForBillBinding.inflate(inflater,container,false)
        statusBarTextBlack()
        statusBarColor(R.color.black)
        ServiceTypeID= getSharedPreference().getString("servicetype").toString()
        Name=getSharedPreference().getString("operator_nameELECT").toString()
        icon=getSharedPreference().getString("operator_iconELECT").toString()
        CustomerIDType= getSharedPreference().getString("account_displayELECT").toString()
        discription= getSharedPreference().getString("discriptionELECT").toString()
        AmountRange= getSharedPreference().getString("amount_rangeELECT").toString()


        setTextData()

        return _binding?.root
    }

    private fun setTextData() {
        _binding!!.name.text = Name
        Glide.with(requireContext()).load(icon).circleCrop().into(_binding!!.icon)
        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.CustomerID.hint = CustomerIDType
        if (discription.isNullOrEmpty().not()){
            _binding!!.discription.text = discription
        }
        _binding!!.change.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.CustomerID.addTextChangedListener {
            if (_binding!!.CustomerID.text!!.length>=2){
                _binding!!.conform.isSelected = true
                _binding!!.conform.isEnabled = true
            }else{
                _binding!!.conform.isSelected = false
            }
        }
        _binding!!.conform.setOnClickListener {
            if (_binding!!.CustomerID.text.isNullOrEmpty().not()){
                getSharedPreference().saveString("numberELECT",_binding!!.CustomerID.text.toString())
                if(getSharedPreference().getString("bill_fetchELECT").equals("1")){
                    //featch bill
                    ContainerActivity.openContainerForDTHRecharge(requireContext(),"ProceedForBillAmountAndDetaileFeatch")
                }else{
                    // enter Amount
                    ContainerActivity.openContainerForDTHRecharge(requireContext(),"ProceedForEnterAmount")
                }
            }else{
                Snackbar.make(requireView(),"Please enter valid Customer ID", Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AccountNumberForBillFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}