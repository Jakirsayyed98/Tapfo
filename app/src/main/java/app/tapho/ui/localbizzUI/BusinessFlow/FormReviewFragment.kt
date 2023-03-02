package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.tapho.R
import app.tapho.databinding.FragmentFormReviewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.SaveBusiness.SaveBusinessRes
import com.bumptech.glide.Glide


class FormReviewFragment : BaseFragment<FragmentFormReviewBinding>() {

    var address: String = ""
    var always_open: String = ""
    var area: String = ""
    var average_rating: String = ""
    var business_category_id: String = ""
    var business_category_name: String = ""
    var business_name: String = ""
    var business_subcategory_id: String = ""
    var business_subcategory_name: String = ""
    var business_type_id: String = ""
    var business_type_name: String = ""
    var city: String = ""
    var contacts: String = ""
    var created_at: String = ""
    var description: String = ""
    var email: String = ""
    var established_year: String = ""
    var floor: String = ""
    var fri_closing: String = ""
    var fri_opening: String = ""
    var gst: String = ""
    var logo: String = ""
    var image: String = ""
    var landmark: String = ""
    var latitude: String = ""
    var longitude: String = ""
    var mon_closing: String = ""
    var mon_opening: String = ""
    var pancard: String = ""
    var pincode: String = ""
    var sat_closing: String = ""
    var sat_opening: String = ""
    var status: String = ""
    var sun_closing: String = ""
    var sun_opening: String = ""
    var temparary_close: String = ""
    var thu_closing: String = ""
    var thu_opening: String = ""
    var tue_closing: String = ""
    var tue_opening: String = ""
    var wed_closing: String = ""
    var wed_opening: String = ""
    var whatsapp_business: String = ""
    var tags: String = ""
    var tagsIds: String = ""


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
        _binding = FragmentFormReviewBinding.inflate(inflater, container, false)
        _binding!!.toolbar.tvTitle.text = "Review and Submit"
        _binding!!.btnVerify.isSelected = true
        _binding!!.main.visibility = View.VISIBLE
        _binding!!.loadingScreen.visibility = View.GONE
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        statusBarColor(R.color.white)
       statusBarTextWhite()
        setAllTextInStringFormate()
        return _binding?.root
    }

    private fun setAllTextInStringFormate() {

        address = getSharedPreference().getString("BusinessCurrentLocation").toString()
        always_open = getSharedPreference().getString("alwaysopen").toString()
        area = getSharedPreference().getString("area")!!
        business_category_id = getSharedPreference().getString("category_id").toString()
        business_category_name = getSharedPreference().getString("category_name").toString()
        business_name = getSharedPreference().getString("BusinessName").toString()
        business_subcategory_id = getSharedPreference().getString("service_id").toString()
        business_subcategory_name = getSharedPreference().getString("service_name").toString()
        business_type_id = getSharedPreference().getString("bussinessTypeid").toString()
        business_type_name = getSharedPreference().getString("bussinessType").toString()
        city = getSharedPreference().getString("City").toString()
        contacts = getSharedPreference().getString("secondary_number").toString()+","+getSharedPreference().getString("optional_number").toString()
        description = getSharedPreference().getString("businessDescription").toString()
        email = getSharedPreference().getString("businessEmail").toString()
        established_year = getSharedPreference().getString("ESTB_YEAR").toString()
        floor = getSharedPreference().getString("floor").toString()
        fri_closing = getSharedPreference().getString("Fridayend").toString()
        fri_opening = getSharedPreference().getString("Fridaystart").toString()
        gst = getSharedPreference().getString("BusinessGSTNNumber").toString()
        logo = getSharedPreference().getString("profile_image").toString()
        image = getSharedPreference().getString("SaveProfile_banner").toString()
        landmark = getSharedPreference().getString("landmark").toString()
        latitude = getSharedPreference().getString("latitude").toString()
        longitude = getSharedPreference().getString("longitude").toString()
        mon_closing = getSharedPreference().getString("mondayend").toString()
        mon_opening = getSharedPreference().getString("mondaystart").toString()
        pancard = getSharedPreference().getString("BusinessPAN").toString()
        pincode = getSharedPreference().getString("PinCode").toString()
        sat_closing = getSharedPreference().getString("Saturdayend").toString()
        sat_opening = getSharedPreference().getString("Saturdaystart").toString()
        sun_closing = getSharedPreference().getString("Sundayend").toString()
        sun_opening = getSharedPreference().getString("Sundaystart").toString()
        temparary_close ="0"
        thu_closing = getSharedPreference().getString("Thursdayend").toString()
        thu_opening = getSharedPreference().getString("Thursdaystart").toString()
        tue_closing = getSharedPreference().getString("tuesdayend").toString()
        tue_opening = getSharedPreference().getString("tuesdaystart").toString()
        wed_closing = getSharedPreference().getString("Wednesdayend").toString()
        wed_opening = getSharedPreference().getString("Wednesdaystart").toString()
        tags = getSharedPreference().getString("businesstags").toString()
        tagsIds = getSharedPreference().getString("businesstagIds").toString()
        whatsapp_business = getSharedPreference().getString("business_whatsapp_number").toString()


        _binding!!.city.setText(city.toString())
        _binding!!.pincode.setText(pincode.toString())
        _binding!!.area.setText(area.toString())
        _binding!!.tags.setText(tags.toString())
        _binding!!.service.text = business_subcategory_name

    }

    override fun onStart() {
        super.onStart()
        setAllTextForReview()
    }

    private fun setAllTextForReview() {

        Glide.with(requireContext()).load(logo)//.load(getSharedPreference().getString("profile_image"))
            .circleCrop().centerCrop().into(_binding!!.iconProfile)
        var profile_banner = getSharedPreference().getString("SaveProfile_banner")
        if (!profile_banner.isNullOrEmpty()) {
            Glide.with(requireContext()).load(image).centerCrop()
                .into(_binding!!.shopImage)
            _binding!!.shopImageAdd.visibility = View.GONE
        }
        _binding!!.businessTypeDescription.text = getSharedPreference().getString("bussinessType")
        _binding!!.categoryName.text = getSharedPreference().getString("category_name")
        _binding!!.storeNameData.text = getSharedPreference().getString("BusinessName")
        _binding!!.businessPancard.text = getSharedPreference().getString("BusinessPAN")
        _binding!!.GSTNNumber.text = getSharedPreference().getString("BusinessGSTNNumber")
        _binding!!.floor.setText( getSharedPreference().getString("floor"))
        _binding!!.landmark.setText( getSharedPreference().getString("landmark"))
        _binding!!.apply {
            businessDescription.text = if (getSharedPreference().getString("businessDescription")
                    .isNullOrEmpty()
            ) "Tell About your Business" else getSharedPreference().getString("businessDescription")
            contactnumber.text = if (getSharedPreference().getString("secondary_number")
                    .isNullOrEmpty()
            ) "Enter your business Conatcs" else getSharedPreference().getString("secondary_number").toString()+","+getSharedPreference().getString("optional_number").toString()
            addEmail.text = if (getSharedPreference().getString("businessEmail")
                    .isNullOrEmpty()
            ) "Enter your business email id" else getSharedPreference().getString("businessEmail")
            businessWhatsappNumber.text =
                if (getSharedPreference().getString("business_whatsapp_number")
                        .isNullOrEmpty()
                ) "Enter your Business WhatsApp Number" else getSharedPreference().getString("business_whatsapp_number")
            businessWebsiteUrl.text = if (getSharedPreference().getString("businesswebsite")
                    .isNullOrEmpty()
            ) "Enter your business website Url" else getSharedPreference().getString("businesswebsite")
            establisedYear.text = if (getSharedPreference().getString("ESTB_YEAR")
                    .isNullOrEmpty()
            ) "Enter your business working since" else getSharedPreference().getString("ESTB_YEAR")
            currentLocation.text = getSharedPreference().getString("BusinessCurrentLocation")





            mondaystart.text = getSharedPreference().getString("mondaystart")
            mondayend.text = getSharedPreference().getString("mondayend")
            tuesdaystart.text = getSharedPreference().getString("tuesdaystart")
            tuesdayend.text = getSharedPreference().getString("tuesdayend")
            Wednesdaystart.text = getSharedPreference().getString("Wednesdaystart")
            Wednesdayend.text = getSharedPreference().getString("Wednesdayend")
            Thursdaystart.text = getSharedPreference().getString("Thursdaystart")
            Thursdayend.text = getSharedPreference().getString("Thursdayend")
            Fridaystart.text = getSharedPreference().getString("Fridaystart")
            Fridayend.text = getSharedPreference().getString("Fridayend")
            Saturdaystart.text = getSharedPreference().getString("Saturdaystart")
            Saturdayend.text = getSharedPreference().getString("Saturdayend")
            Sundaystart.text = getSharedPreference().getString("Sundaystart")
            Sundayend.text = getSharedPreference().getString("Sundayend")

            if (getSharedPreference().getString("alwaysopen").toString().equals("0")){
                _binding!!.ervrdaytime.visibility = View.GONE
                _binding!!.custome.visibility = View.VISIBLE
            }else{
                _binding!!.ervrdaytime.visibility = View.VISIBLE
                _binding!!.custome.visibility = View.GONE
            }
        }

        _binding!!.btnVerify.setOnClickListener {
            _binding!!.main.visibility = View.GONE
            _binding!!.loadingScreen.visibility = View.VISIBLE
                    viewModel.getSaveBusiness(getUserId(), business_type_id, business_category_id, business_subcategory_id, business_name, pancard, gst, image,logo, description, contacts, email, whatsapp_business, established_year, latitude, longitude, address,floor,
                area, city, pincode, landmark, temparary_close, always_open, sun_opening, sun_closing, mon_opening, mon_closing, tue_opening, tue_closing, wed_opening,
                wed_closing, thu_opening, thu_closing, fri_opening, fri_closing, sat_opening, sat_closing,"",tagsIds, this, object : ApiListener<SaveBusinessRes,Any?>{
                    override fun onSuccess(t: SaveBusinessRes?, mess: String?) {
                        t.let {
                            if (it!!.errorCode.toString() == "0"){
                                LocalbizContainerActivity.openContainer(requireContext(), "SendForTheReview")
                            }
                        }
                    }

                })



        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            FormReviewFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}