package app.tapho.ui.TapfoFood.UserUI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FoodSuperlayoutBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel1
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import com.bumptech.glide.Glide

class FoodSuperAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<FoodCustomeSuperCategoryModel1, FoodSuperAdapter.Holder>() {

    inner class Holder(val binding: FoodSuperlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: FoodCustomeSuperCategoryModel1) {

            if (data.image.toString().isEmpty().not()){
                Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loding_app_icon).into(binding.image)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            FoodSuperlayoutBinding.inflate(
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