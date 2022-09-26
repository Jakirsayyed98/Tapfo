package app.tapho.ui.scanner

import android.Manifest
import android.R.attr.data
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.ActivityScannerBinding
import app.tapho.ui.BaseActivity
import com.budiyev.android.codescanner.*


class scanner : BaseActivity<ActivityScannerBinding>() {
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor=Color.BLACK
        permissionTaking()

    }

    companion object {
        var main_upi_id:String?=null
        var UPI_paying_name:String?=null

    }
    private fun permissionTaking() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            startScanning()
        }
    }

    private fun startScanning() {

        codeScanner = CodeScanner(this, binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE

        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val textData=it.text
                if (it.text.contains("http")) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.text.toString()))
                    startActivity(browserIntent)
                }else if (it.text.contains("upi://pay?pa")){
                    showCopyDialog(textData)
                  //  upiIdPayment(textData)

                } else {
                    showCopyDialog(textData)
                }

            }
        }
        codeScanner.errorCallback = ErrorCallback {
            Toast.makeText(
                applicationContext,
                "Camera initialization error: ${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun upiIdPayment(textData: String?) {
        startActivity(Intent(applicationContext,Upi_Redirecting::class.java))
        val upidata=textData?.substringAfter("=")
        main_upi_id=upidata?.substringBefore("&")//main UPI created here

        val UPI_Name_start=upidata?.substringAfter("=")
        val UPI_Name_end_cut=UPI_Name_start?.substringBefore("&")
        val re = "[^A-Za-z ]".toRegex()
        UPI_paying_name = re.replace(UPI_Name_end_cut.toString(), "")//to paying name
    }

    private fun showCopyDialog(textData: String?) {
        val dialog: Dialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.upi_dialog, null)
        val scandata: TextView = view.findViewById(R.id.scandata)
        scandata.text=textData.toString()

        val copy: Button = view.findViewById(R.id.copy)
        copy.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textData)
            clipboardManager.setPrimaryClip(clipData)
            dialog.dismiss()
        }
        val search: Button = view.findViewById(R.id.searchtext)
        search.setOnClickListener {

            val generateLink="https://www.google.co.in/search?q="+textData
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(generateLink))
            startActivity(browserIntent)
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "permission granted", Toast.LENGTH_SHORT).show()
                startScanning()
            } else {
                Toast.makeText(applicationContext, "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }

}