package app.tapho.ui.News

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2
import app.tapho.databinding.ActivityNewsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.News.Adapter.*
import app.tapho.ui.News.Model.AllNews.Data
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.utils.AppSharedPreference
import kotlin.math.abs

class NewsActivity : BaseActivity<ActivityNewsBinding>(), ApiListener<getAllNewsdata, Any?>,
    RecyclerClickListener, GestureDetector.OnGestureListener {

    private var ShortsViewPager: VerticalNewsPager? = null
    lateinit var gestureDetector: GestureDetector
    var page = 1
    var listnews = ArrayList<Data>()

    var x1: Float = 0.0f
    var x2: Float = 0.0f
    var y1: Float = 0.0f
    var y2: Float = 0.0f

    var CategorieID = 1

    companion object {
        var position = 0
        val MIN_DISTANCE = 150
        var listsizedata = 0
        fun openNews(
            context: Context?,
        ) {
            context?.startActivity(Intent(context, NewsActivity::class.java).apply {})
        }

        var i = 1
        var CategorieID = 1
        var CategorieName = ""
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewmodeldata()
//        setFullScreen(window)
//        lightstatusBar(window,false)

        window.statusBarColor = Color.BLACK

//        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
//
//        windowInsetsController?.isAppearanceLightNavigationBars = true

        gestureDetector = GestureDetector(this, this)
        binding.name.setOnClickListener {
            startActivity(Intent(this, HeadlinesActivity::class.java))
            finish()
        }
        binding.backIv.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }



        gestureDetector = GestureDetector(this, this)
        // gestureDetector = GestureDetector( binding.layout,this)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        when (event?.action) {

            // when we start to swipe
            0 -> {
                x1 = event.x
                y1 = event.y
            }
            //when we end the swipe
            1 -> {
                x2 = event.x
                y2 = event.y
                val valueX = x2 - x1
                val valueY = y2 - y1

                if (abs(valueX) > MIN_DISTANCE) {
                    if (x2 > x1) {
                        startActivity(Intent(this, HeadlinesActivity::class.java))
                        finish()
                        // Toast.makeText(this,"Right",Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(this,"Left",Toast.LENGTH_SHORT).show()
                    }
                } else if (abs(valueY) > MIN_DISTANCE) {
                    //detect top to bottom value
                    if (y2 > y1) {
                        //  Toast.makeText(this,"Bottom",Toast.LENGTH_SHORT).show()
                    } else {
                        //   Toast.makeText(this,"Top",Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }

        return super.onTouchEvent(event)
    }

      fun viewmodeldata() {
          viewModel.getNews(AppSharedPreference.getInstance(this).getUserId(), CategorieID.toString(), page.toString(), this, this
          )
      }

    private fun setHeadlinesData(it: List<Data>) {
        listnews.addAll(it)
        ShortsViewPager = VerticalNewsPager(this, listnews, this)
        binding.ShotsViewPager.adapter = ShortsViewPager
        var data = binding.ShotsViewPager.adapter!!.itemCount

        //      binding.ShotsViewPager.setPageTransformer(pageTransformation)
    }

    private val pageTransformation = ViewPager2.PageTransformer { page, position ->
        val minimumScal = 0.75f

        when {
            position < -1 -> {
                page.alpha = 0f
            }
            position <= 0 -> {
                page.alpha = 1f
                page.translationY = 0f
                page.translationZ = 1f
                page.scaleX = 1f
            }
            position <= 1 -> {
                page.translationY = page.height * -position
                page.translationZ = -1f
                val scale = minimumScal + (1 - minimumScal) * (1 - abs(position))
                page.scaleY = scale
                page.scaleX = scale


            }
            else -> {
                page.alpha = 0f
            }
        }

    }

    override fun onSuccess(t: getAllNewsdata?, mess: String?) {
        t.let {
            it!!.data.let {
                setHeadlinesData(it)
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        page++
        viewmodeldata()
        listnews = data as ArrayList<Data>
        position = pos
    }

    override fun onDown(e: MotionEvent): Boolean {

        return false
    }

    override fun onShowPress(e: MotionEvent) {

    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {

    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }


}