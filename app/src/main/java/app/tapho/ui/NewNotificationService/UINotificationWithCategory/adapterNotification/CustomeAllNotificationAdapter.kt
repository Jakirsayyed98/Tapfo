package app.tapho.ui.NewNotificationService.UINotificationWithCategory.adapterNotification

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.AllNotificationAdapterLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter

import app.tapho.utils.parseTime
import com.bumptech.glide.Glide

class CustomeAllNotificationAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, CustomeAllNotificationAdapter<S>.Holder>()  {



    inner class Holder(val binding: AllNotificationAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {

            var image : String?=""
            var title : String?=""
            var time : String?=""
            var discription : String?=""
            var Url : String?=""
            var pending : String?=""
            var type : String?=""


            Glide.with(itemView.context).load(image).circleCrop().into(binding.icon)
            binding.time.text = parseTime(time)
            binding.name.text = title
            binding.discription.text = discription


            if (pending.equals("0")){
                binding.pendinglayout.visibility =View.GONE
            }else{
                binding.pendinglayout.visibility =View.VISIBLE
                binding.pending.text = pending
            }

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, Url, type!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AllNotificationAdapterLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setSpannable(s: String?, textView: TextView) {
        try {
            textView.text = SpannableString(s).apply {
                setSpan(StyleSpan(Typeface.BOLD), 5, length - 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        } catch (e: Exception) {
            textView.text = s
        }

    }

}