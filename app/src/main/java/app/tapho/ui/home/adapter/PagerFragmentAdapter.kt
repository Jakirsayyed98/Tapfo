package app.tapho.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerFragmentAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
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

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
       return list[position]
    }
}