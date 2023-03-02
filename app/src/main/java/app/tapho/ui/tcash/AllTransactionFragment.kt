package app.tapho.ui.tcash


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
import app.tapho.databinding.FragmentAllTransactionBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.tcash.adapter.TCashbackCategoryAdapter
import app.tapho.ui.tcash.adapter.TCashbackStatusAdapter
import app.tapho.ui.tcash.adapter.TcashbackOnlyWalletTransaction_Adapter
import app.tapho.ui.tcash.adapter.Tcashbackverified_Adapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcash.model.TransactionStatusModelCustome
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import java.net.URLDecoder

class AllTransactionFragment : BaseFragment<FragmentAllTransactionBinding>(),RecyclerClickListener {

    var walletAdapter: TcashbackOnlyWalletTransaction_Adapter? = null
    var walletAdapterV: Tcashbackverified_Adapter? = null

    val TXNData: ArrayList<Txn> = ArrayList()
    var Transaction_type=""
    var lifetimeEaning=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTransactionBinding.inflate(layoutInflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
        Transaction_type = activity?.intent?.getStringExtra("Tcash_type").toString()
        _binding!!.textView26.text  = activity?.intent?.getStringExtra(TITLE).toString()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.filterChip.setOnClickListener {
            openTimeDialog()
        }

        setRecycler()
        setRecyclerForMini()
        activity?.intent?.getStringExtra(DATA).let {
           val data  = Gson().fromJson(it,TCashDasboardRes::class.java)
            lifetimeEaning = data.lifetime_earning.toString()
           setLayoutData(data)
       }




        return _binding?.root
    }

    private fun openTimeDialog() {
        TimePeriodDialog().apply {
            arguments = Bundle().apply {

            }
        }.show(childFragmentManager, "TimePeriodDialog")
    }


    fun getData(startDate: String, endDate: String, textTime: String) {
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
        walletAdapter?.clear()
        TXNData.clear()
        viewModel.getTCashDashboard(getUserId(), startDate, endDate,"1", this, object : ApiListener<TCashDasboardRes, Any?> {
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t.let {
                        TXNData.addAll(it!!.txn)
//                        _binding!!.mainLayout.visibility = View.VISIBLE
//                        _binding!!.progress.visibility = View.GONE

                        setLayoutData(it)

                        if (it.txn.isEmpty().not()) {
                            _binding!!.noTransaction.visibility = View.GONE
                        } else {
                            _binding!!.noTransaction.visibility = View.VISIBLE
                        }
                    }
                }
            })
        _binding!!.filterChip.text = textTime
        "$startDate / $endDate".also { }
    }

    private fun setLayoutData(it: TCashDasboardRes) {
        walletAdapter!!.clear()
        walletAdapterV!!.clear()
        _binding!!.mainLayout.visibility = View.VISIBLE
        _binding!!.progress.visibility = View.GONE
        when (Transaction_type){
            "Recharge"->{
                val txn: ArrayList<Txn> = ArrayList()
                it.txn.forEach {
                    if (it.type.equals("3")){
                       txn.add(it)
                    }
                }
                walletAdapter!!.addAllItem(txn)
                _binding!!.transaction.visibility = View.VISIBLE
                _binding!!.transactionMinis.visibility = View.GONE
                _binding!!.noTransaction.visibility = if (txn.isEmpty()) View.VISIBLE else View.GONE
            }
            "walletTransaction"->{
                   walletAdapter!!.addAllItem(it.txn)
                _binding!!.transaction.visibility = View.VISIBLE
                _binding!!.transactionMinis.visibility = View.GONE
                _binding!!.noTransaction.visibility = if (it.txn.isEmpty()) View.VISIBLE else View.GONE
            }
            "Minisave"->{
                val VerifiedData: ArrayList<TCashDashboardData> = ArrayList()
                it.data!!.forEach {
                    if (it.status!!.uppercase().equals("VERIFIED")) {
                        VerifiedData.add(it)
                    } else if (it.status!!.uppercase().equals("VALIDATED")) {
                        VerifiedData.add(it)
                    }
                }
                VerifiedData.let {it1->
                    walletAdapterV?.addAllItem(it1)
                    _binding!!.transaction.visibility = View.GONE
                    _binding!!.transactionMinis.visibility = View.VISIBLE
                    _binding!!.noTransaction.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE
                }

            }
            "Cashback"->{
                val txn: ArrayList<Txn> = ArrayList()
                it.txn.forEach {
                    if (it.type.equals("7")){
                       txn.add(it)
                    }
                }

                walletAdapter!!.addAllItem(txn)
                _binding!!.transaction.visibility = View.VISIBLE
                _binding!!.transactionMinis.visibility = View.GONE
                _binding!!.noTransaction.visibility = if (txn.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }


    private fun setRecycler() {
        walletAdapter = TcashbackOnlyWalletTransaction_Adapter(this)
        _binding!!.transaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = walletAdapter
        }
//        getData(TimePeriodDialog.getDate(1,0), TimePeriodDialog.getCurrentDate(), getString(R.string.till_date))
    }

    private fun setRecyclerForMini() {
        walletAdapterV = Tcashbackverified_Adapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "detail" && data is TCashDashboardData) {
                    PopUpBar(data)
                }
            }
        })
        _binding!!.transactionMinis.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = walletAdapterV
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


        date.text = parseDate3(data.date) +" "+ if (data.date.toString().length>18){
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


    companion object {
        @JvmStatic
        fun newInstance() =
            AllTransactionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}