package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.AppCategory
import com.bumptech.glide.Glide

class ItemTypeAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<AppCategory, ItemTypeAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: AppCategory) {
            if (binding is LayoutCategoriesLayoutBinding) {


                when (data.name) {
                    itemView.context.getString(R.string.more) -> {
//                        Glide.with(itemView.context).load(R.drawable.ic_more_blue).circleCrop().into(binding.image)
                        Glide.with(itemView.context).load(R.drawable.new_down_icon).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.image)
                    }
                    itemView.context.getString(R.string.less) -> {
                        Glide.with(itemView.context).load(R.drawable.upicon_icon).placeholder(R.drawable.loding_app_icon).circleCrop()
                            .into(binding.image)
                    }
                    else -> {
                        Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loding_app_icon)//.circleCrop()//.apply(RequestOptions().transform(CenterCrop(), RoundedCorners(ROUND_CORNER_RADIUS)).placeholder(R.drawable.logo_circle))
                            .into(binding.image)
                    }


                }

               // Glide.with(itemView.context).load(data.image).circleCrop().centerCrop().into(binding.image)
                binding.nameTv.text = data.name!!.replaceAfter(" ","")
                binding.image.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "AppCategory")
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTypeAdapter.Holder {

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