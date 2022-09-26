package app.tapho.ui.recharge

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.databinding.DialogSelectOperatorBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.ui.model.AppCategory
import app.tapho.ui.recharge.adapter.ServiceOperatorAdapter
import app.tapho.ui.recharge.model.ServiceOperatorData
import app.tapho.ui.recharge.model.ServiceOperatorRes
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.utils.OPERATOR
import app.tapho.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectOperatorDialogFragment : BottomSheetDialogFragment(), LoaderListener,
    ApiListener<ServiceOperatorRes, Any?>, RecyclerClickListener {
    private var binding: DialogSelectOperatorBinding? = null
    private var mAdapter: ServiceOperatorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#60909090")))
        binding = DialogSelectOperatorBinding.inflate(inflater, container, false)
        setRecycler()
        return binding?.root
    }

    private fun setRecycler() {
        mAdapter = ServiceOperatorAdapter(this)
        binding?.operatorRecycler?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        getData()
    }

    private fun getData() {
        kotlin.runCatching {
            ViewModelProvider(this).get(RequestViewModel::class.java)
                .getRechargeServiceOperator(AppSharedPreference.getInstance(requireContext())
                    .getUserId(), (arguments?.getSerializable(DATA) as AppCategory).id, this, this)
        }
    }

    override fun showLoader() {
        binding?.progress?.visibility = View.VISIBLE
    }

    override fun dismissLoader() {
        binding?.progress?.visibility = View.GONE
    }

    override fun showMess(mess: String?) {
        context?.toast(mess)
    }

    override fun onSuccess(t: ServiceOperatorRes?, mess: String?) {
        t?.data?.let { mAdapter?.addAllItem(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        kotlin.runCatching {
            requireParentFragment().parentFragmentManager.setFragmentResult(OPERATOR,
                bundleOf(Pair(OPERATOR, data as ServiceOperatorData)))
            dismiss()
        }
    }

}