package app.tapho.ui.games.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.GamecategoryLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.models.GamezopCategory.Data
import app.tapho.ui.model.AppCategory
import com.bumptech.glide.Glide

class GamezopCategoriAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, GamezopCategoriAdapter<S>.Holder>() {

    inner class Holder(val binding: GamecategoryLayoutBinding)  : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var name: String? = ""
            var id: String? = ""
            var image: String? = ""
            var isSelected = false
            if (data is Data) {
                name = data.name
                image = data.image
                id = data.id
                isSelected = data.isSelected
            } else if (data is AppCategory) {
                name = data.name
                image = data.image
                isSelected = data.isSelected
            }
            kotlin.runCatching {
                Glide.with(itemView.context).load(image).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.image)
            }
            binding.nameTv.text = name
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                unSelectAll(data)
                isSelected = true
                clickListener.onRecyclerItemClick(0, data, "")
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            GamecategoryLayoutBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    private fun unSelectAll(data: S) {
        list.forEach {
            if (it is Data)
                it.isSelected = false
            else if (it is AppCategory)
                it.isSelected = false
        }
        if (data is Data)
            data.isSelected = true
        else if (data is AppCategory)
            data.isSelected = true
    }

    override fun getItemCount(): Int {
        return list.size
    }
}