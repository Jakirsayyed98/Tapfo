package app.tapho.ui.tcash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowTimeBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.TimeModel
import app.tapho.ui.tcash.model.TransactionStatusModelCustome

class TCashbackStatusAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<TransactionStatusModelCustome, TCashbackStatusAdapter.Holder>() {

    inner class Holder(val binding: RowTimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: TransactionStatusModelCustome) {
            binding.timeTv.text = s.name
            binding.timeTv.isSelected = s.isSelected
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
        return Holder(RowTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}