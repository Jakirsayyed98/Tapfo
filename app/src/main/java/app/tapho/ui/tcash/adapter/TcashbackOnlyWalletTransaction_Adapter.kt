package app.tapho.ui.tcash.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowAddmoneyWalletTransactionBinding
import app.tapho.databinding.RowTcashbackBinding
import app.tapho.databinding.RowWalletTransactionBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.*
import com.bumptech.glide.Glide

class TcashbackOnlyWalletTransaction_Adapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Txn, TcashbackOnlyWalletTransaction_Adapter.Holder>() {
    inner class Holder(val walletBindig: RowAddmoneyWalletTransactionBinding) :
        RecyclerView.ViewHolder(walletBindig.root) {

        fun setData(data: Txn) {

            when(data.payment_plus_minus){
                "1"->{
                    Glide.with(itemView.context).load("").into(walletBindig.icon)
                    walletBindig.payoption.text = data.pay_option
                    walletBindig.credited.text = "credited"
                    if (data.type.toString().equals("4")){
                        walletBindig.Amount.text ="+"+withSuffixAmount(data.cashback)
                    }else{
                        walletBindig.Amount.text ="+"+withSuffixAmount(data.amount)
                    }

                    walletBindig.Amount.setTextColor(itemView.context.resources.getColor(R.color.new_green2))
                    walletBindig.dateTv.text = parseDate3(data.created_at)
                }
                "2"-> {
                    Glide.with(itemView.context).load("").into(walletBindig.icon)
                    walletBindig.payoption.text = data.pay_option
                    if (data.type.toString().equals("4")){
                        walletBindig.Amount.text ="+"+withSuffixAmount(data.cashback)
                    }else{
                        walletBindig.Amount.text ="+"+withSuffixAmount(data.amount)
                    }
                    walletBindig.Amount.setTextColor(itemView.context.resources.getColor(R.color.red))
                    walletBindig.dateTv.text = parseDate3(data.created_at)
                    walletBindig.credited.text = "Debited"
                }
            }




                walletBindig.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "detail")
                }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder( RowAddmoneyWalletTransactionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    //    holder.(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}