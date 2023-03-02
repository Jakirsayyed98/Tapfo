package app.tapho.ui.tcash

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.DialogTcashbackBinding
import app.tapho.ui.merchants.TCashWorkFragment
import app.tapho.ui.model.TCashCategory
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.DATA
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson


class TCashbackDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogTcashbackBinding? = null
    private val binding get() = _binding!!
    private var res: WebTCashRes? = null

    companion object {
        fun newInstance(t: WebTCashRes, fullData: Boolean): TCashbackDialogFragment {
            val args = Bundle().apply {
                putString(DATA, Gson().toJson(t))
                putBoolean("FULL_DATA", fullData)
            }
            val fragment = TCashbackDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#60000000")))
        _binding = DialogTcashbackBinding.inflate(inflater, container, false)
        setSpannableRule()
       setData()
        binding.howTCashWorkTv.setOnClickListener {
            TCashWorkFragment.newInstance().show(childFragmentManager, "TCashWorkFragment")
        }
        binding.continueLi.setOnClickListener { dismiss() }
        return _binding?.root
    }

    private fun setData() {
        Gson().fromJson(arguments?.getString(DATA), WebTCashRes::class.java)?.let {
            res = it
            binding.companyNameTv.text = it.mini_app?.get(0)?.name
            binding.nameTv2.text = it.mini_app?.get(0)?.name
            it.data[0].cashback?.let { it1 -> setReward(it1) }
            binding.termsTv.text = it.data[0].terms
            binding.trackingReportTimeTv.text =
                getString(R.string.cashback_tracking_report, it.data[0].report)
            Glide.with(binding.companyImageIv).load(it.mini_app?.get(0)?.image)
                .into(binding.companyImageIv)


            if (arguments?.getBoolean("FULL_DATA") == true) {
                binding.lockIv.visibility = View.GONE
                binding.hintUnlockTv.visibility = View.GONE
                binding.headingMerchantTv.text = getString(R.string.active_merchant)
                binding.trackingReportTimeTv.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.green_light_bg)
                binding.trackingReportTimeTv.setTextColor(ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.green_light))
                setRecycler(it.category)
            } else
                binding.recyclerLi.visibility = View.GONE
        }
    }

    private fun setSpannableRule() {
        val s = SpannableString(getString(R.string.don_t_forget_about_cashback_rule_terms))
        s.setSpan(object : ClickableSpan() {
            override fun onClick(p0: View) {
                res?.let {
//
                    dismiss()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
            }
        }, 22, s.length, 0)
        binding.rulesTermsTv.movementMethod = LinkMovementMethod.getInstance()
        binding.rulesTermsTv.text = s
    }

    private fun setReward(text: String) {
        val split = text.split(" ")
        if (split.size > 2)
            binding.rewardTv.text = SpannableString(text).apply {
                setSpan(ForegroundColorSpan(Color.RED),
                    split[0].length,
                    split[0].length + split[1].length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        else
            binding.rewardTv.text = text
    }

    private fun setRecycler(list: List<TCashCategory>?) {
//        val mAdapter = RatesAdapter()
//        mAdapter.addItem(TCashCategory("", "", "", "", "", "", ""))
//        if (list != null) {
//            mAdapter.addAllItem(list)
//        }
//        binding.recyclerRates.apply {
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context,
//                DividerItemDecoration.VERTICAL).apply {
//                ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
//                    setDrawable(it)
//                }
//            })
//            adapter = mAdapter
//        }
    }

}