package app.tapho.ui.help.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.SupportlistLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.help.model.CustomeServicelist
import com.bumptech.glide.Glide

class ServicelListAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeServicelist, ServicelListAdapter.Holder>() {

    inner class Holder(val binding: SupportlistLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeServicelist) {

            Glide.with(itemView.context).load(data.image).placeholder(R.drawable.loding_app_icon).into(binding.image)
            binding.title.text = data.title
            binding.discription.text = data.discription

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            SupportlistLayoutBinding.inflate(
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