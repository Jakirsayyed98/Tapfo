package app.tapho.ui.merchants.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.databinding.RowAllCatIvBinding
import app.tapho.databinding.RowFevCatIvBinding
import app.tapho.databinding.RowSubCateoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.model.AppSubCategory

class SubCategoryTabsAdapter(private val clickListener: RecyclerClickListener) :
    ApiListener<MiniAppRes, Any?>, BaseRecyclerAdapter<AppSubCategory, SubCategoryTabsAdapter.Holder>() {

    fun getSelectedPos():Int{
        list.forEachIndexed { index, appSubCategory ->
            if(appSubCategory.isSelected)
                return index
        }
        return 0
    }

    inner class Holder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: AppSubCategory) {


            if (binding is RowSubCateoryBinding) {
                    binding.catNameTv.text = data.name

            }
            binding.root.isSelected = data.isSelected
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return when (viewType) {
            0 -> {
                Holder(RowAllCatIvBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false))
            }
            1 -> {
                Holder(RowFevCatIvBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false))
            }
            else -> Holder(RowSubCateoryBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])

        holder.binding.root.setOnClickListener {
            list.forEachIndexed { index, appSubCategory ->
                appSubCategory.isSelected = index == position
               // clickListener.onRecyclerItemClick(position, list[position], "sub_cat_click")
                clickListener.onRecyclerItemClick(position, list[position].id, "sub_cat_click")
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onSuccess(t: MiniAppRes?, mess: String?) {
        var imageData=t!!.mini_app?.get(0)?.image

    }

}