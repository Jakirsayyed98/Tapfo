package app.tapho.ui.wallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowPopularDenominationBinding
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.wallet.model.DenominationModel

class PopularDenominationAdapter :
    BaseRecyclerAdapter<DenominationModel, PopularDenominationAdapter.Holder>() {

    inner class Holder(val binding: RowPopularDenominationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: DenominationModel) {
            binding.amountTv.text =
                itemView.context.getString(R.string.rupee_, data.amount.toString())
            binding.root.isSelected=data.isSelected
            binding.root.setOnClickListener {
                list.forEach {
                    it.isSelected=false
                }
                data.isSelected=true
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowPopularDenominationBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}