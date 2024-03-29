package app.tapho.ui.home.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.CategoriesCashbackCardBinding
import app.tapho.databinding.SponsoredCardLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.ui.model.PromotedApp
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class SponsoredAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, SponsoredAdapter<S>.Holder>() {

    inner class Holder(private val binding: SponsoredCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var url:String?=""

            if (s is PromotedApp) {
                name = s.name
                image = s.image
                url=s.url

            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                url=s.url
            }
            Glide.with(itemView.context).load(image).circleCrop().into(binding.ivPartner)
            binding.nameTv.text=name
            binding.root.setOnClickListener {
               clickListener.onRecyclerItemClick(0,url,"")

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            SponsoredCardLayoutBinding.inflate(
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