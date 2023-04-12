package app.tapho.ui.scanner.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.scanner.model.customePaymentMode

class TapfoPaymentModeAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoPaymentModeAdapter<S>.Holder>() {

    inner class Holder(private val binding: TapfomartPaymentMethodsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun setData(s: S) {
            if (s is customePaymentMode) {
                binding.nameTv.text = s.name
                var isChecked = s.checked
                binding.discription.text = s.discription
                binding.checked.isChecked = isChecked
                val data = s as customePaymentMode

                if (s.status.equals("1")) {
                    binding.view.visibility = View.VISIBLE
                } else {
                    binding.view.visibility = View.GONE
                }

                binding.checkbox.setOnClickListener {
                    if (s.status.equals("2")) {
                        unCheckeddAll(data)
                        isChecked = true
                        clickListener.onRecyclerItemClick(0, data, "")
                        notifyDataSetChanged()
                    }

                }
            }
        }

    }

    private fun unCheckeddAll(data: customePaymentMode) {
        list.forEach {
            if (it is customePaymentMode) {
                it.checked = false
            }
        }
        data.checked = true
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            TapfomartPaymentMethodsBinding.inflate(
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