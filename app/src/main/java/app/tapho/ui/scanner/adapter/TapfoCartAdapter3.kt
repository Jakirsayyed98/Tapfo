package app.tapho.ui.scanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.scanner.model.SearchCurrentOrder.Item
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide

class TapfoCartAdapter3<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoCartAdapter3<S>.Holder>() {

    inner class Holder(private val binding: Tapfocart3LayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: S) {
            if (s is Item){
                s.business_user_item_detail.let{
                    Glide.with(itemView.context).load(it.image).into(binding.image)
                    binding.nameTv.text = it.name + " ( "+s.qty+" )"
                    binding.price.text = withSuffixAmount(it.mrp)
                    binding.disprice.text = withSuffixAmount(it.price)
                    binding.totalPrice.text = withSuffixAmount(s.total_price)
                }

            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            Tapfocart3LayoutBinding.inflate(
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