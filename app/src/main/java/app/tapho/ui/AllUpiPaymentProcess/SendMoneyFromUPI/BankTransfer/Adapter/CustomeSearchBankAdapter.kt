package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.BankTransfer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.SerachBankLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CustomeSearchBankAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<SendMoneyCustomeCategory, CustomeSearchBankAdapter.Holder>() {

    inner class Holder(val binding: SerachBankLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: SendMoneyCustomeCategory) {


            binding.bankname.text = data.title
            if (data.icon.toString().isNullOrEmpty()) {
                binding.profileName.visibility = View.VISIBLE
                binding.profileIv.visibility = View.GONE
                binding.profileName.text =data.title

            } else {
                binding.profileName.visibility = View.GONE
                Glide.with(itemView.context).load(data.icon).apply(
                    RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
                ).into(binding.profileIv)
            }

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.title,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(SerachBankLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}