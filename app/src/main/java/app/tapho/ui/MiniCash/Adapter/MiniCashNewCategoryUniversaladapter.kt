package app.tapho.ui.MiniCash.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.MinicashMiniappDataBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide
import java.net.URLDecoder

class MiniCashNewCategoryUniversaladapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, MiniCashNewCategoryUniversaladapter<S>.Holder>() {
    private var morePos = 0
    private var showRupee = true


    inner class Holder(private val binding: MinicashMiniappDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var card: String? = ""
            var miniApp: MiniApp? = null
            var tcash: String? = ""
            var cashback: String? = ""
            var discription: String? = ""
            var totalFavCount: String? = ""
            var popular: String? = ""
            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                card = s.mini_app?.card
        //        miniApp = s.mini_app
                cashback = s.cashback
                discription = s.mini_app?.punchline
                tcash = s.mini_app?.tcash
                popular = s.popular_cashback
                totalFavCount = s.mini_app?.total_favourite_count

                if (s.mini_app?.merchant_model != null) {
                    if (popular.equals("0")){
                        binding.populartag.visibility = View.GONE
                    }else  if (popular.equals("1")){
                        binding.populartag.visibility = View.VISIBLE
                    }
                } else   binding.populartag.visibility = View.GONE
            }else if (s is MiniApp) {
                name = s.name
                image = s.image
                card = s.card
                miniApp = s
                tcash = s.tcash.toString()
                cashback = s.cashback
                discription = s.punchline
                popular = s.popular_cashback
                totalFavCount = s.total_favourite_count.toString()

                if (s.merchant_payout != null) {
                    if (s.merchant_payout!!.popular_cashback.equals("0")){
                        binding.populartag.visibility = View.GONE
                        binding.star.visibility = View.GONE
                    }else  if (s.merchant_payout!!.popular_cashback.equals("1")){
                        binding.populartag.visibility = View.VISIBLE
                        binding.star.visibility = View.VISIBLE
                    }
                } else  {
                    binding.populartag.visibility = View.GONE
                    binding.star.visibility = View.GONE
                }
            }

            if (morePos != 0 && adapterPosition == morePos - 1) {

                Glide.with(itemView.context).load(R.drawable.ic_more_right).into(binding.ivPartner)
                binding.nameTv.text = itemView.context.getString(R.string.more)
                binding.discription.text = discription
                binding.lovepeople.text = totalFavCount+" people liked"

                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, list, "MiniAppFragment")
                }
            } else {
                kotlin.runCatching {
//
                    binding.cashback.text = if (cashback.isNullOrEmpty()) "" else URLDecoder.decode(
                        cashback,
                        "UTF-8"
                    ) //getSpannableCashbackText(cashback,itemView.resources.getColor(R.color.new_green2))

                }


//                if (popular.equals("0")){
//                    binding.populartag.visibility = View.GONE
//                }else  if (popular.equals("1")){
//                    binding.populartag.visibility = View.VISIBLE
//                }
                Glide.with(itemView.context).load(image).centerCrop().into(binding.ivPartner)
                Glide.with(itemView.context).load(card).centerCrop().into(binding.cardIcon)
                binding.discription.text = discription
                binding.nameTv.text = name
                binding.lovepeople.text = totalFavCount+" people liked"
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)
                    //   ActiveCashbackForWebActivity.openWebView(it.context,url,miniAppId,image,tCashType,isFavourite)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            MinicashMiniappDataBinding.inflate(
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