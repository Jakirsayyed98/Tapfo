package app.tapho.ui.home.ShopProduct.ShopAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.databinding.ShopcategorylayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.ShopProduct.Model.ShopSubCategory.Data
import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class ShopSubcategoryAdapter  (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Data, ShopSubcategoryAdapter.Holder>() {

    var isSelected = false
    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {
            isSelected=data.isSelected
            if (binding is ShopcategorylayoutBinding) {


                when (data.name) {

                    itemView.context.getString(R.string.All) -> {
                        Glide.with(itemView.context).load(R.drawable.allcategories)
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
                    clickListener.onRecyclerItemClick(0, data, "AppCategory")
                    unSelectAll(data)
                    isSelected = true

                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopSubcategoryAdapter.Holder {

        return Holder(
            ShopcategorylayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    private fun unSelectAll(data: Data) {
        list.forEach {
            if (it is Data)
                it.isSelected = false
        }
        if (data is Data)
            data.isSelected = true

    }


    override fun getItemCount(): Int {
        return list.size
    }
}