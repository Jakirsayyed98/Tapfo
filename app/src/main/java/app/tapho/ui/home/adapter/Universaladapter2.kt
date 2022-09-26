package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.MerchantDatamodelClass.MiniApp
import app.tapho.ui.WebViewActivity
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide


class Universaladapter2 <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, Universaladapter2<S>.Holder>()  {

    inner class Holder(private val binding: RecommendedItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {

            if (s is MiniApp) {
                Glide.with(itemView.context).load(s.image).centerCrop().into(binding.ivPartner)
                binding.nameTv.text = s.name
                if (s.cashback.equals("null")){
                    binding.rupee.visibility= View.GONE
                }else if (s.cashback.isNullOrEmpty()){
                    binding.rupee.visibility= View.GONE
                }
                binding.root.setOnClickListener {
                    WebViewActivity.openWebView(itemView.context,
                        s.url,
                        s.id,
                        s.image,
                        s.tcash.toString(),
                        s.is_favourite.toString(),s.cashback,s.app_category_id)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            RecommendedItemLayoutBinding.inflate(
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