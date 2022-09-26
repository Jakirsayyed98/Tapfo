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
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.DialogTcashbackFullBinding
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.RatesAdapter
import app.tapho.ui.merchants.TermsRulesFragment
import app.tapho.ui.model.TCashCategory
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.DATA
import com.bumptech.glide.Glide
import com.google.gson.Gson


class TCashbackDialogFullFragment : DialogFragment() {
    private var _binding: DialogTcashbackFullBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(t: WebTCashRes?, showBack: Boolean): TCashbackDialogFullFragment {
            val args = Bundle().apply {
                putString(DATA, Gson().toJson(t))
                putBoolean("SHOW_BACK", showBack)
            }
            val fragment = TCashbackDialogFullFragment()
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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        _binding = DialogTcashbackFullBinding.inflate(inflater, container, false)
        setSpannableRule()
        getData()
        binding.backIv.setOnClickListener {
            activity?.finish()
        }
        binding.continueLi.setOnClickListener { loadUrl() }
        binding.termConditionRl.setOnClickListener {
            TermsRulesFragment.newInstance().show(childFragmentManager, null)
        }

        if (arguments?.getBoolean("SHOW_BACK") == false) {
            binding.backIv.visibility = View.INVISIBLE
            binding.line.visibility = View.INVISIBLE
        } else
            binding.continueTv.text = getString(R.string.activate_cashback)
        return _binding?.root
    }

    private fun getData() {
        Gson().fromJson(arguments?.getString(DATA), WebTCashRes::class.java)?.let {
            setData(it)
        }
    }

    private fun setData(it: WebTCashRes) {
        binding.nameTv.text = it.mini_app?.get(0)?.name
        binding.nameTv2.text = it.mini_app?.get(0)?.name
        it.data[0].cashback?.let { it1 -> setReward(it1) }
        Glide.with(binding.companyImageIv).load(it.mini_app?.get(0)?.image)
            .into(binding.companyImageIv)

        binding.timeTv.text = it.data[0].report
        setRecycler(it.category)
    }

    private fun setSpannableRule() {
        val s =
            SpannableString(getString(R.string.important_points_to_ensure_your_cashback_rules_trems))
        s.setSpan(object : ClickableSpan() {
            override fun onClick(p0: View) {
                TermsRulesFragment.newInstance().show(childFragmentManager, null)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
            }
        }, s.length - 13, s.length, 0)
        binding.termsTv.movementMethod = LinkMovementMethod.getInstance()
        binding.termsTv.text = s
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
        val mAdapter = RatesAdapter()
        if (list != null) {
            mAdapter.addAllItem(list)
        }
        binding.recyclerRates.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                    setDrawable(it)
                }
            })
            adapter = mAdapter
        }
    }

    private fun loadUrl() {
        if (activity is WebViewActivity && arguments?.getBoolean("SHOW_BACK") == true) {
            (activity as WebViewActivity).initWebView()
        }
        dismiss()
    }

}