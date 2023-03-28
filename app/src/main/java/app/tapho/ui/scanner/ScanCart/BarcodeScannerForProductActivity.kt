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
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

        SaveToCart()


        binding.searchET.addTextChangedListener {
            if (it!!.length==13){
                binding.search.visibility = View.VISIBLE
            }else{
                binding.search.visibility = View.GONE
            }
        }

        binding.search.setOnClickListener {
            showCopyDialog(binding.searchET.text.toString())
        }

        binding.storename.text = getSharedPreference().getBusinessData()!!.business_name

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

    private fun SaveToCart() {
        getDatabase(this).appDao().getAllProductSet().observe(this){
            var Amount = 0.0
            var Qty = 0
            it.forEach {
                for(i in 1..it.qty.toInt()){
                    Amount+=it.price.toDouble()
                }
            }
            it.forEach {
               Qty+=it.qty
            }

            binding.itemCountAndPrice.text = Qty.toString() + " Items | "+ withSuffixAmount(Amount.toString())
        }
    }

    private fun showCopyDialog(textData: String?) {
        if (textData != null) {
            GlobalScope.launch {
                getDatabase(this@BarcodeScannerForProductActivity).appDao().ProductByEANisExist(textData).let {
                    if(it){
                        GlobalScope.launch {
                            getDatabase(this@BarcodeScannerForProductActivity).appDao().searchProduct(textData).let {
                                this@BarcodeScannerForProductActivity.runOnUiThread(Runnable {
                                    OpenCartBottomSheet(it)
                                })

                            }
                        }
                    }else{
                        this@BarcodeScannerForProductActivity.runOnUiThread(Runnable {
                            Toast.makeText(this@BarcodeScannerForProductActivity,"Product Not Fond "+textData,Toast.LENGTH_SHORT).show()
                        })

                    }
                }
            }
        }
    }


    private fun OpenCartBottomSheet(it: Data) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.tapfomart_cart_layout,null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
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
        price.text = withSuffixAmount(it.mrp)
        disprice.text = withSuffixAmount(it.price)
        var qtyd = 1
        GlobalScope.launch {
            val Product = getDatabase(this@BarcodeScannerForProductActivity).appDao().ProductByBarcodeISExist(it.ean)
            if (Product){
                GlobalScope.launch {
                    getDatabase(this@BarcodeScannerForProductActivity).appDao().getProductByBarcode(it.ean).let {
                        QtyData.text = it.qty.toString()
                        qtyd = it.qty
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
            binding.searchET.setText("")
            binding.scannerView.performClick()
            GlobalScope.launch {
                val Product = getDatabase(this@BarcodeScannerForProductActivity).appDao().ProductByBarcodeISExist(it.ean)
                if (Product){
                    GlobalScope.launch {
                        getDatabase(this@BarcodeScannerForProductActivity).appDao().getProductByBarcode(it.ean).let {
                            GlobalScope.launch {
                                getDatabase(this@BarcodeScannerForProductActivity).appDao().UpdateProductToCart(qtyd,it.ean,(qtyd * it.price.toDouble()))
                            }
                        }
                    }
                }else{
                    GlobalScope.launch {
                        getDatabase(this@BarcodeScannerForProductActivity).appDao().AddPRoductToCart(
                            Cart(it.id,it.business_id,it.business_user_category_id,it.business_user_sub_category_id,it.created_at,it.description,
                                it.ean,it.food_type,it.image,it.mrp,it.name,it.price,it.qty,it.status,it.user_id,qtyd,
                                (qtyd * it.price.toDouble()))
                        )
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "permission granted", Toast.LENGTH_SHORT).show()
                startScanning()
            } else {
                Toast.makeText(applicationContext, "Camera permission denied", Toast.LENGTH_SHORT).show()
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