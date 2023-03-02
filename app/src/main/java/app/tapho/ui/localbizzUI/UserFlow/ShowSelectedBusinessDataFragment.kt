package app.tapho.ui.localbizzUI.UserFlow

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.*
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.databinding.FragmentShowSelectedBusinessDataBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PrivacyPolicyActivity
import app.tapho.ui.intro.IntroActivity
import app.tapho.ui.localbizzUI.Adapter.CalltoContactAdapter
import app.tapho.ui.localbizzUI.Adapter.ProfilesRatingAndReviewsShowAdapter
import app.tapho.ui.localbizzUI.Adapter.business_services_adapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.AddRating.AddBusinessRatingRes
import app.tapho.ui.localbizzUI.Model.ContactModel
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.BusinessTag
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Data
import app.tapho.ui.localbizzUI.Model.SearchAllBusinesses.Rating
import app.tapho.ui.login.VerifyOtpActivity
import app.tapho.utils.*
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ShowSelectedBusinessDataFragment : BaseFragment<FragmentShowSelectedBusinessDataBinding>(),
    ConnectivityListener {
    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var Mylatitude: Double = 0.0
    private var Mylongitude: Double = 0.0
    private var MyAltitude: Double = 0.0
    var businessSevicesAdapter: business_services_adapter<BusinessTag>? = null
    var RatingAndReviewsAdapter: ProfilesRatingAndReviewsShowAdapter<Rating>? = null
    var WhatsappNumber = ""
    var calltoContactAdapter: CalltoContactAdapter<ContactModel>? = null

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
        _binding = FragmentShowSelectedBusinessDataBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        progressVISIBLE()
        checkConnection()

        return _binding?.root
    }


    override fun onResume() {
        super.onResume()

        setConnectivityListener(this)
    }

    private fun checkConnection() {
        val isConnected = ConnectionReceiver.isConnected(requireContext())

        if (isConnected) {
            getCurrentLocationData()
        } else {
            noInternetConnection()
        }

        _binding!!.retryinternetButton.setOnClickListener {
            checkConnection()
        }


    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            getCurrentLocationData()
        } else {
            noInternetConnection()
        }
    }


    private fun setAllMethodaAndFinctions() {
        superlinksSubCategories()
        setRatingAndReviewsLayout()
        _binding!!.backbutton.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.chat1.setOnClickListener {
            chatwithwhatsapp()
        }
        _binding!!.chatwithus.setOnClickListener {
            chatwithwhatsapp()
        }

        showHome()
        activity?.intent?.getStringExtra(DATA).let {
            val data = Gson().fromJson(it, Data::class.java)
            if (data.image.isNullOrEmpty().not()){
                Glide.with(requireContext()).load(data.image).centerCrop().into(_binding!!.banner)
                _binding!!.bannerCard.visibility = View.VISIBLE
            }else{
                _binding!!.bannerCard.visibility = View.GONE
            }


            Glide.with(requireContext()).load(data.logo).centerCrop().into(_binding!!.icon)
            getDirectionClickMethod(data.latitude, data.longitude)

            _binding!!.callNow1.setOnClickListener {
                openBottomSheetForCall(data.contacts,data.business_name)
            }
            RatingAndReviewsAdapter!!.addAllItem(if (data.rating_list.size > 4) data.rating_list.subList(0, 4) else data.rating_list)


            val runnable = object : Runnable{
                override fun run() {
                    synchronized(requireContext()){
                      data.rating_list.forEach {
                          if(it.user_id.equals(getUserId())){
                              _binding!!.rating.visibility = View.GONE
                          }
                      }
                    }
                }
            }
            val thread = Thread(runnable)
            thread.start()

            _binding!!.ratingstar.rating = data.average_rating.toFloat()
            _binding!!.avrageRating.text = data.average_rating.toString()
            _binding!!.totalreview.text = getString(R.string.s_total_reviews, data.rating_list.size.toString())
            _binding!!.usersReview.visibility = if (data.rating_list.isNullOrEmpty().not()) View.VISIBLE else View.GONE

            _binding!!.searchEt.setHint(data.business_name)
            _binding!!.servicesby.text = getString(R.string.services_by,data.business_name)
            _binding!!.name.text = if (data.business_name.isNullOrEmpty()) "Not Available" else requireContext().CamelCaseValue(data.business_name)
            _binding!!.name1.text = if (data.business_name.isNullOrEmpty()) "Not Available" else data.business_name
            _binding!!.shortAddress.text = if (data.area.isNullOrEmpty()) "Not Available" else data.area
            _binding!!.shortAddress1.text = if (data.area.isNullOrEmpty()) "Not Available" else data.area
            _binding!!.addressTv.text = if (data.address.isNullOrEmpty()) "Not Available" else data.address
            _binding!!.roadnumber.text = if (data.address.isNullOrEmpty()) "Not Available" else data.floor
            _binding!!.addressTv1.text = if (data.address.isNullOrEmpty()) "Not Available" else data.floor + "\n" + data.address
//            _binding!!.contactNumber.text = if (data.contacts.isNullOrEmpty()) "Not Available" else data.contacts.dropLast(1)
            _binding!!.description.text = data.business_name + " established in " + data.established_year + ". " + if (data.description.isNullOrEmpty()) "" else data.description


            _binding!!.ratingdata.text = if (data.average_rating.toString().isNullOrEmpty()) "No Review" else data.average_rating.toString()
            _binding!!.distance.text = if (data.distance.isNullOrEmpty()) "Not able to calculate" else data.distance.removeRange(3, data.distance.length) + " km Away"
            _binding!!.ratingCount.text = if (data.rating_list.isNullOrEmpty()) "No Review" else {
               "( "+ data.rating_list.size.toString() + " rating"+" )"
            }
            WhatsappNumber = data.whatsapp_business
            getStoreTimeData(data)


            _binding!!.gstlayout.visibility = if (data!!.gst.isNullOrEmpty().not()) View.VISIBLE else View.GONE

            _binding!!.stablised.text = if (data.established_year.isNullOrEmpty()) "Not Available" else data.established_year
            _binding!!.contact.text = data.contacts.replace(",","\n").replace(" ","")
            _binding!!.whatsappNumber.text = data.whatsapp_business
            _binding!!.emailid.text = data.email
            _binding!!.avrageReview.text = if (data.average_rating.toString().isNullOrEmpty()) "No Review" else getString(R.string._4_7_5, data.average_rating.toString())
            _binding!!.GSTNNumber.text = if (data.gst.isNullOrEmpty().not()) data.gst else ""

            val simpleDate = SimpleDateFormat("yyyy")
            val currentyear = simpleDate.format(Date())
            val years = currentyear.toInt() - data!!.established_year.toString().toInt()
            _binding!!.oldBussinessYear.text = years.toString() + "+"
            _binding!!.avrageRatingExpration.text = setLayoutData(data)
            _binding!!.description.text =
                data.business_name + " Established in the year" + data.established_year + ". " + if (data.description.isNullOrEmpty()) "" else data.description


            if (data.business_tag.isNullOrEmpty().not()) {
                _binding!!.businessserviceslay.visibility = View.VISIBLE
                businessSevicesAdapter!!.addAllItem(data.business_tag)
            } else {
                _binding!!.businessserviceslay.visibility = View.GONE
            }
            _binding!!.rating.setOnClickListener {
                OpenRatingBar(data)
            }
            _binding!!.busizzCard.setOnClickListener {
                LocalbizContainerActivity.openContainerforEditBusiness(requireContext(), "GetBusinessCard", data.id)
            }

            _binding!!.ratingCount.setOnClickListener {
                LocalbizContainerActivity.openContainerforEditBusiness(requireContext(), "BusinessPersonRatingAndReviewFragment", data.id)
            }
            _binding!!.allReviews.setOnClickListener {
                LocalbizContainerActivity.openContainerforEditBusiness(requireContext(), "BusinessPersonRatingAndReviewFragment", data.id)
            }
//            _binding!!.createBusiness.setOnClickListener {
//                LocalbizContainerActivity.openContainer(requireContext(), "AddBusiness")
//            }

        }


    }

    private fun setRatingAndReviewsLayout() {
        RatingAndReviewsAdapter =
            ProfilesRatingAndReviewsShowAdapter(object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                }
            })
        _binding!!.UsersReview.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = RatingAndReviewsAdapter
        }
    }

    private fun setLayoutData(data: Data?): String {
        var ratingstatus = ""
        if (data!!.average_rating.toString().isNullOrEmpty().not()) {
            if (data.average_rating.toString().toDouble() >= 3.0) {
                ratingstatus = "Good"
            } else if (data.average_rating.toString().toDouble() >= 4.0) {
                ratingstatus = "Excellent"
            } else if (data.average_rating.toString().toDouble() == 0.0) {
                ratingstatus = "-"
            } else {
                ratingstatus = "Average"
            }
        } else {
            ratingstatus = "-"
        }
        return ratingstatus
    }

    private fun openBottomSheetForCall(contacts: String,name: String) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.call_to_business_layout, null)
        val rvList = view.findViewById<RecyclerView>(R.id.contactList)
        val business_name = view.findViewById<TextView>(R.id.business_name)
        val okay = view.findViewById<AppCompatButton>(R.id.okay)

        business_name.text = name
        val listofcontact = contacts.split(",")

        okay.setOnClickListener {
            dialog.dismiss()
        }

        calltoContactAdapter = CalltoContactAdapter<ContactModel>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                dialog.dismiss()
            }

        }).apply {
            listofcontact.forEach {
                addItem(ContactModel(it.toString()))
            }


        }
        rvList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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


        _binding!!.Shoptime.text = "( "+shoptimestatus(data)+" )"


    }

    private fun shoptimestatus(data: Data?): String {
        var timestatus = ""
        val simpleDate = SimpleDateFormat("HH:MM")
        val currenttime = simpleDate.format(Date())
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        if (data!!.always_open.toString().equals("1")) {
            timestatus = "Always Open"
        } else {
            when (day) {
                Calendar.MONDAY -> {
                    if (data.mon_opening.equals("Closed").not()) {
                        if (currenttime >= data.mon_opening && currenttime <= data.mon_closing) {
                            timestatus = data.mon_opening + " to " + data.mon_closing + " (Open)"
                        } else {
                            timestatus = data.mon_opening + " to " + data.mon_closing + " (Closed)"

                        }
                    } else {
                        timestatus = "Closed"
                    }

                }
                Calendar.TUESDAY -> {
                    if (data.tue_opening.equals("Closed").not()) {
                        if (currenttime >= data.tue_opening && currenttime <= data.tue_closing) {
                            timestatus = data.tue_opening + " to " + data.tue_closing + " (Open)"
                        } else {
                            timestatus = data.tue_opening + " to " + data.tue_closing + " (Closed)"
                        }
                    } else {
                        timestatus = "Closed"
                    }

                }
                Calendar.WEDNESDAY -> {
                    if (data.wed_opening.equals("Closed").not()) {
                        if (currenttime >= data.wed_opening && currenttime <= data.wed_closing) {
                            timestatus = data.wed_opening + " to " + data.wed_closing + " (Open)"
                        } else {
                            timestatus = data.wed_opening + " to " + data.wed_closing + " (Closed)"
                        }
                    } else {
                        timestatus = "Closed"
                    }

                }
                Calendar.THURSDAY -> {
                    if (data.thu_opening.equals("Closed").not()) {
                        if (currenttime >= data.thu_opening && currenttime <= data.thu_closing) {
                            timestatus = data.thu_opening + " to " + data.thu_closing + " (Open)"
                        } else {
                            timestatus = data.thu_opening + " to " + data.thu_closing + " (Closed)"
                        }
                    } else {
                        timestatus = "Closed"
                    }

                }
                Calendar.FRIDAY -> {
                    if (data.fri_opening.equals("Closed").not()) {
                        if (currenttime >= data.fri_opening && currenttime <= data.fri_closing) {
                            timestatus = data.fri_opening + " to " + data.fri_closing + " (Open)"

                        } else {
                            timestatus = data.fri_opening + " to " + data.fri_closing + " (Closed)"
                        }
                    } else {
                        timestatus = "Closed"
                    }

                }
                Calendar.SATURDAY -> {
                    if (data.sat_opening.equals("Closed").not()) {
                        if (currenttime >= data.sat_opening && currenttime <= data.sat_closing) {
                            timestatus = data.sat_opening + " to " + data.sat_closing + " (Open)"

                        } else {
                            timestatus = data.sat_opening + " to " + data.sat_closing + " (Closed)"
                        }
                    } else {
                        timestatus = "Closed"
                    }

                }
                Calendar.SUNDAY -> {
                    if (data.sun_opening.equals("Closed").not()) {
                        if (currenttime >= data.sun_opening && currenttime <= data.sun_closing) {
                            timestatus = data.sun_opening + " to " + data.sun_closing + " (Open)"

                        } else {
                            timestatus = data.sun_opening + " to " + data.sun_closing + " (Closed)"
                        }
                    } else {
                        timestatus = "Closed"
                    }

                }
                else -> {
                    timestatus = "Closed"
                }
            }
        }

        return timestatus
    }

    private fun openBottomSheet(data: Data?) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.shop_timing_sheet_layout, null)

        dialog.setCancelable(true)


        val monday = view.findViewById<TextView>(R.id.mondaytime)
        val tuesdaytime = view.findViewById<TextView>(R.id.tuesdaytime)
        val wednesdaytime = view.findViewById<TextView>(R.id.wednesdaytime)
        val thursdaytime = view.findViewById<TextView>(R.id.thursdaytime)
        val fridaytime = view.findViewById<TextView>(R.id.fridaytime)
        val sattime = view.findViewById<TextView>(R.id.sattime)
        val suntime = view.findViewById<TextView>(R.id.suntime)
        val okbtn = view.findViewById<AppCompatButton>(R.id.okbtn)

        val AlwaysaOpen = view.findViewById<TextView>(R.id.AlwaysaOpen)
        val businessTimes = view.findViewById<ConstraintLayout>(R.id.businessTimes)


        if (data!!.always_open.toString().equals("1")) {
            AlwaysaOpen.visibility = View.VISIBLE
            businessTimes.visibility = View.GONE
        } else {
            AlwaysaOpen.visibility = View.GONE
            businessTimes.visibility = View.VISIBLE
        }

        monday.text =
            getString(R.string.monday_9_30_am_6_30_pm, "Monday", data.mon_opening, data.mon_closing)
        tuesdaytime.text = getString(
            R.string.monday_9_30_am_6_30_pm, "Tuesday",
            data.tue_opening, data.tue_closing
        )
        wednesdaytime.text = getString(
            R.string.monday_9_30_am_6_30_pm, "Wednesday",
            data.wed_opening, data.wed_closing
        )
        thursdaytime.text = getString(
            R.string.monday_9_30_am_6_30_pm, "Thursday",
            data.thu_opening, data.thu_closing
        )
        fridaytime.text =
            getString(R.string.monday_9_30_am_6_30_pm, "Friday", data.fri_opening, data.fri_closing)
        sattime.text = getString(
            R.string.monday_9_30_am_6_30_pm,
            "Saturday",
            data.sat_opening,
            data.sat_closing
        )
        suntime.text =
            getString(R.string.monday_9_30_am_6_30_pm, "Sunday", data.sun_opening, data.sun_closing)


        okbtn.setOnClickListener {
            dialog.dismiss()
        }


        dialog.setContentView(view)
        dialog.show()


    }

    private fun chatwithwhatsapp() {

        requireContext().ContactOnWhatsapp(WhatsappNumber)

    }

    private fun OpenRatingBar(data: Data?) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.add_rating_layout, null)

        dialog.setCancelable(true)
