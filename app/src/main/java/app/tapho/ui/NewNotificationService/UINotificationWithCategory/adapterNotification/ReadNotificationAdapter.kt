package app.tapho.ui.NewNotificationService.UINotificationWithCategory.adapterNotification

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ReadnotificationLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.Data
import app.tapho.ui.WebViewActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.utils.parseDate2
import app.tapho.utils.parseTime
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReadNotificationAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ReadNotificationAdapter<S>.Holder>() {

    inner class Holder(val binding: ReadnotificationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var time : String?= ""
            var date : String?= ""
            var name : String?= ""
            var discription : String?= ""
            var image : String?= ""
            var type : String?= ""
            var Url : String?= ""
            var id : String?=""
            var isfav : String?=""

            if (data is Data){


                if (data.noti_type.equals("0")){
                    image = data.data.get(0).image//.toString()
                    Url = data.data.get(0).url.toString()
                    date = parseDate2(data.created_at.toString())?.dropLast(11)
                    name =data.data.get(0).title
                    time = parseTime(data.created_at.toString())
                    type = data.noti_type
                    //       discription = data.data.get(0).description.toString()

                } else if (data.noti_type.equals("1")){
                        image = data.offer_data.get(0).image//.toString()
                    Url = data.offer_data.get(0).url.toString()
                    name = data.offer_data.get(0).name
                    time = parseTime(data.created_at.toString())
                    date = parseDate2(data.created_at.toString())?.dropLast(11)
                    type = data.noti_type
//                    discription = data.offer_data.get(0).description.toString()

                }else if (data.noti_type.equals("2")){
                    image = data.gamezop.get(0).assets.brick
                    Url = data.gamezop.get(0).url
                    discription = data.gamezop.get(0).description
                    name = data.title
                    time = parseTime(data.created_at.toString())
                    date = parseDate2(data.created_at.toString())?.dropLast(11)
                    type = data.noti_type
                    id = data.gamezop.get(0).id
                }

                binding.time.text = time.toString()
                binding.dateTv.text = date.toString()
                binding.name.text = name
                binding.discription.text = discription
                if (image.toString().isNullOrEmpty()) {
                    binding.imagelayout.visibility = View.GONE
                } else {
                    binding.imagelayout.visibility = View.VISIBLE
                    Glide.with(itemView.context).load(image).into(binding.image)
//                binding.pending.text = data.pending
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                try {
                    val time = sdf.parse(date).time
                    val now = System.currentTimeMillis()
                    val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                    binding.dateTv.text= ago
                } catch (e: ParseException) {
                    e.printStackTrace()
                }



                binding.root.setOnClickListener {
                    if (type.toString().equals("2")){
                        GameWebViewActivity.openWebView(itemView.context,Url.toString(),id,name,"","")
                    }else{
                        WebViewActivity.openwebView(itemView.context, Url.toString())
                    }
//                    clickListener.onRecyclerItemClick(0, Url, type!!)
                }

            }



        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ReadnotificationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1:ArrayList<S>){
        list=list1
        notifyDataSetChanged()
    }

}