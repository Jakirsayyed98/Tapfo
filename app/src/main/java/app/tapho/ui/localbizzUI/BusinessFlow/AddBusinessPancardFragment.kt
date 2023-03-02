package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessNameBinding
import app.tapho.databinding.FragmentAddBusinessPancardBinding
import app.tapho.ui.BaseFragment


class AddBusinessPancardFragment : BaseFragment<FragmentAddBusinessPancardBinding>() {

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
        _binding = FragmentAddBusinessPancardBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        if (!getSharedPreference().getString("BusinessPAN").isNullOrEmpty()){
            _binding!!.businesspannumber.setText(getSharedPreference().getString("BusinessPAN"))
        }
        statusBarTextWhite()
        statusBarColor(R.color.white)
        setClickEvent()
        return _binding!!.root
    }

    private fun setClickEvent() {
        _binding!!.businesspannumber.addTextChangedListener {
            if (_binding!!.businesspannumber.text!!.length >=1){
                _binding!!.btnVerify.isSelected = true
            } else {
                _binding!!.btnVerify.isSelected = false
            }
        }
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.btnVerify.setOnClickListener {
            if (activity?.intent?.getStringExtra("INPUT_TYPE").equals("1")){
                getSharedPreference().saveString("BusinessPANEdit",_binding!!.businesspannumber.text.toString())
                activity?. onBackPressedDispatcher?.onBackPressed()
            }else{
                getSharedPreference().saveString("BusinessPAN",_binding!!.businesspannumber.text.toString())
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
        }

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            AddBusinessPancardFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}