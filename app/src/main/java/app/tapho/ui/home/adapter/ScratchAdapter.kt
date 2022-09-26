package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ScratchLayoutBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import com.bumptech.glide.Glide

class ScratchAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<scartchModel, ScratchAdapter.Holder>() {

    inner class Holder(val binding: ScratchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: scartchModel) {
            //binding.imageIv.setImageResource(data.image)
//            Glide.with(itemView.context).load(data.image).centerCrop().into(binding.card)
            binding.card.setScratchDrawable(data.image)
            binding.card.setScratchEnabled(true)
            binding.card.revealScratch()

//            binding.nameTv.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ScratchLayoutBinding.inflate(
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