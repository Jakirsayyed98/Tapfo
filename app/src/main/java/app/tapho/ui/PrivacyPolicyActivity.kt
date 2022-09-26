package app.tapho.ui

import android.os.Bundle
import app.tapho.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>() {
    private val PRIVACY_POLICY = "https://tapfo.in/privacy-policy/"
    private val TERMS_CONDITION = "https://tapfo.in/terms-conditions/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true

        if (intent.getStringExtra("TYPE") == "1")
            binding.webView.loadUrl(PRIVACY_POLICY)
        else
            binding.webView.loadUrl(TERMS_CONDITION)
    }
}