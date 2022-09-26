package app.tapho.ui.tcash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentWalletHistoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.tcash.adapter.TcashbackWallet_Adapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcashback_detail_Activity
import app.tapho.utils.DATA
import app.tapho.utils.onlyDatewithMonth
import app.tapho.utils.parseDate2
import app.tapho.utils.withSuffixAmount
import com.google.gson.Gson

class Fragment_wallet_history : BaseFragment<FragmentWalletHistoryBinding>(),
    ApiListener<TCashDasboardRes, Any?>, RecyclerClickListener {
    private var walletAdapter: TcashbackWallet_Adapter? = null
    private var type: String? = ""
    var idCreatedAt=""

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
        if (type != "1")
            statusBarColor(R.color.white)




        bindingwallet = FragmentWalletHistoryBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        init()
        setRecycler()
        getNewHomeResData()
        return bindingwallet?.root
    }


    private fun init() {
    //    if (activity is HomeActivity)
            bindingwallet!!.back.setOnClickListener {
                activity?.onBackPressed()
            }

       // bindingwallet!!.timeDialogIv.setOnClickListener { openTimeDialog() }
       // setSpannable()
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun getNewHomeResData() {
        viewModel.getHomeDataNew("home", getUserId(), this,
            object : ApiListener<HomeResData, Any?> {
                override fun onSuccess(t: HomeResData?, mess: String?) {
                    t?.profile_detail?.get(0).let {
                        idCreatedAt = it!!.created_at
                        getDataNew(idCreatedAt)
                    }
                }
            })
    }

    private fun getDataNew(idCreatedAt: String) {
        viewModel.getTCashDashboard(
            getSharedPreference().getUserId(),
            TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate()
            /*TimePeriodDialog.getCurrentDate()*/, this, this
        )
    }

    private fun setRecycler() {
        walletAdapter = TcashbackWallet_Adapter(this)
        bindingwallet!!.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = walletAdapter
        }
        getData(
            TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(), getString(R.string.this_month)
        )
    }

    fun getData(startDate: String, endDate: String, textTime: String) {
        walletAdapter?.clear()

        "$startDate / $endDate".also { }
    }

    private fun openTimeDialog() {
        TimePeriodDialog().apply {
            arguments = Bundle().apply {
             //   putString("TIME", bindingwallet!!.timeDialogIv.text.toString())
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
            var VerifiedData: ArrayList<TCashDashboardData> = ArrayList()

            it.data!!.forEach {
                if (it.status!!.uppercase().equals("VERIFIED")) {
                    VerifiedData.add(it)
                } else if (it.status!!.uppercase().equals("VALIDATED")) {
                    VerifiedData.add(it)
                }
            }

          //  bindingwallet!!.balance.text= withSuffixAmount(it.cash_available)
            bindingwallet!!.lastUpdatedTv.text=getString(R.string.last_updated, onlyDatewithMonth(VerifiedData.get(0).date/* ?: ""*/))


            t.data?.let { it1 ->
                walletAdapter?.addAllItem(it1)

                bindingwallet!!.icRupee1.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE
             //   bindingwallet!!.noOrder.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE

           //     setData(it)
            }
        }
    }

    private fun setData(res: TCashDasboardRes) {
        kotlin.runCatching {
            if (res.data.isNullOrEmpty().not()) {
//                binding1!!.lastUpdatedTv.text =
//                    getString(R.string.last_updated, parseDate2(res.data?.get(0)?.date ?: ""))
            } else {
                //              binding1!!.lastUpdatedTv.text = getString(R.string.last_updated, "")
            }
        }
    }

    private fun setSpannable() {

        val s = SpannableString(getString(R.string.you_don_t_have_any_orders)).apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    if (activity is HomeActivity)
                        (activity as HomeActivity).addHome()
                    else {
                        activity?.setResult(Activity.RESULT_OK)
                        activity?.finish()
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
                }
            }, 45, 49, 0)
        }
//        bindingwallet!!.emptyListTv.movementMethod = LinkMovementMethod.getInstance()
//        bindingwallet!!.emptyListTv.text = s
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "detail" && data is TCashDashboardData) {
           // startActivity(Intent(context, TCashbackDetailActivity::class.java).apply {
                startActivity(Intent(context, tcashback_detail_Activity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(DATA, Gson().toJson(data))
            })
        }
    }

    private fun redeem() {
        launcher.launch(Intent(context, TCashRedeemActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        })
    }
}