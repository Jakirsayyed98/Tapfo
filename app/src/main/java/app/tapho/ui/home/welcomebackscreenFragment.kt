package app.tapho.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.BuildConfig
import app.tapho.CamelCaseValue
import app.tapho.R
import app.tapho.databinding.FragmentWelcomebackscreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.MiniCash.Fragments.MiniCashActivity
import app.tapho.ui.model.HomeRes
import app.tapho.utils.DATA
import app.tapho.utils.parseDate4
import java.util.*

class welcomebackscreenFragment : BaseFragment<FragmentWelcomebackscreenBinding>() {

    var handler = Handler(Looper.getMainLooper())
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
        _binding = FragmentWelcomebackscreenBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        val name = getSharedPreference().getLoginData()!!.name.toString()
        _binding!!.welcomename.text ="Hi "+ requireContext().CamelCaseValue(name).replaceAfter(" ","")+","

        _binding!!.appversion.text = BuildConfig.VERSION_NAME



        handler.postDelayed({
            kotlin.runCatching {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                activity?.finish()
            }
        },1000)


        return _binding?.root
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            welcomebackscreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}