package app.tapho.GetContactList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ContactItemLayoutBinding

class FeatchContactListAdapter ( val contactList: ArrayList<ContactModel>
) : RecyclerView.Adapter<FeatchContactListAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ContactItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameTV = binding.tvName
        val numberTV = binding.number
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ContactItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = contactList[position]
        holder.binding.tvName.text = item.displayName
        holder.binding.number.text = item.number
    }

    override fun getItemCount(): Int = contactList.size
}