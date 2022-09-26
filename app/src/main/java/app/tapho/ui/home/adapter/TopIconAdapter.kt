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
import app.tapho.databinding.TopiconBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class TopIconAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TopIconAdapter<S>.Holder>() {

    inner class Holder(private val binding: TopiconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var cashback: String? = ""
            var miniApp: MiniApp? = null

            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                miniApp = s.mini_app
                cashback=s.cashback
            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                miniApp = s
                cashback=s.cashback
            }
            Glide.with(itemView.context).load(image).circleCrop()
                .into(binding.ivPartner)
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)
//                }
            }
//            binding.cashbackTv.text = cashback?.let { getSpannableCashbackText(it,itemView.resources.getColor(
//                R.color.black)) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            TopiconBinding.inflate(
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