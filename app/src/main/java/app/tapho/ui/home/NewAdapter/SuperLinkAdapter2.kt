package app.tapho.ui.home.NewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.databinding.TapfooneLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import com.bumptech.glide.Glide

class SuperLinkAdapter2 (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeSuperCategoryModel, SuperLinkAdapter2.Holder>() {

    inner class Holder(val binding: TapfooneLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeSuperCategoryModel) {
            if (data.image.toString().contains("gif")){
                Glide.with(itemView.context).asGif().load(data.image).into(binding.image)
            }else{
                Glide.with(itemView.context).load(data.image).into(binding.image)
            }

            binding.nameTv.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            TapfooneLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
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