package app.tapho.ui.Faqs.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FaqQutionlayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.Faqs.Model.FaqQuestion

class faqsQutionLayoutAdapter <S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, faqsQutionLayoutAdapter<S>.Holder>() {

    inner class Holder(private val binding: FaqQutionlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            var Quetion = ""
            var Answer = ""
           if (s is FaqQuestion){
               Quetion = s.question
               Answer = s.answer

           }

            binding.Quetion.text = Quetion
            binding.Answer.text = Answer

            binding.Quetion.setOnClickListener {
                if (binding.Answer.isVisible){
                    binding.Answer.visibility=View.GONE
                    binding.arrow.setImageResource(R.drawable.arrow_down)
                }else{
                    binding.Answer.visibility=View.VISIBLE
                    binding.arrow.setImageResource(R.drawable.arrow_up)
                }
            }
            binding.arrow.setOnClickListener {
                if (binding.Answer.isVisible){
                    binding.Answer.visibility=View.GONE
                    binding.arrow.setImageResource(R.drawable.arrow_down)
                }else{
                    binding.Answer.visibility=View.VISIBLE
                    binding.arrow.setImageResource(R.drawable.arrow_up)
                }
            }

            binding.root.setOnClickListener {

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            FaqQutionlayoutBinding.inflate(
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

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(list1:ArrayList<S>){
        list=list1
        notifyDataSetChanged()
    }

}