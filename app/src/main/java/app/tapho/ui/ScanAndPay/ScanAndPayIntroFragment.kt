package app.tapho.ui.ScanAndPay

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentScanAndPayIntroBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ScanAndPay.adapter.ScanAndPay_Features
import app.tapho.ui.ScanAndPay.model.CustomeScanAndPayFeatureModel
import app.tapho.ui.login.model.LoginData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import org.json.JSONArray
import org.json.JSONObject


class ScanAndPayIntroFragment :BaseFragment<FragmentScanAndPayIntroBinding>() {
    private var myBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanAndPayIntroBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.name.text = "MID: "+getSharedPreference().getLoginData()!!.name!!.replace(" ","")+"@tapfopay".replace(" ","")
        superlinks()

        getUserData()
        return _binding?.root
    }

    private fun getUserData() {
     //   val bitmap : Bitmap? = GenrateQRCode(getSharedPreference().getLoginData())


        var bitmap: Bitmap? = null
        try {
            bitmap = GenrateQRCode(getSharedPreference().getLoginData())
            myBitmap = bitmap
        } catch (we: WriterException) {
            we.printStackTrace()

        }
        if (bitmap != null) {
            _binding!!.qrcode.setImageBitmap(bitmap)
        }

    }

    private fun GenrateQRCode(loginData: LoginData?): Bitmap? {
        val AddDataToQRCode = "tapfo0"+getUserId()
        val bitMatrix : BitMatrix = MultiFormatWriter().encode(AddDataToQRCode, BarcodeFormat.QR_CODE, 660, 660)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (bitMatrix[j, i]) {
                    pixels[i * width + j] = -0x1000000
                } else {
                    pixels[i * width + j] = -0x1
                }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }


    private fun superlinks() {
       val customShopCategory = ScanAndPay_Features(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
            }

        }).apply {
            addItem(CustomeScanAndPayFeatureModel(R.drawable.seamless_experience,"Seamless\nExperience",""))
            addItem(CustomeScanAndPayFeatureModel(R.drawable.track_expense,"Track\nExpenses",""))
            addItem(CustomeScanAndPayFeatureModel(R.drawable.pay_everywhere,"Pay\nEverywhere",""))
//            addItem(CustomeScanAndPayFeatureModel(R.drawable.all_in_one_upi_apps,"All in One\nUPI App",""))
//            addItem(CustomeScanAndPayFeatureModel(R.drawable.fa_payment_security,"2FA Payment\nSecurity",""))
//            addItem(CustomeScanAndPayFeatureModel(R.drawable.expenses_control,"Expenses\nControl",""))
            addItem(CustomeScanAndPayFeatureModel(R.drawable.upto_cashback_30,"Upto 30%\nCashback\n",""))
//            addItem(CustomeScanAndPayFeatureModel(R.drawable.assured_cashback,"Assured\nCashback",""))
       }


        _binding!!.features.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = customShopCategory
        }
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            ScanAndPayIntroFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}