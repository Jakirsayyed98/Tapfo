package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.EndtoendencriptionLayoutBinding
import app.tapho.databinding.FoodSuperlayoutBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel1
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import com.bumptech.glide.Glide

class EndtoEndEncriptionPointsAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeEndToEndModel, EndtoEndEncriptionPointsAdapter.Holder>() {

    inner class Holder(val binding: EndtoendencriptionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeEndToEndModel) {

            if (data.image.toString().isEmpty().not()){
                Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loding_app_icon).into(binding.icon)
            }
            binding.t1.text = data.name
            binding.t2.text = data.discription

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            EndtoendencriptionLayoutBinding.inflate(
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