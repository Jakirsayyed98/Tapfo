package app.tapho.ui.tcash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowTimeBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.TimeModel

class TimeAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<TimeModel, TimeAdapter.Holder>() {

    inner class Holder(val binding: RowTimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: TimeModel) {
            binding.timeTv.text = s.time
            binding.timeTv.isSelected = s.isSelected
            binding.root.setOnClickListener {
                select(s.time)
                clickListener.onRecyclerItemClick(layoutPosition, s, "")
            }
        }
    }

    fun select(text: String) {
        list.forEach {
            if (it.time.equals(text)){
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