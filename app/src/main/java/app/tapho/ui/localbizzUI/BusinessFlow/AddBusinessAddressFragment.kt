package app.tapho.ui.localbizzUI.BusinessFlow

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessAddressBinding
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.LoaderFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class AddBusinessAddressFragment :BaseFragment<FragmentAddBusinessAddressBinding>(){

    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


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
        _binding = FragmentAddBusinessAddressBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        statusBarTextWhite()
        statusBarColor(R.color.white)
        getCurrentLocationData()
        _binding!!.btnVerify.isSelected = true
        _binding!!.getCuurentLocationData.setOnClickListener {
            showLoaderonLocation()
            getCurrentLocationData()
        }

        clickevents()
        return _binding?.root
    }



    private fun dismissLoaderonLocation() {
        kotlin.runCatching {
            LoaderFragment.dismissLoader(childFragmentManager)
        }
    }

    private fun showLoaderonLocation() {
        kotlin.runCatching {
            LoaderFragment.showLoader(childFragmentManager)
        }
      Timer().schedule(object  :  TimerTask(){
          override fun run() {
              dismissLoaderonLocation()
          }

      },500)
    }


    private fun clickevents() {
        var floor=""
        _binding!!.landmark.addTextChangedListener {
            if (_binding!!.landmark.text.toString().length>=1){
//                if (_binding!!.floor.text.toString().isNullOrEmpty()){
//                    _binding!!.radioComplete.isChecked = false
//                }else{
//                    _binding!!.radioComplete.isChecked = true
//                }

            }
        }

        if (getSharedPreference().getString("floor").isNullOrEmpty().not()){
            _binding!!.floor.setText(getSharedPreference().getString("floor"))
        }
        if (getSharedPreference().getString("landmark").isNullOrEmpty().not()){
            _binding!!.landmark.setText(getSharedPreference().getString("landmark"))
        }

        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.btnVerify.setOnClickListener {
            if (_binding!!.floor.text.toString().isNullOrEmpty()){
                this.requireView().showShortSnack("Please enter Shop Number")
            }else if (_binding!!.landmark.text.toString().isNullOrEmpty()){
                this.requireView().showShortSnack("Please enter Landmark")
            }else{
                getSharedPreference().saveString("floor",_binding!!.floor.text.toString())
                getSharedPreference().saveString("landmark",_binding!!.landmark.text.toString())
                getSharedPreference().saveString("City",_binding!!.city.text.toString())
                getSharedPreference().saveString("PinCode",_binding!!.pincode.text.toString())
                getSharedPreference().saveString("area",_binding!!.area.text.toString())
                LocalbizContainerActivity.openContainer(requireContext(),"BusinessTimingFragment")
            }


        }
        _binding!!.floor.addTextChangedListener {

            floor=it.toString()
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
                getSharedPreference().saveString("latitude",latitude.toString())
                getSharedPreference().saveString("longitude",longitude.toString())

                getLocationNameData(latitude,longitude)

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed on getting current location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getLocationNameData(latitude: Double, longitude: Double) {
        val myLocation = Geocoder(requireContext(), Locale.getDefault())
        val myList: List<Address> = myLocation.getFromLocation(latitude, longitude, 1) as List<Address>
        val address: Address = myList[0] as Address
        var addressStr = ""
        addressStr += address.getAddressLine(0).toString()


      //  val area = address.subThoroughfare+", "+address.thoroughfare+", " +address.subLocality
        val area = address.featureName+", " +address.subLocality

        _binding!!.currentLocation.text = addressStr
        _binding!!.city.setText(address.locality)
        _binding!!.pincode.setText(address.postalCode)
        _binding!!.area.setText(area)
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


    companion object {

        @JvmStatic
        fun newInstance() =
            AddBusinessAddressFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}