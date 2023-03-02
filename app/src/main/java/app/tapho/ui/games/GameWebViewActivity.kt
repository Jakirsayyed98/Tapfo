package app.tapho.ui.games

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.NavSheet.Fragment_favorite_nav
import app.tapho.R
import app.tapho.databinding.ActivityGameWebViewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.ui.BaseActivity
import app.tapho.ui.InternetErrorFragment
import app.tapho.ui.games.adapter.PopularBrandAdapter
import app.tapho.ui.games.models.GameAddToRecent.addGameToRecentList
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.games.models.GameFavandUnFav.getFavUnFav
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.WebviewLoadingDialog
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.URISyntaxException
import java.text.DecimalFormat
import kotlin.random.Random

class GameWebViewActivity : BaseActivity<ActivityGameWebViewBinding>(), ApiListener<Games, Any?>,RecyclerClickListener {

    private var loadingDialog: WebviewLoadingDialog? = null
    private var randomGameinExit: PopularBrandAdapter<Data>? = null

    private var tCash: WebTCashRes? = null
    private var fullData: Boolean = false
    private var onceDialogVisible: Boolean = false
    private val TAG = "WEB_VIEW_ERROR"
    private var res: WebTCashRes? = null
    var Game_name:String?=null
    var discription:String?=null
    var plays:String?=null
    var game_id:String?=null
    var rating:String?=null
    var icon_url:String?=null
    var totalplays=""


