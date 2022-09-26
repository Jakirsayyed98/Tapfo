package app.tapho.ui.home.CabsContainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.FragmentCabsBinding
import app.tapho.databinding.FragmentNewHomeBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.GamesFavouriteFragment
import app.tapho.ui.home.GamesForYouFragment
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.utils.getCustomTab
import com.google.android.material.tabs.TabLayoutMediator

class CabsFragment : BaseFragment<FragmentCabsBinding>() {
    private var madapter: PagerFragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cabsBinding = FragmentCabsBinding.inflate(inflater, container, false)
        setCashbackMerchantsPager()
        return cabsBinding?.root
    }

    private fun setCashbackMerchantsPager() {
        madapter = PagerFragmentAdapter(this)
        cabsBinding!!.pagerCashbackMerchantsFragment.adapter = madapter
        madapter!!.addFragment(OlaFragment.newInstance(), getString(R.string.Ola))
        madapter!!.addFragment(UberFragment.newInstance(), getString(R.string.Uber))
        //bindingForGames!!.tabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_on_primary))
        cabsBinding!!.tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.white), ContextCompat.getColor(requireContext(), R.color.white))
        TabLayoutMediator(
            cabsBinding!!.tabLayout,
            cabsBinding!!.pagerCashbackMerchantsFragment
        ) { tab, position ->
            tab.customView = getCustomTab(context, madapter?.getTitle(position))
        }.attach()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CabsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}