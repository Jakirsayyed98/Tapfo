package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.CustomeLayoutCategoriesBinding
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.databinding.RowProfileBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide


class CustomeShopCategoryAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeShopCategoryModel, CustomeShopCategoryAdapter.Holder>() {

    inner class Holder(val binding: CustomeLayoutCategoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeShopCategoryModel) {
            //binding.imageIv.setImageResource(data.image)
            Glide.with(itemView.context).load(data.image)/*.circleCrop()*/.into(binding.image)
            binding.discription.visibility= View.GONE
            binding.nameTv.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.type,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(CustomeLayoutCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}