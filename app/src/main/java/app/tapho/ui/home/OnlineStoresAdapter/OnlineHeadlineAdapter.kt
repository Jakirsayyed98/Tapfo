package app.tapho.ui.home.OnlineStoresAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.Headline


class OnlineHeadlineAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Headline/*AppCategory*/, OnlineHeadlineHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineHeadlineHolder {
        return OnlineHeadlineHolder(RowHeadlineBinding.inflate(LayoutInflater.from(parent.context),
            parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: OnlineHeadlineHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}