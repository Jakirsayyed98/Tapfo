package app.tapho.ui.NewNotificationService.UINotificationWithCategory.adapterNotification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ReadnotificationLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter

import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationCategory.Data

class CustomeReadNotificationAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<Data, CustomeReadNotificationAdapter.Holder>() {

    inner class Holder(val binding: ReadnotificationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: Data) {


//            binding.time.text = data.time
            binding.name.text = data.name
       //     binding.discription.text = data.discription

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0, data.type, "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ReadnotificationLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}