package app.tapho.ui.TapfoFood.UserUI

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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.R
import app.tapho.databinding.FragmentTapfoFoodHomeBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.ui.BaseFragment
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.TapfoFood.UserUI.Adapter.AllRestorantsAdapter
import app.tapho.ui.TapfoFood.UserUI.Adapter.FoodCategoriesAdapter
import app.tapho.ui.TapfoFood.UserUI.Adapter.FoodSuperAdapter
import app.tapho.ui.TapfoFood.UserUI.Adapter.NearbyRestorantsAdapter
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel1
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import app.tapho.utils.getGreetingForRestro
import app.tapho.utils.isLocationEnabled
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*


class TapfoFoodHomeFragment : BaseFragment<FragmentTapfoFoodHomeBinding>(), ConnectivityListener {


    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


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
        // Inflate the layout for this fragment
        _binding = FragmentTapfoFoodHomeBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        _binding!!.backbutton.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        val name  = getSharedPreference().getLoginData()!!.name!!.replaceAfter(" ","")
        _binding!!.namewish.text =getString(R.string.hi_name_what_s_your_pick,name, getGreetingForRestro())

        setFoodSuperCategoryData()
        setFoodCategoryData()
        setNearByRestorentsData()
        setAllRestorentsData()
        _binding!!.getLocation.setOnClickListener { 
            openBottomSheetForLocation()
        }
        return _binding?.root
    }

    private fun openBottomSheetForLocation() {
        val sheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.gameexitsheet,null)
        sheet.setCancelable(true)


        sheet.setContentView(view)
        sheet.show()
    }

    private fun setFoodSuperCategoryData() {
        val FoodSuperCategory = FoodSuperAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        }).apply {

            for (i in 1..3){
                addItem(FoodCustomeSuperCategoryModel1(R.drawable.supersaver_food,"","1",""))
                addItem(FoodCustomeSuperCategoryModel1(R.drawable.spotlight_food,"","1",""))
                addItem(FoodCustomeSuperCategoryModel1(R.drawable.free_delivery,"","1",""))
                addItem(FoodCustomeSuperCategoryModel1(R.drawable.special_food,"","1",""))
                addItem(FoodCustomeSuperCategoryModel1(R.drawable.delicious_food,"","1",""))
            }

        }
        _binding!!.foodSuperCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = FoodSuperCategory
        }
    }

    private fun setFoodCategoryData() {
        val FoodCategory = FoodCategoriesAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        }).apply {
            val image1="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8n6AyM78IGl8-uEY-WLK_uoydcr1h6vPnZw&usqp=CAU"
            val image2="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcCNSeZvhe0IINQH4G_2CFsEUqZpsd1-JT9A&usqp=CAU"
            val image3="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZRPded47UhfS8cs6nK7GemD_1OmysXuviu9gYpfkLIN268MYW92QrYH5qruB1Z7U_hws&usqp=CAU"
            val image4="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTO4aHXThsmIJeuSIHlbQ6p39D6sBxbLl7AZeosJHu_R8zgjmGX7RcrQdXJz7yXglU0mdw&usqp=CAU"
            val image5="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_KVf2L1a6hYtWmOi36DHJb5HyXj7SQLzLSw&usqp=CAU"
            val image6="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgmpjIPmhaNPz4xrpwMiZQLWmf3FNYDMUL8g&usqp=CAU"
            val image7="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjwgJ60SExrC8W58b43Iw6yqLA9wmqWQM2bA&usqp=CAU"
            val image8="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtqmIppxAB3ezgPZuRPL-287jxSXb54ts5LA&usqp=CAU"


            addItem(FoodCustomeSuperCategoryModel(image1,"North Indian","1",""))
            addItem(FoodCustomeSuperCategoryModel(image2,"South Indian","1",""))
            addItem(FoodCustomeSuperCategoryModel(image3,"Chinese","1",""))
            addItem(FoodCustomeSuperCategoryModel(image4,"Pure Veg","1",""))
            addItem(FoodCustomeSuperCategoryModel(image5,"Biryani","1",""))
            addItem(FoodCustomeSuperCategoryModel(image6,"Rolls","1",""))
            addItem(FoodCustomeSuperCategoryModel(image7,"Cakes","1",""))
            addItem(FoodCustomeSuperCategoryModel(image8,"Paratha","1",""))

        }
        _binding!!.FoodCategories.apply {
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = FoodCategory
        }
    }

    private fun setNearByRestorentsData() {
        val nearbyretorants = NearbyRestorantsAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                TapfoFoodContainerActivity.openContainer(requireContext(),"RestaurantDetailFragment",data)
            }
        }).apply {
            val image1="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQV57fXq0GwLeKMTS_zBnYKSleLC-P09m2vVg&usqp=CAU"
            val image2="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjWONOxh9-J2DlvnwBfeqisB-F859XmHDT9Q&usqp=CAU"
            val image3="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZHpCkvT1JZ_McDf2BEGUIHNo0uSgBBs1-fw&usqp=CAU"
            val image4="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwuecEAkWwdzP_XN2ug8sky3JZQ2gFbI0KVA&usqp=CAU"



            addItem(FoodCustomeSuperCategoryModel(image1,"Monster Pizza","1",""))
            addItem(FoodCustomeSuperCategoryModel(image2,"Cafe Coffe Day","1",""))
            addItem(FoodCustomeSuperCategoryModel(image3,"Capitol Cafe","1",""))
            addItem(FoodCustomeSuperCategoryModel(image4,"Cafe Tapfo","1",""))


        }
        _binding!!.NearbyRestorents.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = nearbyretorants
        }
    }

    private fun setAllRestorentsData() {
        val allRestorantsAdapter = AllRestorantsAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                TapfoFoodContainerActivity.openContainer(requireContext(),"RestaurantDetailFragment",data)

            }
        }).apply {
            val image1="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxHD5S5z5zGhjDKj-K95kmBpOUNFtpAVrp-4Yqd77v&s"
            val image2="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQF0LjrsgkVi79EIFUmXABwDTKKS0go6rn-S1xDIU7Tmo_UecYTu81-v6nfNHFrwbZ0YcA&usqp=CAU"
            val image3="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT7s_BYGWp21LC_mYwiW1FVCAKcqcPV-Rao73lj2izFwAuw80XzAOAPBTi2V7H-qnJgtbU&usqp=CAU"
            val image4="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFTNC8E5E4yRbTQR1zm4pg9O33pzY8vXnmNg&usqp=CAU"
            val image5="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSB89_2T9PqX7PtkWLQZrqpGewHG4_17CGIw&usqp=CAU"
            val image6="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfQBg7GUVnhL2nIhvoWAj0JfhUYt4IWkGKVA&usqp=CAU"
            val image7="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfQBg7GUVnhL2nIhvoWAj0JfhUYt4IWkGKVA&usqp=CAU"
            val image8="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyNdFjj87XDvCG7Dng-lwKiFHmZWoZWwd-YQ&usqp=CAU"


            Thread(object : Runnable{
                override fun run() {
                    for ( i in 1..10){
                        addItem(FoodCustomeSuperCategoryModel(image1,"Monster Pizza","1",""))
                        addItem(FoodCustomeSuperCategoryModel(image2,"Cafe Coffe Day","1",""))
                        addItem(FoodCustomeSuperCategoryModel(image3,"Capitol Cafe","1",""))
                        addItem(FoodCustomeSuperCategoryModel(image4,"Cafe Tapfo","1",""))
                        addItem(FoodCustomeSuperCategoryModel(image5,"Marol Cafe","1",""))
                        addItem(FoodCustomeSuperCategoryModel(image6,"Cafe safari","1",""))
                        addItem(FoodCustomeSuperCategoryModel(image7,"Tibbat Tea","1",""))
                        addItem(FoodCustomeSuperCategoryModel(image8,"Ustad da Dhaba","1",""))
                    }
                }
            }).start()






        }
        _binding!!.AllRestorents.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = allRestorantsAdapter
        }
    }


    override fun onResume() {
        super.onResume()
        checkConnection()
        setConnectivityListener(this)
    }

    private fun checkConnection() {
        val isConnected = ConnectionReceiver.isConnected(requireContext())

        if (isConnected) {
            setAllMethodAndFunction()
        } else{
//            lowConnection()
        }


    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            setAllMethodAndFunction()
        } else{
//            lowConnection()
        }
    }

    private fun setAllMethodAndFunction() {
        if (requireContext().isLocationEnabled(requireContext())){
            getCurrentLocationData()
        }else{

        }
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
                if (latitude.toString().isNullOrEmpty().not()){
                    latitude = location.latitude.toDouble()
                    longitude = location.longitude.toDouble()
                    getLocationNameData(latitude,longitude)
                }else{
                    requireContext().showShort("we are not able to open right now")
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed on getting current location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getLocationNameData(latitude: Double, longitude: Double) {
        val myLocation = Geocoder(requireActivity().applicationContext, Locale.getDefault())
        val myList: List<Address> = myLocation.getFromLocation(latitude, longitude, 1) as List<Address>
        val address: Address = myList[0] as Address
        var addressStr = ""
        addressStr += address.getAddressLine(0).toString()
        val area = address.featureName// +", " +address.subLocality
        _binding!!.mainlocation.text = area
//        _binding!!.fullAddress.text = addressStr

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocationData()
                } else {
                    Toast.makeText(requireContext(), "You need to grant permission to access location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TapfoFoodHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}