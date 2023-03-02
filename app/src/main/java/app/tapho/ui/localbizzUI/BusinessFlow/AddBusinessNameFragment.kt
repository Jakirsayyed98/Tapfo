package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessNameBinding
import app.tapho.ui.BaseFragment

class AddBusinessNameFragment : BaseFragment<FragmentAddBusinessNameBinding>() {

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
        _binding = FragmentAddBusinessNameBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Add Business Info"
        if (!getSharedPreference().getString("BusinessName").isNullOrEmpty()){
            _binding!!.StoreName.setText(getSharedPreference().getString("BusinessName"))
        }
        statusBarTextWhite()
        statusBarColor(R.color.white)
        setClickEvent()
        return _binding!!.root
    }

    private fun setClickEvent() {
        _binding!!.StoreName.addTextChangedListener {
            if (_binding!!.StoreName.text!!.length >=1){
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
                getSharedPreference().saveString("BusinessNameEDIT",_binding!!.StoreName.text.toString())
                activity?. onBackPressedDispatcher?.onBackPressed()
            }else{
                getSharedPreference().saveString("BusinessName",_binding!!.StoreName.text.toString())
                activity?. onBackPressedDispatcher?.onBackPressed()
            }
        }

    }

   companion object {
     @JvmStatic
     fun newInstance() =
         AddBusinessNameFragment().apply {
            arguments = Bundle().apply {

            }
         }
   }
}