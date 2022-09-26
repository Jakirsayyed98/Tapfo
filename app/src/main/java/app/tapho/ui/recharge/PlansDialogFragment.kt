package app.tapho.ui.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.DialogPlansFragmentBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.model.CashbackMerchantCategory
import app.tapho.ui.recharge.adapter.PlanTabAdapter
import app.tapho.ui.recharge.adapter.PlansAdapter

class PlansDialogFragment : DialogFragment(), RecyclerClickListener {
    private var binding: DialogPlansFragmentBinding? = null
    private var tabAdapter: PlanTabAdapter? = null

    companion object {
        fun newInstance(): PlansDialogFragment {
            val args = Bundle()

            val fragment = PlansDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#60909090")))
        binding = DialogPlansFragmentBinding.inflate(inflater, container, false)

        setPlanType()
        setPlanAdapter()
        return binding?.root
    }

    private fun setPlanType() {
        tabAdapter = PlanTabAdapter(this)
        tabAdapter?.addItem(CashbackMerchantCategory("", "", "All Plans", "", false))
        tabAdapter?.addItem(CashbackMerchantCategory("", "", "Popular", "", false))
        tabAdapter?.addItem(CashbackMerchantCategory("", "", "Data", "", false))
        binding?.recyclerPlanType?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tabAdapter
        }
    }

    private fun setPlanAdapter(){
        binding?.recyclerPlans?.apply {
            layoutManager=LinearLayoutManager(context)
            adapter= PlansAdapter()
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}