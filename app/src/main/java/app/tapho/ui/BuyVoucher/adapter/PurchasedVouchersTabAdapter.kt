package app.tapho.ui.BuyVoucher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowCatgoryTabforminiBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.PurchasedVoucherTabModel
import app.tapho.ui.model.AppCategory

class PurchasedVouchersTabAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, PurchasedVouchersTabAdapter<S>.Holder>() {

    inner class Holder(val binding: RowCatgoryTabforminiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var name: String? = ""
            var isSelected = false
            if (data is PurchasedVoucherTabModel) {
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
            if (it is PurchasedVoucherTabModel)
                it.isSelected = false
            else if (it is AppCategory)
                it.isSelected = false
        }
        if (data is PurchasedVoucherTabModel)
            data.isSelected = true
        else if (data is AppCategory)
            data.isSelected = true
    }

    override fun getItemCount(): Int {
        return list.size
    }
}