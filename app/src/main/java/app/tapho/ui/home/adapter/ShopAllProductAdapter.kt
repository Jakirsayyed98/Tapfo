package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.CustomehopcategorylayoutBinding
import app.tapho.databinding.ShopproducatlayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.CustomeCategorySection.Adapter.ShopCustomeCategoryAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class  ShopAllProductAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ShopAllProductAdapter<S>.Holder>() {
    private var morePos = 0
    var cashbackdata = ""
    var cashbackFinal = ""
    var DMrp = ""


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


                var cashback = s.merchant_payout//.cashback.toString()
                if (s.mini_app.tcash.equals(0)){
                    Log.d("CashbackData","${s.name} + $cashback")
                }else{
                  //  binding.cashbackPer.text = s.merchant_payout.cashback.let { getSpannableCashbackText(it,itemView.resources.getColor(R.color.white)) }
                }

                //       Glide.with(itemView.context).load(s.mini_app.image).circleCrop().into(binding.icon)
                binding.nameTv.text = s.name
                /*
//                if (s.sale_price.isNullOrEmpty()) {
//                    if (!s.mrp_price.contains("₹")){
//                        binding.dMrp.text = withSuffixAmount(s.mrp_price.trim())
//                    }
//
//                    binding.cutprice.visibility = View.GONE
//                    binding.cashbackPercent.visibility = View.GONE
//                } else {
//                    if (!s.mrp_price.contains("₹")){
//                        binding.mrp.text = withSuffixAmount(s.mrp_price.trim())
//                    }
//                    if (!s.sale_price.contains("₹")){
//                        binding.dMrp.text = withSuffixAmount(s.sale_price.trim())
//                    }
//
//                    if (s.mrp_price.contains("""""")){
//                        binding.cutprice.visibility = View.GONE
//                    }
//                    if (s.mrp_price.isNullOrEmpty().not()) {
//                        if (s.sale_price.contains("₹").not()){
//                            var calculate = s.sale_price.toString().replace(",", "").toDouble() / s.mrp_price.toString().replace(",", "").toDouble() * 100
//                            var percentdata = 100.00 - calculate.toDouble()
//                            var data = String.format("%.0f", percentdata).toDouble().toString()
//                            binding.cashbackPercent.text = "(" + data.dropLast(2) + "% off)"
//                        }
//
//
//
//                    }
//                }
//                if (s.mrp_price.isNullOrEmpty()){
//                    binding.cutprice.visibility = View.GONE
//                }else{
//                    binding.cutprice.visibility = View.VISIBLE
//                }

//                binding.sallername.text = "Seller :" + s.mini_app.name

                 */
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