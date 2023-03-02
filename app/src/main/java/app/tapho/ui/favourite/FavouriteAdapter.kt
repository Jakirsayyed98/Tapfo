package app.tapho.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowCategoriesDataBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class FavouriteAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<MiniApp, FavouriteAdapter.Holder>() {

    inner class Holder(private val binding: RowCategoriesDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: MiniApp) {
            Glide.with(binding.image).load(data.image)/*.transform(CenterCrop(), RoundedCorners(ROUND_CORNER_RADIUS))*/.circleCrop().placeholder(R.drawable.loding_app_icon).into(binding.image1)
            binding.nameTv.text = data.name
            binding.saleTv.text = data.punchline
            binding.btnAdd.text = if (data.is_favourite == "1") "Added" else "Add"
            binding.btnAdd.isSelected = data.is_favourite == "1"
       //     binding.rupee.visibility = if (data.tcash == "1") View.VISIBLE else View.INVISIBLE
            binding.cashbackAmountTv.text =
                data.cashback?.let {
                    getSpannableCashbackText(it,
                        itemView.resources.getColor(
                            R.color.orange1))
                }

            binding.btnAdd.setOnClickListener {
                clickListener.onRecyclerItemClick(adapterPosition, data, "favourite")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowCategoriesDataBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

     fun makeFev(position: Int, type: String) {
        if (type == "Fev")
            list[position].is_favourite = "1"
        else
            list[position].is_favourite = "0"
        notifyItemChanged(position)
    }

}