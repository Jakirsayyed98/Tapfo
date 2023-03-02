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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.R
import app.tapho.databinding.FragmentListofAllBusinessesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Adapter.AllBusinessListAdapter
import app.tapho.ui.localbizzUI.Adapter.AllProfilesShowAdapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Data
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.SearchAllBusinessList
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.getBusinessListResForBusinessPerson
import app.tapho.ui.localbizzUI.UserFlow.CustomeData.CustomeModel
import app.tapho.ui.localbizzUI.UserFlow.CustomeData.businessCustomeAdapter
import app.tapho.utils.TITLE
import app.tapho.utils.isLocationEnabled
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class ListofAllBusinessesFragment : BaseFragment<FragmentListofAllBusinessesBinding>(),
    ConnectivityListener {
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var name : String =""
    var categoryID : String =""
    var SubcategoryID : String =""
    var AllBusinessListAdapter: AllBusinessListAdapter<Data>?=null

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
        _binding = FragmentListofAllBusinessesBinding.inflate(inflater,container,false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        statusBarColor(R.color.white)
        statusBarTextWhite()
        getSharedPreference().saveString("StartLoading","0")

        progressVISIBLE()
        checkConnection()

        categoryID = activity?.intent?.getStringExtra("categoryID").toString()
        SubcategoryID = activity?.intent?.getStringExtra("SubcategoryID").toString()
        name = activity?.intent?.getStringExtra(TITLE).toString()
        _binding!!.searchEt.setHint(name)
        return _binding?.root
    }

    override fun onResume() {
        super.onResume()

        setConnectivityListener(this)
    }

    private fun checkConnection() {
        val isConnected = ConnectionReceiver.isConnected(requireContext())

        if (isConnected) {
            AllMethodAndLayoutFunctions()
        } else{
            noInternetConnection()
        }

        _binding!!.retryinternetButton.setOnClickListener {
            checkConnection()
        }


    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            AllMethodAndLayoutFunctions()
        } else{
            noInternetConnection()
        }
    }


    private fun AllMethodAndLayoutFunctions() {


        if (requireContext().isLocationEnabled(requireContext())){

            progressVISIBLE()
            val handler = Handler(Looper.getMainLooper())
            val runnable = object : Runnable{
                override fun run() {
                    synchronized(this@ListofAllBusinessesFragment){
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

            //  setCutomeReyclerViewData()
            setrecyclerLayout()
            getCurrentLocationData()
            _binding!!.backbutton.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
        }else{

            Location()
        }


    }

    private fun setrecyclerLayout() {
        AllBusinessListAdapter = AllBusinessListAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data)
                    LocalbizContainerActivity.openContainer(requireContext(),"ShowSelectedBusinessData",data,false,data.business_name)
            }
        })

        _binding!!.AllListedbusinesspartnar.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = AllBusinessListAdapter
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

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude
                getListFromBusiness(latitude,longitude)
                getLocationNameData(latitude,longitude)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed on getting current location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getListFromBusiness(latitude: Double, longitude: Double) {
        getSharedPreference().saveString("StartLoading","0")
        viewModel.getBusinessListForUser(getUserId(),categoryID,SubcategoryID,"",latitude.toString(),longitude.toString(),"15","",this,object : ApiListener<SearchAllBusinessList,Any?>{
            override fun onSuccess(t: SearchAllBusinessList?, mess: String?) {
                t!!.let {
                    getSharedPreference().saveString("StartLoading","1")
                    showHome()
                    AllBusinessListAdapter!!.addAllItem(it.data)
                }
            }

        })
    }

    private fun getLocationNameData(latitude: Double, longitude: Double) {
        val myLocation = Geocoder(requireContext(), Locale.getDefault())
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
                    Toast.makeText(requireContext(), "You need to grant permission to access location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    fun progressVISIBLE() {
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.Location.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.VISIBLE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }
    fun lowConnection() {
        _binding!!.lowconnection.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.Location.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }
    fun Location() {

        _binding!!.Location.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }
    fun showHome() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.Location.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.VISIBLE

    }
    fun noInternetConnection() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.Location.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.noconnection.visibility = View.VISIBLE
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            ListofAllBusinessesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}