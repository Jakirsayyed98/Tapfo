package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.CardforFeaturedBinding
import app.tapho.databinding.OfferForYouLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.ui.model.Popular
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class offerforyou_Adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, offerforyou_Adapter<S>.Holder>() {
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

    inner class Holder(private val binding: OfferForYouLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var cardImg: String? = ""
            var cashback: String? = ""
            var miniApp: MiniApp? = null
            var punchline:String?=""

            if (s is CashbackMerchant) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                cardImg = s.mini_app?.card
                miniApp = s.mini_app
                cashback=s.cashback
                punchline=s.mini_app?.punchline
            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                cardImg = s.card
                miniApp = s
                cashback=s.cashback
                punchline=s.punchline
            }

            if (cashback.toString().contains("₹")){
                val cashbackdata =cashback?.let { getSpannableCashbackText(it,itemView.resources.getColor(R.color.black)) }.toString().replace("Upto ","")
                binding.cashbackTv.text=cashbackdata.dropLast(9) +"\nCashback"
            }else if (cashback.toString().contains("%")){
                val cashbackdata =cashback?.let { getSpannableCashbackText(it,itemView.resources.getColor(R.color.black)) }.toString().replace("Upto ","")
                binding.cashbackTv.text=cashbackdata.replaceAfter("%","")+"\nCashback"
            }

            if (cardImg.toString().isNullOrEmpty()){
                Toast.makeText(itemView.context,"Card Image is empty",Toast.LENGTH_SHORT).show()
            }else{
                Glide.with(itemView.context).load(cardImg)
                    .into(binding.ivCardpartner)
            }

            if (image.toString().isNullOrEmpty()){
                Toast.makeText(itemView.context,"Card Image is empty",Toast.LENGTH_SHORT).show()
            }else{
                Glide.with(itemView.context).load(image).circleCrop()
                    .into(binding.ivPartner)
            }

            binding.nameTv.text = name

            binding.discription.text=punchline
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)

            }
         //   binding.cashbackTv.text ="Get "+ cashback?.let { getSpannableCashbackText(it,itemView.resources.getColor(R.color.black)) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            OfferForYouLayoutBinding.inflate(
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