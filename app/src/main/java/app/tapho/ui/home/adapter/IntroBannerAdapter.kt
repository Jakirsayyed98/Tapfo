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

class IntroBannerAdapter  internal constructor(sliderItem: MutableList<SliderModelIntro>, viewPager: ViewPager2, val clickListener: RecyclerClickListener):
    RecyclerView.Adapter<IntroBannerAdapter.SliderViewHolder>(){
     lateinit var context: Context
    val SliderItemlist:List<SliderModelIntro>
    val ViewPager2: ViewPager2


    init {
        this.SliderItemlist=sliderItem
        this.ViewPager2=viewPager
    }

    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sliderItem: ImageView =itemView.findViewById(R.id.sliderItem)



        fun SliderData(SliderImageItem: SliderModelIntro){
            Glide.with(itemView.context).load(SliderImageItem.image).placeholder(R.drawable.loding_app_icon)//.centerCrop()
                .into(sliderItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.newbanner,parent,false))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.SliderData(SliderItemlist[position])
        holder.sliderItem.setOnClickListener {

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