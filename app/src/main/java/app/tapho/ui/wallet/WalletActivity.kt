package app.tapho.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.ActivityWalletBinding
import app.tapho.ui.BaseActivity
import app.tapho.ui.payment.PaymentActivity
import app.tapho.ui.wallet.adapter.PopularDenominationAdapter
import app.tapho.ui.wallet.model.DenominationModel
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class WalletActivity : BaseActivity<ActivityWalletBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.rechargeNowTv.setOnClickListener {
            WalletOfferDialogFragment.newInstance().show(supportFragmentManager, "")
        }

        binding.toolbar.tvTitle.text = getString(R.string.twallet)
        binding.toolbar.backIv.setOnClickListener { onBackPressed() }
        binding.recyclerPopularDenomination.apply {
            layoutManager =
                LinearLayoutManager(this@WalletActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = PopularDenominationAdapter().apply {
                addItem(DenominationModel(500, false))
                addItem(DenominationModel(1000, false))
                addItem(DenominationModel(3000, false))
                addItem(DenominationModel(5000, false))
                addItem(DenominationModel(9000, false))
            }
        }
        keypadListener()
        binding.payRe.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
    }

    private fun keypadListener() {
        setEventListener(
            this,
            KeyboardVisibilityEventListener {
                binding.bottomLi.visibility = if (it) View.GONE else View.VISIBLE
            })
    }
}