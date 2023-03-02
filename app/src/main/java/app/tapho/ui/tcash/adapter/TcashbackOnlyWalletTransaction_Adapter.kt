package app.tapho.ui.tcash.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowAddmoneyWalletTransactionBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.VoucherPayments.VoucherPaymentBaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONObject

class TcashbackOnlyWalletTransaction_Adapter(private val clickListener: RecyclerClickListener) :
        BaseRecyclerAdapter<Txn, TcashbackOnlyWalletTransaction_Adapter.Holder>() {
    inner class Holder(val walletBindig: RowAddmoneyWalletTransactionBinding) :
        RecyclerView.ViewHolder(walletBindig.root) {

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun setData(data: Txn) {

            when(data.type){
                "1" ->{
                    Glide.with(itemView.context).load(R.drawable.wallet_wallet_icon).into(walletBindig.icon)
                    walletBindig.type.text = "Top-Up"
                    walletBindig.payoption.text ="Tapfo Pay wallet"
                    walletBindig.dateTv.text = parseDate3(data.created_at)
                    walletBindig.credited.visibility = View.GONE
//                    if (data.payment_plus_minus.equals("1")){
//                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
//                    } else {
                        walletBindig.Amount.setTextColor(Color.parseColor("#000000"))
//                    }
                    if (data.toString().contains("cashback")){
                        if (data.cashback.isNullOrEmpty().not()){
                            val Amountdata = data.cashback.toDouble()+ data.amount.toDouble()
                            walletBindig.Amount.text ="+"+ withSuffixAmount(Amountdata.toString())!!.dropLast(3)
                        }else{
                            walletBindig.Amount.text ="+"+ withSuffixAmount(data.cashback)
                        }
                    }
                    walletBindig.root.setOnClickListener {
                        ContainerActivity.openContainerforPaymentStatus(itemView.context,"AddMoneyTransactionStatus","0",data.txn_id,"","wallet",data.pay_option,"",data)
                    }

                }

                "2" ->{
                    Glide.with(itemView.context).load(R.drawable.wallet_wallet_icon).into(walletBindig.icon)
                    walletBindig.credited.visibility = View.GONE
                    walletBindig.type.text = "Bonus"
                    walletBindig.payoption.text = "Reward for Sign Up"
                    walletBindig.dateTv.text = parseDate3(data.created_at)
//                    if (data.payment_plus_minus.equals("1")){
//                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
//                    } else {
                        walletBindig.Amount.setTextColor(Color.parseColor("#000000"))
//                    }
                    walletBindig.Amount.text =if (data.payment_plus_minus.equals("2"))  withSuffixAmount(data.cashback) else "+"+ withSuffixAmount(data.cashback)
                }

                "3" ->{
                    walletBindig.type.text = "Mobile Recharge"

                    data.let {

                        if (it.pay_option.isNullOrEmpty().not()){
                            walletBindig.credited.text ="Debited-"
                            when(it.pay_option){
                                "Tapfo"->{
                                    walletBindig.pspAppsicon.visibility = View.GONE

                                }
                                "GPay"->{
                                    walletBindig.pspAppsicon.visibility = View.VISIBLE
                                    Glide.with(itemView.context).load(R.drawable.gpay).centerCrop().into(walletBindig.pspAppsicon)
                                }
                                "Paytm"->{
                                    walletBindig.pspAppsicon.visibility = View.VISIBLE
                                    Glide.with(itemView.context).load(R.drawable.paytm_icon).centerCrop().into(walletBindig.pspAppsicon)
                                }
                                "PhonePe"->{
                                    walletBindig.pspAppsicon.visibility = View.VISIBLE
                                    Glide.with(itemView.context).load(R.drawable.phonepe_icon).centerCrop().into(walletBindig.pspAppsicon)
                                }
                                else->{
                                    walletBindig.pspAppsicon.visibility = View.GONE
                                }
                            }
                        }else{
                            walletBindig.pspAppsicon.visibility = View.GONE
                        }

                        if (it.amount.isNullOrEmpty().not()){
                            if (it.amount.toDouble()<=0){
                                walletBindig.wallet.visibility = View.GONE
                            } else{
                                walletBindig.wallet.visibility = View.VISIBLE
                            }
                        }else{
                            walletBindig.wallet.visibility = View.GONE
                        }

                        if (it.payment_amount.isNullOrEmpty().not()){
                            if (it.amount.toDouble()<=0){
                                walletBindig.pspAppsicon.visibility = View.GONE
                            } else{
                                walletBindig.pspAppsicon.visibility = View.VISIBLE
                            }
                        }else{
                            walletBindig.pspAppsicon.visibility = View.GONE
                        }
                    }
                    if (data.recharge_detail.isEmpty().not()){
                        if (data.recharge_detail.get(0).status.equals("1")){
                            walletBindig.credited.visibility = View.GONE

                        } else{
                            walletBindig.credited.text ="failed"
                            walletBindig.failedIcon.visibility = View.VISIBLE
                            walletBindig.pspAppsicon.visibility = View.GONE
                            walletBindig.credited.setTextColor(Color.parseColor("#EF5350"))
                        }
                        Glide.with(itemView.context).load(data.recharge_detail.get(0).operator_detail.image).into(walletBindig.icon)
                        walletBindig.payoption.text = data.recharge_detail.get(0).number




                        walletBindig.Amount.text =if (data.payment_plus_minus.equals("2"))  withSuffixAmount( data.recharge_detail.get(0).amount) else "+"+ withSuffixAmount( data.recharge_detail.get(0).amount)
                    }else{
                        walletBindig.root.visibility = View.VISIBLE
                    }
                        walletBindig.Amount.setTextColor(Color.parseColor("#000000"))
                        walletBindig.dateTv.text = parseDate3(data.created_at)
                    if (data.recharge_detail.isNullOrEmpty().not()){
                        val status = data.recharge_detail.get(0).status
                        var result = ""
                        if (status.equals("1")) result="2" else result="1"
                        walletBindig.root.setOnClickListener {
                            ContainerActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatus","",data.id,status,"Recharge",data.pay_option,result,data)
                        }
                    }

                }

                "4" ->{
                    Glide.with(itemView.context).load(R.drawable.wallet_wallet_icon).into(walletBindig.icon)
                    walletBindig.credited.visibility = View.GONE
                    walletBindig.type.text = "Referral Bonus"
                    if (data.referral_user_detail.isEmpty().not()){
//                        ContextCompat.getColor(walletBindig.Amount.context,R.color.green_dark)
                        walletBindig.payoption.text =data.referral_user_detail.get(0).name+" on Tapfo"
                    }
//                    if (data.payment_plus_minus.equals("1")){
//                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
//                    } else {
                        walletBindig.Amount.setTextColor(Color.parseColor("#000000"))
//                    }
                    walletBindig.dateTv.text = parseDate3(data.created_at)
                    walletBindig.Amount.text =if (data.payment_plus_minus.equals("2"))  withSuffixAmount(data.cashback) else "+"+ withSuffixAmount(data.cashback)
                }

                "5" ->{
                    Glide.with(itemView.context).load(R.drawable.vouchers_icon).into(walletBindig.icon)
//                    walletBindig.credited.text = if (data.payment_plus_minus.equals("2")) "Debited" else "Credited"
                    walletBindig.credited.visibility = View.GONE
                    walletBindig.type.text = "Gift Voucher"
//                    if (data.payment_plus_minus.equals("1")){
//                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
//                    } else {
                        walletBindig.Amount.setTextColor(Color.parseColor("#000000"))
//                    }
//                    if (data.toString().contains("VoucherDetail(")){
//                        if(data.voucher_detail.toString().isEmpty().not()){
//                            if (data.voucher_detail.total_price.isEmpty().not()){
//                                if (data.voucher_detail.response_json.vPullVouchersResult.PullVouchers.isEmpty().not()){
//                                    walletBindig.payoption.text ="Purchased for "+ data.voucher_detail.response_json.vPullVouchersResult.PullVouchers.get(0).ProductName
//                                }
//
//                                walletBindig.Amount.text = if (data.payment_plus_minus.equals("2")) withSuffixAmount( data.voucher_detail.total_price) else  "+"+ withSuffixAmount( data.voucher_detail.total_price)
//                            }
//                        }
//                    }
                    walletBindig.dateTv.text = parseDate3(data.created_at)
                    walletBindig.root.setOnClickListener {
                        VoucherPaymentBaseActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatusForVoucher","0",data.id.toString(),"","Recharge",data.pay_option,)
                    }
                }

                "6" ->{



                    walletBindig.credited.text = if (data.payment_plus_minus.equals("2")) "Debited" else "Credited"
                    walletBindig.type.text = "Mobile Recharge Refund"
                    if (data.recharge_detail.isEmpty().not()){
                        walletBindig.payoption.text = data.recharge_detail.get(0).number
                        walletBindig.Amount.text =if (data.payment_plus_minus.equals("2"))  withSuffixAmount( data.recharge_detail.get(0).amount) else "+"+ withSuffixAmount( data.recharge_detail.get(0).amount)
                    }else{
                        walletBindig.root.visibility = View.VISIBLE
                    }
                    Glide.with(itemView.context).load(data.recharge_detail.get(0).operator_detail.image).into(walletBindig.icon)
//                    if (data.payment_plus_minus.equals("1")){
//                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
//                    } else {
                        walletBindig.Amount.setTextColor(Color.parseColor("#000000"))
//                    }
                        walletBindig.dateTv.text = parseDate3(data.created_at)
                    walletBindig.root.setOnClickListener {
                   //       ContainerActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatus","0",data.txn_id,"","wallet",data.pay_option,"")
                    }
                }

                "7" ->{

                    walletBindig.credited.text = if (data.payment_plus_minus.equals("2")) "Debited" else "Credited"
                    walletBindig.type.text = "Mobile Recharge Cashback"
                    if (data.recharge_detail.isEmpty().not()){
                        walletBindig.payoption.text = data.recharge_detail.get(0).number
                        walletBindig.Amount.text =if (data.payment_plus_minus.equals("2"))  withSuffixAmount( data.cashback) else "+"+ withSuffixAmount( data.cashback)
                    }else{
                        walletBindig.root.visibility = View.VISIBLE
                    }
                    Glide.with(itemView.context).load(data.recharge_detail.get(0).operator_detail.image).into(walletBindig.icon)
//                    if (data.payment_plus_minus.equals("1")){
//                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
//                    } else {
                        walletBindig.Amount.setTextColor(Color.parseColor("#000000"))
//                    }
                        walletBindig.dateTv.text = parseDate3(data.created_at)
                    walletBindig.root.setOnClickListener {
                   //       ContainerActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatus","0",data.txn_id,"","wallet",data.pay_option,"")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RowAddmoneyWalletTransactionBinding.inflate( LayoutInflater.from(parent.context), parent, false)
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