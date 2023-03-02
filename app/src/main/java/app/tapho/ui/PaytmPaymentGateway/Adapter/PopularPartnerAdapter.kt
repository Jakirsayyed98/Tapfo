package app.tapho.ui.PaytmPaymentGateway.Adapter

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.NewlyaddedlayoutBinding
import app.tapho.databinding.PopularMiniappLayoutBinding
import app.tapho.databinding.PopulargamesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.adapter.PopularBrandAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.text.DecimalFormat

class PopularPartnerAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, PopularPartnerAdapter<S>.Holder>()  {
    var totalplays=""

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

    inner class Holder(private val binding: PopularMiniappLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is Popular) {
                Glide.with(itemView.context).load(s.mini_app?.logo).into(binding.logo)
                binding.cashback.text =   s.cashback!!.replace("Upto","").replace("Cashback","")
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,
                        ActiveCashbackForWebActivity.openWebView(itemView.context, s.mini_app?.url, s.mini_app?.id, s.mini_app?.image, s.mini_app?.tcash, s.mini_app?.is_favourite, s.mini_app?.cashback, s.mini_app?.app_category_id, s.mini_app?.url_type,s.mini_app?.name), OPEN_WEB_VIEW)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            PopularMiniappLayoutBinding.inflate(
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