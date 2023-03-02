package app.tapho.ui.home.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.CardforFeaturedBinding
import app.tapho.databinding.HomecashbackpartnerlayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.ui.model.Service
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class HomeCashbackPartnerAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, HomeCashbackPartnerAdapter<S>.Holder>() {
    private var morePos = 0

    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
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

    inner class Holder(private val binding: HomecashbackpartnerlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var card: String? = ""
            var cashback: String? = ""
            var url: String? = ""
            var miniApp: MiniApp? = null
            var discription:String?=""

            if (s is BannerList) {
                url = s.url
                card = s.image
            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                card= s.card
                miniApp = s
                cashback=s.cashback
                discription=s.punchline
            }

            Glide.with(itemView.context).load(image).circleCrop().into(binding.ivPartner)
            Glide.with(itemView.context).load(card).centerCrop().into(binding.card)
            binding.nameTv.text = name
            binding.discription.text=discription
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, url, OPEN_WEB_VIEW)
//                }
            }
//            setSpannable(s.cashback, binding.cashbackTv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            HomecashbackpartnerlayoutBinding.inflate(
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

    private fun setSpannable(s: String?, textView: TextView) {
        try {
            textView.text = SpannableString(s).apply {
                setSpan(StyleSpan(Typeface.BOLD), 5, length - 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        } catch (e: Exception) {
            textView.text = s
        }

    }
}