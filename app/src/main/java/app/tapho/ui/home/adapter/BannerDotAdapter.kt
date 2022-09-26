package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.databinding.DotlayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.BannerList

class BannerDotAdapter  (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<BannerList, BannerDotAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: BannerList) {
            if (binding is DotlayoutBinding) {
                binding.img.isSelected=true
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerDotAdapter.Holder {
        return Holder(
            DotlayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position]) }

    override fun getItemCount(): Int {
        return list.size
    }
}