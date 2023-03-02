package app.tapho.ui.activecashback.ActiveCashbackScreenNew

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentCashbackOrderBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.BaseFragment
import app.tapho.ui.PrivacyPolicyActivity
import app.tapho.ui.activecashback.adapter.OrdersAdapter
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CashbackOrderFragment  : BaseFragment<FragmentCashbackOrderBinding>(),
    RecyclerClickListener {
    private var res: WebTCashRes? = null
    private var mAdapter: OrdersAdapter? = null
    private var t: TCashDasboardRes? = null

    var lifetimeearing =0.0
    var pending =0.0
    var saleAmount =0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        orderBinding = FragmentCashbackOrderBinding.inflate(inflater, container, false)
        // getData1()
        statusBarColor(R.color.greenshade)
        statusBarTextBlack()
        orderBinding!!.mainLayout.visibility = View.GONE
        orderBinding!!.progress.visibility = View.VISIBLE
        orderBinding!!.backBtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        val data = activity?.intent?.getStringExtra(DATA)
        Gson().fromJson(data,TCashDasboardRes::class.java).let {
            this.t = it

        }

        getDataViewMOdel()
        Handler(Looper.getMainLooper()).postDelayed({
            orderBinding!!.mainLayout.visibility = View.VISIBLE
            orderBinding!!.progress.visibility = View.GONE
        },3000)

        return orderBinding?.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        res?.let {
            setData(it, 50)
        }
        setRecycler()
    }
    private fun getDataViewMOdel() {
        viewModel.getMiniAppTCash(AppSharedPreference.getInstance(requireContext()).getUserId(),
            activity?.intent?.getStringExtra(GAME_ID), this,
            object : ApiListener<WebTCashRes, Any?> {
                override fun onSuccess(t: WebTCashRes?, mess: String?) {
                    t?.let {
                        res=it
                        setData(it, 50)
                    }
                    setRecyclerData()
                }
            })
    }

    fun setData(res: WebTCashRes, timeout: Long) {
        this.res = res
        Handler(Looper.getMainLooper()).postDelayed({
             orderBinding!!.emptyTv.text = getString(R.string.make_your_first_move_to_start_earnings_extra_on_s, res.mini_app?.get(0)?.name)
        }, timeout)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CashbackOrderFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    private fun setRecyclerData() {
        var totalAmount:Int=0
        val mList = ArrayList<TCashDashboardData>()

        var Expense:Int?=null


        t?.let {
            it.data?.let { list ->
                list.forEach { d ->
                    if (d.offer_name == res?.mini_app?.get(0)?.name) {
                        mList.add(d)
                    }
                }
            }

        }
        if (mList.size.equals("0")){
            orderBinding!!.transactioncount.text =  "Transaction"
        }else{
            orderBinding!!.transactioncount.text = if(mList.size.equals(1)) mList.size.toString() + " Transaction" else mList.size.toString() + " Transactions"
        }

        mList.forEachIndexed { index, tCashDashboardData ->
            if (tCashDashboardData.status!!.uppercase() == "VERIFIED") {
                lifetimeearing =lifetimeearing.toDouble()+tCashDashboardData.user_commision!!.toDouble()

            } else if (tCashDashboardData.status!!.uppercase() == "VALIDATED") {
                lifetimeearing =lifetimeearing.toDouble()+tCashDashboardData.user_commision!!.toDouble()

            } else if (tCashDashboardData.status!!.uppercase() == "PENDING") {
                pending =pending.toDouble()+tCashDashboardData.user_commision!!.toDouble()
            }
        }
        mList.forEachIndexed { index, tCashDashboardData ->
            saleAmount = saleAmount + tCashDashboardData.sale_amount!!.toDouble()
        }
        if (pending==0.0){
            orderBinding!!.cashbackPercentage.text ="0.0%"
        }else{
            orderBinding!!.cashbackPercentage.text =String.format("%.2f", ((lifetimeearing+pending) / saleAmount * 100.00 ).toDouble()) + "%"
        }


        orderBinding!!.lifetimeearningamount.text = withSuffixAmount(lifetimeearing.toString())
        orderBinding!!.saleAmount.text = withSuffixAmount(saleAmount.toString())
        orderBinding!!.name.text=getString(R.string.you_have_0_is_pending_from_hammer,withSuffixAmount(pending.toString()),res!!.mini_app?.get(0)?.name)

        mAdapter?.addAllItem(mList)
        orderBinding!!.emptyTv.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE
        orderBinding!!.icRupee1.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE

    }



    override fun showLoader() {

        orderBinding!!.emptyTv.visibility = View.GONE
        orderBinding!!.icRupee1.visibility = View.GONE

    }

    override fun dismissLoader() {

        orderBinding!!.emptyTv.visibility = View.GONE
        orderBinding!!.icRupee1.visibility = View.GONE
    }

    private fun setRecycler() {
        mAdapter = OrdersAdapter(this)
        orderBinding!!.recycler.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = false
//                reverseLayout = true
            }
            adapter = mAdapter
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//        val newFragment: Fragment = NewOrderFragment.newInstance(data as TCashDashboardData?,true)
//        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.viewpagerActiveCashback, newFragment)?.commit()

        if (data is TCashDashboardData){
            PopUpBar(data)
        }
//
//        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//        transaction.replace(R.id.viewpagerActiveCashback, newFragment)
//        mAdapter!!.clear()
//        transaction.addToBackStack(null)
//        transaction.commit()

//        findNavController().navigate(R.id.action_orderDetailFragment, Bundle().apply {
//            putString("ORDER", Gson().toJson(data))
//        })
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


        date.text = parseDate3(data.date) +" "+ if (data.date.toString().length>18){
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
            startActivity(
                Intent(context, PrivacyPolicyActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("TYPE", "2")
                })
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun getValidationPeriod(data: TCashDashboardData): String {
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
        return ""
    }
}