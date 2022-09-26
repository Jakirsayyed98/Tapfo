package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.MiniappHeadlinesLayoutBinding
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.ui.model.NewHomeRes.MiniAppX
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.getSpannableCashbackText
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition

class MiniAppHeadlinesAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, MiniAppHeadlinesAdapter<S>.Holder>() {
    private var morePos = 0

    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    inner class Holder(private val binding: MiniappHeadlinesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var name: String? = ""
            var image: String? = ""
            var card: String? = ""
            var logo: String? = ""
            var ID: String? = ""
            var App_Category_id: String? = ""
            var miniApp: MiniApp? = null
            var tcash: String? = ""
            var cashback: String? = ""
            var fav: String? = ""
            var url:String?=""
            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                card = s.mini_app?.card
                miniApp = s.mini_app
                cashback = s.cashback
                tcash = s.mini_app?.tcash
            } else if (s is MiniAppX) {
                name = s.name
                image = s.card
                card = s.card
                ID=s.id
                url=s.url
                fav= s.is_favourite.toString()
              //  miniApp = s.
                tcash = s.tcash.toString()
                App_Category_id = s.app_category_id.toString()
                cashback = s.name
            } else if (s is MiniApp) {
                name = s.name
                image = s.image
                card = s.card
                logo = s.logo
                ID=s.id
                url=s.url
                fav= s.is_favourite.toString()
                //  miniApp = s.
                tcash = s.tcash.toString()
                App_Category_id = s.app_category_id.toString()
                cashback = s.cashback
            }
//            if (tcash.equals("0")){
//                binding.cashbackribbion.visibility = View.GONE
//            }else{
//                binding.cashbackribbion.visibility = View.VISIBLE
//            }

                Glide.with(itemView.context).asBitmap().load(image).circleCrop().into(object : BitmapImageViewTarget(binding.image) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(resource, transition)
                        createPaletteSync(resource).vibrantSwatch?.rgb?.let {
                            binding.middelLine.setBackgroundColor(it)
                        }
                    }
                })

                binding.name.text = name

                binding.root.setOnClickListener {
                     WebViewActivity.openWebView(it.context,url,ID,image,tcash,fav,"",App_Category_id)
            }

        }
    }
    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

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