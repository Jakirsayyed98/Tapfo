package app.tapho.ui.home.CabsContainer

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import app.tapho.databinding.FragmentUberBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.InternetErrorFragment
import app.tapho.ui.home.WebviewLoadingDialog
import app.tapho.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.lang.String
import java.net.URISyntaxException
import kotlin.Any
import kotlin.Boolean
import kotlin.apply
import kotlin.let


class UberFragment : BaseFragment<FragmentUberBinding>() {

    @JvmField
    var mGeolocationOrigin: String? = null

    @JvmField
    var mGeolocationCallback: GeolocationPermissions.Callback? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        UberBinding = FragmentUberBinding.inflate(inflater, container, false)
        LoadDataInWebView()
        return UberBinding?.root
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun LoadDataInWebView() {
        UberBinding!!.webView.settings.javaScriptEnabled = true
        UberBinding!!.webView.settings.domStorageEnabled = true
        UberBinding!!.webView.settings.databaseEnabled = true
        UberBinding!!.webView.settings.setAppCacheEnabled(true)
        UberBinding!!.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        UberBinding!!.webView.settings.allowFileAccess = true
        UberBinding!!.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        CookieManager.getInstance().setAcceptThirdPartyCookies(UberBinding!!.webView, true)
        UberBinding!!.webView.webChromeClient = UberGeoLocation(this)


        UberBinding!!.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: kotlin.String?) {
                super.onPageFinished(view, url)


            }

            override fun onPageCommitVisible(view: WebView?, url1: kotlin.String?) {
                super.onPageCommitVisible(view, url1)
            }


            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: kotlin.String?,
                failingUrl: kotlin.String?,
            ) {

                if (errorCode == -2) {
//                    toast("Internet error")
                    //      InternetErrorFragment.newInstance().show(supportFragmentManager, "")
                }
            }


            /*

//            override fun shouldOverrideUrlLoading(view: WebView, url123: String?): Boolean {
//                url123?.let {
//
//                    if (url123.startsWith("tel:")) {
//
//                        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(url123)))
//                    } else if (url123.startsWith("http") || url123.startsWith("https")) {
//                        return false
//                    } else
//                        if (url123.startsWith("intent")) {
//                            try {
//                                val intent = Intent.parseUri(url123, Intent.URI_INTENT_SCHEME)
//                                val fallbackUrl = intent.getStringExtra("browser_fallback_url")
//                                if (fallbackUrl != null) {
//                                    view.loadUrl(fallbackUrl);
//                                    return true
//                                }
//                            } catch (e: URISyntaxException) {
//                                e.printStackTrace()
//                            }
//                        }
//                }
//                return super.shouldOverrideUrlLoading(view, url123)
//            }



             */


        }
        UberBinding!!.webView.loadUrl("https://tapfo.in/delta/link/Y0ZlN05zUnBkQjhBQUs4VjZhYng4OW5HUDJ5a2JvYUJlUlFUM3BLTFN5eGY1VGNScEJLQW9WYUQ2d2FhZjV5QStLemlyc1FVZzdqblF1L3RzYldrQzk4VVNkZmV1KzdIS3JMRnVUNlp3V29iZ2NEaGJJN3N0QkdlVHU4dGxoTFA=")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                if (UberBinding!!.webView.canGoBack()) {
                    UberBinding!!.webView.goBack()
                }
                else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<kotlin.String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_FINE_LOCATION -> {
                var allow = false
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // user has allowed this permission
                    allow = true
                }
                if (mGeolocationCallback != null) {
                    // call back to web chrome client
                    mGeolocationCallback?.invoke(mGeolocationOrigin.toString(), allow, false)
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UberFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

   
}