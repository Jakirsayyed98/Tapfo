package app.tapho.ui.games.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.databinding.PopulargamesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.Data

import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat

class GetRecentlyPlayedGameAdapterList (private val clickListener: RecyclerClickListener):
    BaseRecyclerAdapter<Data, GetRecentlyPlayedGameAdapterList.Holder>() {

    var totalplays=""

    inner class Holder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {
            if (binding is PopulargamesBinding) {

                        Glide.with(itemView.context).load(data.assets.square).circleCrop().centerCrop()
                            .placeholder(R.drawable.logo_circle)
                            .into(binding.ivPartner)
                binding.nameTv.text = data.name
                binding.plays.text = data.gamePlays

                val count:Double=data.gamePlays.toDouble()
                suffixFunction(count)
                binding.plays.text = totalplays
                binding.popular.setOnClickListener {
                    GameWebViewActivity.openWebView(itemView.context,data.url,data.id,data.name,data.assets.square,"")

                   // clickListener.onRecyclerItemClick(0, data, "AppCategory")
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
        Log.d("numbersnew","$totalplays")
        return totalplays;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetRecentlyPlayedGameAdapterList.Holder {

        return Holder(
            PopulargamesBinding.inflate(
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