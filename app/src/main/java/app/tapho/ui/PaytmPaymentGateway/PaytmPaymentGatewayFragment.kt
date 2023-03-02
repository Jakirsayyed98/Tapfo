package app.tapho.ui.PaytmPaymentGateway

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.FragmentPaytmPaymentGatewayListDialogBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.showShortSnack
import app.tapho.ui.PaytmPaymentGateway.Adapter.PSPModelClass
import app.tapho.ui.PaytmPaymentGateway.Adapter.SmartIntentPSPAdapter
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes2
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*



class PaytmPaymentGatewayFragment : BottomSheetDialogFragment(), LoaderListener {

    private var _binding: FragmentPaytmPaymentGatewayListDialogBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: RequestViewModel


    val BHIM_UPI = "in.org.npci.upiapp"
    val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
    val PHONE_PE = "com.phonepe.app"
    val PAYTM = "net.one97.paytm"
    val Amazon_Pay = "in.amazon.mShop.android.shopping"
    val Axis = "com.axis.mobile"
    val Cred = "com.dreamplug.androidapp"
    val Whatsapp = "com.whatsapp"
    val WhatsappB = "com.whatsapp.w4b"
    val ICICI = "com.csam.icici.bank.imobile"
    val upiApps = listOf<String>(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI,Amazon_Pay,Axis,Cred,Whatsapp,WhatsappB,ICICI)

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPaytmPaymentGatewayListDialogBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)
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
        cash_available = getSharedPreference().getString("cash_available").toString()
        setAllTextAndIcons()

//        val mArgs = arguments
//        val myValue = mArgs!!.getString("keyjakirdata")

        progressVisible()

        getAndSetAllData()
        onBackPressed()
        return binding.root

    }

    private fun progressVisible() {
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.main.visibility = View.GONE
    }

    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(requireContext())
    }


    private fun getAndSetAllData() {
        val walletPay = cash_available.split(".")[0].toInt()

        // full Amount Reddem Code
/*
        if (walletPay.toInt()>=Amount.toInt()){
            val PaybleAmount = 0
            getSharedPreference().saveString("WalletAmount",Amount.toString())
            getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
            _binding!!.payment.text = "PAY ${withSuffixAmount(Amount.toString())} VIA TAPFO PAY"
            _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
            PayOnlyThroughWallet()
            _binding!!.progress.visibility = View.GONE
            _binding!!.main.visibility = View.VISIBLE
            _binding!!.payment.setOnClickListener {
                TransactionProcessingPageActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                activity?.finish()
            }

        }else{
            if (walletPay>=1){
                val PaybleAmount = Amount.toInt() - walletPay
                Log.d("MyTransactionAmount",roundOffAmount(PaybleAmount.toString()).toString())
                getSharedPreference().saveString("WalletAmount",walletPay.toString())
                getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
            }else{
                onlyPspPay()
            }
        }
*/
        // full Amount Reddem Code

        // 70% start

            if(walletPay>=1){

                if (walletPay.toInt()>=Amount.toInt()){
                    val walletPayAmount = roundOffAmount(( Amount.toDouble()/100*70).toString())
                    val PaybleAmount = Amount.toDouble() - walletPayAmount.toDouble()
                    getSharedPreference().saveString("WalletAmount",walletPayAmount.toString())
                    getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                    _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                    _binding!!.minimumbalance.visibility = View.VISIBLE
                    _binding!!.minimumbalance.text= "You can redeem "+ roundOff(walletPayAmount.toString()) +" on this recharge"
                    startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())

                }else if (walletPay.toInt()<= Amount.toInt()){
                    val walletPayAmount = roundOffAmount(( walletPay.toDouble()/100*70).toString())
                    val PaybleAmount = Amount.toDouble() - walletPayAmount.toDouble()
                    getSharedPreference().saveString("WalletAmount",walletPayAmount.toString())
                    getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
                    _binding!!.minimumbalance.visibility = View.VISIBLE
                    _binding!!.minimumbalance.text= "You can redeem "+roundOff(walletPayAmount.toString())+" on this recharge"
                    _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
                    startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
                }else{
                    onlyPspPay()
                }


            }else{
                onlyPspPay()
            }


        // 70% End

    }

    private fun setAllTextAndIcons() {
        _binding!!.apply {
            val walletBalance = cash_available.toString()
            Glide.with(requireContext()).load(operator_icon).circleCrop().into(opretorIcon)
            opretorName.text = operator_name+ " - " + circle_name
            numberandCircle.text = mobileNumber
            rechargeAmount.text = withSuffixAmount(Amount)
            paybleAmount.text = withSuffixAmount(Amount)
            avilableBalance.text = withSuffixAmount(walletBalance).toString()
        }
    }

    private fun onlyPspPay() {
        val PaybleAmount = Amount.toInt()
        getSharedPreference().saveString("WalletAmount", "0")
        getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
        _binding!!.pspAppsLayout.visibility = View.VISIBLE
        _binding!!.minimumbalance.visibility = View.GONE
        _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())

        startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
    }

    private fun startPaymentInitializ(PayableAmount: String) {
        val charset =('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMMyyhhmm")
        val currentDate = simpleDate.format(Date())
        val FinalorderID = randomString + currentDate+"T"+getSharedPreference().getUserId()+"PY"

        viewModel.getInitiateTransaction(getSharedPreference().getUserId(), PayableAmount.toDouble(), FinalorderID, this, object : ApiListener<InitiateTransactionRes2, Any?> {
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
        viewModel.getTransactionProcess(getSharedPreference().getUserId(), orderID.toString(), "UPI_INTENT", "", txnToken.toString(),
            this, object : ApiListener<TransactionProcessRes, Any?> {
                override fun onSuccess(t: TransactionProcessRes?, mess: String?) {
                    i = "1"
                    t!!.data.let {
                        val uri = it.body.deepLinkInfo.deepLink
                        val orderid = it.body.deepLinkInfo.orderId
                        DeeplInkURI = uri
                        OrderId = orderid
                        setLayoutAsAAdapter()
                    }
                }
            })
    }

    private fun setLayoutAsAAdapter() {
        val SmartIntentPSPAdapterdaa = SmartIntentPSPAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
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
                _binding!!.PSPApps.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
                _binding!!.main.visibility = View.VISIBLE
                requireContext().customToast("No UPI Supported Apps Found", true)
            }else{
                _binding!!.nestedScrollView.visibility = View.VISIBLE
                _binding!!.linearLayout6.visibility = View.VISIBLE
                _binding!!.PSPApps.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
                _binding!!.main.visibility = View.VISIBLE
            }
        }
        _binding!!.PSPApps.apply {
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
            dialog.dismiss()
            activity?.onBackPressedDispatcher?.onBackPressed()
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


















    private fun PayOnlyThroughWallet() {
        _binding!!.pspAppsLayout.visibility = View.GONE
        _binding!!.minimumbalance.visibility = View.GONE
        _binding!!.payment.visibility = View.VISIBLE
        _binding!!.nestedScrollView.visibility = View.VISIBLE
        _binding!!.linearLayout6.visibility = View.VISIBLE
//        _binding!!.progress.visibility = View.GONE
        _binding!!.PSPApps.visibility = View.VISIBLE
        _binding!!.linearLayout7.visibility = View.VISIBLE
//        setVisilityOfLayout()
    }


    companion object {

        fun newInstance(): PaytmPaymentGatewayFragment =
            PaytmPaymentGatewayFragment().apply {
                arguments = Bundle().apply {

                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showLoader() {

    }

    override fun dismissLoader() {
    }

    override fun showMess(mess: String?) {
    }
}