package app.tapho.ui.TapfoFi.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.TapfoFi.Model.TapfoFiCategories.Data
import app.tapho.ui.model.AppCategory
import com.bumptech.glide.Glide

class TapfoFiCategoryAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoFiCategoryAdapter<S>.Holder>() {

    inner class Holder(private val binding: LayoutCategoriesLayout1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: S) {
            var name=""
            var image=""
            val data : AppCategory?=null

            if (s is Data){
                name = s.name.toString().split(" ")[0]
                image =s.image.toString()
                binding.nameTv.text = name
                binding.nameTv.setTextColor(Color.BLACK)
                binding.image.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s, "AppCategory")
                }
            }


            when (name) {
                itemView.context.getString(R.string.more) -> {
                    Glide.with(itemView.context).load(R.drawable.new_down_icon).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.image)
                }
                itemView.context.getString(R.string.less) -> {
                    Glide.with(itemView.context).load(R.drawable.upicon_icon).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.image)
                }
                else -> {
                    Glide.with(itemView.context).load(image).placeholder(R.drawable.loding_app_icon)//.circleCrop()//.apply(RequestOptions().transform(CenterCrop(), RoundedCorners(ROUND_CORNER_RADIUS)).placeholder(R.drawable.logo_circle))
                        .into(binding.image)
                }
            }




        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            LayoutCategoriesLayout1Binding.inflate(
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