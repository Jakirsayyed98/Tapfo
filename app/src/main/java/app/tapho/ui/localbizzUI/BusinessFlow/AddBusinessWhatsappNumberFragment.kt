package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessWhatsappNumberBinding
import app.tapho.ui.BaseFragment


class AddBusinessWhatsappNumberFragment : BaseFragment<FragmentAddBusinessWhatsappNumberBinding>() {


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
        _binding =FragmentAddBusinessWhatsappNumberBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        setTextAndClickData()
        if (getSharedPreference().getString("business_whatsapp_number").isNullOrEmpty().not()){
            _binding!!.businessWhatsappNumber.setText(getSharedPreference().getString("business_whatsapp_number"))
        }
        return _binding?.root
    }

    private fun setTextAndClickData() {
        _binding!!.apply {
            businessWhatsappNumber.addTextChangedListener {
                if (businessWhatsappNumber.text!!.length == 10){
                     btnVerify.isSelected = true
                }
            }
            toolbar.backIv.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
            btnVerify.setOnClickListener {
                if (btnVerify.isSelected == true){
                    if (activity?.intent?.getStringExtra("INPUT_TYPE").equals("1")){
                        getSharedPreference().saveString("business_whatsapp_numberEdit",businessWhatsappNumber.text.toString())
                        activity?. onBackPressedDispatcher?.onBackPressed()
                    }else{
                        getSharedPreference().saveString("business_whatsapp_number",businessWhatsappNumber.text.toString())
                        activity?. onBackPressedDispatcher?.onBackPressed()
                    }



                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddBusinessWhatsappNumberFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}