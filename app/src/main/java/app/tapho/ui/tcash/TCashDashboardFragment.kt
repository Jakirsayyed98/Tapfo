package app.tapho.ui.tcash

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.FragmentTCashDashboardBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.News.NewsActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.*
import app.tapho.ui.model.*
import app.tapho.ui.model.NewHomeRes.HomeResData
import app.tapho.ui.tcash.adapter.TCashbackAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcashback_detail_Activity
import app.tapho.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson


class TCashDashboardFragment : BaseFragment<FragmentTCashDashboardBinding>(),
    ApiListener<TCashDasboardRes, Any?>, RecyclerClickListener {
    private var mAdapter: TCashbackAdapter? = null
    private var offersForYouAdapter: offerforyou_Adapter<CashbackMerchant>? = null
    private var type: String? = ""
    var idCreatedAt=""

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
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        if (type != "1")
      //      statusBarColor(R.color.purple_500)
            statusBarColor(R.color.orange1)
        _binding = FragmentTCashDashboardBinding.inflate(inflater, container, false)
        init()
        statusBarTextWhite()
        getNewHomeResData()
        setRecycler()
        Tcashbanner()
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }

        cashbackmerchantviewmodel()
        return _binding?.root
    }

    private fun Tcashbanner() {
        val customFinaceCategory = TCashbannerAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {

                }
            }
        }).apply {
            addItem(CustomeShopCategoryModel(R.drawable.tcashbanner, "", ""))
            addItem(CustomeShopCategoryModel(R.drawable.tcashbanner, "", ""))
            addItem(CustomeShopCategoryModel(R.drawable.tcashbanner, "", ""))
            addItem(CustomeShopCategoryModel(R.drawable.tcashbanner, "", ""))

        }
        _binding!!.recyclerviewBanner.apply {

            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//            layoutManager = GridLayoutManager(context, 5)
            //layoutManager = LinearLayoutManager(context)
            adapter = customFinaceCategory
        }
    }


    private fun cashbackmerchantviewmodel() {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(), this, object:ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t.let {
                    it!!.new_cashback.let {
                        cashbackMerchantsdata(it)
                    }
                }
            }
        }
        )
    }

    private fun cashbackMerchantsdata(it: ArrayList<NewCashback>?) {
        var myadapter = RecommendedAdapter<NewCashback>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (type == "MiniAppFragment")
                    openAllPopularCashbackMerchants("2")
                else if (type == OPEN_WEB_VIEW && data is MiniApp) {
                    openWebView(data)
                }
            }
        })
        myadapter.setShowRupee(true)
        myadapter.setMoreImagePos(16)
        _binding!!.cashbackMerchant.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = myadapter
        }
        myadapter.addAllItem(if (it!!.size > 8) it.subList(0, 8) else it)
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
            TCashDashboardFragment().apply {
                arguments = Bundle().apply { putString("TYPE", type) }
            }
        var LastUpdate:String?=null
        var MyUserID: String? = null
    }

    private fun openWebView(data: MiniApp) {
        //    ActiveCashbackActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id)
        WebViewActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id
        )
    }
    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()

        if (type == "1")
        //  openContainer("MiniAppSearch", list, false, "data.name")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        //    SearchFragment.showSearch(childFragmentManager, list)
        else
            openContainer(
                getString(R.string.popular_merchants),
                list,
                true,
                getString(R.string.popular_merchants)
            )
    }

    @SuppressLint("ResourceType")
    private fun init() {
        if (activity is HomeActivity)
            binding.backIv.visibility = View.GONE
        binding.rechargeService.setOnClickListener {
            RechargeServiceActivity.openRechargeService(context)
        }
        binding.news.setOnClickListener {
            NewsActivity.openNews(context)
        }
        binding.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
//        binding.newCashbackPartnerMoreIv.setOnClickListener {
//            openContainer(getString(R.string.cashback_merchants),
//                null, false, getString(R.string.cashback_merchants))
//        }
        binding.transactionHistory.setOnClickListener {
            ContainerActivity.openContainer(context, "transactionHistory", "")

//            val intent = Intent(context, Tcashback_History::class.java)
//            startActivity(intent)

        }
        binding.walletHistory.setOnClickListener {
            ContainerActivity.openContainer(context, "walletHistory", "")
//            val intent = Intent(context, Tcashback_History::class.java)
//            startActivity(intent)

        }
        binding.withdrawTv.setOnClickListener { redeem() }
        //     binding.withdrawIv.setOnClickListener { redeem() }
        binding.pendingCashback.setOnClickListener {
            PopUpBar()
        }
        setSpannable()
    }

    private fun PopUpBar() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.cashback_rule_popup, null)
        dialog.setCancelable(true)
        val AllTransaction: TextView = view.findViewById(R.id.viewAllTransaction)
        AllTransaction.setOnClickListener {
            val newFragment: Fragment = Fragment_Tcash_HistoryData()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }


    private fun setRecycler() {
        mAdapter = TCashbackAdapter(this)
        binding.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        getData(TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),
            getString(R.string.this_month)
        )
    }

    private fun getNewHomeResData() {
        viewModel.getHomeDataNew("home", getUserId(), this,
            object : ApiListener<HomeResData, Any?> {
                override fun onSuccess(t: HomeResData?, mess: String?) {
                    t?.profile_detail?.get(0).let {
                        idCreatedAt = it!!.created_at
                        getDatanew(idCreatedAt)
                    }
                }
            })
    }

    private fun getDatanew(idCreatedAt: String) {
        idCreatedAt.toString()
        MyUserID = getSharedPreference().getUserId()
        viewModel.getTCashDashboard(getSharedPreference().getUserId(),
            TimePeriodDialog.getDate(1, -12),TimePeriodDialog.getCurrentDate(), this, this)
        Log.d("DateDATA","$idCreatedAt")
        Log.d("DateDATA","${TimePeriodDialog.getCurrentDate()}")
    }

    fun getData(startDate: String, endDate: String, textTime: String) {
        mAdapter?.clear()

        "$startDate / $endDate"

    }

    private fun openTimeDialog() {
        TimePeriodDialog().apply {
            arguments = Bundle().apply {
                //        putString("TIME", binding.timePeriodTv.text.toString())
            }
        }.show(childFragmentManager, "TimePeriodDialog")
    }


    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
        t?.let {
            t.data?.let { it1 ->

                mAdapter?.addAllItem(if (it1!!.size > 5) it1.subList(0, 5) else it1)

                //  mAdapter?.addAllItem(it1)
                  _binding?.emptyListTv?.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE
                  _binding?.noOrder?.visibility = if (it1.isEmpty()) View.VISIBLE else View.GONE
                setData(it)
            }
        }
    }



    private fun setData(res: TCashDasboardRes) {
        kotlin.runCatching {
            binding.balanceTv.text = withSuffixAmount(res.cash_available)
            binding.totalEarningTv.text = withSuffixAmount(res.lifetime_earning.toString())
            binding.pendingAmountTv12.text ="your Pending cashback : "+ withSuffixAmount(res.pending)
//            binding.pendingAmountTv.text = withSuffixAmount(res.pending)
//            binding.verifiedAmountTv.text = withSuffixAmount(res.verified)
//            binding.rejectedAmountTv.text = withSuffixAmount(res.reject)
            if (res.data.isNullOrEmpty().not()) {
                LastUpdate=res.data?.get(0)?.date?:"".toString()
                binding.lastUpdatedTv.text = getString(R.string.last_updated, parseDate2(res.data?.get(0)?.date ?: ""))
                binding.lastUpdatedTv1.text = getString(R.string.last_updated, parseDate2(res.data?.get(0)?.date!!.dropLast(9) ?: ""))
            } else
                LastUpdate=binding.lastUpdatedTv.text.toString()
              //  binding.lastUpdatedTv.text = getString(R.string.last_updated, "") 8788316985
        }
    }

    private fun setSpannable() {
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
            putExtra("TOTAL_BALENCE", binding.balanceTv.text.toString())
        })
    }
}