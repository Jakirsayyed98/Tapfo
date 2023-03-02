package app.tapho.ui.localbizzUI.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.tapho.CamelCaseValue
import app.tapho.R
import app.tapho.databinding.BusinessPartnearLayoutBinding
import app.tapho.databinding.BusinessServiceLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.BusinessTag
import java.util.ArrayList


class business_services_adapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, business_services_adapter<S>.Holder>() {


    inner class Holder(private val binding: BusinessServiceLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is BusinessTag) {

                binding.nameTv.text =itemView.context.CamelCaseValue(s.name)
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,s,"")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(BusinessServiceLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()
    }
}