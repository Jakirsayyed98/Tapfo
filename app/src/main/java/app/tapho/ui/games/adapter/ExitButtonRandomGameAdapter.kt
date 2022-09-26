package app.tapho.ui.games.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide

class ExitButtonRandomGameAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ExitButtonRandomGameAdapter<S>.Holder>()  {


    inner class Holder(private val binding: RecommendedItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is Data) {
                Glide.with(itemView.context).load(s.assets.square).centerCrop().into(binding.ivPartner)
                binding.nameTv.text = s.name
                binding.rupee.visibility=View.GONE
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s.url, OPEN_WEB_VIEW)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RecommendedItemLayoutBinding.inflate(
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


}