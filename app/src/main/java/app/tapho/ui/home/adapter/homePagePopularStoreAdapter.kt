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
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class homePagePopularStoreAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, homePagePopularStoreAdapter<S>.Holder>() {
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

    inner class Holder(private val binding: MyhomepopularLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var cardImg: String? = ""
            var cashback: String? = ""
            var miniApp: MiniApp? = null
            var popular: Popular? = null
            var punchline:String?=""
            var tcashtype : String?=""

             if (s is Popular) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                miniApp = s.mini_app
                cashback=s.cashback!!.replace("Upto","").replace("Cashback","\nCashback").replace("cashback","\ncashback")
                popular = s
                 tcashtype = s.mini_app!!.tcash

            }


            val cashbackdata =cashback?.let { getSpannableCashbackText(it,itemView.resources.getColor(
                R.color.black)) }.toString()
            binding.cashback.text=cashbackdata

//            if (tcashtype.equals("1")){
//                binding.cashbackt.visibility = View.VISIBLE
//            }else{
//                binding.cashbackt.visibility = View.GONE
//            }


            if (image.toString().isNullOrEmpty()){
//                Toast.makeText(itemView.context,"Card Image is empty", Toast.LENGTH_SHORT).show()
            }else{
                Glide.with(itemView.context).load(image).circleCrop().placeholder(R.drawable.loding_app_icon)  .into(binding.icon)
            }

            binding.name.text ="save money on\n"+ name//+" with cashback"

            binding.root.setOnLongClickListener {
                clickListener.onRecyclerItemClick(1, popular, "")
               true
            }

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, popular, OPEN_WEB_VIEW)

            }
            //   binding.cashbackTv.text ="Get "+ cashback?.let { getSpannableCashbackText(it,itemView.resources.getColor(R.color.black)) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            MyhomepopularLayoutBinding.inflate(
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