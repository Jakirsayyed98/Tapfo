package app.tapho.ui.merchants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
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
        fun setData(data: OfferData) {
            if (data.mini_app.isEmpty().not()) {
                Glide.with(binding.imageIv).load(data.mini_app[0].image).circleCrop().into(binding.imageIv)
            }
            if (data.cashback.isNullOrEmpty().not()) {
               // binding.cashbackTv.text = URLDecoder.decode(data.cashback, "UTF-8")
       //         binding.cashbackLi.visibility = View.VISIBLE
            } else
        //        binding.cashbackLi.visibility = View.INVISIBLE
/*
//            if (data.type == "2") {
//                binding.offerTv2.text = "Offers"
//                binding.offerTv2.backgroundTintList =
//                    ContextCompat.getColorStateList(itemView.context, R.color.green_light_bg)
//                binding.offerTv2.setTextColor(ContextCompat.getColor(itemView.context,
//                    R.color.green_dark))
//            } else {
//                binding.offerTv2.text = "Coupon"
//                binding.offerTv2.backgroundTintList =
//                    ContextCompat.getColorStateList(itemView.context, R.color.red_light)
//                binding.offerTv2.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
//            }
*/
            binding.nameTv.text=data.mini_app.get(0).name
            binding.descriptionTv.text = data.name
        //    binding.visitedTv.text = itemView.context.getString(R.string._interested, data.visited)
            binding.offerTv.text = data.label
         //   binding.daysTv.text = itemView.context.getString(R.string._days_left, data.end)

            binding.offerTv.visibility = if (data.label.isNullOrEmpty()) View.GONE else View.VISIBLE
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