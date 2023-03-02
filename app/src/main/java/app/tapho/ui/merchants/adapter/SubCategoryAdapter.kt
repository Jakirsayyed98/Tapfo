package app.tapho.ui.merchants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowSubCategoriesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.ROUND_CORNER_RADIUS
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.net.URLDecoder

class SubCategoryAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<MiniApp, SubCategoryAdapter.Holder>() {

    inner class Holder(val binding: RowSubCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: MiniApp) {
            Glide.with(binding.merchantImage).load(data.image)
                .apply(
                    RequestOptions().transform(
                        RoundedCorners(ROUND_CORNER_RADIUS)
                    ).placeholder(R.drawable.loding_app_icon)
                )
                .into(binding.merchantImage)
            binding.nameTv.text = data.name
            binding.headlineTv.text = data.punchline
            binding.fevIv.isSelected = data.is_favourite == "1"
            binding.offerTv.text = URLDecoder.decode(data.cashback, "UTF-8")
            binding.rupee.visibility = if (data.tcash == "1") View.VISIBLE else View.INVISIBLE

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, OPEN_WEB_VIEW)
            }
            binding.fevIv.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "favourite")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowSubCategoriesBinding.inflate(LayoutInflater.from(parent.context),
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