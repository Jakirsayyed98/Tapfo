package app.tapho.ui.tcash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.DetailLayoutBinding
import app.tapho.databinding.QuickRechargeLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.tcash.model.custome_quickrechargemodel
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide

class quickrecharegAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<custome_quickrechargemodel, quickrecharegAdapter.Holder>() {

    inner class Holder(val binding: QuickRechargeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: custome_quickrechargemodel) {
            if (data.name.equals("Custom")){
                binding.amount.text = data.name
            }else{
                binding.amount.text = withSuffixAmount(data.name)!!.dropLast(3)
            }

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.name, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            QuickRechargeLayoutBinding.inflate(
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