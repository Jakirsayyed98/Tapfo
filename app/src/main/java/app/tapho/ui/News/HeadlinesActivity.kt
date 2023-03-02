package app.tapho.ui.News

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.ActivityHeadlinesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseActivity
import app.tapho.ui.News.Adapter.CategoriesAdapter
import app.tapho.ui.News.Adapter.HeadLinesAdapter
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.News.Model.AllNews.Data
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.utils.AppSharedPreference
import com.bumptech.glide.Glide
import java.util.*
import kotlin.math.abs


class HeadlinesActivity : BaseActivity<ActivityHeadlinesBinding>(), ApiListener<getAllNewsdata, Any?>,
    RecyclerClickListener, GestureDetector.OnGestureListener {
    private var Headlines: HeadLinesAdapter? = null
    var categoryData: CategoriesAdapter? = null

    private var news = mutableListOf<Data>()

    private lateinit var itemList: List<Data>
    lateinit var gestureDetector: GestureDetector
    var rowsArrayList: ArrayList<Data> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null
    var count = 0
    var page = 1
    var x1: Float = 0.0f
    var x2: Float = 0.0f
    var y1: Float = 0.0f
    var y2: Float = 0.0f
    var CategorieID = 1
    var lastScrollPosition = 1
    var isLoading = false
    var i = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeadlinesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData(CategorieID)
        getData1(i)
        setRecyclerBrand()
        setRecyclerBrand1()
        statusBarTextWhite()
        window.statusBarColor = Color.WHITE
        categoryViewModel()
        setHeadlinesData()
        initdata()
        //Color.TRANSPARENT
        gestureDetector = GestureDetector(this, this)
        binding.share.setImageResource(R.drawable.whatsappshareimg)
        binding.menu.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            getData(1)
            i = 0
            Handler().postDelayed(Runnable {
                binding.swipeRefreshLayout.isRefreshing = false
            }, 4000)
        }

        binding.name.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
            finish()
        }

        binding.backIv.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
            finish()
        }
        val layoutManager = LinearLayoutManager(this)

        /*
//        binding.newsHeadLines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                i++
//                getData(CategorieID)
//            }
//        })
*/
    }

    private fun initdata() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterListData(p0.toString())
            }
        })
    }

    private fun filterListData(searchName: String) {

        var tempList: ArrayList<Data> = ArrayList()
        for (x in itemList) {
            if (searchName.lowercase(Locale.getDefault()) in x.name.toString()
                    .lowercase(Locale.getDefault())
            ) {
                tempList.add(x)
            }
        }
        Headlines!!.updatelist(tempList)
    }

    private fun categoryViewModel() {
        getSharedPreference().getUserId().let {
            viewModel.getNewsCategories(
                getUserId(),
                this,
                object : ApiListener<getCategories, Any?> {
                    override fun onSuccess(t: getCategories?, mess: String?) {
                        t?.data.let {
                            categoryData!!.addAllItem(it!!)
                        }
                    }

                })
        }
    }

    private fun setRecyclerBrand() {
        categoryData = CategoriesAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                Headlines!!.clear()
                Headlines!!.notifyDataSetChanged()
                CategorieID = data.toString().toInt()
                getData(CategorieID)

            }
        })
        binding.Category.apply {
            //layoutManager = GridLayoutManager(context, 4)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = categoryData
        }
    }

    private fun setRecyclerBrand1() {
        categoryData = CategoriesAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                Headlines!!.clear()
                Headlines!!.notifyDataSetChanged()
                CategorieID = data.toString().toInt()
                i = 1
                getData(CategorieID)

            }
        })
        binding.Category1.apply {
            //layoutManager = GridLayoutManager(context, 4)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryData
        }
    }

    override fun onBackPressed() {

        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    internal fun getData1(id: Int) {

        //  viewModel.getNews( AppSharedPreference.getInstance(this).getUserId(), CategorieID.toString(),i.toString(), this, this )
        viewModel.getNews(AppSharedPreference.getInstance(this).getUserId(), id.toString(),
            i.toString(), this, object : ApiListener<getAllNewsdata, Any?> {
                override fun onSuccess(t: getAllNewsdata?, mess: String?) {
                    t.let {
                        setNewsData(it)
                    }
                }
            })
    }

    internal fun getData(id: Int) {
        //  viewModel.getNews( AppSharedPreference.getInstance(this).getUserId(), CategorieID.toString(),i.toString(), this, this )
        viewModel.getNews(AppSharedPreference.getInstance(this).getUserId(), id.toString(),
            i.toString(), this, object : ApiListener<getAllNewsdata, Any?> {
                override fun onSuccess(t: getAllNewsdata?, mess: String?) {

                    var tempList: ArrayList<Data> = ArrayList()
                    itemList = t!!.data
                    t.let {
                        it.data.let {
                            updateDataList(it)
                            //Headlines!!.addAllItem(it)
//                           it.forEach {
//                                Headlines!!.addItem(it)
//                                tempList.add(it)
//
//                            }
                        }
                  //      rowsArrayList.addAll(tempList)
                        updateDataList(tempList)

                    }
                }

            })

    }

    private fun updateDataList(tempList1: List<Data>) {
        val tempList = news.toMutableList()
        tempList.addAll(tempList1)
        tempList.forEach {
            Headlines!!.addItem(it)
        }
        //Headlines!!.addAllItem(tempList)
        news = tempList
    }

    /*

    internal fun getData(id: Int) {
        //  viewModel.getNews( AppSharedPreference.getInstance(this).getUserId(), CategorieID.toString(),i.toString(), this, this )
        viewModel.getNews(AppSharedPreference.getInstance(this).getUserId(), id.toString(),
            i.toString(), this, object : ApiListener<getAllNewsdata, Any?> {
                override fun onSuccess(t: getAllNewsdata?, mess: String?) {
                    itemList = t!!.data
                    t.let {
                        it.data.let {
                            //Headlines!!.addAllItem(it)
                            it.forEach {
                                Headlines!!.addItem(it)

                            }
                        }
                        i++
                        getData(CategorieID)

                    }
                }

            })

    }


     */
    private fun setNewsData(it: getAllNewsdata?) {
        var image = it!!.data.get(0).featured_image
        var url = it!!.data.get(0).link
        var headlines = it!!.data.get(0).name
        binding.headlines.text = Html.fromHtml(headlines)
        Glide.with(this).load(image).centerCrop().into(binding.images)

        if (url.contains("www.")) {
            var providerName = it.data.get(0).link.replace("https://www.", "")
            var name = providerName.replaceAfter(".", "")
            binding.providerName.text = "· " + "" + name
        } else {
            var providerName = it.data.get(0).link.replace("https://", "")
            var name = providerName.replaceAfter(".", "")
            binding.providerName.text = "· " + "" + name
        }
        binding.share.setOnClickListener {
            shareNews(url)
        }

    }

    private fun shareNews(url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        // intent.setPackage("com.whatsapp")
        val appLink: String = "https://play.google.com/store/apps/details?id=app.tapho"
        //val message: String = "I found an amazing product for you, hope you will like it checkout here"
        val newMessage: String =
            "also I’m saving on every shopping over 200+ stores like Flipkart, Myntra, Ajio, Mamaearth & more with Tapfo, Just download the app to save more"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey\n" + url.toString() + "\n\n\n" + newMessage + "\n\n" + appLink
        )
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }


    override fun onSuccess(t: getAllNewsdata?, mess: String?) {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setHeadlinesData() {
        val layoutManager1 = LinearLayoutManager(this)
        binding.newsHeadLines.layoutManager = layoutManager1
        Headlines = HeadLinesAdapter(this)
        binding.newsHeadLines.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = false
                reverseLayout = false }
            adapter = Headlines
            adapter?.notifyDataSetChanged()
            binding.newsHeadLines.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isLoading) {
                        if (layoutManager1.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) {
                            i++
                            getData(i)
                            isLoading = true
                        }
                    }
                }
            })
        }

        Headlines!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                layoutManager1.scrollToPositionWithOffset(positionStart, 0)
            }
        })




    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

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

                if (abs(valueX) > NewsActivity.MIN_DISTANCE) {
                    if (x2 > x1) {

                        //  Toast.makeText(this,"Right", Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(Intent(this, NewsActivity::class.java))
                        finish()
                        //  Toast.makeText(this,"Left", Toast.LENGTH_SHORT).show()
                    }
                } else if (abs(valueY) > NewsActivity.MIN_DISTANCE) {
                    //detect top to bottom value
                    if (y2 > y1) {
                        //   Toast.makeText(this,"Bottom", Toast.LENGTH_SHORT).show()
                    } else {
                        // Toast.makeText(this,"Top", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }

        return super.onTouchEvent(event)
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
}