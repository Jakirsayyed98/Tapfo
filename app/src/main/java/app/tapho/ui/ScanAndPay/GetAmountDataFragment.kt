package app.tapho.ui.ScanAndPay

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentGetAmountDataBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes2
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.ScanAndPay.PaymentGateway.Adapter.ScanAndPayIntentPSPAdapter
import app.tapho.ui.ScanAndPay.PaymentGateway.Model.ScanAndPayPSPModelClass
import app.tapho.ui.model.UserDetails.getUserDetailRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.DATA
import app.tapho.utils.withSuffixAmount
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


class GetAmountDataFragment : BaseFragment<FragmentGetAmountDataBinding>() {

    val BHIM_UPI = "in.org.npci.upiapp"
    val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
    val PHONE_PE = "com.phonepe.app"
    val PAYTM = "net.one97.paytm"
    val upiApps = listOf<String>(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI)
    var data = ""
    var DeeplInkURI = ""
    var OrderId = ""
    var cashAvailable = ""
    var payingto =""

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
        _binding = FragmentGetAmountDataBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        payingto = activity?.intent?.getStringExtra(DATA).toString()
        getSharedPreference().saveString("payingToUser",payingto.dropLast(1).drop(1))
        setDataToLayout(payingto.replace("tapfo0","").dropLast(1).drop(1))
        statusBarTextWhite()
        PayThroughData()
        getTcashdashboard()
        return _binding?.root
    }

    private fun setDataToLayout(ToPayDATA: String) {
        viewModel.get_user_detail(ToPayDATA,this,object : ApiListener<getUserDetailRes,Any?>{
            override fun onSuccess(t: getUserDetailRes?, mess: String?) {
                t!!.data.get(0).let {
                    _binding!!.name.text = it.name
                    _binding!!.upiId.text = it.name.replace(" ","")+"@tapfopay"
                }
            }
        })

    }

    private fun getTcashdashboard() {
        viewModel.getTCashDashboard(getUserId(),TimePeriodDialog.getCurrentDate(),TimePeriodDialog.getCurrentDate(),"2",this,object :ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
             t!!.let {
                 cashAvailable = it.cash_available.toString()
             }
            }
        })
    }

    private fun PayThroughData() {
        _binding!!.Amount.addTextChangedListener {
            if (it.toString().isNullOrEmpty().not()) {
                if (it.toString().toInt() <= 0) {
                    _binding!!.cashbackpopup.visibility = View.GONE
                    _binding!!.conformbtn.isSelected = false
                } else {
                    _binding!!.cashbackpopup.visibility = View.VISIBLE
                    _binding!!.conformbtn.isSelected = true
                }
            } else {
                _binding!!.cashbackpopup.visibility = View.GONE
                _binding!!.conformbtn.isSelected = false
            }
        }

//        _binding!!.conformbtn.setOnClickListener {
//            if(it.isSelected == true){
//               ScanAndPayContainerActivity.openContainer(requireContext(),"TapfoPayPinVerifyFragment",data,_binding!!.Amount.text.toString(),_binding!!.notes.text.toString())
//            }
//        }

        _binding!!.conformbtn.setOnClickListener {
            if (cashAvailable.isNullOrEmpty().not()){
                openPaymentOptionDialog()
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun openPaymentOptionDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.tapfopay_paymentoption,null)
        dialog.setCancelable(false)
        val cancel :ImageView = view.findViewById(R.id.cancel_btn)
        val walletAmount :TextView = view.findViewById(R.id.walletAmount)
        val PaybleAmountTv :TextView = view.findViewById(R.id.PaybleAmount)
        val pspAppsLayout : LinearLayout = view.findViewById(R.id.pspAppsLayout)
        val linearLayout7 : ConstraintLayout = view.findViewById(R.id.linearLayout7)
        val payment : AppCompatButton = view.findViewById(R.id.payment)
        val PSPAppsRV :RecyclerView = view.findViewById(R.id.PSPApps)


        walletAmount.text = withSuffixAmount(cashAvailable)
        var PaybleAmount=""
        val pspPaybleAmount =   _binding!!.Amount.text.toString().toDouble() - cashAvailable.toDouble()
        if (pspPaybleAmount<=0.0){
            pspAppsLayout.visibility = View.GONE
            PaybleAmount = _binding!!.Amount.text.toString()
            linearLayout7.visibility = View.VISIBLE
            payment.text = "PAY ${withSuffixAmount(PaybleAmount)} VIA TAPFO PAY"
            payment.setOnClickListener {
                getSharedPreference().saveString("WalletPayAmount",_binding!!.Amount.text.toString())
                getSharedPreference().saveString("TransactionNote",_binding!!.notes.text.toString())
                ScanAndPayContainerActivity.openContainer(requireContext(),"TapfoPayPinVerifyFragment",data,_binding!!.Amount.text.toString(),_binding!!.notes.text.toString())
            }
        }else{
            PaybleAmount = withSuffixAmount(pspPaybleAmount.toString()).toString()
            val walletpayble = _binding!!.Amount.text.toString().toDouble() - pspPaybleAmount.toDouble()


            getSharedPreference().saveString("WalletPayAmount",walletpayble.toString())
            getSharedPreference().saveString("TransactionNote",_binding!!.notes.text.toString())
            getSharedPreference().saveString("PspPayAmount",PaybleAmount.drop(1))

            PaybleAmountTv.text = pspPaybleAmount.toString()
            pspAppsLayout.visibility = View.VISIBLE

            PaybleAmount = withSuffixAmount(pspPaybleAmount.toString().replace("-","")).toString()

            InitiatePaymentTransaction(PaybleAmount.drop(1))
            setLayouttoPSPRV(PSPAppsRV,linearLayout7,payment,PaybleAmount.drop(1))
        }


        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun InitiatePaymentTransaction(paybleAmount: String) {

        val charset = ('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmmss")
        val currentDate = simpleDate.format(Date())
        val FinalorderID = randomString + currentDate+"T"+getUserId()+"PY"
        viewModel.getInitiateTransaction(getUserId(),paybleAmount.toDouble(),FinalorderID,this,object :ApiListener<InitiateTransactionRes2,Any?>{
            override fun onSuccess(t: InitiateTransactionRes2?, mess: String?) {
             t!!.data.let {
                 it.let {
                     if (it.body.resultInfo.resultStatus.equals("S")) {
                         paymentProseccingAfterIntialising(it.body.txnToken, FinalorderID)
                     } else {
                         requireView().showShortSnack(it.body.resultInfo.resultMsg)
                     }
                 }
             }
            }
        })
    }

    private fun paymentProseccingAfterIntialising(txnToken: String, finalorderID: String) {
        viewModel.getTransactionProcess(getUserId(), finalorderID, "UPI_INTENT", "",
            txnToken, this, object : ApiListener<TransactionProcessRes, Any?> {
            override fun onSuccess(t: TransactionProcessRes?, mess: String?) {
                t!!.data.let {
                    val uri = it.body.deepLinkInfo.deepLink
                    val orderid = it.body.deepLinkInfo.orderId
                    DeeplInkURI = uri
                    OrderId = orderid
                }
            }
        })
    }

    private fun setLayouttoPSPRV(pspAppsRV: RecyclerView,linearLayout7: ConstraintLayout ,payment : AppCompatButton,PaybleAmount: String) {
        val SmartIntentPSPAdapter = ScanAndPayIntentPSPAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                linearLayout7.visibility = View.VISIBLE
                payment.text =getString(R.string.pay_with_gpay_upi, withSuffixAmount(PaybleAmount),type.toString())
                payment.setOnClickListener {
                    if(DeeplInkURI.isNullOrEmpty().not()){
                        ScanAndPayPaymentProcessingActivity.openContainer(requireContext(),"ScanAndPayPaymentProcessingActivity","",DeeplInkURI,type.toString(),OrderId)
                    }else{
                        requireContext().showShort("please wait")
                    }
                }

            }
        }).apply {
            for (i in upiApps.indices) {
                val p = upiApps[i]
                if (isAppInstalled(p) == true  &&isAppUpiReady(p)) {
                    addItem(ScanAndPayPSPModelClass(p, false))
                }
            }
        }
       pspAppsRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = SmartIntentPSPAdapter
        }
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
            GetAmountDataFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}