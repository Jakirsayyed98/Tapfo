package app.tapho.ui.PaytmPaymentGateway

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSelectUPIAppsForPaymentBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.MyApiV2
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.PaytmPaymentGateway.Adapter.PSPModelClass
import app.tapho.ui.PaytmPaymentGateway.Adapter.SmartIntentPSPAdapter
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes2
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class SelectUPIAppsForPaymentFragment : BaseFragment<FragmentSelectUPIAppsForPaymentBinding>() {

    val BHIM_UPI = "in.org.npci.upiapp"
    val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
    val PHONE_PE = "com.phonepe.app"
    val PAYTM = "net.one97.paytm"
    val upiApps = listOf<String>(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI)
    var PSPModelClass: ArrayList<PSPModelClass> = ArrayList()
    private var SmartIntentPSPAdapterdaa: SmartIntentPSPAdapter? = null

    var OrderId = ""
    var DeeplInkURI = ""
    var Operator_Code = ""
    var Circle_Code = ""
    var mobileNumber = ""
    var servicetype = ""
    var discription = ""
    var Amount = ""
    var operator_name = ""
    var circle_name = ""
    var operator_icon = ""
    var circle_id = ""
    var operator_id = ""
    var cash_available = ""


    private val REQUEST_CODE = 123
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
        _binding = FragmentSelectUPIAppsForPaymentBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        progressvisible()
        Operator_Code = getSharedPreference().getString("Operator_Code").toString()
        Circle_Code = getSharedPreference().getString("Circle_Code").toString()
        mobileNumber = getSharedPreference().getString("number").toString()
        servicetype = getSharedPreference().getString("servicetype").toString()
        discription = getSharedPreference().getString("discription").toString()
        Amount = getSharedPreference().getString("Amount").toString()
        operator_name = getSharedPreference().getString("operator_name").toString()
        circle_name = getSharedPreference().getString("circle_name").toString()
        operator_icon = getSharedPreference().getString("operator_icon").toString()
        circle_id = getSharedPreference().getString("circle_id").toString()
        operator_id = getSharedPreference().getString("operator_id").toString()

        getTCashDashBoard()
        onBackPressed()

        return _binding?.root
    }

    private fun getTCashDashBoard() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),"2",this,object :
            ApiListener<TCashDasboardRes, Any?> {
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                t.let {
                    cash_available = it!!.cash_available.toString()
                    getAndSetAllData()
                }
            }
        })
    }



    private fun getAndSetAllData() {

        val walletPay = cash_available.toDouble()

// above 99 cash reedem code here
        /*
        if (walletPay.toDouble()<99){
            val walletPayAmount = 0
            val PaybleAmount = Amount.toDouble()
            getSharedPreference().saveString("WalletAmount",walletPayAmount.toString())
            getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
            _binding!!.radio0.isChecked = false
            _binding!!.radio0.isClickable = false
            _binding!!.pspAppsLayout.visibility = View.VISIBLE
            _binding!!.minimumbalance.visibility = View.VISIBLE
            _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
            setVisilityOfLayout()
            startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
        }else{



            if (walletPay>=Amount.toDouble()){

                _binding!!.radio0.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked){
                        val PaybleAmount =0 // Amount.toDouble()- walletPay
                        getSharedPreference().saveString("WalletAmount", Amount.toString())
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.pspAppsLayout.visibility = View.GONE
                        _binding!!.minimumbalance.visibility = View.GONE
                        _binding!!.PSPApps.visibility = View.GONE
                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        _binding!!.payment.setOnClickListener {
                            TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                            activity?.finish()
                        }
                        _binding!!.linearLayout7.visibility = View.VISIBLE
                        setVisilityOfLayout()
                    }else{

                        val PaybleAmount = Amount.toDouble()//- walletPay
                        getSharedPreference().saveString("WalletAmount", "0")
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.pspAppsLayout.visibility = View.VISIBLE
                        _binding!!.minimumbalance.visibility = View.GONE
                        _binding!!.payment.visibility = View.GONE
                        _binding!!.PSPApps.visibility = View.GONE
//                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        _binding!!.payment.setOnClickListener {
                            TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                            activity?.finish()
                        }
                        _binding!!.linearLayout7.visibility = View.VISIBLE
                        setVisilityOfLayout()
                        startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                    }
                }


                val PaybleAmount =0 // Amount.toDouble()- walletPay
                getSharedPreference().saveString("WalletAmount", Amount.toString())
                getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                _binding!!.pspAppsLayout.visibility = View.GONE
                _binding!!.minimumbalance.visibility = View.GONE
                _binding!!.payment.visibility = View.VISIBLE
                _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                _binding!!.payment.setOnClickListener {
                    TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                    activity?.finish()
                }
                _binding!!.linearLayout7.visibility = View.VISIBLE
                setVisilityOfLayout()


            }else
                if (walletPay<Amount.toDouble()){


                _binding!!.radio0.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked){
                        val PaybleAmount =Amount.toDouble()- walletPay
                        getSharedPreference().saveString("WalletAmount", walletPay.toString())
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.pspAppsLayout.visibility = View.VISIBLE
                        _binding!!.minimumbalance.visibility = View.GONE
                        _binding!!.payment.visibility = View.GONE
                        _binding!!.PSPApps.visibility = View.GONE
//                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        _binding!!.payment.setOnClickListener {
                            TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                            activity?.finish()
                        }
                        _binding!!.linearLayout7.visibility = View.VISIBLE
                        setVisilityOfLayout()
                    }else{
                        val PaybleAmount = Amount.toDouble()
//                      val PaybleAmount =Amount.toDouble()- cash_available.toDouble()
                        getSharedPreference().saveString("WalletAmount", walletPay.toString())
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.payment.visibility = View.GONE
                        _binding!!.pspAppsLayout.visibility = View.VISIBLE
                        _binding!!.minimumbalance.visibility = View.GONE
                        _binding!!.PSPApps.visibility = View.GONE
                        //      _binding!!.payment.text = "PAY ${withSuffixAmount(PaybleAmount.toString())} VIA TAPFO CARD"

                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        setVisilityOfLayout()
                        startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                    }
                }


                    val PaybleAmount =Amount.toDouble()- walletPay
                    getSharedPreference().saveString("WalletAmount", walletPay.toString())
                    getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                    _binding!!.pspAppsLayout.visibility = View.VISIBLE
                    _binding!!.minimumbalance.visibility = View.GONE
                    _binding!!.payment.visibility = View.GONE
//                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                    _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                    _binding!!.payment.setOnClickListener {
                        TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                        activity?.finish()
                    }
                    _binding!!.linearLayout7.visibility = View.VISIBLE
                    setVisilityOfLayout()


            }else{
                    val PaybleAmount = Amount.toDouble()
//            val PaybleAmount =Amount.toDouble()- cash_available.toDouble()
                    getSharedPreference().saveString("WalletAmount", "0")
                    getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                    _binding!!.pspAppsLayout.visibility = View.VISIBLE
                    _binding!!.minimumbalance.visibility = View.GONE

                    //      _binding!!.payment.text = "PAY ${withSuffixAmount(PaybleAmount.toString())} VIA TAPFO CARD"
                    _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                    setVisilityOfLayout()
                    startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                }

        }

        */
        // above 99 cash reedem code here


        // full Amount Reddem Code

/*
        if (walletPay>=Amount.toDouble()){

            _binding!!.radio0.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    val PaybleAmount =0 // Amount.toDouble()- walletPay
                    getSharedPreference().saveString("WalletAmount", Amount.toString())
                    getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                    _binding!!.pspAppsLayout.visibility = View.GONE
                    _binding!!.minimumbalance.visibility = View.GONE
                    _binding!!.PSPApps.visibility = View.GONE
                    _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                    _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                    _binding!!.payment.setOnClickListener {
                        TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                        activity?.finish()
                    }
                    _binding!!.linearLayout7.visibility = View.VISIBLE
                    setVisilityOfLayout()
                }else{

                    val PaybleAmount = Amount.toDouble()//- walletPay
                    getSharedPreference().saveString("WalletAmount", "0")
                    getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                    _binding!!.pspAppsLayout.visibility = View.VISIBLE
                    _binding!!.minimumbalance.visibility = View.GONE
                    _binding!!.payment.visibility = View.GONE
                    _binding!!.PSPApps.visibility = View.GONE
//                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                    _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                    _binding!!.payment.setOnClickListener {
                        TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                        activity?.finish()
                    }
                    _binding!!.linearLayout7.visibility = View.VISIBLE
                    setVisilityOfLayout()
                    startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                }
            }


            val PaybleAmount =0 // Amount.toDouble()- walletPay
            getSharedPreference().saveString("WalletAmount", Amount.toString())
            getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
            _binding!!.pspAppsLayout.visibility = View.GONE
            _binding!!.minimumbalance.visibility = View.GONE
            _binding!!.payment.visibility = View.VISIBLE
            _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
            _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
            //for now

              _binding!!.nestedScrollView.visibility = View.VISIBLE
                _binding!!.linearLayout6.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
                _binding!!.PSPApps.visibility = View.VISIBLE
             //for now
            _binding!!.payment.setOnClickListener {
                TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                activity?.finish()
            }
            _binding!!.linearLayout7.visibility = View.VISIBLE
            setVisilityOfLayout()


        }else
            if (walletPay<Amount.toDouble()){
                _binding!!.radio0.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked){
                        val PaybleAmount =Amount.toDouble()- walletPay
                        getSharedPreference().saveString("WalletAmount", walletPay.toString())
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.pspAppsLayout.visibility = View.VISIBLE
                        _binding!!.minimumbalance.visibility = View.GONE
                        _binding!!.payment.visibility = View.GONE
                        _binding!!.PSPApps.visibility = View.GONE
//                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        _binding!!.payment.setOnClickListener {
                            TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                            activity?.finish()
                        }
                        _binding!!.linearLayout7.visibility = View.VISIBLE
                        setVisilityOfLayout()
                    }else{
                        val PaybleAmount = Amount.toDouble()
//                      val PaybleAmount =Amount.toDouble()- cash_available.toDouble()
                        getSharedPreference().saveString("WalletAmount", walletPay.toString())
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.payment.visibility = View.GONE
                        _binding!!.pspAppsLayout.visibility = View.VISIBLE
                        _binding!!.minimumbalance.visibility = View.GONE
                        _binding!!.PSPApps.visibility = View.GONE
                        //      _binding!!.payment.text = "PAY ${withSuffixAmount(PaybleAmount.toString())} VIA TAPFO CARD"

                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        setVisilityOfLayout()
                        startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                    }
                }


                val PaybleAmount =Amount.toDouble()- walletPay
                getSharedPreference().saveString("WalletAmount", walletPay.toString())
                getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                _binding!!.pspAppsLayout.visibility = View.VISIBLE
                _binding!!.minimumbalance.visibility = View.GONE
                _binding!!.payment.visibility = View.GONE
//                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                _binding!!.payment.setOnClickListener {
                    TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                    activity?.finish()
                }
                _binding!!.linearLayout7.visibility = View.VISIBLE
                setVisilityOfLayout()


            }else{
                val PaybleAmount = Amount.toDouble()
//            val PaybleAmount =Amount.toDouble()- cash_available.toDouble()
                getSharedPreference().saveString("WalletAmount", "0")
                getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                _binding!!.pspAppsLayout.visibility = View.VISIBLE
                _binding!!.minimumbalance.visibility = View.GONE

                //      _binding!!.payment.text = "PAY ${withSuffixAmount(PaybleAmount.toString())} VIA TAPFO CARD"
                _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                setVisilityOfLayout()
                startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
            }
*/

        // full Amount Reddem Code


        // 70 % Logic


        _binding!!.radio0.setOnCheckedChangeListener { buttonView, isChecked ->
            progressvisible()
            if (isChecked){
                if (cash_available.toDouble()<=0.0){

                    val PaybleAmount = Amount.toDouble()
//          val PaybleAmount =Amount.toDouble()- cash_available.toDouble()

                    getSharedPreference().saveString("WalletAmount","0")
                    getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                    _binding!!.minimumbalance.text = "insufficient balance"
                    _binding!!.minimumbalance.setTextColor(Color.parseColor("#ff8000"))

                    _binding!!.minimumbalance.visibility = View.GONE
                    _binding!!.PSPApps.visibility = View.GONE
                    _binding!!.pspAppsLayout.visibility = View.VISIBLE
                    //      _binding!!.payment.text = "PAY ${withSuffixAmount(PaybleAmount.toString())} VIA TAPFO CARD"
                    _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())

                    startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                    setVisilityOfLayout()
                }else{
                    if (cash_available.toDouble()>=Amount.toDouble()){
                        val walletPay = roundOff((Amount.toDouble()/100*70).toString()).replace("₹","")
                        val PaybleAmount = Amount.toDouble() - walletPay.toDouble()

                        getSharedPreference().saveString("WalletAmount",walletPay.toString())
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.minimumbalance.text= "You can redeem ₹"+walletPay.toString()+" on this recharge"
                        _binding!!.minimumbalance.setTextColor(Color.parseColor("#008D3A"))
                        _binding!!.minimumbalance.visibility = View.VISIBLE
                        _binding!!.minimumbalance.text= "You can use only ₹"+walletPay.toString()+" on this recharge"
                        _binding!!.pspAppsLayout.visibility = View.VISIBLE
                        _binding!!.PSPApps.visibility = View.GONE
                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        setVisilityOfLayout()
                        startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                    }else if (cash_available.toDouble()<Amount.toDouble()){

                        val walletPay =roundOff((cash_available.toDouble()/100*70).toString()).replace("₹","")

                        val PaybleAmount = Amount.toDouble() - walletPay.toDouble()
                        _binding!!.minimumbalance.visibility = View.VISIBLE
                        _binding!!.minimumbalance.text= "You can use only ₹"+walletPay.toString()+" on this recharge"
                        getSharedPreference().saveString("WalletAmount",walletPay.toString())
                        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                        _binding!!.minimumbalance.text= "You can redeem ₹"+walletPay.toString()+" on this recharge"
                        _binding!!.minimumbalance.setTextColor(Color.parseColor("#008D3A"))
                        _binding!!.pspAppsLayout.visibility = View.VISIBLE
                        _binding!!.PSPApps.visibility = View.GONE
                        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                        setVisilityOfLayout()
                        startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())

                    }
                }
            }else{
                val PaybleAmount = Amount.toDouble()//- walletPay
                getSharedPreference().saveString("WalletAmount", "0")
                getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                _binding!!.pspAppsLayout.visibility = View.VISIBLE
                _binding!!.minimumbalance.visibility = View.GONE
                _binding!!.payment.visibility = View.GONE
                _binding!!.PSPApps.visibility = View.GONE

                _binding!!.minimumbalance.visibility = View.GONE

//                        _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
                _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                _binding!!.payment.setOnClickListener {
                    TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                    activity?.finish()
                }
                _binding!!.linearLayout7.visibility = View.VISIBLE

                startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                setVisilityOfLayout()
            }
        }

        if (cash_available.toDouble()<=0.0){

            val PaybleAmount = Amount.toDouble()
//          val PaybleAmount =Amount.toDouble()- cash_available.toDouble()

            getSharedPreference().saveString("WalletAmount","0")
            getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
            _binding!!.minimumbalance.text = "insufficient balance"
            _binding!!.minimumbalance.setTextColor(Color.parseColor("#ff8000"))

            _binding!!.pspAppsLayout.visibility = View.VISIBLE
            //      _binding!!.payment.text = "PAY ${withSuffixAmount(PaybleAmount.toString())} VIA TAPFO CARD"
            _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())

            startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
            setVisilityOfLayout()
        }else{
            if (cash_available.toDouble()>=Amount.toDouble()){
                val walletPay =roundOff(( Amount.toDouble()/100*70).toString()).replace("₹","")
                val PaybleAmount = Amount.toDouble() - walletPay.toDouble()

                getSharedPreference().saveString("WalletAmount",walletPay.toString())
                getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                _binding!!.minimumbalance.visibility = View.VISIBLE
                _binding!!.minimumbalance.text= "You can redeem ₹"+walletPay.toString()+" on this recharge"
                _binding!!.minimumbalance.setTextColor(Color.parseColor("#008D3A"))
                _binding!!.pspAppsLayout.visibility = View.VISIBLE
                _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())

                startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                setVisilityOfLayout()
            }else if (cash_available.toDouble()<Amount.toDouble()){

                val walletPay = roundOff((cash_available.toDouble()/100*70).toString()).replace("₹","")

                val PaybleAmount = Amount.toDouble() - walletPay.toDouble()
                _binding!!.minimumbalance.visibility = View.VISIBLE
                _binding!!.minimumbalance.text= "You can use only ₹"+walletPay.toString()+" on this recharge"
                _binding!!.minimumbalance.setTextColor(Color.parseColor("#008D3A"))
                getSharedPreference().saveString("WalletAmount",walletPay.toString())
                getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                _binding!!.pspAppsLayout.visibility = View.VISIBLE
                _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())

                startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                setVisilityOfLayout()
            }
        }

        // 70% end

        setAllTextAndIcons()
    }

    private fun setVisilityOfLayout() {

//        this 3
//        _binding!!.nestedScrollView.visibility = View.VISIBLE
//        _binding!!.linearLayout6.visibility = View.VISIBLE
//        _binding!!.progress.visibility = View.GONE


     //   _binding!!.PSPApps.visibility = View.VISIBLE

//        _binding!!.linearLayout7.visibility = View.VISIBLE
    }


    private fun progressvisible(){
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.linearLayout7.visibility = View.GONE
        _binding!!.nestedScrollView.visibility = View.GONE
        _binding!!.linearLayout6.visibility = View.GONE
    }

    private fun setAllTextAndIcons() {
        _binding!!.apply {
            val walletBalance = cash_available
            Glide.with(requireContext()).load(operator_icon).circleCrop().into(opretorIcon)
            opretorName.text = operator_name
            numberandCircle.text = mobileNumber + " - " + circle_name
            rechargeAmount.text = withSuffixAmount(Amount)
            avilableBalance.text = getString(R.string.fopay_balance, withSuffixAmount(walletBalance))

        }
    }

    private fun startPaymentInitializ(PayableAmount : String) {
        val charset =('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMMyyhhmm")
        val currentDate = simpleDate.format(Date())
            //   abc1210230110T000000PY
        val FinalorderID = randomString + currentDate+"T"+getUserId()+"PY"

        viewModel.getInitiateTransaction(getUserId(), PayableAmount.toDouble(), FinalorderID, this, object : ApiListener<InitiateTransactionRes2, Any?> {
                override fun onSuccess(t: InitiateTransactionRes2?, mess: String?) {
                    t!!.data.let {
                        if (it.body.resultInfo.resultStatus.equals("S")) {
                            paymentProseccingAfterIntialising(it.body.txnToken, FinalorderID)
                        } else {
                            requireView().showShortSnack(it.body.resultInfo.resultMsg)
                        }
                    }
                }

            })
    }

    private fun paymentProseccingAfterIntialising(txnToken: String, orderID: String) {
        var i = "0"
        Handler(Looper.getMainLooper()).postDelayed({

            if (i != "1"){
                paymentProseccingAfterIntialising(txnToken,orderID)
            }
        }, 20000)

        viewModel.getTransactionProcess(getUserId(), orderID.toString(), "UPI_INTENT", "", txnToken.toString(),
            this, object : ApiListener<TransactionProcessRes, Any?> {
                override fun onSuccess(t: TransactionProcessRes?, mess: String?) {
                    i = "1"
                    t!!.data.let {
                        val uri = it.body.deepLinkInfo.deepLink
                        val orderid = it.body.deepLinkInfo.orderId
                        DeeplInkURI = uri
                        OrderId = orderid
//                        _binding!!.nestedScrollView.visibility = View.VISIBLE
//                        _binding!!.linearLayout6.visibility = View.VISIBLE
//                        _binding!!.progress.visibility = View.GONE
//                        _binding!!.PSPApps.visibility = View.VISIBLE
                        setLayoutAsAAdapter()
                    }
                }

            })
    }

    private fun setLayoutAsAAdapter() {
        val SmartIntentPSPAdapterdaa = SmartIntentPSPAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeeplInkURI))
