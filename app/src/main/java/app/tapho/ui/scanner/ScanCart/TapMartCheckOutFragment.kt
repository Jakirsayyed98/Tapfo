package app.tapho.ui.scanner.ScanCart

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.FragmentTapMartCheckOutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.scanner.adapter.TapfoCartAdapter
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart
import app.tapho.utils.CART_ID
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.text.SimpleDateFormat
import java.util.*


class TapMartCheckOutFragment : BaseFragment<FragmentTapMartCheckOutBinding>() {

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
        // Inflate the layout for this fragment
        _binding= FragmentTapMartCheckOutBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        getcartItems()
        setTextData()
        _binding!!.backbtn.setOnClickListener {
           activity?.onBackPressedDispatcher?.onBackPressed()
        }


//        fun setImage() {
//            Glide.with(requireContext()).load(setBusinessQR("")).into(_binding!!.qrcode)
//        }

        return _binding?.root
    }

    private fun getcartItems() {
        getDatabase(requireContext()).appDao().getAllProductSet().observe(viewLifecycleOwner){
            setLayout(it)
            SeDataForQR(it)
        }
    }

    private fun setTextData() {
        getSharedPreference().getBusinessData().let {
            _binding!!.apply {
                storename.text = it!!.business_name
                storeaddress.text = it.address
            }
        }
    }


    private fun setLayout(it: List<Cart>?) {
        var total = 0.0
        var BuyQty = 0
        it!!.forEach {
            for (i  in 1..it.qty.toInt()){
                total+=it.price.toDouble()
            }
        }
        it.forEach {
            BuyQty+=it.qty.toInt()
        }
        _binding!!.paybleAmount.text = withSuffixAmount(total.toString())
       _binding!!.cartcount.text = "Cart summary : "+it!!.size+" items"
        val tapfoCartAdapter  = TapfoCartAdapter<Cart>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })

        _binding!!.rrvData.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = tapfoCartAdapter
        }

        tapfoCartAdapter.addAllItem(it)

    }

    private fun SeDataForQR(it: List<Cart>?) {
        var bitmap: Bitmap? = null
        try {
            bitmap = GenrateQRCode(it)
            myBitmap = bitmap
        } catch (we: WriterException) {
            we.printStackTrace()

        }
        if (bitmap != null) {
            _binding!!.qrcode.setImageBitmap(bitmap)
        }

    }


    private fun GenrateQRCode(it: List<Cart>?): Bitmap? {
        val AddDataToQRCode = it.toString()
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

    companion object {

        @JvmStatic
        fun newInstance() =
            TapMartCheckOutFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}