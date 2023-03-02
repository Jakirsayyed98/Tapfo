package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RedeemStepLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.merchants.model.CustomeModel
import com.bumptech.glide.Glide

class CustomeHowCBRedeemBloksAdapter  (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeModel, CustomeHowCBRedeemBloksAdapter.Holder>() {

    inner class Holder(val binding: RedeemStepLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeModel) {

            Glide.with(itemView.context).load(data.icon).placeholder(R.drawable.loding_app_icon).into(binding.image)
            binding.root.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RedeemStepLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}