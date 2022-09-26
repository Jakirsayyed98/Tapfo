package app.tapho.ui.NewNotificationService.UINotificationWithCategory.adapterNotification

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.AllNotificationAdapterLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationCategory.Data
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewCustomeAllNotification (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Data, NewCustomeAllNotification.Holder>() {

    inner class Holder(val binding: AllNotificationAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {

  //          Glide.with(itemView.context).load(data.image).circleCrop().into(binding.icon)
//            binding.time.text = data.date
            Log.d("TAGDATA", "setData: ${data.date}")
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val time = sdf.parse(data.date).time
                val now = System.currentTimeMillis()
                val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                binding.time.text= ago
                Log.d("TAGDATA", "setData: ${ago}")

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (data.name=="Merchant Trasactions"){
                binding.name.text = "Cashback & rewards"
            }else{
                binding.name.text = data.name
            }

//            binding.discription.text = data.notification.get(0).title
//            if (data.pending.isNullOrEmpty()){
//                binding.pendinglayout.visibility = View.GONE
//            }else{
//                binding.pendinglayout.visibility =View.VISIBLE
//                binding.pending.text = data.pending
//            }
            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AllNotificationAdapterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}