package app.tapho.ui.NewNotificationService.AllNotificationUI.Adapter

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

class AllNotificationAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AllNotificationAdapter<S>.Holder>() {

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
            var miniAppID : String?=""
            if (data is Data){

                if (data.noti_type.equals("0")){
                    image = R.drawable.notification_icon.toString()
                    miniAppID = data.app_category_mini_app_id


                    date = data.created_at
                    name =data.title
                    type = data.noti_type
//                    discription = data.data.get(0).description.toString()
                    discription = data.data.get(0).description

                } else if (data.noti_type.equals("1")){
                        image = data.offer_data.get(0).image//.toString()
                    if (!data.offer_data.get(0).url.isNullOrEmpty()){
                        Url = data.offer_data.get(0).url.toString()
                    }
                        date = data.created_at
                        type = data.noti_type
                    if (data.merchant_postback_value.isNullOrEmpty().not()){
                        var date=""
                        if(data.merchant_postback_value.get(0).date.toString().length<=11){
                            date = parseDate3(data.merchant_postback_value.get(0).date).toString()
                        }else{
                            date = getDate(data.merchant_postback_value.get(0).date).toString()
                        }

                        val status = data.merchant_postback_value.get(0).status
                        if (status.uppercase()=="VERIFIED"){
                            discription = "You have recieved "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision.toString())+" Cashback from "+data.offer_name+" order id "+data.merchant_postback_value.get(0).trans_id+" on amount "+ withSuffixAmount(data.merchant_postback_value.get(0).sale_amount) +" on "+ date+"."
                            name =  "Congratulations! "+data.offer_data.get(0).name+" has confirmed your "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision)+" cashback"
                        } else if (status.uppercase()=="VALIDATED"){
                            discription = "You have recieved "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision.toString())+" Cashback from "+data.offer_name+" order id "+data.merchant_postback_value.get(0).trans_id+" on amount "+ withSuffixAmount(data.merchant_postback_value.get(0).sale_amount) +" on "+ date+"."
                            name =  "Congratulations! "+data.offer_data.get(0).name+" has confirmed your "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision)+" cashback"
                        }else if (status.uppercase()=="PENDING"){
                            name ="Wow! you have received "+withSuffixAmount(data.merchant_postback_value.get(0).user_commision)+" cashback from "+data.offer_data.get(0).name

                            discription = "You have recieved "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision.toString())+" Cashback from "+data.offer_name+" order id "+data.merchant_postback_value.get(0).trans_id+" on transcation amount "+ withSuffixAmount(data.merchant_postback_value.get(0).sale_amount) +" on "+ date + ". T&C Apply"

                        }else if (status.uppercase()=="FAILED"){
                            discription = "Your "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision.toString())+" Cashback from "+data.offer_name+" order id "+data.merchant_postback_value.get(0).trans_id+" on transcation amount "+ withSuffixAmount(data.merchant_postback_value.get(0).sale_amount) +" on "+ date +"."
                            name = data.offer_data.get(0).name+" has declined your "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision)+" cashback."
                        }else if (status.uppercase()=="REJECTED"){
                            discription = "Your "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision.toString())+" Cashback from "+data.offer_name+" order id "+data.merchant_postback_value.get(0).trans_id+" on transcation amount "+ withSuffixAmount(data.merchant_postback_value.get(0).sale_amount) +" on "+ date +"."
                            name = data.offer_data.get(0).name+"has declined your "+ withSuffixAmount(data.merchant_postback_value.get(0).user_commision)+" cashback."
                        }
                    }


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
                } else {
                    binding.imagelayout.visibility = View.VISIBLE
                    Glide.with(itemView.context).load(image).placeholder(R.drawable.loding_app_icon).into(binding.image)
                }
                    binding.time.text= parseAgoDate(data.created_at)
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