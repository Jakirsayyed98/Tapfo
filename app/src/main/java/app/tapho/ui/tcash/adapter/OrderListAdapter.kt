package app.tapho.ui.tcash.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.OrderListLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.parseDate
import app.tapho.utils.withSuffixAmount
import java.util.*
import kotlin.collections.ArrayList

class OrderListAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, OrderListAdapter<S>.Holder>() {

    inner class Holder(private val binding: OrderListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun setData(s: S) {

            if (s is Txn) {
                binding.apply {
                    s.business_order_detail.let { s ->
                        orderId.text = s.code
                        Date.text = parseDate(s.created_at)
                        if (s.staff_detail.isNullOrEmpty().not()) {
                            mobileNumber.text = s.staff_detail.get(0).name
                        }

                        name.text = "Id :- " + s.business_staff_id

                        totalAmount.text = withSuffixAmount(s.total_amount)
                        var qtyd = 0
                        s.items.forEach {
                            qtyd += it.qty.toInt()
                        }
                        qty.text = qtyd.toString()

                        when (s.status) {
                            "0" -> {
                                status.text = "Pending"
                                status.setTextColor(Color.parseColor("#FF7C2B"))
                            }
                            "1" -> {
                                status.text = "Paid"
                                status.setTextColor(Color.parseColor("#008D3A"))
                            }
                            else -> {
                                status.text = "Rejected"
                                status.setTextColor(Color.RED)
                            }
                        }
                        binding.root.setOnClickListener {
                            clickListener.onRecyclerItemClick(0, s, "")
                        }
                    }

                }

            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            OrderListLayoutBinding.inflate(
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

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()
    }

}