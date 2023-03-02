package app.tapho.ui.localbizzUI.BusinessProfileFlow

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import app.tapho.R
import app.tapho.databinding.FragmentBizzCardBinding
import app.tapho.interfaces.ApiListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Model.getBusinessDetails.getBusinessDetailRes
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


class BizzCardFragment : BaseFragment<FragmentBizzCardBinding>() {


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
        _binding = FragmentBizzCardBinding.inflate(inflater,container,false)
        getViewmodelForBusinessCard()
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        statusBarTextWhite()
        statusBarColor(R.color.white)
        _binding!!.toolbar.tvTitle.text = "Business Card"

        return _binding?.root
    }

    private fun getViewmodelForBusinessCard() {
        viewModel.getbusinessDetails(getUserId(),activity?.intent?.getStringExtra("business_id"),this,object : ApiListener<getBusinessDetailRes,Any?>{
            override fun onSuccess(t: getBusinessDetailRes?, mess: String?) {
                t.let {
                    it!!.data.get(0).let {
                        _binding!!.name.text = it.business_name

                        _binding!!.mobileNumber.text =it.contacts
                        _binding!!.emailid.text =it.email

                        _binding!!.address.text =it.floor+", "+it.address.replace("India","")

                        getBusinessCardSave()
                    }
                }
            }
        })
    }

    private fun getBusinessCardSave() {
        _binding!!.saveCard.setOnClickListener {
           // saveCardinGallary()
            saveCardDataAsImage(_binding!!.cardFront)
        }


    }

    private fun saveCardDataAsImage(view: LinearLayout): Bitmap? {
        val totalHeight = view.height
        val totalWidth = view.width
        val percent = 1.0f //use this value to scale bitmap to specific size


        val canvasBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(canvasBitmap)
        canvas.scale(percent, percent)
        view.draw(canvas)

        saveImageToGallarybit(canvasBitmap)

        return canvasBitmap
    }

    private fun saveImageToGallarybit(canvasBitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

          activity?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it)

                }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            canvasBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, it)

            this.requireView().showShortSnack("Business card saved in your gallary")

        }
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            BizzCardFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}