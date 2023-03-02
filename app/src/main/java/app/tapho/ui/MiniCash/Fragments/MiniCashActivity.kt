package app.tapho.ui.MiniCash.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityMiniCashBinding
import app.tapho.ui.BaseActivity
class MiniCashActivity : BaseActivity<ActivityMiniCashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiniCashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addFragment(MiniCashHomePageFragment.newInstance())

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}