package app.tapho.ui.BuyVoucher.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.ShopproducatlayoutBinding
import app.tapho.databinding.VouchersCategoriesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.CategoriesModel.Data
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

class  voucher_categories_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, voucher_categories_adapter<S>.Holder>() {

    inner class Holder(private val binding: VouchersCategoriesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
          if (s is Data){
              Glide.with(itemView.context).load(s.image).placeholder(R.drawable.staricon).into(binding.ivPartner)
              binding.nameTv.text = s.name

              binding.root.setOnClickListener {
                  clickListener.onRecyclerItemClick(0,s,"")
              }
          }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            VouchersCategoriesLayoutBinding.inflate(
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