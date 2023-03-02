package app.tapho.ui.tcash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.DetailLayoutBinding
import app.tapho.databinding.TcashHistoryCategoryLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import com.bumptech.glide.Glide

class customeTransactionCategoriesAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeTcashCategoryModel, customeTransactionCategoriesAdapter.Holder>() {

    inner class Holder(val binding: TcashHistoryCategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeTcashCategoryModel) {
            //binding.imageIv.setImageResource(data.image)
            Glide.with(itemView.context).load(data.image).centerCrop().into(binding.image)
            binding.name.text = data.name
            binding.discription.text = data.discription

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            TcashHistoryCategoryLayoutBinding.inflate(
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