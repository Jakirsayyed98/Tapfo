package app.tapho.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.FragmentVerifyErrorBinding
import app.tapho.ui.BaseFragment

class VerifyErrorFragment : BaseFragment<FragmentVerifyErrorBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyErrorBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // binding.toolbar.backIV.setOnClickListener { activity?.onBackPressed() }
        binding.tvTryAgain.setOnClickListener { activity?.onBackPressed() }
        setForgotPasscode()
    }

    private fun setForgotPasscode() {
        binding.forgotPasscodeTv1.movementMethod = LinkMovementMethod.getInstance()
        val s = SpannableString(getString(R.string.forgot_passcode_try_with_otp))
            .apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        startActivity(Intent(context, LoginActivity::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        })
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                        ds.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
                    }
                }, 17, length, 0)
            }
        binding.forgotPasscodeTv1.text = s
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VerifyErrorFragment()
    }
}