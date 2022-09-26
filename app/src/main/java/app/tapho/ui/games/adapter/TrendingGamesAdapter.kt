package app.tapho.ui.games.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowGamesCategoryBinding
import app.tapho.databinding.RowTrendingGamesBinding
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide

class TrendingGamesAdapter : BaseRecyclerAdapter<String, TrendingGamesAdapter.Holder>() {

    inner class Holder(private val binding: RowTrendingGamesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData() {
            Glide.with(binding.image).load(R.drawable.ic_splash_bg).circleCrop().into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowTrendingGamesBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int {
        return 4
    }

}