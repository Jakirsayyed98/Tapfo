package app.tapho.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
//import app.tapho.databinding.MerchantDealsBinding
import app.tapho.databinding.NewOfferCardLayoutBinding
import app.tapho.databinding.RowMerchantDealsBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.home.OfferAndCoupon.Adapter.AllOfferCardAdapter
import app.tapho.ui.merchants.model.OfferData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition

class DealsAndOfferAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, DealsAndOfferAdapter<S>.Holder>() {
    var Backgroundcolor = 0

    inner class Holder(val binding: NewOfferCardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data: S) {

            if (data is OfferData) {

                binding.name.text = data.mini_app.get(0).name
                binding.lable.text = data.label

                if (data.name!!.contains("Rs")){
                    binding.discription.text = data.name!!.replace("Rs","₹")
                }else{
                    binding.discription.text = data.name
                }
                if (data.type!!.contains("3")){
                    binding.offerType.text =  "Coupon"
                }else if (data.type!!.contains("2")){
                    binding.offerType.text = "Offer"
                }
                binding.visited.text = data.visited +" Used today"

                Glide.with(itemView.context).asBitmap().load(data.mini_app.get(0).image).circleCrop()
                    .into(object : BitmapImageViewTarget(binding.icon) {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            super.onResourceReady(resource, transition)

                            setColor(resource)

                            binding.backgroundCard.setBackgroundColor(Backgroundcolor)

                        }
                    })

                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0,data.url , Backgroundcolor.toString())
                }
            }

        }
    }

    private fun setColor(resource: Bitmap) {
        createPaletteSync(resource).vibrantSwatch?.rgb?.let {
            setColordata(it)

        }
    }

    private fun setColordata(Color: Int) {
        Backgroundcolor =Color
    }

    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(NewOfferCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()

    }
}