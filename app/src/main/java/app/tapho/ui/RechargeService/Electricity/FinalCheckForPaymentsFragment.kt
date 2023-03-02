package app.tapho.ui.RechargeService.Electricity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentFinalCheckForPaymentsBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog


class FinalCheckForPaymentsFragment : BaseFragment<FragmentFinalCheckForPaymentsBinding>() {

    var ServiceTypeID=""
    var Name=""
    var icon=""
    var CustomerIDType=""
    var discription=""
    var AmountRange=""
    var CustomerID=""
    var rechargeAmount=""
    var bill_fetch=""
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
        _binding = FragmentFinalCheckForPaymentsBinding.inflate(inflater,container,false)
        statusBarTextBlack()
        statusBarColor(R.color.black)
        rechargeAmount= activity?.intent?.getStringExtra("Amount").toString()
        ServiceTypeID= getSharedPreference().getString("servicetype").toString()
        Name=getSharedPreference().getString("operator_nameELECT").toString()
        icon= getSharedPreference().getString("operator_iconELECT").toString()
        CustomerIDType= getSharedPreference().getString("account_displayELECT").toString()
        discription= getSharedPreference().getString("discriptionELECT").toString()
        AmountRange= getSharedPreference().getString("amount_rangeELECT").toString()
        CustomerID= getSharedPreference().getString("numberELECT").toString()
        rechargeAmount= getSharedPreference().getString("Amount").toString()
        bill_fetch= getSharedPreference().getString("bill_fetchELECT").toString()

        if (bill_fetch.equals("1")){
            _binding!!.viewDetails.visibility = View.VISIBLE
            _binding!!.dueDate.text = "Bill Due Date: 29 Aug 22"
            _binding!!.discription.text = "Bill Owner Name"
            _binding!!.dueDate.setOnClickListener {

            }
        }else{
            _binding!!.viewDetails.visibility = View.GONE
            _binding!!.dueDate.text = "Change"
            _binding!!.discription.text = discription
            _binding!!.dueDate.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
        }


        setAllText()

        _binding!!.viewDetails.setOnClickListener {
            OpenBottomSheetForViewDetail()
        }

        return _binding?.root
    }

    private fun OpenBottomSheetForViewDetail() {
        val dialogAllOprator = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.rechargeserviceoperator, null)
        val orperator = view.findViewById<RecyclerView>(R.id.orperator)

        dialogAllOprator.setContentView(view)
        dialogAllOprator.show()

    }

    private fun setAllText() {
        Glide.with(requireContext()).load(icon).circleCrop().into(_binding!!.icon)
        _binding!!.Name.text = Name
        _binding!!.rechargeAmount.text =  withSuffixAmount(rechargeAmount)
        _binding!!.CutomerID.text = getString(R.string.recharge_for_0000000000,CustomerID)
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
            FinalCheckForPaymentsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}