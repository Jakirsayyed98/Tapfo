package app.tapho.ui.localbizzUI.UserFlow

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentElectroBinding
import app.tapho.databinding.FragmentHomeScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.home.adapter.ItemTypeAdapter
import app.tapho.ui.localbizzUI.Adapter.BusinessCategoriesAdapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.BusinessCategories.BusinessCategory
import app.tapho.ui.localbizzUI.Model.BusinessCategories.Data
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.MiniApp
import app.tapho.utils.OPEN_WEB_VIEW
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class HomeScreenFragment  : BaseFragment<FragmentHomeScreenBinding>(), ApiListener<BusinessCategory,Any?>,RecyclerClickListener{

    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var appCategoryList: List<Data>? = null
    var businesscategoriesadapter : BusinessCategoriesAdapter<Data>?= null
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
        statusBarColor(R.color.light_purple_new)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        onClickItems()
        setAllMethodAndFunction()
        return _binding!!.root
    }

    private fun setAllMethodAndFunction() {
        setBusinessCategoriesData()
        getCurrentLocationData()

    }



    private fun setBusinessCategoriesData() {
        viewModel.getBusinesscategory(getUserId(),this,this)
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
                    latitude = location.latitude.toDouble()
                    longitude = location.longitude.toDouble()
                    Log.d("latitude",latitude.toString())
                    Log.d("latitude",longitude.toString())
                    getLocationNameData(latitude,longitude)

                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed on getting current location", Toast.LENGTH_SHORT).show()
                }
        }

        private fun getLocationNameData(latitude: Double, longitude: Double) {
            val myLocation = Geocoder(requireActivity().applicationContext, Locale.getDefault())
            val myList: List<Address> = myLocation.getFromLocation(latitude, longitude, 1)
            val address: Address = myList[0] as Address
            var addressStr = ""
            addressStr += address.getAddressLine(0).toString()

//            Log.d("Current Location 0", address.getAddressLine(0).toString())
//            Log.d("Current Location 0", address.adminArea)
//            Log.d("Current Location 0", address.subAdminArea)
//            Log.d("Current Location 0", address.subLocality)
//            Log.d("Current Location 0", address.locality)
//            Log.d("Current Location 0", address.featureName)
//            Log.d("Current Location 0", address.thoroughfare)

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
                        Toast.makeText(requireContext(), "You need to grant permission to access location",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



    private fun onClickItems() {
        _binding!!.floatingActionButton.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(),"AddBusiness")
        }
        _binding!!.backbutton.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.profile.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(),"BusinessProfile")
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
                appCategoryList =it
                setCategoriesLayout(it)
                setAppCategory(getString(R.string.more))
            }
        }
    }

    private fun setAppCategory(moretext: String) {
        businesscategoriesadapter!!.clear()
        appCategoryList.let {

            if (it!!.size > 16 && moretext == getString(R.string.more))
                businesscategoriesadapter?.addAllItem(
                    it.subList(0, 15).toList()
                )
             else
                businesscategoriesadapter!!.addAllItem(it)
                businesscategoriesadapter!!.addItem(Data("","","","",moretext,""))
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