package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.ShopproducatlayoutBinding
import app.tapho.databinding.VoucherCartLayoutBinding
import app.tapho.databinding.VouchersCategoriesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showLong
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.CategoriesModel.Data
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersData
import app.tapho.ui.BuyVoucher.VoucherDetails.CustomeVoucherlistDenomination
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

class  voucher_cart_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, voucher_cart_adapter<S>.Holder>() {

    inner class Holder(private val binding: VoucherCartLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
          if (s is VouchersData){

              var finalQuntity = s.Qty.toInt()
              binding.name.text =s.id.toString() + s.name
              binding.Qty.text =finalQuntity.toString()
              binding.less.setOnClickListener {
                  QuntityData(finalQuntity--.toString(),s)
              }
              binding.add.setOnClickListener {
                  QuntityData(finalQuntity++.toString(),s)
              }
          }
        }
        private fun QuntityData(Quntity: String,s: VouchersData) {
          //  itemView.context.showLong(Quntity)
            binding.Qty.text =Quntity
        val data = VouchersData(s.id.toLong(),s.MiniApp_icon,s.name,s.ActualAmount,s.PaybleAmount,Quntity,s.voucherID,s.denominationId)
            if (Quntity.toInt()<=0){
                clickListener.onRecyclerItemClick(Quntity.toInt(),data,"delete")
            }else {
                clickListener.onRecyclerItemClick(Quntity.toInt(),data,"Update")
            }

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            VoucherCartLayoutBinding.inflate(
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