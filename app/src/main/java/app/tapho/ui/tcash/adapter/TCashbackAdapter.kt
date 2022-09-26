package app.tapho.ui.tcash.adapter

import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowTcashbackBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.bumptech.glide.Glide

class TCashbackAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<TCashDashboardData, TCashbackAdapter.Holder>() {

    inner class Holder(val binding: RowTcashbackBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: TCashDashboardData) {
            if (data.sale_amount.equals("0")){
                binding.salesAmountTv.visibility = View.GONE
//                binding.layout1.visibility = View.GONE
            }
            Glide.with(binding.image).load(data.image).circleCrop().into(binding.image)
            binding.brandNameTv.text = data.offer_name
            val merchant_name = data.offer_name
            binding.refrenceIdTv.text = getSpannableBold("Txn Id: " + data.trans_id + "...", 7, -1, null, 0f)
            binding.salesAmountTv.text = withSuffixAmount(data.sale_amount)

            binding.balenceTv.text = withSuffixAmount(data.user_commision)

                binding.dateTv.text =onlyDatewithMonth(data.date)


            //End Condition Date Formateer
            val cashbackAmt = data.user_commision.toString()
            val transactionAmt = data.sale_amount.toString()
            val percentage = cashbackAmt.toDouble() / transactionAmt.toDouble() * 100
            val finalValue = String.format("%.2f", percentage).toDouble()


            if (finalValue.toString().equals("Infinity")){
                binding.cashbackPercent.text = withSuffixAmount(data.user_commision) + " Cashback"
            }else{
                binding.cashbackPercent.text = finalValue.toString() + "% Cashback"
            }

            val status = data.status?.replaceFirstChar { it.uppercase() }
            binding.statusTv.text = status + " from " + merchant_name
            if (status.toString()=="Validated"){
                binding.statusTv.text = "Verified" + " from " + merchant_name
            }else{
                binding.statusTv.text = status + " from " + merchant_name
            }

            binding.statusTv.setTextColor(
                when (data.status?.uppercase()) {
                    "VERIFIED" -> ContextCompat.getColor(
                        binding.statusTv.context,
                        R.color.green_dark
                    )
                    "VALIDATED" -> ContextCompat.getColor(
                        binding.statusTv.context,
                        R.color.green_dark
                    )
                    "PENDING" -> ContextCompat.getColor(
                        binding.statusTv.context,
                        R.color.offer_coupon
                    )
                    else -> ContextCompat.getColor(binding.statusTv.context, R.color.red)
                }
            )

            binding.transactionCard.setCardBackgroundColor(
                when (data.status?.uppercase()) {
                    "VERIFIED" -> ContextCompat.getColor(
                        binding.transactionCard.context,
                        R.color.transactionVerified
                    )
                    "VALIDATED" -> ContextCompat.getColor(
                        binding.transactionCard.context,
                        R.color.transactionVerified
                    )
                    "PENDING" -> ContextCompat.getColor(
                        binding.transactionCard.context,
                        R.color.transactionPending
                    )
                    else -> ContextCompat.getColor(binding.transactionCard.context, R.color.transactionRejected)
                }
            )
            //  binding.dateTv.text = parseDate(data.date)
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "detail")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RowTcashbackBinding.inflate(
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