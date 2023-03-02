package app.tapho.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import app.tapho.R
import app.tapho.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>() {
    private val PRIVACY_POLICY = "https://tapfo.in/privacy-policy/"
    private val TERMS_CONDITION = "https://tapfo.in/terms-conditions/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        statusBarColor(R.color.white)
        setContentView(binding.root)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true

        if (intent.getStringExtra("TYPE") == "1") {
            binding.webView.loadUrl(PRIVACY_POLICY)
            binding.progress.visibility = View.GONE
        }else{
            binding.webView.loadUrl(TERMS_CONDITION)
            binding.progress.visibility = View.GONE
        }

    }
}