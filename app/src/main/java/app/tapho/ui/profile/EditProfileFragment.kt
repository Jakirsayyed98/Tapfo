package app.tapho.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import app.tapho.R
import app.tapho.databinding.FragmentEditProfileBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.ui.BaseFragment
import app.tapho.ui.login.model.LoginData
import app.tapho.ui.login.model.LoginRes
import app.tapho.utils.LOGIN_DATA
import app.tapho.utils.RealPathUtil
import app.tapho.utils.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import java.text.SimpleDateFormat
import java.util.*


class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(){
    private var loginData: LoginData? = null
    private var image: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        statusBarTextBlack()
        statusBarColor(R.color.black)
        _binding!!.btnUpdateProfile.isSelected = true
        init()
        return _binding?.root
    }

    private fun init() {
       // binding.toolbar.tvTitle.text = getString(R.string.update_profile)
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.birthdayEt.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(),
                { p0, p1, p2, p3 ->
                    calendar.set(Calendar.YEAR, p1)
                    calendar.set(Calendar.MONTH, p2)
                    calendar.set(Calendar.DAY_OF_MONTH, p3)
                    binding.birthdayEt.setText(SimpleDateFormat("dd-MM-yyyy",
                        Locale.ENGLISH).format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        loginData = getSharedPreference().getLoginData()
        binding.nameEt.setText(loginData?.name)
        binding.emailEt.setText(loginData?.email)
        binding.birthdayEt.text = loginData?.dob
        if (loginData?.gender.isNullOrEmpty().not())
            binding.rgGender.check(if (loginData?.gender == "1") R.id.maleRb else R.id.femaleRb)
        setImage(loginData?.image)

        binding.btnUpdateProfile.setOnClickListener { updateProfile() }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }


    private fun setImage(image: String?) {
//        Glide.with(this).load(image).apply(
//            RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon)
//        ).into(binding.profileIv)

//        if (loginData!!.image.isNullOrEmpty()) {
//            _binding!!.profileName.visibility = View.VISIBLE
//            _binding!!.profileIv.visibility = View.GONE
//            _binding!!.profileName.text = loginData!!.name
//
//        } else {
//            _binding!!.profileName.visibility = View.GONE
//            Glide.with(this).load(loginData!!.image).apply(
//                RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon)
//            ).into(_binding!!.profileIv)
//        }
    }

    private fun updateProfile() {
        if (binding.nameEt.text!!.isEmpty()) {
         //   showMess("Please enter name")
            Toast.makeText(requireContext(),"Please enter name",Toast.LENGTH_SHORT).show()
        } else if (binding.emailEt.text!!.isEmpty()) {
            Toast.makeText(requireContext(),"Please enter email address",Toast.LENGTH_SHORT).show()
        }  else{
              UpdateProfile()
        }
    }

    private fun UpdateProfile() {
        viewModel.updateProfile2(getUserId(), binding.emailEt.text.toString(), binding.nameEt.text.toString(), "1", "",  this, object : ApiListener<LoginRes,Any?>{
            override fun onSuccess(t: LoginRes?, mess: String?) {
                    t!!.data?.let {
                        getSharedPreference().saveString(LOGIN_DATA, Gson().toJson(it[0]))
                        activity?.onBackPressedDispatcher?.onBackPressed()
                    }

            }

            override fun onError(mess: String?) {
                super.onError(mess)
                Toast.makeText(requireContext(),mess.toString(),Toast.LENGTH_SHORT).show()
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        })

    }

}