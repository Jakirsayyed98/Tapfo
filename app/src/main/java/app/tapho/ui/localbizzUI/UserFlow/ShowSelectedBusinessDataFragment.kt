package app.tapho.ui.localbizzUI.UserFlow

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.ContactOnWhatsapp
import app.tapho.R
import app.tapho.databinding.FragmentShowSelectedBusinessDataBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Adapter.CalltoContactAdapter
import app.tapho.ui.localbizzUI.Adapter.ProfilesRatingAndReviewsShowAdapter
import app.tapho.ui.localbizzUI.Adapter.business_services_adapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.AddRating.AddBusinessRatingRes
import app.tapho.ui.localbizzUI.Model.ContactModel
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.BusinessTag
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Data
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Rating
import app.tapho.utils.DATA
import app.tapho.utils.parseDate3
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ShowSelectedBusinessDataFragment : BaseFragment<FragmentShowSelectedBusinessDataBinding>(){
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var Mylatitude: Double = 0.0
    private var Mylongitude: Double = 0.0
    private var MyAltitude: Double = 0.0
    var businessSevicesAdapter : business_services_adapter<BusinessTag>?= null
    var RatingAndReviewsAdapter : ProfilesRatingAndReviewsShowAdapter<Rating>? = null
    var WhatsappNumber = ""
    var calltoContactAdapter : CalltoContactAdapter<ContactModel>?=null
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
        _binding = FragmentShowSelectedBusinessDataBinding.inflate(inflater,container,false)
        statusBarColor(R.color.light_purple_new)
        statusBarTextBlack()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        setAllMethodaAndFinctions()


        getCurrentLocationData()
        return _binding?.root
    }


    private fun setAllMethodaAndFinctions() {
        superlinksSubCategories()

        _binding!!.backbutton.setOnClickListener{
           activity?.onBackPressed()
        }
        _binding!!.chat.setOnClickListener{
            chatwithwhatsapp()
        }
        _binding!!.chat1.setOnClickListener{
            chatwithwhatsapp()
        }
        _binding!!.whatsapp.setOnClickListener {
            chatwithwhatsapp()
        }


        activity?.intent?.getStringExtra(DATA).let {
            val data = Gson().fromJson(it, Data::class.java)
            Glide.with(requireContext()).load(data.image).centerCrop().into(_binding!!.banner)
            Glide.with(requireContext()).load(data.logo).centerCrop().into(_binding!!.icon)
            getDirectionClickMethod(data.latitude,data.longitude)

            _binding!!.call.setOnClickListener {
                openBottomSheetForCall(data.contacts)
            }

            _binding!!.searchEt.setHint(data.business_name)
            _binding!!.name.text = if (data.business_name.isNullOrEmpty()) "Not Available" else data.business_name
            _binding!!.name1.text = if (data.business_name.isNullOrEmpty()) "Not Available" else data.business_name
            _binding!!.shortAddress.text =if (data.area.isNullOrEmpty()) "Not Available" else data.area
            _binding!!.shortAddress1.text =if (data.area.isNullOrEmpty()) "Not Available" else data.area
            _binding!!.addressTv.text =if (data.address.isNullOrEmpty()) "Not Available" else data.floor+"\n"+data.address
            _binding!!.addressTv1.text =if (data.address.isNullOrEmpty()) "Not Available" else data.floor+"\n"+data.address
            _binding!!.contactNumber.text =if (data.contacts.isNullOrEmpty()) "Not Available" else data.contacts.dropLast(1)
            _binding!!.description.text =data.business_name+" established in "+data.established_year+". "+ if (data.description.isNullOrEmpty()) "" else data.description
            _binding!!.stablised.text =if (data.established_year.isNullOrEmpty()) "Not Available" else data.established_year
//            _binding!!.contact.text = data.contacts
//            _binding!!.whatsappNumber.text = data.whatsapp_business
//            _binding!!.website.text = data.website
            _binding!!.avrageReview.text =   if (data.average_rating.toString().isNullOrEmpty()) "No Review" else getString(R.string._4_7_5,data.average_rating.toString())
//            _binding!!.GSTNNumber.text = getString(R.string.gstin,data.gst.toString())
//            _binding!!.panNumber.text = getString(R.string.pan_no,data.pancard.toString())
            _binding!!.ratingdata.text =if (data.average_rating.toString().isNullOrEmpty()) "No Review" else data.average_rating.toString()
            _binding!!.distance.text =if (data.distance.isNullOrEmpty()) "Not able to calculate" else data.distance.removeRange(3,data.distance.length)+" km Away"
            _binding!!.ratingCount.text =if (data.rating_list.isNullOrEmpty()) "No Review" else {
                data.rating_list.size.toString()+" rating"
            }
            WhatsappNumber = data.whatsapp_business
            getStoreTimeData(data)
         //   setRatingAndReviewsLayoutWithData(data.id)
            if (data.business_tag.isNullOrEmpty().not()){
                businessSevicesAdapter!!.addAllItem(data.business_tag)
            }


            setLayoutData(data)
            _binding!!.rating.setOnClickListener {
                OpenRatingBar(data)
            }
            _binding!!.busizzCard.setOnClickListener {
                LocalbizContainerActivity.openContainerforEditBusiness(requireContext(), "GetBusinessCard", data.id)
            }

            _binding!!.ratingCount.setOnClickListener {
                LocalbizContainerActivity.openContainerforEditBusiness(requireContext(), "BusinessPersonRatingAndReviewFragment",data.id)
            }
            _binding!!.createBusiness.setOnClickListener {
                LocalbizContainerActivity.openContainer(requireContext(),"AddBusiness")
            }

            _binding!!.rating.setOnClickListener {
                OpenRatingBar(data)
            }
        }



    }

    private fun openBottomSheetForCall(contacts: String) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.call_to_business_layout, null)
        val rvList = view.findViewById<RecyclerView>(R.id.contactList)
        var listofcontact = contacts.split(",")


       calltoContactAdapter = CalltoContactAdapter<ContactModel>(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                dialog.dismiss()
            }

        }).apply {
           listofcontact.forEach {
               addItem(ContactModel(it.toString()))
           }


       }
        rvList.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = calltoContactAdapter
        }


        dialog.setCancelable(true)


        dialog.setContentView(view)
        dialog.show()
    }

    private fun getStoreTimeData(data: Data?) {

        _binding!!.Shoptime.setOnClickListener {
            openBottomSheet(data)
        }

        val simpleDate = SimpleDateFormat("HH:MM")
        val currenttime = simpleDate.format(Date())
        val convertTime = SimpleDateFormat("HH:MM")

        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        if (data!!.always_open.toString().equals("1")){
            _binding!!.Shoptime.text = "Always Open"
        }else{
            when (day){
                Calendar.MONDAY ->{
                    if (data!!.mon_opening.equals("Closed").not()){
                        if (currenttime>=data!!.mon_opening  && currenttime<=data.mon_closing){
                            _binding!!.Shoptime.text = data.mon_opening +" - " + data.mon_closing
                        }else{
                            _binding!!.Shoptime.text = "Closed"

                        }
                    }

                }
                Calendar.TUESDAY  ->{
                    if (data!!.tue_opening.equals("Closed").not()){
                        if (currenttime>=data!!.tue_opening  && currenttime<=data.tue_closing){
                            _binding!!.Shoptime.text = data.tue_opening +" - " + data.tue_closing
                        }else{
                            _binding!!.Shoptime.text = "Closed"

                        }
                    }

                }
                Calendar.WEDNESDAY ->{
                    if (data!!.wed_opening.equals("Closed").not()){
                        if (currenttime>=data!!.wed_opening  && currenttime<=data.wed_closing){
                            _binding!!.Shoptime.text = data.wed_opening +" - " +data.wed_closing
                        }else{
                            _binding!!.Shoptime.text = "Closed"
                        }
                    }

                }
                Calendar.THURSDAY  ->{
                    if (data!!.thu_opening.equals("Closed").not()){
                        if (currenttime>=data!!.thu_opening  && currenttime<=data.thu_closing){
                            _binding!!.Shoptime.text = data.thu_opening +" - " + data.thu_closing
                        }else{
                            _binding!!.Shoptime.text = "Closed"
                        }
                    }

                }
                Calendar.FRIDAY  ->{
                    if (data!!.fri_opening.equals("Closed").not()){
                        if (currenttime>=data!!.fri_opening  && currenttime<=data.fri_closing){
                            _binding!!.Shoptime.text =data.fri_opening +" - " + data.fri_closing
                        }else{
                            _binding!!.Shoptime.text = "Closed"
                        }
                    }

                }
                Calendar.SATURDAY ->{
                    if (data!!.sat_opening.equals("Closed").not()){
                        if (currenttime>=data!!.sat_opening  && currenttime<=data.sat_closing){
                            _binding!!.Shoptime.text = data.sat_opening +" - " + data.sat_closing
                        }else{
                            _binding!!.Shoptime.text = "Closed"
                        }
                    }

                }
                Calendar.SUNDAY  ->{
                    if (data!!.sun_opening.equals("Closed").not()){
                        if (currenttime>=data!!.sun_opening  && currenttime<=data.sun_closing){
                            _binding!!.Shoptime.text = data.sun_opening +" - " + data.sun_closing
                        }else{
                            _binding!!.Shoptime.text = "Closed"
                        }
                    }

                }
            }
        }



    }

    private fun openBottomSheet(data: Data?) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.shop_timing_sheet_layout, null)

        dialog.setCancelable(true)



        var monday = view.findViewById<TextView>(R.id.mondaytime)
        var tuesdaytime = view.findViewById<TextView>(R.id.tuesdaytime)
        var wednesdaytime = view.findViewById<TextView>(R.id.wednesdaytime)
        var thursdaytime = view.findViewById<TextView>(R.id.thursdaytime)
        var fridaytime = view.findViewById<TextView>(R.id.fridaytime)
        var sattime = view.findViewById<TextView>(R.id.sattime)
        var suntime = view.findViewById<TextView>(R.id.suntime)
        var okbtn = view.findViewById<AppCompatButton>(R.id.okbtn)

        var businessTimes = view.findViewById<ConstraintLayout>(R.id.businessTimes)
        var AlwaysaOpen = view.findViewById<TextView>(R.id.AlwaysaOpen)


        if (data!!.always_open.toString().equals("1")){
            AlwaysaOpen.visibility = View.VISIBLE
            businessTimes.visibility = View.GONE
        }else{
            AlwaysaOpen.visibility = View.GONE
            businessTimes.visibility = View.VISIBLE
        }

        monday.text = getString(R.string.monday_9_30_am_6_30_pm,"Monday",data!!.mon_opening,data.mon_closing)
        tuesdaytime.text = getString(R.string.monday_9_30_am_6_30_pm,"Tuesday",data!!.tue_opening,data.tue_closing)
        wednesdaytime.text = getString(R.string.monday_9_30_am_6_30_pm,"Wednesday",data!!.wed_opening,data.wed_closing)
        thursdaytime.text = getString(R.string.monday_9_30_am_6_30_pm,"Thursday",data!!.thu_opening,data.thu_closing)
        fridaytime.text = getString(R.string.monday_9_30_am_6_30_pm,"Friday",data!!.fri_opening,data.fri_closing)
        sattime.text = getString(R.string.monday_9_30_am_6_30_pm,"Saturday",data!!.sat_opening,data.sat_closing)
        suntime.text = getString(R.string.monday_9_30_am_6_30_pm,"Sunday",data!!.sun_opening,data.sun_closing)


        okbtn.setOnClickListener {
            dialog.dismiss()
        }


        dialog.setContentView(view)
        dialog.show()


    }


    private fun chatwithwhatsapp() {

        requireContext().ContactOnWhatsapp(WhatsappNumber)

    }

    private fun setLayoutData(data: Data?) {
        val simpleDate = SimpleDateFormat("yyyy")
        val currentyear = simpleDate.format(Date())
        val years = currentyear.toInt() - data!!.established_year.toString().toInt()


        _binding!!.oldBussinessYear.text = years.toString()+"+"

        if (data.average_rating.toString().isNullOrEmpty().not()){
            if (data.average_rating.toString().toDouble() >= 3.0){
                _binding!!.avrageRatingExpration.text ="Good"
            }else if (data.average_rating.toString().toDouble() >= 4.0){
                _binding!!.avrageRatingExpration.text ="Excellent"
            }else if (data.average_rating.toString().toDouble() == 0.0){
                _binding!!.avrageRatingExpration.text ="-"
            }else{
                _binding!!.avrageRatingExpration.text ="Average"
            }
        }else{
            _binding!!.avrageRatingExpration.text ="-"
        }

    }

    private fun OpenRatingBar(data: Data?) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.add_rating_layout, null)

        dialog.setCancelable(true)
        val icon: ImageView = view.findViewById(R.id.iv_partner)
        val name: TextView = view.findViewById(R.id.business_name)
        val send_review: AppCompatButton = view.findViewById(R.id.send_review)
        val review: TextInputEditText = view.findViewById(R.id.businee_description)
        val rating: RatingBar = view.findViewById(R.id.rating)

        Glide.with(requireContext()).load(data!!.logo).centerCrop().into(icon)
        name.text = data.business_name




        send_review.setOnClickListener {
            if (rating.rating.toString().equals("0.0")){
                requireView().showShortSnack("Please select stars for rating")
            }else{
                Log.d("BusinessRatings",rating.rating.toString())
                AddBusinessRatingModel(review.text.toString(),rating.rating.toString(),data.id)
                dialog.dismiss()
            }

        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun AddBusinessRatingModel(review: String, rating: String,business_id: String) {
        viewModel.addRatingToBusiness(getUserId(),business_id,rating,review,this,object : ApiListener<AddBusinessRatingRes,Any?>{
            override fun onSuccess(t: AddBusinessRatingRes?, mess: String?) {
                if (t!!.errorCode.equals("0")){
                    PopupDialogAfterSendReview()
                }else{
                    this@ShowSelectedBusinessDataFragment.requireView().showShortSnack("Review not Submitted")
                }
            }

        })
    }

    private fun PopupDialogAfterSendReview() {
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.after_review_dialog,null)
        dialog.setCancelable(false)
        val Done: AppCompatButton = view.findViewById(R.id.Done)

        Done.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
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
                Mylatitude = location.latitude
                Mylongitude = location.longitude
                MyAltitude= location.altitude
                getLocationNameData(Mylatitude,Mylongitude)
                setAllMethodaAndFinctions()
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

    private fun getDirectionClickMethod(latitude: String, longitude: String) {
        _binding!!.getDirection.setOnClickListener {
            if (latitude != null) {
                val packageName = "com.google.android.apps.maps"
//                val query = java.lang.String.format("google.navigation:q=",Mylatitude.toString()+","+Mylongitude.toString()+"/"+latitude,longitude)
                val query  = String.format("http://maps.google.com/maps?saddr="+Mylatitude+","+Mylongitude+"&daddr="+latitude+","+longitude)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(query))
                intent.setPackage(packageName)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "please get current location", Toast.LENGTH_SHORT).show()
            }
        }
        _binding!!.direction.setOnClickListener {
            if (latitude != null) {
                val packageName = "com.google.android.apps.maps"
//                val query = java.lang.String.format("google.navigation:q=",Mylatitude.toString()+","+Mylongitude.toString()+"/"+latitude,longitude)
                val query  = String.format("http://maps.google.com/maps?saddr="+Mylatitude+","+Mylongitude+"&daddr="+latitude+","+longitude)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(query))
                intent.setPackage(packageName)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "please get current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun superlinksSubCategories() {
        businessSevicesAdapter = business_services_adapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })
        _binding!!.SuperLinksSUBRV.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = businessSevicesAdapter
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ShowSelectedBusinessDataFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}