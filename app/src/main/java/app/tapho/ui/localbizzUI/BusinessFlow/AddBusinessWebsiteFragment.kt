package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessWebsiteBinding
import app.tapho.ui.BaseFragment


class AddBusinessWebsiteFragment : BaseFragment<FragmentAddBusinessWebsiteBinding>(){


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
        _binding = FragmentAddBusinessWebsiteBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        if (getSharedPreference().getString("businesswebsite").isNullOrEmpty().not()){
            _binding!!.website.setText(getSharedPreference().getString("businesswebsite"))
        }
        statusBarTextWhite()
        statusBarColor(R.color.white)
        setTextAndClickData()
        return _binding?.root
    }

    private fun setTextAndClickData() {
        _binding!!.apply {
            website.addTextChangedListener {
                if (website.text.toString().length >=1){
                    btnVerify.isSelected =true
                }
            }
            toolbar.backIv.setOnClickListener {
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
            btnVerify.setOnClickListener {
                if (btnVerify.isSelected ==true){
                    getSharedPreference().saveString("businesswebsite",website.text.toString())
                    activity?. onBackPressedDispatcher?.onBackPressed()
                }
            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddBusinessWebsiteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}