//                intent.data = Uri.parse(DeeplInkURI)
//                intent.setPackage(data.toString())
//                startActivityForResult(intent, REQUEST_CODE)
                _binding!!.payment.text =getString(R.string.pay_with_gpay_upi, withSuffixAmount(getSharedPreference().getString("PayableAmount")),type.toString())
                _binding!!.linearLayout7.visibility = View.VISIBLE
                _binding!!.payment.visibility = View.VISIBLE
                _binding!!.payment.setOnClickListener {
                    TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", DeeplInkURI, OrderId, data.toString(),"0")
                    activity?.finish()
                }

            }

        }).apply {
            for (i in upiApps.indices) {
                val p = upiApps[i]
                if (isAppInstalled(p) == true &&isAppUpiReady(p)) {
                    addItem(PSPModelClass(p, false))
                }
            }

            if (this.list.isNullOrEmpty()){
                _binding!!.nestedScrollView.visibility = View.VISIBLE
                _binding!!.linearLayout6.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
                _binding!!.PSPApps.visibility = View.VISIBLE
                requireContext().customToast("PSP Apps Not Found", true)

            }else{
                _binding!!.nestedScrollView.visibility = View.VISIBLE
                _binding!!.linearLayout6.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
                _binding!!.PSPApps.visibility = View.VISIBLE
            }
        }
        _binding!!.PSPApps.apply {
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = SmartIntentPSPAdapterdaa
        }
    }

    fun onBackPressed() {
        _binding!!.back.setOnClickListener {
            BackPressAction()
        }
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                BackPressAction()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback)
    }

    private fun BackPressAction() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.paymentexit_bottomsheet1, null)
        dialog.setCancelable(false)

        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)
        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)


        exit.setOnClickListener {
            activity?. onBackPressed()
            activity?.finish()
        }


        app_continue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()


    }

    fun isAppInstalled(packageName: String): Boolean {
        val pm = requireContext().getPackageManager()
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false;
    }

    fun isAppUpiReady(packageName: String): Boolean {
        var appUpiReady = false
        val upiIntent = Intent(Intent.ACTION_VIEW, Uri.parse("upi://pay"))
        val pm = requireContext().getPackageManager()
        val upiActivities: List<ResolveInfo> = pm.queryIntentActivities(upiIntent, 0)
        for (a in upiActivities){
            if (a.activityInfo.packageName == packageName) appUpiReady = true
        }
        return appUpiReady
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SelectUPIAppsForPaymentFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}