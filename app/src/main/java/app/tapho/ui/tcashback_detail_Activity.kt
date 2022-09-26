package app.tapho.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.ActivityTcashbackDetail2Binding
import app.tapho.databinding.ActivityTcashbackDetailBinding
import app.tapho.interfaces.SpanClickListener
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

class tcashback_detail_Activity  : BaseActivity<ActivityTcashbackDetail2Binding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTcashbackDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        setData()
    }


    private fun setData() {


        Gson().fromJson(intent.getStringExtra(DATA), TCashDashboardData::class.java)?.let { data ->
            var intColor = R.color.green_dark

            when (data.status?.uppercase()) {
                "VERIFIED" -> {
                    binding.verifedConformation.setImageResource(R.drawable.verifiedok)
                    binding.status.setTextColor(Color.parseColor("#008D3A"))
                    binding.statusTextTv.setTextColor(Color.parseColor("#008D3A"))
                    binding.validation.visibility=View.GONE
                }
                "VALIDATED" -> {
                    binding.verifedConformation.setImageResource(R.drawable.verifiedok)
                    binding.status.setTextColor(Color.parseColor("#008D3A"))
                    binding.statusTextTv.setTextColor(Color.parseColor("#008D3A"))
                    binding.validation.visibility=View.GONE
                }
                "PENDING" -> {
                    binding.status.setTextColor(Color.parseColor("#E67E22"))
                    binding.statusTextTv.setTextColor(Color.parseColor("#E67E22"))
                    binding.validationPeriodTv.text =getValidationPeriod(data)
                }
                "FAILED" -> {
                    binding.verifedConformation.setImageResource(R.drawable.rejectedok)
                    binding.status.setTextColor(Color.parseColor("#EF5350"))
                    binding.statusTextTv.setTextColor(Color.parseColor("#EF5350"))
                    binding.validationPeriodTv.text ="Rejected"
                    binding.valid.text ="Validation"
                }
                "REJECTED" -> {
                    binding.verifedConformation.setImageResource(R.drawable.rejectedok)
                    binding.status.setTextColor(Color.parseColor("#EF5350"))
                    binding.statusTextTv.setTextColor(Color.parseColor("#EF5350"))
                    binding.validationPeriodTv.text ="Rejected"
                    binding.valid.text ="Validation"
                }
                else -> {
                }
            }

            binding.back.setOnClickListener {
                onBackPressed()
            }

            val cashbackAmt = data.user_commision.toString()
            val transactionAmt = data.sale_amount.toString()
            val percentage = cashbackAmt.toDouble() / transactionAmt.toDouble() * 100
            val finalValue = String.format("%.2f", percentage).toDouble()

            if (data.sale_amount.equals("0")){
                binding.orderAmountTv.visibility = View.GONE
                binding.aftercashback.text = withSuffixAmount(data.user_commision)

                binding.cashbackPercentage.text = withSuffixAmount(data.user_commision) + " CASHBACK"
                binding.cashbackPercentage2.text =withSuffixAmount(data.user_commision) + "CASHBACK"

            }else{
                if (data.sale_amount.toString().isNullOrEmpty().not() || data.user_commision.toString().isNullOrEmpty().not()){
                    val total=data.sale_amount.toString().toDouble() - data.user_commision.toString().toDouble()
                    binding.aftercashback.text = withSuffixAmount(total.toString())
                }
                binding.cashbackPercentage.text = finalValue.toString() + "% CASHBACK"
                binding.cashbackPercentage2.text = finalValue.toString() + "% CASHBACK"
            }

            binding.redirectline.text=getString(R.string.cashback_recieved_from,data.offer_name)
            binding.redirectline1.text=getString(R.string.cashbackhold2,data.offer_name)
            binding.statusTextTv.text=getString(R.string.cashback_pending_from_,data.status,data.offer_name)


            binding.merchantNameTv.text = data.offer_name
            Glide.with(binding.merchantIv).load(data.image).into(binding.merchantIv)

            binding.transactionId.text ="ID: "+ data.trans_id
            if(data.date.toString().length==10){
                binding.dateTv.text = parseDate4(data.date)
            }else{
                binding.dateTv.text = parseDate5(data.date)
            }

            binding.orderAmountTv.text = withSuffixAmount(data.sale_amount)
            binding.cashbackAmountTv2.text =getString(R.string._74_cashback,withSuffixAmount(data.user_commision))
            binding.cashbackAmountTv.text = withSuffixAmount(data.user_commision) +" cashback "+data.status+"\nfrom "+data.offer_name

            binding.status.text= getString(R.string.your_cashback_is_pending_from_swiggy,data.status,data.offer_name)


        }
    }
    private fun getValidationPeriod(data: TCashDashboardData): String {
        try {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(data.date)?.let {
                return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(
                    Calendar.getInstance()
                        .apply {
                            time = it
                            add(Calendar.DATE, 60)
                        }.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}