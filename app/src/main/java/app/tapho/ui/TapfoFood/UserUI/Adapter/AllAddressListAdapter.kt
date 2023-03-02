package app.tapho.ui.TapfoFood.UserUI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.AddressitemLayoutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.TapfoFood.UserUI.model.addresslistModel

class AllAddressListAdapter (private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<addresslistModel,AllAddressListAdapter.Holder>() {
    inner class Holder(val binding: AddressitemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun setData(data : addresslistModel){

                binding.firstline.text = data.flatnumber
                binding.secondline.text = data.flatnumber+" "+data.Roadname+" "+data.landmark+ " "+data.optional

                binding.root.setOnClickListener {
                    binding.buttoncheck.isChecked = true
                    if (binding.buttoncheck.isChecked){
                        clickListener.onRecyclerItemClick(0,"","click")
                    }

                }

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(AddressitemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}