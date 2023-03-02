package app.tapho.ui.home.NewGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import app.tapho.R
import app.tapho.databinding.FragmentGamesOneTimeSplashBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity


class GamesOneTimeSplashFragment : BaseFragment<FragmentGamesOneTimeSplashBinding>(){


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
        _binding = FragmentGamesOneTimeSplashBinding.inflate(inflater,container,false)
        activity?.window?.addFlags(  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            //WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        _binding!!.btnContinue.setOnClickListener {
            if (getSharedPreference().getString("gameIntro").isNullOrEmpty()){
                getSharedPreference().saveString("gameIntro","1")
                ContainerActivity.openContainer(requireContext(), "Games", "")
                activity?.finish()
            }else{

            }

        }
        return _binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GamesOneTimeSplashFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}