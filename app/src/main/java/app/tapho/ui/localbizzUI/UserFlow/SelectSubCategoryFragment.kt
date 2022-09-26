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
import app.tapho.databinding.FragmentSelectSubCategoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Adapter.BusinessCategoriesAdapter
import app.tapho.ui.localbizzUI.Adapter.BusinessSubCategoriesAdapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.BusinessSubCategory
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.Data
import app.tapho.utils.DATA
import app.tapho.utils.TITLE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.dialog_tcashback_full.*
import java.util.*


class SelectSubCategoryFragment :BaseFragment<FragmentSelectSubCategoryBinding>(),RecyclerClickListener {
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var name : String =""

    var businesscategoriesadapter : BusinessSubCategoriesAdapter<Data>?= null
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
        _binding = FragmentSelectSubCategoryBinding.inflate(inflater,container,false)
        statusBarColor(R.color.light_purple_new)
        name = activity?.intent?.getStringExtra(TITLE).toString()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setLayoutAndMethod()
        _binding!!.searchEt.setHint(name)

        return _binding?.root
    }

    private fun setLayoutAndMethod() {
        HandelAllClickAction()
        callSubCategoriesViewmodel()
        SetSubCategoryLayout()
        getCurrentLocationData()
    }

    private fun HandelAllClickAction() {


        _binding!!.backbutton.setOnClickListener {
            activity?.onBackPressed()
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
                latitude = location.latitude
                longitude = location.longitude
                getLocationNameData(latitude,longitude)

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed on getting current location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getLocationNameData(latitude: Double, longitude: Double) {
        val myLocation = Geocoder(requireContext(), Locale.getDefault())
        val myList: List<Address> = myLocation.getFromLocation(latitude, longitude, 1)
        val address: Address = myList[0] as Address
        var addressStr = ""
        addressStr += address.getAddressLine(0).toString()

        Log.d("Current Location 0", address.getAddressLine(0).toString())


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

    private fun callSubCategoriesViewmodel() {
        viewModel.getBusinessSubCategory(getUserId(),activity?.intent?.getStringExtra("categoryID").toString(),this, object: ApiListener<BusinessSubCategory,Any?> {
            override fun onSuccess(t: BusinessSubCategory?, mess: String?) {
                t!!.data.let {
                    businesscategoriesadapter!!.addAllItem(it)
                }
            }

        })
    }
    private fun SetSubCategoryLayout() {
        businesscategoriesadapter = BusinessSubCategoriesAdapter(this)
        _binding!!.AllSubCategories.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.HORIZONTAL,false)
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = businesscategoriesadapter
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SelectSubCategoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is Data){
            LocalbizContainerActivity.openContainer(requireContext(),"ShowAllBusiness",data.business_category_id,data.id,false,data.name)
        }
    }
}