package app.tapho.ui.games.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.RecenPlayedGameLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.Data
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class GetRecentlyPlayedGameAdapterList (private val clickListener: RecyclerClickListener):
    BaseRecyclerAdapter<Data, GetRecentlyPlayedGameAdapterList.Holder>() {

    var totalplays=""

    inner class Holder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {
            if (binding is RecenPlayedGameLayoutBinding) {
                Glide.with(itemView.context).load(data.assets.square).centerCrop().placeholder(R.drawable.loding_app_icon).into(binding.ivPartner)
                binding.nameTv.text = data.name
                binding.plays.text = data.gamePlays
                val count:Double=data.gamePlays.toDouble()
                suffixFunction(count)
                binding.plays.text = totalplays
                binding.popular.setOnClickListener {
                    GameWebViewActivity.openWebView(itemView.context,data.url,data.id,data.name,data.assets.square,"")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetRecentlyPlayedGameAdapterList.Holder {

        return Holder(
            RecenPlayedGameLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
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