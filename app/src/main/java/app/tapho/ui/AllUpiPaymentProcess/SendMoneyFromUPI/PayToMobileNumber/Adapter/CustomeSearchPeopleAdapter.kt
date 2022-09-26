package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.SearchPeopleUpiBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CustomeSearchPeopleAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<SearchPeopleCustomeDataModel, CustomeSearchPeopleAdapter.Holder>() {

    inner class Holder(val binding: SearchPeopleUpiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: SearchPeopleCustomeDataModel) {

       //     Glide.with(itemView.context).load(data.icon).circleCrop().into(binding.icon)

            if (data.icon.isNullOrEmpty()) {
                binding.profileName.visibility = View.VISIBLE
                binding.profileIv.visibility = View.GONE
                binding.profileName.text =data.Name

            } else {
                binding.profileName.visibility = View.GONE
                Glide.with(itemView.context).load(data.icon).apply(
                    RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
                ).into(binding.profileIv)
            }

            binding.number.text = data.Number
            binding.name.text = data.Name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(SearchPeopleUpiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}