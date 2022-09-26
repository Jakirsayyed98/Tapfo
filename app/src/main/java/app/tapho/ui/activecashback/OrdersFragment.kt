package app.tapho.ui.activecashback

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentOrdersBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.BaseFragment
import app.tapho.ui.activecashback.adapter.OrdersAdapter
import app.tapho.ui.home.GameFragment
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.DATA
import com.google.gson.Gson

class OrdersFragment : BaseFragment<FragmentOrdersBinding>(), ApiListener<TCashDasboardRes, Any?>,
    RecyclerClickListener {
    private var res: WebTCashRes? = null
    private var mAdapter: OrdersAdapter? = null
    private var t: TCashDasboardRes? = null
    var transactionAmt:String?=null
    var dataDashboard: TCashDashboardData?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
       // getData1()
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        res?.let {
            setData(it, 50)
        }
        setRecycler()
    }
    fun setData(res: WebTCashRes, timeout: Long) {
        this.res = res
        Handler(Looper.getMainLooper()).postDelayed({
            binding.emptyTv.text = getString(R.string.your_earnings_appear_here, res.mini_app?.get(0)?.name)
//            binding.cashbackRatesTv.text =
//                getString(R.string.cashback_rates_/*, res.mini_app?.get(0)?.name*/)
            binding.tcTv.text = getString(R.string.check_cashback_rates, res.mini_app?.get(0)?.name)

            binding.reCashbackRates1.setOnClickListener {
                cashbackRates()
            }
            getData()
        }, timeout)

    }
    /*
//    // 30/03/2022 start
//    private fun getData1() {
//        Gson().fromJson(arguments?.getString(DATA), WebTCashRes::class.java)?.let {
//            setData12(it,300)
//        }
//    }
//    fun setData12(res: WebTCashRes, timeout: Long) {
//        this.res = res
//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.emptyTv.text =
//                getString(R.string.your_earnings_appear_here, res.mini_app?.get(0)?.name)
////            binding.cashbackRatesTv.text =
////                getString(R.string.cashback_rates_/*, res.mini_app?.get(0)?.name*/)
//            //  binding.tcTv.text = getString(R.string.check_cashback_rates, res.mini_app?.get(0)?.name)
//            binding.tcTv.text = getString(R.string.check_swiggy_s_cash, res.mini_app?.get(0)?.name)
//            binding.reCashbackRates1.setOnClickListener {
//                cashbackRates()
//            }
//            //getData()
//        }, timeout)
//
//    }
//    // 30/03/2022 end
*/
    fun setData1(res: WebTCashRes, timeout: Long) {
        this.res = res
        Handler(Looper.getMainLooper()).postDelayed({
            binding.emptyTv.text =
                getString(R.string.your_earnings_appear_here, res.mini_app?.get(0)?.name)
//            binding.cashbackRatesTv.text =
//                getString(R.string.cashback_rates_/*, res.mini_app?.get(0)?.name*/)
            //  binding.tcTv.text = getString(R.string.check_cashback_rates, res.mini_app?.get(0)?.name)
            binding.tcTv.text = getString(R.string.check_swiggy_s_cash, res.mini_app?.get(0)?.name)
            binding.reCashbackRates1.setOnClickListener {
                cashbackRates()
            }
            //getData()
        }, timeout)

    }


    companion object {

        var numberOfOrder:String?=null
        var appNameMerchant:String?=null
        @JvmStatic
        fun newInstance(t:String/*t: WebTCashRes?*/, showBack: Boolean) =
            OrdersFragment().apply {
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
                               Log.d("Dashboard",it?.verified.toString())
                                mList.add(d)
                            }
                        }
            }

        }
        appNameMerchant=res?.mini_app?.get(0)?.name
        numberOfOrder=mList.size.toString()
        mAdapter?.addAllItem(mList)
        binding.emptyTv.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE
        binding.icRupee1.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE
        binding.progressBar.visibility = View.GONE

    }


    private fun cashbackRates() {
//        findNavController().navigate(
//            R.id.action_ordersFragment_to_cashbackRatesFragment,
//            Bundle().apply {
//                putString("DATA", Gson().toJson(res))
//            })


    }

    override fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyTv.visibility = View.GONE
        binding.icRupee1.visibility = View.GONE

    }

    override fun dismissLoader() {
        binding.progressBar.visibility = View.GONE
        binding.emptyTv.visibility = View.GONE
        binding.icRupee1.visibility = View.GONE
    }

    private fun setRecycler() {
        mAdapter = OrdersAdapter(this)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = false
                reverseLayout = true
            }
            adapter = mAdapter
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//        findNavController().navigate(R.id.action_orderDetailFragment, Bundle().apply {
//            putString("ORDER", Gson().toJson(data))
//        })
    }


}