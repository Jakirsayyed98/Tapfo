package app.tapho.ui.TapfoFood.UserUI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.AllRestorantsLayoutBinding
import app.tapho.databinding.FoodCategorylayoutBinding
import app.tapho.databinding.FoodSuperlayoutBinding
import app.tapho.databinding.FoodcartLayoutBinding
import app.tapho.databinding.NearbyRestorantsLayoutBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import com.bumptech.glide.Glide

class AllItemofCartAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<FoodCustomeSuperCategoryModel, AllItemofCartAdapter.Holder>() {

    inner class Holder(val binding: FoodcartLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: FoodCustomeSuperCategoryModel) {


            binding.name.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data,"resturants")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            FoodcartLayoutBinding.inflate(
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