package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityVouchers2Binding
import app.tapho.ui.BaseActivity
import app.tapho.ui.tcash.Fragment_Tcash_HistoryData

class VouchersActivity : BaseActivity<ActivityVouchers2Binding>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_vouchers2)
        binding = ActivityVouchers2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        setTab()
        tabClicked(binding.homeTab)
        addFragment(VoucherHomeScreenFragment.newInstance())
    }


    private fun setTab() {
        binding.homeTab.setOnClickListener { tabClicked(it) }
        binding.History.setOnClickListener { tabClicked(it) }
    }

    fun tabClicked(view: View) {
        binding.homeTab.isSelected = false
        binding.homeIv.isSelected = false
        binding.History.isSelected = false
        binding.historyiv.isSelected = false
        view.isSelected = true
        if (view.id == R.id.homeTab)
            binding.homeIv.isSelected = true
        when (view) {
            binding.homeTab -> addFragment(VoucherHomeScreenFragment.newInstance())
            binding.History -> addFragment(Fragment_Tcash_HistoryData.newInstance())
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack("BACK").commit()
    }

    fun addHome() {
        binding.homeTab.performClick()
    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//    }


}