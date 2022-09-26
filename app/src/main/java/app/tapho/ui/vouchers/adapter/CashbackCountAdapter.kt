package app.tapho.ui.vouchers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowCashbackCountBinding
import app.tapho.ui.BaseRecyclerAdapter

class CashbackCountAdapter : BaseRecyclerAdapter<String, CashbackCountAdapter.Holder>() {

    inner class Holder(val binding:RowCashbackCountBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun getItemCount(): Int {
        return 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowCashbackCountBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }
}