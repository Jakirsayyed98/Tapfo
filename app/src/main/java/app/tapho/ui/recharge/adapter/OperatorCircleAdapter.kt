package app.tapho.ui.recharge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowOperatorCircleBinding
import app.tapho.ui.BaseRecyclerAdapter

class OperatorCircleAdapter : BaseRecyclerAdapter<String, OperatorCircleAdapter.Holder>() {

    inner class Holder(val binding: RowOperatorCircleBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowOperatorCircleBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }
}