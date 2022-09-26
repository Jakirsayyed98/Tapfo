package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.PopulargamesBinding
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.adapter.SearchGames
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class AllMiniAppSearchAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AllMiniAppSearchAdapter<S>.Holder>() {


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

    inner class Holder(private val binding: RecommendedItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is MiniApp) {
                val miniApp: MiniApp?
                val name: String? = s.name
                val image: String? = s.image
                miniApp = s
                val tcash: String? = s.tcash

                binding.rupee.visibility =  if (tcash == "1") View.VISIBLE else View.INVISIBLE
                Glide.with(itemView.context).load(image).circleCrop()
                    .into(binding.ivPartner)
                binding.nameTv.text = name
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RecommendedItemLayoutBinding.inflate(
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

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()

    }

}