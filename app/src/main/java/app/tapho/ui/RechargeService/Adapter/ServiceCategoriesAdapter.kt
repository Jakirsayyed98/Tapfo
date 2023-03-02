package app.tapho.ui.RechargeService.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RechargeServiceLayoutBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.RechargeService.ModelData.CustomeRechargeService
import app.tapho.ui.RechargeService.ModelData.RechargeServices.Data
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import com.bumptech.glide.Glide

class ServiceCategoriesAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ServiceCategoriesAdapter<S>.Holder>() {
    inner class Holder(private val binding: RechargeServiceLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            if (data is CustomeRechargeService) {
                Glide.with(itemView.context).load(data.image).into(binding.image)
                binding.name.text = data.name
                if (data.tag.isNullOrEmpty().not()){
                    binding.tags.text = data.tag
                    binding.tags.visibility = View.VISIBLE
                }else
                {
                    binding.tags.visibility = View.GONE
                }
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, data.api_name)
                }
            }else if (data is Data) {
                Glide.with(itemView.context).load(data.image).into(binding.image)
                binding.name.text = data.name

                binding.tags.visibility = View.GONE
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, data.api_name)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RechargeServiceLayoutBinding.inflate(
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