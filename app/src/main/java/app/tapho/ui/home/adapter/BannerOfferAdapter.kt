package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowBrandOfferBinding
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.model.BannerList
import com.bumptech.glide.Glide

class BannerOfferAdapter : BaseRecyclerAdapter<BannerList, BannerOfferAdapter.Holder>() {

    inner class Holder(private val binding: RowBrandOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(bannerList: BannerList) {
            Glide.with(itemView.context).load(bannerList.image)
                .into(binding.image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RowBrandOfferBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
        holder.itemView.setOnClickListener {
            WebViewActivity.openWebView(holder.itemView.context,
                list[position].url, "", "", "1", "","","")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}