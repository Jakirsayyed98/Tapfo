package app.tapho

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import app.tapho.NavSheet.Fragment_club
import app.tapho.NavSheet.Fragment_favorite_nav
import app.tapho.NavSheet.Fragment_forYou
import app.tapho.ui.home.HomeFragment

internal class ViewpagerAdapter(var context: HomeFragment, fragment:FragmentManager, var totalTab:Int):FragmentPagerAdapter(fragment) {
    override fun getCount(): Int {
            return totalTab
    }

    override fun getItem(position: Int): Fragment {
        return  when(position){
            0-> {
                Fragment_favorite_nav()
            }
            1-> {
                Fragment_forYou()
            }
            2->{
                Fragment_club()
            }
            else ->
                getItem(position)
        }

    }

}