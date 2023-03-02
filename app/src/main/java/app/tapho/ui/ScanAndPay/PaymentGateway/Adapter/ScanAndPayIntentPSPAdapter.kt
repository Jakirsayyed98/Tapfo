package app.tapho.ui.ScanAndPay.PaymentGateway.Adapter

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ScanandpayIntentpspLayoutBinding
import app.tapho.databinding.SmartintentpspLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.PaytmPaymentGateway.Adapter.PSPModelClass
import app.tapho.ui.ScanAndPay.PaymentGateway.Model.ScanAndPayPSPModelClass
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchantCategory
import com.bumptech.glide.Glide
import com.google.android.gms.common.wrappers.Wrappers.packageManager


class ScanAndPayIntentPSPAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<ScanAndPayPSPModelClass, ScanAndPayIntentPSPAdapter.Holder>() {

    inner class Holder(private val binding: ScanandpayIntentpspLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: ScanAndPayPSPModelClass) {
            var isSelected = s.isSelected
            val pm = itemView.context.getPackageManager()
            val info = pm.getApplicationInfo(s.packageName, PackageManager.GET_META_DATA);
            Glide.with(itemView.context).load(pm.getApplicationIcon(s.packageName)).circleCrop().into(binding.appIcon)
            binding.name.text = pm.getApplicationLabel(info)
//            binding.card.isSelected = isSelected
            if (isSelected){
                binding.checkbutton.isChecked = true
                print("MyChecdata checkdata selected "+pm.getApplicationLabel(info))
            }else{
                binding.checkbutton.isChecked = true
                print("MyChecdata checkdata selected false "+pm.getApplicationLabel(info))
            }
            binding.dis.text = "Pay via "+pm.getApplicationLabel(info)
            binding.root.setOnClickListener {
                    unSelectAll(s)
                    isSelected = true
                    clickListener.onRecyclerItemClick(0, s.packageName, pm.getApplicationLabel(info).toString())
                    notifyDataSetChanged()
                    //   ActiveCashbackForWebActivity.openWebView(it.context,url,miniAppId,image,tCashType,isFavourite)
                }

        }
    }

    private fun unSelectAll(data: ScanAndPayPSPModelClass) {
        list.forEach {
            if (it is ScanAndPayPSPModelClass)
                it.isSelected = false
        }
        if (data is ScanAndPayPSPModelClass)
            data.isSelected = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ScanandpayIntentpspLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}