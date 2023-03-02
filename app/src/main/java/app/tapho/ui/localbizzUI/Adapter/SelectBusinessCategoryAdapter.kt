package app.tapho.ui.localbizzUI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.checkchanged
import app.tapho.databinding.BusinessCategorySelectionBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.localbizzUI.Model.BusinessCategories.Data

class SelectBusinessCategoryAdapter <S>(private val clickListener: RecyclerClickListener) : BaseRecyclerAdapter<S,SelectBusinessCategoryAdapter<S>.Holder>() {

    inner class Holder(private val binding : BusinessCategorySelectionBinding) : RecyclerView.ViewHolder(binding.root){
        fun setData(s: S) {
            if(s is Data){
                binding.name.text = s.name
                binding.checkbox.checkchanged { compoundButton, b ->
//                    if (binding.checkbox.isChecked ==true){

                        binding.checkbox.isChecked = true
                        clickListener.onRecyclerItemClick(0,s,"")
//                    }
                }
                binding.root.setOnClickListener {
                    binding.checkbox.isChecked = true
                    clickListener.onRecyclerItemClick(0,s,"")
                }

            }

            if(s is app.tapho.ui.localbizzUI.Model.BusinessSubCategory.Data){
                binding.name.text = s.name
                binding.checkbox.checkchanged { compoundButton, b ->
//                    if (binding.checkbox.isChecked ==true){

                        binding.checkbox.isChecked = true
                        clickListener.onRecyclerItemClick(0,s,"")
//                    }
                }
                binding.root.setOnClickListener {

                    binding.checkbox.isChecked = true
                    clickListener.onRecyclerItemClick(0,s,"")
                }

            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(BusinessCategorySelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }



    override fun getItemCount(): Int {
        return list.size
    }


}