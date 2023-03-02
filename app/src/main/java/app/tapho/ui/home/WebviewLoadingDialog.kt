package app.tapho.ui.home

import android.R.attr.statusBarColor
import android.R.attr.text
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.palette.graphics.Palette
import app.tapho.R
import app.tapho.databinding.DialogWebviewLoadingBinding
import app.tapho.utils.CASHBACK
import app.tapho.utils.DATA
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
//import com.Homerommansabbir.animationx.*



class WebviewLoadingDialog : DialogFragment() {
    private var binding: DialogWebviewLoadingBinding? = null

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            imageUrl: String?,
            cashback: String?,
        ): WebviewLoadingDialog { return WebviewLoadingDialog().apply {
            arguments = Bundle().apply {
                    putString(DATA, imageUrl)
                    putString(CASHBACK, cashback)
                }
                show(fragmentManager, "WebviewLoadingDialog")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Tapfo)
        isCancelable = false
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogWebviewLoadingBinding.inflate(inflater, container, false)


        return binding?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        binding?.imageIv?.animationXAttention(Attention.ATTENTION_BOUNCE, duration = 500, listener = object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                binding?.imageIv?.animationXZoom(Zoom.ZOOM_IN_DOWN)
            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {
                binding?.imageIv?.animationXZoom(Zoom.ZOOM_IN_DOWN)
            }

        })
*/

        binding!!.backbtnWeb.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
/*
        binding?.redirectingTv?.animationXAttention(Attention.ATTENTION_FLASH, duration = 500, listener = object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                binding?.redirectingTv?.animationXSlide(Slide.SLIDE_IN_LEFT)
            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {
                binding?.redirectingTv?.animationXSlide(Slide.SLIDE_IN_LEFT)
            }

        })
*/

        Glide.with(this).asBitmap().load(arguments?.getString(DATA)).circleCrop().placeholder(R.drawable.loding_app_icon).into(object : BitmapImageViewTarget(view.findViewById(R.id.imageIv)) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    super.onResourceReady(resource, transition)

                    setColor(resource)
                }
            })



        val cashback = arguments?.getString(CASHBACK)
       // val cashbackTv = view.findViewById<TextView>(R.id.offerTv)
        if (cashback.isNullOrEmpty()) {
         //   cashbackTv.visibility = View.GONE
            binding?.redirectingTv22?.text = ""
        } else {
            binding?.redirectingTv22?.visibility = View.VISIBLE
             binding?.redirectingTv22!!.text = cashback+" Activated"
//            binding?.redirectingTv?.visibility = View.GONE
           // binding?.animationView?.visibility = View.GONE
        }
        kotlin.runCatching {
            Handler(Looper.getMainLooper()).postDelayed({
                binding?.redirectingTv22?.visibility = View.VISIBLE
                binding?.redirectingTv22?.text = getString(R.string.redirected_please_wait2)
            }, 10000)
        }
    }

    private fun setColor(bitmap: Bitmap) {
        createPaletteSync(bitmap).vibrantSwatch?.rgb?.let { setColor(it) }
    }

    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    private fun setColor(color: Int) {
        dialog?.window?.statusBarColor=color
        dialog?.window?.addFlags(  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS //    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                 )
        view?.findViewById<RelativeLayout>(R.id.mainRe)?.setBackgroundColor(color)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}