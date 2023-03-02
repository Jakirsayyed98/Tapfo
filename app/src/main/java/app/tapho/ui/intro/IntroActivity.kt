package app.tapho.ui.intro


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.ActivityIntroBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.home.adapter.IntroBannerAdapter
import app.tapho.ui.home.adapter.SliderModelIntro
import app.tapho.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator


class IntroActivity : BaseActivity<ActivityIntroBinding>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        theme?.applyStyle(R.style.TextViewBold1, true)
        statusBarColor(R.color.black)
        val bannerdata: MutableList<sliderItem> = ArrayList()


        bannerdata.add(sliderItem(R.drawable.firstintro1))
        bannerdata.add(sliderItem(R.drawable.firstintro2))
        bannerdata.add(sliderItem(R.drawable.firstintro3))
        setBanner(bannerdata)

        binding.btnContinue.setOnClickListener { startLogin() }
    }





    private fun setBanner(banners: MutableList<sliderItem>) {
        if (banners.isNullOrEmpty()) {
            binding.banner1.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
        } else {
            binding.banner1.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
        }

        val imageList = java.util.ArrayList<SliderModelIntro>()

        for (x in banners) {
                imageList.add(SliderModelIntro(x.image))
        }
        binding.banner1.adapter = IntroBannerAdapter(imageList, binding.banner1, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        binding.banner1.clipToPadding = false
        binding.banner1.clipChildren = false
        binding.banner1.offscreenPageLimit = 3
        binding.banner1.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.banner1.setPadding(10, 0, 10, 0)

        if (binding.banner1.currentItem == 2){

            binding.btnContinue.isSelected = true
            binding.btnContinue.isClickable = true
        }

        TabLayoutMediator(binding.tabLayout, binding.banner1,false) { _,_ -> }.attach()
    }


    private fun startLogin() {
        startActivity(Intent(this, LoginActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
            })
        finish()
    }


}