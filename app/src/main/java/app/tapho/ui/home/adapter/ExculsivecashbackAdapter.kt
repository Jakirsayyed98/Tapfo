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
import app.tapho.databinding.CategoriesCashbackCardBinding
import app.tapho.databinding.ExculsivecashbackadapterBinding
import app.tapho.databinding.InTheSpotlightBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.Categorys.adapter.CategoryCardAdapter
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.ui.model.Popular
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class ExculsivecashbackAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ExculsivecashbackAdapter<S>.Holder>() {

    inner class Holder(private val binding: ExculsivecashbackadapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var id:String?=""
            var image: String? = ""
            var url:String?=""
            var banner:String?=""


            if (s is BannerList) {
                banner = s.image
                url = s.url
            } else if (s is MiniApp) {
                id=s.id
                image = s.image
                url=s.url
                banner="https://tapfo.in/delta/public/storage/all_images/"+s.image
            }
            Glide.with(itemView.context).load(banner).into(binding.background)
            binding.MiniAppCard.setOnClickListener {

                clickListener.onRecyclerItemClick(0,url.toString(),"")

              //  NewWebViewActivity.openWebView(itemView.context, url, id, "", "", "", "", "")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ExculsivecashbackadapterBinding.inflate(
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
}