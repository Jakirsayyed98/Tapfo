package app.tapho.ui.games.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.CouponcategorytabBinding
import app.tapho.databinding.GamecategoryLayoutBinding
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.databinding.RowGamesCategoryBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.ContainerActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.GamezopCategory.Data
import app.tapho.ui.home.NewAdapter.couponCategoriesAdapter
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory
import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class GamezopCategoriAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, GamezopCategoriAdapter<S>.Holder>() {

    inner class Holder(val binding: GamecategoryLayoutBinding)  : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var name: String? = ""
            var id: String? = ""
            var isSelected = false
            if (data is Data) {
                name = data.name
                id = data.id
                isSelected = data.isSelected
            } else if (data is AppCategory) {
                name = data.name
                isSelected = data.isSelected
            }
            binding.nameTv.text = name
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                unSelectAll(data)
                isSelected = true
                clickListener.onRecyclerItemClick(0, id, id!!)
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