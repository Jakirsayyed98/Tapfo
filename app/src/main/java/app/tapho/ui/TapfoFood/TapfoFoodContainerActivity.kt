package app.tapho.ui.TapfoFood

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.tapho.R
import app.tapho.databinding.ActivityTapfoFoodContainerBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.TapfoFood.UserUI.*
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.DATA
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson

class TapfoFoodContainerActivity : BaseActivity<ActivityTapfoFoodContainerBinding>() {
    companion object{
        fun openContainer(
            context : Context,
            type :  String,
            data : Any?,
        ){
            context.startActivity(Intent(context,TapfoFoodContainerActivity::class.java).apply {
                putExtra(CONTAINER_TYPE_KEY,type)
                if (data is Bundle){
                    putExtras(data)
                }else if (data != null){
                    putExtra(DATA,Gson().toJson(data))
                }
            })
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTapfoFoodContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        val type = intent?.getStringExtra(CONTAINER_TYPE_KEY)

        when(type){
            "TapfoFoodLocationFragment"->{
                addFragment(TapfoFoodLocationFragment.newInstance())
            }
            "TapfoFoodHomeFragment"->{
                addFragment(TapfoFoodHomeFragment.newInstance())
            }
            "RestaurantDetailFragment"->{
                addFragment(RestaurantDetailFragment.newInstance())
            }
            "TapfoFoodCartFragment"->{
                addFragment(TapfoFoodCartFragment.newInstance())
            }
            "OrderPlacedPreviewFragment"->{
                addFragment(OrderPlacedPreviewFragment.newInstance())
            }
        }

    }

    private fun addFragment(myFragments: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container,myFragments).commit()
    }
}