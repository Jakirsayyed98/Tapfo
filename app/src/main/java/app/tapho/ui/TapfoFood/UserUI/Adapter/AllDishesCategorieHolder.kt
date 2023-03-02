package app.tapho.ui.TapfoFood.UserUI.Adapter

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.AlldishescategoryLayoutBinding
import app.tapho.databinding.RowHeadlineBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ContainerActivity
import app.tapho.ui.model.Headline
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.bumptech.glide.Glide
import com.google.common.collect.Lists

class AllDishesCategorieHolder(
    val binding: AlldishescategoryLayoutBinding,
    private val clickListener: RecyclerClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    var Miniappsdata : ArrayList<MiniApp> = ArrayList()
    fun setData(headline: /*AppCategory*/Headline) {

        binding.title.text=headline.name

       val  mAdapter = OnlineAllDishesWithcartAdapter<MiniApp>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                clickListener.onRecyclerItemClick(pos,"",type)
            }
        })

        binding.AllDishes.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = mAdapter
        }
        headline.mini_app?.let {

            val miniappsarr:ArrayList<MiniApp> =ArrayList()
            it.forEach {
                if (it.tcash.equals("1")){
                    miniappsarr.add(it)
                }
            }

            mAdapter.addAllItem(it)


        }
    }



}