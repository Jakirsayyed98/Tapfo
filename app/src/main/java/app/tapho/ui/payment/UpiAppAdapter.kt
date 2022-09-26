package app.tapho.ui.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowUpiAppBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.one97.paytm.nativesdk.instruments.upicollect.models.UpiOptionsModel

class UpiAppAdapter : BaseRecyclerAdapter<UpiOptionsModel, UpiAppAdapter.Holder>() {
    private var selectedPos = -1

    inner class Holder(val binding: RowUpiAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: UpiOptionsModel, position: Int) {
            Glide.with(binding.image).load(data.drawable).apply(RequestOptions().circleCrop())
                .into(binding.image)
            binding.checkIv.visibility = if (position == selectedPos) View.VISIBLE else View.GONE

            binding.image.setOnClickListener {
                selectedPos = position
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowUpiAppBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}