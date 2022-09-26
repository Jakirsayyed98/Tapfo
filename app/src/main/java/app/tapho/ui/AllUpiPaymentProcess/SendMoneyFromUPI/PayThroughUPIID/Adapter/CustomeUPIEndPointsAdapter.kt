package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.UpiEndpointsLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayThroughUPIID.Model.CustomeUPIEndpoints
import app.tapho.ui.BaseRecyclerAdapter

class CustomeUPIEndPointsAdapter  (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeUPIEndpoints, CustomeUPIEndPointsAdapter.Holder>() {

    inner class Holder(val binding: UpiEndpointsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: CustomeUPIEndpoints) {

            binding.endpoints.text = data.name

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.type,"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(UpiEndpointsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}