package app.tapho.ui.electro

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.palette.graphics.Palette
import app.tapho.R
import app.tapho.databinding.ActivityContainerBinding
import app.tapho.databinding.ActivityElectroBinding
import app.tapho.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_electro.*

class ElectroActivity : BaseActivity<ActivityElectroBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElectroBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val toolbar: Toolbar = findViewById(R.id.toolBarLayout)
//        setSupportActionBar(toolbar)
      //  supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.heart_icon_green)
        Palette.from(bitmap).generate { palette ->
            if (palette != null){
                collapsingToolbar.setContentScrimColor(palette.getVibrantColor(R.attr.colorPrimary))
            }
        }

    }
}