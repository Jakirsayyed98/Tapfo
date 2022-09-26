package app.tapho.ui.MiniCash.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.AppCategory
import com.bumptech.glide.Glide

class MinicashappcategoryAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<AppCategory, MinicashappcategoryAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: AppCategory) {
            if (binding is LayoutCategoriesLayoutBinding) {

             Glide.with(itemView.context).load(data.image).circleCrop().centerCrop().into(binding.image)
                binding.nameTv.text = data.name
                Log.d("miniAppName",data.name.toString())
                binding.image.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "AppCategory")
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinicashappcategoryAdapter.Holder {

        return Holder(
            LayoutCategoriesLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}