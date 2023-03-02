package app.tapho.ui.home.OnlineStoresAdapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.adapter.MiniAppHeadlinesAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition

class OnlinePageMiniAppsHeadlineAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, OnlinePageMiniAppsHeadlineAdapter<S>.Holder>() {
    private var morePos = 0

    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    inner class Holder(private val binding: HomePagerecommendedStoreLayoutBinding) :
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
            var urlType:String?=""
            if (s is NewCashback) {
                name = s.mini_app?.name
                image = s.mini_app?.image
                urlType = s.mini_app?.url_type
                card = s.mini_app?.card
                miniApp = s.mini_app
                cashback = s.cashback
                tcash = s.mini_app?.tcash
                ID = s.mini_app?.id
            }  else if (s is MiniApp) {
                name = s.name
                image = s.image
                urlType = s.url_type
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
            binding.cashbackt.visibility = View.GONE
            if (morePos != 0 && adapterPosition == morePos - 1) {
//                binding.icon.setBackgroundResource(R.drawable.button_boarder_2)
//                binding.icon.setBackgroundColor(Color.parseColor("#000000cls"))
//                Glide.with(itemView.context).load(R.drawable.ic_more_right).placeholder(R.drawable.arrow_right).circleCrop().into(binding.icon)
                Glide.with(itemView.context).load(R.drawable.right_more_icon).placeholder(R.drawable.loding_app_icon).centerCrop().circleCrop().into(binding.icon)
                binding.name.text = "More"
                binding.cashbackt.visibility = View.GONE
                binding.cashback.visibility = View.GONE
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, list, "MiniAppFragment")
                }
            }else{
                if (!image.isNullOrEmpty()){
                    Glide.with(itemView.context).load(image).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.icon)
                }else{
                    Glide.with(itemView.context).load(logo).placeholder(R.drawable.loding_app_icon).circleCrop().into(binding.icon)
                }

                binding.cashback.visibility = View.GONE
                if (tcash.equals("1")){
                    binding.cashbackt.visibility = View.VISIBLE
                }else{
                    binding.cashbackt.visibility = View.GONE
                }

                binding.name.text = name
//            binding.cashback.text = cashback!!.replace("Upto","")

                binding.root.setOnClickListener {
                    ActiveCashbackForWebActivity.openWebView(it.context,url,ID,image,tcash,fav,cashback,App_Category_id,urlType,name)
                }

            }



        }
    }
    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(HomePagerecommendedStoreLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false))
//        return Holder(MiniappHeadlinesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false))
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