package app.tapho.ui.ScanAndPay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ScanandpayFeaturesLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.ScanAndPay.model.CustomeScanAndPayFeatureModel
import com.bumptech.glide.Glide

class ScanAndPay_Features(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeScanAndPayFeatureModel, ScanAndPay_Features.Holder>() {

    inner class Holder(val binding: ScanandpayFeaturesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeScanAndPayFeatureModel) {

            Glide.with(itemView.context).load(data.image).circleCrop().into(binding.image)

            binding.name.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ScanandpayFeaturesLayoutBinding.inflate(
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