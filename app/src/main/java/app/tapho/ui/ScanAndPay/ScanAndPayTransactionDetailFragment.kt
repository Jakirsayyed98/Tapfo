package app.tapho.ui.ScanAndPay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentScanAndPayTransactionDetailBinding
import app.tapho.ui.BaseFragment


class ScanAndPayTransactionDetailFragment : BaseFragment<FragmentScanAndPayTransactionDetailBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScanAndPayTransactionDetailBinding.inflate(inflater,container,false)
        return _binding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ScanAndPayTransactionDetailFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}