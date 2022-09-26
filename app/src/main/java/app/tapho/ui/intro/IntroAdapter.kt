package app.tapho.ui.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.RowIntroBinding

class IntroAdapter : RecyclerView.Adapter<IntroAdapter.Holder>() {
    inner class Holder(val binding: RowIntroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(RowIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (position) {
            0 -> {
                holder.binding.ivIcon.setImageResource(R.drawable.onboarding1)
//                holder.binding.tvHeading.text = "Everything in just one tap!"
//                holder.binding.tvDescription.text = "With Tapfo no need to download each app, save money and time over 300+ lite apps"
            }
            1 -> {
                holder.binding.ivIcon.setImageResource(R.drawable.onboarding2)
//                holder.binding.tvHeading.text = "Earn Extra Cashback"
//                holder.binding.tvDescription.text = "With Tapfo Earn Extra Cashback from Ajio, Myntra 300+ cashback merchant partners"
            }
            2 -> {
                holder.binding.ivIcon.setImageResource(R.drawable.onboarding3)
//                holder.binding.tvHeading.text = "Simple, Fast & Easy"
//                holder.binding.tvDescription.text = "300+ Lite Apps simplifying your online experience with fast and easy to use in just one tap"
            }

            /*
            0 -> {
                holder.binding.ivIcon.setImageResource(R.drawable.intro_1)
                holder.binding.tvHeading.text = "Everything in just one tap!"
                holder.binding.tvDescription.text = "With Tapfo no need to download each app, save money and time over 300+ lite apps"
            }
            1 -> {
                holder.binding.ivIcon.setImageResource(R.drawable.intro_2)
                holder.binding.tvHeading.text = "Earn Extra Cashback"
                holder.binding.tvDescription.text = "With Tapfo Earn Extra Cashback from Ajio, Myntra 300+ cashback merchant partners"
            }
            2 -> {
                holder.binding.ivIcon.setImageResource(R.drawable.intro_3)
                holder.binding.tvHeading.text = "Simple, Fast & Easy"
                holder.binding.tvDescription.text = "300+ Lite Apps simplifying your online experience with fast and easy to use in just one tap"
            }
            3 -> {
                holder.binding.ivIcon.setImageResource(R.drawable.intro_4)
                holder.binding.tvHeading.text = "Unlock Offers & Coupons"
                holder.binding.tvDescription.text = "Get the best daily offers and coupons from 300+ popular merchants in just one tap!"
            }

             */
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}