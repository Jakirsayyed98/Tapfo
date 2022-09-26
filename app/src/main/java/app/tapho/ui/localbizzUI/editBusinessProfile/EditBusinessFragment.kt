package app.tapho.ui.localbizzUI.editBusinessProfile

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import app.tapho.R
import app.tapho.databinding.FragmentEditBusinessBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.LoaderFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.SaveBusiness.Data
import app.tapho.ui.localbizzUI.Model.SaveBusiness.SaveBusinessRes
import app.tapho.ui.localbizzUI.Model.getBusinessDetails.getBusinessDetailRes
import app.tapho.utils.RealPathUtil
import app.tapho.utils.toast
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import java.text.SimpleDateFormat
import java.util.*


class EditBusinessFragment : BaseFragment<FragmentEditBusinessBinding>() {
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var shop_image = ""
    private var logo = ""


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
        _binding = FragmentEditBusinessBinding.inflate(inflater, container, false)
        statusBarTextWhite()
        statusBarColor(R.color.white)
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
        setAllImageData()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        Timeselect()
        getDataFromViewmodel()
        _binding!!.getCuurentLocationData.setOnClickListener {
            showLoaderonLocation()
            getCurrentLocationData()
        }
        _binding!!.businessBusinessName.setOnClickListener {
            OpenPopup(_binding!!.storeNameData)
        }
        _binding!!.businessDiscription.setOnClickListener {
            OpenPopup(_binding!!.businessDescription)
        }
        _binding!!.businessEmail.setOnClickListener {
            OpenPopup(_binding!!.addEmail)
        }
        _binding!!.BusinessWhatsApp.setOnClickListener {
            OpenPopup(_binding!!.businessWhatsappNumber)
        }
        _binding!!.businessWebsite.setOnClickListener {
            OpenPopup(_binding!!.businessWebsiteUrl)
        }

