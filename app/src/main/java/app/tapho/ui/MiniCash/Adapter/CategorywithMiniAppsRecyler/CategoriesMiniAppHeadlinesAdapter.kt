package app.tapho.ui.MiniCash.Adapter.CategorywithMiniAppsRecyler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.MiniappHeadlinesLayoutBinding
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide

class CategoriesMiniAppHeadlinesAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, CategoriesMiniAppHeadlinesAdapter<S>.Holder>() {
    private var morePos = 0
    private var showRupee = true


    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    inner class Holder(private val binding: MiniappHeadlinesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var ID: String? = ""
            var App_Category_id: String? = ""
            var miniApp: MiniApp? = null
            var tcash: String? = ""
            var cashback: String? = ""
            var fav: String? = ""
            var url:String?=""
            var urltype:String?=""
            if (s is NewCashback) {
                ID = s.mini_app?.id
                name = s.mini_app?.name
                image = s.mini_app?.image
                miniApp = s.mini_app
                cashback = s.cashback
                tcash = s.mini_app?.tcash
                urltype = s.mini_app?.url_type
            } else if (s is NewCashback) {
                ID = s.mini_app!!.id
                name = s.mini_app?.name
                image = s.mini_app?.image
                miniApp = s.mini_app
                cashback = s.cashback
                tcash = s.mini_app?.tcash
                urltype = s.mini_app?.url_type
            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                ID=s.id
                url=s.url
                fav= s.is_favourite.toString()
                //  miniApp = s.
                tcash = s.tcash.toString()
                App_Category_id = s.app_category_id.toString()
                cashback = s.cashback
                urltype = s.url_type
            }
                kotlin.runCatching {
//                    binding.cashback.text =
//                        getSpannableCashbackText(if (cashback.isNullOrEmpty()) "" else cashback,
//                            itemView.resources.getColor(
//                                R.color.black))
                }

                Glide.with(itemView.context).load(image).into(binding.image)

                binding.root.setOnClickListener {
                    ActiveCashbackForWebActivity.openWebView(it.context,url,ID,image,tcash,fav,cashback,App_Category_id,urltype,name)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            MiniappHeadlinesLayoutBinding.inflate(
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