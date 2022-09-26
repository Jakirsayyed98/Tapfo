package app.tapho.ui.activecashback

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.tapho.R
import app.tapho.databinding.FragmentCashbackRatesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.activecashback.adapter.OrdersAdapter
import app.tapho.ui.home.adapter.RatesAdapter
import app.tapho.ui.model.TCashCategory
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.model.TCashDasboardRes
import com.google.gson.Gson
import org.eazegraph.lib.models.PieModel


class CashbackRatesFragment : BaseFragment<FragmentCashbackRatesBinding>(),
    ApiListener<TCashDasboardRes, Any?> {
    private var res: WebTCashRes? = null
    var orderFragment=OrdersFragment()
    var orderNumbers:Int =0
    private var mAdapter: OrdersAdapter? = null
    private var t: TCashDasboardRes? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCashbackRatesBinding.inflate(inflater, container, false)
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.backbtn.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })
        getData()
    }


    private fun getData() {
        try {
            Gson().fromJson(arguments?.getString("DATA"), WebTCashRes::class.java)?.let { res ->
                res.mini_app?.get(0)?.name?.let {
                    setCashbackSpannable(it)
                }

                binding.placeOrderOnTv.text = getString(R.string.place_your_order_on_, res.mini_app?.get(0)?.name)
                binding.redirectline.text=getString(R.string.you_will_be_redirected_to_web_app_mweb,res.mini_app?.get(0)?.name)
                binding.merchantNameDashboard.text=getString(R.string.merchantName_dashboard,res.mini_app?.get(0)?.name)
                binding.cashbackCreditedTv.text = getString(R.string.your_cashback_will_be_credited, res.mini_app?.get(0)?.name,res.data[0].report)

                val mPieChart = _binding?.piechart

                mPieChart?.addPieSlice(PieModel("Verified", 50f, Color.parseColor("#008D3A")))
                mPieChart?.addPieSlice(PieModel("Pending", 35f, Color.parseColor("#EE8300")))
                mPieChart?.addPieSlice(PieModel("Rejected", 15f, Color.parseColor("#EF5350")))


                mPieChart?.startAnimation()
                _binding!!.totalOrder.text=getString(R.string.total_order_08,OrdersFragment.numberOfOrder)
                //  _binding!!.totalSpent.text=getString(R.string.total_spent_5000,OrdersFragment.count.toString())

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setCashbackSpannable(appName: String) {
        val s = getString(R.string.you_re_just_one_step_away, appName)
        binding.cashbackHeadingTv.text = SpannableString(s).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf("Extra"),
                s.indexOf("Extra") + 14,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            setSpan(
                StyleSpan(Typeface.BOLD),
                s.indexOf(appName),
                s.indexOf(appName) + appName.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        val s2 = getString(R.string.activate_, appName)
        binding.activeTv.text = SpannableString(s2).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                s2.indexOf(appName),
                s2.indexOf(appName) + appName.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
//        val s3 = getString(R.string.your_cashback_will_be_credited, appName)
//        binding.cashbackCreditedTv.text=SpannableString(s3).apply {
//            setSpan(
//                StyleSpan(Typeface.BOLD),
//                s3.indexOf(appName),
//                s3.indexOf(appName) + appName.length,
//                Spannable.SPAN_INCLUSIVE_INCLUSIVE
//            )
//        }

        binding.noteDontCancelTv.text =
            SpannableString(getString(R.string.note_don_t_cancel)).apply {
                setSpan(
                    ForegroundColorSpan(Color.RED), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
    }


    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {

        t?.let {

            t.data?.let { it1 ->

            }
        }
    }

    private fun setRecycler(list: List<TCashCategory>?) {
        val mAdapter = RatesAdapter()
        if (list != null) {
            mAdapter.addAllItem(list)
        }
        /*
        //cash back rates recycler view will be here
//        binding.recyclerRates.apply {
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(
//                DividerItemDecoration(
//                    context,
//                    DividerItemDecoration.VERTICAL
//                ).apply {
//                    ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
//                        setDrawable(it)
//                    }
//                })
//            adapter = mAdapter
//        }
*/

    }

}