package app.tapho.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerFragmentAdapter2(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val list = ArrayList<Fragment>()
    private val listTitle = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        list.add(fragment)
        listTitle.add(title)
        notifyDataSetChanged()
    }

    fun getAllFragments(): ArrayList<Fragment> {
        return list
    }

    fun getTitle(position: Int): String {
        return listTitle[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}