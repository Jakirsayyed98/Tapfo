package app.tapho.ui.home.NewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.CouponcategorytabBinding
import app.tapho.databinding.FinanceCategoryLayoutBinding
import app.tapho.databinding.RowCatgoryTabBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.CustomeFinanceCategoryAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory
import com.bumptech.glide.Glide

class couponCategoriesAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, couponCategoriesAdapter<S>.Holder>() {

    inner class Holder(val binding: CouponcategorytabBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var name: String? = ""
            var id: String? = ""
            var isSelected = false
            if (data is AppCategory) {
                name = data.name
                id = data.id
                isSelected = data.isSelected
            } else if (data is AppCategory) {
                name = data.name
                isSelected = data.isSelected
            }
            binding.tab.text = name
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                unSelectAll(data)
                isSelected = true
                clickListener.onRecyclerItemClick(0, id, "tabClick")
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            CouponcategorytabBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    private fun unSelectAll(data: S) {
        list.forEach {
            if (it is CashbackMerchantCategory)
                it.isSelected = false
            else if (it is AppCategory)
                it.isSelected = false
        }
        if (data is CashbackMerchantCategory)
            data.isSelected = true
        else if (data is AppCategory)
            data.isSelected = true
    }

    override fun getItemCount(): Int {
        return list.size
    }
}