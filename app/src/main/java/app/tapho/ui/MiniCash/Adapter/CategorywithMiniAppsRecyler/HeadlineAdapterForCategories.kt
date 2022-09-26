package app.tapho.ui.MiniCash.Adapter.CategorywithMiniAppsRecyler

import android.view.LayoutInflater
import android.view.ViewGroup
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.HeadlineHolder
import app.tapho.ui.merchants.model.CashbackMerchantsAllRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory
import app.tapho.ui.model.NewHomeRes.Headline
import app.tapho.ui.model.NewHomeRes.HomeResData

class HeadlineAdapterForCategories (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CashbackMerchantCategory, HeadlineHolderForCategories>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineHolderForCategories {
        return HeadlineHolderForCategories(RowHeadlineBinding.inflate(LayoutInflater.from(parent.context),
            parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: HeadlineHolderForCategories, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}