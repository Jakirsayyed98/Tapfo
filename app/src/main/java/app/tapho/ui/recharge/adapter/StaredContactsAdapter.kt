package app.tapho.ui.recharge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.ContactItemLayoutBinding
import app.tapho.databinding.ContactStaredItemLayoutBinding
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.recharge.model.ContactsModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class StaredContactsAdapter : BaseRecyclerAdapter<ContactsModel, StaredContactsAdapter.Holder>() {

    inner class Holder(val binding: ContactStaredItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: ContactsModel) {
            binding.tvName.text = data.name
            binding.tvPhone.text = data.phone
            Glide.with(binding.profileIv).load(data.image)
                .apply(RequestOptions().placeholder(R.drawable.logo_circle).circleCrop())
                .into(binding.profileIv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ContactStaredItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
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