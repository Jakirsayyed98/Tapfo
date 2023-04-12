package app.tapho.ui.scanner

import android.Manifest
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import app.tapho.R
import app.tapho.databinding.ActivityScannerBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.intro.IntroNewAdapter
import app.tapho.ui.intro.sliderItem
import app.tapho.ui.scanner.ScanCart.BarcodeScannerForProductActivity
import app.tapho.ui.scanner.ScanCart.ContainerForProductActivity
import app.tapho.ui.scanner.model.BusinessDetail.searchBusinessRes
import app.tapho.utils.*
import com.budiyev.android.codescanner.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLDecoder
import java.util.ArrayList


class scanner : BaseActivity<ActivityScannerBinding>() {
    private lateinit var codeScanner: CodeScanner
    val sliderHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.black)
        window.statusBarColor = Color.BLACK
        permissionTaking()

        val bannerdata: MutableList<sliderItem> = ArrayList()
        bannerdata.add(sliderItem(R.drawable.scanner_banner1))
        bannerdata.add(sliderItem(R.drawable.scanner_banner2))

        Setbannerdata(bannerdata)
        setPager()


    }

    companion object {
        var main_upi_id: String? = null
        var UPI_paying_name: String? = null

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


    private fun Setbannerdata(bannerdata: MutableList<sliderItem>) {
        binding.banner1.adapter = IntroNewAdapter(bannerdata, binding.banner1)

        binding.banner1.clipToPadding = false
        binding.banner1.clipChildren = false

        binding.banner1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position < 2) {
                    sliderHandler.removeCallbacks(sliderRunnable)
                }
            }
        })

    }

    private fun setPager() {
        TabLayoutMediator(binding.tabLayout, binding.banner1, false) { _, _ -> }.attach()
    }

    private val sliderRunnable = Runnable {
        binding.banner1.currentItem = binding.banner1.currentItem + 1
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
                val textData = it.text
                if (textData.contains("http")) {
                    if (textData.contains("https://tapfo.onelink.me/k6rU/MID:")) {
                        getStoreDetail(textData.replace("https://tapfo.onelink.me/k6rU/MID:", ""))
                    } else {
                        this.setOnCustomeCrome(textData)
                    }
                } else {
                    getStoreDetail(textData)
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

    private fun getStoreDetail(textData: String?) {
        Log.d("@@@@@@", "J" + textData + "A")
        viewModel.searchBusiness(getUserId(), textData!!, this,
            object : ApiListener<searchBusinessRes, Any?> {
                override fun onSuccess(t: searchBusinessRes?, mess: String?) {
                    t!!.let {
                        if (it.data.isNullOrEmpty().not()) {
                            getSharedPreference().saveString(
                                BUSINESS_DATA,
                                Gson().toJson(it.data.get(0))
                            )
                            getSharedPreference().saveString(CART_ID, getCartIdRandom())
                            viewModel.DeleteBusinessProductList()
                            ContainerForProductActivity.openContainer(
                                this@scanner,
                                "StoreNameDialogFragment",
                                it.data.get(0),
                                false,
                                ""
                            )
                            finish()
                        } else {
                            showCopyDialog(textData)
                        }
                    }
                }
            })
    }

    private fun showCopyDialog(textData: String?) {
        if (textData!!.contains("userid")) {
            val data = URLDecoder.decode(textData)
            val obj: JSONObject = JSONObject(data)
            val jsonOBJ: JSONArray = obj.getJSONArray("userdata")
            val json = jsonOBJ.get(0)
        } else {
            val dialog: Dialog = Dialog(this)
            val view = layoutInflater.inflate(R.layout.upi_dialog, null)
            val scandata: TextView = view.findViewById(R.id.scandata)
            scandata.text = textData.toString()
            val copy: Button = view.findViewById(R.id.copy)
            copy.setOnClickListener {
                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", textData)
                clipboardManager.setPrimaryClip(clipData)
                dialog.dismiss()
            }
            val search: Button = view.findViewById(R.id.searchtext)
            search.setOnClickListener {

                val generateLink = "https://www.google.co.in/search?q=" + textData
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(generateLink))
                startActivity(browserIntent)
            }

            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
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