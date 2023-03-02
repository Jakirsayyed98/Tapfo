package app.tapho.ui.games.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.FavGamesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.Data

import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class GamesFavListAdapter (private val clickListener: RecyclerClickListener):
    BaseRecyclerAdapter<Data, GamesFavListAdapter.Holder>() {
    inner class Holder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {
            if (binding is FavGamesLayoutBinding) {


                when (data.name) {
                    itemView.context.getString(R.string.more) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_more_blue).circleCrop()
                            .into(binding.image)
                    }
                    itemView.context.getString(R.string.less) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_less_blue).circleCrop()
                            .into(binding.image)
                    }
                    else -> {
                        Glide.with(itemView.context).load(data.assets.square).circleCrop().centerCrop()
                            .apply(
                                RequestOptions().transform(
                                    CenterCrop(), RoundedCorners(
                                        ROUND_CORNER_RADIUS
                                    )
                                ).placeholder(R.drawable.loding_app_icon))
                            .into(binding.image)
                    }
                }
                binding.nameTv.text = data.name
                binding.image.setOnClickListener {
                     GameWebViewActivity.openWebView(itemView.context,data.url,data.id,data.name,data.assets.square,"")
                    /* clickListener.onRecyclerItemClick(0, data, "AppCategory") */
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesFavListAdapter.Holder {

        return Holder(
            FavGamesLayoutBinding.inflate(
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