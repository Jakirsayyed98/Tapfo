package app.tapho.ui.MiniCash.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.LayoutCategoriesLayoutBinding
import app.tapho.databinding.MerchantLayoutDataBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.CashbackMerchant
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide

class MerchantsDataAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CashbackMerchant, MerchantsDataAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CashbackMerchant) {
            if (binding is MerchantLayoutDataBinding) {

                if (data.mini_app == null){

                }else{
                    if (data.mini_app!!.logo.isNullOrEmpty().not()){
                        Glide.with(itemView.context).load(data.mini_app!!.logo).into(binding.logo)
                    }
                    binding.nametv.text = data.mini_app!!.name

                    binding.discription.text = data.mini_app!!.punchline
                    binding.Cashback.text = data.cashback!!.replace("Upto","").replace("Cashback","")
                    if (data.popular_cashback=="1"){
                        binding.toprated.visibility = View.VISIBLE
                    }else{
                        binding.toprated.visibility = View.GONE
                    }
                    binding.reward.text = data.report
                    binding.root.setOnClickListener {
                        clickListener.onRecyclerItemClick(0,data.mini_app, OPEN_WEB_VIEW)
                    }

                }


            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantsDataAdapter.Holder {

        return Holder(
            MerchantLayoutDataBinding.inflate(
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