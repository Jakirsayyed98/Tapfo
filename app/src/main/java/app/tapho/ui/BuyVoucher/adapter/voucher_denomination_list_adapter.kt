package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.VouchersDenominationLayoutBinding
import app.tapho.databinding.VouchersListLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersData
import app.tapho.ui.BuyVoucher.VoucherDetails.CustomeVoucherlistDenomination
import app.tapho.utils.getSpannableBold
import app.tapho.utils.withSuffixAmount
import app.tapho.utils.withSuffixAmount2
import kotlin.collections.ArrayList

class voucher_denomination_list_adapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, voucher_denomination_list_adapter<S>.Holder>() {
    inner class Holder(private val binding: VouchersDenominationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var custome = 0
        lateinit var currentdata: CustomeVoucherlistDenomination

        @SuppressLint("SetTextI18n")
        fun setData(s: S) {

            if (s is CustomeVoucherlistDenomination) {
                val sd = s.price.toInt() * s.user_discount.toInt() /100

                binding.save.text =s.user_discount+"% discount | Get it for "+ withSuffixAmount2((s.price.toInt()-sd).toString())
                binding.name.text =itemView.context.getString(R.string.myntra_e_gift_card_worth_100,s.Mininame)
                binding.price.text ="₹"+s.price
                currentdata = s

                binding.add.setOnClickListener {
                    if (custome>=5){

                    }else{
                        currentdata = s

                        custome++
                        QuntityData()
                    }

                }

                binding.less.setOnClickListener {
                    if (custome<=0){
                    }else{
                        currentdata = s
                        custome--
                        QuntityData()
                    }


                }


            }

        }

        @SuppressLint("SetTextI18n")
        private fun QuntityData() {
//            binding.finalquantity.text = Quntity.toString()
            binding.qty.text = custome.toString()
            val payprice = custome.toInt() * currentdata.price.toInt()
            val PaybleAmt = payprice - (payprice / currentdata.user_discount.toInt())


            val savedAmount = payprice * currentdata.user_discount.toInt() / 100
            binding.save.text = withSuffixAmount2(savedAmount.toString()) + " Saved here"

            //    binding.totalPrice.text = itemView.context.getString(R.string.payable_amount_s,PaybleAmt.toString())

            val voucher = VouchersData(
                0,
                currentdata.Mini_icon,
                currentdata.Mininame,
                currentdata.price,
                PaybleAmt.toString(),
                custome.toString(),
                currentdata.voucherlist_id,
                currentdata.denomination_key,
                currentdata.denomination_id
            )

            if (custome.toInt() < 1) {
                clickListener.onRecyclerItemClick(custome.toInt(), voucher, "delete")
            } else {
                clickListener.onRecyclerItemClick(custome.toInt(), voucher, "add")
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            VouchersDenominationLayoutBinding.inflate(
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