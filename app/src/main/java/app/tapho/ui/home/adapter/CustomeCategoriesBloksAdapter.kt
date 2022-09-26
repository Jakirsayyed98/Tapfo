package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.CategoryBlockBinding
import app.tapho.databinding.CustomeLayoutCategoriesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide

class CustomeCategoriesBloksAdapter  (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeShopCategoryModel, CustomeCategoriesBloksAdapter.Holder>() {

    inner class Holder(val binding: CategoryBlockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeShopCategoryModel) {
            //binding.imageIv.setImageResource(data.image)

            if (data.type=="BuyGiftCard"){
                binding.coming.visibility=View.VISIBLE
            }
            if (data.type=="Bills"){
                binding.coming.visibility=View.VISIBLE
            }


            Glide.with(itemView.context).load(data.image).into(binding.image)
            binding.nameTv.text = data.name
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.type,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(CategoryBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}