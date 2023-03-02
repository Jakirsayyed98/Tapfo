package app.tapho.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentOffersBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.BannerAdapter
import app.tapho.ui.home.adapter.PagerFragmentAdapter
import app.tapho.ui.merchants.MerchantOfferFragment
import app.tapho.ui.model.BannerList
import app.tapho.utils.getCustomTab
import com.google.android.material.tabs.TabLayoutMediator


class OffersFragment : BaseFragment<FragmentOffersBinding>() {
    private val bannerAdapter=BannerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOffersBinding.inflate(inflater, container, false)
        activity?.window?.statusBarColor=Color.WHITE
        statusBarTextWhite()
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        init()
        return _binding?.root
    }

    private fun init() {
        binding.banner1.adapter=bannerAdapter

        val mAdapter = PagerFragmentAdapter(this)
        mAdapter.addFragment(CategoriesFragment.newInstance(), getString(R.string.categories))
        mAdapter.addFragment(MerchantOfferFragment.newInstance(), getString(R.string.merchant_deals))
        binding.recycler.adapter = mAdapter
        TabLayoutMediator(binding.tabLayout, binding.recycler) { tab, pos ->
            tab.customView = getCustomTab(context, mAdapter.getTitle(pos))
            _binding!!.progress.visibility = View.GONE
            _binding!!.mainLayout.visibility = View.VISIBLE
        }.attach()

    }

    fun setBannerData(list: List<BannerList>){


        kotlin.runCatching {

            bannerAdapter.clear()
            bannerAdapter.addAllData(list)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OffersFragment()
    }
}