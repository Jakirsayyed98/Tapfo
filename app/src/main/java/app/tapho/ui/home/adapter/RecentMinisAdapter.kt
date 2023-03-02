package app.tapho.ui.home.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.HomePagerecommendedStoreLayoutBinding
import app.tapho.databinding.MyhomepopularLayoutBinding
import app.tapho.databinding.RecentLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class RecentMinisAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, RecentMinisAdapter<S>.Holder>() {
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

    inner class Holder(private val binding: RecentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var image: String? = ""
            var name: String? = ""
            var miniapp: MiniApp? = null

             if (s is MiniApp) {
                image = s.image
                 miniapp = s
                 name  = s.name
            }



            binding.nameTv.text = name
            if (image.toString().isNullOrEmpty()){

            }else{
                Glide.with(itemView.context).load(image).circleCrop().placeholder(R.drawable.loding_app_icon)  .into(binding.image)
            }

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, miniapp, OPEN_WEB_VIEW)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RecentLayoutBinding.inflate(
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