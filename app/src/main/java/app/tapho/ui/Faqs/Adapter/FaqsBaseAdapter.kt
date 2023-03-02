package app.tapho.ui.Faqs.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import app.tapho.databinding.FaqsheadlinesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.Faqs.Model.Data


class FaqsBaseAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Data/*AppCategory*/, FaqsQutionAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsQutionAdapter {
        return FaqsQutionAdapter(FaqsheadlinesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: FaqsQutionAdapter, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}