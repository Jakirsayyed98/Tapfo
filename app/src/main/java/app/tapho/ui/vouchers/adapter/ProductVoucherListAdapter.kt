package app.tapho.ui.vouchers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowProductVoucherBinding
import app.tapho.databinding.RowSubCategoriesBinding
import app.tapho.databinding.RowVouchersBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.vouchers.model.VoucherListData
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class ProductVoucherListAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<VoucherListData, ProductVoucherListAdapter.Holder>() {

    inner class Holder(val binding: RowProductVoucherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: VoucherListData) {
             Glide.with(binding.image).load(data.banner)
                 .apply(
                     RequestOptions().transform(
                         RoundedCorners(ROUND_CORNER_RADIUS)
                     )
                 )
                 .into(binding.image)
            binding.nameTv.text = data.name+" eVoucher"

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "detail")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowProductVoucherBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}