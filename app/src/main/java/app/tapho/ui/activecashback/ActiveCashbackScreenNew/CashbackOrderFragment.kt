package app.tapho.ui.activecashback.ActiveCashbackScreenNew

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentCashbackOrderBinding

import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.BaseFragment
import app.tapho.ui.activecashback.adapter.OrdersAdapter
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.utils.MINI_APP_ID
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.gson.Gson


class CashbackOrderFragment  : BaseFragment<FragmentCashbackOrderBinding>(),
    ApiListener<TCashDasboardRes, Any?>,
    RecyclerClickListener {
    private var res: WebTCashRes? = null
    private var mAdapter: OrdersAdapter? = null
    private var t: TCashDasboardRes? = null
    var transactionAmt:String?=null
    var dataDashboard: TCashDashboardData?=null

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
        statusBarTextWhite()
        orderBinding!!.mainLayout.visibility = View.GONE
        orderBinding!!.progress.visibility = View.VISIBLE
        orderBinding!!.backBtn.setOnClickListener {
            activity?.onBackPressed()
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
            activity?.intent?.getStringExtra(MINI_APP_ID), this,
            object : ApiListener<WebTCashRes, Any?> {
                override fun onSuccess(t: WebTCashRes?, mess: String?) {
                    t?.let {
                        res=it
                        setData(it, 50)
                    }
                }
            })
    }

    fun setData(res: WebTCashRes, timeout: Long) {
        this.res = res

      //  Glide.with(requireContext()).load(res.mini_app!!.get(0).logo).into(orderBinding!!.logo)
        Handler(Looper.getMainLooper()).postDelayed({
             orderBinding!!.emptyTv.text = getString(R.string.make_your_first_move_to_start_earnings_extra_on_s, res.mini_app?.get(0)?.name)
            getData()
        }, timeout)

    }

    companion object {

        @JvmStatic
        fun newInstance(t: WebTCashRes?, showBack: Boolean) =
            CashbackOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(DATA, Gson().toJson(t))
                    putBoolean("SHOW_BACK", showBack)
                }
            }
    }
    private fun getData() {
        if (t != null) {
            setRecyclerData()
        } else {
            RequestViewModel().getTCashDashboard(
                getSharedPreference().getUserId(),
                TimePeriodDialog.getDate(1, -12),
                TimePeriodDialog.getCurrentDate(), this, this
            )
        }

    }

    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
        this.t = t
        setRecyclerData()

    }


    private fun setRecyclerData() {
        var totalAmount:Int=0
        val mList = ArrayList<TCashDashboardData>()

        var Expense:Int?=null


        t?.let {
            it.data?.let { list ->
                list.forEach { d ->
                    if (d.offer_name == res?.mini_app?.get(0)?.name) {
                        Log.d("Dashboard", it.verified.toString())
                        Log.d("Dashboard1", res?.mini_app?.get(0)?.name.toString())
                        Log.d("Dashboard2", d.offer_name.toString())
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
        Log.d("PendingBalnace",pending.toString())

        if (pending==0.0){
            orderBinding!!.cashbackPercentage.text ="0.0%"
        }else{
            orderBinding!!.cashbackPercentage.text =String.format("%.2f", ((lifetimeearing+pending) / saleAmount * 100.00 ).toDouble()) + "%"
        }


        orderBinding!!.lifetimeearningamount.text = withSuffixAmount(lifetimeearing.toString())
        orderBinding!!.saleAmount.text = withSuffixAmount(saleAmount.toString())
        orderBinding!!.name.text=getString(R.string.you_have_0_is_pending_from_hammer,withSuffixAmount(pending.toString()),res!!.mini_app?.get(0)?.name)

//        OrdersFragment.appNameMerchant =res?.mini_app?.get(0)?.name
//        OrdersFragment.numberOfOrder =mList.size.toString()
        mAdapter?.addAllItem(mList)
        orderBinding!!.emptyTv.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE
        orderBinding!!.icRupee1.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE

    }


    private fun cashbackRates() {
//        findNavController().navigate(
//            R.id.action_ordersFragment_to_cashbackRatesFragment,
//            Bundle().apply {
//                putString("DATA", Gson().toJson(res))
//            })


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
        val newFragment: Fragment = NewOrderFragment.newInstance(data as TCashDashboardData?,true)
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.viewpagerActiveCashback, newFragment)?.commit()

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
}