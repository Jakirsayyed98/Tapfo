package app.tapho.ui.News.Adapter

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.News.Model.NewsSliderModel
import app.tapho.ui.home.adapter.NewBannerDataAdapter
import app.tapho.ui.home.adapter.SliderModelMain
import com.bumptech.glide.Glide

class NewsSliderAdapter internal constructor(
    sliderItem: MutableList<NewsSliderModel>,
    viewPager: ViewPager2,
    val clickListener: RecyclerClickListener
) :
    RecyclerView.Adapter<NewsSliderAdapter.SliderViewHolder>() {
    lateinit var context: Context
    val SliderItemlist: List<NewsSliderModel>
    val ViewPager2: ViewPager2


    init {
        this.SliderItemlist = sliderItem
        this.ViewPager2 = viewPager
    }

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val images: ImageView = itemView.findViewById(R.id.images)
        val share: ImageView = itemView.findViewById(R.id.share)
        val headlines: TextView = itemView.findViewById(R.id.headlines)
        val providerName: TextView = itemView.findViewById(R.id.providerName)


        fun SliderData(SliderImageItem: NewsSliderModel) {
            val url = SliderImageItem.Url
            Glide.with(itemView.context).load(SliderImageItem.featured_image).centerCrop()
                .into(images)
            headlines.text = Html.fromHtml(SliderImageItem.name)
            if (url!!.contains("www.")) {
                var providerName1 = url.replace("https://www.", "")
                var name = providerName1.replaceAfter(".", "")
                providerName.text = "· " + "" + name
            } else {
                var providerName1 = url.replace("https://", "")
                var name = providerName1.replaceAfter(".", "")
                providerName.text = "· " + "" + name
            }
            share.setOnClickListener {
                shareNews(url)
            }
        }

        private fun shareNews(url: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            // intent.setPackage("com.whatsapp")
            val appLink: String = "https://play.google.com/store/apps/details?id=app.tapho"
            //val message: String = "I found an amazing product for you, hope you will like it checkout here"
            val newMessage: String =
                "also I’m saving on every shopping over 200+ stores like Flipkart, Myntra, Ajio, Mamaearth & more with Tapfo, Just download the app to save more"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey\n" + url.toString() + "\n\n\n" + newMessage + "\n\n" + appLink
            )
            intent.type = "text/plain"
            itemView.context.startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.news_slider_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.SliderData(SliderItemlist[position])

        holder.images.setOnClickListener {
            clickListener.onRecyclerItemClick(0, SliderItemlist[position].Url, "")
        }
    }


    override fun getItemCount(): Int {
        return SliderItemlist.size
    }

}