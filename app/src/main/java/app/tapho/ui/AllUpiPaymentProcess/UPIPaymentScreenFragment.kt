package app.tapho.ui.AllUpiPaymentProcess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentUPIPaymentScreenBinding
import app.tapho.ui.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class UPIPaymentScreenFragment : BaseFragment<FragmentUPIPaymentScreenBinding>(){

    var UPIID = ""
    var isSelected=false
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
        _binding = FragmentUPIPaymentScreenBinding.inflate(inflater,container,false)
        UPIID = activity?.intent?.getStringExtra("UPIID").toString()
        setTextForData()
        return _binding?.root
    }

    private fun setTextForData() {


        _binding!!.UPIID.text = UPIID
        _binding!!.proceedToPayment.isSelected=false
        _binding!!.Amount.addTextChangedListener {
            if (_binding!!.Amount.text!!.length >= 1) {
                _binding!!.proceedToPayment.isClickable=true
                _binding!!.proceedToPayment.isSelected=true
            }else{
                _binding!!.proceedToPayment.isClickable=false
                _binding!!.proceedToPayment.isSelected=false
            }
        }

        if (getSharedPreference().getLoginData()?.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = UPIID

        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(getSharedPreference().getLoginData()?.image).apply(
                RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
            ).into(_binding!!.profileIv)
        }

        _binding!!.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UPIPaymentScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}