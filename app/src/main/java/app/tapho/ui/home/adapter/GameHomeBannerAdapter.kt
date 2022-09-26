package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.home.HomeActivity
import com.bumptech.glide.Glide

class GameHomeBannerAdapter  internal constructor(sliderItem: MutableList<SliderModelMain>, viewPager: ViewPager2, val clickListener: RecyclerClickListener):
    RecyclerView.Adapter<GameHomeBannerAdapter.SliderViewHolder>(){
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

            Glide.with(itemView.context).load(SliderImageItem.image).centerCrop()
                .into(sliderItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.homegamesbanner,parent,false))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.SliderData(SliderItemlist[position])

        holder.sliderItem.setOnClickListener {
            clickListener.onRecyclerItemClick(0, SliderItemlist[position].url, "AppCategory")
        }
    }


    override fun getItemCount(): Int {
        return SliderItemlist.size
    }

}