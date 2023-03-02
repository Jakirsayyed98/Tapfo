package app.tapho.ui.tcash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentTcashHistoryDataBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.PrivacyPolicyActivity
import app.tapho.ui.tcash.adapter.TCashbackAdapter
import app.tapho.ui.tcash.adapter.TCashbackStatusAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcash.model.TransactionStatusModelCustome
import app.tapho.utils.*
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Fragment_Tcash_HistoryData : BaseFragment<FragmentTcashHistoryDataBinding>(),
    ApiListener<TCashDasboardRes, Any?>, RecyclerClickListener {
        private var mAdapter: TCashbackAdapter? = null

    private var type: String? = ""
    var lifetimeEaning = ""
    var transactiontype="1"
    val VerifiedData: ArrayList<TCashDashboardData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString("TYPE")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusBarColor(R.color.orange1)
        binding1 = FragmentTcashHistoryDataBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        binding1!!.progress.visibility = View.VISIBLE
        init()
        binding1!!.filterChip.setOnClickListener {
            openTimeDialog()
        }

        binding1!!.status.setOnClickListener {
            openStatusDialog()
        }

        return binding1?.root
    }

    private fun openStatusDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.transaction_status_change_dialog, null)
        dialog.setCancelable(true)
        var mAdapter: TCashbackStatusAdapter? = null
        val closeIv: ImageView = view.findViewById(R.id.closeIv)
        val recyclerstatus: RecyclerView = view.findViewById(R.id.recyclerstatus)

        mAdapter = TCashbackStatusAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is TransactionStatusModelCustome){
                    transactiontype = data.type
                    addDataInAdapter(transactiontype)
                    binding1!!.status.text = data.name
                    dialog.dismiss()
                }
            }
        })
        mAdapter.addItem(TransactionStatusModelCustome(getString(R.string.all), "0",false))
        mAdapter.addItem(TransactionStatusModelCustome(getString(R.string.pending), "1",false))
        mAdapter.addItem(TransactionStatusModelCustome(getString(R.string.verified), "2",false))
        mAdapter.addItem(TransactionStatusModelCustome(getString(R.string.rejected), "3",false))

        recyclerstatus.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = mAdapter
        }


        closeIv.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }


    private fun init() {
        binding1!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        setRecycler()
    }

    private fun setRecycler() {
        mAdapter = TCashbackAdapter(this)
        binding1!!.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        getData(
            TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),
            getString(R.string.till_date)
        )
    }

    fun getData(startDate: String, endDate: String, textTime: String) {
        binding1!!.progress.visibility = View.VISIBLE
        binding1!!.noTransaction.visibility = View.GONE

        mAdapter?.clear()
        viewModel.getTCashDashboard(getUserId(), startDate, endDate,"2", this, this)
        binding1!!.filterChip.text = textTime
        "$startDate / $endDate".also { }
    }

    private fun openTimeDialog() {
        TimePeriodDialog().apply {
            arguments = Bundle().apply {

            }
        }.show(childFragmentManager, "TimePeriodDialog")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Fragment_Tcash_HistoryData().apply {
                arguments = Bundle().apply {

                }
            }
    }


    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
        t?.let {
            mAdapter!!.clear()
            VerifiedData!!.clear()
            lifetimeEaning = it.lifetime_earning.toString()
            it.data!!.forEach {
                VerifiedData.add(it)
            }
            binding1!!.progress.visibility = View.GONE
            addDataInAdapter(transactiontype)

        }
    }

    private fun addDataInAdapter(tcashType:String) {

        mAdapter!!.clear()
            when(tcashType) {
                "0" -> {
                    val data: ArrayList<TCashDashboardData> = ArrayList()
                    VerifiedData.let {
                        data.addAll(it)
                    }
                    if (data.isNullOrEmpty()){
                        binding1!!.noTransaction.visibility = View.VISIBLE
                    }else{
                        binding1!!.noTransaction.visibility = View.GONE
                        mAdapter!!.addAllItem(data)
                    }
                }
                "1" -> {
                    val data: ArrayList<TCashDashboardData> = ArrayList()
                    VerifiedData.forEach {
                        if (it.status!!.uppercase().equals("PENDING")) {
                           data.add(it)
                        }
                    }
                    if (data.isNullOrEmpty()){
                        binding1!!.noTransaction.visibility = View.VISIBLE
                    }else{
                        binding1!!.noTransaction.visibility = View.GONE
                        mAdapter!!.addAllItem(data)
                    }
                }
                "2" -> {
                    val data: ArrayList<TCashDashboardData> = ArrayList()
                    VerifiedData.forEach {
                        if (it.status?.uppercase() == "VERIFIED" || it.status?.uppercase() == "VALIDATED") {
                            data.add(it)
                        }
                    }
                    if (data.isNullOrEmpty()){
                        binding1!!.noTransaction.visibility = View.VISIBLE
                    }else{
                        binding1!!.noTransaction.visibility = View.GONE
                        mAdapter!!.addAllItem(data)
                    }
                }
                "3"-> {
                    val data: ArrayList<TCashDashboardData> = ArrayList()
                    VerifiedData.forEach {
                        if (it.status!!.uppercase().equals("FAILED") || it.status?.uppercase() == "REJECTED") {
                            data.add(it)
                        }
                    }
                    if (data.isNullOrEmpty()){
                        binding1!!.noTransaction.visibility = View.VISIBLE
                    }else{
                        binding1!!.noTransaction.visibility = View.GONE
                        mAdapter!!.addAllItem(data)
                    }
                }
                else -> {
                    val data: ArrayList<TCashDashboardData> = ArrayList()
                    VerifiedData.let {
                        data.addAll(it)
                    }

                    if (data.isNullOrEmpty()){
                        binding1!!.noTransaction.visibility = View.VISIBLE
                    }else{
                        binding1!!.noTransaction.visibility = View.GONE
                        mAdapter!!.addAllItem(data)
                    }
                }


            }

    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "detail" && data is TCashDashboardData) {
            PopUpBar(data)
        }
    }

    private fun PopUpBar(data: TCashDashboardData) {
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
        val verifedConformation: LottieAnimationView = view.findViewById(R.id.verifedConformation)
        val careditedToWallet: LottieAnimationView = view.findViewById(R.id.careditedToWallet)


        when (data.status?.uppercase()) {
            "VERIFIED" -> {
                verifedConformation.setImageResource(R.drawable.verifiedok)
                careditedToWallet.setImageResource(R.drawable.verifiedok)
                statusTextTv.text =  "Congrates, Cashback  has been verified from "+data.offer_name
                statusTextTv.setTextColor(Color.parseColor("#008D3A"))
                percentage1.setTextColor(Color.parseColor("#008D3A"))
            }
            "VALIDATED" -> {
                verifedConformation.setImageResource(R.drawable.verifiedok)
                careditedToWallet.setImageResource(R.drawable.verifiedok)
                statusTextTv.text = "Congrates, Cashback  has been verified from "+data.offer_name
                statusTextTv.setTextColor(Color.parseColor("#008D3A"))
                percentage1.setTextColor(Color.parseColor("#008D3A"))
            }
            "PENDING" -> {
                statusTextTv.text = getString(R.string.cashback_pending_from2,"pending",data.offer_name)
                statusTextTv.setTextColor(Color.parseColor("#FF7C2B"))
                percentage1.setTextColor(Color.parseColor("#FF7C2B"))
            }
            "FAILED" -> {
               verifedConformation.setImageResource(R.drawable.rejectedok)
                careditedToWallet.setImageResource(R.drawable.rejectedok)
                statusTextTv.text = getString(R.string.cashback_pending_from2,"rejected",data.offer_name)
                statusTextTv.setTextColor(Color.parseColor("#EF5350"))
                percentage1.setTextColor(Color.parseColor("#EF5350"))

            }
            "REJECTED" -> {
                verifedConformation.setImageResource(R.drawable.rejectedok)
                careditedToWallet.setImageResource(R.drawable.rejectedok)
                statusTextTv.text = getString(R.string.cashback_pending_from2,"rejected",data.offer_name)
                statusTextTv.setTextColor(Color.parseColor("#EF5350"))
                percentage1.setTextColor(Color.parseColor("#EF5350"))

            }

        }


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

    private fun getValidationPeriod(data: TCashDashboardData): String {
        if (data.date!!.toString().length<12){
            try {
                SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(data.date)?.let {
                    return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(
                        Calendar.getInstance()
                            .apply {
                                time = it
                                add(Calendar.DATE, 60)
                            }.time)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            try {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(data.date)?.let {
                    return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(
                        Calendar.getInstance()
                            .apply {
                                time = it
                                add(Calendar.DATE, 60)
                            }.time)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return ""
    }


}