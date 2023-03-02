package app.tapho.ui.tcash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTcashrewardsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PrivacyPolicyActivity
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.Data
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.ui.tcash.adapter.TCashbackCategoryAdapter
import app.tapho.ui.tcash.adapter.TcashbackOnlyWalletTransaction_Adapter
import app.tapho.ui.tcash.adapter.Tcashbacktransaction_Adapter
import app.tapho.ui.tcash.adapter.Tcashbackwallettransaction_Adapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcash.model.TransactionStatusModelCustome
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_payment_status_screen.*
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TcashrewardsFragment : BaseFragment<FragmentTcashrewardsBinding>(),RecyclerClickListener {
    private var mAdapter: Tcashbacktransaction_Adapter? = null
    var walletAdapter: Tcashbackwallettransaction_Adapter? = null
    var tcashdashboard : TCashDasboardRes? = null
    var screenpointType=""
    var title=""
    var lifetimeEaning=""
    var Txntype = ""
    var OpretorList : ArrayList<Data> = ArrayList()
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
        _binding = FragmentTcashrewardsBinding.inflate(inflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()

        screenpointType = activity?.intent?.getStringExtra("pointType").toString()
        title = activity?.intent?.getStringExtra(TITLE).toString()
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.mainLayout.visibility = View.GONE
        getdatafromapi()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.filterChip.setOnClickListener {
            openTimeDialog()
        }


        return _binding?.root
    }



    private fun getdatafromapi() {
        setRecycler()
        setWallettransaction()
        getData(TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(), getString(R.string.till_date))
    }

    private fun setWallettransaction() {
        walletAdapter = Tcashbackwallettransaction_Adapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })

        _binding!!.WalletTransactionRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = walletAdapter
        }
    }

    fun getData(startDate: String, enddate: String, tilldate: String) {
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.title.text = title
        mAdapter!!.clear()

        val data = activity?.intent?.getStringExtra(DATA)

        if (!data.isNullOrEmpty()){

            val tcash = Gson().fromJson(data,TCashDasboardRes::class.java)
            tcash!!.let {
                it.let {
                    lifetimeEaning=it!!.lifetime_earning.toString()
                    _binding!!.filterChip.text = tilldate
                    tcashdashboard = it
                    setAlltextData(it)
                }
            }
        }else{
            viewModel.getTCashDashboard(getUserId(),startDate, enddate,screenpointType,this,object : ApiListener<TCashDasboardRes,Any?>{
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t.let {
                        lifetimeEaning=it!!.lifetime_earning.toString()
                        _binding!!.filterChip.text = tilldate
                        tcashdashboard = it
                        setAlltextData(it)
                    }
                }
            })
        }


    }



    private fun setRecycler() {
        mAdapter = Tcashbacktransaction_Adapter(this)
        _binding!!.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }


    private fun setAlltextData(it:TCashDasboardRes) {


        when(screenpointType){
            "0"->{
                mAdapter!!.clear()
                walletAdapter!!.clear()
                _binding!!.recentLayout.visibility = View.VISIBLE
                _binding!!.finalAmount.text = withSuffixAmount(it.pending.toString())
                _binding!!.rewardamount.text = withSuffixAmount(it.lifetime_earning.toString())
                _binding!!.discription.text ="all pending Cashback & rewards will be validated on terms & conditions"
                _binding!!.rewardandstatus.text ="Earned till date:"
                _binding!!.image.setImageResource(R.drawable.pending_layout_icon)

                _binding!!.historyBtn.text = "See history"
                _binding!!.recyclerCashback.visibility = View.VISIBLE
                _binding!!.WalletTransactionRV.visibility = View.GONE
                val title1 = "Waiting for validation"
                val discription="your outstanding/pending cashback from ordered merchants will be validated on their return, refund & cancellation policies. Generally, it takes 60-90 days to validate the pending cashback from the rewarded date."
                val discription1="Pending cashback will be credited to your wallet on your next recharge & bill payment."
                _binding!!.info.setOnClickListener {
                    pendinglayoutdetail(title1,discription,discription1)
                }

                _binding!!.historyBtn.setOnClickListener {
                    ContainerActivity.openContainer(requireContext(), "transactionHistory", "")
                }

                _binding!!.progress.visibility = View.GONE
                _binding!!.mainLayout.visibility = View.VISIBLE

                _binding!!.swithtoanother.setOnClickListener {
                    screenpointType = "1"
                    title = "Total Rewards"
                    getData(TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(), getString(R.string.till_date))

                }
                val pendingdata :ArrayList<TCashDashboardData> = ArrayList()
                it.data!!.forEach {
                    if (it.status.equals("pending")){
                        pendingdata.add(it)
                    }
                }

                it.txn.let {

                    walletAdapter!!.addAllItem(it)
                }
                if (pendingdata.isNullOrEmpty()){
                    _binding!!.noTransaction.visibility = View.VISIBLE
                }else{
                    _binding!!.noTransaction.visibility = View.GONE
                }

//                if (pendingdata.isNullOrEmpty()){
//                    _binding!!.recentLayout.visibility = View.GONE
//                }
                setCategoryLayout("0",pendingdata)
                mAdapter?.addAllItem(/*if (pendingdata.size>4) pendingdata.subList(0,4) else */pendingdata)
            }


            "1"->{
                mAdapter!!.clear()
                walletAdapter!!.clear()
                _binding!!.recyclerCashback.visibility = View.VISIBLE
                _binding!!.WalletTransactionRV.visibility = View.GONE
                _binding!!.recentLayout.visibility = View.VISIBLE
                _binding!!.finalAmount.text = withSuffixAmount(it.lifetime_earning.toString())
                _binding!!.rewardamount.text = withSuffixAmount(it.pending.toString())
                _binding!!.discription.text ="all validated Cashback & rewards are now instantly credited to your wallet"
                _binding!!.rewardandstatus.text ="Pending rewards:"
                _binding!!.image.setImageResource(R.drawable.totalrewards_icon)
                _binding!!.historyBtn.setOnClickListener {
                    ContainerActivity.openContainer(requireContext(), "AvailableBalance", "")
                }


                val title1 = "Rewards earned till date"
                val discription="This is your pure earned reward from Tapfo and it has been credited to your TapfoPay Wallet for redemption."
                val discription1=""

                _binding!!.info.setOnClickListener {
                    infoterms(title1,discription)
                }

                _binding!!.historyBtn.text = "See history"
                _binding!!.progress.visibility = View.GONE
                _binding!!.mainLayout.visibility = View.VISIBLE

                _binding!!.swithtoanother.setOnClickListener {
                    screenpointType = "0"
                    title = "Pending Rewards"
                    getData(TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(), getString(R.string.till_date))
                }

                val pendingdata :ArrayList<TCashDashboardData> = ArrayList()
                it.data!!.forEach {
                    if (it.status!!.equals("verified")){
                        pendingdata.add(it)
                    }else if ((it.status!!.equals("validated"))){
                        pendingdata.add(it)
                    }
                }
                if (pendingdata.isNullOrEmpty()){
                    _binding!!.noTransaction.visibility = View.VISIBLE
                }else{
                    _binding!!.noTransaction.visibility = View.GONE
                }

                it.txn.forEach {
                    if (it.type.equals(Txntype)){
                         walletAdapter!!.addItem(it)
                    }
                }

//                if (pendingdata.isNullOrEmpty()){
//                    _binding!!.recentLayout.visibility = View.GONE
//                }
                setCategoryLayout("1",pendingdata)
                mAdapter?.addAllItem(pendingdata)
            }


        }
    }



    private fun pendinglayoutdetail(title1: String, discription: String,discription1: String) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.cashback_terms_popup1, null)
        dialog.setCancelable(true)
        val gotit: AppCompatButton = view.findViewById(R.id.gotit)
        val discriptionTV: TextView = view.findViewById(R.id.discriptionTV2)
        val discription1TV: TextView = view.findViewById(R.id.discriptionTV1)

        discriptionTV.text = discription
        discription1TV.text = discription1

        gotit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun openTimeDialog() {
        TimePeriodDialog().apply {
            arguments = Bundle().apply {

            }
        }.show(childFragmentManager, "TimePeriodDialog")
    }

    private fun setCategoryLayout(types:String,pendingdata: ArrayList<TCashDashboardData>) {
        val tabAdapter = TCashbackCategoryAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is TransactionStatusModelCustome){
                    if(data.type.equals("1")){
                        _binding!!.recyclerCashback.visibility = View.GONE
                        _binding!!.WalletTransactionRV.visibility = View.VISIBLE
                        Txntype = "7"
                        setLayoutData(types)
                    }else if(data.type.equals("2")){
                        _binding!!.recyclerCashback.visibility = android.view.View.GONE
                        _binding!!.WalletTransactionRV.visibility = android.view.View.VISIBLE
                        Txntype = "4"
                        setLayoutData(types)
                    }else if(data.type.equals("3")){
                        _binding!!.recyclerCashback.visibility = View.GONE
                        _binding!!.WalletTransactionRV.visibility = View.VISIBLE
                        Txntype = "5"
                        setLayoutData(types)
                    }else{
                        _binding!!.recyclerCashback.visibility = View.VISIBLE
                        _binding!!.WalletTransactionRV.visibility = View.GONE
                        _binding!!.noTransaction.visibility = if (pendingdata.isNullOrEmpty())  View.VISIBLE else View.GONE
                    }

                }
            }
        }).apply {
            addItem(TransactionStatusModelCustome("Mini Save", "0",true))
            addItem(TransactionStatusModelCustome("Recharge", "1",false))
            addItem(TransactionStatusModelCustome("Referral", "2",false))
            addItem(TransactionStatusModelCustome("Gift cards", "3",false))
        }


        _binding!!.recyclerstatus.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = tabAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setLayoutData(types : String) {
        walletAdapter!!.clear()
        val listd : ArrayList<Txn> = ArrayList()
        tcashdashboard!!.txn.forEach {
            if (it.type.equals(Txntype)){
                if (it.cashback.isNullOrEmpty().not()){
                    if ( it.cashback.toDouble() > 0){
                        listd.add(it)
                    }
                }
            }
        }
        if (listd.isNullOrEmpty()){
            _binding!!.noTransaction.visibility = View.VISIBLE
        }else{
            _binding!!.noTransaction.visibility = View.GONE
        }
        walletAdapter!!.addAllItem(listd)
        walletAdapter!!.notifyDataSetChanged()
    }

    private fun infoterms(title: String, discription: String) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.cashback_terms_popup, null)
        dialog.setCancelable(true)
        val gotit: AppCompatButton = view.findViewById(R.id.gotit)
        val titleTV: TextView = view.findViewById(R.id.titleTV)
        val discriptionTV: TextView = view.findViewById(R.id.discriptionTV)
        val terms: TextView = view.findViewById(R.id.terms)

        titleTV.text= title
        discriptionTV.text= discription


        gotit.setOnClickListener {
            dialog.dismiss()
        }
        terms.setOnClickListener {
            startActivity(Intent(context, PrivacyPolicyActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("TYPE", "2")
                })
        }
        dialog.setContentView(view)
        dialog.show()
    }


    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is TCashDashboardData) {
                if (type.equals("verified")){
                    verifiedpopup(data)
                }else{
                    pendingpopup(data)
                }

//            startActivity(Intent(context, tcashback_detail_Activity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                putExtra(DATA, Gson().toJson(data))
//            })
        }
    }

    private fun pendingpopup(data: TCashDashboardData) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.casback_status_screen_dialog, null)
        dialog.setCancelable(true)

        val cashbackAmt = data.user_commision.toString()
        val transactionAmt = data.sale_amount.toString()
        val percentage = cashbackAmt.toDouble() / transactionAmt.toDouble() * 100
        val finalValue = String.format("%.2f", percentage).toDouble()

        val icon: ImageView = view.findViewById(R.id.icon)
        val earned: TextView = view.findViewById(R.id.earned)
        val satorename: TextView = view.findViewById(R.id.satorename)
        val orderID: TextView = view.findViewById(R.id.orderID)
        val orderamount: TextView = view.findViewById(R.id.orderamount)
        val date: TextView = view.findViewById(R.id.date)
        val percentage1: TextView = view.findViewById(R.id.percentage)
        val activeTv: TextView = view.findViewById(R.id.activeTv)
        val active2: TextView = view.findViewById(R.id.active2)
        val active3: TextView = view.findViewById(R.id.active3)
        val statusTextTv: TextView = view.findViewById(R.id.statusTextTv)
        val dateofvalid: TextView = view.findViewById(R.id.dateofvalid)
        val termsandCondition: TextView = view.findViewById(R.id.termsandCondition)
        val openWeb: LinearLayout = view.findViewById(R.id.openWebbtn)


        date.text = parseDate3(data.date)+" "+ if (data.date.toString().length>18){
            parsetime3(data.date)
        } else {
            ""
        }

        dateofvalid.text ="Validation period over till "+getValidationPeriod(data)

        percentage1.text = finalValue.toString()+"%"
        earned.text = withSuffixAmount(data.user_commision) +"  Cashback\nPending from "+data.offer_name
        orderamount.text = withSuffixAmount(data.sale_amount)
        satorename.text = "Cashback Store : "+data.offer_name
        statusTextTv.text = getString(R.string.cashback_pending_from,data.offer_name)
        activeTv.text = getString(R.string.your_transcation_has_been,data.offer_name)
        active2.text = getString(R.string.cashback_has_been_released,data.offer_name)
        active3.text = getString(R.string.swiggy_outstanding_cashback_for_validation,data.offer_name)
