package app.tapho.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import app.tapho.BuildConfig
import app.tapho.CamelCaseValue
import app.tapho.R
import app.tapho.databinding.FragmentWelcomebackscreenBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.model.AppCategory
import app.tapho.utils.AppSharedPreference
import com.google.gson.Gson
import java.util.*

class welcomebackscreenFragment :BaseFragment<FragmentWelcomebackscreenBinding>(){// DialogFragment() {
//        var _binding : FragmentWelcomebackscreenBinding?=null
    var handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomebackscreenBinding.inflate(inflater,container,false)
        statusBarTextWhite()
//        dialog?.window?.statusBarColor = Color.BLACK
        val name = getSharedPreference().getLoginData()!!.name.toString()
        _binding!!.welcomename.text ="Hi "+ requireContext().CamelCaseValue(name).replaceAfter(" ","")+","
        _binding!!.appversion.text = BuildConfig.VERSION_NAME

        handler.postDelayed({
            kotlin.runCatching {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                activity?.finish()
//                dialog!!.dismiss()
            }
        },1000)


        return _binding?.root
    }

//    fun getSharedPreference(): AppSharedPreference {
//        return AppSharedPreference.getInstance(requireContext())
//    }

    companion object {
        fun newInstance(tabs: ArrayList<AppCategory>?): welcomebackscreenFragment {
            val args = Bundle()
            args.putString("TABS", Gson().toJson(tabs))

            val fragment = welcomebackscreenFragment()
            fragment.arguments = args
            return fragment
        }
    }
}