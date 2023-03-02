package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.RowFeaturedPartnerBinding
import app.tapho.databinding.RowMerchantsBigBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.MiniApp
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily


class CashbackMerchantAdapter(private val showBig: Boolean,private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CashbackMerchant, CashbackMerchantAdapter.Holder>() {

    inner class Holder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(cashbackMerchant: CashbackMerchant) {
            if (binding is RowFeaturedPartnerBinding) {
                Glide.with(itemView.context).load(cashbackMerchant.mini_app?.image)
                    .into(binding.ivPartner)

                binding.nameTv.text = cashbackMerchant.mini_app?.name


                binding.cashbackTv.text = cashbackMerchant.cashback?.let {
                        getSpannableCashbackText(it, itemView.resources.getColor(
                            R.color.black))
                    }

                val data = cashbackMerchant.cashback?.let {
                        getSpannableCashbackText(it, itemView.resources.getColor(
                            R.color.black))
                    }

                binding.root.setOnClickListener {

                    onClick(cashbackMerchant.mini_app)

                }
            } else if (binding is RowMerchantsBigBinding) {
//                Glide.with(itemView.context).load(cashbackMerchant.mini_app?.image).circleCrop()
//                    .into(binding.ivPartner)

                val radius = binding.root.context.resources.getDimension(R.dimen.dp10)
                binding.ivPartner.shapeAppearanceModel = binding.ivPartner.shapeAppearanceModel
                    .toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, radius)
                    .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                    .build()
                Glide.with(itemView.context).load(cashbackMerchant.image)
                    .into(binding.ivPartner)

                binding.nameTv.text = cashbackMerchant.mini_app?.name
                binding.cashbackTv.text =
                    cashbackMerchant.cashback?.let { getSpannableCashbackText(it, itemView.resources.getColor(R.color.black)) }
                binding.earnNow.setOnClickListener {
                    onClick(cashbackMerchant.mini_app)
                }
            }
        }
    }

    private fun onClick(miniApp: MiniApp?){
        clickListener.onRecyclerItemClick(0,miniApp,"")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        if (showBig) {
            return Holder(RowMerchantsBigBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        } else {
            return Holder(RowFeaturedPartnerBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}