//        open.text = "Open "+data.offer_name
        orderID.text = "ID: "+data.trans_id

        Glide.with(requireContext()).load(data.image).circleCrop().into(icon)

        openWeb.setOnClickListener {
            dialog.dismiss()
        }
        termsandCondition.setOnClickListener {
            startActivity(Intent(context, PrivacyPolicyActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("TYPE", "2")
                })
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun verifiedpopup(data: TCashDashboardData) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.verified_cashback_screen, null)
        dialog.setCancelable(true)

        val cashbackAmt = data.user_commision.toString()
        val transactionAmt = data.sale_amount.toString()
        val percentage = cashbackAmt.toDouble() / transactionAmt.toDouble() * 100
        val finalValue = String.format("%.2f", percentage).toDouble()

        val icon: ImageView = view.findViewById(R.id.icon)
        val earned: TextView = view.findViewById(R.id.earned)
        val satorename: TextView = view.findViewById(R.id.satorename)
        val orderID: TextView = view.findViewById(R.id.orderID)
        val orderamount: TextView = view.findViewById(R.id.orderamount)
        val open: TextView = view.findViewById(R.id.open)
        val cashback: TextView = view.findViewById(R.id.cashback)
        val lifetimeEaningt: TextView = view.findViewById(R.id.lifetimeEarning)
        val date: TextView = view.findViewById(R.id.date)
        val percentage1: TextView = view.findViewById(R.id.percentage)
        val openWeb: LinearLayout = view.findViewById(R.id.openWebbtn)


        date.text = parseDate3(data.date)+" "+ if (data.date.toString().length>18){
            parsetime3(data.date)
        } else {
            ""
        }

        percentage1.text = finalValue.toString()+"%"
        earned.text = withSuffixAmount(data.user_commision) +"  Cashback Earned"
        orderamount.text = withSuffixAmount(data.sale_amount)
        lifetimeEaningt.text = withSuffixAmount(lifetimeEaning)
        satorename.text = "Cashback Store : "+data.offer_name
//        open.text = "Open "+data.offer_name
        orderID.text = "ID: "+data.trans_id
        cashback.text = URLDecoder.decode(data.cashback, "UTF-8")

        Glide.with(requireContext()).load(data.image).circleCrop().into(icon)

        openWeb.setOnClickListener {
            dialog.dismiss()
//            ActiveCashbackForWebActivity.openWebView(requireContext(),data.url,"",data.image,data.tcash,data.is_favourite,data.cashback,"","1",data.offer_name)
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun getValidationPeriod(data: TCashDashboardData): String {
        try {
            if (data.date.toString().length>18){
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(data.date)?.let {
                    return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(
                        Calendar.getInstance()
                            .apply {
                                time = it
                                add(Calendar.DATE, 60)
                            }.time)
                }
            }else{
                SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(data.date)?.let {
                    return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(
                        Calendar.getInstance()
                            .apply {
                                time = it
                                add(Calendar.DATE, 60)
                            }.time)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TcashrewardsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}