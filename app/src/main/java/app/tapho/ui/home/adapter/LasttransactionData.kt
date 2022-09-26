package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import app.tapho.databinding.InTheSpotlightBinding
import app.tapho.databinding.LastTransactionLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.HomeFragment
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import app.tapho.ui.tcash.TCashbackDetailActivity
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.DATA
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson

class LasttransactionData <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, LasttransactionData<S>.Holder>()  {

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

    inner class Holder(private val binding: LastTransactionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(s: S) {

            if (s is TCashDashboardData) {


                binding.cashbackAmt.text="Congrates! you received ₹"+ s.payout+"\n cashback on "+s.offer_name+" order."

                binding.refrenceIdTv.text="Reference ID: "+HomeFragment.UserId +s.trans_id

                Glide.with(itemView.context).load(s.image).listener(object :
                    RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                    ): Boolean {
                        Toast.makeText(itemView.context,"Failed", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                    ): Boolean {
                        Palette.from(resource!!.toBitmap()).generate { palette->palette.let {
                            val intColor=it?.vibrantSwatch?.rgb?:0
                            binding.card.setBackgroundColor(intColor) } }
                        return false
                    }
                }).circleCrop().into(binding.imageIcon)
                binding.root.setOnClickListener {
                    itemView.context.startActivity(Intent(itemView.context, TCashbackDetailActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(DATA, Gson().toJson(s))
                    })
                }

            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LastTransactionLayoutBinding.inflate(
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