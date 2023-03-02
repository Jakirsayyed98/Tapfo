package app.tapho.ui.RechargeService.MobileRechcrge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import app.tapho.R
import app.tapho.databinding.FragmentBeforeRechargeAllDetailskBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.PaytmPaymentGatewayFragment
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide


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
    var min_recharge=""
    var user_commission=""
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
        statusBarTextWhite()
        statusBarColor(R.color.white)
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

        user_commission =  getSharedPreference().getString("user_commission").toString()
        min_recharge =  getSharedPreference().getString("min_recharge").toString()



        _binding!!.backbtn.setOnClickListener {
            ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")
            activity?.finish()
        }

//        _binding!!.proceedTorecharge.setOnClickListener {
//            OpenPaymentMethod()
////                ContainerActivity.openContainer(requireContext(),"SelectpaymentMethods","","")
////                activity?.finish()
//        }
        getTCashDashBoard()


        if (Amount.toDouble()>=min_recharge.toDouble()){
            _binding!!.casbacklayout.visibility = View.VISIBLE
            val cashbacklayout = Amount.toDouble()/100*user_commission.toDouble()
            _binding!!.cashback.text = getString(R.string.cashback_applied, withSuffixAmount(cashbacklayout.toString()))
        }else{
            _binding!!.casbacklayout.visibility = View.GONE
        }

        setAlltext()
        backpressedbtn()
        return _binding?.root
    }

    private fun getTCashDashBoard() {
        viewModel.getTCashDashboard(getSharedPreference().getUserId(), TimePeriodDialog.getCurrentDate(), TimePeriodDialog.getCurrentDate(),"2",this,object :
            ApiListener<TCashDasboardRes, Any?> {
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                t.let {
                    cash_available = it!!.cash_available.toString()
                    getSharedPreference().saveString("cash_available",cash_available)
                    _binding!!.proceedTorecharge.setOnClickListener {
                        OpenPaymentMethod()
//                ContainerActivity.openContainer(requireContext(),"SelectpaymentMethods","","")
//                activity?.finish()
                    }


                }
            }
        })
    }


    private fun OpenPaymentMethod() {
        val paytmPaymentgatewayDialog = PaytmPaymentGatewayFragment()
//        val args = Bundle()
//        args.putString("keyjakirdata", "Your value is very secret")
//       paytmPaymentgatewayDialog.setArguments(args)
        paytmPaymentgatewayDialog.isCancelable = false
        paytmPaymentgatewayDialog.show(childFragmentManager,paytmPaymentgatewayDialog.tag)
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