package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.copyElement
import app.tapho.databinding.PurchasedVoucherLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.AllVoucher_data
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class  purchased_vouchers_list_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, purchased_vouchers_list_adapter<S>.Holder>() {

    inner class Holder(private val binding: PurchasedVoucherLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(s: S) {
          if (s is AllVoucher_data){
              Glide.with(itemView.context).load(s.icon).circleCrop().into(binding.icon)
              binding.name.text = s.name
              binding.VoucherValue.text = withSuffixAmount(s.value)
              binding.cardNumber.text = s.VoucherGC_Code
              binding.validity.text = s.validity
              binding.PinNumber.text = s.pin

              s.validity.compareTo(Date().toString())

              if (s.pin.isNullOrEmpty()){
                  binding.pinLayout.visibility = View.GONE
              }else{
                  binding.pinLayout.visibility = View.VISIBLE
              }

              binding.copybtn.setOnClickListener {
                  itemView.context.copyElement(binding.cardNumber.text.toString())
                  it.showShortSnack("Copied Sucessfully")
              }
              binding.copybtnPin.setOnClickListener {
                  itemView.context.copyElement(binding.PinNumber.text.toString())
                  it.showShortSnack("Copied Sucessfully")
              }



//              binding.root.setOnClickListener {
//                  clickListener.onRecyclerItemClick(0,s,"")
//              }
          }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            PurchasedVoucherLayoutBinding.inflate(
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