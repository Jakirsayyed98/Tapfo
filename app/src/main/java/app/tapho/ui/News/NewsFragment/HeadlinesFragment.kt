package app.tapho.ui.News.NewsFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentHeadlinesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.News.Adapter.CategoriesAdapter
import app.tapho.ui.News.Adapter.HeadLinesAdapter
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.News.Model.AllNews.Data
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.utils.AppSharedPreference
import com.bumptech.glide.Glide
import java.util.*

class HeadlinesFragment : BaseFragment<FragmentHeadlinesBinding>(),
    ApiListener<getAllNewsdata, Any?>,
    RecyclerClickListener {

    private var Headlines: HeadLinesAdapter? = null
    var categoryData: CategoriesAdapter? = null

    private var news = mutableListOf<Data>()
    var tempList2: ArrayList<Data> = ArrayList()

    private lateinit var itemList: List<Data>
    lateinit var gestureDetector: GestureDetector
    var rowsArrayList: ArrayList<Data> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null
    var count = 0
    var page = 1
    var CategorieID = 1
    var lastScrollPosition = 1
    var i = 1

    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        headlinesbinding = FragmentHeadlinesBinding.inflate(inflater, container, false)

        getData(CategorieID,i)
        //getData1(i)
        setRecyclerBrand()
        setRecyclerBrand1()
        statusBarTextWhite()
        activity?.window?.statusBarColor = Color.WHITE
        categoryViewModel()
        setHeadlinesData()
        categoryList()
        initdata()
        //Color.TRANSPARENT

        headlinesbinding!!.share.setImageResource(R.drawable.whatsappshareimg)
        headlinesbinding!!.menu.setOnClickListener {
            headlinesbinding!!.drawer.openDrawer(GravityCompat.START)
        }
/*
        headlinesbinding!!.newsHeadLines.addOnScrollListener(object : PaginationScrollListener(headlinesbinding!!.newsHeadLines.layoutManager as LinearLayoutManager){
            override fun loadMoreItems() {
                isLoading = true
                i += 1

                Handler(Looper.myLooper()!!).postDelayed({

                   getData(CategorieID,i)
                }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })
*/
        val layoutManager = LinearLayoutManager(requireContext())

        return headlinesbinding?.root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HeadlinesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun initdata() {
        headlinesbinding!!.searchEt.addTextChangedListener(object : TextWatcher {
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
                tempList2.clear()
                getData(CategorieID,1)

            }
        })
        headlinesbinding!!.Category.apply {
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
                tempList2.clear()
                getData(CategorieID,1)

            }
        })
        headlinesbinding!!.Category1.apply {
            //layoutManager = GridLayoutManager(context, 4)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryData
        }
    }
    internal fun categoryList() {
        //  viewModel.getNews( AppSharedPreference.getInstance(this).getUserId(), CategorieID.toString(),i.toString(), this, this )
        viewModel.getNewsCategories(AppSharedPreference.getInstance(requireContext()).getUserId(),this, object : ApiListener<getCategories, Any?> {
                override fun onSuccess(t: getCategories?, mess: String?) {
                    val Templist =ArrayList<Int>()
                    val Templist2 =ArrayList<Int>()
                    t.let {
                          it!!.data.forEach {
                              Templist.add(it.id)
                          }
                        Templist2.addAll(Templist.shuffled())
                        getData1(Templist2.get(1))
                    }
                }
            })
    }

    internal fun getData1(id: Int) {
        //  viewModel.getNews( AppSharedPreference.getInstance(this).getUserId(), CategorieID.toString(),i.toString(), this, this )
        viewModel.getNews(AppSharedPreference.getInstance(requireContext()).getUserId(), id.toString(),
            1.toString(), this, object : ApiListener<getAllNewsdata, Any?> {
                override fun onSuccess(t: getAllNewsdata?, mess: String?) {
                    t.let {

                        setNewsData(it)
                    }
                }
            })
    }

    internal fun getData(id: Int,i:Int) {
        //  viewModel.getNews( AppSharedPreference.getInstance(this).getUserId(), CategorieID.toString(),i.toString(), this, this )
        viewModel.getNews(AppSharedPreference.getInstance(requireContext()).getUserId(), id.toString(), i.toString(), this, object : ApiListener<getAllNewsdata, Any?> {
                override fun onSuccess(t: getAllNewsdata?, mess: String?) {
                    itemList = t!!.data
                    t.let {
                        it.data.let {
                            updateDataList(it)
//                            tempList2.addAll(it)
//
//                            if (tempList2.size>=50){
//                                Headlines!!.addAllItem(tempList2)
//                            }else
//                                getData(CategorieID,page++)
                        }

                    }
                }

            })

    }

    private fun updateDataList(tempList1: List<Data>) {

      var  tempList = news.toMutableList()

       // tempList.addAll(tempList1)
        tempList2.addAll(tempList1)

        if (tempList2.size>=200){
            Headlines!!.addAllItem(tempList2)
        }else
        getData(CategorieID,i++)


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
        headlinesbinding!!.headlines.text = Html.fromHtml(headlines)
        Glide.with(this).load(image).centerCrop().into(headlinesbinding!!.images)

        if (url.contains("www.")) {
            var providerName = it.data.get(0).link.replace("https://www.", "")
            var name = providerName.replaceAfter(".", "")
            headlinesbinding!!.providerName.text = "· " + "" + name
        } else {
            var providerName = it.data.get(0).link.replace("https://", "")
            var name = providerName.replaceAfter(".", "")
            headlinesbinding!!.providerName.text = "· " + "" + name
        }
        headlinesbinding!!.share.setOnClickListener {
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
        val layoutManager1 = LinearLayoutManager(requireContext())
        headlinesbinding!!.newsHeadLines.layoutManager = layoutManager1
        Headlines = HeadLinesAdapter(this)
        headlinesbinding!!.newsHeadLines.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = false
                reverseLayout = false }
            adapter = Headlines
            adapter?.notifyDataSetChanged()
        }

//        Headlines!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                layoutManager1.scrollToPositionWithOffset(positionStart, 0)
//
//                if (itemCount==rowsArrayList.size-1){
//                    getData(CategorieID,i++)
//                }
//
//            }
//        })
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}