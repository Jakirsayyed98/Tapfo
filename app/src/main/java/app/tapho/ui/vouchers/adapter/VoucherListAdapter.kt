package app.tapho.ui.vouchers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowSubCategoriesBinding
import app.tapho.databinding.RowVouchersBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.vouchers.model.VoucherListData
import app.tapho.utils.OPEN_WEB_VIEW

class VoucherListAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<VoucherListData, VoucherListAdapter.Holder>() {

    inner class Holder(val binding: RowVouchersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: VoucherListData?) {
            /* Glide.with(binding.merchantImage).load(data.image)
                 .apply(
                     RequestOptions().transform(
                         RoundedCorners(ROUND_CORNER_RADIUS)
                     )
                 )
                 .into(binding.merchantImage)*/
//            binding.nameTv.text = data.name
//            binding.typeTv.text = data.punchline
//            binding.fevIv.isSelected = data.is_favourite == "1"
//            binding.offerTv.text = URLDecoder.decode(data.cashback, "UTF-8")
//            if (data.cashback.isNullOrEmpty())
//                binding.offerLi.visibility = View.INVISIBLE
//            else
//                binding.offerLi.visibility = View.VISIBLE

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
            }
            binding.fevIv.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "favourite")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowVouchersBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(null)
    }

    override fun getItemCount(): Int {
        return 10
    }
}