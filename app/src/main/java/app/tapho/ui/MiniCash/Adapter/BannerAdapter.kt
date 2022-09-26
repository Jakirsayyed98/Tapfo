package app.tapho.ui.MiniCash.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.MiniCash.model.BannerModel
import app.tapho.ui.home.NewGame.Model.GamebannerModel
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class BannerAdapter internal constructor(
    sliderItem: MutableList<BannerModel>,
    viewPager: ViewPager2,
    val clickListener: RecyclerClickListener
) :
    RecyclerView.Adapter<BannerAdapter.SliderViewHolder>() {
    lateinit var context: Context
    val SliderItemlist: List<BannerModel>
    val ViewPager2: ViewPager2

    init {
        this.SliderItemlist = sliderItem
        this.ViewPager2 = viewPager
    }

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val banner: ImageView = itemView.findViewById(R.id.banner)
        fun SliderData(SliderImageItem: BannerModel) {
            Glide.with(itemView.context).load(SliderImageItem.image).centerCrop().into(banner)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.banner_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.SliderData(SliderItemlist[position])

        holder.banner.setOnClickListener {
            clickListener.onRecyclerItemClick(0, SliderItemlist[position].url, "AppCategory")
        }
    }


    override fun getItemCount(): Int {
        return SliderItemlist.size
    }

}