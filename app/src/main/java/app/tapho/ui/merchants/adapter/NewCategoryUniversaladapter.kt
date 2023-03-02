package app.tapho.ui.merchants.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.NewCategoryMiniappBinding
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide
import java.net.URLDecoder

class NewCategoryUniversaladapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, NewCategoryUniversaladapter<S>.Holder>() {
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

    inner class Holder(private val binding: NewCategoryMiniappBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var miniApp: MiniApp? = null
            var tcash: String? = ""
            var cashback: String? = ""
            var discription: String? = ""
            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                miniApp = s.mini_app
                cashback = s.cashback
                discription=s.mini_app?.punchline
                tcash = s.mini_app?.tcash
            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                miniApp = s
                tcash = s.tcash
                cashback = s.cashback
                discription=s.punchline
            }
            if (morePos != 0 && adapterPosition == morePos - 1) {

                Glide.with(itemView.context).load(R.drawable.ic_more_right)
                    .into(binding.ivPartner)
                binding.nameTv.text = itemView.context.getString(R.string.more)
                binding.discription.text = discription

              //  binding.cashback.text= getSpannableCashbackText(if (cashback.isNullOrEmpty()) "" else cashback, itemView.resources.getColor(R.color.new_green2))
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,
                        list,
                        "MiniAppFragment")
                }
            } else {
                    kotlin.runCatching {
//
                            binding.cashback.text =if (cashback.isNullOrEmpty()) "" else URLDecoder.decode(cashback, "UTF-8") //getSpannableCashbackText(cashback,itemView.resources.getColor(R.color.new_green2))

                }
                Glide.with(itemView.context).load(image).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.ivPartner)
                binding.discription.text = discription
                binding.nameTv.text = name
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)
                    //   ActiveCashbackForWebActivity.openWebView(it.context,url,miniAppId,image,tCashType,isFavourite)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            NewCategoryMiniappBinding.inflate(
                LayoutInflater.from(parent.context),
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