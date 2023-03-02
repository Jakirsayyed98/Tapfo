package app.tapho.ui.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.databinding.ActivityNewScannerBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.PaytmPaymentGateway.Adapter.PSPModelClass
import app.tapho.ui.PaytmPaymentGateway.Adapter.SmartIntentPSPAdapter
import app.tapho.ui.PaytmPaymentGateway.TransactionProcessingPageActivity
import app.tapho.ui.intro.IntroNewAdapter
import app.tapho.ui.intro.sliderItem
import app.tapho.utils.customToast
import app.tapho.utils.withSuffixAmount
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.io.IOException
import java.util.ArrayList


class NewScannerActivity : BaseActivity<ActivityNewScannerBinding>() {

    val BHIM_UPI = "in.org.npci.upiapp"
    val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
    val PHONE_PE = "com.phonepe.app"
    val PAYTM = "net.one97.paytm"
    val upiApps = listOf<String>(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI)
    private val REQUEST_CODE = 125

    val REQUEST_CAMERA_PERMISSION = 201
    val sliderHandler= Handler(Looper.getMainLooper())
    companion object{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.black)
        statusBarTextblack()
        initialiseDetectorsAndSources()

        val bannerdata: MutableList<sliderItem> = ArrayList()
        bannerdata.add(sliderItem(R.drawable.scanner_banner1))
        bannerdata.add(sliderItem(R.drawable.scanner_banner2))

        binding.cancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        Setbannerdata(bannerdata)
        setPager()


    }

    fun initialiseDetectorsAndSources(){
        val barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        val cameras= CameraSource.Builder(this,barcodeDetector)
            .setRequestedPreviewSize(1920,2080)
            .setAutoFocusEnabled(true)
            .build()

        binding.scannerView.holder.addCallback(object  : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

                try {
                    if (ActivityCompat.checkSelfPermission(this@NewScannerActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameras.start(binding.scannerView.getHolder())
                    } else {
                        ActivityCompat.requestPermissions(this@NewScannerActivity, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                try {
                    if (ActivityCompat.checkSelfPermission(this@NewScannerActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameras.start(binding.scannerView.getHolder())
                    } else {
                        ActivityCompat.requestPermissions(this@NewScannerActivity, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameras.stop()
            }

        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode?> {
            override fun release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val barcodes: SparseArray<Barcode?> = detections.getDetectedItems()
                if (barcodes.size() != 0) {
                    runOnUiThread {
                        cameras.stop()
                        val textData=barcodes.valueAt(0)!!.displayValue
                        if (textData.contains("http")) {
                            showCopyDialog(textData)
                        }else if (textData.contains("upi://pay?pa")){
                            setUpiPayment(textData)

                        } else {
                            showCopyDialog(textData)
                        }

                    }

                }
            }
        })

    }


    @SuppressLint("MissingInflatedId")
    private fun setUpiPayment(textData: String) {
        val dialog  = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.upi_apps_list, null)
        dialog.setCancelable(true)
        val rv: RecyclerView = view.findViewById(R.id.PSPApps)
        setRVforPSP(rv,textData)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun setRVforPSP(PSPRv: RecyclerView,DeeplInkURI: String) {
        val SmartIntentPSPAdapterdaa = SmartIntentPSPAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                Log.d("DeepLink",DeeplInkURI)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DeeplInkURI))
                intent.data = Uri.parse(DeeplInkURI)
                intent.setPackage(data.toString())
                startActivityForResult(intent, REQUEST_CODE)
            }

        }).apply {
            for (i in upiApps.indices) {
                val p = upiApps[i]
                if (isAppInstalled(p) == true &&isAppUpiReady(p)) {
                    addItem(PSPModelClass(p, false))
                }
            }

        }
        PSPRv.apply {
            layoutManager = GridLayoutManager(this@NewScannerActivity, 4)
            adapter = SmartIntentPSPAdapterdaa
        }
    }


    fun isAppInstalled(packageName: String): Boolean {
        val pm = this.getPackageManager()
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false;
    }

    fun isAppUpiReady(packageName: String): Boolean {
        var appUpiReady = false
        val upiIntent = Intent(Intent.ACTION_VIEW, Uri.parse("upi://pay"))
        val pm = this.getPackageManager()
        val upiActivities: List<ResolveInfo> = pm.queryIntentActivities(upiIntent, 0)
        for (a in upiActivities){
            if (a.activityInfo.packageName == packageName) appUpiReady = true
        }
        return appUpiReady
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            Log.d("Payment Request ", requestCode.toString())
            Toast.makeText(this,"Payment Request"+requestCode,Toast.LENGTH_SHORT).show()
        }
    }


    private fun showCopyDialog(textData: String) {
        val dialog= BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.upi_dialog, null)
        val scandata: TextView = view.findViewById(R.id.scandata)
        scandata.text=textData.toString()
        val copy: Button = view.findViewById(R.id.copy)
        copy.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textData)
            clipboardManager.setPrimaryClip(clipData)
            initialiseDetectorsAndSources()

            dialog.dismiss()
        }
        val search: Button = view.findViewById(R.id.searchtext)
        search.setOnClickListener {
            val generateLink="https://www.google.co.in/search?q="+textData
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(generateLink))
            startActivity(browserIntent)
            initialiseDetectorsAndSources()

            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }


    private fun Setbannerdata(bannerdata: MutableList<sliderItem>) {
        binding.banner1.adapter= IntroNewAdapter(bannerdata,binding.banner1)

        binding.banner1.clipToPadding=false
        binding.banner1.clipChildren=false

        binding.banner1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position<2){
                    sliderHandler.removeCallbacks(sliderRunnable)
                }else{

                }
            }
        })

    }

    private fun setPager() {
        TabLayoutMediator(binding.tabLayout, binding.banner1,false) { _,_ -> }.attach()
    }

    private val sliderRunnable= Runnable {
        binding.banner1.currentItem=binding.banner1.currentItem+1
    }

}