//        val icon: ImageView = view.findViewById(R.id.iv_partner)
        val name: TextView = view.findViewById(R.id.business_name)
        val send_review: AppCompatButton = view.findViewById(R.id.send_review)
        val review: TextInputEditText = view.findViewById(R.id.businee_description)
        val rating: RatingBar = view.findViewById(R.id.rating)

        name.text = data!!.business_name
        send_review.setOnClickListener {
            if (rating.rating.toString().equals("0.0")) {
                requireView().showShortSnack("Please select stars for rating")
            } else {
                AddBusinessRatingModel(review.text.toString(), rating.rating.toString(), data.id)
                dialog.dismiss()
            }

        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun AddBusinessRatingModel(review: String, rating: String, business_id: String) {
        viewModel.addRatingToBusiness(getUserId(), business_id, rating, review, this,
            object : ApiListener<AddBusinessRatingRes, Any?> {
                override fun onSuccess(t: AddBusinessRatingRes?, mess: String?) {
                    if (t!!.errorCode.equals("0")) {
                        PopupDialogAfterSendReview()
                    } else {
                        this@ShowSelectedBusinessDataFragment.requireView()
                            .showShortSnack("Review not Submitted")
                    }
                }

            })
    }

    private fun PopupDialogAfterSendReview() {
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.after_review_dialog, null)
        dialog.setCancelable(false)
        val Done: AppCompatButton = view.findViewById(R.id.Done)

        Done.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun getCurrentLocationData() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );

            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                Mylatitude = location.latitude
                Mylongitude = location.longitude
                MyAltitude = location.altitude
                getLocationNameData(Mylatitude, Mylongitude)
                setAllMethodaAndFinctions()
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failed on getting current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getLocationNameData(latitude: Double, longitude: Double) {
        val myLocation = Geocoder(requireContext(), Locale.getDefault())
        val myList: List<Address> =
            myLocation.getFromLocation(latitude, longitude, 1) as List<Address>
        val address: Address = myList[0] as Address
        var addressStr = ""
        addressStr += address.getAddressLine(0).toString()

        _binding!!.fulladdress.text = addressStr
        _binding!!.locationname.text = address.subLocality //addressStr
        getSharedPreference().saveString("BusinessCurrentLocation", addressStr)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(
                        requireContext(), "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getDirectionClickMethod(latitude: String, longitude: String) {
        _binding!!.direction.setOnClickListener {
            getDirectionFunction(latitude, longitude)
        }
        _binding!!.direction1.setOnClickListener {
            getDirectionFunction(latitude, longitude)

        }
    }

    private fun getDirectionFunction(latitude: String, longitude: String) {
        if (latitude != null) {
            val packageName = "com.google.android.apps.maps"
            val query =
                String.format("http://maps.google.com/maps?saddr=" + Mylatitude + "," + Mylongitude + "&daddr=" + latitude + "," + longitude)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(query))
            intent.setPackage(packageName)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "please get current location", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun superlinksSubCategories() {
        businessSevicesAdapter = business_services_adapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })
        _binding!!.SuperLinksSUBRV.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = businessSevicesAdapter
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
            ShowSelectedBusinessDataFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}