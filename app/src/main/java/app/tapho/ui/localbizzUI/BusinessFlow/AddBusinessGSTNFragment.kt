package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessGSTNBinding
import app.tapho.databinding.FragmentAddBusinessPancardBinding
import app.tapho.ui.BaseFragment


class AddBusinessGSTNFragment : BaseFragment<FragmentAddBusinessGSTNBinding>() {

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
        _binding = FragmentAddBusinessGSTNBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        setClickEvent()
        return _binding!!.root
    }

    private fun setClickEvent() {
        _binding!!.businessGSTNnumber.addTextChangedListener {
            if (_binding!!.businessGSTNnumber.text!!.length >=1){
                _binding!!.btnVerify.isSelected = true
            } else {
                _binding!!.btnVerify.isSelected = false
            }
        }
        if (!getSharedPreference().getString("BusinessGSTNNumber").isNullOrEmpty()){
            _binding!!.businessGSTNnumber.setText(getSharedPreference().getString("BusinessGSTNNumber"))
        }
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.btnVerify.setOnClickListener {

            if (activity?.intent?.getStringExtra("INPUT_TYPE").equals("1")){
                getSharedPreference().saveString("BusinessGSTNNumberEdit",_binding!!.businessGSTNnumber.text.toString())
                activity?. onBackPressedDispatcher?.onBackPressed()
            }else{
                getSharedPreference().saveString("BusinessGSTNNumber",_binding!!.businessGSTNnumber.text.toString())
                activity?. onBackPressedDispatcher?.onBackPressed()
            }


        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddBusinessGSTNFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}