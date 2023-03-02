package app.tapho.ui.tcash.adapter

import android.graphics.Color
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowTcashbackBinding
import app.tapho.databinding.RowWalletTransactionBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.bumptech.glide.Glide

class TCashbackAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<TCashDashboardData, TCashbackAdapter.Holder>() {

    inner class Holder(val binding: RowWalletTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: TCashDashboardData) {

            binding.brandNameTv.text ="Cashback from "+ data.offer_name+" Order"
            binding.cashback.text ="+ "+ withSuffixAmount(data.user_commision)
            Glide.with(itemView.context).load(data.image).circleCrop().into(binding.icon)
            binding.dateTv.text =  parseDate3(data.date)+" "+ if (data.date.toString().length>18) parsetime3(data.date) else ""

            if (data.status?.uppercase()=="PENDING"){
                binding.cashback.setTextColor(Color.parseColor("#FF7C2B"))
            } else if (data.status?.uppercase() == "VERIFIED" || data.status?.uppercase() == "VALIDATED") {
                binding.cashback.setTextColor(Color.parseColor("#248104"))
            } else{
                binding.cashback.setTextColor(Color.parseColor("#EF5350"))
            }


            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "detail")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RowWalletTransactionBinding.inflate(
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


}