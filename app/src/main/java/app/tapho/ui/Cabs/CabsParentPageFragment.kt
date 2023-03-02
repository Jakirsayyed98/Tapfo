package app.tapho.ui.Cabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentCabsParentPageBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.utils.getCustomTab2
import com.google.android.material.tabs.TabLayoutMediator


class CabsParentPageFragment : BaseFragment<FragmentCabsParentPageBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCabsParentPageBinding.inflate(inflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()
        init()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        return _binding?.root
    }

    private fun init() {
        val mAdapter = PagerFragmentAdapter(this)
        mAdapter.addFragment(OlaCabsFragment.newInstance(),"Ola")
        mAdapter.addFragment(UberCabsFragment.newInstance(), "Uber")
        binding.viewPager.adapter = mAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.customView = getCustomTab2(context, mAdapter.getTitle(pos))
        }.attach()

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CabsParentPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}