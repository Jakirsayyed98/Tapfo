package app.tapho.ui.News.NewsFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentNewsHeadlineBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.News.Adapter.CategoriesAdapter
import app.tapho.ui.News.Adapter.NewsSliderAdapter
import app.tapho.ui.News.Adapter.newCategoryAdapter
import app.tapho.ui.News.Model.AllCategories.getCategories
import app.tapho.ui.News.Model.AllNews.Data
import app.tapho.ui.News.Model.AllNews.getAllNewsdata
import app.tapho.ui.News.Model.NewsSliderModel
import app.tapho.ui.News.NewsWebViewActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.utils.AppSharedPreference
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NewsHeadlineFragment :  BaseFragment<FragmentNewsHeadlineBinding>() {


    val newsList:MutableList<String> =ArrayList()
    var tempList2: ArrayList<Data> = ArrayList()
    var categoryData: newCategoryAdapter<app.tapho.ui.News.Model.AllCategories.Data>? = null
//    var categoryData: CategoriesAdapter? = null
    var categoryDataNavigation: CategoriesAdapter? = null
    var page=1
    var isloading=false
    var limit=20
    var CategoryID=1
    private lateinit var itemList: List<Data>

    private var bannerlist1: ArrayList<Data>? = null

    lateinit var adapter: NewsHeadlineFragment.RecyclerAdapter
    lateinit var layout: LinearLayoutManager


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
        blackFragmentbinding = FragmentNewsHeadlineBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        layout = LinearLayoutManager(requireContext())
        getData(CategoryID,page)
        categoryList()
        categoryViewModel()
        setRecyclerBrand1()

        blackFragmentbinding!!.recycler.layoutManager=layout
//        if (activity is HomeActivity)
//            (activity as HomeActivity).tabClicked((activity as HomeActivity).binding.News)


        blackFragmentbinding!!.back.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        blackFragmentbinding!!.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    val visibleItemCount = layout.childCount
                    val pastVisble = layout.findFirstCompletelyVisibleItemPosition()
                    val total =adapter.itemCount

                    if (!isloading){

                        if ((visibleItemCount + pastVisble) >= total){
                            page++
                            getData(CategoryID,page)
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        initdata()
        return blackFragmentbinding?.root
    }
    private fun getData(i: Int, page: Int) {
        isloading=true
        blackFragmentbinding!!.progress.visibility=View.VISIBLE
        viewModel.getNews(AppSharedPreference.getInstance(requireContext()).getUserId(), i.toString(), page.toString(), this, object :
            ApiListener<getAllNewsdata, Any?> {
            override fun onSuccess(t: getAllNewsdata?, mess: String?) {
                t.let {
                    it!!.data.let {
                        itemList = t!!.data
                        it.forEach {
                            tempList2.add(it)
                        }
                        Handler().postDelayed({
                            if (::adapter.isInitialized){
                                adapter.notifyDataSetChanged()
                            }else{
                                adapter = NewsHeadlineFragment.RecyclerAdapter(this@NewsHeadlineFragment)
                                blackFragmentbinding!!.recycler.adapter = adapter
                            }
                            isloading=false
                            blackFragmentbinding!!.progress.visibility=View.GONE
                        },1000)

                    }
                }
            }
        })


    }
    internal fun categoryList() {

        viewModel.getNewsCategories(AppSharedPreference.getInstance(requireContext()).getUserId(),this, object :
            ApiListener<getCategories, Any?> {
            override fun onSuccess(t: getCategories?, mess: String?) {
                val Templist = java.util.ArrayList<Int>()
                val Templist2 = java.util.ArrayList<Int>()
                t.let {
                    it!!.data.forEach {
                        Templist.add(it.id)
                    }
                    Templist2.addAll(Templist.shuffled())
                }
            }
        })
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
                            categoryDataNavigation!!.addAllItem(it!!)
                        }
                    }

                })
        }
    }

    private fun setRecyclerBrand1() {
//        categoryData = CategoriesAdapter(object : RecyclerClickListener {
        categoryData = newCategoryAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                adapter!!.notifyDataSetChanged()
                CategoryID = data.toString().toInt()
                page = 1
                tempList2.clear()
                getData(CategoryID,1)

            }
        })
        blackFragmentbinding!!.Category1.apply {
            //layoutManager = GridLayoutManager(context, 4)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryData
        }
    }

    private fun initdata() {
        blackFragmentbinding!!.searchEt.addTextChangedListener(object : TextWatcher {
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
        if (itemList.isNullOrEmpty()){
//            Toast.makeText(requireContext(),"No data found",Toast.LENGTH_SHORT).show()
        }else{
            var tempList: ArrayList<Data> = ArrayList()
            for (x in itemList) {
                if (searchName.lowercase(Locale.getDefault()) in x.name.toString()
                        .lowercase(Locale.getDefault())
                ) {
                    tempList.add(x)
                }
            }
            adapter.updatelist(tempList)
        }

    }



    companion object {

        @JvmStatic
        fun newInstance() =
            NewsHeadlineFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    class RecyclerAdapter (val activity: NewsHeadlineFragment): RecyclerView.Adapter<RecyclerAdapter.NumberViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
            return NumberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.headline_layout2,parent,false))

        }

        override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {

            val url=activity.tempList2[position].link //data.link
            if (url.contains("www.")){
                var providerName = url.replace("https://www.", "")
                var name = providerName.replaceAfter(".", "")
                holder.providerName.text = "· " + "" + name.replace(".","")
            }else{
                var providerName1 = url.replace("https://", "")
                var name = providerName1.replaceAfter(".", "")
                holder.providerName.text =  name.replace(".","")
            }

            var date = activity.tempList2[position].date

            /*
//            val pattern = "yyyy-MM-dd"
//            val simpleDateFormat = SimpleDateFormat(pattern)
//            val currentdate: String = simpleDateFormat.format(Date())
//
//
//            var data1=date.dropLast(9)
//
//            if (data1.equals(currentdate)){
//                holder.date.text=date.drop(10)
//            }
//            else{
//                holder.date.text= date.dropLast(9)
//            }
*/

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val time = sdf.parse(date).time
                val now = System.currentTimeMillis()
                val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                holder.date.text= ago
            } catch (e: ParseException) {
                e.printStackTrace()
            }


            holder.sliderItem.text =Html.fromHtml(activity.tempList2[position].name)

            holder.share.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                // intent.setPackage("com.whatsapp")
                val appLink: String = "https://play.google.com/store/apps/details?id=app.tapho"
                //val message: String = "I found an amazing product for you, hope you will like it checkout here"
                val newMessage: String =
                    "also I’m saving on every shopping over 200+ stores like Flipkart, Myntra, Ajio, Mamaearth & more with Tapfo, Just download the app to save more"
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey\n" +  activity.tempList2[position].link.toString() + "\n\n\n" + newMessage + "\n\n" + appLink
                )
                intent.type = "text/plain"
                holder.itemView.context.startActivity(Intent.createChooser(intent, "Share To:"))
            }

            Glide.with(holder.itemView.context).load(activity.tempList2[position].featured_image).centerCrop().into(holder.images)
            holder.headlineCard.setOnClickListener {
                NewsWebViewActivity.openwebView(holder.itemView.context,activity.tempList2[position].link)
            }
        }

        override fun getItemCount(): Int {
            return activity.tempList2.size
        }

        class NumberViewHolder(v : View) : RecyclerView.ViewHolder(v){
            val sliderItem=v.findViewById<TextView>(R.id.headlines)
            val providerName=v.findViewById<TextView>(R.id.providerName)
            val date=v.findViewById<TextView>(R.id.date)
            val images=v.findViewById<ImageView>(R.id.images)
            val headlineCard=v.findViewById<LinearLayout>(R.id.headlineCard)
            val share=v.findViewById<ImageView>(R.id.share)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun updatelist(list1:ArrayList<Data>){
            activity.tempList2=list1
            notifyDataSetChanged()

        }

    }

}