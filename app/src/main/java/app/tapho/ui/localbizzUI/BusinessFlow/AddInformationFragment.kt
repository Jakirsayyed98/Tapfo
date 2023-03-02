package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.tapho.R
import app.tapho.databinding.FragmentAddInformationBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class AddInformationFragment : BaseFragment<FragmentAddInformationBinding>() {

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
        _binding = FragmentAddInformationBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        clickevents()
        _binding!!.saveBtn.isSelected = true
        return _binding!!.root
    }

    override fun onStart() {
        super.onStart()
     //   setAllTextData()
    }

    override fun onResume() {
        super.onResume()
        setAllTextData()
    }

    private fun setAllTextData() {
        _binding!!.businessTypeDescription.text =if ( getSharedPreference().getString("bussinessType").isNullOrEmpty()) "Select the domain category of your business" else getSharedPreference().getString("bussinessType")
        _binding!!.categoryName.text =if ( getSharedPreference().getString("category_name").isNullOrEmpty()) "Select your business category" else getSharedPreference().getString("category_name")
        _binding!!.storeNameData.text = if ( getSharedPreference().getString("BusinessName").isNullOrEmpty()) "Enter your business name" else getSharedPreference().getString("BusinessName")
        _binding!!.businessPancard.text = if ( getSharedPreference().getString("BusinessPAN").isNullOrEmpty()) "Enter your Pan number" else getSharedPreference().getString("BusinessPAN")
        _binding!!.GSTNNumber.text = if ( getSharedPreference().getString("BusinessGSTNNumber").isNullOrEmpty()) "Enter your business GSTN number" else getSharedPreference().getString("BusinessGSTNNumber")
        _binding!!.service.text = if (getSharedPreference().getString("service_name").isNullOrEmpty())"Select your business Sub Category" else getSharedPreference().getString("service_name").toString()
        _binding!!.tags.text =if (getSharedPreference().getString("businesstags").isNullOrEmpty()) "Select your business Tags" else getSharedPreference().getString("businesstags").toString()
        _binding!!.establisedYear.text =if (getSharedPreference().getString("ESTB_YEAR").isNullOrEmpty()) "Enter your business working since" else getSharedPreference().getString("ESTB_YEAR")

//        if (getSharedPreference().getString("ESTB_YEAR").isNullOrEmpty().not()){
//            _binding!!.radioComplete.isChecked = true
//        }else{
//            _binding!!.radioComplete.isChecked = false
//        }

    }

    private fun clickevents() {
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.businessType.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "business_type")
        }
        _binding!!.businessCategory.setOnClickListener {
            if (getSharedPreference().getString("BusinessName").isNullOrEmpty()){
                Snackbar.make(requireView()," Please enter Business name first ",Toast.LENGTH_SHORT).show()
            }else{
                LocalbizContainerActivity.openContainer(requireContext(), "category_name")
            }

        }
        _binding!!.businessServices.setOnClickListener {
            if (getSharedPreference().getString("category_name").isNullOrEmpty()){
                Snackbar.make(requireView()," Please enter Business Category first ",Toast.LENGTH_SHORT).show()
            }else {
                LocalbizContainerActivity.openContainer(requireContext(), "businessServices")
            }
        }
        _binding!!.businessTag.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "businessTag")
        }
        _binding!!.businessBusinessName.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "AddbusinessName","0")
        }
         _binding!!.businessPancardLay.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "AddBusinessPancardFragment","0")
        }

        _binding!!.saveBtn.setOnClickListener {
            if (getSharedPreference().getString("ESTB_YEAR").isNullOrEmpty()){
                Snackbar.make(requireView()," Please fill all the information ",Toast.LENGTH_SHORT).show()
            }else {
                LocalbizContainerActivity.openContainer(requireContext(), "businessProfile")
            }
        }
        _binding!!.businessEstbYear.setOnClickListener {
            if (getSharedPreference().getString("service_name").isNullOrEmpty()) {
                Snackbar.make(requireView(), " Please enter Business Sub Category first ", Toast.LENGTH_SHORT).show()
            } else {
                LocalbizContainerActivity.openContainer(requireContext(), "BusinessEstbYear")
            }
        }
        _binding!!.businessGSTIN.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "businessGSTINNumber","0")
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddInformationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}