package app.tapho.ui.home.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.InTheSpotlightBinding
import app.tapho.databinding.VerticalBannerDataBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide

class VerticalBannerAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, VerticalBannerAdapter<S>.Holder>()  {



    inner class Holder(private val binding: VerticalBannerDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is BannerList) {
                Glide.with(itemView.context).load(s.image).centerCrop().into(binding.banner)
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s.url, OPEN_WEB_VIEW)
                }
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            VerticalBannerDataBinding.inflate(
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