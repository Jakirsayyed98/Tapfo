package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.PaymentPageVoucherDenominationBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersData
import app.tapho.utils.withSuffixAmount2
import kotlin.collections.ArrayList

class  voucherlist_paymentpage_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, voucherlist_paymentpage_adapter<S>.Holder>() {

    inner class Holder(private val binding: PaymentPageVoucherDenominationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(s: S) {
          if (s is VouchersData){
                binding.name.text = s.name+" ("+ withSuffixAmount2(s.ActualAmount)+" )"

                val datatotal = s.Qty.toInt() * s.ActualAmount.toInt()
                binding.Qty.text = "Qty: "+s.Qty+"  "+ withSuffixAmount2(datatotal.toString())

              val payable =  s.PaybleAmount.toInt()
              binding.PaybleAmount.text =  withSuffixAmount2(payable.toString())
              binding.root.setOnClickListener {

              }
          }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder( PaymentPageVoucherDenominationBinding.inflate(
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