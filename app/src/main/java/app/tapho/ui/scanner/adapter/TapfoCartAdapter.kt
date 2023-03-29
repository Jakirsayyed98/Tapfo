package app.tapho.ui.scanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.*
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.ui.BaseRecyclerAdapter
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TapfoCartAdapter<S>(private val clickListener: RecyclerClickListener) :
    BaseRecyclerAdapter<S, TapfoCartAdapter<S>.Holder>() {

    inner class Holder(private val binding: TapfocartLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(s: S) {
            if (s is Cart){
               binding.nameTv.text = s.name
               binding.qty.text = s.qty.toString()


         //      binding.qty.text = withSuffixAmount((s.qty.toDouble()*s.price.toDouble()).toString()).toString()

                if (s.price.isNullOrEmpty()){
                    binding.price.text = withSuffixAmount((s.mrp.toDouble()*s.qty.toDouble()).toString())
                    binding.price.visibility = View.GONE
                    binding.disprice.text = withSuffixAmount((s.mrp.toDouble()*s.qty.toDouble()).toString()) //+" ( "+s.qty.toString() + " ) "
                    binding.disprice1.text =itemView.context.getString(R.string.price_4_unit,withSuffixAmount(s.mrp))
                    binding.savepercent.visibility = View.GONE
                }else{
                    binding.price.text = withSuffixAmount((s.mrp.toDouble()*s.qty.toDouble()).toString())
                    binding.disprice.text = withSuffixAmount((s.price.toDouble()*s.qty.toDouble()).toString())
                    binding.disprice1.text =itemView.context.getString(R.string.price_4_unit,withSuffixAmount(s.price))
                    binding.savepercent.visibility = View.VISIBLE
                    binding.savepercent.text = ((s.mrp.toDouble()-s.price.toDouble())/s.mrp.toDouble()).toString()+" OFF"
                }


               Glide.with(itemView.context).load(s.image).into(binding.image)

                binding.less.setOnClickListener {
                    GlobalScope.launch {
                        getDatabase(itemView.context).appDao().getProductByBarcode(s.ean).let {

                            if (it.qty > 1) {
                                GlobalScope.launch {
                                    getDatabase(itemView.context).appDao().UpdateProductToCart((it.qty - 1), it.ean, ((it.qty - 1) * it.price.toDouble()))
                                }
                            }else{
                                GlobalScope.launch {
                                    getDatabase(itemView.context).appDao().DeleteProduct(it.ean)
                                }
                            }
                        }
                    }
                }
                binding.add.setOnClickListener {
                    GlobalScope.launch {
                        getDatabase(itemView.context).appDao().getProductByBarcode(s.ean).let {
                            if (it.qty<it.AvailQty.toInt()){
                                GlobalScope.launch {
                                    getDatabase(itemView.context).appDao().UpdateProductToCart((it.qty+1),it.ean,((it.qty+1) * it.price.toDouble()))
                                }
                            }else{
//                                this@TapfoCartAdapter.runOnUiThread(Runnable {
//                                    Toast.makeText(this,"Product Not Fond "+textData,
//                                        Toast.LENGTH_SHORT).show()
//                                })
//                                itemView.context.showShort("Only ${it.AvailQty} units available for this item ")
                            }

                        }
                    }
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(
            TapfocartLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}