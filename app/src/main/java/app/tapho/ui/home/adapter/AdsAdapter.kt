package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.AdsitemlayoutBinding
import app.tapho.databinding.CategoriesCashbackCardBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class AdsAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AdsAdapter<S>.Holder>() {

    inner class Holder(private val binding: AdsitemlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var image: String? = ""
            var url:String?=""

            if (s is BannerList) {
                image = s.image
                url=s.url

            } else if (s is /*app.tapho.ui.MerchantDatamodelClass.*/MiniApp) {
                image = s.image
                url=s.url

            }
            Glide.with(itemView.context).load(image).centerCrop().into(binding.background)
            binding.MiniAppCard.setOnClickListener {
                NewWebViewActivity.openWebView(itemView.context, url, "", "", "", "", "", "")

              //  WebViewActivity.openWebView(itemView.context,url ,"", "", "", "", "", "")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AdsitemlayoutBinding.inflate(
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