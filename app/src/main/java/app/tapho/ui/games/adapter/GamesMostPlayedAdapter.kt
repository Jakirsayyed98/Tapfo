package app.tapho.ui.games.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowGamesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily

class GamesMostPlayedAdapter(private val clickListener: RecyclerClickListener) : BaseRecyclerAdapter<String, GamesMostPlayedAdapter.Holder>() {

    inner class Holder(private val binding: RowGamesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData() {
            val radius = binding.root.context.resources.getDimension(R.dimen.dp10)
            binding.image.shapeAppearanceModel = binding.image.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
            Glide.with(itemView.context).load(R.drawable.ic_splash_bg)
                .into(binding.image)

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,"","play")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowGamesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int {
        return 2
    }

}