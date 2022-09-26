package app.tapho.ui.RechargeService.MobileRechcrge.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RechargeCircleLayoutBinding
import app.tapho.databinding.RechargeoperaterBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.Data

class RechargeCircleAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, RechargeCircleAdapter<S>.Holder>()  {


    inner class Holder(private val binding: RechargeCircleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is Data) {
                binding.orperatorName.text=s.name
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s,s.code)

                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RechargeCircleLayoutBinding.inflate(
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


    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1:ArrayList<S>){
        list=list1
        notifyDataSetChanged()

    }

}