package app.tapho.ui.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import app.tapho.BuildConfig
import app.tapho.R
import app.tapho.databinding.ActivityHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.network.BaseRes
import app.tapho.ui.BaseActivity
import app.tapho.ui.Cabs.CabsParentPageFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.News.NewsFragment.NewsHeadlineFragment
import app.tapho.ui.home.NewGame.GameHomeFragment
import app.tapho.ui.model.HomeRes
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.DATA
import app.tapho.utils.REACHED_HOME
import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.ktx.messaging

import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson


class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    var CHANNEL_ID = "Example_01"
    var notificationID = 101
    var backPressedTime: Long = 0



    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        getSharedPreference().saveBoolean(REACHED_HOME, true)
        setTab()
        tabClicked(binding.homeTab)

        updateFirebaseToken()

        addFragment(HomeTabFragment.newInstance())
//        addFragment(HomeFragment.newInstance())
//        addFragment(NewHomeFragment.newInstance())

    }


    private fun setTab() {
        binding.offerTab.setOnClickListener { tabClicked(it) }
        binding.Games.setOnClickListener { tabClicked(it) }
        binding.News.setOnClickListener { tabClicked(it) }
        binding.favouriteTab.setOnClickListener { tabClicked(it) }
        binding.homeTab.setOnClickListener { tabClicked(it) }
        binding.tCashTab.setOnClickListener { tabClicked(it) }
        binding.Cashback.setOnClickListener { tabClicked(it) }
        binding.ShopNow.setOnClickListener { tabClicked(it) }


    }

    fun tabClicked(view: View) {
        binding.offerTab.isSelected = false
        binding.Games.isSelected = false
        binding.News.isSelected = false
        binding.favouriteTab.isSelected = false
        binding.Cashback.isSelected = false
        binding.homeTab.isSelected = false
        binding.tCashTab.isSelected = false
        binding.ShopNow.isSelected = false
        binding.homeIv.isSelected = false
        view.isSelected = true
        if (view.id == R.id.homeTab)
            binding.homeIv.isSelected = true
        when (view) {
//            binding.homeTab -> addFragment(HomeFragment.newInstance())
//          binding.homeTab -> addFragment(NewHomeFragment.newInstance())
            binding.offerTab -> addFragment(OffersFragment.newInstance())
            binding.Games -> addFragment(GameHomeFragment.newInstance())
            binding.News -> addFragment(NewsHeadlineFragment.newInstance())
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack("BACK").commit()
    }

    fun addHome() {
        binding.homeTab.performClick()
    }


    private fun updateFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Token ID :-  d8F1TQ_rQCqJ_kY9ELyd4M:APA91bH_yeiKZdAaVYSjnNhe3tDrzX4nwZal-FSzSD0d4uASMpq191G-m8ObnH9Tj8tNiw4wQ6dEQANIlSkKiffGU9KyOzeWndoGe4MAC_I-QmJsS0aBGsdO5BwqBTd-FU1mgtAljE8k
            // Get new FCM registration token
            val token = task.result

            Log.e("Token",token)
            viewModel.updateNotification(getUserId(), token, this,
                object : ApiListener<BaseRes, Any?> {
                    override fun onSuccess(t: BaseRes?, mess: String?) {
                        t.let {

                        }
                    }
                })
        })
    }


    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.getOnBackPressedDispatcher().onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }


}