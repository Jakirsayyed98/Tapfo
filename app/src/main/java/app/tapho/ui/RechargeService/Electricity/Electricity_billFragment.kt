package app.tapho.ui.RechargeService.Electricity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentElectricityBillBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.DTHRecharge.Adapter.DTHRechargeOpratorAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.Data
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.RechargeService.ModelData.RechargeServices.ServiceBanner
import app.tapho.ui.home.adapter.NewBannerDataAdapter1
import app.tapho.ui.home.adapter.SliderModelMain
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.ArrayList


class Electricity_billFragment : BaseFragment<FragmentElectricityBillBinding>() {
    var serviceTypeID = ""
    private var orporaterAdapter: DTHRechargeOpratorAdapter<Data>? = null
    private lateinit var itemList:List<Data>
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
        _binding = FragmentElectricityBillBinding.inflate(inflater,container,false)
        statusBarTextBlack()
        statusBarColor(R.color.black)
         getSharedPreference().saveString("Amount","")
        serviceTypeID= getSharedPreference().getString("servicetype").toString()
        setLayoutForOprator()
        SetOperatorData()
        getHomeData()

        initdata()

        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return _binding?.root
    }

    private fun setLayoutForOprator() {
        viewModel.getRechargeServiceOperator(getUserId(),serviceTypeID,this,object : ApiListener<ServiceOperatorRes,Any?>{
            override fun onSuccess(t: ServiceOperatorRes?, mess: String?) {
                t.let {
                    it!!.data.let {
                        itemList=it
                        orporaterAdapter!!.addAllItem(it)
                    }
                }
            }
        })
    }


    private fun getHomeData() {
        viewModel.getRechargeService(getUserId(),this,object : ApiListener<RechargeServiceRes,Any?>{
            override fun onSuccess(t: RechargeServiceRes?, mess: String?) {
                t!!.let {
                    it.data.forEach {
                        if (it.id.equals(getSharedPreference().getString("servicetype"))){
                            setBannerAuto(it.service_banner)
                        }
                    }
                }
            }

        })
    }

    private fun setBannerAuto(banners:List<ServiceBanner>) {

        if (banners.isNullOrEmpty()) {

            _binding!!.bannerlay.visibility = View.GONE
        } else {
            _binding!!.bannerlay.visibility = View.VISIBLE
        }
        val imageList = ArrayList<SliderModelMain>()

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(x.image, x.url, x.id,x.type, "", ""))
            }

        }

        _binding!!.banner.adapter = NewBannerDataAdapter1(imageList, _binding!!.banner, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        _binding!!.banner.clipToPadding = false
        _binding!!.banner.clipChildren = false
        _binding!!.banner.offscreenPageLimit = 3
        _binding!!.banner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.banner.setPadding(20, 0, 20, 0)

        //Handler Start
        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.banner.currentItem

                        if (i == imageList.size - 1) {
                            i = -1//0
                            _binding!!.banner.currentItem = i
                        }
                        i++
                        _binding!!.banner.setCurrentItem(i, true)
                        postDelayed(this, 2000)
                    }
                }
            }
            postDelayed(runnable, 2000)
        }

        TabLayoutMediator(_binding!!.tablayout,_binding!!.banner,false) { _,_ -> }.attach()
    }

    private fun initdata() {
        _binding!!.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterListData(p0.toString())
            }
        })


    }

    private fun filterListData(searchName: String) {

        val tempList: ArrayList<Data> = ArrayList()
        for (x in itemList){
            if (searchName.lowercase(Locale.getDefault()) in x.name.toString().lowercase(Locale.getDefault())){
                tempList.add(x)
            }
        }
        orporaterAdapter!!.updatelist(tempList)
    }


    private fun SetOperatorData() {

        orporaterAdapter = DTHRechargeOpratorAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data) {
                    if (data.image.isNullOrEmpty().not()){
                        getSharedPreference().saveString("operator_iconELECT",data.image)
                    }else{
                        getSharedPreference().saveString("operator_iconELECT", "")
                    }
                    getSharedPreference().saveString("operator_idELECT", data.id)
                    getSharedPreference().saveString("operator_nameELECT", data.name)
                    getSharedPreference().saveString("account_displayELECT", data.account_display)
                    getSharedPreference().saveString("amount_rangeELECT", data.amount_range)
                    getSharedPreference().saveString("bill_fetchELECT", data.bill_fetch)
                    getSharedPreference().saveString("discriptionELECT", "Enter 10 digit Customer Id Starting with 3. To Locate the Customer ID, press the MENU Button on your remote.")

                        ContainerActivity.openContainerForDTHRecharge(requireContext(), "Electricity_AccountNumber")

                }

            }

        })
        _binding!!.electricityOpretor.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
             reverseLayout = true
             stackFromEnd = true
            }
            adapter = orporaterAdapter
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            Electricity_billFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}