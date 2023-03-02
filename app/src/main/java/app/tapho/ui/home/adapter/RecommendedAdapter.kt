package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RecommendedItemLayoutBinding
//import app.tapho.databinding.RowCashbackPartnerBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class RecommendedAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, RecommendedAdapter<S>.Holder>() {
    private var morePos = 0
    private var showRupee = true

    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    fun setShowRupee(show: Boolean) {
        showRupee = show
    }

    fun removeItem(id: String) {
        var removePos = -1
        list.forEachIndexed { index, s ->
            if (s is MiniApp) {
                if (s.id == id) {
                    removePos = index
                    return@forEachIndexed
                }
            }
        }
        if (removePos != -1) {
            list.removeAt(removePos)
            notifyDataSetChanged()
        }
    }

    inner class Holder(private val binding: RecommendedItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var miniApp: MiniApp? = null
            var tcash: String? = ""
            var cashback: String? = ""
            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                miniApp = s.mini_app
                cashback = s.cashback
                tcash = s.mini_app?.tcash
            } else if (s is MiniApp)
            {
                name = s.name
                image = s.image
                miniApp = s
                tcash = s.tcash
                cashback = s.cashback
            }
            if (morePos != 0 && adapterPosition == morePos - 1) {
                binding.rupee.visibility = View.INVISIBLE
                Glide.with(itemView.context).load(R.drawable.ic_more_right)
                    .into(binding.ivPartner)
                binding.nameTv.text = itemView.context.getString(R.string.more)
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,
                        list,
                        "MiniAppFragment")
                }
            } else {
//                kotlin.runCatching {
//                    binding.cashbackAmountTv.text =
//                        getSpannableCashbackText(if (cashback.isNullOrEmpty()) "" else cashback,
//                            itemView.resources.getColor(
//                                R.color.black))
//                }
                binding.rupee.visibility = if (tcash == "1" && showRupee) View.VISIBLE else View.INVISIBLE
                Glide.with(itemView.context).load(image)
                    .placeholder(R.drawable.loding_app_icon)
                    .circleCrop()
                    .into(binding.ivPartner)
                binding.nameTv.text = name
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)
//                    ActiveCashbackForWebActivity.openWebView(it.context,url,miniAppId,image,tCashType,isFavourite)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RecommendedItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}