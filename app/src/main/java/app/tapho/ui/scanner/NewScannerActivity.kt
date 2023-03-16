package app.tapho.ui.scanner

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.ActivityNewScannerBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.intro.IntroNewAdapter
import app.tapho.ui.intro.sliderItem
import app.tapho.ui.scanner.ScanCart.BarcodeScannerForProductActivity
import app.tapho.ui.scanner.ScanCart.ContainerForProductActivity
import app.tapho.ui.scanner.model.Data
import app.tapho.ui.scanner.model.TapfoMartProductRes
import app.tapho.ui.tcash.DirectPaytmTransaction.StartPaymentprocessingActivity
import app.tapho.utils.setOnCustomeCrome
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class NewScannerActivity : BaseActivity<ActivityNewScannerBinding>() {


    val REQUEST_CAMERA_PERMISSION = 201
    val sliderHandler= Handler(Looper.getMainLooper())

    companion object{
        fun openContainer(
            context: Context
        ){
            context.startActivity(Intent(context,NewScannerActivity::class.java))
        }
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
         binding.mycart.setOnClickListener {
             ContainerForProductActivity.openContainer(this@NewScannerActivity,"ProductCartFragment","",false,"")

         }



        Setbannerdata(bannerdata)
        setPager()


    }

    @OptIn(DelicateCoroutinesApi::class)
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
                        val textData=barcodes.valueAt(0)!!.displayValue.toString()
                        if (textData.contains("http")) {
                            this@NewScannerActivity.setOnCustomeCrome(textData)
                        }else if (textData.contains("tapfo")){
                            startActivity(Intent(this@NewScannerActivity,BarcodeScannerForProductActivity::class.java))
                         /*
                         }else if (textData.contains("[0-9]".toRegex())){
                            viewModel.TapfoMartsearchBarcodeproduct(getUserId(),textData,this@NewScannerActivity,object :ApiListener<TapfoMartProductRes,Any?>{
                                override fun onSuccess(t: TapfoMartProductRes?, mess: String?) {
                                    t!!.data.let {
                                        if (it.isNullOrEmpty().not()){
                                            GlobalScope.launch {
                                                val Product = getDatabase(this@NewScannerActivity).appDao().ProductByBarcodeISExist(it.get(0).barcode)
                                                if (Product){
                                                    GlobalScope.launch {
                                                        getDatabase(this@NewScannerActivity).appDao().getProductByBarcode(it.get(0).barcode).let {
                                                            GlobalScope.launch {
                                                                getDatabase(this@NewScannerActivity).appDao().UpdateProductToCart(it.buyingQty.toInt()+1,it.barcode)
                                                            }
                                                        }
                                                    }
                                                }else{
                                                    GlobalScope.launch {
                                                        getDatabase(this@NewScannerActivity).appDao().AddPRoductToCart(Data(it.get(0).id,1,it.get(0).barcode,it.get(0).created_at,it.get(0).description,it.get(0).image,it.get(0).mrp_price,it.get(0).name,it.get(0).qty,it.get(0).sale_price,it.get(0).sku,it.get(0).status,it.get(0).user_id))
                                                    }
                                                }
                                            }
                                        }else{
                                            Toast.makeText(this@NewScannerActivity,"Product Not Fond"+textData,Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }

                            })
*/
                        } else {
                            showCopyDialog(textData)
                        }
                    }
                }
            }
        })

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

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
//        initialiseDetectorsAndSources()
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


