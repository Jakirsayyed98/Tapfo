package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentBusinesDiscriptionBinding
import app.tapho.ui.BaseFragment


class businesDiscriptionFragment :BaseFragment<FragmentBusinesDiscriptionBinding>() {

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
        _binding = FragmentBusinesDiscriptionBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        setDescriptionData()
        return _binding?.root
    }

    private fun setDescriptionData() {
        if (getSharedPreference().getString("businessDescription").isNullOrEmpty().not()){
            _binding!!.busineeDescription.setText(getSharedPreference().getString("businessDescription"))
        }


        _binding!!.apply {
            busineeDescription.addTextChangedListener {
                if (busineeDescription.text!!.length >=1){
                    btnVerify.isSelected =true
                }else{
                    btnVerify.isSelected =false
                }
            }
            toolbar.backIv.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }

            btnVerify.setOnClickListener {
                if (btnVerify.isSelected==true){
                    if (activity?.intent?.getStringExtra("INPUT_TYPE").equals("1")){
                        getSharedPreference().saveString("businessDescriptionEdit",busineeDescription.text.toString())
                        activity?. onBackPressedDispatcher?.onBackPressed()
                    }else{
                        getSharedPreference().saveString("businessDescription",busineeDescription.text.toString())
                        activity?. onBackPressedDispatcher?.onBackPressed()
                    }

                }

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            businesDiscriptionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}