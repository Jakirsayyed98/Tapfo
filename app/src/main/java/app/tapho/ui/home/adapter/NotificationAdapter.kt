package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowNotificationsBinding
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.NotificationData
import app.tapho.ui.model.NotificationData2
import app.tapho.utils.ROUND_CORNER_RADIUS
import app.tapho.utils.getDate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.perfomer.blitz.setTimeAgo

class NotificationAdapter : BaseRecyclerAdapter<NotificationData, NotificationAdapter.Holder>() {

    inner class Holder(val binding: RowNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data2: NotificationData2) {
            Glide.with(binding.logoIv).load(data2.image).apply(
                RequestOptions().transform(
                    RoundedCorners(ROUND_CORNER_RADIUS)
                ).placeholder(R.drawable.ic_notification_holder)
            ).into(binding.logoIv)
            binding.headingTv.text = data2.title
            binding.tvDescription.text = data2.description
            data2.created_at?.let { getDate(it)?.let { date -> binding.dateTv.setTimeAgo(date) } }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowNotificationsBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (list[position].data.isNullOrEmpty().not())
            list[position].data?.get(0)?.let { holder.setData(it) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}