package app.tapho.ui.tcash

import android.content.Intent
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
import app.tapho.databinding.FragmentGeneralQuestionsDialogBinding
import app.tapho.interfaces.SpanClickListener
import app.tapho.ui.help.SupportServiceActivity
import app.tapho.utils.getSpannableBold
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class GeneralQuestionsDialogFragment : BottomSheetDialogFragment(), SpanClickListener {
    private lateinit var binding: FragmentGeneralQuestionsDialogBinding

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
        return inflater.inflate(R.layout.fragment_general_questions_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeneralQuestionsDialogBinding.bind(view)

        binding.reContactUs.setOnClickListener {
            startActivity(Intent(context,SupportServiceActivity::class.java))
        }

        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(view.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.closeTv.setOnClickListener { dismiss() }

        binding.question1.text =
            getSpannableBold(getString(R.string.question1), 0, 10, null, 1.1f)
        binding.question2.text =
            getSpannableBold(getString(R.string.question2), 0, 12, null, 1.1f)

        binding.question3.text =
            getSpannableBold(getString(R.string.question3), 0, 8, null, 1.1f)

        binding.question4.text =
            getSpannableBold(getString(R.string.question4), 0, 9, null, 1.1f)

        binding.question6.text =
            getSpannableBold(getString(R.string.question6), 0, 18, null, 1.1f)

        binding.noteTv.text =
            getSpannableBold(getString(R.string.note), 0, 5, null, 1.1f)

        binding.question5.movementMethod = LinkMovementMethod.getInstance()
        binding.question5.text =
            getSpannableBold(getString(R.string.question5), 0, 9, null, 1.1f)
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
            GeneralQuestionsDialogFragment()
    }

    override fun onSpanClick() {

    }
}