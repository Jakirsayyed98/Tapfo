package app.tapho.ui.home.OnlineStoresAdapter

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
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.WebViewActivity
import app.tapho.ui.model.MiniApp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition

class OnlineMiniAppHeadlinesAdapter/* <S>*/(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<MiniApp, OnlineMiniAppHeadlinesAdapter/*<S>*/.Holder>() {
    private var morePos = 0
    private val shortList = ArrayList<MiniApp>()

    override fun addAllItem(list: List<MiniApp>) {

        super.addAllItem(list)
        showLess()
    }

    private fun showLess() {
        shortList.clear()
        if (list.size > 6) {
            morePos = 6
            shortList.addAll(list.subList(0, 5))
            shortList.add(MiniApp().apply {
                name = "More"
            })
            notifyDataSetChanged()
        } else{
            showAll()
        }

    }

    private fun showAll() {
        morePos = 0
        shortList.clear()
        shortList.addAll(list)
        if (list.size > 8) {
            shortList.add(MiniApp().apply {
                name = "Less"
            })
        }
        notifyItemRangeChanged(0, itemCount)
    }

    override fun clear() {
        super.clear()
        shortList.clear()
        notifyItemRangeRemoved(0, shortList.size)
    }


    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    inner class Holder(private val binding: MiniappHeadlinesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: MiniApp) {


            val name = s.name
            val image = s.logo
            val urlType = s.url_type
            val ID=s.id
            val url=s.url
            val fav= s.is_favourite.toString()
            val tcash = s.tcash.toString()
            val App_Category_id = s.app_category_id.toString()

            if (s.name == "More" || s.name == "Less") {

                when (s.name) {
                    itemView.context.getString(R.string.more) -> {
//                        Glide.with(itemView.context).load(R.drawable.ic_more_blue).into(binding.moreBtn)
                        binding.moreBtn.visibility = View.VISIBLE
                        binding.namemore.text = "More"
                        s.name = "More"
                    }
                    itemView.context.getString(R.string.less) -> {
//                        Glide.with(itemView.context).load(R.drawable.ic_less_blue).into(binding.moreBtn)
                        binding.namemore.text = "Less"
                        binding.moreBtn.visibility = View.VISIBLE
                        s.name = "Less"
                    }
                }
//                binding.nameTv.text = itemView.context.getString(R.string.more)
                binding.root.setOnClickListener {
                    if (s.name == "More"){
                        binding.namemore.text = "Less"
                        showAll()

                        binding.moreBtn.visibility = View.VISIBLE
                        binding.miniapps.visibility = View.INVISIBLE
                    }
                    else{
                        binding.namemore.text = "More"
                        showLess()
                        binding.moreBtn.visibility = View.VISIBLE
                        binding.miniapps.visibility = View.INVISIBLE
                    }

                }


            }
            else{
                Glide.with(itemView.context).asBitmap().load(image).placeholder(R.drawable.loding_app_icon).into(object : BitmapImageViewTarget(binding.image) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(resource, transition)
                        createPaletteSync(resource).vibrantSwatch?.rgb?.let {
                            //  binding.middelLine.setBackgroundColor(it)
                        }
                    }
                })

                binding.name.text = name
                binding.root.setOnClickListener {

                        ActiveCashbackForWebActivity.openWebView(it.context,url,ID,image,tcash,fav,"",App_Category_id,urlType,name)


                }
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
    fun updatelist(list1:ArrayList<MiniApp>){
        list=list1
        notifyDataSetChanged()
    }

}