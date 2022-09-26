package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentAddBusinessEmailBinding
import app.tapho.ui.BaseFragment
import app.tapho.utils.isValidEmail
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_business_name.*


class AddBusinessEmailFragment : BaseFragment<FragmentAddBusinessEmailBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBusinessEmailBinding.inflate(inflater,container,false)
        _binding!!.toolbar.tvTitle.text="Listing Business"
        statusBarTextWhite()
        statusBarColor(R.color.white)
        if (getSharedPreference().getString("businessEmail").isNullOrEmpty().not()){
            _binding!!.businessEmail.setText(getSharedPreference().getString("businessEmail"))

        }
        setClickEvent()
        return _binding?.root
    }

    private fun setClickEvent() {
        _binding!!.apply {
            businessEmail.addTextChangedListener {
                if (businessEmail.text!!.length>=1){
                    btnVerify.isSelected=true
                }
            }
            toolbar.backIv.setOnClickListener {
                activity?.onBackPressed()
            }
            btnVerify.setOnClickListener {
                if (btnVerify.isSelected==true){
                    if (businessEmail.text.toString().isValidEmail()){
                        getSharedPreference().saveString("businessEmail",businessEmail.text.toString())
                        activity?.onBackPressed()
                    }else{
                        Snackbar.make(requireView(),"Please enter valid Email Address " ,Snackbar.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddBusinessEmailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}