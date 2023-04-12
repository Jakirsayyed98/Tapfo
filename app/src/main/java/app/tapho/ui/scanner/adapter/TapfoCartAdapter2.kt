package app.tapho.ui.scanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart
import com.bumptech.glide.Glide

class TapfoCartAdapter2<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoCartAdapter2<S>.Holder>() {

    inner class Holder(private val binding: TapfocartLayout2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(s: S) {
            if (s is Cart) {
                Glide.with(itemView.context).load(s.image).placeholder(R.drawable.loding_app_icon)
                    .centerCrop().into(binding.image)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            TapfocartLayout2Binding.inflate(
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