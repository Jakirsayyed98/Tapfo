package app.tapho.ui.profile.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.RowProfileBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.ProfileOptionsModel
import com.bumptech.glide.Glide


class ProfileOptionsAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<ProfileOptionsModel, ProfileOptionsAdapter.Holder>() {

    inner class Holder(val binding: RowProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: ProfileOptionsModel) {
         //   binding.imageIv.setImageResource(data.image)

            Glide.with(itemView.context).load(data.image).into(binding.imageIv)
            binding.nameTv.text = data.name
            binding.discription.text = data.discription
            if (data.toggle.equals("1")){
                binding.switchBtn.visibility = View.VISIBLE
            }else{
                binding.switchBtn.visibility = View.GONE
            }

            binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                switchColor(isChecked)
                if (isChecked){
                    itemView.context.getSharedPref().saveString("ScreenLock","0")
                }else{
                    itemView.context.getSharedPref().saveString("ScreenLock","1")
                }
            }




            if(itemView.context.getSharedPref().getString("ScreenLock").toString().equals("1")){
                binding.switchBtn.isChecked = false
            }else{
                binding.switchBtn.isChecked = true
            }

            binding.root.setOnClickListener {
                clickListener.onRecyclerItemClick(0,data.type,"")
            }
        }

        private fun switchColor(checked: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                binding.switchBtn.getThumbDrawable().setColorFilter(if (checked) Color.parseColor("#008D3A") else Color.BLACK, PorterDuff.Mode.MULTIPLY)
                binding.switchBtn.getTrackDrawable().setColorFilter(if (!checked) Color.BLACK else Color.DKGRAY, PorterDuff.Mode.MULTIPLY)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}