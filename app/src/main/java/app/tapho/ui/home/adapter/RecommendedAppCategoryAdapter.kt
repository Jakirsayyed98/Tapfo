package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.CatNewLayoutBinding
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class RecommendedAppCategoryAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<AppCategory, RecommendedAppCategoryAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: AppCategory) {
            if (binding is CatNewLayoutBinding) {


                when (data.name) {
                    itemView.context.getString(R.string.more) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_more_blue)
                            .into(binding.image)
                    }
                    itemView.context.getString(R.string.ShopCompair) -> {
                        Glide.with(itemView.context).load(R.drawable.shopping)
                            .into(binding.image)
                    }
                    itemView.context.getString(R.string.less) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_less_blue)
                            .into(binding.image)
                    }
                    itemView.context.getString(R.string.All) -> {
                        Glide.with(itemView.context).load(R.drawable.allcategories)
                            .into(binding.image)
                    }
                    itemView.context.getString(R.string.Offers) -> {
                        Glide.with(itemView.context).load(R.drawable.newoffericon)
                            .into(binding.image)
                    }
                    else -> {
                        Glide.with(itemView.context).load(data.image).apply(
                            RequestOptions().transform(
                                CenterCrop(), RoundedCorners(ROUND_CORNER_RADIUS)
                            ).placeholder(R.drawable.logo_circle))
                            .into(binding.image)
                    }
                }

                binding.nameTv.text = data.name
                binding.image.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data.id, "AppCategory")
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedAppCategoryAdapter.Holder {

        return Holder(
            CatNewLayoutBinding.inflate(
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