package app.tapho.ui.TapfoFi.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.TapfoFi.Model.TapfoFiCategories.Data
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.FinMiniApp
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.finminiapp_detail
import app.tapho.ui.model.AppCategory
import app.tapho.utils.setOnCustomeCrome
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide

class TapfoFiMiniAppsAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoFiMiniAppsAdapter<S>.Holder>() {

    inner class Holder(private val binding: TapfofiMiniappLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(s: S) {
            val name = ""
            var image = ""
            if (s is FinMiniApp) {
                if (s.card.isNullOrEmpty().not()) {
                    image = s.card.toString()
                }
                if (s.k1.isNullOrEmpty()) {
                    binding.k1Layout.visibility = View.GONE
                }

                if (s.k2.isNullOrEmpty()) {
                    binding.k2Layout.visibility = View.GONE
                }
                binding.label.text = s.punchline
                binding.discription.text = s.description
                binding.k1.text = withSuffixAmount(s.k1)
                binding.k2.text = withSuffixAmount(s.k2)
                if (s.fin_merchant_payout.isNullOrEmpty().not()) {
                    binding.cashback.text = withSuffixAmount(s.fin_merchant_payout.get(0).cashback)

                } else {
                    binding.cashbacklayout.visibility = View.GONE
                }

                binding.nameTv.text = s.name
                binding.nameTv.setTextColor(Color.BLACK)

                val TapfoFiNotesAdapter = TapfoFiNotesAdapter<finminiapp_detail>(object : RecyclerClickListener {
                        override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                        }
                    })
                binding.notes.apply {
                    layoutManager =
                        LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                    adapter = TapfoFiNotesAdapter
                }

                if (s.fin_mini_app_detail.isNullOrEmpty().not()) {
                    TapfoFiNotesAdapter.addAllItem(
                        if (s.fin_mini_app_detail.size > 3) s.fin_mini_app_detail.subList(
                            0,
                            2
                        ) else s.fin_mini_app_detail
                    )
                }
                binding.applynow.setOnClickListener {
                    itemView.context.setOnCustomeCrome(s.url)
                }
                binding.layout.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s, "AppCategory")
                }
            }

            when (name) {
                itemView.context.getString(R.string.more) -> {
                    Glide.with(itemView.context).load(R.drawable.new_down_icon)
                        .placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.image)
                }
                itemView.context.getString(R.string.less) -> {
                    Glide.with(itemView.context).load(R.drawable.upicon_icon)
                        .placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.image)
                }
                else -> {
                    Glide.with(itemView.context).load(image)
                        .placeholder(R.drawable.loding_app_icon)//.circleCrop()//.apply(RequestOptions().transform(CenterCrop(), RoundedCorners(ROUND_CORNER_RADIUS)).placeholder(R.drawable.logo_circle))
                        .into(binding.image)
                }
            }


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            TapfofiMiniappLayoutBinding.inflate(
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