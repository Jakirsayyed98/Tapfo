package app.tapho.ui.localbizzUI.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.BusinessCategoriesItemLayoutBinding
import app.tapho.databinding.RecommendedItemLayoutBinding
//import app.tapho.databinding.RowCashbackPartnerBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.localbizzUI.Model.BusinessCategories.Data
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class BusinessCategoriesAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, BusinessCategoriesAdapter<S>.Holder>() {
    private var morePos = 0
    private var showRupee = true

    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    fun setShowRupee(show: Boolean) {
        showRupee = show
    }

    fun removeItem(id: String) {
        var removePos = -1
        list.forEachIndexed { index, s ->
            if (s is MiniApp) {
                if (s.id == id) {
                    removePos = index
                    return@forEachIndexed
                }
            }
        }
        if (removePos != -1) {
            list.removeAt(removePos)
            notifyDataSetChanged()
        }
    }

    inner class Holder(private val binding: BusinessCategoriesItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var data = s
            var miniApp: MiniApp? = null
            var tcash: String? = ""
            var cashback: String? = ""
            var id: String ?=""
            if (s is Data) {
                name = s.name
                data = s
                id= s.id

                when (data!!.name) {
                    itemView.context.getString(R.string.more) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_more_blue)
                            .into(binding.ivPartner)
                    }

                    itemView.context.getString(R.string.less) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_less_blue)
                            .into(binding.ivPartner)
                    }

                    else -> {
                        Glide.with(itemView.context).load(data.image)
                            .into(binding.ivPartner)
                    }
                }
            }else if (s is app.tapho.ui.localbizzUI.Model.BusinessSubCategory.Data){
                name = s.name
                data = s
                id= s.id

                when (data!!.name) {
                    itemView.context.getString(R.string.more) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_more_blue)
                            .into(binding.ivPartner)
                    }

                    itemView.context.getString(R.string.less) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_less_blue)
                            .into(binding.ivPartner)
                    }

                    else -> {
                        Glide.with(itemView.context).load(data.image)
                            .into(binding.ivPartner)
                    }
                }
            }

                binding.nameTv.text = name
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, data, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(BusinessCategoriesItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1:ArrayList<S>){
        list=list1
        notifyDataSetChanged()
    }
}