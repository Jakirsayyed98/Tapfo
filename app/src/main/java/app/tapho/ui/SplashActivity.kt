package app.tapho.ui

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.ActivitySplashBinding
import app.tapho.ui.intro.IntroActivity
import app.tapho.ui.login.VerifyOtpActivity
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.utils.LogAppData
import app.tapho.utils.REACHED_HOME
import app.tapho.utils.VERIFY_OTP
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.concurrent.Executor


class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    var today=""
    var assigend = ""
    lateinit var info: String
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        belowStatusBar()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.white)
        statusBarTextWhite()



        today = TimePeriodDialog.getCurrentDate().toString()
        if (getSharedPreference().getString("ScreenLock").toString().equals("1")){
            splashCode()
        }else{
            val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                checkDeviceHasBiometric()
            }else{
                val i = km.createConfirmDeviceCredentialIntent("Use Fingerprint for login", "screen_lock_desc")

                startActivityForResult(i, 100)
            }


        }

    }
/*
    private fun biometricfunction() {
        val biometricmanager: BiometricManager = BiometricManager.from(this)

        when(biometricmanager.canAuthenticate()){

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->{
                LogAppData("Device does not have fingerprint ")
                splashCode()
//                showdialogforSetup()
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->{
                LogAppData("Not Working")
                splashCode()
//                showdialogforSetup()
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->{
                LogAppData("Not Fingerprint Assigned")
                assigend="1"
                showdialogforSetup()
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                TODO()
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                TODO()
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                TODO()
            }
            BiometricManager.BIOMETRIC_SUCCESS -> {
                TODO()
            }
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence, ) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult, ) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })


        val promtinfo : PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Enter phone screen lock pattern, PIN, password or fingerprint")
            .setDescription("Unlock Tapfo")
            .setConfirmationRequired(false)
            .setDeviceCredentialAllowed(true).build()




        biometricPrompt.authenticate(promtinfo)

    }
*/
    fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Toast.makeText(applicationContext, "App can authenticate using biometrics.", Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                splashCode()
                Toast.makeText(applicationContext, "No biometric features available on this device.", Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                splashCode()
                Toast.makeText(applicationContext, "Biometric features are currently unavailable.", Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                assigend="1"
                Toast.makeText(applicationContext, "Biometric features are currently available. want to enrol", Toast.LENGTH_LONG).show()
                showdialogforSetup()

            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Toast.makeText(applicationContext, "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED", Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Toast.makeText(applicationContext, "BIOMETRIC_ERROR_UNSUPPORTED", Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Toast.makeText(applicationContext, "BIOMETRIC_STATUS_UNKNOWN", Toast.LENGTH_LONG).show()
            }
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence, ) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode!=BiometricPrompt.ERROR_CANCELED){
                    if (assigend.equals("1").not()){
                        finish()
                        LogAppData(errString.toString() + errorCode)
                    }else{
                        LogAppData(errString.toString() + errorCode)
                    }
                }

                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult, ) {
                super.onAuthenticationSucceeded(result)
                splashCode()
                Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_LONG).show()
            }
        })


        val promtinfo : PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Enter phone screen lock pattern, PIN, password or fingerprint")
            .setDescription("Unlock Tapfo")
            .setConfirmationRequired(false)
            .setDeviceCredentialAllowed(true).build()




        biometricPrompt.authenticate(promtinfo)
      //  binding.tvShowMsg.text = info
    }

    private fun showdialogforSetup() {
        val last_date = getSharedPreference().getString("LastDate")
        if (last_date.equals(today)) {
            splashCode()
        } else {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.screenlock_request_lock, null)
            dialog.setCancelable(false)

            val doitletter: TextView = view.findViewById(R.id.doitletterbtn)
            val enableLock: AppCompatButton = view.findViewById(R.id.enableLock)
            enableLock.setOnClickListener {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                startActivityForResult(enrollIntent, 100)
                dialog.dismiss()
                checkDeviceHasBiometric()
            }

            doitletter.setOnClickListener {
                getSharedPreference().saveString("LastDate",today)
                dialog.dismiss()
                splashCode()
            }
            dialog.setContentView(view)
            dialog.show()
        }
    }

    private fun splashCode() {
        val tag  = intent?.getStringExtra("TAGS")
        val type  = intent?.getStringExtra("type")
        getSharedPreference().saveString("TAGS",tag.toString())
        getSharedPreference().saveString("type",type.toString())


        val verifyOtp = getSharedPreference().getBoolean(VERIFY_OTP)
        if (verifyOtp && getSharedPreference().getBoolean(REACHED_HOME).not())
            getSharedPreference().clear()


        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable{
            override fun run() {
                synchronized(this@SplashActivity) {
                    kotlin.runCatching {
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                kotlin.runCatching {
                                    val loginData = getSharedPreference().getLoginData()
                                    if (loginData == null) {
                                        startActivity(
                                            Intent(
                                                this@SplashActivity,
                                                IntroActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        if (getSharedPreference().getBoolean(REACHED_HOME)) {
                                            ContainerActivity.openContainer(
                                                this@SplashActivity,
                                                "WelcomeBackScreen",
                                                ""
                                            )
                                            finish()
                                        } else
                                            startActivity(
                                                Intent(
                                                    this@SplashActivity,
                                                    VerifyOtpActivity::class.java
                                                )
                                            )
                                    }
                                }
                            }
                        }, 2000)
                    }
                }
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }

}