package app.tapho.ui.AllUpiPaymentProcess.RecivedMoneyFromUPI

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.databinding.FragmentGetMoneyFromOtherBinding
import app.tapho.ui.BaseFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter


class GetMoneyFromOtherFragment : BaseFragment<FragmentGetMoneyFromOtherBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recivemoneybinding = FragmentGetMoneyFromOtherBinding.inflate(inflater, container, false)

        setUpiID()
        GenrateQRCodeForRecicvedMoney()

        return recivemoneybinding?.root
    }

    private fun GenrateQRCodeForRecicvedMoney() {

        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix =
                qrCodeWriter.encode(recivemoneybinding!!.upiid.text.toString().drop(9), BarcodeFormat.QR_CODE, 200, 200)
            val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565)
            for (x in 0..199) {
                for (y in 0..199) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            recivemoneybinding!!.QRCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpiID() {
        getSharedPreference().getLoginData().let {
            recivemoneybinding!!.upiid.text ="UPI id:- "+ it!!.name!!.subSequence(0,4).toString()+""+it!!.mobile_no.toString()+"@okaxis"
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GetMoneyFromOtherFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}