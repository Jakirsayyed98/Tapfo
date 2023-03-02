package app.tapho.ui.News.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowNewsCategoryTabBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.News.Model.AllCategories.Data

import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory

class newCategoryAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, newCategoryAdapter<S>.Holder>() {

    inner class Holder(val binding: RowNewsCategoryTabBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var name: String? = ""
            var id: Int = 1
            var isSelected = false
            if (data is Data) {
                id=data.id
                name = data.name
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
                clickListener.onRecyclerItemClick(id, id, "AppCategory")
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RowNewsCategoryTabBinding.inflate(
                LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    private fun unSelectAll(data: S) {
        list.forEach {
            if (it is Data)
                it.isSelected = false
            else if (it is AppCategory)
                it.isSelected = false
        }
        if (data is Data)
            data.isSelected = true
        else if (data is AppCategory)
            data.isSelected = true
    }

    override fun getItemCount(): Int {
        return list.size
    }
}