package app.tapho.ui

//import androidx.databinding.tool.writer.ViewBinding


import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.network.RequestViewModel
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.toast


abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(), LoaderListener {
    lateinit var viewModel: RequestViewModel
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)

    }

    fun belowStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }


    fun statusBarTextWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    fun statusBarTextblack() {
        theme?.applyStyle(R.style.TextViewBold, true)
    }
    fun statusBarColor(color:Int) {
        window.statusBarColor = ContextCompat.getColor(this,color)

    }

    override fun showLoader() {
      LoaderFragment.showLoader(supportFragmentManager)

    }

    override fun dismissLoader() {
        LoaderFragment.dismissLoader(supportFragmentManager)
    }

    override fun showMess(mess: String?) {
        toast(mess)
    }

    fun getUserId(): String {
        getSharedPreference().getLoginData()?.let {
            it.id?.let { it1 ->
                return it1
            }
        }
        return ""
    }

    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(this)
    }

}