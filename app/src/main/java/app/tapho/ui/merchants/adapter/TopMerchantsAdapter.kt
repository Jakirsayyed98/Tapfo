package app.tapho.ui.merchants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowTopMerchantsBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TopMerchantsAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<MiniApp, TopMerchantsAdapter.Holder>() {

    inner class Holder(private val binding: RowTopMerchantsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: MiniApp) {
            Glide.with(binding.imageIv).load(data.image)
                .transform(RoundedCorners(ROUND_CORNER_RADIUS))
                .placeholder(R.drawable.ic_tab3_selected)
                .into(binding.imageIv)
            binding.headingTv.text = data.name
            binding.punchlineTv.text = data.punchline
            binding.rupee.visibility = if (data.tcash == "1") View.VISIBLE else View.INVISIBLE

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
//                    WebViewActivity.openWebView(it.context,url,miniAppId,image,tCashType,isFavourite)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowTopMerchantsBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}