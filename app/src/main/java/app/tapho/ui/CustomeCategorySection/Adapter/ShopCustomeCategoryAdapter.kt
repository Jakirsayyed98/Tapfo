package app.tapho.ui.CustomeCategorySection.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.CustomehopcategorylayoutBinding
import app.tapho.databinding.ShopproducatlayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.home.adapter.ShopAllProductAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class ShopCustomeCategoryAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ShopCustomeCategoryAdapter<S>.Holder>() {
    private var morePos = 0
    var cashbackdata = ""
    var cashbackFinal = ""
    var DMrp = ""
    var totalplays=""

    inner class Holder(private val binding: CustomehopcategorylayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            val randomViews = (500..10000).random()
            suffixFunction(randomViews.toDouble())

            binding.views.text = totalplays
            if (s is Data) {
                if (s.mrp_price.toString() == s.sale_price.toString()){
                    binding.cutprice.visibility = View.GONE
                }

                if (s.mrp_price.isNullOrEmpty() && s.sale_price.isNullOrEmpty()){
                    binding.root.visibility = View.GONE
                }

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
                    binding.cashbackPer.visibility = View.GONE
                }else{
                    binding.cashbackPer.text = s.merchant_payout.cashback.let { getSpannableCashbackText(it,itemView.resources.getColor(R.color.black)) }
                }

                Glide.with(itemView.context).load(s.mini_app.image).circleCrop().into(binding.icon)
                binding.nameTv.text = s.name
                if (s.sale_price.isNullOrEmpty()) {
                    if (!s.mrp_price.contains("₹")){
                        if (s.mrp_price.toString().contains("Sale priceâ\u0082¹")){
                            binding.dMrp.text = withSuffixAmount(s.mrp_price.trim().replace("Sale priceâ\u0082¹",""))
                        }else{
                            binding.dMrp.text = withSuffixAmount(s.mrp_price.trim())//+" plus "+cashbackData
                        }

                    }

                    binding.cutprice.visibility = View.GONE
                    binding.cashbackPercent.visibility = View.GONE
                } else {
                    binding.cashbackPercent.visibility = View.VISIBLE
                    if (!s.mrp_price.contains("₹")){
                        binding.mrp.text = withSuffixAmount(s.mrp_price.trim())
                    }
                    if (!s.sale_price.contains("₹")){
                        if (s.sale_price.toString().contains("Sale priceâ\u0082¹ ")){
                            binding.dMrp.text = withSuffixAmount(s.sale_price.trim().replace("Sale priceâ\u0082¹ ",""))
                        }else{
                            binding.dMrp.text = withSuffixAmount(s.sale_price.trim())//+" plus "+cashbackData
                        }
                    }

                    if (s.mrp_price.contains("""""")){
                        binding.cutprice.visibility = View.GONE
                    }
                    if (s.mrp_price.isNullOrEmpty().not()) {
                        if (s.sale_price.contains("₹").not()){
                            var calculate = s.sale_price.toString().replace(",", "").toDouble() / s.mrp_price.toString().replace(",", "").toDouble() * 100
                            var percentdata = 100.00 - calculate.toDouble()
                            var data = String.format("%.0f", percentdata).toDouble().toString()

                            if (data.dropLast(2).toString().equals("0")){
                                binding.cashbackPercent.visibility = View.GONE
                            }else{
                                binding.cashbackPercent.visibility = View.VISIBLE
                                binding.cashbackPercent.text = "(" + data.dropLast(2) + "% off)"
                            }


                        }



                    }
                }
                if (s.mrp_price.isNullOrEmpty()){
                    binding.cutprice.visibility = View.GONE
                }else{
                    binding.cutprice.visibility = View.VISIBLE
                }

//                binding.sallername.text = "Seller :" + s.mini_app.name
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s.other_website_url , OPEN_WEB_VIEW)
                }
            }
        }
    }

    private fun suffixFunction(count: Double) :String {
        val df = DecimalFormat("#.#")
        totalplays = if (Math.abs(count / 1000000) >= 1) {
            df.format(count / 1000000.0).toString() + "M views"
        } else if (Math.abs(count / 1000.0) >= 1) {
            df.format(count / 1000.0).toString() + "K views"
        } else {
            count.toString()+" views"
        }
        Log.d("numbersnew","$totalplays")
        return totalplays;
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            CustomehopcategorylayoutBinding.inflate(
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