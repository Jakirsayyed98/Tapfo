package app.tapho.ui.home.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.CashbackCardsBinding
import app.tapho.databinding.ShopproducatlayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.home.ShopProduct.ShopAdapter.ShopProducatAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.getSpannableCashbackText
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide

class CardAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, CardAdapter<S>.Holder>() {
    private var morePos = 0
    var cashbackdata=""


    inner class Holder(private val binding: CashbackCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name:String?=""
            var logo:String?=""
            var pending:String?=""
            var validated:String?=""
            var cashback:String?=""
            var status:String?=""
            var totaltransaction:String?=""

            if (s is TCashDashboardData) {
                cashback= s.user_commision
                status= s.status
                name= s.offer_name

            }
            binding.name.text = name
            when (status!!.uppercase()){
                "VERIFIED" -> {
                    binding.verifiedCashback.text = withSuffixAmount(cashback!!)
                }
                "VALIDATED" -> {
                    binding.verifiedCashback.text =  withSuffixAmount(cashback!!)
                }
                "PENDING" -> {
                    binding.pendingAmt.text =  withSuffixAmount(cashback!!)
                }
                else -> {

                }
            }

            binding.root.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            CashbackCardsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}