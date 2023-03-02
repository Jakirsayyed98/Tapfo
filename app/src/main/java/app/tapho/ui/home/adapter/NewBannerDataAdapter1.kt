package app.tapho.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.interfaces.RecyclerClickListener
import com.bumptech.glide.Glide

class NewBannerDataAdapter1  internal constructor(sliderItem: MutableList<SliderModelMain>, viewPager: ViewPager2, val clickListener: RecyclerClickListener):
    RecyclerView.Adapter<NewBannerDataAdapter1.SliderViewHolder>(){
     lateinit var context: Context
    val SliderItemlist:List<SliderModelMain>
    val ViewPager2: ViewPager2


    init {
        this.SliderItemlist=sliderItem
        this.ViewPager2=viewPager
    }

    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sliderItem: ImageView =itemView.findViewById(R.id.bannerImg)



        fun SliderData(SliderImageItem: SliderModelMain){
            Glide.with(itemView.context).load(SliderImageItem.image).placeholder(R.drawable.loding_app_icon).centerCrop()
                .into(sliderItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.new_banner2,parent,false))
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