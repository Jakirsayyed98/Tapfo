package app.tapho.ui.home.NewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.CustomeLayoutCategoriesBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import com.bumptech.glide.Glide

class SuperLinkAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeShopCategoryModel, SuperLinkAdapter.Holder>() {

    inner class Holder(val binding: SpuerlinkLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeShopCategoryModel) {
            //binding.imageIv.setImageResource(data.image)
            Glide.with(itemView.context).load(data.image).centerCrop().into(binding.image)

//            binding.nameTv.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            SpuerlinkLayoutBinding.inflate(
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