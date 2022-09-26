package app.tapho.ui.LeaderShip.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.LeadboardLayoutBinding
import app.tapho.databinding.RecommendedItemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.LeaderShip.Model.Data
import app.tapho.ui.LeaderShip.Model.leaderboardRes
import app.tapho.ui.home.adapter.AllMiniAppSearchAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestOptions
import java.util.*
import kotlin.collections.ArrayList

class leadershipAdapter <S>/*(private val clickListener: RecyclerClickListener) */:
    BaseRecyclerAdapter<S, leadershipAdapter<S>.Holder>() {


    inner class Holder(private val binding: LeadboardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData1(s: S, position: Int){
            var pos = position+1
            binding.rank.text ="#" + pos.toString()
            if (position+1==1){
                binding.rank.setTextColor(Color.parseColor("#0DCA42"))
            }

            if (s is Data){

                var ImageUrl = "https://tapfo.in/delta/public/storage/user/"+s.image

                if (s.image.isNullOrEmpty()) {
                    binding.profileName.visibility = View.VISIBLE
                    binding.profileIv.visibility = View.GONE
                    binding.profileName.text = s.name

                } else {
                    binding.profileName.visibility = View.GONE
                    Glide.with(itemView.context).load(ImageUrl).apply(
                        RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
                    ).into(binding.profileIv)
                }

                binding.name.text = s.name.toUpperCase(Locale.getDefault())
                binding.cashback.text = withSuffixAmount( s.lifetime_earning.toString())



            }
        }
/*
        fun setData(s: S) {

            binding.rank.text ="#"

            if (s is Data){

                if (s.image.isNullOrEmpty()) {
                    binding.profileName.visibility = View.VISIBLE
                    binding.profileIv.visibility = View.GONE
                    binding.profileName.text = s.name

                } else {
                    binding.profileName.visibility = View.GONE
                    Glide.with(itemView.context).load(s.image).apply(
                        RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
                    ).into(binding.profileIv)
                }

                binding.name.text = s.name
                binding.cashback.text = s.lifetime_earning.toString()



            }

        }

 */
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LeadboardLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
      //  holder.setData(list[position])
        holder.setData1(list[position],position)
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