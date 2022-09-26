package app.tapho.ui.RechargeService.MobileRechcrge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import app.tapho.R
import app.tapho.databinding.FragmentBeforeRechargeAllDetailskBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.payment.UpiAppAdapter
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_enter_bank_detail.*
import net.one97.paytm.nativesdk.PaytmSDK
import net.one97.paytm.nativesdk.dataSource.PaytmPaymentsUtilRepository


class BeforeRechargeAllDetailskFragment : BaseFragment<FragmentBeforeRechargeAllDetailskBinding>() {

    var Operator_Code=""
    var Circle_Code=""
    var mobileNumber=""
    var servicetype=""
    var discription=""
    var Amount=""
    var operator_name=""
    var circle_name=""
    var operator_icon=""
    var circle_id=""
    var operator_id=""
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
        statusBarTextWhite()
        _binding = FragmentBeforeRechargeAllDetailskBinding.inflate(inflater,container,false)
        Operator_Code = getSharedPreference().getString("Operator_Code").toString()
        Circle_Code =  getSharedPreference().getString("Circle_Code").toString()
        mobileNumber =  getSharedPreference().getString("number").toString()
        servicetype = getSharedPreference().getString("servicetype").toString()
        discription =  getSharedPreference().getString("discription").toString()
        Amount =  getSharedPreference().getString("Amount").toString()
        operator_name =  getSharedPreference().getString("operator_name").toString()
        circle_name = getSharedPreference().getString("circle_name").toString()
        operator_icon =  getSharedPreference().getString("operator_icon").toString()
        circle_id =  getSharedPreference().getString("circle_id").toString()
        operator_id =  getSharedPreference().getString("operator_id").toString()

        _binding!!.backIv.setOnClickListener {
            ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")
            activity?.finish()
        }

        _binding!!.proceedTorecharge.setOnClickListener {
            ContainerActivity.openContainer(requireContext(),"SelectpaymentMethods","","")
            activity?.finish()
        }




        setAlltext()
        backpressedbtn()
        return _binding?.root
    }


    private fun backpressedbtn() {
        val  OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")
                activity?.finish()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), OnBackPressedCallback)
    }



    private fun setAlltext() {
        _binding!!.rechargeAmount.text = withSuffixAmount(Amount)
        _binding!!.mobileNumber.text = getString(R.string.recharge_for_0000000000,mobileNumber)
//        _binding!!.proceedTorecharge.text = getString(R.string.proceed_000,withSuffixAmount(Amount))
        _binding!!.discription.text = discription
        _binding!!.opratorAndCircleName.text = operator_name +" - "+circle_name
        Glide.with(requireContext()).load(operator_icon).circleCrop().into(_binding!!.icon)

    }






    companion object {

        @JvmStatic
        fun newInstance() =
            BeforeRechargeAllDetailskFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}