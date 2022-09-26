package app.tapho.ui.recharge

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.tapho.R
import app.tapho.databinding.FragmentRechargeAmountBinding
import app.tapho.ui.BaseFragment
import app.tapho.utils.DATA
import app.tapho.utils.OPERATOR

class RechargeAmountFragment : BaseFragment<FragmentRechargeAmountBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRechargeAmountBinding.inflate(inflater, container, false)

        binding.proceedBtn.setOnClickListener {  }
        setFragmentResultListener()
        setChangeOperator()
        binding.contactTypeTv.setOnClickListener { selectOperator() }
        binding.regionSelectTv.setOnClickListener { selectOperatorCircle() }
        binding.planBtn.setOnClickListener {
            findNavController().navigate(R.id.action_rechargeAmountFragment_to_plansDialogFragment)
        }
        return _binding?.root
    }

    private fun setChangeOperator() {
        val s = SpannableString(getString(R.string.change_operator)).apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    selectOperator()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }, length - 16, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.notNumberTv.movementMethod = LinkMovementMethod.getInstance()
        binding.notNumberTv.text = s

        val s2 = SpannableString(getString(R.string.unable_to_fetch_bill_of)).apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }, length - 11, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.fetchAgainTv.movementMethod = LinkMovementMethod.getInstance()
        binding.fetchAgainTv.text = s2
    }

    private fun setFragmentResultListener() {
        requireParentFragment().parentFragmentManager.setFragmentResultListener(OPERATOR, this,
            { requestKey, result -> result.getSerializable(OPERATOR) })
    }

    private fun selectOperator() {
        findNavController().navigate(R.id.action_rechargeAmountFragment_to_selectOperatorDialogFragment,
            Bundle().apply {
                putSerializable(DATA, arguments?.getSerializable(
                    DATA))
            })
    }

    private fun selectOperatorCircle(){
        findNavController().navigate(R.id.action_rechargeAmountFragment_to_operatorCircleDialogFragment)
    }
}