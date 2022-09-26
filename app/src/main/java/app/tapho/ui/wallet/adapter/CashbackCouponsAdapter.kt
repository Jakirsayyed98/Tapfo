package app.tapho.ui.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowCashbackCouponsBinding
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.wallet.model.CashbackCouponsModel

class CashbackCouponsAdapter :
    BaseRecyclerAdapter<CashbackCouponsModel, CashbackCouponsAdapter.Holder>() {

    inner class Holder(val binding: RowCashbackCouponsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CashbackCouponsModel) {
            binding.cashbackAmountTv.text =
                itemView.context.getString(R.string.rupee_, data.cashback)
            binding.totalAmountTv.text =
                itemView.context.getString(R.string.rupee_, data.amount)
            binding.cashbackPerTv.text =
                itemView.context.getString(R.string._cashback, data.cashback_percentage, "%")
            binding.popularTv.visibility = View.GONE
            binding.radio.isChecked = data.isSelected
            binding.cashbackTv2.isSelected = data.isSelected
            binding.cashbackAmountTv.isSelected = data.isSelected

            binding.radio.setOnClickListener {
                list.forEach {
                    it.isSelected = false
                }
                data.isSelected = true
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowCashbackCouponsBinding.inflate(LayoutInflater.from(parent.context),
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