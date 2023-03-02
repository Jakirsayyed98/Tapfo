package app.tapho.ui.games.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.GameSearchLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.model.MiniApp
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import kotlin.random.Random

class NewlyAddedGamesAdapter  <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, NewlyAddedGamesAdapter<S>.Holder>()  {
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

//    inner class Holder(private val binding: NewlyaddedBinding) :
    inner class Holder(private val binding: GameSearchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is Data) {

                val myRandomValues = List(10) { Random.nextInt(0, 10) }
                myRandomValues.forEach {
                    Glide.with(itemView.context)
                        .load(s.assets.square)
                        .centerCrop()
                        .into(binding.ivPartner)
                    binding.nameTv.text = s.name
                    if (s.rating.contains("0")){
                        binding.rating.text=" 2.3 "
                    }else{
                        binding.rating.text =" "+s.rating+" "
                    }
                    if (s.rating/*.toInt()*/<= 2.4.toString()){
                       val color= ContextCompat.getColor(itemView.context, R.color.red)
                        binding.rating.setBackgroundColor(color)
                   } else if (s.rating>=2.4.toString() && s.rating<=3.9.toString()){
                        val color= ContextCompat.getColor(itemView.context, R.color.orange)
                    binding.rating.setBackgroundColor(color)
                }else if (s.rating>=4.toString()){
                        val color= ContextCompat.getColor(itemView.context, R.color.screenGreen)
                    binding.rating.setBackgroundColor(color)
                }


                    val count:Double=s.gamePlays.toDouble()
                    suffixFunction(count)
                    binding.plays.text = totalplays
                    binding.root.setOnClickListener {
                        GameWebViewActivity.openWebView(itemView.context,s.url,s.id,s.name,s.assets.square,"")
                      //  clickListener.onRecyclerItemClick(0, s.url, OPEN_WEB_VIEW)
                    }

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
            GameSearchLayoutBinding.inflate(
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