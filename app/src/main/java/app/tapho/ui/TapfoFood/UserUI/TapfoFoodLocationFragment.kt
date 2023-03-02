package app.tapho.ui.TapfoFood.UserUI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.R
import app.tapho.databinding.FragmentTapfoFoodLocationBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.intro.IntroActivity
import app.tapho.ui.login.VerifyOtpActivity
import app.tapho.utils.REACHED_HOME
import app.tapho.utils.isLocationEnabled
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class TapfoFoodLocationFragment : BaseFragment<FragmentTapfoFoodLocationBinding>(),ConnectivityListener {


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
        _binding = FragmentTapfoFoodLocationBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())




        return _binding?.root
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
                latitude = location.latitude.toDouble()
                longitude = location.longitude.toDouble()


                getLocationNameData(latitude,longitude)

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
        _binding!!.fullAddress.text = addressStr

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable{
            override fun run() {
                synchronized(requireContext()){
                    handler.postDelayed(object :Runnable{
                        override fun run() {
                            kotlin.runCatching {
                                TapfoFoodContainerActivity.openContainer(requireContext(),"TapfoFoodHomeFragment","")
                                activity?.finish()
                            }
                        }
                    },1000)

                }

            }
        }
        val thread = Thread(runnable)
        thread.start()
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
            TapfoFoodLocationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}