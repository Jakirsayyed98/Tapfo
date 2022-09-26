package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.VouchersDenominationLayoutBinding
import app.tapho.databinding.VouchersListLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.VoucherDetails.CustomeVoucherlistDenomination
import app.tapho.utils.withSuffixAmount
import kotlin.collections.ArrayList

class  voucher_denomination_list_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, voucher_denomination_list_adapter<S>.Holder>() {
    inner class Holder(private val binding: VouchersDenominationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var currentdata : CustomeVoucherlistDenomination
        @SuppressLint("SetTextI18n")
        fun setData(s: S) {
            if (s is CustomeVoucherlistDenomination){

                var finalQuntity = 1
                binding.voucheramount.text = withSuffixAmount(s.price)
                binding.voucherAmount.text = s.price
                binding.userDiscount.text = s.user_discount+" %"

                binding.saveAmount.text = itemView.context.getString(R.string.saving_amount_20,"0")

                binding.addLayout.setOnClickListener {
                    currentdata = s
                    binding.quntitylayout.visibility = View.VISIBLE
                    binding.linearLayout16.visibility = View.VISIBLE
                    binding.addLayout.visibility = View.GONE
                }
                binding.add.setOnClickListener {
                    currentdata = s
                    QuntityData(finalQuntity++.toString())
//                    binding.finalquantity.text = finalQuntity++.toString()
//                    binding.Quantity.text ="* "+  finalQuntity++.toString()
                }

                binding.less.setOnClickListener {
                    currentdata = s
                    if (finalQuntity<=0){
                        binding.quntitylayout.visibility = View.GONE
                        binding.linearLayout16.visibility = View.GONE
                        binding.addLayout.visibility = View.VISIBLE
                    }else{
                        QuntityData(finalQuntity--.toString())
//                        binding.finalquantity.text = finalQuntity--.toString()
//                        binding.Quantity.text ="* "+finalQuntity--.toString()
                        binding.quntitylayout.visibility = View.VISIBLE
                        binding.linearLayout16.visibility = View.VISIBLE
                        binding.addLayout.visibility = View.GONE
                    }


                }


            }

        }

        private fun QuntityData(Quntity: String) {
            binding.finalquantity.text = Quntity
            binding.Quantity.text ="* "+  Quntity

            if (Quntity.toInt()<=0){
                clickListener.onRecyclerItemClick(Quntity.toInt(),currentdata,"delete")
            }else{
                clickListener.onRecyclerItemClick(Quntity.toInt(),currentdata,"add")
            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            VouchersDenominationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false )
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