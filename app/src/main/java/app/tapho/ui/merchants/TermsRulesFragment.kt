package app.tapho.ui.merchants

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.DialogFragment
import app.tapho.R


class TermsRulesFragment : DialogFragment() {

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
        return inflater.inflate(R.layout.fragment_terms_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.closeTv).setOnClickListener { dismiss() }
        initWebView()
    }

    private fun initWebView() {
        val view = view?.findViewById<WebView>(R.id.webView)
        view?.settings?.javaScriptEnabled = true
        view?.loadUrl("file:///android_asset/merchant_rules.html")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TermsRulesFragment()
    }
}