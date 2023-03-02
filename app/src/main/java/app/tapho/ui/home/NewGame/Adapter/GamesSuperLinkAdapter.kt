package app.tapho.ui.home.NewGame.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.GamesSuperlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.NewGame.Model.GameCustomeSuperCategoryModel
import com.bumptech.glide.Glide

class GamesSuperLinkAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<GameCustomeSuperCategoryModel, GamesSuperLinkAdapter.Holder>() {

    inner class Holder(val binding: GamesSuperlinkLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: GameCustomeSuperCategoryModel) {
            if (data.image.toString().contains("gif")){
                Glide.with(itemView.context).asGif().load(data.image).into(binding.image)
            }else{
                Glide.with(itemView.context).load(data.image).into(binding.image)
            }
            if (data.tags.isNullOrEmpty().not()){
                binding.tags.visibility = View.VISIBLE
                binding.tags.text = data.tags
            }else
            {
                binding.tags.visibility = View.GONE
            }
            binding.name.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            GamesSuperlinkLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
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