package app.tapho.ui.scanner.ScanCart

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityContainerBinding
import app.tapho.databinding.ActivityContainerForProductBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.BuyGiftCardFragment
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.DATA
import app.tapho.utils.SET_OLD_DATA
import app.tapho.utils.TITLE
import com.google.gson.Gson

class ContainerForProductActivity : BaseActivity<ActivityContainerForProductBinding>(){

    companion object{
        fun openContainer(
            context: Context?,
            type: String?,
            data: Any?,
            isOldData: Boolean,
            title: String?,
        ) {
            context?.startActivity(Intent(context, ContainerForProductActivity::class.java).apply {
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
        binding = ActivityContainerForProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.status_bar)
        window.statusBarColor = Color.WHITE
        init()
    }

    private fun init() {

        val type = intent?.getStringExtra(CONTAINER_TYPE_KEY)

        when(type){
            "ProductCartFragment"->{
                addFragment(ProductCartFragment.newInstance())
            }

            "StoreNameDialogFragment"->{
                addFragment(StoreNameDialogFragment.newInstance())
            }
            "SelectPaymentmodeFragment"->{
                addFragment(SelectPaymentmodeFragment.newInstance())
            }
            "TapMartCheckOutFragment"->{
                addFragment(TapMartCheckOutFragment.newInstance())
            }
            else->{
                addFragment(BuyGiftCardFragment.newInstance())
            }
        }

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

    }
}