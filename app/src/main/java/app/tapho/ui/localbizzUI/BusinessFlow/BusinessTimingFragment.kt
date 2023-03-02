package app.tapho.ui.localbizzUI.BusinessFlow

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import app.tapho.R
import app.tapho.databinding.FragmentBusinessTimingBinding
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.utils.parseAgoDate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_business_timing.*
import java.text.SimpleDateFormat
import java.util.*


class BusinessTimingFragment : BaseFragment<FragmentBusinessTimingBinding>() {
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
        _binding = FragmentBusinessTimingBinding.inflate(inflater, container, false)
        _binding!!.toolbar.tvTitle.text = "Listing Business"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        statusBarTextWhite()
        statusBarColor(R.color.white)
        clickevents()
        Timeselect()

        return _binding?.root
    }

    override fun onStart() {
        super.onStart()
        setAllTextData()
    }

    private fun setAllTextData() {

        _binding!!.everday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                _binding!!.apply {
                    _binding!!.btnVerify.isSelected = true
                    monday.isChecked = false
                    Tuesday.isChecked = false
                    Wednesday.isChecked = false
                    Thursday.isChecked = false
                    Friday.isChecked = false
                    Saturday.isChecked = false
                    Sunday.isChecked = false
                    getSharedPreference().saveString("alwaysopen","1")
                }
            } else {
                getSharedPreference().saveString("everdaystart", "")
                getSharedPreference().saveString("everdayend", "")
                getSharedPreference().saveString("alwaysopen","0")

            }

        }
        _binding!!.monday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding!!.mondaytime.visibility = View.VISIBLE
                _binding!!.everday.isChecked = false
                _binding!!.btnVerify.isSelected = true
            } else {
                getSharedPreference().saveString("mondaystart", "")
                getSharedPreference().saveString("mondayend", "")
                _binding!!.mondaytime.visibility = View.GONE
            }
        }
        _binding!!.Tuesday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding!!.tuesdaytime.visibility = View.VISIBLE
                _binding!!.everday.isChecked = false
                _binding!!.btnVerify.isSelected = true
            } else {
                getSharedPreference().saveString("tuesdaystart", "")
                getSharedPreference().saveString("tuesdayend", "")
                _binding!!.tuesdaytime.visibility = View.GONE
            }
        }
        _binding!!.Wednesday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding!!.Wednesdaytime.visibility = View.VISIBLE
                _binding!!.everday.isChecked = false
                _binding!!.btnVerify.isSelected = true
            } else {
                getSharedPreference().saveString("Wednesdaystart", "")
                getSharedPreference().saveString("Wednesdayend", "")
                _binding!!.Wednesdaytime.visibility = View.GONE
            }

        }
        _binding!!.Thursday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding!!.Thursdaytime.visibility = View.VISIBLE
                _binding!!.everday.isChecked = false
                _binding!!.btnVerify.isSelected = true
            } else {
                getSharedPreference().saveString("Thursdaystart", "")
                getSharedPreference().saveString("Thursdayend", "")
                _binding!!.Thursdaytime.visibility = View.GONE
            }
        }
        _binding!!.Friday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding!!.Fridaytime.visibility = View.VISIBLE
                _binding!!.everday.isChecked = false
                _binding!!.btnVerify.isSelected = true
            } else {
                getSharedPreference().saveString("Fridaystart", "")
                getSharedPreference().saveString("Fridayend", "")
                _binding!!.Fridaytime.visibility = View.GONE
            }
        }
        _binding!!.Saturday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding!!.Saturdaytime.visibility = View.VISIBLE
                _binding!!.everday.isChecked = false
                _binding!!.btnVerify.isSelected = true
            } else {
                getSharedPreference().saveString("Saturdaystart", "")
                getSharedPreference().saveString("Saturdayend", "")
                _binding!!.Saturdaytime.visibility = View.GONE
            }
        }
        _binding!!.Sunday.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _binding!!.Sundaytime.visibility = View.VISIBLE
                _binding!!.everday.isChecked = false
                _binding!!.btnVerify.isSelected = true
            } else {
                getSharedPreference().saveString("Sundaystart", "")
                getSharedPreference().saveString("Sundayend", "")
                _binding!!.Sundaytime.visibility = View.GONE
            }
        }
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
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            time.text = SimpleDateFormat("hh.mm aa").format(cal.time)
            //     getSharedPreference().saveString(time.toString(),SimpleDateFormat("HH:mm a").format(cal.time))
        }
       // TimePickerDialog(requireContext(), timesetListnear, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        TimePickerDialog(requireContext(), R.style.DialogTheme ,timesetListnear, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

    }


    private fun clickevents() {

        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.btnVerify.setOnClickListener {
            if (_binding!!.everday.isChecked == true){
                saveInSharedPref()
                LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
            }else{
                if (_binding!!.monday.isChecked ==true){
                    if (_binding!!.mondayend.text.equals("Closed")){
                        this.requireView().showShortSnack("Please select proper timing")
                    }else{
                        saveInSharedPref()
                        LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
                    }

                }else  if (_binding!!.Tuesday.isChecked ==true){
                    if (_binding!!.tuesdayend.text.equals("Closed")){
                        this.requireView().showShortSnack("Please select proper timing")
                    }else{
                        saveInSharedPref()
                        LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
                    }
                }else  if (_binding!!.Wednesday.isChecked ==true){
                    if (_binding!!.Wednesdayend.text.equals("Closed")){
                        this.requireView().showShortSnack("Please select proper timing")
                    }else{
                        saveInSharedPref()
                        LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
                    }
                }else  if (_binding!!.Thursday.isChecked ==true){
                    if (_binding!!.Thursdayend.text.equals("Closed")){
                        this.requireView().showShortSnack("Please select proper timing")
                    }else{
                        saveInSharedPref()
                        LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
                    }
                }else  if (_binding!!.Friday.isChecked ==true){
                    if (_binding!!.Fridayend.text.equals("Closed")){
                        this.requireView().showShortSnack("Please select proper timing")
                    }else{
                        saveInSharedPref()
                        LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
                    }
                }else  if (_binding!!.Saturday.isChecked ==true){
                    if (_binding!!.Saturdayend.text.equals("Closed")){
                        this.requireView().showShortSnack("Please select proper timing")
                    }else{
                        saveInSharedPref()
                        LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
                    }
                }else  if (_binding!!.Sunday.isChecked ==true){
                    if (_binding!!.Sundayend.text.equals("Closed")){
                        this.requireView().showShortSnack("Please select proper timing")
                    }else{
                        saveInSharedPref()
                        LocalbizContainerActivity.openContainer(requireContext(), "businessTag","0")
                    }
                }else{
                    this.requireView().showShortSnack("Please Select Your Business Timing")
                }
            }




            // direct to review form
       //     LocalbizContainerActivity.openContainer(requireContext(), "FormReviewFragment")
        }

    }

    private fun saveInSharedPref() {
        getSharedPreference().saveString("mondaystart", mondaystart.text.toString())
        getSharedPreference().saveString("mondayend", mondayend.text.toString())
        getSharedPreference().saveString("tuesdaystart", tuesdaystart.text.toString())
        getSharedPreference().saveString("tuesdayend", tuesdayend.text.toString())
        getSharedPreference().saveString("Wednesdaystart", Wednesdaystart.text.toString())
        getSharedPreference().saveString("Wednesdayend", Wednesdayend.text.toString())
        getSharedPreference().saveString("Thursdaystart", Thursdaystart.text.toString())
        getSharedPreference().saveString("Thursdayend", Thursdayend.text.toString())
        getSharedPreference().saveString("Fridaystart", Fridaystart.text.toString())
        getSharedPreference().saveString("Fridayend", Fridayend.text.toString())
        getSharedPreference().saveString("Saturdaystart", Saturdaystart.text.toString())
        getSharedPreference().saveString("Saturdayend", Saturdayend.text.toString())
        getSharedPreference().saveString("Sundaystart", Sundaystart.text.toString())
        getSharedPreference().saveString("Sundayend", Sundayend.text.toString())
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BusinessTimingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}