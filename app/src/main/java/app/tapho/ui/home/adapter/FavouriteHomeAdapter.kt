package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.games.models.GamezopCategory.Data
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide

class FavouriteHomeAdapter(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<MiniApp, FavouriteHomeAdapter.Holder>() {
    private var morePos = 0
    private val shortList = ArrayList<MiniApp>()

    override fun addAllItem(list: List<MiniApp>) {
        super.addAllItem(list)
        showLess()
    }

    private fun showLess() {
        shortList.clear()
        if (list.size > 8) {
            shortList.addAll(list)
        } else
            showAll()
    }

    private fun showAll() {
//        morePos = 0
        shortList.clear()
        shortList.addAll(list)
//        if (list.size > 8) {
//            shortList.add(MiniApp().apply {
//                name = "Less"
//            })
//        }
//        notifyItemRangeChanged(0, itemCount)
    }

    override fun clear() {
        super.clear()
        shortList.clear()
        notifyItemRangeRemoved(0, shortList.size)
    }

    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    inner class Holder(private val binding: RecommendedItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: MiniApp) {
            val miniApp: MiniApp?
            val name: String? = s.name
            val image: String? = s.image
            miniApp = s
            val tcash: String? = s.tcash
            val cashback: String? = s.cashback
            if (s.name == "More" || s.name == "Less") {
                binding.rupee.visibility = View.INVISIBLE
                when (s.name) {
                    itemView.context.getString(R.string.more) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_more_blue)
                            .placeholder(R.drawable.loding_app_icon).into(binding.ivPartner)
                        binding.nameTv.text = "More"
                        s.name = "More"
                    }
                    itemView.context.getString(R.string.less) -> {
                        Glide.with(itemView.context).load(R.drawable.ic_less_blue)
                            .placeholder(R.drawable.loding_app_icon).into(binding.ivPartner)
                        binding.nameTv.text = "Less"
                        s.name = "Less"
                    }
                }
                binding.nameTv.text = itemView.context.getString(R.string.more)
                binding.root.setOnClickListener {
                    if (s.name == "More"){
                  //      showAll()
                        binding.nameTv1.visibility = View.VISIBLE
                        binding.nameTv.visibility = View.INVISIBLE
                    }
                    else
                   //     showLess()
                    binding.nameTv.visibility = View.VISIBLE
                    binding.nameTv1.visibility = View.INVISIBLE
                }
            } else {
                kotlin.runCatching {
//                    binding.cashbackAmountTv.text =
//                        getSpannableCashbackText(if (cashback.isNullOrEmpty()) "" else cashback,
//                            itemView.resources.getColor(
//                                R.color.black))
                }
                binding.rupee.visibility = if (tcash == "1") View.VISIBLE else View.INVISIBLE
                Glide.with(itemView.context).load(image).placeholder(R.drawable.loding_app_icon).circleCrop()
                    .into(binding.ivPartner)
                binding.nameTv.text = name
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, miniApp, OPEN_WEB_VIEW)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RecommendedItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(shortList[position])
    }

    override fun getItemCount(): Int {
        return shortList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1 : ArrayList<MiniApp>){
        list=list1
        notifyDataSetChanged()
    }
}