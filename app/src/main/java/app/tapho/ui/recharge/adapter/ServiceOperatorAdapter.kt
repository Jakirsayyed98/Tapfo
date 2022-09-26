package app.tapho.ui.recharge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowServiceOperatorBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.recharge.model.ServiceOperatorData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ServiceOperatorAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<ServiceOperatorData, ServiceOperatorAdapter.Holder>() {

    inner class Holder(val binding: RowServiceOperatorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: ServiceOperatorData) {
            binding.nameTv.text=data.name
            binding.offerTv.text=""
            Glide.with(binding.imageIv).load(data.image)
                .placeholder(R.drawable.logo_circle)
                .circleCrop()
                .into(binding.imageIv)

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowServiceOperatorBinding.inflate(LayoutInflater.from(parent.context),
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