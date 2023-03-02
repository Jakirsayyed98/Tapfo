package app.tapho.ui.RechargeService.MobileRechcrge.adapter

import android.annotation.SuppressLint
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
import app.tapho.utils.withSuffixAmount

class AllPlansAdapters <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AllPlansAdapters<S>.Holder>()  {

    inner class Holder(private val binding: MobileRechargePlansBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is PlanDescription) {
                binding.rate.text= withSuffixAmount(s.recharge_amount)!!.dropLast(3)
                if (s.recharge_validity.isNullOrEmpty().not()){
                    binding.validity.text="Validity: "+s.recharge_validity
                }else{
                    binding.validity.text="Validity: NA"
                }

                binding.rechargeDiscription.text=s.recharge_long_desc
                binding.rechargeType.text=s.recharge_type
                binding.root.setOnClickListener {
//                    MobileRechargeFragment.openRechargeActivityAgain(itemView.context,MobileRechargeFragment.number
//                        ,MobileRechargeFragment.operator_id,MobileRechargeFragment.circle_id, s.recharge_amount,s.recharge_validity,s.recharge_talktime)
                    clickListener.onRecyclerItemClick(0, s.recharge_amount, s.recharge_long_desc)
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


    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1:ArrayList<S>){
        list=list1
        notifyDataSetChanged()

    }

}