package app.tapho.ui.News.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class NewsTabAdapter (fragment:FragmentManager) : FragmentPagerAdapter(fragment) {
    private val list=ArrayList<Fragment>()
    private val listTitle=ArrayList<String>()

    fun addFragment(fragment: Fragment,title: String){
        list.add(fragment)
        listTitle.add(title)
        notifyDataSetChanged()
    }

    fun getAllFragments():ArrayList<Fragment>{
        return list
    }

    fun getTitle(position: Int):String{
        return listTitle[position]
    }

//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return list[position]
//    }

    override fun getCount(): Int {
        return list.size
    }


    override fun getItem(position: Int): Fragment {
        return list[position]
    }
}