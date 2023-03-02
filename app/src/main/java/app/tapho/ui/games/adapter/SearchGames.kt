package app.tapho.ui.games.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.GameSearchLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Data
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class SearchGames <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, SearchGames<S>.Holder>()  {
    var totalplays=""


    inner class Holder(private val binding: GameSearchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is Data) {
                Glide.with(itemView.context).load(s.assets.square).centerCrop().into(binding.ivPartner)
                binding.nameTv.text = s.name
                //binding.plays.text=s.gamePlays

                val count:Double=s.gamePlays.toDouble()
                suffixFunction(count)
                binding.plays.text=totalplays.toString()

                binding.root.setOnClickListener {
                    GameWebViewActivity.openWebView(itemView.context,s.url,s.id,s.name,s.assets.square,"")
                }

            }
        }
    }

    private fun suffixFunction(count: Double) :String {
        val df = DecimalFormat("#.# ")
        totalplays = if (Math.abs(count / 1000000) >= 1) {
            df.format(count / 1000000.0).toString() + "M Plays"
        } else if (Math.abs(count / 1000.0) >= 1) {
            df.format(count / 1000.0).toString() + "K Plays"
        } else {
            count.toString()
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

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1:ArrayList<S>){
        list=list1
        notifyDataSetChanged()

    }

}