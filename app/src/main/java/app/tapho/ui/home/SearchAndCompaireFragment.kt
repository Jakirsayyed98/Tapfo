package app.tapho.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentSearchAndCompaireBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.CustomeFinanceCategoryAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.URISyntaxException

class SearchAndCompaireFragment : BaseFragment<FragmentSearchAndCompaireBinding>() {

    @JvmField
    var mGeolocationOrigin: java.lang.String? = null

    @JvmField
    var mGeolocationCallback: GeolocationPermissions.Callback? = null

    var url=""
 //   var icondata:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingSearchAndCompaire = FragmentSearchAndCompaireBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        statusBarColor(R.color.orange1)
        // statusBarTextWhite()
        statusBarTextBlack()

        SetSearchTab()
         url= "https://www.flipkart.com/search?q="
        bindingSearchAndCompaire!!.changeIcon.setOnClickListener {
            BottomSheet()
        }
        var data=  bindingSearchAndCompaire!!.searchEt.text

        bindingSearchAndCompaire!!.searchEt.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                loadUrldata(url,data.toString())
            }

        })
   //     icondata = R.drawable.flipkart
   //     Glide.with(requireContext()).load(icondata).into(bindingSearchAndCompaire!!.icon)
        return bindingSearchAndCompaire?.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrldata(url: String, queryString: String) {
        bindingSearchAndCompaire!!.webView.settings.javaScriptEnabled = true
        bindingSearchAndCompaire!!.webView.settings.domStorageEnabled = true
        bindingSearchAndCompaire!!.webView.settings.databaseEnabled = true
        bindingSearchAndCompaire!!.webView.settings.setAppCacheEnabled(true)
        bindingSearchAndCompaire!!.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        bindingSearchAndCompaire!!.webView.settings.allowFileAccess = true
//        binding.webView.settings.builtInZoomControls = true

        bindingSearchAndCompaire!!.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        CookieManager.getInstance().setAcceptThirdPartyCookies(bindingSearchAndCompaire!!.webView, true)
        bindingSearchAndCompaire!!.webView.webChromeClient = GeoWebChromeClientSearchAndCompare(this)

        bindingSearchAndCompaire!!.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

            }

            override fun onPageCommitVisible(view: WebView?, url1: String?) {
                super.onPageCommitVisible(view, url1)

            }


            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?,
            ) {
            }

            override fun shouldOverrideUrlLoading(view: WebView, url123: String?): Boolean {
                url123?.let {

                    if (url123.startsWith("tel:")) {

                        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(url123)))
                    } else if (url123.startsWith("http") || url123.startsWith("https")) {
                        return false
                    } else
                        if (url123.startsWith("intent")) {
                            try {
                                val intent = Intent.parseUri(url123, Intent.URI_INTENT_SCHEME)
                                val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                                if (fallbackUrl != null) {
                                    view.loadUrl(fallbackUrl);
                                    return true
                                }
                            } catch (e: URISyntaxException) {
                                e.printStackTrace()
                            }
                        }
                }
                return super.shouldOverrideUrlLoading(view, url123)
            }
        }
        bindingSearchAndCompaire!!.webView.loadUrl(url+queryString)
    }

    private fun SetSearchTab() {
        Glide.with(requireContext()).load(R.drawable.flipkart).circleCrop()
            .into(bindingSearchAndCompaire!!.icon)
    }

    private fun BottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.searchandicon, null)
        val icon : RecyclerView =view.findViewById(R.id.icons)


        val customShopCategory = CustomeFinanceCategoryAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {

                    "Amazon" -> {
                        url= "https://www.amazon.in/s?k="
                        dialog.dismiss()
                    }
                    "Flipkart" -> {
                        url= "https://www.flipkart.com/search?q="
                        dialog.dismiss()

                    }
                    "Mintra" -> {
                        url= "https://www.myntra.com/"
                        dialog.dismiss()
                    }
                }
            }

        }).apply {

            addItem(CustomeShopCategoryModel(R.drawable.beauty_img_icon, "Amazon", "Amazon"))
            addItem(CustomeShopCategoryModel(R.drawable.flipkart, "Flipkart", "Flipkart"))
            addItem(CustomeShopCategoryModel(R.drawable.camera_icon, "Mintra", "Mintra"))
            addItem(CustomeShopCategoryModel(R.drawable.beauty_img_icon, "Amazon", "Amazon"))
            addItem(CustomeShopCategoryModel(R.drawable.flipkart, "Flipkart", "Flipkart"))
            addItem(CustomeShopCategoryModel(R.drawable.camera_icon, "Mintra", "Mintra"))
            addItem(CustomeShopCategoryModel(R.drawable.beauty_img_icon, "Amazon", "Amazon"))
            addItem(CustomeShopCategoryModel(R.drawable.flipkart, "Flipkart", "Flipkart"))
            addItem(CustomeShopCategoryModel(R.drawable.camera_icon, "Mintra", "Mintra"))

        }
        icon.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context,4)
            //layoutManager = LinearLayoutManager(context)
            adapter = customShopCategory
        }


        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun setIconData(icon: RecyclerView) {

    }



    companion object {

        @JvmStatic
        fun newInstance() =
            SearchAndCompaireFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}