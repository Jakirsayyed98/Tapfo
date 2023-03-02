package app.tapho.ui.BuyVoucher.VoucherPayments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentInitiatePaymentForVoucherBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersData
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersDatabase
import app.tapho.ui.BuyVoucher.VoucherListViewModel.Data
import app.tapho.ui.BuyVoucher.adapter.voucherlist_paymentpage_adapter
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.Adapter.PSPModelClass
import app.tapho.ui.PaytmPaymentGateway.Adapter.SmartIntentPSPAdapter
import app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel.InitiateTransactionRes2
import app.tapho.ui.PaytmPaymentGateway.TransactionProcess.TransactionProcessRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.DATA
import app.tapho.utils.withSuffixAmount
import app.tapho.utils.withSuffixAmount3
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class InitiatePaymentForVoucherFragment : BaseFragment<FragmentInitiatePaymentForVoucherBinding>(){

    val BHIM_UPI = "in.org.npci.upiapp"
    val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
    val PHONE_PE = "com.phonepe.app"
    val PAYTM = "net.one97.paytm"

    val upiApps = listOf<String>(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI)
    var cash_available = ""
    var OrderId = ""
    var DeeplInkURI = ""
    var Amount = 0

    var Voucherlistadapter : voucherlist_paymentpage_adapter<VouchersData>?=null
    lateinit var database: VouchersDatabase
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
        _binding = FragmentInitiatePaymentForVoucherBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        database = VouchersDatabase.getDatabase(requireContext())
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.nestedScrollView.visibility = View.GONE
        _binding!!.linearLayout6.visibility = View.GONE
        getTcashData()
        getRoomDataBase()



        _binding!!.back.setOnClickListener {
            BackPressAction()
        }
        setLayoutData()
        onBackPressed()
        return _binding?.root
    }

    private fun getRoomDataBase() {
        database.getVouchersDatabase().getAllVouchersData().observe(requireActivity(), androidx.lifecycle.Observer {
            Voucherlistadapter!!.addAllItem(it)
            it.forEach {
                Amount +=it.PaybleAmount.toInt()
            }
        })
    }

    private fun getTcashData() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12),"2", TimePeriodDialog.getCurrentDate(),this,object :
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
        if (cash_available.toString().toDouble()>=Amount.toString().toDouble()){
            setVisilityOfLayout()
            getSharedPreference().saveString("WalletAmount",Amount.toString())
            getSharedPreference().saveString("PayableAmount","0")
            _binding!!.payment.setOnClickListener {
                VoucherTransactionProcessActivity.openContainerForPayment(requireContext(), "VoucherProcessTransaction", "", OrderId, "app.tapho","1")
                activity?.finish()
            }
            _binding!!.pspAppsLayout.visibility = View.GONE
            _binding!!.PaybleAmount.text = withSuffixAmount(Amount.toString())
            _binding!!.paybleAmount.text = withSuffixAmount(Amount.toString())
        }else if(cash_available.toString().toDouble()<Amount.toString().toDouble()){
            val PaybleAmount =Amount.toDouble()- cash_available.toString().toDouble()
            startPaymentInitializ(withSuffixAmount3(PaybleAmount.toString()).toString())
            getSharedPreference().saveString("WalletAmount",cash_available.toString())
            getSharedPreference().saveString("PayableAmount",PaybleAmount.toString())
            _binding!!.pspAppsLayout.visibility = View.VISIBLE
            _binding!!.PaybleAmount.text = withSuffixAmount(PaybleAmount.toString())
            _binding!!.paybleAmount.text = withSuffixAmount(PaybleAmount.toString())
        } else{
            getSharedPreference().saveString("WalletAmount","0")
            startPaymentInitializ(Amount.toString())
            _binding!!.pspAppsLayout.visibility = View.VISIBLE
            _binding!!.PaybleAmount.text = withSuffixAmount(Amount.toString())
            _binding!!.paybleAmount.text = withSuffixAmount(Amount.toString())
        }

        setAllTextAndIcons()
    }

    private fun setVisilityOfLayout() {
        _binding!!.nestedScrollView.visibility = View.VISIBLE
        _binding!!.linearLayout6.visibility = View.VISIBLE
        _binding!!.progress.visibility = View.GONE
        _binding!!.PSPApps.visibility = View.VISIBLE
    }

    private fun setAllTextAndIcons() {
        _binding!!.apply {
            val walletBalance = cash_available
            avilableBalance.text = getString(R.string.fopay_balance, withSuffixAmount(walletBalance))
        }
    }

    private fun startPaymentInitializ(PayableAmount : String) {
        val charset =  ('A'..'Z') + ('0'..'9')
        val randomString = List(5) { charset.random() }.joinToString("")
        val simpleDate = SimpleDateFormat("ddMMyyhhmm")
        val currentDate = simpleDate.format(Date())
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
        viewModel.getTransactionProcess(getUserId(), orderID, "UPI_INTENT", "", txnToken, this, object : ApiListener<TransactionProcessRes, Any?> {
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

    private fun setLayoutData() {
        Voucherlistadapter = voucherlist_paymentpage_adapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        _binding!!.VouchersData.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = Voucherlistadapter
        }
    }

    private fun setLayoutAsAAdapter() {
        val SmartIntentPSPAdapterdaa = SmartIntentPSPAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

//                _binding!!.payment.text =getString(R.string.pay_with_gpay_upi, withSuffixAmount(getSharedPreference().getString("PayableAmount")),type.toString())

                _binding!!.linearLayout7.visibility = View.VISIBLE
                _binding!!.payment.setOnClickListener {
                    VoucherTransactionProcessActivity.openContainerForPayment(requireContext(), "VoucherProcessTransaction",  DeeplInkURI, OrderId, data.toString(),"0")
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

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("InflateParams")
    private fun BackPressAction() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.paymentexit_bottomsheet1, null)
        dialog.setCancelable(false)

        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)
        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)


        exit.setOnClickListener {
            activity?.intent?.getStringExtra(DATA).let {
                val data = Gson().fromJson(it, Data::class.java)
                ContainerActivity.openContainer(requireContext(),"OpenVoucherDetailForBuy",data)
                activity?.finish()
            }
            GlobalScope.launch {
                database.getVouchersDatabase().deleteAll()
            }
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
            InitiatePaymentForVoucherFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}