package app.tapho.ui.home.NewGame.Adapter

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
import app.tapho.ui.home.NewGame.Model.GamebannerModel
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class TopBannerGameAdapter  internal constructor(sliderItem: MutableList<GamebannerModel>, viewPager: ViewPager2, val clickListener: RecyclerClickListener):
    RecyclerView.Adapter<TopBannerGameAdapter.SliderViewHolder>(){
    lateinit var context: Context
    val SliderItemlist:List<GamebannerModel>
    val ViewPager2: ViewPager2


    init {
        this.SliderItemlist=sliderItem
        this.ViewPager2=viewPager
    }

    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var totalplays=""
        val banner :RelativeLayout =itemView.findViewById(R.id.banner)
        val image: ImageView =itemView.findViewById(R.id.image)
        val name: TextView =itemView.findViewById(R.id.name)
        val plays: TextView =itemView.findViewById(R.id.plays)
        val discription: TextView =itemView.findViewById(R.id.discription)

        fun SliderData(SliderImageItem: GamebannerModel){

            Glide.with(itemView.context).load(SliderImageItem.image).centerCrop().into(image)

            name.text = SliderImageItem.name
            val count:Double=SliderImageItem.plays!!.toDouble()
            suffixFunction(count)

            plays.text = totalplays.toString()
            discription.text = SliderImageItem.discription

        }
        private fun suffixFunction(count: Double) :String {
            val df = DecimalFormat("#.#")
            totalplays = if (Math.abs(count / 1000000) >= 1) {
                df.format(count / 1000000.0).toString() + "M Plays"
            } else if (Math.abs(count / 1000.0) >= 1) {
                df.format(count / 1000.0).toString() + "K Plays"
            } else {
                count.toString()+" Plays"
            }
            return totalplays;
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.game_top_banner_layout,parent,false))
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