package app.tapho.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import app.tapho.R
import app.tapho.databinding.FragmentProfileBaseBinding
import app.tapho.ui.BaseFragment

class ProfileBaseFragment : BaseFragment<FragmentProfileBaseBinding>() {
    private var  navController:NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment= childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
//        navController.navigate(R.id.action_profileDetailFragment_to_editProfileFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileBaseFragment()
    }
}