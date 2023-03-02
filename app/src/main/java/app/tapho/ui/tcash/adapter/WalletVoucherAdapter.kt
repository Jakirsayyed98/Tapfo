package app.tapho.ui.tcash.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.WalletVoucherLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.tcash.model.AddMoneyVoucers.Data
import app.tapho.utils.withSuffixAmount
import java.util.*


class WalletVoucherAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, WalletVoucherAdapter<S>.Holder>() {

    inner class Holder(val binding: WalletVoucherLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: S) {
            if (data is Data ){
                binding.Amount.text = withSuffixAmount(data.amount)!!.dropLast(3)
                binding.cashbackAmount.text = itemView.context.getString(R.string.FLat_data,
                    withSuffixAmount(data.cashback)!!.dropLast(3))

                binding.AddMoneyToWallet.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "detail")
                }

            }


        }
    }

    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            WalletVoucherLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
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