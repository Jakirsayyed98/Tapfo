package app.tapho.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowProfileBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.ProfileOptionsModel
import com.bumptech.glide.Glide

class ProfileOptionsAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<ProfileOptionsModel, ProfileOptionsAdapter.Holder>() {

    inner class Holder(val binding: RowProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ProfileOptionsModel) {
         //   binding.imageIv.setImageResource(data.image)

            Glide.with(itemView.context).load(data.image).into(binding.imageIv)
            binding.nameTv.text = data.name
            binding.discription.text = data.discription

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.type,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}