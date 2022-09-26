package app.tapho.ui.RechargeService.MobileRechcrge.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.MobileRechargePlansBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.RechargeService.ModelData.RechargePlans.PlanDescription

class AllPlanCategoryAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AllPlanCategoryAdapter<S>.Holder>()  {

    inner class Holder(private val binding: MobileRechargePlansBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is PlanDescription) {
                binding.rechargeType.text=s.recharge_type
                binding.root.setOnClickListener {
//                    MobileRechargeFragment.openRechargeActivityAgain(itemView.context,MobileRechargeFragment.number
//                        ,MobileRechargeFragment.operator_id,MobileRechargeFragment.circle_id, s.recharge_amount,s.recharge_validity,s.recharge_talktime)
                    //clickListener.onRecyclerItemClick(0, s.id, "")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(MobileRechargePlansBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setSpannable(s: String?, textView: TextView) {
        try {
            textView.text = SpannableString(s).apply {
                setSpan(StyleSpan(Typeface.BOLD), 5, length - 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        } catch (e: Exception) {
            textView.text = s
        }

    }

}