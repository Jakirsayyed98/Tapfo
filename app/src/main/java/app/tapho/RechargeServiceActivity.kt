package app.tapho

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.databinding.ActivityRechargeServiceBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.Adapter.ServiceCategoriesAdapter
import app.tapho.ui.RechargeService.ModelData.CustomeRechargeService
import app.tapho.ui.RechargeService.ModelData.RechargeServices.Data
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.RechargeService.ModelData.RechargeServices.ServiceBanner
import app.tapho.ui.home.adapter.NewBannerDataAdapter
import app.tapho.ui.home.adapter.SliderModelMain
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.HomeRes
import app.tapho.utils.DATA
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class RechargeServiceActivity : BaseActivity<ActivityRechargeServiceBinding>(){
    companion object {
        fun openRechargeService(
            context: Context?,

        ) { context?.startActivity(Intent(context, RechargeServiceActivity::class.java).apply {
        })
        }
    }
    var showmore ="LessVisible"

    var serviceAdapter : ServiceCategoriesAdapter<Data>?=null
    var Servicearray: ArrayList<Data> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRechargeServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.black)
        statusBarTextblack()

        getHomeScreen()
        rechargeServiceCall()
        binding.backbtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun getHomeScreen() {
        viewModel.getHomeData("Home",getUserId(),this,object : ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
             t!!.let {
                it.banner_list1!!.let {
                    setBannerAuto(it)
                }
             }
            }
        })
    }


    private fun rechargeServiceCall() {
        viewModel.getRechargeService(getUserId(),this,object : ApiListener<RechargeServiceRes,Any?>{
            override fun onSuccess(t: RechargeServiceRes?, mess: String?) {
                t.let {
                    setLayoutData(it!!.data)
                }
            }
        })
    }


    private fun setBannerAuto(banners: ArrayList<BannerList>) {

        if (banners.isNullOrEmpty()) {
            binding.banner1.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
        } else {
            binding.banner1.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
        }

        val imageList = ArrayList<SliderModelMain>()

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(x.image, x.url, x.id,x.type, "", ""))
            }

        }
        binding.banner1.adapter = NewBannerDataAdapter(imageList, binding!!.banner1, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        binding.banner1.clipToPadding = false
        binding.banner1.clipChildren = false
        binding.banner1.offscreenPageLimit = 3
        binding.banner1.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.banner1.setPadding(10, 0, 10, 0)

        //Handler Start
        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i =binding.banner1.currentItem

                        if (i == banners.size - 1) {
                            i = -1//0
                            binding.banner1.currentItem = i
                        }
                        i++
                        binding.banner1.setCurrentItem(i, true)
                        postDelayed(this, 4000)
                    }
                }
            }
            postDelayed(runnable, 4000)
        }


        TabLayoutMediator(binding.tabLayout, binding.banner1,false) { _,_ -> }.attach()
    }

    private fun setLayoutData(data: java.util.ArrayList<Data>) {
        serviceAdapter =  ServiceCategoriesAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data) {
                    getSharedPreference().saveString("servicetype", data.id)
                    when (type) {
                        "Prepaid-Mobile" -> { ContainerActivity.openContainer(this@RechargeServiceActivity, "mobile_prepaid", "")
                        }
                        "DTH" -> {
                            ContainerActivity.openContainer(this@RechargeServiceActivity, "DTH_Recharge", "")
                        }
                        "Electricity" -> {
                            ContainerActivity.openContainer(this@RechargeServiceActivity, "Electricity_bill", "")
                        }
                        else -> {
                            ContainerActivity.openContainer(this@RechargeServiceActivity, "BuyGiftCard", "")
                        }
                    }
                }
            }
        })
        binding.rechargeService1.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = serviceAdapter
        }

        serviceAdapter!!.addAllItem(data)
    }


    private fun setAllDataToEmpty() {
        getSharedPreference().saveString("Operator_Code","")
        getSharedPreference().saveString("Circle_Code","")
        getSharedPreference().saveString("number","")
        getSharedPreference().saveString("servicetype","")
        getSharedPreference().saveString("discription","")
        getSharedPreference().saveString("Amount","")
        getSharedPreference().saveString("operator_name","")
        getSharedPreference().saveString("circle_name","")
        getSharedPreference().saveString("operator_icon","")
        getSharedPreference().saveString("circle_id","")
        getSharedPreference().saveString("operator_id","")
        getSharedPreference().saveString("min_recharge","")
        getSharedPreference().saveString("user_commission","")
    }

    override fun onResume() {
        super.onResume()
      //  setAllDataToEmpty()
    }




}
