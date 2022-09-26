package app.tapho.ui.vouchers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentVoucherDetailBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.vouchers.adapter.CashbackCountAdapter
import app.tapho.ui.vouchers.model.VoucherListData


class VoucherDetailFragment : BaseFragment<FragmentVoucherDetailBinding>() {
    private var voucherListData: VoucherListData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVoucherDetailBinding.inflate(inflater, container, false)
        init()
        return _binding?.root
    }

    private fun init() {
        binding.backIv.setOnClickListener { activity?.onBackPressed() }

        setCashbackRecycler()
    }

    private fun setCashbackRecycler() {
        binding.recyclerCashbackCount.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CashbackCountAdapter()
        }
    }

    private fun setRecycler(){
        binding.recyclerCoupons.apply {
            layoutManager=LinearLayoutManager(context)
        }
    }

}