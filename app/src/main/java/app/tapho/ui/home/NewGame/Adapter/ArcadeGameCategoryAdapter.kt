package app.tapho.ui.home.NewGame.Adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.MostplayedGameLayoutBinding
import app.tapho.databinding.NewgamesLayoutBinding
import app.tapho.databinding.PopulargamesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.model.MiniApp
import com.bumptech.glide.Glide
import java.text.DecimalFormat


class ArcadeGameCategoryAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ArcadeGameCategoryAdapter<S>.Holder>()  {
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

//    inner class Holder(private val binding: RowGamesBinding) :
    inner class Holder(private val binding: NewgamesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is Data) {

                Glide.with(itemView.context).load(s.assets.square).circleCrop().into(binding.icon)
                Glide.with(itemView.context).load(s.assets.square).centerCrop().into(binding.ivPartner)

                binding.nameTv.text = s.name


                val count:Double=s.gamePlays.toDouble()
                suffixFunction(count)
                binding.plays.text=totalplays.toString()


                binding.root.setOnClickListener {
                    GameWebViewActivity.openWebView(itemView.context,s.url,s.id,s.name,s.assets.square,"")
             //       clickListener.onRecyclerItemClick(0, s.url, OPEN_WEB_VIEW)
                }
            }
        }
    }

    private fun suffixFunction(count: Double) :String {
        val df = DecimalFormat("#.#")
        totalplays = if (Math.abs(count / 1000000) >= 1) {
            df.format(count / 1000000.0).toString() + "M Plays"
        } else if (Math.abs(count / 1000.0) >= 1) {
            df.format(count / 1000.0).toString() + "K Plays"
        } else {
            count.toString()+" Plays"
        }
        return totalplays;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            NewgamesLayoutBinding.inflate(
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