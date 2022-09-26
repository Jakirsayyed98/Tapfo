package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowRatesBinding
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.TCashCategory
import app.tapho.utils.roundOff

class RatesAdapter : BaseRecyclerAdapter<TCashCategory, RatesAdapter.Holder>() {

    inner class Holder(val binding: RowRatesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setDate(data: TCashCategory) {
            if (data.id.isNullOrEmpty()) {
                binding.termsTv.text = "Cashback Rates"
                if (list.size > 1)
                    binding.cashbackRatesTv.text = getUser(list[1])
            } else {
                binding.termsTv.text = data.category
                binding.cashbackRatesTv.text =
                    if (data.payout_type == "1") getCashbackRatesPercentage(data) else
                        getCashbackRatesRupees(data)
            }
        }

        private fun getCashbackRatesPercentage(category: TCashCategory?): StringBuilder {
            return StringBuilder()
                .append(roundOff(category?.getAllUser()) ?: "")
                .append(if (category?.getAllUser().isNullOrEmpty()) "" else "%")
                .append(roundOff(category?.getNewUser()) ?: "")
                .append(if (category?.getNewUser().isNullOrEmpty()) "" else "%")
                .append(getDivider(category))
                .append(roundOff(category?.getOldUser()) ?: "")
                .append(if (category?.getOldUser().isNullOrEmpty()) "" else "%")
        }

        private fun getCashbackRatesRupees(category: TCashCategory?): StringBuilder {
            return StringBuilder()
                .append(if (category?.getAllUser()
                        .isNullOrEmpty()
                ) "" else itemView.context.getString(
                    R.string.rupee))
                .append(roundOff(category?.getAllUser()) ?: "")
                .append(if (category?.getNewUser()
                        .isNullOrEmpty()
                ) "" else itemView.context.getString(
                    R.string.rupee))
                .append(roundOff(category?.getNewUser()) ?: "")
                .append(getDivider(category))
                .append(if (category?.getOldUser()
                        .isNullOrEmpty()
                ) "" else itemView.context.getString(
                    R.string.rupee))
                .append(roundOff(category?.getOldUser()) ?: "")
        }

        private fun getDivider(category: TCashCategory?): StringBuilder {
            return StringBuilder(if (category?.getNewUser()
                    .isNullOrEmpty() || category?.getOldUser()
                    .isNullOrEmpty()
            )
                "" else "/")
        }

        private fun getUser(category: TCashCategory?): String {
            return StringBuilder().append(if (category?.getAllUser()
                    .isNullOrEmpty()
            ) "" else "(All User)")
                .append(if (category?.getNewUser().isNullOrEmpty()) "" else "New")
                .append(getDivider(category))
                .append(if (category?.getOldUser().isNullOrEmpty()) "" else "Old")
                .toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setDate(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}