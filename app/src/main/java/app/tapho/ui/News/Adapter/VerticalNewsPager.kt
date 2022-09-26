package app.tapho.ui.News.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.News.Model.AllNews.Data
import app.tapho.ui.News.NewsActivity
import app.tapho.ui.News.NewsWebViewActivity
import app.tapho.ui.WebViewActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class VerticalNewsPager(val context: Context, val news: List<Data>,private val clickListener: RecyclerClickListener) :RecyclerView.Adapter<VerticalNewsPager.ViewHolder>() {
//    lateinit var mOnClickListener: NewsInterface
    lateinit var gestureDetector:GestureDetector
    var url=""
    var tep=0
    var position12=NewsActivity.position

    inner class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val  discription=itemview.findViewById<TextView>(R.id.discription)
        val  heading=itemview.findViewById<TextView>(R.id.heading)
        val  image=itemview.findViewById<ImageView>(R.id.image)
        val  category=itemview.findViewById<TextView>(R.id.category)
        val  share=itemview.findViewById<CardView>(R.id.share)
        val  pub_name=itemview.findViewById<TextView>(R.id.pub_name)
        val  date=itemview.findViewById<TextView>(R.id.date)
        val  shortNews=itemview.findViewById<RelativeLayout>(R.id.shortNews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val itemview=inflater.inflate(R.layout.shots_news_layout,parent,false)
        return ViewHolder(itemview)

    }

    @SuppressLint("LogConditional")
    override fun onBindViewHolder(holder: ViewHolder, position1: Int) {

        var tem=position1
        if (tem==0){
            tep=position12+tem
        }
        tep=position12+tem
        var position=tep
        if(news.size-2==position){
            clickListener.onRecyclerItemClick(position,news,"")
        }

        val publishername=news.get(position).description.substringAfterLast("Publisher_name:")
        val pub_name=publishername.replace("</p>","")
        val pub_name1=pub_name.replace("-"," ")

       // holder.pub_name.text=pub_name1
        holder.date.text=news.get(position).date
        Glide.with(context).load(news[position].featured_image).centerCrop().into(holder.image)
        holder.discription.text= Html.fromHtml(news.get(position).description.replace("Publisher_name:","")).toString()
        holder.heading.text=Html.fromHtml(news.get(position).name).toString()
  //      holder.category.text=news.get(position).id.toString()
        url=news.get(position).link

        if (url.contains("www.")){
            var providerName = url.replace("https://www.", "")
            var name = providerName.replaceAfter(".", "")
            holder.pub_name.text = "· " + "" + name.replace(".","")
        }else{
            var providerName = url.replace("https://", "")
            var name = providerName.replaceAfter(".", "")
            holder.pub_name.text =  name.replace(".","")
        }

        holder.share.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            // intent.setPackage("com.whatsapp")
            val appLink: String = "https://play.google.com/store/apps/details?id=app.tapho"
            //val message: String = "I found an amazing product for you, hope you will like it checkout here"
            val newMessage: String =
                "also I’m saving on every shopping over 200+ stores like Flipkart, Myntra, Ajio, Mamaearth & more with Tapfo, Just download the app to save more"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey\n" +  url.toString() + "\n\n\n" + newMessage + "\n\n" + appLink
            )
            intent.type = "text/plain"

          context.startActivity(Intent.createChooser(intent, "Share To:"))
        }

        holder.shortNews.setOnClickListener (object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                NewsWebViewActivity.openwebView(context,url)
            }
        })
    }

    abstract class DoubleClickListener : View.OnClickListener {
        var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }

        abstract fun onDoubleClick(v: View?)

        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }

}



