package app.tapho.ui.localbizzUI.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.BusinessRatingShowLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.localbizzUI.Model.getBusinessDetails.Rating
import app.tapho.utils.parseDate3
import com.bumptech.glide.Glide

import java.util.*

class ProfilesRatingAndReviewsShowAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, ProfilesRatingAndReviewsShowAdapter<S>.Holder>() {


    inner class Holder(private val binding: BusinessRatingShowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is Rating) {
                binding.profileName.text = s.user_profile_detail.get(0).name.replaceAfter(" ", "")
                binding.name.text = s.user_profile_detail.get(0).name
                binding.ratingdate.text = parseDate3(s.created_at)
                binding.reviews.text = s.review
                binding.rating.rating = s.rating.toFloat()
                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s, "")
                }
            }else if (s is app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Rating) {
                binding.profileName.text = s.user_profile_detail.get(0).name.replaceAfter(" ", "")
                binding.name.text = s.user_profile_detail.get(0).name
                binding.ratingdate.text = parseDate3(s.created_at)
                binding.reviews.text = s.review
                binding.rating.rating = s.rating.toFloat()

                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, s, "")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            BusinessRatingShowLayoutBinding.inflate(
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

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1: ArrayList<S>) {
        list = list1
        notifyDataSetChanged()
    }
}