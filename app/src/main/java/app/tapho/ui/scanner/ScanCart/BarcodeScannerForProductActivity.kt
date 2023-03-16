package app.tapho.ui.scanner.ScanCart

import android.Manifest
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.ActivityBarcodeScannerForProductBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.scanner.model.Data
import app.tapho.ui.scanner.model.TapfoMartProductRes
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLDecoder


class BarcodeScannerForProductActivity : BaseActivity<ActivityBarcodeScannerForProductBinding>() {


    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerForProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mycart.setOnClickListener {
            ContainerForProductActivity.openContainer(this@BarcodeScannerForProductActivity,"ProductCartFragment","",false,"")
        }
        permissionTaking()
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

    fun startScanning() {

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



    private fun showCopyDialog(textData: String?) {
        if (textData != null) {
            viewModel.TapfoMartsearchBarcodeproduct(getUserId(),textData,this,object :
                ApiListener<TapfoMartProductRes, Any?> {
                override fun onSuccess(t: TapfoMartProductRes?, mess: String?) {
                    t!!.data.let {
                        if (it.isNullOrEmpty().not()){
                            GlobalScope.launch {
                                val Product = getDatabase(this@BarcodeScannerForProductActivity).appDao().ProductByBarcodeISExist(it.get(0).barcode)
                                if (Product){
                                    GlobalScope.launch {
                                        getDatabase(this@BarcodeScannerForProductActivity).appDao().getProductByBarcode(it.get(0).barcode).let {
                                            GlobalScope.launch {
                                                getDatabase(this@BarcodeScannerForProductActivity).appDao().UpdateProductToCart(it.buyingQty.toInt()+1,it.barcode)
                                            }
                                        }
                                    }
                                }else{
                                    GlobalScope.launch {
                                        getDatabase(this@BarcodeScannerForProductActivity).appDao().AddPRoductToCart(
                                            Data(it.get(0).id,1,it.get(0).barcode,it.get(0).created_at,it.get(0).description,it.get(0).image,it.get(0).mrp_price,it.get(0).name,it.get(0).qty,it.get(0).sale_price,it.get(0).sku,it.get(0).status,it.get(0).user_id)
                                        )
                                    }
                                }
                            }
                        }else{
                            Toast.makeText(this@BarcodeScannerForProductActivity,"Product Not Fond"+textData,Toast.LENGTH_LONG).show()
                        }
                    }
                }

            })
        }
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