package app.tapho.ui.tcash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.databinding.SuperlinkForTcashLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import com.bumptech.glide.Glide

class SuperLinkAdapterFotTcash (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeSuperCategoryModel, SuperLinkAdapterFotTcash.Holder>() {

    inner class Holder(val binding: SuperlinkForTcashLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeSuperCategoryModel) {
            Glide.with(itemView.context).load(data.image).into(binding.image)
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            SuperlinkForTcashLayoutBinding.inflate(
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