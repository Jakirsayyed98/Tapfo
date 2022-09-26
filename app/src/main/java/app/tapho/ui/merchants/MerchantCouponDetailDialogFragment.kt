package app.tapho.ui.merchants

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import app.tapho.R
import app.tapho.databinding.FragmentCouponDetailDialogBinding
import app.tapho.ui.WebViewActivity
import app.tapho.ui.merchants.model.OfferData
import app.tapho.utils.customToast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*


class MerchantCouponDetailDialogFragment : DialogFragment() {
    private var binding: FragmentCouponDetailDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#60909090")))
        dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.bg_grey)
        binding = FragmentCouponDetailDialogBinding.inflate(inflater, container, false)

        init()
        return binding?.root
    }

    private fun init() {
        binding?.closeIv?.setOnClickListener { dismiss() }
        binding?.copyIv?.setOnClickListener {
            getSystemService(requireContext(), ClipboardManager::class.java)?.apply {
                setPrimaryClip(ClipData.newPlainText(binding?.couponsTv?.text,
                    binding?.couponsTv?.text))
                context?.customToast("code coppied", false)
            }
        }

        Gson().fromJson(arguments?.getString("DATA"), OfferData::class.java)?.let { data ->
            binding?.let { bind ->
                if (data.mini_app.isNullOrEmpty().not()) {
                    bind.nameTv.text = data.mini_app[0].name

                    Glide.with(bind.imageIv).load(data.mini_app[0].image).into(bind.imageIv)
                    if (data.cashback.isNullOrEmpty())
                        bind.cashbackLi.visibility = View.GONE
                    else
                        bind.cashbackInfoIv.text = URLDecoder.decode(data.cashback, "UTF-8")
                } else {
                    bind.cashbackLi.visibility = View.GONE
                }

                bind.titleTv.text = data.name
                bind.couponsTv.text = data.code
                bind.lableTv.text = data.label
                bind.validTv.text = getString(R.string.valid_till_, parseDate(data.end_date))

                bind.btnRedeem.setOnClickListener {
                    WebViewActivity.openWebView(context,
                        data.url,
                        data.mini_app[0].id,
                        data.mini_app[0].image,
                        "",
                        "",data.cashback,data.app_category_id)
                    dismiss()
                }
            }
        }
    }

    private fun parseDate(s: String): String {
        kotlin.runCatching {
            return SimpleDateFormat("dd MMMM", Locale.ENGLISH).format(SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH).parse(s))
        }
        return s
    }

    companion object {
        @JvmStatic
        fun newInstance(data: OfferData) =
            MerchantCouponDetailDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("DATA", Gson().toJson(data))
                }
            }
    }
}