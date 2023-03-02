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
import app.tapho.databinding.RechargeoperaterBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.Data
import com.bumptech.glide.Glide

class RechargeOrporaterAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, RechargeOrporaterAdapter<S>.Holder>()  {

    companion object{
        var operraterName:String?=null
        var operraterCode:String?=null
        var Operator_ID:String?=null
    }

    inner class Holder(private val binding: RechargeoperaterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is Data) {
                val imageUrl=s.image
                Glide.with(itemView.context).load(imageUrl).centerCrop().circleCrop().into(binding.image)
                binding.orperatorName.text=s.name
                binding.root.setOnClickListener {
                    operraterName=s.name
                    operraterCode=s.operator_code
                    Operator_ID=s.id
                    clickListener.onRecyclerItemClick(0, s,imageUrl )
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RechargeoperaterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false))
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