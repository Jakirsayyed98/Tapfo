package app.tapho.ui.Stories.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.StoriesAdapterLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.Stories.Model.Data
import com.bumptech.glide.Glide

class StoriesAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, StoriesAdapter<S>.Holder>()  {

    inner class Holder(private val binding: StoriesAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is Data) {
                Glide.with(itemView.context).load(s.image).circleCrop().into(binding.icon)
                binding.name.text = s.name

                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,s.id,"")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(StoriesAdapterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}