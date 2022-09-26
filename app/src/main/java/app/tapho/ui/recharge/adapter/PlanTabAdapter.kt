package app.tapho.ui.recharge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowPlanTabBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.CashbackMerchantCategory

class PlanTabAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CashbackMerchantCategory, PlanTabAdapter.Holder>() {

    inner class Holder(val binding: RowPlanTabBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CashbackMerchantCategory) {
            binding.tab.text = data.name
            binding.root.isSelected = data.isSelected
            binding.root.setOnClickListener {
                unSelectAll()
                data.isSelected = true
                clickListener.onRecyclerItemClick(0, data, "tabClick")
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowPlanTabBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    private fun unSelectAll() {
        list.forEach {
            it.isSelected = false
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}