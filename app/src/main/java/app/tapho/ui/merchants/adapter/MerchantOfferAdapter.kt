package app.tapho.ui.merchants.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowMerchantOfferBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.models.GamezopCategory.Data
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.ui.model.NotificationData
import com.bumptech.glide.Glide

class MerchantOfferAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<StoreDeals, MerchantOfferAdapter.Holder>() {

    private val localList = ArrayList<StoreDeals>()

    override fun addAllItem(list: List<StoreDeals>) {
        this.list.addAll(list)
        this.localList.addAll(list)
        notifyDataSetChanged()
    }

    fun search(s: String) {
        localList.clear()
        if (s.isEmpty()) {
            localList.addAll(list)
        } else {
            list.forEach {
                if (it.name?.lowercase()?.contains(s.lowercase()) == true) {
                    localList.add(it)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class Holder(val binding: RowMerchantOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: StoreDeals) {
            Glide.with(binding.imageIv).load(data.image).into(binding.imageIv)
            binding.headingTv.text = data.name
            binding.offerTv.text = itemView.context.getString(R.string._offers, data.offer_count)
            binding.couponsTv.text = itemView.context.getString(R.string._coupons, data.coupon_count)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowMerchantOfferBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(localList[position])
        holder.itemView.setOnClickListener { clickListener.onRecyclerItemClick(0, localList[position], "") }
    }

    override fun getItemCount(): Int {
        return localList.size
    }
}