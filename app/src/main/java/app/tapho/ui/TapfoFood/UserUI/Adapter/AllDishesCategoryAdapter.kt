package app.tapho.ui.TapfoFood.UserUI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import app.tapho.databinding.AlldishescategoryLayoutBinding
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.Headline


class AllDishesCategoryAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Headline/*AppCategory*/, AllDishesCategorieHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDishesCategorieHolder {
        return AllDishesCategorieHolder(AlldishescategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: AllDishesCategorieHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}