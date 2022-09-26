package app.tapho.ui.merchants

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import app.tapho.R


class TCashWorkFragment : DialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_t_cash_work, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.closeTv).setOnClickListener { dismiss() }
        setSpannable(view.findViewById(R.id.completeOrderTv))
    }

    private fun setSpannable(textView: TextView){
        textView.movementMethod=LinkMovementMethod.getInstance()
        textView.text=SpannableString(getString(R.string.complete_order_info)).apply {
            setSpan(object :ClickableSpan(){
                override fun onClick(p0: View) {
                    TermsRulesFragment.newInstance().show(childFragmentManager,"TermsRulesFragment")
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText=false
                    ds.color=ContextCompat.getColor(requireContext(),R.color.purple_500)
                }
            },length-19,length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TCashWorkFragment()
    }
}