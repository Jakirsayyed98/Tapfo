package app.tapho.ui.tcash.AddMoneyPopup.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.AddmoneysuggetionadapterLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.AddMoneyPopup.Model.CustomeAddMoneyModel
import app.tapho.utils.withSuffixAmount


class CustomeAddMoneySuggetionAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<CustomeAddMoneyModel, CustomeAddMoneySuggetionAdapter.Holder>() {

    inner class Holder(val binding: AddmoneysuggetionadapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun setData(data: CustomeAddMoneyModel) {
            var isSelected = false

                isSelected = data.isSelected
                binding.amount.text = withSuffixAmount(data.Amount.toString())!!.dropLast(3)


            binding.root.isSelected = isSelected

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "tabClick")
                unSelectAll(data)
                isSelected = true

                notifyDataSetChanged()


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AddmoneysuggetionadapterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private fun unSelectAll(data: CustomeAddMoneyModel) {
        list.forEach {
            if (true)
                it.isSelected = false

        }
        if (true){
            data.isSelected = true

        }


    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}