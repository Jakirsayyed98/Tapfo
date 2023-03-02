package app.tapho.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.WebViewActivity
import com.bumptech.glide.Glide
//import com.makeramen.roundedimageview.RoundedImageView

class PopularBannerDataAdapter  internal constructor(sliderItem: MutableList<SliderModelMain2>, viewPager: ViewPager2, val clickListener: RecyclerClickListener):
    RecyclerView.Adapter<PopularBannerDataAdapter.SliderViewHolder>(){
     lateinit var context: Context
    val SliderItemlist:List<SliderModelMain2>
    val ViewPager2: ViewPager2


    init {
        this.SliderItemlist=sliderItem
        this.ViewPager2=viewPager
    }

    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sliderItem: ImageView =itemView.findViewById(R.id.sliderItem)
//        val icon: ImageView =itemView.findViewById(R.id.icon)
//        val cashback : TextView = itemView.findViewById(R.id.cashback)

        fun SliderData(SliderImageItem: SliderModelMain2){
            kotlin.runCatching {
                Glide.with(itemView.context).load(SliderImageItem.image).placeholder(R.drawable.loding_app_icon).centerCrop().into(sliderItem)
            }
//            kotlin.runCatching {
//                Glide.with(itemView.context).load(SliderImageItem.icon).placeholder(R.drawable.loding_app_icon).circleCrop().into(icon)
//            }


//            cashback.text = SliderImageItem.cashback!!.replace(" Cashback","\nCashback").replace(" cashback","\ncashback")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.popularbanneradapter,parent,false))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.SliderData(SliderItemlist[position])
        holder.sliderItem.setOnClickListener {
            clickListener.onRecyclerItemClick(0, SliderItemlist[position], "AppCategory")
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