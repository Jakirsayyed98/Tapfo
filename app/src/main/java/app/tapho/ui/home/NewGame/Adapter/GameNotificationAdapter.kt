package app.tapho.ui.home.NewGame.Adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.ReadnotificationLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.model.AllNotification.Data
import app.tapho.utils.*
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GameNotificationAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, GameNotificationAdapter<S>.Holder>() {

    inner class Holder(val binding: ReadnotificationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {
            var date : String?= ""
            var name : String?= ""
            var discription : String?= ""
            var image : String?= ""
            var type : String?= ""
            var Url : String?= ""
            var id : String?=""
            val miniAppID : String?=""
            if (data is Data){

                if (data.noti_type.equals("0")){
//
                    binding.root.visibility = View.GONE
                } else if (data.noti_type.equals("1")){

                    binding.root.visibility = View.GONE

                } else if (data.noti_type.equals("2")){
                    image = data.gamezop.get(0).assets.square
                    Url = data.gamezop.get(0).url
                    discription = data.gamezop.get(0).description
                    name = data.title
                    date = data.created_at
                    type = data.noti_type
                    id = data.gamezop.get(0).id
                }
                binding.name.text = name
                binding.discription.text = discription
                if (image.toString().isNullOrEmpty()) {
                  //  binding.imagelayout.visibility = View.GONE
                } else {
                    binding.imagelayout.visibility = View.VISIBLE
                    Glide.with(itemView.context).load(image).placeholder(R.drawable.loding_app_icon).into(binding.image)
                }



                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                try {
                    val time = sdf.parse(date).time
                    val now = System.currentTimeMillis()
                    val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                    binding.time.text= ago
                } catch (e: ParseException) {
                    e.printStackTrace()
                }



                binding.root.setOnClickListener {
                    if (type.toString().equals("1")){
                        ContainerActivity.openContainer(itemView.context, "transactionHistory", "")

                    }else if (type.toString().equals("2")){
                        GameWebViewActivity.openWebView(itemView.context,Url.toString(),id,name,"","")
                    }else if (type.toString().equals("0")){
                        clickListener.onRecyclerItemClick(0, miniAppID, "")
                    }else{
                        ActiveCashbackForWebActivity.openwebView(itemView.context, Url.toString(),"1")
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