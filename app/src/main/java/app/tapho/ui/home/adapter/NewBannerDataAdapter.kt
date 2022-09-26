package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.WebViewActivity
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class NewBannerDataAdapter  internal constructor(sliderItem: MutableList<SliderModelMain>, viewPager: ViewPager2,val clickListener: RecyclerClickListener):
    RecyclerView.Adapter<NewBannerDataAdapter.SliderViewHolder>(){
     lateinit var context: Context
    val SliderItemlist:List<SliderModelMain>
    val ViewPager2: ViewPager2


    init {
        this.SliderItemlist=sliderItem
        this.ViewPager2=viewPager
    }

    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sliderItem: ImageView =itemView.findViewById(R.id.sliderItem)

        fun SliderData(SliderImageItem: SliderModelMain){

            Glide.with(itemView.context).load(SliderImageItem.image)//.centerCrop()
                .into(sliderItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.newbanner,parent,false))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.SliderData(SliderItemlist[position])
//        if (position==SliderItemlist.size-1){
//            ViewPager2.post(runnable)
//        }
        holder.sliderItem.setOnClickListener {
//            if (SliderItemlist[position].url.toString().contains("http://")) {
//                WebViewActivity.openWebView(context, SliderItemlist[position].url, SliderItemlist[position].id,
//                    "", SliderItemlist[position].tcash, SliderItemlist[position].fav, SliderItemlist[position].cashback, "")
//            } else if (SliderItemlist[position].url.toString().contains("https://")) {
//                val browserIntent =
//                    Intent(Intent.ACTION_VIEW, Uri.parse(SliderItemlist[position].url.toString()))
//                context.startActivity(browserIntent)
//            }
            clickListener.onRecyclerItemClick(0, SliderItemlist[position].url, "AppCategory")
        }
    }


    override fun getItemCount(): Int {
        return SliderItemlist.size
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private val runnable= Runnable {
//        sliderItem.addAll(sliderItem)
//        notifyDataSetChanged()
//    }
}