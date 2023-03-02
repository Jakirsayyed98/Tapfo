package app.tapho.ui.RechargeService.DTHRecharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentDTHRechargeFinalScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide


class DTHRechargeFinalScreenFragment : BaseFragment<FragmentDTHRechargeFinalScreenBinding>() {

    var ServiceTypeID=""
    var Name=""
    var icon=""
    var CustomerIDType=""
    var discription=""
    var AmountRange=""
    var CustomerID=""
    var rechargeAmount=""
    var cash_available=""

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

        _binding = FragmentDTHRechargeFinalScreenBinding.inflate(inflater,container,false)
        statusBarTextBlack()
        statusBarColor(R.color.black)
        rechargeAmount= activity?.intent?.getStringExtra("Amount").toString()
        ServiceTypeID= getSharedPreference().getString("servicetype").toString()
        Name=getSharedPreference().getString("operator_nameDTH").toString()
        icon= getSharedPreference().getString("operator_iconDTH").toString()
        CustomerIDType= getSharedPreference().getString("account_displayDTH").toString()
        discription= getSharedPreference().getString("discriptionDTH").toString()
        AmountRange= getSharedPreference().getString("amount_rangeDTH").toString()
        CustomerID= getSharedPreference().getString("numberDTH").toString()
        rechargeAmount= getSharedPreference().getString("Amount").toString()


        setAllText()


        return _binding?.root
    }

    private fun setAllText() {
            Glide.with(requireContext()).load(icon).circleCrop().into(_binding!!.icon)
            _binding!!.Name.text = Name
            _binding!!.rechargeAmount.text =  withSuffixAmount(rechargeAmount)
            _binding!!.CutomerID.text = getString(R.string.recharge_for_0000000000,CustomerID)
//          _binding!!.discription.text= discription
            _binding!!.proceedTorecharge.text = getString(R.string.proceed_000/*, withSuffixAmount(rechargeAmount)*/)

        _binding!!.proceedTorecharge.setOnClickListener {
                ContainerActivity.openContainer(requireContext(),"SelectpaymentMethods","","")
                activity?.finish()

        }

        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            DTHRechargeFinalScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}