package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddbusinessPhoneNumberBinding
import app.tapho.ui.BaseFragment
import com.google.android.material.snackbar.Snackbar


class AddbusinessPhoneNumberFragment : BaseFragment<FragmentAddbusinessPhoneNumberBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddbusinessPhoneNumberBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        if (!getSharedPreference().getString("secondary_number").isNullOrEmpty()){
            _binding!!.secondaryNumber.setText(getSharedPreference().getString("secondary_number"))
        }
        if (!getSharedPreference().getString("optional_number").isNullOrEmpty()) {
            _binding!!.optionalNumber.setText(getSharedPreference().getString("optional_number"))
        }

        setAndEditText()
        return _binding?.root
    }

    private fun setAndEditText() {
        _binding!!.apply {
            primaryNumber.setText(getSharedPreference().getLoginData()!!.mobile_no)
            primaryNumber.isClickable = false

            secondaryNumber.addTextChangedListener {
                if (secondaryNumber.text!!.length==10){
                    btnVerify.isSelected = true
                }else{
                    btnVerify.isSelected = false
                }
            }
            optionalNumber.addTextChangedListener {
                if (optionalNumber.text!!.length==10){
                    btnVerify.isSelected = true
                }else{
                    Snackbar.make(requireView(),"Please Enter a valid number",Snackbar.LENGTH_SHORT).show()
                    btnVerify.isSelected = false
                }
            }
            toolbar.backIv.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
            btnVerify.setOnClickListener {
                if (secondaryNumber.text!!.length<10){
                    secondaryNumber.setError("Please Enter proper mobile Number")
                }else{
                    if (activity?.intent?.getStringExtra("INPUT_TYPE").equals("1")){
                        getSharedPreference().saveString("secondary_numberEdit",secondaryNumber.text.toString())
                        getSharedPreference().saveString("optional_numberEdit",optionalNumber.text.toString())
                        activity?. onBackPressedDispatcher?.onBackPressed()
                    }else{
                        getSharedPreference().saveString("secondary_number",secondaryNumber.text.toString())
                        getSharedPreference().saveString("optional_number",optionalNumber.text.toString())
                        activity?. onBackPressedDispatcher?.onBackPressed()
                    }



                }
            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddbusinessPhoneNumberFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}