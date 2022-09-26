package app.tapho.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentBillsBinding
import app.tapho.databinding.FragmentMerchantDealBinding
import app.tapho.databinding.FragmentTcashPointBinding
import app.tapho.ui.BaseFragment


class BillsFragment : BaseFragment<FragmentBillsBinding>() {


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
        billsbinding = FragmentBillsBinding.inflate(inflater, container, false)
        return billsbinding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BillsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}