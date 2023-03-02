package app.tapho.ui.tcash

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentWalletHistoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.tcash.adapter.Tcashbackverified_Adapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.URLDecoder

class Fragment_wallet_history : BaseFragment<FragmentWalletHistoryBinding>(),
    ApiListener<TCashDasboardRes, Any?>, RecyclerClickListener {
    private var walletAdapter: Tcashbackverified_Adapter? = null
    private var type: String? = ""
    var idCreatedAt=""
    var lifetimeEaning=""
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (activity is HomeActivity)
                    (activity as HomeActivity).addHome()
                else {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString("TYPE")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingwallet = FragmentWalletHistoryBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        bindingwallet!!.progress.visibility = View.VISIBLE
        init()
        bindingwallet!!.filterChip.setOnClickListener {
            openTimeDialog()
        }
        return bindingwallet?.root
    }


    private fun init() {
        bindingwallet!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        setRecycler()
    }

    private fun setRecycler() {
        walletAdapter = Tcashbackverified_Adapter(this)
        bindingwallet!!.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = walletAdapter
        }
        getData(TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(), getString(R.string.till_date))
    }

    fun getData(startDate: String, endDate: String, textTime: String) {
        bindingwallet!!.progress.visibility = View.VISIBLE
        bindingwallet!!.noTransaction.visibility = View.GONE
        walletAdapter?.clear()
        viewModel.getTCashDashboard(getUserId(), startDate, endDate,"2", this, this)
            bindingwallet!!.filterChip.text = textTime
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
            Fragment_wallet_history().apply {
                arguments = Bundle().apply { putString("TYPE", type) }
            }
    }

    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {

        t?.let {
            val VerifiedData: ArrayList<TCashDashboardData> = ArrayList()
            lifetimeEaning = it.lifetime_earning.toString()
            it.data!!.forEach {
                if (it.status!!.uppercase().equals("VERIFIED")) {
                    VerifiedData.add(it)
                } else if (it.status!!.uppercase().equals("VALIDATED")) {
                    VerifiedData.add(it)
                }
            }
            VerifiedData.let {it1->
                walletAdapter?.addAllItem(it1)
                bindingwallet!!.progress.visibility = View.GONE
                bindingwallet!!.noTransaction.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE
        //        bindingwallet!!.topbar.visibility = if (it1.isEmpty()) View.GONE else View.VISIBLE

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


}