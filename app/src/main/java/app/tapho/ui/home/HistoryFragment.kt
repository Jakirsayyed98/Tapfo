package app.tapho.ui.home

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.FragmentHistoryBinding
import app.tapho.ui.BaseFragment
import com.google.android.play.core.review.ReviewManagerFactory

// TODO: Rename parameter arguments, choose names that match
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        statusBarColor(R.color.bg_grey)
        _binding=FragmentHistoryBinding.inflate(inflater,container,false)


        setSpannable()
        return _binding?.root
    }



    private fun setSpannable() {
        val s = SpannableString(getString(R.string.you_don_t_have_any_orders)).apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    (activity as HomeActivity).addHome()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(requireContext(), R.color.purple_500)
                }
            }, 45, 49, 0)
        }
        binding.emptyListTv.movementMethod = LinkMovementMethod.getInstance()
        binding.emptyListTv.text = s
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HistoryFragment()
    }
}