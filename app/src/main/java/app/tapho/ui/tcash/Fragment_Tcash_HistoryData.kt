package app.tapho.ui.tcash

import android.app.Activity.RESULT_OK
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
import app.tapho.databinding.FragmentTcashHistoryDataBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.tcash.adapter.TCashbackAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcashback_detail_Activity
import app.tapho.utils.*
import com.google.gson.Gson


class Fragment_Tcash_HistoryData : BaseFragment<FragmentTcashHistoryDataBinding>(),
    ApiListener<TCashDasboardRes, Any?>, RecyclerClickListener {
    private var mAdapter: TCashbackAdapter? = null
    private var type: String? = ""
    var idCreatedAt = ""

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (activity is HomeActivity)
                    (activity as HomeActivity).addHome()
                else {
                    activity?.setResult(RESULT_OK)
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
            statusBarColor(R.color.orange1)


        statusBarColor(R.color.orange1)
        binding1 = FragmentTcashHistoryDataBinding.inflate(inflater, container, false)
        binding1!!.mainLayout.visibility = View.GONE
        binding1!!.progress.visibility = View.VISIBLE
        init()
        setRecycler()
        getData()
        return binding1?.root
    }

    private fun init() {
      //  if (activity is HomeActivity)
        //  binding1!!.backIv.visibility = View.GONE
            binding1!!.backIv.setOnClickListener {
                activity?.onBackPressed()
            }
//        binding.newCashbackPartnerMoreIv.setOnClickListener {
//            openContainer(getString(R.string.cashback_merchants),
//                null, false, getString(R.string.cashback_merchants))
//        }


//        binding1!!.timeDialogIv.setOnClickListener { openTimeDialog() }
        // setSpannable()
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun setRecycler() {
        mAdapter = TCashbackAdapter(this)
        binding1!!.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
//        getData(
//            TimePeriodDialog.getDate(1, -12),
//            TimePeriodDialog.getCurrentDate(),
//            getString(R.string.this_month)
//        )
    }

    private fun getData() {

        mAdapter?.clear()
        viewModel.getTCashDashboard(  getSharedPreference().getUserId(),
            TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(), this, this
        )
    }





    private fun openTimeDialog() {
        TimePeriodDialog().apply {
            arguments = Bundle().apply {
//                putString("TIME", binding1!!.timeDialogIv.text.toString())
            }
        }.show(childFragmentManager, "TimePeriodDialog")
    }

    companion object {
        @JvmStatic
        fun newInstance(/*type: String*/) =
            Fragment_Tcash_HistoryData().apply {
                arguments = Bundle().apply { putString("TYPE", type) }
            }
    }

    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
        t?.let {

            t.data?.let { it1 ->
                mAdapter?.addAllItem(it1)
                binding1!!.emptyListTv.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE
                binding1!!.progress.visibility = View.GONE
                binding1!!.mainLayout.visibility = View.VISIBLE
                setData(it)
            }
        }
    }

    private fun setData(res: TCashDasboardRes) {
        kotlin.runCatching {
            if (res.data.isNullOrEmpty().not()) {
                binding1!!.lastUpdatedTv.visibility = View.VISIBLE
                binding1!!.lastUpdatedTv.text = getString(R.string.last_updated,parseDate3(res.data?.get(0)?.date))
                binding1!!.totalEarningTv.text = withSuffixAmount( res.lifetime_earning)
                binding1!!.pendingAmountTv.text = withSuffixAmount( res.pending)
                binding1!!.verifiedAmountTv.text = withSuffixAmount( res.cash_available)
                binding1!!.rejectedAmountTv.text = withSuffixAmount( res.reject)


//                binding1!!.lastUpdatedTv.text = getString(R.string.last_updated, parseDate2(res.data?.get(0)?.date ?: ""))
            } else {
                binding1!!.lastUpdatedTv.text = getString(R.string.last_updated, "")
                binding1!!.lastUpdatedTv.visibility = View.GONE
                binding1!!.totalEarningTv.text = withSuffixAmount( "0.0")
                binding1!!.pendingAmountTv.text = withSuffixAmount("0.0")
                binding1!!.verifiedAmountTv.text = withSuffixAmount( "0.0")
                binding1!!.rejectedAmountTv.text = withSuffixAmount( "0.0")
            }
        }
    }

    private fun setSpannable() {
        binding1!!.emptyListTv.visibility = View.VISIBLE
        binding1!!.progress.visibility = View.GONE
        binding1!!.mainLayout.visibility = View.VISIBLE

        val s = SpannableString(getString(R.string.you_don_t_have_any_orders)).apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    if (activity is HomeActivity)
                        (activity as HomeActivity).addHome()
                    else {
                        activity?.setResult(RESULT_OK)
                        activity?.finish()
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
                }
            }, 45, 49, 0)
        }
        binding1!!.emptyListTv.movementMethod = LinkMovementMethod.getInstance()
        binding1!!.emptyListTv.text = s
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "detail" && data is TCashDashboardData) {
//            startActivity(Intent(context, TCashbackDetailActivity::class.java).apply {
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