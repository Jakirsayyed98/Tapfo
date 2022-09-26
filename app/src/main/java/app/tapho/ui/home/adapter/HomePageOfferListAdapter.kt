package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowMerchantDeales2Binding
import app.tapho.databinding.RowMerchantDealsBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.merchants.adapter.MerchantDealOfferAdapter
import app.tapho.ui.merchants.model.OfferData
import com.bumptech.glide.Glide
import java.net.URLDecoder

class HomePageOfferListAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<OfferData, HomePageOfferListAdapter.Holder>() {

    inner class Holder(val binding: RowMerchantDeales2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: OfferData) {
            if (data.mini_app.isEmpty().not()) {
                Glide.with(itemView.context).load(data.mini_app[0].image).circleCrop().into(binding.icon)
            }
            if (data.cashback.isNullOrEmpty().not()) {
                 binding.cashback.text = URLDecoder.decode(data.cashback, "UTF-8")
                binding.cashback.visibility = View.VISIBLE
            } else
                binding.cashback.visibility = View.INVISIBLE

            if (data.type == "2") {

          //      binding.linearLayout.setBackgroundColor(Color.parseColor("#0DCA42"))

            } else {
   //             binding.linearLayout.setBackgroundColor(Color.parseColor("#0DCA42"))
            }

//            binding.name.text=data.mini_app.get(0)
            binding.discription.text =data.name

            binding.label.text = data.label
            //   binding.daysTv.text = itemView.context.getString(R.string._days_left, data.end)

            binding.label.visibility = if (data.label.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowMerchantDeales2Binding.inflate(
                LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
        holder.itemView.setOnClickListener {
            clickListener.onRecyclerItemClick(0, list[position], "")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}