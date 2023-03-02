package app.tapho.ui.localbizzUI.editBusinessProfile

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.provider.CalendarContract.Colors
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
import app.tapho.utils.customToast
import app.tapho.utils.customToastForSupport
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
import kotlin.collections.ArrayList


class EditBusinessFragment : BaseFragment<FragmentEditBusinessBinding>() {
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var shop_image = ""
    private var logo = ""
    var data : ArrayList<app.tapho.ui.localbizzUI.Model.getBusinessDetails.Data>? = null


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
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.toolbar.tvTitle.text = "Edit Business Information"
        setSharedPrefToBlank()
        setAllImageData()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        Timeselect()
        getDataFromViewmodel()
        _binding!!.getCuurentLocationData.setOnClickListener {
            showLoaderonLocation()
            getCurrentLocationData()
        }
        _binding!!.businessBusinessName.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "AddbusinessName","1")
//            OpenPopup(_binding!!.storeNameData)
        }
        _binding!!.businessDiscription.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "businessDiscription","1")
//            OpenPopup(_binding!!.businessDescription)
        }
        _binding!!.businessEmail.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "businessEmail","1")
//            OpenPopup(_binding!!.addEmail)
        }
        _binding!!.BusinessWhatsApp.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "businessWhatsappNumber","1")
//            OpenPopup(_binding!!.businessWhatsappNumber)
        }
        _binding!!.businessWebsite.setOnClickListener {
//            OpenPopup(_binding!!.businessWebsiteUrl)
        }

        _binding!!.businessPancardLay.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "AddBusinessPancardFragment","1")
//            OpenPopup(_binding!!.businessPancard)
        }
        _binding!!.businessGSTIN.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "businessGSTINNumber","1")
