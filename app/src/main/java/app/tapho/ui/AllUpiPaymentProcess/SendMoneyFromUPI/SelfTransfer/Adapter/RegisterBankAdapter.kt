package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.SelfTransfer.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.SerachBankLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.SendMoneyCustomeCategory
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.SelfTransfer.SelfBankTransferFragment
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RegisterBankAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<SendMoneyCustomeCategory, RegisterBankAdapter.Holder>() {

    inner class Holder(val binding: SerachBankLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: SendMoneyCustomeCategory) {

            var BankID = SelfBankTransferFragment.SelecttedBankAccount.toString()
            if (BankID.equals(data.id)){
                binding.layout.visibility = View.GONE
                binding.layout2.visibility = View.GONE
            }
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
                clickListener.onRecyclerItemClick(0,data.id,"")
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