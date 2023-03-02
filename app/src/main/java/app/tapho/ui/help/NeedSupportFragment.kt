package app.tapho.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentNeedSupportBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.showShort
import app.tapho.ui.BaseFragment
import app.tapho.ui.help.model.CustomeServicelist
import app.tapho.utils.DATA
import app.tapho.utils.customToastForSupport
import com.bumptech.glide.Glide
import com.google.gson.Gson

class NeedSupportFragment : BaseFragment<FragmentNeedSupportBinding>(), ApiListener<BaseRes, Any?> {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNeedSupportBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        getData()

        return _binding?.root
    }

    private fun getData() {
        activity?.intent?.getStringExtra(DATA).let {
            val data = Gson().fromJson(it, CustomeServicelist::class.java)
            setData(data)
        }
    }

    private fun setData(data: CustomeServicelist?) {

        _binding!!.title.text = data!!.title
        _binding!!.discription.text = data.discription
        Glide.with(requireContext()).load(data.image).placeholder(R.drawable.loding_app_icon).into(_binding!!.image)
        _binding!!.message.addTextChangedListener {
            if (_binding!!.message.text.toString().isNullOrEmpty().not()){
                _binding!!.btnVerify.isSelected = true
                _binding!!.btnVerify.isClickable = true
            }else{
                _binding!!.btnVerify.isSelected = false
                _binding!!.btnVerify.isClickable = false
            }
        }

        _binding!!.btnVerify.setOnClickListener {
            if ( _binding!!.message.text.toString().isNullOrEmpty().not()){
                support(data)
            }else{
                context?.showShort("Please enter some message")
            }

        }

         _binding!!.backbtn.setOnClickListener {
             activity?. onBackPressedDispatcher?.onBackPressed()
        }

    }

    private fun support(data: CustomeServicelist) {
        val mobilenumber = getSharedPreference().getLoginData()!!.mobile_no
        viewModel.support(getSharedPreference().getUserId(), data.title, _binding!!.message.text.toString(), mobilenumber, data.serviceType, this, this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NeedSupportFragment().apply {

            }
    }

    override fun onSuccess(t: BaseRes?, mess: String?) {
        context?.customToastForSupport("successfully submitted.",false)
        activity?. onBackPressedDispatcher?.onBackPressed()
    }
}