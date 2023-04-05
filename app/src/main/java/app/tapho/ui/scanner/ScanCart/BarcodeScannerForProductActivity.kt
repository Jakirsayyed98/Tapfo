package app.tapho.ui.scanner.ScanCart

import android.Manifest
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.ActivityBarcodeScannerForProductBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart
import app.tapho.utils.customToast
import app.tapho.utils.withSuffixAmount
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class BarcodeScannerForProductActivity : BaseActivity<ActivityBarcodeScannerForProductBinding>() {


    lateinit var barcodeDetector: BarcodeDetector//? = null
    lateinit var cameraSource: CameraSource//? = null
    private val REQUEST_CAMERA_PERMISSION = 201

    //This class provides methods to play DTMF tones
    private var toneGen1: ToneGenerator? = null
    private var barcodeData: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerForProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor= Color.BLACK
        binding.name.text = getSharedPreference().getBusinessData()!!.business_name
        binding.address.text = getSharedPreference().getBusinessData()!!.address
        binding.eannumber.addTextChangedListener {
            if (it!!.length==13){
                binding.search.visibility = View.VISIBLE
            }else{
                binding.search.visibility = View.GONE
            }
        }

        binding.search.setOnClickListener {
            val intent = Intent()
            intent.putExtra("ENCodeData", binding.eannumber.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
        toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        initialiseDetectorsAndSources();
    }
    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.EAN_13)
            .build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@BarcodeScannerForProductActivity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource.start(binding.surfaceView.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                            this@BarcodeScannerForProductActivity,
                            arrayOf<String>(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {

                    barcodeData = barcodes.valueAt(0).displayValue
                    val intent = Intent()
                    intent.putExtra("ENCodeData", barcodeData)
                    setResult(RESULT_OK, intent)
                    finish()
                    toneGen1!!.startTone(ToneGenerator.TONE_CDMA_PIP, 150)

                }
            }
        })
    }
    override fun onPause() {
        super.onPause()
        cameraSource.release()
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }

}