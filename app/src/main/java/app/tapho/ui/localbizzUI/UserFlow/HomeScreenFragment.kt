package app.tapho.ui.localbizzUI.UserFlow

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.R
import app.tapho.databinding.FragmentHomeScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.NewBannerDataAdapter1
import app.tapho.ui.home.adapter.SliderModelMain
import app.tapho.ui.localbizzUI.Adapter.BusinessCategoriesAdapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.BusinessCategories.BusinessCategory
import app.tapho.ui.localbizzUI.Model.BusinessCategories.Data
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.BannerList4
import app.tapho.ui.model.HomeRes
import app.tapho.utils.isLocationEnabled
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class HomeScreenFragment  : BaseFragment<FragmentHomeScreenBinding>(), ApiListener<BusinessCategory,Any?>,RecyclerClickListener,ConnectivityListener{

    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var appCategoryList: List<Data>? = null
    var businesscategoriesadapter : BusinessCategoriesAdapter<Data>?= null

    fun setConnectivityListener(listener: ConnectivityListener) {
        ConnectionReceiver.connectivityListener = listener
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        progressVISIBLE()
        checkConnection()


//        _binding!!.retryLocation.setOnClickListener {
//            checkConnection()
//        }

        return _binding!!.root
    }

    override fun onResume() {
        super.onResume()
//        checkConnection()
        setConnectivityListener(this)
    }

    private fun checkConnection() {
        val isConnected = ConnectionReceiver.isConnected(requireContext())

        if (isConnected) {
            setAllMethodAndFunction()
        } else{
            lowConnection()
        }

        _binding!!.retry.setOnClickListener {
            checkConnection()
        }


    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            setAllMethodAndFunction()
        } else{
            lowConnection()
        }
    }

    fun progressVISIBLE() {
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.Location.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.VISIBLE
        _binding!!.lowconnection.visibility = View.GONE
    }
    fun lowConnection() {
        _binding!!.lowconnection.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.Location.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE

    }
    fun Location() {
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.Location.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE

    }
    fun showHome() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.Location.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.VISIBLE

    }


    private fun setAllMethodAndFunction() {
        if (requireContext().isLocationEnabled(requireContext())){
            progressVISIBLE()
            val handler = Handler(Looper.getMainLooper())
            val runnable = object : Runnable{
                override fun run() {
                    synchronized(this@HomeScreenFragment){
                        handler.postDelayed(object :Runnable{
                            override fun run() {
                                kotlin.runCatching {
                                    if (getSharedPreference().getString("StartLoading").equals("0")) {
                                        lowConnection()
                                    } else {
                                        handler.removeCallbacksAndMessages(null)
                                    }
                                }
                            }
                        },10000)
                    }
                }
            }
            val thread = Thread(runnable)
            thread.start()
            onClickItems()
            setBusinessCategoriesData()
            getCurrentLocationData()
        }else{
            Location()
        }

    }

    private fun setBusinessCategoriesData() {
        getSharedPreference().saveString("StartLoading","0")
        viewModel.getBusinesscategory(getUserId(),this,this)
        viewModel.getHomeData("home",getUserId(),this,object : ApiListener<HomeRes,Any?>{
            override fun onSuccess(t: HomeRes?, mess: String?) {
             t.let {
                 it!!.banner_list4.let {
                     setBannerAuto(it!!)
                 }
             }
            }
        })
    }

    private fun setBannerAuto(banners: ArrayList<BannerList4>) {

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

        _binding!!.banner.adapter =
            NewBannerDataAdapter1(imageList, _binding!!.banner, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                }
            })
        _binding!!.banner.clipToPadding = false
        _binding!!.banner.clipChildren = false
        _binding!!.banner.offscreenPageLimit = 3
        _binding!!.banner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        _binding!!.banner.setPadding(10, 0, 10, 0)

        //Handler Start


        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = _binding!!.banner.currentItem

                        if (i == banners.size - 1) {
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


        TabLayoutMediator(_binding!!.bannertab,_binding!!.banner,false) { _,_ -> }.attach()
    }

    private fun getCurrentLocationData() {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // request permission
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE);

                return
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // getting the last known or current location
                    kotlin.runCatching {
                        latitude = location.latitude.toDouble()
                        longitude = location.longitude.toDouble()

                        getLocationNameData(latitude,longitude)
                    }

                }
                .addOnFailureListener {
                }
        }

    private fun getLocationNameData(latitude: Double, longitude: Double) {
            val myLocation = Geocoder(requireActivity().applicationContext, Locale.getDefault())
            val myList: List<Address> = myLocation.getFromLocation(latitude, longitude, 1) as List<Address>
            val address: Address = myList[0] as Address
            var addressStr = ""
            addressStr += address.getAddressLine(0).toString()


            _binding!!.fulladdress.text = addressStr
            _binding!!.locationname.text =address.subLocality //addressStr
            getSharedPreference().saveString("BusinessCurrentLocation",addressStr)
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            when (requestCode) {
                LOCATION_PERMISSION_REQ_CODE -> {
                    if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission granted
                    } else {
                        // permission denied
                        Location()

                    }
                }
            }
        }

    private fun onClickItems() {
        _binding!!.floatingActionButton.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(),"AddBusiness")
        }
        _binding!!.backbutton.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.profile.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(),"PinBizProfileFragment")
        }
        _binding!!.notification.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(),"BusinessNotification")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: BusinessCategory?, mess: String?) {
        t!!.let {
            it.data.let {
                getSharedPreference().saveString("StartLoading","1")
                showHome()
                appCategoryList =it
                setCategoriesLayout(it)
                setAppCategory(getString(R.string.more))
            }
        }
    }

    private fun setAppCategory(moretext: String) {
        businesscategoriesadapter!!.clear()
        appCategoryList.let {

//            if (it!!.size > 16 && moretext == getString(R.string.more))
//                businesscategoriesadapter?.addAllItem(
//                    it.subList(0, 15).toList()
//                )
//             else
                businesscategoriesadapter!!.addAllItem(it!!)
//                businesscategoriesadapter!!.addItem(Data("","","","",moretext,""))
        }

    }

    private fun setCategoriesLayout(it: List<Data>) {
        businesscategoriesadapter = BusinessCategoriesAdapter(this)
     //   businesscategoriesadapter!!.setMoreImagePos(8)
        binding.AllCategories.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = businesscategoriesadapter
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is Data) {
            when (data.name) {
                getString(R.string.more) -> {
                    setAppCategory(getString(R.string.less))
                }
                getString(R.string.less) -> {
                    setAppCategory(getString(R.string.more))
                }
                else ->
                openSubCategory(data)

            }
        }
    }

    private fun openSubCategory(data: Data) {
        LocalbizContainerActivity.openContainer(requireContext(),"SubCategories",data.id.toString(),"",false,data.name)
    }

}