package app.tapho.ui.localbizzUI.UserFlow.CustomeData

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.BusinessPartnearLayoutBinding
import app.tapho.databinding.BusinessnotificationLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide


class businessCustomeAdapter (private val clickListener: RecyclerClickListener):
    BaseRecyclerAdapter<CustomeModel, businessCustomeAdapter.Holder>() {
    inner class Holder(val binding: BusinessnotificationLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeModel) {


            Glide.with(itemView.context).load(data.image).into(binding.ivPartner)

            binding.nameTv.text = data.name
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,"","")
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(BusinessnotificationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}