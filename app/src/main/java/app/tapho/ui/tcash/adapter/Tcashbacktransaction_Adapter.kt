package app.tapho.ui.tcash.adapter

import android.graphics.Color
import android.graphics.Color.parseColor
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowWalletTransactionBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.bumptech.glide.Glide

class Tcashbacktransaction_Adapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<TCashDashboardData, Tcashbacktransaction_Adapter.Holder>() {
    inner class Holder(val walletBindig: RowWalletTransactionBinding) :
        RecyclerView.ViewHolder(walletBindig.root) {

        fun setData(data: TCashDashboardData) {
            if (data.status?.uppercase() == "PENDING") {

                walletBindig.brandNameTv.text ="Cashback from "+ data.offer_name//+" Order"
                walletBindig.cashback.text ="+ "+ withSuffixAmount(data.user_commision)
                walletBindig.cashback.setTextColor(Color.parseColor("#909090"))
                Glide.with(itemView.context).load(data.image).circleCrop().into(walletBindig.icon)

                walletBindig.dateTv.text =  parseDate3(data.date)





                walletBindig.walletCard.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "pending")
                }
            }
            else if (data.status?.uppercase() == "VERIFIED" || data.status?.uppercase() == "VALIDATED") {

                walletBindig.brandNameTv.text ="Cashback from "+ data.offer_name//+" Order"
                walletBindig.cashback.text ="+ "+ withSuffixAmount(data.user_commision)

                Glide.with(itemView.context).load(data.image).circleCrop().into(walletBindig.icon)
                walletBindig.cashback.setTextColor(Color.parseColor("#248104"))
                walletBindig.dateTv.text =  parseDate3(data.date)



                walletBindig.walletCard.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "verified")
                }
            }
            else{
                walletBindig.walletCard.visibility=View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder( RowWalletTransactionBinding.inflate(
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