package app.tapho.ui.localbizzUI.BusinessProfileFlow

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.databinding.FragmentLocalbizOnBoardingBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.MiniCash.UI.MiniCashFragmentContainerActivity
import app.tapho.ui.intro.IntroNewAdapter
import app.tapho.ui.intro.sliderItem
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import com.google.android.material.tabs.TabLayoutMediator


class localbizOnBoardingFragment : BaseFragment<FragmentLocalbizOnBoardingBinding>() {

    val sliderHandler= Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocalbizOnBoardingBinding.inflate(inflater,container,false)
        statusBarTextBlack()
        statusBarColor(R.color.lightpurple4)
        val bannerdata: MutableList<sliderItem> = ArrayList()
        bannerdata.add(sliderItem(R.drawable.pinbizsc1))
        bannerdata.add(sliderItem(R.drawable.pinbizs2))
        bannerdata.add(sliderItem(R.drawable.pinbizs3))
        bannerdata.add(sliderItem(R.drawable.pinbizs4))
        Setbannerdata(bannerdata)
        _binding!!.btnContinue.setOnClickListener {
            getSharedPreference().saveString("localbizOnBoarding","1")
            LocalbizContainerActivity.openContainer(requireContext(),"HomePage")
            activity?.finish()
        }
        setPager()
        return _binding?.root
    }
    private fun setPager() {
        TabLayoutMediator(_binding!!.tabLayout, _binding!!.pager,false) { _,_ -> }.attach()
    }


    private fun Setbannerdata(bannerdata: MutableList<sliderItem>) {
        _binding!!.pager.adapter= IntroNewAdapter(bannerdata,binding.pager)

        _binding!!.pager.clipToPadding=false
        _binding!!.pager.clipChildren=false
        //      _binding!!.pager.offscreenPageLimit=3
        _binding!!.pager.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER


        _binding!!.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //   sliderHandler.postDelayed(sliderRunnable,1000)

                if (position<3){
                    sliderHandler.removeCallbacks(sliderRunnable)
                    _binding!!.btnContinue.visibility = View.GONE
                }else{
                    // sliderHandler.removeCallbacks(sliderRunnable)
                    _binding!!.btnContinue.visibility = View.VISIBLE
                }
            }
        })
    }

    private val sliderRunnable= Runnable {
        _binding!!.pager.currentItem=binding.pager.currentItem+1
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            localbizOnBoardingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}