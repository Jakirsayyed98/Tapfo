package app.tapho.ui.RechargeService.Electricity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentDTHRechargeFinalScreenBinding
import app.tapho.databinding.FragmentFinalCheckForPaymentsBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.MobileRechcrge.MobileRechargeFragment
import app.tapho.ui.RechargeService.MobileRechcrge.adapter.RechargeCircleAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.Data
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
        statusBarTextWhite()
        statusBarColor(R.color.white)
        rechargeAmount= activity?.intent?.getStringExtra("Amount").toString()
        ServiceTypeID= getSharedPreference().getString("servicetype").toString()
        Name=getSharedPreference().getString("operator_name").toString()
        icon= getSharedPreference().getString("operator_icon").toString()
        CustomerIDType= getSharedPreference().getString("account_display").toString()
        discription= getSharedPreference().getString("discription").toString()
        AmountRange= getSharedPreference().getString("amount_range").toString()
        CustomerID= getSharedPreference().getString("number").toString()
        rechargeAmount= getSharedPreference().getString("Amount").toString()
        bill_fetch= getSharedPreference().getString("bill_fetch").toString()


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
             activity?.onBackPressed()
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
        }

        _binding!!.backIv.setOnClickListener {
            activity?.onBackPressed()
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