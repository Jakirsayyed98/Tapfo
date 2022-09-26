package app.tapho.ui.localbizzUI.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.tapho.ContactOnWhatsapp
import app.tapho.DirectCall
import app.tapho.R
import app.tapho.databinding.BusinessPartnearLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Data
import com.bumptech.glide.Glide
import java.util.ArrayList


class AllBusinessListAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AllBusinessListAdapter<S>.Holder>() {


    inner class Holder(private val binding: BusinessPartnearLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is Data) {
                Glide.with(itemView.context).load(s.image).centerCrop(). into(binding.cardIcon)
                var tag=""
                s.business_tag.forEach {
                    tag = tag+", "+it.name
                }

                binding.tags.text = tag.drop(1)

                binding.distance.text = s.distance.removeRange(3,s.distance.length)+" km Away"

                binding.avrageRating.text = s.average_rating.toString()

                binding.chat.setOnClickListener {
                    itemView.context.ContactOnWhatsapp(s.whatsapp_business)

                }
                val number = s.contacts.split(",")
                val callNumber = number.get(0).toString()
                binding.call.setOnClickListener {
                    itemView.context.DirectCall(callNumber)

                }

                binding.ratingCount.text =s.rating_list.size.toString()+" ratings"
                binding.nameTv.text = s.business_name
                binding.areaName.text = s.landmark
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,s,"")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            BusinessPartnearLayoutBinding.inflate(
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

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()
    }
}