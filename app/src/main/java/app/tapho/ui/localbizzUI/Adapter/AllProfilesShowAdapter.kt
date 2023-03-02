package app.tapho.ui.localbizzUI.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.ShowProfileForEditBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.Data
import app.tapho.utils.parseDate6
import com.bumptech.glide.Glide

import java.util.*

class AllProfilesShowAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, AllProfilesShowAdapter<S>.Holder>() {


    inner class Holder(private val binding: ShowProfileForEditBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun setData(s: S) {
            if (s is Data) {
                Glide.with(itemView.context).load(s.image).centerCrop().into(binding.banner)
                Glide.with(itemView.context).load(s.logo).centerCrop().into(binding.profileIcon)
                binding.businessName.text = s.business_name

                if (s.status.toString().equals("0")){
                    binding.profileStatus.text = "Under Verification"
                    binding.profileStatus.setTextColor(R.color.orange1)
                }else if (s.status.toString().equals("1")){
                    binding.profileStatus.text = "Verified"
                    binding.profileStatus.setTextColor(R.color.green_dark)
                }else{
                    binding.profileStatus.text = "Rejected"
                    binding.profileStatus.setTextColor(R.color.red)
                }

                binding.createAt.text = "Your business submitted on " + parseDate6(s.created_at) + " is being processed"
                binding.edit.setOnClickListener {
                    LocalbizContainerActivity.openContainerforEditBusiness(itemView.context, "EditBusinessFragment",s.id)
                }
                binding.rating.setOnClickListener {
                    LocalbizContainerActivity.openContainerforEditBusiness(itemView.context, "BusinessPersonRatingAndReviewFragment",s.id)
                }
                binding.BizzCard.setOnClickListener {
                    LocalbizContainerActivity.openContainerforEditBusiness(itemView.context, "GetBusinessCard", s.id)
                }

                binding.root.setOnClickListener {
                    clickListener.onRecyclerItemClick(0, "", "")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ShowProfileForEditBinding.inflate(
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