        _binding!!.businessPancardLay.setOnClickListener {
            OpenPopup(_binding!!.businessPancard)
        }
        _binding!!.businessGSTIN.setOnClickListener {
            OpenPopup(_binding!!.GSTNNumber)
        }
        setOnClickAndAllAction()
        return _binding?.root
    }

    private fun OpenPopup(textDataForSet: TextView) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.edit_business_popup_dialog, null)
        dialog.setCancelable(true)

        val save: AppCompatButton = view.findViewById(R.id.send_review)
        val discripion: TextView = view.findViewById(R.id.discripion)
        val EditedData: TextInputEditText = view.findViewById(R.id.editedData)

        discripion.text = textDataForSet.text.toString()

        save.setOnClickListener {
            dialog.dismiss()
            textDataForSet.text =EditedData.text.toString()
        }


        dialog.setContentView(view)
        dialog.show()

    }

    private fun setAllImageData() {
        _binding!!.iconProfile.setOnClickListener {
            PickImageDialog.build(PickSetup(),object : IPickResult{
                override fun onPickResult(r: PickResult?) {
                    if (r?.error == null) {
                        logo = RealPathUtil.getRealPath(context, r?.uri)
                        shoplogoImage(logo)
                    } else {
                        context?.toast(r.error.message)
                    }
                }
            }).show(childFragmentManager)
        }
        _binding!!.shopImage.setOnClickListener {
            PickImageDialog.build(PickSetup(), object : IPickResult{
                override fun onPickResult(r: PickResult?) {
                    if (r?.error == null) {
                        shop_image = RealPathUtil.getRealPath(context, r?.uri)
                        shopbannerImage(shop_image)
                    } else {
                        context?.toast(r.error.message)
                    }
                }
            }).show(childFragmentManager)
        }
    }

    private fun shoplogoImage(logo: String?) {
        Glide.with(requireContext()).load(logo).circleCrop().centerCrop().into(_binding!!.iconProfile)
        setOnClickAndAllAction()
    }

    private fun shopbannerImage(shopImage: String?) {
        Glide.with(requireContext()).load(shopImage).circleCrop().centerCrop().into(_binding!!.shopImage)
        setOnClickAndAllAction()
    }

    private fun setOnClickAndAllAction() {
        var lati= if (latitude.toString().isNullOrEmpty() )"" else if (latitude.toString().equals("0.0")) " " else latitude
        var longi= if (longitude.toString().isNullOrEmpty() )"" else if (longitude.toString().equals("0.0")) " " else longitude
        _binding!!.apply {

            btnVerify.isSelected = true
            btnVerify.setOnClickListener {
                Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
                viewModel.getSaveBusiness(getUserId(),
                    "1",
                    "",
                    "",
                    storeNameData.text.toString(),
                    businessPancard.text.toString(),
                    GSTNNumber.text.toString(),
                    shop_image,
                    logo,
                    businessDescription.text.toString(),
                    contactnumber.text.toString(),
                    addEmail.text.toString(),
                    businessWhatsappNumber.text.toString(),
                    establisedYear.text.toString(),
                    lati.toString(),
                    longi.toString(),
                    currentLocation.text.toString(),
                    floor.text.toString(),
                    area.text.toString(),
                    city.text.toString(),
                    pincode.text.toString(),
                    landmark.text.toString(),
                    "",
                    "",
                    Sundaystart.text.toString(),
                    Sundayend.text.toString(),
                    mondaystart.text.toString(),
                    mondayend.text.toString(),
                    tuesdaystart.text.toString(),
                    tuesdayend.text.toString(),
                    Wednesdaystart.text.toString(),
                    Wednesdayend.text.toString(),
                    Thursdaystart.text.toString(),
                    Thursdayend.text.toString(),
                    Fridaystart.text.toString(),
                    Fridayend.text.toString(),
                    Saturdaystart.text.toString(),
                    Saturdayend.text.toString(),
                    activity?.intent?.getStringExtra("business_id"),
                    getSharedPreference().getString("businesstagIds"),
                    this@EditBusinessFragment,
                    object : ApiListener<SaveBusinessRes, Any?> {
                        override fun onSuccess(t: SaveBusinessRes?, mess: String?) {
                            t.let {
                                if(it!!.errorCode.toString() =="0"){
                                    Toast.makeText(requireContext(),"Save SucessFully",Toast.LENGTH_SHORT).show()
                                    val snack = Snackbar.make(requireView(),"This is a simple Snackbar",Snackbar.LENGTH_LONG)
                                    snack.show()
                                    getSharedPreference().saveString("businesstags","")
                                    getSharedPreference().saveString("businesstagIds","")
                                    activity?.onBackPressed()
                                }
                            }
                        }

                    })
            }

        }
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
                setOnClickAndAllAction()
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


        var area = address.subThoroughfare+", "+address.thoroughfare+", " +address.subLocality

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

    private fun getDataFromViewmodel() {
        viewModel.getbusinessDetails(getUserId(), activity?.intent?.getStringExtra("business_id"),this,
            object : ApiListener<getBusinessDetailRes, Any?> {
                override fun onSuccess(t: getBusinessDetailRes?, mess: String?) {
                    t!!.data.let {
                        setAllDataForEdit(it)
                    }
                }
            })
    }

    fun Timeselect() {
        _binding!!.apply {
            mondaystart.setOnClickListener {
                setBusinessTime(mondaystart)

            }
            mondayend.setOnClickListener {
                setBusinessTime(mondayend)

            }

            tuesdaystart.setOnClickListener {
                setBusinessTime(tuesdaystart)

            }
            tuesdayend.setOnClickListener {
                setBusinessTime(tuesdayend)

            }

            Wednesdaystart.setOnClickListener {
                setBusinessTime(Wednesdaystart)

            }
            Wednesdayend.setOnClickListener {
                setBusinessTime(Wednesdayend)

            }


            Thursdaystart.setOnClickListener {
                setBusinessTime(Thursdaystart)

            }
            Thursdayend.setOnClickListener {
                setBusinessTime(Thursdayend)

            }

            Fridaystart.setOnClickListener {
                setBusinessTime(Fridaystart)

            }
            Fridayend.setOnClickListener {
                setBusinessTime(Fridayend)

            }

            Saturdaystart.setOnClickListener {
                setBusinessTime(Saturdaystart)

            }
            Saturdayend.setOnClickListener {
                setBusinessTime(Saturdayend)

            }

            Sundaystart.setOnClickListener {
                setBusinessTime(Sundaystart)

            }
            Sundayend.setOnClickListener {
                setBusinessTime(Sundayend)

            }
        }
    }

    private fun setBusinessTime(time: TextView) {
        var cal = Calendar.getInstance()
        val timesetListnear = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
            cal.set(Calendar.MINUTE,minute)
            time.text = SimpleDateFormat("hh.mm aa").format(cal.time)
            //     getSharedPreference().saveString(time.toString(),SimpleDateFormat("HH:mm a").format(cal.time))
        }
        TimePickerDialog(requireContext(),timesetListnear,cal.get(Calendar.HOUR_OF_DAY),cal.get(
            Calendar.MINUTE),false).show()

    }

    private fun setAllDataForEdit(it: List<app.tapho.ui.localbizzUI.Model.getBusinessDetails.Data>) {

        _binding!!.apply {
            it.get(0).let {
                Glide.with(requireContext()).load(it.image).centerCrop().into(shopImage)
                Glide.with(requireContext()).load(it.logo).centerCrop().into(iconProfile)
                storeNameData.text = it.business_name
                businessType.text = it.business_type_name
                categoryName.text = it.business_category_name
                service.text = it.business_subcategory_name
                businessPancard.text = it.pancard
                GSTNNumber.text = it.gst
                businessDescription.text = it.description
                contactnumber.text = it.contacts
                addEmail.text = it.email
                businessWebsiteUrl.text = ""
                businessWhatsappNumber.text = it.whatsapp_business
                establisedYear.text = it.established_year
                currentLocation.text = it.address
                floor.setText(it.floor)
                area.setText(it.area)
                city.setText(it.city)
                pincode.setText(it.pincode)
                landmark.setText(it.landmark)


                mondaystart.text = it.mon_opening
                mondayend.text = it.mon_closing

                tuesdaystart.text = it.tue_opening
                tuesdayend.text = it.tue_closing

                Wednesdaystart.text = it.wed_opening
                Wednesdayend.text = it.wed_closing

                Thursdaystart.text = it.thu_opening
                Thursdayend.text = it.thu_closing

                Fridaystart.text = it.fri_opening
                Fridayend.text = it.fri_closing

                Saturdaystart.text = it.sat_opening
                Saturdayend.text = it.sat_closing

                Sundaystart.text = it.sun_opening
                Sundayend.text = it.sun_closing

                var tag =""
                it.business_tag.forEach {
                  tag=tag +","+  it.name
                }

                getSharedPreference().saveString("category_id",it.business_category_id)
                _binding!!.tags.text =if (getSharedPreference().getString("businesstags").isNullOrEmpty()) tag.drop(1) else getSharedPreference().getString("businesstags").toString()
                _binding!!.businessTag.setOnClickListener {
                    LocalbizContainerActivity.openContainer(requireContext(), "businessTag")
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        setOnClickAndAllAction()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            EditBusinessFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}