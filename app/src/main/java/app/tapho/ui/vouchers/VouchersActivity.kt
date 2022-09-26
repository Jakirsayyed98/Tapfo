package app.tapho.ui.vouchers

import android.os.Bundle
import app.tapho.R
import app.tapho.databinding.ActivityVouchersBinding
import app.tapho.ui.BaseActivity

class VouchersActivity : BaseActivity<ActivityVouchersBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityVouchersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}