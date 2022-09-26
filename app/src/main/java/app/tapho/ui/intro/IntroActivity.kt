package app.tapho.ui.intro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.databinding.ActivityIntroBinding
import app.tapho.lightstatusBar
import app.tapho.ui.BaseActivity
import app.tapho.ui.login.LoginActivity
import app.tapho.ui.merchants.TCashWorkFragment
import app.tapho.ui.model.BannerList
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    val sliderHandler=Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        theme?.applyStyle(R.style.TextViewBold1, true)
        statusBarColor(R.color.black)
        val bannerdata: MutableList<sliderItem> = ArrayList()

        bannerdata.add(sliderItem(R.drawable.screen_1))
        bannerdata.add(sliderItem(R.drawable.screen_2))
        bannerdata.add(sliderItem(R.drawable.screen_3))
        bannerdata.add(sliderItem(R.drawable.screen_4))
        bannerdata.add(sliderItem(R.drawable.screen_5))
        bannerdata.add(sliderItem(R.drawable.screen_6))


        Setbannerdata(bannerdata)
        binding.btnContinue.setOnClickListener { startLogin() }
        setPager()
    }

    private fun Setbannerdata(bannerdata: MutableList<sliderItem>) {
        binding.pager.adapter=IntroNewAdapter(bannerdata,binding.pager)

        binding.pager.clipToPadding=false
        binding.pager.clipChildren=false

        binding.pager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position<5){
                    sliderHandler.removeCallbacks(sliderRunnable)
                    binding.btnContinue.visibility = View.GONE
                }else{
                    binding.btnContinue.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun setPager() {
        TabLayoutMediator(binding.tabLayout, binding.pager,false) { _,_ -> }.attach()
    }

    private fun startLogin() {
        startActivity(Intent(this, LoginActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
            })
        finish()
    }
    private val sliderRunnable= Runnable {
        binding.pager.currentItem=binding.pager.currentItem+1
    }

}