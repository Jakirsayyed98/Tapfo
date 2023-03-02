package app.tapho.ui.tcash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.BannerList
import com.bumptech.glide.Glide

class TcashBannerAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<BannerList, TcashBannerAdapter.Holder>() {

    inner class Holder(private val binding: TcashBannerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: BannerList) {

            Glide.with(itemView.context).load(R.drawable.recharge_and_bills_icon).centerCrop().into(binding.icon)
            binding.name.text =data.title
            binding.discription.text = data.description
            Glide.with(itemView.context).load(data.icon).centerCrop().into(binding.icon)
            Glide.with(itemView.context).load(data.image).centerCrop().into(binding.banner)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TcashBannerAdapter.Holder {

        return Holder(
            TcashBannerLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
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