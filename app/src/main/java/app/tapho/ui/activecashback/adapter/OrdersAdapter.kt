package app.tapho.ui.activecashback.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.log


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
/*
            if (data.date.toString().length == 10) {
                Log.d("Date", data.date.toString())
                val date100 = data.date.toString().trim() + " 10/10/10"
                val date0 = date100
                val data1 = date0//.trim()
                val length1 = data1.length
                val data2 = data1.removeRange(10, 19)
                val data3 = data2;

                val data4 = data3.reversed()
                //start
                val data5 = data4.dropLast(8) //81
                val maindate = data5.reversed()//18
                //end
                //start 1
                val data6 = data1.removeRange(7, 18)
                val data8 = data6.removeRange(0, 4)

                val data7 = data8.reversed()
                val mainMonth = data7.removeRange(2, 4)
                //end 1
                //statrt2
                val year = data3.removeRange(4, 10)

                var month: String? = null
                if (mainMonth.equals("01")) {
                    month = "Jan"
                } else if (mainMonth.equals("02")) {
                    month = "Feb"
                } else if (mainMonth.equals("03")) {
                    month = "Mar"
                } else if (mainMonth.equals("04")) {
                    month = "Apr"
                } else if (mainMonth.equals("05")) {
                    month = "May"
                } else if (mainMonth.equals("06")) {
                    month = "Jun"
                } else if (mainMonth.equals("07")) {
                    month = "Jul"
                } else if (mainMonth.equals("08")) {
                    month = "Aug"
                } else if (mainMonth.equals("09")) {
                    month = "Sep"
                } else if (mainMonth.equals("10")) {
                    month = "Oct"
                } else if (mainMonth.equals("11")) {
                    month = "Nov"
                } else if (mainMonth.equals("12")) {
                    month = "Dec"
                }

                val dataMonth = month.toString()
                val actualDate = maindate + " " + dataMonth + " " + year
                val actualDate1 = maindate + " " + dataMonth

                binding.dateTv.text = actualDate
                binding.date2Tv.text = actualDate1
            }
            else if (data.date.toString().length == 19) {
                var date = data.date
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                try {
                    val time = sdf.parse(date).time
                    val now = System.currentTimeMillis()
                    val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                    binding.dateTv.text  = ago
                    binding.date2Tv.text  = ago
                    lastdate = ago.toString()
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            }
            else {
                binding.dateTv.text = data.date
                binding.date2Tv.text = data.date
            }
*/
            binding.dateTv.text = parseDate3(data.date)
            binding.date2Tv.text = parseDate3(data.date)

            binding.percentage.text =  String.format("%.2f", (data.user_commision!!.toDouble() / data.sale_amount!!.toDouble() * 100.00 ).toDouble()) + "% Cashback Reward"

            binding.amountTv.text = withSuffixAmount(data.sale_amount)
            binding.cashbackAmountTv.text = withSuffixAmount2(data.user_commision)

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