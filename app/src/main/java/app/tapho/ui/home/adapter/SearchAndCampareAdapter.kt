package app.tapho.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.ui.home.ShopProduct.Model.SearchAndCompairdata
import app.tapho.ui.intro.sliderItem
import com.bumptech.glide.Glide

class SearchAndCampareAdapter internal constructor(sliderItem: MutableList<SearchAndCompairdata>, viewPager: ViewPager2):
    RecyclerView.Adapter<SearchAndCampareAdapter.SliderViewHolder>(){
//
//  //  val SearchAndCompairdata:List<SearchAndCompairdata>
//    val ViewPager2: ViewPager2
//    init {
//        this.SearchAndCompairdata=sliderItem
//        this.ViewPager2=viewPager
//    }

    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val icon: ImageView =itemView.findViewById(R.id.iv_partner)
        val name: TextView =itemView.findViewById(R.id.nameTv)
        val rupee: TextView =itemView.findViewById(R.id.rupee)


        fun SliderData(){
//            Glide.with(itemView.context).load(SliderImageItem.image).centerCrop().into(icon)
//            name.text=SliderImageItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recommended_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
//        holder.SliderData(SearchAndCompairdata[position])
//        if (position==SearchAndCompairdata.size-1){
//            ViewPager2.post(runnable)
//        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    private val runnable= Runnable {
        sliderItem.addAll(sliderItem)
        notifyDataSetChanged()
    }
}