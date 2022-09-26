package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.SendmoneyCategoryLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import com.bumptech.glide.Glide

class CustomeSendMoneyCategory (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<SendMoneyCustomeCategory, CustomeSendMoneyCategory.Holder>() {

    inner class Holder(val binding: SendmoneyCategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: SendMoneyCustomeCategory) {

            Glide.with(itemView.context).load(data.icon).circleCrop().into(binding.icon)

            binding.title.text = data.title
            binding.discription.text = data.discription

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.type,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(SendmoneyCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}