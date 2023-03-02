package app.tapho.ui.TapfoFood.UserUI.Adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.model.MiniApp


class OnlineAllDishesWithcartAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, OnlineAllDishesWithcartAdapter<S>.Holder>() {
    private var morePos = 0

    fun setMoreImagePos(morePos: Int) {
        this.morePos = morePos
    }

    inner class Holder(private val binding: DishesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(s: S) {
            if (s is MiniApp){
                var finalQuntity = 0

                binding.name.text =s.name
                binding.addlayout.setOnClickListener {
                    binding.cartQty.visibility = View.VISIBLE
                    binding.addlayout.visibility = View.GONE
                }

                binding.qty.text =finalQuntity.toString()
                binding.less.setOnClickListener {
                    QuntityData(finalQuntity--.toString(),s)
                }
                binding.add.setOnClickListener {
                    QuntityData(finalQuntity++.toString(),s)
                }
            }

        }
        private fun QuntityData(Quntity: String,s: MiniApp) {
            //  itemView.context.showLong(Quntity)

            binding.qty.text =Quntity


            if (Quntity.toInt()<=0){
                clickListener.onRecyclerItemClick(Quntity.toInt(),"","delete")
            }else{
                clickListener.onRecyclerItemClick(Quntity.toInt(),"","add")
            }


        }
    }
    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DishesLayoutBinding.inflate(
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