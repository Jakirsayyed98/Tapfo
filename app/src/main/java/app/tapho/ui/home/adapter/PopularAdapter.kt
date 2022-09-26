package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowItemBrandBinding
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.Popular
import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class PopularAdapter :
    BaseRecyclerAdapter<Popular, PopularAdapter.Holder>() {

    inner class Holder(private val binding: RowItemBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Popular) {
            Glide.with(itemView.context).load(data.mini_app?.image)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(ROUND_CORNER_RADIUS)
                    )
                )
                .into(binding.image)
            binding.nameTv.text = data.mini_app?.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RowItemBrandBinding.inflate(
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
}