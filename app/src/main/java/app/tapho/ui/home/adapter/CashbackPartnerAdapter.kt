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
import app.tapho.databinding.RowFeaturedPartnerBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class CashbackPartnerAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, CashbackPartnerAdapter<S>.Holder>() {
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

    inner class Holder(private val binding: CardforFeaturedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var cashback: String? = ""
            var miniApp: MiniApp? = null
            var discription:String?=""

            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                miniApp = s.mini_app
                cashback=s.cashback
                discription=s.mini_app?.punchline
            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                miniApp = s
                cashback=s.cashback
                discription=s.punchline
            }
/*
//            if (morePos != 0 && adapterPosition == morePos - 1) {
//                Glide.with(itemView.context).load(R.drawable.ic_more_right)
//                    .into(binding.ivPartner)
//                binding.nameTv.text = itemView.context.getString(R.string.more)
//                binding.root.setOnClickListener {
//                    clickListener.onRecyclerItemClick(0,
//                        list,
//                        "MiniAppFragment")
//                }
//            } else {

 */
            Glide.with(itemView.context).load(image).circleCrop()
                .into(binding.ivPartner)
            binding.nameTv.text = name
            binding.grab.text="Grab Offer"
            binding.discription.text=discription
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)
//                }
            }
            binding.cashbackTv.text ="Get "+ cashback?.let { getSpannableCashbackText(it,itemView.resources.getColor(
                R.color.black)) }
//            setSpannable(s.cashback, binding.cashbackTv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(CardforFeaturedBinding.inflate(LayoutInflater.from(parent.context),
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