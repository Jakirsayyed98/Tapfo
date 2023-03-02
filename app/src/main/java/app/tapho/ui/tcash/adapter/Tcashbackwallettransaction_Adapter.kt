package app.tapho.ui.tcash.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowWalletTransactionBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.BuyVoucher.VoucherPayments.VoucherPaymentBaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.getOpretordata
import app.tapho.utils.parseDate3
import app.tapho.utils.parsetime3
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide

class Tcashbackwallettransaction_Adapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Txn, Tcashbackwallettransaction_Adapter.Holder>() {
    inner class Holder(val walletBindig: RowWalletTransactionBinding) :
        RecyclerView.ViewHolder(walletBindig.root) {

        fun setData(data: Txn) {
            Log.d("RechargeLog",data.toString())
            when (data.status){
                "0"->{

                    if (data.recharge_detail.isEmpty().not()){

                        data.recharge_detail.get(0).let {

//                            walletBindig.brandNameTv.text ="Cashback from "+it.operator.name+" Recharge"
//                            Glide.with(itemView.context).load(it.operator.image).circleCrop().into(walletBindig.icon)
                            walletBindig.cashback.text ="+ "+ withSuffixAmount(it.user_commission_amount)
                            walletBindig.cashback.setTextColor(Color.parseColor("#909090"))
                        }
                        if (data.recharge_detail.isNullOrEmpty().not()){
                            val status = data.recharge_detail.get(0).status
                            var result = ""
                            if (status.equals("1")) result="2" else result="1"
                            walletBindig.walletCard.setOnClickListener {
                                ContainerActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatus","",data.id,status,"Recharge",data.pay_option,result,data)
                            }
                        }

                        walletBindig.dateTv.text =  parseDate3(data.created_at) +" "+ if (data.created_at.toString().length>18){
                            parsetime3(data.created_at)
                        } else {
                            ""
                        }
                    }

                }
                "1"->{
                    when (data.type){
                        "7"->{
                            if (data.recharge_detail.isEmpty().not()) {
                                data.recharge_detail.get(0).let {
                                    walletBindig.brandNameTv.text ="Cashback from "+it.operator_detail.name+" Recharge"
                                    Glide.with(itemView.context).load(it.operator_detail.image).circleCrop().into(walletBindig.icon)
                                }

                                walletBindig.cashback.text = "+ " + withSuffixAmount(data.recharge_detail.get(0).user_commission_amount)
                                walletBindig.cashback.setTextColor(Color.parseColor("#248104"))

                                if (data.recharge_detail.isNullOrEmpty().not()){
//                        val objectd = JSONObject(data.recharge_detail.get(0).api_response)
//                        val status = objectd.getString("Status")
                                    walletBindig.type.text = "Prepaid Recharge"
                                    val status = data.recharge_detail.get(0).status
                                    var result = ""
                                    if (status.equals("1")) result="2" else result="1"

                                    walletBindig.walletCard.setOnClickListener {

                                        ContainerActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatus","",data.id,status,"Recharge",data.pay_option,result,data)
                                    }
                                }

                                walletBindig.dateTv.text = parseDate3(data.created_at) + " " + if (data.created_at.toString().length > 18) { parsetime3(data.created_at) } else {
                                    ""
                                }




                            }
                        }
                        "4"->{

                            Glide.with(itemView.context).load(R.drawable.wallet_wallet_icon).into(walletBindig.icon)
                            walletBindig.type.text = "Referral Bonus"
                            if (data.referral_user_detail.isEmpty().not()){
//                              ContextCompat.getColor(walletBindig.Amount.context,R.color.green_dark)
                                walletBindig.brandNameTv.text =data.referral_user_detail.get(0).name+" on Tapfo"
                            }
        //                    if (data.payment_plus_minus.equals("1")){
        //                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
        //                    } else {
                                //    walletBindig.cashback.setTextColor(Color.parseColor("#000000"))
//                           }
                            walletBindig.dateTv.text = parseDate3(data.created_at)
                            walletBindig.root.visibility = if (data.cashback.toDouble()<=0.0) View.GONE else View.VISIBLE
                            walletBindig.cashback.text =if (data.payment_plus_minus.equals("2"))  withSuffixAmount(data.cashback) else "+"+ withSuffixAmount(data.cashback)
                            walletBindig.verified.setOnClickListener {

                            }
                        }

                        "5"->{
                            Glide.with(itemView.context).load(R.drawable.vouchers_icon).into(walletBindig.icon)
//                    walletBindig.credited.text = if (data.payment_plus_minus.equals("2")) "Debited" else "Credited"

                            walletBindig.type.text = "Gift Voucher"
//                    if (data.payment_plus_minus.equals("1")){
//                        walletBindig.Amount.setTextColor(Color.parseColor("#008D3A"))
//                    } else {
//                            walletBindig.cashback.setTextColor(Color.parseColor("#000000"))
//                    }
                            if (data.cashback.isNullOrEmpty().not()){
                                walletBindig.cashback.text = withSuffixAmount(data.cashback)
                            }
//                            if (data.toString().contains("VoucherDetail(")){
//                                if(data.voucher_detail.toString().isEmpty().not()){
//                                    if (data.voucher_detail.total_price.isEmpty().not()){
//                                        if (data.voucher_detail.response_json.vPullVouchersResult.PullVouchers.isEmpty().not()){
//                                            walletBindig.brandNameTv.text ="Purchased for "+ data.voucher_detail.response_json.vPullVouchersResult.PullVouchers.get(0).ProductName
//                                        }
//                                    }
//                                }
//                            }
                            walletBindig.dateTv.text = parseDate3(data.created_at)
                            walletBindig.verified.setOnClickListener {
                                VoucherPaymentBaseActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatusForVoucher","0",data.id.toString(),"","Recharge",data.pay_option)
                            }
                        }
                    }

                }

            }
            when (data.type){
                "0"->{
                    if (data.recharge_detail.isEmpty().not()){
                        data.recharge_detail.get(0).let {
                            walletBindig.brandNameTv.text ="Cashback from "+it.operator_detail.name+" Recharge"
                            Glide.with(itemView.context).load(it.operator_detail.image).circleCrop().into(walletBindig.icon)
                            walletBindig.cashback.text ="+ "+ withSuffixAmount(it.user_commission_amount)
                            walletBindig.cashback.setTextColor(Color.parseColor("#909090"))
                        }
                        if (data.recharge_detail.isNullOrEmpty().not()){
                            val status = data.recharge_detail.get(0).status
                            var result = ""
                            if (status.equals("1")) result="2" else result="1"

                            walletBindig.walletCard.setOnClickListener {
                                  ContainerActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatus","",data.id,status,"Recharge",data.pay_option,result,data)
                            }
                        }

                        walletBindig.dateTv.text =  parseDate3(data.created_at) +" "+ if (data.created_at.toString().length>18){
                            parsetime3(data.created_at)
                        } else {
                            ""
                        }

                    }

                }
                "1"->{
                    if (data.recharge_detail.isEmpty().not()) {

                        data.recharge_detail.get(0).let {
                            walletBindig.brandNameTv.text ="Cashback from "+it.operator_detail.name+" Recharge"
                            Glide.with(itemView.context).load(it.operator_detail.image).circleCrop().into(walletBindig.icon)
                            walletBindig.cashback.text ="+ "+ withSuffixAmount(it.user_commission_amount)
                            walletBindig.cashback.setTextColor(Color.parseColor("#248104"))
                        }


                        if (data.recharge_detail.isNullOrEmpty().not()){
//                        val objectd = JSONObject(data.recharge_detail.get(0).api_response)
//                        val status = objectd.getString("Status")
                            val status = data.recharge_detail.get(0).status
                            var result = ""
                            if (status.equals("1")) result="2" else result="1"
                            walletBindig.walletCard.setOnClickListener {
                                ContainerActivity.openContainerforPaymentStatus(itemView.context,"TransactionStatus","",data.id,status,"Recharge",data.pay_option,result,data)
                            }
                        }
                        walletBindig.dateTv.text = parseDate3(data.created_at) + " " + if (data.created_at.toString().length > 18) { parsetime3(data.created_at) } else {
                                ""
                            }

                    }
                }
                "2"->{

                }
            }


            walletBindig.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "detail")
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