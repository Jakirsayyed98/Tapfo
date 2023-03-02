package app.tapho.ui.News.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.tapho.R
import app.tapho.databinding.HeadlineLayout2Binding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.News.Model.AllNews.Data
import app.tapho.ui.News.NewsWebViewActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HeadLinesAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Data, HeadLinesAdapter.Holder>() {

    inner class Holder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: Data) {
            if (binding is HeadlineLayout2Binding) {

                if (data.description.isNullOrEmpty()){
                    binding.cad.visibility=View.GONE
                }else{
                    Glide.with(itemView.context).load(data.featured_image).centerCrop().into(binding.images)
//                    Glide.with(itemView.context).load(R.drawable.newsprovider).circleCrop().into(binding.newsImg)
                    binding.headlines.text = Html.fromHtml(data.name)
                    val url=data.link
                    if (url.contains("www.")){
                        val providerName = url.replace("https://www.", "")
                        var name = providerName.replaceAfter(".", "")
                        binding.providerName.text = "· " + "" + name.replace(".","")
                    }else{
                        var providerName = url.replace("https://", "")
                        var name = providerName.replaceAfter(".", "")
                        binding.providerName.text =  name.replace(".","")
                    }

                    var date = data.date
                    val pattern = "yyyy-MM-dd"
                    val simpleDateFormat = SimpleDateFormat(pattern)
                    val currentdate: String = simpleDateFormat.format(Date())

                    var data1=date.dropLast(9)
                    if (data1.equals(currentdate)){
                        binding.date.text=date.drop(10)
                    }
                    else{
                        binding.date.text= date
                    }
                    binding.share.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        // intent.setPackage("com.whatsapp")
                        val appLink: String = "https://play.google.com/store/apps/details?id=app.tapho"
                        //val message: String = "I found an amazing product for you, hope you will like it checkout here"
                        val newMessage: String =
                            "also I’m saving on every shopping over 200+ stores like Flipkart, Myntra, Ajio, Mamaearth & more with Tapfo, Just download the app to save more"
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey\n" +  data.link.toString() + "\n\n\n" + newMessage + "\n\n" + appLink
                        )
                        intent.type = "text/plain"
                        itemView.context.startActivity(Intent.createChooser(intent, "Share To:"))
                    }
                }

                //binding.headlines.text = Html.fromHtml(data.name).toString()
                binding.headlineCard.setOnClickListener {
                  //  clickListener.onRecyclerItemClick(0, data, "AppCategory")
                    NewsWebViewActivity.openwebView(itemView.context,data.link)
                }
            }
        }
        /*
        fun setData1(data: Data) {
            if (binding is HeadlinesLayoutBinding) {

                    Glide.with(itemView.context).load(data.featured_image).centerInside().into(binding.images)

                binding.share.setOnClickListener {
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    // intent.setPackage("com.whatsapp")
                    val appLink: String = "https://play.google.com/store/apps/details?id=app.tapho"
                    //val message: String = "I found an amazing product for you, hope you will like it checkout here"
                    val newMessage: String =
                        "also I’m saving on every shopping over 200+ stores like Flipkart, Myntra, Ajio, Mamaearth & more with Tapfo, Just download the app to save more"
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey\n" + data.link.toString() + "\n\n\n" + newMessage + "\n\n" + appLink
                    )
                    intent.type = "text/plain"
                    itemView.context.startActivity(Intent.createChooser(intent, "Share To:"))
                }

                binding.headlines.text = Html.fromHtml(data.name)
                //binding.headlines.text = Html.fromHtml(data.name).toString()
                binding.headlineCard.setOnClickListener {
                  //  clickListener.onRecyclerItemClick(0, data, "AppCategory")
                    NewsWebViewActivity.openwebView(itemView.context,data.link)
                }
            }
        }
*/
    }
  //  textView.setText(Html.fromHtml(Html.fromHtml(mHtmlString).toString()));



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadLinesAdapter.Holder {
        return Holder(HeadlineLayout2Binding.inflate(LayoutInflater.from(parent.context), parent, false )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1:ArrayList<Data>){
        list=list1
        notifyDataSetChanged()

    }
}