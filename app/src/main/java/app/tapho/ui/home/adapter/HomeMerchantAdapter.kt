package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.HomemerchantadapterlayoutBinding
import app.tapho.databinding.RowMerchantDeales2Binding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.merchants.model.MiniAppRes
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide
import java.net.URLDecoder

class HomeMerchantAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<MiniApp, HomeMerchantAdapter.Holder>() {

    inner class Holder(val binding: HomemerchantadapterlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: MiniApp) {

            Glide.with(itemView.context).load(data.logo).into(binding.icon)
            binding.discription.text = data.punchline
            binding.cashback.text = data.cashback.let { getSpannableCashbackText(it!!,itemView.resources.getColor(R.color.black)) }


            if (data.popular_cashback.isNullOrEmpty()){
                binding.popular.visibility = View.GONE
            }
            if(data.is_favourite!!.contains("0")){
                binding.favbtn.visibility=View.GONE
            }
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            HomemerchantadapterlayoutBinding.inflate(
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