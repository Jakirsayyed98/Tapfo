package app.tapho.ui.News.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.databinding.NewscategoriesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.News.Model.AllCategories.Data
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory


class CategoriesAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Data, CategoriesAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun setData(data: Data) {
            var isSelected = false

            if (binding is NewscategoriesBinding) {
                binding.root.isSelected = isSelected
            //    isSelected=false
                binding.newsCategories.text = data.name
                binding.newsCategories.setOnClickListener {
                    unSelectAll(data)
                    isSelected = true
                    clickListener.onRecyclerItemClick(data.id, data.id.toString(), "AppCategory")
                    notifyDataSetChanged()
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapter.Holder {

        return Holder(
            NewscategoriesBinding.inflate( LayoutInflater.from(parent.context), parent, false )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }
    private fun unSelectAll(data: Data) {
        list.forEach {
                it.isSelected = false
        }
            data.isSelected = true
    }


    override fun getItemCount(): Int {
        return list.size
    }
}