//            OpenPopup(_binding!!.GSTNNumber)
        }
        _binding!!.businessPhoneNumber.setOnClickListener{
            LocalbizContainerActivity.openContainer(requireContext(),"businessPhoneNumber","1")
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

        EditedData.setHint(textDataForSet.toString())
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
        getSharedPreference().saveString("profile_logoEdit",logo!!)
        Glide.with(requireContext()).load(logo).circleCrop().centerCrop().into(_binding!!.iconProfile)
        setOnClickAndAllAction()
    }

    private fun shopbannerImage(shopImage: String?) {
        getSharedPreference().saveString("profile_imageEdit",shopImage!!)
        Glide.with(requireContext()).load(shopImage).circleCrop().centerCrop().into(_binding!!.shopImage)
        setOnClickAndAllAction()
    }

    private fun setOnClickAndAllAction() {
        val lati= if (latitude.toString().isNullOrEmpty() )"" else if (latitude.toString().equals("0.0")) " " else latitude
        val longi= if (longitude.toString().isNullOrEmpty() )"" else if (longitude.toString().equals("0.0")) " " else longitude
        _binding!!.apply {

            btnVerify.isSelected = true
            btnVerify.setOnClickListener {
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
                    this@EditBusinessFragment, object : ApiListener<SaveBusinessRes, Any?> {
                        override fun onSuccess(t: SaveBusinessRes?, mess: String?) {
                            t.let {
                                if (it!!.errorCode.toString() == "0") {
                                    requireContext().customToastForSupport(
                                        "Save Sucessfully",
                                        false
                                    )
                                    getSharedPreference().saveString("businesstags", "")
                                    getSharedPreference().saveString("businesstagIds", "")
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                } else {
                                    requireContext().customToast("Failed", false)
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
        val myList: List<Address> = myLocation.getFromLocation(latitude, longitude, 1) as List<Address>
        val address: Address = myList[0] as Address
        var addressStr = ""
        addressStr += address.getAddressLine(0).toString()


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

    private fun getDataFromViewmodel() {
        viewModel.getbusinessDetails(getUserId(), activity?.intent?.getStringExtra("business_id"),this,
            object : ApiListener<getBusinessDetailRes, Any?> {
                override fun onSuccess(t: getBusinessDetailRes?, mess: String?) {
                    t!!.data.let {
                        data = it
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
        val cal = Calendar.getInstance()
        val timesetListnear = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
            cal.set(Calendar.MINUTE,minute)
            time.text = SimpleDateFormat("hh.mm aa").format(cal.time)
            //     getSharedPreference().saveString(time.toString(),SimpleDateFormat("HH:mm a").format(cal.time))
        }
        val timedialog = TimePickerDialog(requireContext(),R.style.DialogTheme,timesetListnear,cal.get(Calendar.HOUR_OF_DAY),cal.get(
            Calendar.MINUTE),false)
        timedialog.show()

    }


    private fun setSharedPrefToBlank() {
        getSharedPreference().let {
            it.saveString("BusinessNameEDIT","")
            it.saveString("businessDescriptionEdit","")
            it.saveString("businessEmailEDIT","")
            it.saveString("business_whatsapp_numberEdit","")
            it.saveString("BusinessPANEdit","")
            it.saveString("BusinessGSTNNumberEdit","")
            it.saveString("secondary_numberEdit","")
            it.saveString("optional_numberEdit","")
            it.saveString("profile_imageEdit","")
            it.saveString("profile_logoEdit","")
        }
    }


    private fun setAllDataForEdit(it: List<app.tapho.ui.localbizzUI.Model.getBusinessDetails.Data>) {

        _binding!!.apply {
            it.get(0).let {
                if (getSharedPreference().getString("profile_imageEdit").isNullOrEmpty()){
                    Glide.with(requireContext()).load(it.image).centerCrop().into(shopImage)
                }else{
                    Glide.with(requireContext()).load(getSharedPreference().getString("profile_imageEdit").toString()).centerCrop().into(iconProfile)
                }


                if (getSharedPreference().getString("profile_logoEdit").isNullOrEmpty()){
                    Glide.with(requireContext()).load(it.logo).centerCrop().into(iconProfile)
                }else{
                    Glide.with(requireContext()).load(getSharedPreference().getString("profile_logoEdit").toString()).centerCrop().into(iconProfile)
                }


                storeNameData.text =if (getSharedPreference().getString("BusinessNameEDIT").isNullOrEmpty()) it.business_name else getSharedPreference().getString("BusinessNameEDIT").toString()
                businessType.text = it.business_type_name
                categoryName.text = it.business_category_name
                service.text = it.business_subcategory_name
                businessPancard.text = if (getSharedPreference().getString("BusinessPANEdit").isNullOrEmpty()) it.pancard else getSharedPreference().getString("BusinessPANEdit").toString()
                GSTNNumber.text = if (getSharedPreference().getString("BusinessGSTNNumberEdit").isNullOrEmpty()) it.gst else getSharedPreference().getString("BusinessGSTNNumberEdit").toString()
                businessDescription.text =if (getSharedPreference().getString("businessDescriptionEdit").isNullOrEmpty())  it.description else getSharedPreference().getString("businessDescriptionEdit").toString()
                contactnumber.text = if (getSharedPreference().getString("secondary_numberEdit").isNullOrEmpty())  it.contacts else getSharedPreference().getString("secondary_numberEdit").toString()+","+getSharedPreference().getString("optional_numberEdit").toString()
                addEmail.text = if (getSharedPreference().getString("businessEmailEDIT").isNullOrEmpty()) it.email else getSharedPreference().getString("businessEmailEDIT").toString()
                businessWebsiteUrl.text = ""
                businessWhatsappNumber.text = if (getSharedPreference().getString("business_whatsapp_numberEdit").isNullOrEmpty())  it.whatsapp_business else getSharedPreference().getString("business_whatsapp_numberEdit").toString()
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
              //      LocalbizContainerActivity.openContainer(requireContext(), "businessTag")
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        setOnClickAndAllAction()
        kotlin.runCatching {
            setAllDataForEdit(data!!)
        }

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