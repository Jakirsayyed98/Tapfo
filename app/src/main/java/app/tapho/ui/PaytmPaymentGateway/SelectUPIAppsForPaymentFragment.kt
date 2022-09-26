package app.tapho.ui.PaytmPaymentGateway

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.DoubleRonduFiger
import app.tapho.R
import app.tapho.databinding.FragmentPaymentStatusScreenBinding
import app.tapho.databinding.FragmentSelectUPIAppsForPaymentBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.Adapter.PSPModelClass
import app.tapho.ui.PaytmPaymentGateway.Adapter.SmartIntentPSPAdapter
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.withSuffixAmount
import app.tapho.utils.withSuffixAmount3
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SelectUPIAppsForPaymentFragment : BaseFragment<FragmentSelectUPIAppsForPaymentBinding>() {

    val BHIM_UPI = "in.org.npci.upiapp"
    val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
    val PHONE_PE = "com.phonepe.app"
    val PAYTM = "net.one97.paytm"
   // val AmazonPay = "in.amazon.mShop.android.shopping"


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

        _binding!!.progress.visibility = View.VISIBLE
   //     _binding!!.PSPApps.visibility = View.GONE
        _binding!!.linearLayout7.visibility = View.GONE
        _binding!!.nestedScrollView.visibility = View.GONE
        _binding!!.linearLayout6.visibility = View.GONE
//        txnToken = activity?.intent?.getStringExtra("txnToken").toString()
//        OrderID = activity?.intent?.getStringExtra("orderID").toString()
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

        getTcashData()

        onBackPressed()


//        paymentProseccingAfterIntialising(txnToken,OrderID)

//        setLayoutAsAAdapter()
        return _binding?.root
    }

    private fun getTcashData() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),this,object :ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                t.let {
                    cash_available = it!!.cash_available.toString()
                           getAndSetAllData()
                }
            }
        })
    }

    private fun getAndSetAllData() {
        if (cash_available.toString().toDouble()>=Amount.toString().toDouble()){
            setVisilityOfLayout()
            getSharedPreference().saveString("WalletAmount",Amount)
            getSharedPreference().saveString("PayableAmount","0")
            _binding!!.payment.setOnClickListener {
                ContainerActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", "", OrderId, "app.tapho","1")
                activity?.finish()
            }

            _binding!!.pspAppsLayout.visibility = View.GONE
            _binding!!.payment.text = "PAY VIA TAPFO CARD"
            _binding!!.PaybleAmount.text = withSuffixAmount(Amount)
        }else if(cash_available.toString().toDouble()<Amount.toString().toDouble()){
            val PaybleAmount =Amount.toDouble()- cash_available.toString().toDouble()
            startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
            getSharedPreference().saveString("WalletAmount",cash_available.toString())
            getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())

            _binding!!.pspAppsLayout.visibility = View.VISIBLE
            _binding!!.payment.text = "PAY ${withSuffixAmount(PaybleAmount.toString())} VIA TAPFO CARD"
            _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
        } else{
            getSharedPreference().saveString("WalletAmount","0")
            startPaymentInitializ(Amount)
            _binding!!.pspAppsLayout.visibility = View.VISIBLE
            _binding!!.PaybleAmount.text = withSuffixAmount(Amount)
        }

        setAllTextAndIcons()
    }

    private fun setVisilityOfLayout() {
        _binding!!.nestedScrollView.visibility = View.VISIBLE
        _binding!!.linearLayout6.visibility = View.VISIBLE
        _binding!!.progress.visibility = View.GONE
        _binding!!.PSPApps.visibility = View.VISIBLE
        _binding!!.linearLayout7.visibility = View.VISIBLE
    }

    private fun setAllTextAndIcons() {
        _binding!!.apply {
            var walletBalance = cash_available
            Glide.with(requireContext()).load(operator_icon).circleCrop().into(opretorIcon)
            opretorName.text = operator_name
            numberandCircle.text = mobileNumber + " - " + circle_name
            rechargeAmount.text = withSuffixAmount(Amount)
            avilableBalance.text = getString(R.string.fopay_balance, withSuffixAmount(walletBalance))

        }
    }

    private fun startPaymentInitializ(PayableAmount : String) {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        Log.d("Current Date", charset.toString())
        var randomString = List(15) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyyyhhmmss")
        val currentDate = simpleDate.format(Date())
        Log.d("Current Date", currentDate)

        val FinalorderID = randomString + "" + "" + currentDate+"TAPFO"+getUserId()+"PY"
                Log.d("Current Date id", FinalorderID)
        viewModel.getInitiateTransaction(getUserId(), PayableAmount.toDouble(), FinalorderID, this, object : ApiListener<InitiateTransactionRes, Any?> {
                override fun onSuccess(t: InitiateTransactionRes?, mess: String?) {
                    t!!.let {
                        if (it.body.resultInfo.resultStatus.equals("S")) {
                            paymentProseccingAfterIntialising(it.body.txnToken, FinalorderID)
//                        ContainerActivity.openContainer(requireContext(),"SelectpaymentMethods",it.body.txnToken,orderID)
                        } else {
                            requireView().showShortSnack(it.body.resultInfo.resultMsg)
                        }
                    }
                }

            })
    }

    private fun paymentProseccingAfterIntialising(txnToken: String, orderID: String) {
        viewModel.getTransactionProcess(getUserId(), orderID, "UPI_INTENT", "", txnToken, this, object : ApiListener<TransactionProcessRes, Any?> {
                override fun onSuccess(t: TransactionProcessRes?, mess: String?) {
                    t.let {
                        val uri = it!!.body.deepLinkInfo.deepLink
                        val orderid = it.body.deepLinkInfo.orderId
                        DeeplInkURI = uri
                        OrderId = orderid
                        _binding!!.nestedScrollView.visibility = View.VISIBLE
                        _binding!!.linearLayout6.visibility = View.VISIBLE
                        _binding!!.progress.visibility = View.GONE
                        _binding!!.PSPApps.visibility = View.VISIBLE
                        setLayoutAsAAdapter()
                        // ContainerActivity.openContainer(requireContext(),"SelectpaymentMethods",uri,orderID,"")
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
                _binding!!.payment.setOnClickListener {
                    ContainerActivity.openContainerForPayment(requireContext(), "TranscationProcessingPage", DeeplInkURI, OrderId, data.toString(),"0")
                    activity?.finish()
                }

            }

        }).apply {
            for (i in upiApps.indices) {
                val p = upiApps[i]
                if (isAppInstalled(p) == true  &&isAppUpiReady(p)) {
                    addItem(PSPModelClass(p, false))
                }
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