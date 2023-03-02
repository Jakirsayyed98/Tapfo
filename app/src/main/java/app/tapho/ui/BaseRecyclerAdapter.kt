package app.tapho.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import app.tapho.utils.AppSharedPreference

abstract class BaseRecyclerAdapter<T, I : RecyclerView.ViewHolder> : RecyclerView.Adapter<I>() {
    var list = ArrayList<T>()

    open fun addAllItem(list: List<T>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    infix fun addItem(t: T) {
        this.list.add(t)
        notifyDataSetChanged()
    }

    open fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

     fun Context.getSharedPref(): AppSharedPreference {
        return AppSharedPreference.getInstance(this)
    }

}