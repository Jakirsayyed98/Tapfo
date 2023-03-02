package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.DoubleRoundFiger
import app.tapho.databinding.VoucherHistoryLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.CategoriesModel.Custome_voucher_history
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

class  voucher_purchased_history_list_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, voucher_purchased_history_list_adapter<S>.Holder>() {

    inner class Holder(private val binding: VoucherHistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(s: S) {
          if (s is Custome_voucher_history){

              binding.name.text = s.name+" eVoucher"

              Glide.with(itemView.context).load(s.icon).circleCrop().into(binding.icon)
              binding.Value.text = itemView.context.DoubleRoundFiger(s.value)
              binding.root.setOnClickListener {
                  clickListener.onRecyclerItemClick(0,s,"")
              }
          }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            VoucherHistoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()

    }
}