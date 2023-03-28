package app.tapho.ui.scanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide

class TapfoCartAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoCartAdapter<S>.Holder>() {

    inner class Holder(private val binding: TapfocartLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: S) {
            if (s is Cart){
               binding.nameTv.text = s.name
               binding.qty.text = withSuffixAmount((s.qty.toDouble()*s.price.toDouble()).toString()).toString()
               binding.price.text = withSuffixAmount(s.mrp)
                binding.disprice.text = withSuffixAmount(s.price) +" ( "+s.qty.toString() + " ) "
                Glide.with(itemView.context).load(s.image).into(binding.image)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            TapfocartLayoutBinding.inflate(
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