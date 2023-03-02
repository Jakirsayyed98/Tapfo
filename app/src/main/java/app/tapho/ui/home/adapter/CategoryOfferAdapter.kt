package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowOfferCategoryBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.AppCategory
import com.bumptech.glide.Glide

class CategoryOfferAdapter (private val clickListener: RecyclerClickListener) : BaseRecyclerAdapter<AppCategory, CategoryOfferAdapter.Holder>(){

    inner class Holder(val binding:RowOfferCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun setData(data:AppCategory){
            Glide.with(binding.imageIv).load(data.image).into(binding.imageIv)
            binding.titleTv.text=data.name
            binding.offerTv.text = data.offer_count+" Offers | " + data.coupon_count+" Coupons"

//            (data.getDeals()+" Deals").also { binding.offerTv.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowOfferCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
        holder.itemView.setOnClickListener {
            clickListener.onRecyclerItemClick(0,list[position],"")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}