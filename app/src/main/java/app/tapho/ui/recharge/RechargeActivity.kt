package app.tapho.ui.recharge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.ActivityRechargeBinding
import app.tapho.ui.BaseActivity

class RechargeActivity : BaseActivity<ActivityRechargeBinding>() {
    companion object {
        fun openRechargeService(
            context: Context?, ) { context?.startActivity(Intent(context, RechargeActivity::class.java).apply {})
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRechargeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.toolbar.backIv.setOnClickListener {
            onBackPressed()
        }
    }

    fun setRTitle(title:String?){
        binding.toolbar.tvTitle.text=title
    }
}