package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.databinding.SuperLinksSubCategoryBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import com.bumptech.glide.Glide

class SuperLinksSubCategories (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeShopCategoryModel, SuperLinksSubCategories.Holder>() {

    inner class Holder(val binding: SuperLinksSubCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeShopCategoryModel) {
            //binding.imageIv.setImageResource(data.image)
            Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loding_app_icon).centerCrop().into(binding.icon)

            binding.name.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            SuperLinksSubCategoryBinding.inflate(
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