package app.tapho.ui.home.ReferAndEarn

import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowReferprofileLayoutBinding
import app.tapho.databinding.RowTcashbackBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class referandEarnProfileAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Txn, referandEarnProfileAdapter.Holder>() {

    inner class Holder(val binding: RowReferprofileLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: Txn) {
            if (data.referral_user_detail.isNullOrEmpty().not()){
                if (data.referral_user_detail.get(0).image.isNullOrEmpty()) {
                    kotlin.runCatching {
                        binding.profileName.visibility = View.VISIBLE
                        binding.profileIv.visibility = View.GONE

                        binding.profileName.text = data.referral_user_detail.get(0).name.replaceAfter(" ", "")
                    }

                } else {
                    binding.profileName.visibility = View.GONE
                    Glide.with(itemView.context).load(data.referral_user_detail.get(0).image).apply(RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon)).into(binding.profileIv)
                }

                binding.name.text = data.referral_user_detail.get(0).name
                binding.Amount.text = "+"+ withSuffixAmount(data.cashback)
                binding.dateTv.text ="Joined "+ parseDate4StartMonth(data.created_at)

            }else{
                binding.root.visibility = View.GONE
            }


            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "detail")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RowReferprofileLayoutBinding.inflate(
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