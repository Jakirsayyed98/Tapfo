package app.tapho.ui.tcash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentFoCashDataBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.home.adapter.customeCardDetailsAdapter
import app.tapho.ui.tcash.adapter.TCashbackAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcashback_detail_Activity
import app.tapho.utils.DATA
import app.tapho.utils.parseDate2
import app.tapho.utils.withSuffixAmount
import com.google.gson.Gson


class FoCashDataFragment : BaseFragment<FragmentFoCashDataBinding>(), RecyclerClickListener{

    private var mAdapter: TCashbackAdapter? = null
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
        _binding = FragmentFoCashDataBinding.inflate(inflater,container,false)
        details()
        setMoreClickEvent()
        tcashhistoryDataViewModel1()
        setTransactionData()
        return _binding?.root

    }



    private fun setMoreClickEvent() {
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.transactionLog.setOnClickListener {
            ContainerActivity.openContainer(context, "transactionHistory", "")
        }
    }
    private fun tcashhistoryDataViewModel1() {
        getSharedPreference().getUserId().let { viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12),
                TimePeriodDialog.getCurrentDate(), this, object : ApiListener<TCashDasboardRes, Any?> {
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                        t.let {
                            it!!.data.let {
                                mAdapter!!.addAllItem( if (it!!.size > 4)it.subList(0,4) else it)
                            }

                            _binding!!.lifetimeearning.text= withSuffixAmount(it!!.cash_available)
                            _binding!!.pending.text=getString(R.string.you_have_total_1245_00_focash_pending,withSuffixAmount(it.pending))
                            _binding!!.lastUpdatedTv.text=getString(R.string.last_updated, parseDate2(it.data!!.get(0).date ?: ""))

                        }
                    }

                })
        }
    }

    private fun setTransactionData() {
        mAdapter = TCashbackAdapter(this)
        _binding!!.recyclerCashback.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = mAdapter
        }

    }

    private fun details() {
        val detailsAdapter = customeCardDetailsAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {

                }
            }

        }).apply {

            addItem(CustomeShopCategoryModel(R.drawable.instantcheckout, "INSTANT CHECKOUT", "One-click,easy and fast checkout"))
            addItem(CustomeShopCategoryModel(R.drawable.fastrefund, "INSTANT CASHBACK", "Get instant cashback on top stores"))
            addItem(CustomeShopCategoryModel(R.drawable.consolidmoney, "CONSOLIDATED MONEY", "Gift cards, refund and cashback in one place"))
            addItem(CustomeShopCategoryModel(R.drawable.many_morebanifits, "MANY MORE BENEFITS", "Benefits and offers on using Tapfo Credit"))

        }
        _binding!!.details.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = detailsAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            FoCashDataFragment().apply {
                arguments = Bundle().apply {

                }
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
}