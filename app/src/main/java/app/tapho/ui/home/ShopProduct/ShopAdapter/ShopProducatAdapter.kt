package app.tapho.ui.home.ShopProduct.ShopAdapter

import android.annotation.SuppressLint
import android.graphics.Color.WHITE
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.CardforFeaturedBinding
//import app.tapho.databinding.HeadlinesLayoutBinding
import app.tapho.databinding.ShopproducatlayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.home.adapter.CashbackPartnerAdapter
import app.tapho.ui.home.adapter.ShopAllProductAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide

class ShopProducatAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ShopProducatAdapter<S>.Holder>() {


    var discountMRP =""
    var cashbackData =""


    inner class Holder(private val binding: ShopproducatlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is Data) {

                if (s.image.toString().contains("https:https://")) {
                    val icondata = s.image?.toString()!!.replace("https:https://", "https://")
                    Glide.with(itemView.context).load(icondata)
                        .into(binding.ivPartner)
                } else
                    if (s.image.toString().contains("https://").not()) {
                        val icondata = "https://" + s.image
                        Glide.with(itemView.context).load(icondata)
                            .into(binding.ivPartner)
                    } else {
                        Glide.with(itemView.context).load(s.image)
                            .into(binding.ivPartner)
                    }


                if (s.mini_app.tcash.equals(0)){
                    if (s.sale_price.isNullOrEmpty()) {
                        discountMRP =s.mrp_price.toString()
                    } else {
                        discountMRP =s.sale_price.toString()
                    }
                    Log.d("Enterdata",discountMRP)
                    if (discountMRP.toString().contains("Sale priceâ\u0082¹ ")){
                        binding.deals.text = "Just at ₹"+ discountMRP.replace("Sale priceâ\u0082¹ ","")
                    }else{
                        binding.deals.text = "Just at ₹"+ discountMRP //+" plus "+cashbackData
                    }

                }else{
                      binding.cashbackTV.text = s.merchant_payout.cashback.let { getSpannableCashbackText(it,itemView.resources.getColor(R.color.white)) }
                    cashbackData = binding.cashbackTV.text.toString()

                    if (s.sale_price.isNullOrEmpty()) {
                        discountMRP =s.mrp_price.toString()
                    } else {
                        discountMRP =s.sale_price.toString()
                    }
                    Log.d("Enterdata",discountMRP)
                    if (discountMRP.toString().contains("Sale priceâ\u0082¹ ")){
                        binding.deals.text = "Just at ₹"+ discountMRP.replace("Sale priceâ\u0082¹ ","")+" plus "+cashbackData
                    }else{
                        binding.deals.text = "Just at ₹"+ discountMRP +" plus "+cashbackData
                    }



                }
                       Glide.with(itemView.context).load(s.mini_app.image).circleCrop().into(binding.icon)
                binding.nameTv.text = s.name

                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s.other_website_url , OPEN_WEB_VIEW)
                }
            }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ShopproducatlayoutBinding.inflate(
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


    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()

    }
}