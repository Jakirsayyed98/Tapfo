package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.VouchersListLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.VoucherListViewModel.CustomeVoucherList
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

class  voucher_list_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, voucher_list_adapter<S>.Holder>() {

    inner class Holder(private val binding: VouchersListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(s: S) {
          if (s is CustomeVoucherList){

              binding.name.text  = s.name+" E-Gift Vouchers"
              binding.discount.text  ="Flat "+s.discount +"% Discount"

              binding.reddem.text =if (s.redeemOption.equals("1")) "Online Redeemable" else if (s.redeemOption.equals("2")) "offline Redeemable" else "Online and Offline Redeemable"

              if (s.icon.isNullOrEmpty().not())
                  Glide.with(itemView.context).load(s.icon).circleCrop().into(binding.imageIv)


              binding.root.setOnClickListener {
                  clickListener.onRecyclerItemClick(0,s,"")
              }
          }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            VouchersListLayoutBinding.inflate(
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