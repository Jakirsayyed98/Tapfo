package app.tapho.ui.recharge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowPlansBinding
import app.tapho.ui.BaseRecyclerAdapter

class PlansAdapter : BaseRecyclerAdapter<String, PlansAdapter.Holder>() {

    inner class Holder(val binding: RowPlansBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowPlansBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }
}