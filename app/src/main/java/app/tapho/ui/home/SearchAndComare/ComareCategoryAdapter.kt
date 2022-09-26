package app.tapho.ui.home.SearchAndComare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.CouponcategorytabBinding
import app.tapho.databinding.SearchandcompareBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory
import com.bumptech.glide.Glide

class ComareCategoryAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<SearchCategoryModel, ComareCategoryAdapter.Holder>() {

    inner class Holder(val binding: SearchandcompareBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var isSelected = false
        fun setData(data: SearchCategoryModel) {
            //binding.imageIv.setImageResource(data.image)
            isSelected=data.isSelected
            Glide.with(itemView.context).load(data.image).into(binding.tab)

       //     binding.tab.text = data.name
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                unSelectAll(data)
                isSelected = true
                clickListener.onRecyclerItemClick(0, data.type, "")
                notifyDataSetChanged()
            }
        }
    }
    private fun unSelectAll(data: SearchCategoryModel) {
        list.forEach {
            if (it is SearchCategoryModel)
                it.isSelected = false

        }
        if (data is SearchCategoryModel)
            data.isSelected = true

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            SearchandcompareBinding.inflate(
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