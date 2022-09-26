package app.tapho.ui.home.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.InTheSpotlightBinding
//import app.tapho.databinding.TrandingGamesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.adapter.GamezopTrendingGameAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide

class InTheSpotLightAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, InTheSpotLightAdapter<S>.Holder>()  {

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

    inner class Holder(private val binding: InTheSpotlightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is BannerList) {
                Glide.with(itemView.context).load(s.image).centerCrop().into(binding.banner)
                binding.root.setOnClickListener {

                    clickListener.onRecyclerItemClick(0, s.url, OPEN_WEB_VIEW)
//                    clickListener.onRecyclerItemClick(0, WebViewActivity.openWebView(itemView.context, s.url, "", "", s.type, "", "", ""), OPEN_WEB_VIEW)
//                    clickListener.onRecyclerItemClick(0,   NewWebViewActivity.openWebView(itemView.context, s.url.toString(), "", "", s.type, "", "", ""), OPEN_WEB_VIEW)
                }
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            InTheSpotlightBinding.inflate(
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