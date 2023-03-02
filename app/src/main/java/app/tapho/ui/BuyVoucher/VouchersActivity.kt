package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityVouchers2Binding
import app.tapho.ui.BaseActivity
import app.tapho.ui.home.BuyGiftCardFragment

class VouchersActivity : BaseActivity<ActivityVouchers2Binding>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVouchers2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        setTab()
        tabClicked(binding.homeTab)
        addFragment(VoucherHomeScreenFragment.newInstance())
    }



    private fun setTab() {
        binding.offerTab.setOnClickListener { tabClicked(it) }
        binding.favouriteTab.setOnClickListener { tabClicked(it) }
        binding.homeTab.setOnClickListener { tabClicked(it) }
        binding.tCashTab.setOnClickListener { tabClicked(it) }
        binding.PurchasedTab.setOnClickListener { tabClicked(it) }

    }

    fun tabClicked(view: View) {
        binding.offerTab.isSelected = false
        binding.favouriteTab.isSelected = false
        binding.homeTab.isSelected = false
        binding.PurchasedTab.isSelected = false
        binding.tCashTab.isSelected = false
        binding.homeIv.isSelected = false
        view.isSelected = true
        if (view.id == R.id.homeTab)
            binding.homeIv.isSelected = true
        when (view) {
            binding.homeTab -> addFragment(VoucherHomeScreenFragment.newInstance())
            binding.tCashTab -> addFragment(VoucherPurchasedHistoryFragment.newInstance())
            binding.offerTab -> addFragment(BuyGiftCardFragment.newInstance())

            binding.PurchasedTab -> addFragment(PurchasedVouchersFragment.newInstance())

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun addFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack("BACK").commit()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

    }

    fun addHome() {
        binding.homeTab.performClick()
    }



}