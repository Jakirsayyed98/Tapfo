package app.tapho.ui.RechargeService.ModelData.DeviceContact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ContactRvLayoutBinding
import app.tapho.databinding.RechargeServiceLayoutBinding
import app.tapho.databinding.SpuerlinkLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.RechargeService.ModelData.CustomeRechargeService
import app.tapho.ui.RechargeService.ModelData.RechargeServices.Data
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.*
import com.bumptech.glide.Glide

class DeviceContactModelAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Txn, DeviceContactModelAdapter.Holder>() {
    inner class Holder(private val binding: ContactRvLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Txn) {
            if (data.recharge_detail.isNullOrEmpty().not()) {
                val rechargedetail = data.recharge_detail.get(0)
                rechargedetail.let {
                    Glide.with(itemView.context).load(it.operator_detail.image).circleCrop().into(binding.icon)

//                    binding.name.text ="+91"+ it.number
                    binding.number.text = it.number
                    binding.cashback.text ="Recharged "+  withSuffixAmount(it.amount)
                    binding.dateTv.text =" on "+parseDate4StartMonth(it.created_at)
                    binding.root.setOnClickListener {
                        clickListener.onRecyclerItemClick(0,data,"Recharge")
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ContactRvLayoutBinding.inflate(
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