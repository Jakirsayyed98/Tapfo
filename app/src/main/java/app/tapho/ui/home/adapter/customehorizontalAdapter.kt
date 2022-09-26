package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.CustomeCategoryHorizontalAdapterBinding
import app.tapho.databinding.FinanceCategoryLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide

class customehorizontalAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeShopCategoryModel, customehorizontalAdapter.Holder>() {

    inner class Holder(val binding: CustomeCategoryHorizontalAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeShopCategoryModel) {
            //binding.imageIv.setImageResource(data.image)

            Glide.with(itemView.context).load(data.image).into(binding.image)
            binding.nameTv.text = data.name
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.type,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(CustomeCategoryHorizontalAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}