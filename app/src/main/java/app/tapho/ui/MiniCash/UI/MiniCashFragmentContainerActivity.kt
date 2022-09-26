package app.tapho.ui.MiniCash.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityMiniCashFragmentContainerBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.MiniCash.Fragments.MiniCashHomePageFragment
import app.tapho.ui.MiniCash.Fragments.MiniCashMiniAppListFragment
import app.tapho.utils.*
import com.google.gson.Gson

class MiniCashFragmentContainerActivity : BaseActivity<ActivityMiniCashFragmentContainerBinding>() {

    companion object {

        fun openContainer(
            context: Context?,
            type: String?,
        ) {
            context?.startActivity(Intent(context, MiniCashFragmentContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
            })
        }


        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, MiniCashFragmentContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY, type)
                putExtra(SET_OLD_DATA, isOldData)
                putExtra(TITLE, title)
                if (data is Bundle)
                    putExtras(data)
                else if (data != null)
                    putExtra(DATA, Gson().toJson(data))
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiniCashFragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val type = intent.getStringExtra(CONTAINER_TYPE_KEY)
        when (type) {
            "MiniCashHomePage" -> {
                addFragment(MiniCashHomePageFragment.newInstance())
            }
            "MiniCashStorePage" -> {
                addFragment(MiniCashMiniAppListFragment.newInstance())
            }
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}