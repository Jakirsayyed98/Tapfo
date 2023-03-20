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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.ActivityBarcodeScannerForProductBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.scanner.model.Data
import app.tapho.ui.scanner.model.TapfoMartProductRes
import app.tapho.utils.customToast
import app.tapho.utils.withSuffixAmount
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLDecoder


class BarcodeScannerForProductActivity : BaseActivity<ActivityBarcodeScannerForProductBinding>() {


    private lateinit var codeScanner: CodeScanner
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerForProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor= Color.BLACK
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
                   t!!.let {
                       if (it.data.isNullOrEmpty().not()){
                           t.data.get(0).let {
                               OpenCartBottomSheet(it)
                           }
                       }else{
                           Toast.makeText(this@BarcodeScannerForProductActivity,"Product Not Fond"+textData,Toast.LENGTH_LONG).show()
                       }
                   }



                }

                override fun onError(mess: String?) {
                    super.onError(mess)
                    Toast.makeText(this@BarcodeScannerForProductActivity,"Product Not Fond "+mess,Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun OpenCartBottomSheet(it: Data) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.tapfomart_cart_layout,null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
        val image = view.findViewById<ImageView>(R.id.image)
        val name = view.findViewById<TextView>(R.id.name)
        val price = view.findViewById<TextView>(R.id.price)
        val disprice = view.findViewById<TextView>(R.id.disprice)
        val QtyData = view.findViewById<TextView>(R.id.QtyData)
        val add = view.findViewById<ImageView>(R.id.add)
        val less = view.findViewById<ImageView>(R.id.less)
        val addToCheckOut = view.findViewById<AppCompatButton>(R.id.addToCheckOut)

        Glide.with(this).load(it.image).placeholder(R.drawable.loading_progress).into(image)
        name.text = it.name
        price.text = withSuffixAmount(it.mrp_price)
        disprice.text = withSuffixAmount(it.sale_price)
        var qtyd = 1
        GlobalScope.launch {
            val Product = getDatabase(this@BarcodeScannerForProductActivity).appDao().ProductByBarcodeISExist(it.barcode)
            if (Product){
                GlobalScope.launch {
                    getDatabase(this@BarcodeScannerForProductActivity).appDao().getProductByBarcode(it.barcode).let {
                        QtyData.text = it.buyingQty.toString()
                        qtyd = it.buyingQty
                    }
                }
            }else{
                qtyd =1
            }
        }


        QtyData.text = qtyd.toString()
        add.setOnClickListener{
            qtyd += 1
            QtyData.text = qtyd.toString()
        }

        less.setOnClickListener{
            qtyd -= 1
            QtyData.text = qtyd.toString()
        }

        addToCheckOut.setOnClickListener{click->
           addToCart(qtyd,it)
            dialog.dismiss()
        }

    }

    private fun addToCart(qtyd: Int, data : Data) {
       data.let {

               GlobalScope.launch {
                   val Product = getDatabase(this@BarcodeScannerForProductActivity).appDao().ProductByBarcodeISExist(it.barcode)
                   if (Product){
                       GlobalScope.launch {
                           getDatabase(this@BarcodeScannerForProductActivity).appDao().getProductByBarcode(it.barcode).let {
                               GlobalScope.launch {
                                   getDatabase(this@BarcodeScannerForProductActivity).appDao().UpdateProductToCart(qtyd,it.barcode)
                               }
                           }
                       }
                   }else{
                       GlobalScope.launch {
                           getDatabase(this@BarcodeScannerForProductActivity).appDao().AddPRoductToCart(
                               Data(it.id,qtyd,it.barcode,it.created_at,if(it.description.isNullOrEmpty()) " Description" else it.description,it.image,it.mrp_price,it.name,it.qty,it.sale_price,it.sku,it.status,it.user_id)
                           )
                       }
                   }
               }
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

    override fun onBackPressed() {
        if (backPressedTime + 5000 > System.currentTimeMillis()) {
            super.getOnBackPressedDispatcher().onBackPressed()
            finish()
        } else {
            this.customToast("After leveing this page your Cart will be empty! are you sure you want to exit press back again to leave",true)
        }
        backPressedTime = System.currentTimeMillis()
    }

}