    companion object {
        fun openWebView(
            context: Context?,
            webViewUrl: String?,
            game_id: String?,
            game_name: String?,
            iconUrl: String?,
            favourite: String?,
        ) {
            context?.startActivity(Intent(context, GameWebViewActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(WEB_VIEW_URL, webViewUrl)
                putExtra(Game_id, game_id)
                putExtra(Game_Name, game_name)
                putExtra(ICON_URL, iconUrl)
                putExtra(IS_FAVOURITE, favourite)
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        statusBarColor(R.color.black)
        statusBarTextblack()
        viewmodeldata()
        initWebView()
        GameAddToRecentList()

    }

    private fun GameAddToRecentList() {
        getSharedPreference().getUserId().let {
            viewModel.getaddToRecentData(getUserId(),intent.getStringExtra(Game_id)!!,this, object :ApiListener<addGameToRecentList,Any?>{
                override fun onSuccess(t: addGameToRecentList?, mess: String?) {

                }
            })
        }
    }

    private fun viewmodeldata() {
        viewModel.getAllGames(AppSharedPreference.getInstance(this).getUserId(),"","","", this, this)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }



    private fun setWindowFlag(
        mainActivity: GameWebViewActivity,
        flagTranslucentStatus: Int,
        b: Boolean
    ) {
        val window: Window = this.window
        val winlayout: WindowManager.LayoutParams = window.attributes
        if (b) {
            winlayout.flags != flagTranslucentStatus
        } else {
            winlayout.flags = flagTranslucentStatus
        }
        window.attributes = winlayout
    }

    fun initWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
//        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)

        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true)

        binding.webView.webViewClient = object : WebViewClient() {

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
                if (errorCode == -2) {
//                    toast("Internet error")
                    InternetErrorFragment.newInstance().show(supportFragmentManager, "")
                }
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

        intent.getStringExtra(WEB_VIEW_URL)?.let {
            val data = tCash?.data?.get(0)?.cashback.toString()
            if (data.equals("null")) {
                binding.webView.loadUrl(it)

            } else {

                binding.webView.loadUrl(it)

            }
        }
    }

    override fun onSuccess(t: Games?, mess: String?) {
        val id=intent.getStringExtra(Game_id)

        t.let {
            it!!.data.forEach {
                setDataGame(t!!.data[id!!.toInt()-1])
            }
            val myRandomValues =  List(4) { Random.nextInt(0, t!!.data.size) }
            for (i in myRandomValues){

                randomGameinExit!!.addItem(it.data[i])
            }
            myRandomValues.forEach {i->

                randomGameinExit!!.addItem(it.data[i])
            }
        }



    }

    private fun setDataGame(data: Data) {
        Game_name=data.name
        discription=data.description
        plays=data.gamePlays
        rating=data.rating
        icon_url=data.assets.square
        game_id=data.id

    }
    override fun onBackPressed() {

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.gameexitsheet, null)
        val exit: AppCompatButton = view.findViewById(R.id.leave_btn)
        val app_continue: AppCompatButton = view.findViewById(R.id.continue_btn)
        val game_name: TextView = view.findViewById(R.id.Game_name)
        game_name.text= Game_name.toString()

        val Favbutton: TextView = view.findViewById(R.id.Fav)
        val UnFavbutton: TextView = view.findViewById(R.id.UnFav)

            viewModel.getGameFavList(getUserId(),this, object :ApiListener<getGameFavList,Any?>{
                override fun onSuccess(t: getGameFavList?, mess: String?) {
                t!!.data.forEach {
                    for (i in t.data){
                        if (it.id==game_id.toString()){

                            Favbutton.visibility=View.GONE
                            UnFavbutton.visibility=View.VISIBLE
                            break
                        }else{

                            UnFavbutton.visibility=View.GONE
                            Favbutton.visibility=View.VISIBLE
                            break
                        }
                    }
                }

                }

            })

        Favbutton.setOnClickListener {
            getSharedPreference().getUserId().let {
                if (game_id != null) {
                    viewModel.getGamesFavUnFav(getUserId(),game_id!!,"1",this,object :ApiListener<getFavUnFav,Any?>{
                        override fun onSuccess(t: getFavUnFav?, mess: String?) {
//                            applicationContext showShort
                            Toast.makeText(applicationContext,"Add to Fav",Toast.LENGTH_SHORT).show()
                            UnFavbutton.visibility=View.VISIBLE
                            Favbutton.visibility=View.GONE
                        }

                    })
                }
            }
        }
        UnFavbutton.setOnClickListener {
            getSharedPreference().getUserId().let {
                if (game_id != null) {
                    viewModel.getGamesFavUnFav(getUserId(),game_id!!,"0",this,object :ApiListener<getFavUnFav,Any?>{
                        override fun onSuccess(t: getFavUnFav?, mess: String?) {
                            Toast.makeText(applicationContext,"Remove from Fav",Toast.LENGTH_SHORT).show()
                            UnFavbutton.visibility=View.GONE
                            Favbutton.visibility=View.VISIBLE
                        }

                    })
                }
            }
        }

        val game_discription: TextView = view.findViewById(R.id.discription)
        game_discription.text= discription.toString()

        val play: TextView = view.findViewById(R.id.game_play)
       // play.text= plays.toString()

        val count:Double=if (plays!!.isNullOrEmpty()) 10000.00 else plays!!.toDouble()
        suffixFunction(count)
        play.text=totalplays //plays.toString()

        val rating_game: TextView = view.findViewById(R.id.rating)
        val rating_card: CardView = view.findViewById(R.id.rating_card)
        if (rating!!.contains("0") ){
            rating="3.5"
            rating_game.text= "3.5"

        }else{
            rating_game.text= rating.toString()
        }

        if (rating.isNullOrEmpty()){
        }else{

            if (rating!!/*.toInt()*/<= 2.4.toString()){
                val color= ContextCompat.getColor(this, R.color.red)
                rating_card.setCardBackgroundColor(color)
            } else if (rating!!>=2.4.toString() && rating!!<=3.9.toString()){
                val color= ContextCompat.getColor(this, R.color.orange)
                rating_card.setCardBackgroundColor(color)
            }else if (rating!!>=4.toString()){
                val color= ContextCompat.getColor(this, R.color.screenGreen)
                rating_card.setCardBackgroundColor(color)
            }
        }

// Recommended Games
        val must_try: RecyclerView = view.findViewById(R.id.must_try)
        randomGameinExit = PopularBrandAdapter(this)
        must_try.apply {
              layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
           // layoutManager = GridLayoutManager(context, 3)
            adapter = randomGameinExit
        }
      //
        val icon: ImageView = view.findViewById(R.id.iv_partner)
        Glide.with(this).load(icon_url).centerCrop().circleCrop().into(icon)

        exit.setOnClickListener {
            this.finish()
//            setResult(RESULT_OK, Intent().apply {
//                putExtra(MINI_APP_ID, intent.getStringExtra(MINI_APP_ID))
//                //  putExtra(IS_FAVOURITE, binding.favouriteIv.isSelected)
//            })
//            super.onBackPressed()
        }
        app_continue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()

    }

    private fun suffixFunction(count: Double):String {
        val df = DecimalFormat("#.#")
        totalplays = if (Math.abs(count / 1000000) >= 1) {
            df.format(count / 1000000.0).toString() + "m"
        } else if (Math.abs(count / 1000.0) >= 1) {
            df.format(count / 1000.0).toString() + "k"
        } else {
            count.toString()
        }
        return totalplays;
    }

    private fun removeGameFromFav(gameId: String?) {

    }

    private fun addGameToFav(gameId: String?) {

    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }


}