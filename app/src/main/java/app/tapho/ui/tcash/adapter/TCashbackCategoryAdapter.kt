package app.tapho.ui.tcash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowTimeBinding
import app.tapho.databinding.TcashbackcategorytablayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.TimeModel
import app.tapho.ui.tcash.model.TransactionStatusModelCustome

class TCashbackCategoryAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<TransactionStatusModelCustome, TCashbackCategoryAdapter.Holder>() {

    inner class Holder(val binding: TcashbackcategorytablayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: TransactionStatusModelCustome) {
            binding.tab.text = s.name
            binding.tab.isSelected = s.isSelected
            binding.root.setOnClickListener {
                select(s.name)
                clickListener.onRecyclerItemClick(layoutPosition, s, "")
            }
        }
    }

    fun select(text: String) {

        list.forEach {
            if (it.name.equals(text)){
                it.isSelected = true
            }else{
                it.isSelected = false
            }

        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(TcashbackcategorytablayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}