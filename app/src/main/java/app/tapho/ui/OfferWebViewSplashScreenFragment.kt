package app.tapho.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.palette.graphics.Palette
import app.tapho.R
import app.tapho.databinding.FragmentOfferWebViewSplashScreenBinding
import app.tapho.utils.MINI_APP_ID
import com.bumptech.glide.Glide

class OfferWebViewSplashScreenFragment : DialogFragment() {
    private var binding: FragmentOfferWebViewSplashScreenBinding? = null
    var SplashIcon: Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOfferWebViewSplashScreenBinding.inflate(inflater, container, false)

        kotlin.runCatching {
            Handler(Looper.getMainLooper()).postDelayed({
                //  binding?.internetLi?.visibility = View.VISIBLE
//                Toast.makeText(requireContext(),getString(R.string.redirected_please_wait2),Toast.LENGTH_SHORT).show()
            }, 10000)
        }
        activity?.intent?.getStringExtra(MINI_APP_ID)?.let {
            when (it) {

                "81" -> {

                    Glide.with(this).asBitmap().load(R.drawable.book_my_show_screen).placeholder(R.drawable.loding_app_icon).into(binding!!.newImage)
                }
                "583" -> {
                    Glide.with(this).asBitmap().load(R.drawable.gifter_screen).placeholder(R.drawable.loding_app_icon).into(binding!!.newImage)
                }

                else -> {}
            }
        }

//            .into(object : BitmapImageViewTarget(view?.findViewById(R.id.newImage)) {
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: Transition<in Bitmap>?
//                ) {
//                    super.onResourceReady(resource, transition)
//
//                    //setColor(resource)
//                }
//            })

        dialog?.window?.addFlags(  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                //WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        return binding?.root
    }


    private fun setColor(resource: Bitmap) {
        createPaletteSync(resource).vibrantSwatch?.rgb?.let { setColor(it) }
    }

    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    private fun setColor(color: Int) {

        dialog?.window?.addFlags(  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            //    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
       // view?.findViewById<RelativeLayout>(R.id.mainRe)?.setBackgroundColor(color)
    }



    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            imageUrl: String?,

            ): OfferWebViewSplashScreenFragment { return OfferWebViewSplashScreenFragment().apply {
            arguments = Bundle().apply {
             //   putString("Image", imageUrl)
//                putString(CASHBACK, cashback)

            }
            show(fragmentManager, "WebviewLoadingDialog")
        }
        }
        @JvmStatic
        fun newInstance() =
            OfferWebViewSplashScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}