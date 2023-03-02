package app.tapho.ui.activecashback.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowOrdersBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import java.util.*


class OrdersAdapter(val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<TCashDashboardData, OrdersAdapter.Holder>() {
    private var res: WebTCashRes? = null
    var lastdate = ""

    inner class Holder(val binding: RowOrdersBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(data: TCashDashboardData) {

            if (data.date.toString()==lastdate){
                binding.dateTv.visibility = View.GONE
            }

            val app_name = data.offer_name.toString()
            binding.statusTv1.setText(" from " + app_name.toString())

            binding.dateTv.text = parseDate3(data.date)
            binding.date2Tv.text = parseDate3(data.date)

            binding.percentage.text =  String.format("%.2f", (data.user_commision!!.toDouble() / data.sale_amount!!.toDouble() * 100.00 ).toDouble()) + "% Cashback Reward"

            binding.amountTv.text = withSuffixAmount(data.sale_amount)
            binding.cashbackAmountTv.text = withSuffixAmount(data.user_commision)

             when (data.status?.uppercase()) {
                "VERIFIED" -> {
                    binding.statusTv.text = "verified".replaceFirstChar { it.uppercase() }

                }
                "VALIDATED" -> {
                    binding.statusTv.text = "verified".replaceFirstChar { it.uppercase() }
                }
                "PENDING" -> {
                    binding.statusTv.text = "pending".replaceFirstChar { it.uppercase() }
                    binding.check.visibility = View.GONE
                }
                else -> {
                    binding.statusTv.text = "rejected".replaceFirstChar { it.uppercase() }
                    binding.check.visibility = View.GONE
                }
            }

            val color = when (data.status?.uppercase()) {
                "VERIFIED" -> {
                    ContextCompat.getColor(binding.statusTv.context, R.color.green_dark)

                }
                "VALIDATED" -> {
                    ContextCompat.getColor(binding.statusTv.context, R.color.green_dark)

                }
                "PENDING" -> {
                    ContextCompat.getColor(binding.statusTv.context, R.color.offer_coupon)
                //

                }
                else -> {
                    ContextCompat.getColor(binding.statusTv.context, R.color.red)
                 //   binding.check.visibility = View.GONE

                }
            }
            binding.statusTv.setTextColor(color)
         //   binding.statusTv.text = data.status?.replaceFirstChar { it.uppercase() }
//            val bgShape = binding.reAmount.background as GradientDrawable
//            bgShape.setStroke(3,color)
            binding.reAmount.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}