package app.tapho.ui.Faqs.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import app.tapho.databinding.FaqsheadlinesLayoutBinding
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.Faqs.Model.Data
import app.tapho.ui.Faqs.Model.Faqsmodel
import app.tapho.ui.home.adapter.HeadlineHolder
import app.tapho.ui.model.NewHomeRes.Headline

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