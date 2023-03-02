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
import kotlinx.android.synthetic.main.fragment_add_business_establised_year.*
import java.lang.reflect.Field
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
        _binding!!.btnVerify.isSelected= true

        setTextAndClickData()
        return _binding?.root
    }

    private fun setTextAndClickData() {
        val currentYear = Calendar.YEAR
        _binding!!.apply {
            businessEstablisedYear.addTextChangedListener {
                if (businessEstablisedYear.text!!.length ==4){
                    btnVerify.isSelected= true
                }
            }
            toolbar.backIv.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }


            btnVerify.setOnClickListener {
                if(btnVerify.isSelected==true){
                    dateyear.year.let {
                        getSharedPreference().saveString("ESTB_YEAR",it.toString())
                        activity?. onBackPressedDispatcher?.onBackPressed()
                    }
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