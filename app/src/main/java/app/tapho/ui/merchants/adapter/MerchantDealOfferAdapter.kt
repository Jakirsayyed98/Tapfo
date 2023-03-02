package app.tapho.ui.merchants.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.addRupeesSign
import app.tapho.databinding.RowMerchantDealsBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.merchants.model.OfferData
import com.bumptech.glide.Glide
import java.net.URLDecoder

class MerchantDealOfferAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<OfferData, MerchantDealOfferAdapter.Holder>() {

    inner class Holder(val binding: RowMerchantDealsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun setData(data: OfferData) {
            kotlin.runCatching {
                if (data.mini_app.isEmpty().not()) {
                    Glide.with(itemView.context).load(data.mini_app[0].logo).placeholder(R.drawable.loding_app_icon).into(binding.logoIv)
                }else{
                    Glide.with(itemView.context).load(R.drawable.loding_app_icon).placeholder(R.drawable.loding_app_icon).into(binding.cardimage)
                }
                if (data.image.isNullOrEmpty().not()) {
                    Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loding_app_icon).into(binding.cardimage)

                }else{
                    Glide.with(itemView.context).load(R.drawable.loding_app_icon).into(binding.cardimage)
                }


                if (data.tcash.toString()=="1") {
                    binding.cashback.visibility = View.VISIBLE
                } else{
                    binding.cashback.visibility = View.GONE
                }
                binding.descriptionTv.text =
                    data.name.replace("_","₹").replace("Rs.","₹").replace("Rs ","₹").replace("Rs","₹")
                        .replace("RS","₹")


                binding.offerTv.text = data.label!!.replace("_","₹").replace("Rs.","₹").replace("Rs ","₹").replace("Rs","₹")
                    .replace("RS","₹")


                binding.visited.text = data.visited+" Claimed"
                binding.offerTv.visibility = if (data.label.isNullOrEmpty()) View.GONE else View.VISIBLE

                binding.voucher.text = if (data.type=="2") " offer" else " coupons"
                binding.voucher.setBackgroundColor(if (data.type=="2") Color.BLACK else Color.parseColor("#FF7C2B"))

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowMerchantDealsBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
        holder.itemView.setOnClickListener {
            clickListener.onRecyclerItemClick(0, list[position], "")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}