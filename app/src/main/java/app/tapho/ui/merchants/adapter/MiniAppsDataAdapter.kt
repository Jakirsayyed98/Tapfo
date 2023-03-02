package app.tapho.ui.merchants.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.databinding.MerchantLayoutDataBinding
import app.tapho.databinding.SelectedMiniappLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.decodeCashback
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class MiniAppsDataAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<MiniApp, MiniAppsDataAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: MiniApp) {
            if (binding is SelectedMiniappLayoutBinding) {

                if (data.logo.isNullOrEmpty().not()){
                    Glide.with(itemView.context).load(data.image).circleCrop().into(binding.image)
                }
                if (data.tcash!!.toString()=="1"){
                    val cashback = decodeCashback(data.merchant_payout!!.cashback)
                    binding.cb.text = cashback!!.replace("Upto","").replace("upto","").replace("Cashback","CB").replace("cashback","CB")
                }

                binding.nameTv.text = data.name
                if (data.tcash.equals("1")){
                    binding.cb.visibility = View.VISIBLE
                }else{
                    binding.cb.visibility = View.GONE
                }
//                if (data.is_favourite.equals("1")){
//                    binding.favIcon.visibility = View.VISIBLE
//                }else{
//                    binding.favIcon.visibility = View.GONE
//                }
//
//                if (data.total_favourite_count.equals("0")){
//                    binding.favCountLayout.visibility = View.GONE
//                }else{
//                    binding.favCountLayout.visibility = View.VISIBLE
//                    binding.favCount.text =data.total_favourite_count+" people liked"
//                }

                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,data, OPEN_WEB_VIEW)
                }


            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniAppsDataAdapter.Holder {

        return Holder(
            SelectedMiniappLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
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