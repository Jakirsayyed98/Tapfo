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
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.finminiapp_detail
import app.tapho.ui.model.AppCategory
import com.bumptech.glide.Glide

class TapfoFiNotesAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoFiNotesAdapter<S>.Holder>() {

    inner class Holder(private val binding: PointNotesLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: S) {
            if (s is finminiapp_detail){
               binding.nameTv.text = s.detail
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            PointNotesLayoutBinding.inflate(
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