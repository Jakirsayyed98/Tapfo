package app.tapho.ui.localbizzUI.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.tapho.CamelCaseValue
import app.tapho.ContactOnWhatsapp
import app.tapho.DirectCall
import app.tapho.R
import app.tapho.databinding.BusinessPartnearLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Data
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class AllBusinessListAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AllBusinessListAdapter<S>.Holder>() {


    inner class Holder(private val binding: BusinessPartnearLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is Data) {
                Glide.with(itemView.context).load(s.image).centerCrop(). into(binding.cardIcon)
                var tag=""
                s.business_tag.forEach {
                    tag = tag+", "+ itemView.context.CamelCaseValue(it.name)
                }
                binding.tags.text = tag.drop(1)
                binding.distance.text = s.distance.removeRange(3,s.distance.length)+"km Away"
                binding.avrageRating.text = s.average_rating.toString()

                binding.openorclose.text = checkTime(s)
                val number = s.contacts.split(",")
                val callNumber = number.get(0).toString()
                binding.call.setOnClickListener {
                    itemView.context.DirectCall(callNumber)
                }
                val first_name = itemView.context.CamelCaseValue(s.business_name).replaceAfter(" ","")
                val last_name =  s.business_name.replaceBefore(" ","")
                val last_name2 =  last_name.replace(" ","")


                binding.nameTv.text = first_name +  itemView.context.CamelCaseValue(last_name2)
                binding.areaName.text = s.landmark
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,s,"")
                }
            }
        }
    }

    private fun checkTime(data: Data) :String {
        var timestatus =""
        val simpleDate = SimpleDateFormat("HH:MM")
        val currenttime = simpleDate.format(Date())
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        if (data.always_open.toString().equals("1")){
            timestatus = "Always Open"
        }else{
            when (day){
                Calendar.MONDAY ->{
                    if (data.mon_opening.equals("Closed").not()){
                        if (currenttime>= data.mon_opening  && currenttime<=data.mon_closing){
                            timestatus =  "Open Now"
                        }else{
                            timestatus = "Closed"

                        }
                    }else{
                        timestatus  = "Closed"
                    }

                }
                Calendar.TUESDAY  ->{
                    if (data.tue_opening.equals("Closed").not()){
                        if (currenttime>= data.tue_opening  && currenttime<=data.tue_closing){
                            timestatus =  "Open Now"
                        }else{
                            timestatus = "Closed"
                        }
                    }else{
                        timestatus  = "Closed"
                    }

                }
                Calendar.WEDNESDAY ->{
                    if (data.wed_opening.equals("Closed").not()){
                        if (currenttime>= data.wed_opening  && currenttime<=data.wed_closing){
                            timestatus =  "Open Now"
                        }else{
                            timestatus = "Closed"
                        }
                    }else{
                        timestatus  = "Closed"
                    }

                }
                Calendar.THURSDAY  ->{
                    if (data.thu_opening.equals("Closed").not()){
                        if (currenttime>= data.thu_opening  && currenttime<=data.thu_closing){
                            timestatus =  "Open Now"
                        }else{
                            timestatus  = "Closed"
                        }
                    }else{
                        timestatus  = "Closed"
                    }

                }
                Calendar.FRIDAY  ->{
                    if (data.fri_opening.equals("Closed").not()){
                        if (currenttime>= data.fri_opening  && currenttime<=data.fri_closing){
                            timestatus =  "Open Now"
                        }else{
                            timestatus = "Closed"
                        }
                    }else{
                        timestatus  = "Closed"
                    }

                }
                Calendar.SATURDAY ->{
                    if (data.sat_opening.equals("Closed").not()){
                        if (currenttime>= data.sat_opening  && currenttime<=data.sat_closing){
                            timestatus =  "Open Now"
                        }else{
                            timestatus = "Closed"
                        }
                    }else{
                        timestatus  = "Closed"
                    }

                }
                Calendar.SUNDAY  ->{
                    if (data.sun_opening.equals("Closed").not()){
                        if (currenttime>= data.sun_opening  && currenttime<=data.sun_closing){
                            timestatus =  "Open Now"
                        }else{
                            timestatus = "Closed"
                        }
                    }else{
                        timestatus  = "Closed"
                    }

                }
                else ->{
                    timestatus = "Closed"
                }
            }
        }

        return timestatus
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