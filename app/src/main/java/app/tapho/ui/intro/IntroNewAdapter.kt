package app.tapho.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import com.bumptech.glide.Glide


class IntroNewAdapter internal constructor(sliderItem: MutableList<sliderItem>,viewPager:ViewPager2):RecyclerView.Adapter<IntroNewAdapter.SliderViewHolder>(){

    val SliderItemlist:List<sliderItem>
    val ViewPager2:ViewPager2
    init {
        this.SliderItemlist=sliderItem
        this.ViewPager2=viewPager
    }

    class SliderViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val sliderItem:ImageView=itemView.findViewById(R.id.sliderItem)

        fun SliderData(SliderImageItem:sliderItem){
          //  Glide.with(itemView.context).load(SliderImageItem.image).centerCrop().into(sliderItem)
            Glide.with(itemView.context).load(SliderImageItem.image).centerCrop().into(sliderItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.intropage_layout,parent,false))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
            holder.SliderData(SliderItemlist[position])
        if (position==SliderItemlist.size-1){
            ViewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return SliderItemlist.size
    }

    private val runnable= Runnable {
        sliderItem.addAll(sliderItem)
        notifyDataSetChanged()
    }
}