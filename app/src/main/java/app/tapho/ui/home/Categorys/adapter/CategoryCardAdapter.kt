package app.tapho.ui.home.Categorys.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.CategoriesCashbackCardBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import com.bumptech.glide.Glide



class CategoryCardAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, CategoryCardAdapter<S>.Holder>() {

    inner class Holder(private val binding: CategoriesCashbackCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var id:String?=""
            var categoryID:String?=""
            var name: String? = ""
            var image: String? = ""
            var miniApp: MiniApp? = null
            var tcash: String? = ""
            var cashback: String? = ""
            var url:String?=""
            var urltype:String?=""
            var banner:String?=""
            var is_favourite:String?=""

            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                //miniApp = s.mini_app
                cashback = s.cashback
                tcash = s.mini_app?.tcash
                urltype = s.mini_app?.url_type
                id = s.mini_app?.id
                banner = s.image

            } else if (s is app.tapho.ui.model.MiniApp) {
                id=s.id
                categoryID=s.app_category_id
                name = s.name
                image = s.image
                urltype = s.url_type
               // miniApp = s
                tcash = s.tcash.toString()
                is_favourite = s.is_favourite.toString()
                cashback = s.cashback
                url=s.url
                banner=s.merchant_payout!!.image
            }
//            Glide.with(itemView.context).load(image).circleCrop().into(binding.partnerIV)
            if (banner.isNullOrEmpty().not()){
               Glide.with(itemView.context).load(banner).into(binding.background)
            }else{
                binding.MiniAppCard.visibility = View.GONE
            }


//            binding.cashback.text = cashback
//            kotlin.runCatching { binding.cashback.text = getSpannableCashbackText(if (cashback.isNullOrEmpty()) "" else cashback, itemView.resources.getColor(R.color.white) ) }
            binding.MiniAppCard.setOnClickListener {
                ActiveCashbackForWebActivity.openWebView(itemView.context,url ,id, image, tcash.toString(), is_favourite.toString(), cashback,
                    categoryID,urltype,name)
            //    clickListener.onRecyclerItemClick(0, list, "MiniAppFragment")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            CategoriesCashbackCardBinding.inflate(
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