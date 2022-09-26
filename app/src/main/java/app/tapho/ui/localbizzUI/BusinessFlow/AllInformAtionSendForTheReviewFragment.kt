package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import app.tapho.R
import app.tapho.databinding.FragmentAllInformAtionSendForTheReviewBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import com.bumptech.glide.Glide
import java.util.*


class AllInformAtionSendForTheReviewFragment : BaseFragment<FragmentAllInformAtionSendForTheReviewBinding>() {


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
        _binding = FragmentAllInformAtionSendForTheReviewBinding.inflate(inflater,container,false)
        statusBarColor(R.color.Minicash)
        statusBarTextWhite()
        _binding!!.finaltext.text = getString(R.string.great_jakir_now_prepare_your,getSharedPreference().getLoginData()!!.name.toString())
        setNullToAllValue()
        Timer().schedule(object  :  TimerTask(){
            override fun run() {
                LocalbizContainerActivity.openContainer(requireContext(),"HomePage")
                activity?.finish()
            }
        },3000)
        return _binding?.root
    }

    private fun setNullToAllValue() {
        getSharedPreference().saveString("profile_image","")
        getSharedPreference().saveString("SaveProfile_banner","")
        getSharedPreference().saveString("bussinessType","")
        getSharedPreference().saveString("category_name","")
        getSharedPreference().saveString("BusinessName","")
        getSharedPreference().saveString("BusinessPAN","")
        getSharedPreference().saveString("BusinessGSTNNumber","")
        getSharedPreference().saveString("businessDescription","")
        getSharedPreference().saveString("secondary_number","")
        getSharedPreference().saveString("businessEmail","")
        getSharedPreference().saveString("business_whatsapp_number","")
        getSharedPreference().saveString("businesswebsite","")
        getSharedPreference().saveString("ESTB_YEAR","")
        getSharedPreference().saveString("BusinessCurrentLocation","")
        getSharedPreference().saveString("mondaystart","")
        getSharedPreference().saveString("mondayend","")
        getSharedPreference().saveString("tuesdaystart","")
        getSharedPreference().saveString("tuesdayend","")
        getSharedPreference().saveString("Wednesdaystart","")
        getSharedPreference().saveString("Wednesdayend","")
        getSharedPreference().saveString("Thursdaystart","")
        getSharedPreference().saveString("Thursdayend","")
        getSharedPreference().saveString("Fridaystart","")
        getSharedPreference().saveString("Fridayend","")
        getSharedPreference().saveString("Saturdaystart","")
        getSharedPreference().saveString("Saturdayend","")
        getSharedPreference().saveString("Sundaystart","")
        getSharedPreference().saveString("Sundayend","")

        getSharedPreference().saveString("City","")
        getSharedPreference().saveString("PinCode","")
        getSharedPreference().saveString("area","")
        getSharedPreference().saveString("longitude","")
        getSharedPreference().saveString("latitude","")

        getSharedPreference().saveString("BusinessCurrentLocation","")
        getSharedPreference().saveString("floor","")
        getSharedPreference().saveString("landmark","")
        getSharedPreference().saveString("service_name","")
        getSharedPreference().saveString("category_id","")
        getSharedPreference().saveString("service_id","")
        getSharedPreference().saveString("tagsIds","")
        getSharedPreference().saveString("businesstagsbusinesstagIds","")
        getSharedPreference().saveString("businesstags","")
        getSharedPreference().saveString("optional_number","")
        getSharedPreference().saveString("alwaysopen","")


    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AllInformAtionSendForTheReviewFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}