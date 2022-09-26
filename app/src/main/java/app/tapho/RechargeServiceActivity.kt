package app.tapho

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.databinding.ActivityRechargeServiceBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.Adapter.ServiceCategoriesAdapter
import app.tapho.ui.RechargeService.MobileRechcrge.adapter.RechargeServiceCategoriesAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeServices.Data
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.home.adapter.CustomeShopCategoryModel

class RechargeServiceActivity : BaseActivity<ActivityRechargeServiceBinding>(){
    companion object {
        fun openRechargeService(
            context: Context?, ) { context?.startActivity(Intent(context, RechargeServiceActivity::class.java).apply {})
        }
    }
    var showmore ="LessVisible"
    var serviceAdapter : ServiceCategoriesAdapter<Data>?=null
    var Servicearray: ArrayList<Data> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRechargeServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    //    tabClicked1(binding.mobile)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        RechargeServiceListModel()
        setLayout()
        binding.showMoreBtn.setOnClickListener {
            RechargeServiceListModels(showmore)
        }
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

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
    }

    override fun onResume() {
        super.onResume()
        setAllDataToEmpty()
    }

    private fun setLayout() {
        serviceAdapter = ServiceCategoriesAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data){
                    getSharedPreference().saveString("servicetype",data.id)
                    when (type){
                        "Prepaid-Mobile" -> {
                            ContainerActivity.openContainer(this@RechargeServiceActivity, "mobile_prepaid", "")
                        }
                        "DTH" -> {
                            ContainerActivity.openContainer(this@RechargeServiceActivity, "DTH_Recharge", "")
                        }
                        "Electricity" -> {
                            ContainerActivity.openContainer(this@RechargeServiceActivity, "Electricity_bill", "")
                        }
                        else ->{
                            ContainerActivity.openContainer(this@RechargeServiceActivity, "BuyGiftCard", "")
                        }
                    }
                }
            }

        })
        binding.rechargeService.apply {
            layoutManager = GridLayoutManager(this@RechargeServiceActivity,3)
            adapter = serviceAdapter
        }
    }


    private fun RechargeServiceListModel() {
        viewModel.getRechargeService(getUserId(),this,object : ApiListener<RechargeServiceRes,Any?>{
            override fun onSuccess(t: RechargeServiceRes?, mess: String?) {
                t.let {
                    Servicearray = it!!.data
                    if (it.data.size > 6){
                        binding.card.visibility = View.VISIBLE
                    }else{
                        binding.card.visibility = View.GONE
                    }
                    RechargeServiceListModels(showmore)
                }
            }

        })
    }

    private fun RechargeServiceListModels(Showtype: String) {
        serviceAdapter!!.clear()

        Servicearray.let {

            if (Showtype == "LessVisible") {
                serviceAdapter!!.clear()
                serviceAdapter!!.addAllItem(if (it.size>=6) it.subList(0,6) else it)
                showmore ="MoreVisible"
                binding.showMoreBtn.text = "Tap To view more"



            }else{
                serviceAdapter!!.clear()
                serviceAdapter!!.addAllItem(it)
                showmore ="LessVisible"
                binding.showMoreBtn.text = "Tap To view less"
            }

        }


    }

/*
    private fun RechargeServiceCategory() {
        val customShopCategory = RechargeServiceCategoriesAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {

                    "mobile_prepaid" -> {
                        ContainerActivity.openContainer(this@RechargeServiceActivity, "mobile_prepaid", "")
                    }
                    "DTH_Recharge" -> {
                        ContainerActivity.openContainer(this@RechargeServiceActivity, "DTH_Recharge", "")
                    }
                    "Fastag_Recharge" -> {
                    ContainerActivity.openContainer(this@RechargeServiceActivity, "BuyGiftCard", "")
                    }
                    "Cabel TV" -> {
                        ContainerActivity.openContainer(this@RechargeServiceActivity, "BuyGiftCard", "")
                    }

                }
            }

        }).apply {


            addItem(CustomeShopCategoryModel(R.drawable.mobile_prepaid, "Mobile\nPrepaid", "mobile_prepaid"))
            addItem(CustomeShopCategoryModel(R.drawable.dth_recharge, "DTH\nRecharge", "DTH_Recharge"))
            addItem(CustomeShopCategoryModel(R.drawable.dth_recharge, "Fastag_Recharge", "Fastag_Recharge"))
            addItem(CustomeShopCategoryModel(R.drawable.dth_recharge, "Cabel TV", "Cabel TV"))


        }
        binding!!.rechargeServices.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = customShopCategory
        }
    }
*/
}