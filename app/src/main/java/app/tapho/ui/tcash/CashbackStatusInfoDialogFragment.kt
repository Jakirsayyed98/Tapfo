package app.tapho.ui.tcash

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.FragmentCashbackStatusInfoDialogBinding
import app.tapho.interfaces.SpanClickListener
import app.tapho.utils.getSpannableBold
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CashbackStatusInfoDialogFragment : BottomSheetDialogFragment(), SpanClickListener {
    private var binding: FragmentCashbackStatusInfoDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#60000000")))
        return inflater.inflate(R.layout.fragment_cashback_status_info_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCashbackStatusInfoDialogBinding.bind(view)
        binding?.closeTv?.setOnClickListener { dismiss() }

        binding?.pendingTv?.text =
            getSpannableBold(getString(R.string.pending_info), 0, 8, null, 1.2f)
        binding?.verifiedTv?.text =
            getSpannableBold(getString(R.string.verified_info), 0, 8, null, 1.2f)
        binding?.rejectedTv?.movementMethod=LinkMovementMethod.getInstance()
        binding?.rejectedTv?.text =
            getSpannableBold(getString(R.string.rejected_info), 0, 8, null, 1.2f)
                .apply {
                    setSpan(object : ClickableSpan() {
                        override fun onClick(p0: View) {

                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.isUnderlineText = false
                            ds.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
                        }
                    }, length - 17, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CashbackStatusInfoDialogFragment()
    }

    override fun onSpanClick() {

    }
}