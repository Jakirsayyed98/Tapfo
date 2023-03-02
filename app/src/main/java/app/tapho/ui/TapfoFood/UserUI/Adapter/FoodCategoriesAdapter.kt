package app.tapho.ui.TapfoFood.UserUI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FoodCategorylayoutBinding
import app.tapho.databinding.FoodSuperlayoutBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import com.bumptech.glide.Glide

class FoodCategoriesAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<FoodCustomeSuperCategoryModel, FoodCategoriesAdapter.Holder>() {

    inner class Holder(val binding: FoodCategorylayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: FoodCustomeSuperCategoryModel) {

            if (data.image.toString().isNullOrEmpty().not()){
                Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.image)
            }
            binding.name.text = data.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            FoodCategorylayoutBinding.inflate(
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