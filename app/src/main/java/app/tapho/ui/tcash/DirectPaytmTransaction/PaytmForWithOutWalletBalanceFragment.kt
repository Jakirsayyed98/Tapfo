package app.tapho.ui.tcash.DirectPaytmTransaction

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentPaytmForWithOutWalletBalanceBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.PaytmPaymentGateway.Adapter.PSPModelClass
import app.tapho.ui.PaytmPaymentGateway.Adapter.SmartIntentPSPAdapter
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes2
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PaytmForWithOutWalletBalanceFragment : BaseFragment<FragmentPaytmForWithOutWalletBalanceBinding>() {

    val BHIM_UPI = "in.org.npci.upiapp"
    val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
    val PHONE_PE = "com.phonepe.app"
    val PAYTM = "net.one97.paytm"
//    val AmazonPay = "in.amazon.mShop.android.shopping"


    val upiApps = listOf<String>(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI)
    var PSPModelClass: ArrayList<PSPModelClass> = ArrayList()
    private var SmartIntentPSPAdapterdaa: SmartIntentPSPAdapter? = null
    var OrderId = ""
    var DeeplInkURI = ""
    var AddWalletAmount = ""



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
        _binding = FragmentPaytmForWithOutWalletBalanceBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        _binding!!.progress.visibility = View.VISIBLE
        //     _binding!!.PSPApps.visibility = View.GONE
        _binding!!.linearLayout7.visibility = View.GONE
        _binding!!.nestedScrollView.visibility = View.GONE
        _binding!!.linearLayout6.visibility = View.GONE

        AddWalletAmount = getSharedPreference().getString("AddWalletAmount").toString()
        getAndSetAllData()
        _binding!!.rechargeAmount.text = withSuffixAmount(AddWalletAmount)

        _binding!!.back.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        onBackPressed()
        return _binding?.root
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
            activity?. onBackPressedDispatcher?.onBackPressed()
            activity?.finish()
        }


        app_continue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()


    }

    private fun getAndSetAllData() {
            startPaymentInitializ(AddWalletAmount)
            _binding!!.pspAppsLayout.visibility = View.VISIBLE


        val pm = requireContext().getPackageManager()
        Glide.with(requireContext()).load(pm.getApplicationIcon("app.tapho")).circleCrop().into( _binding!!.opretorIcon)
    }



    private fun startPaymentInitializ(PayableAmount : String) {
        val charset =  ('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMyyhhmm")
        val currentDate = simpleDate.format(Date())
        val FinalorderID = randomString  + currentDate+"T"+getUserId()+"PY"
        viewModel.getInitiateTransaction(getUserId(), PayableAmount.toDouble(), FinalorderID, this, object :
            ApiListener<InitiateTransactionRes2, Any?> {
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

    private fun paymentProseccingAfterIntialising(txnToken: String, orderID: String) {
        viewModel.getTransactionProcess(getUserId(), orderID, "UPI_INTENT", "", txnToken, this, object :
            ApiListener<TransactionProcessRes, Any?> {
            override fun onSuccess(t: TransactionProcessRes?, mess: String?) {
                t!!.data.let {
                    val uri = it.body.deepLinkInfo.deepLink
                    val orderid = it.body.deepLinkInfo.orderId
                    DeeplInkURI = uri
                    OrderId = orderid
                    _binding!!.nestedScrollView.visibility = View.VISIBLE
                    _binding!!.linearLayout6.visibility = View.VISIBLE
                    _binding!!.progress.visibility = View.GONE
                    _binding!!.PSPApps.visibility = View.VISIBLE
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
                _binding!!.linearLayout7.visibility = View.VISIBLE
//                _binding!!.payment.
                _binding!!.payment.text = getString(R.string.pay_with_gpay_upi,withSuffixAmount(AddWalletAmount),type.toString())
                _binding!!.payment.setOnClickListener {
                    StartPaymentprocessingActivity.openContainerForPayment(requireContext(), "PaytmWithoutWalletTranscationProcessingPage", DeeplInkURI, OrderId, data.toString(),"0")
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

            if (this.list.isNullOrEmpty()){
                Toast.makeText(requireContext(),"PSP Apps not found", Toast.LENGTH_LONG).show()
            }
        }
        _binding!!.PSPApps.apply {
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = SmartIntentPSPAdapterdaa
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
            PaytmForWithOutWalletBalanceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}