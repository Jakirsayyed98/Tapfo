package app.tapho.ui.localbizzUI.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import app.tapho.DirectCall
import app.tapho.copyElement
import app.tapho.databinding.ItemLayoutForCallbusinessBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.localbizzUI.Model.ContactModel
import kotlinx.android.synthetic.main.row_offer_category.view.*


class CalltoContactAdapter<S> (private var clickListener: RecyclerClickListener):BaseRecyclerAdapter<S, CalltoContactAdapter<S>.Holder>() {
  inner class Holder(private val binding: ItemLayoutForCallbusinessBinding) : RecyclerView.ViewHolder(binding.root) {
      fun setData(s: S) {
            if (s is ContactModel){

                if (s.Number.isNullOrEmpty()){
                    binding.root.visibility = View.GONE
                }else{
                    binding.numbers.text ="+91 "+ s.Number.toString()
                    binding.copy.setOnClickListener {
                        itemView.context.copyElement(s.Number.toString())
                        it.showShortSnack("Copied successfully")

                        clickListener.onRecyclerItemClick(0,s,"")
                    }
                    if (s.Number.isNullOrEmpty()){
                        binding.root.visibility = View.GONE
                    }

                    binding.call.setOnClickListener {
                        itemView.context.DirectCall(s.Number.toString())
                        clickListener.onRecyclerItemClick(0,s,"")
                    }
                }

            }

      }

  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemLayoutForCallbusinessBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}