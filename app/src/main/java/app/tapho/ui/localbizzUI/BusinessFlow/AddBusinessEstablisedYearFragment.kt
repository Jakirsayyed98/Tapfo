package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessEstablisedYearBinding
import app.tapho.ui.BaseFragment
import java.util.*


class AddBusinessEstablisedYearFragment : BaseFragment<FragmentAddBusinessEstablisedYearBinding>() {


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
        _binding = FragmentAddBusinessEstablisedYearBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        if (getSharedPreference().getString("ESTB_YEAR").isNullOrEmpty().not()){
            _binding!!.businessEstablisedYear.setText(getSharedPreference().getString("ESTB_YEAR"))
        }
        setTextAndClickData()
        return _binding?.root
    }

    private fun setTextAndClickData() {
        val currentYear = Calendar.YEAR
        Log.d("CalenderYearData",currentYear.toString())
        _binding!!.apply {
            businessEstablisedYear.addTextChangedListener {
                if (businessEstablisedYear.text!!.length ==4){
                    btnVerify.isSelected= true
                }
            }
            toolbar.backIv.setOnClickListener {
                activity?.onBackPressed()
            }
            btnVerify.setOnClickListener {
                if(btnVerify.isSelected==true){
                    getSharedPreference().saveString("ESTB_YEAR",businessEstablisedYear.text.toString())
                    activity?.onBackPressed()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddBusinessEstablisedYearFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}