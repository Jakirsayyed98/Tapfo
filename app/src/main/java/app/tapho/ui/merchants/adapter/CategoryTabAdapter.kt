package app.tapho.ui.merchants.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowCatgoryTabforminiBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory

class CategoryTabAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, CategoryTabAdapter<S>.Holder>() {

    inner class Holder(val binding: RowCatgoryTabforminiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var name: String? = ""
            var isSelected = false
            var id = ""
            if (data is CashbackMerchantCategory) {
                name = data.name
                isSelected = data.isSelected
                id = data.id.toString()
            } else if (data is AppCategory) {
                name = data.name
                isSelected = data.isSelected
                id = data.id.toString()
            }
            binding.tab.text = name
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                unSelectAll(data)
                isSelected = true
                clickListener.onRecyclerItemClick(0, data, "tabClick")
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowCatgoryTabforminiBinding.inflate(LayoutInflater.from(parent.context),
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