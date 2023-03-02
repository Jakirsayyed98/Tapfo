package app.tapho.ui.home.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.viewpager.widget.PagerAdapter
import app.tapho.R
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.model.BannerList
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class BannerAdapter : PagerAdapter() {
    private val list = ArrayList<BannerList>()
    lateinit var context: Context


    fun addAllData(list: List<BannerList>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.row_home_offer, container, false)

        Glide.with(view).load(list[position].image).placeholder(R.drawable.loding_app_icon).apply(RequestOptions().transform(RoundedCorners(40))).into(view.findViewById(R.id.image))

        view.setOnClickListener {
            if (list[position].url!!.contains("https")){
                ActiveCashbackForWebActivity.openWebView(view.context,list[position].url,"","","1","","","","2","")
            }
            else
            {

            }

        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

}