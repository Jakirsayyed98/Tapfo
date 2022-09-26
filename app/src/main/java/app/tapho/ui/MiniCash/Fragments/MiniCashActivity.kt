package app.tapho.ui.MiniCash.Fragments

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.tapho.NavSheet.Fragment_favorite_nav
import app.tapho.R
import app.tapho.databinding.ActivityHomeBinding
import app.tapho.databinding.ActivityMiniCashBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.NewNotificationService.AllNotificationUI.NotificationFragment
import app.tapho.ui.NewNotificationService.UINotificationWithCategory.NewNotificationFragment
import app.tapho.ui.News.NewsFragment.NewsHeadlineFragment
import app.tapho.ui.favourite.FavouriteFragment
import app.tapho.ui.home.*
import app.tapho.ui.home.NewGame.GameHomeFragment
import app.tapho.ui.tcash.Fragment_Tcash_HistoryData
import app.tapho.ui.tcash.TCashDashboardFragment

class MiniCashActivity : BaseActivity<ActivityMiniCashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiniCashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addFragment(MiniCashHomePageFragment.newInstance())
        setTab()
        tabClicked(binding.homeTab)
    }

    private fun setTab() {
        binding.offerTab.setOnClickListener { tabClicked(it) }
        binding.favouriteTab.setOnClickListener { tabClicked(it) }
        binding.homeTab.setOnClickListener { tabClicked(it) }
        binding.tCashTab.setOnClickListener { tabClicked(it) }
        binding.notification.setOnClickListener { tabClicked(it) }

    }

    fun tabClicked(view: View) {
        binding.offerTab.isSelected = false
        binding.favouriteTab.isSelected = false
        binding.homeTab.isSelected = false
        binding.notification.isSelected = false
        binding.tCashTab.isSelected = false
        binding.homeIv.isSelected = false
        view.isSelected = true
        if (view.id == R.id.homeTab)
            binding.homeIv.isSelected = true
        when (view) {
            binding.homeTab -> addFragment(MiniCashHomePageFragment.newInstance())
            binding.tCashTab -> addFragment(Fragment_Tcash_HistoryData.newInstance())
            binding.offerTab -> addFragment(BuyGiftCardFragment.newInstance())
            binding.favouriteTab -> addFragment(Fragment_favorite_nav.newInstance())
            binding.notification -> addFragment(NotificationFragment.newInstance())

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack("BACK").commit()
    }
}