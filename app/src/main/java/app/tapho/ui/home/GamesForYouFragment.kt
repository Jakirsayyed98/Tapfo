package app.tapho.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.tapho.databinding.FragmentGamesForYouBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.adapter.*
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.adapter.NewBannerDataAdapter
import app.tapho.ui.home.adapter.SliderModelMain
import app.tapho.ui.model.MiniApp
import app.tapho.utils.*
import app.tapho.viewmodels.RecentGameViewModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import kotlin.random.Random


class GamesForYouFragment : BaseFragment<FragmentGamesForYouBinding>(),
    ApiListener<GamezopCategoryData, Games>,
    RecyclerClickListener {
    var sliderHandler=Handler()
    private var gamecategory: GamezopCategoriAdapter<app.tapho.ui.games.models.GamezopCategory.Data>? = null
    private var RecentGameListAdapter: GetRecentlyPlayedGameAdapterList? = null
    private var populargames: PopularBrandAdapter<Data>? = null//
    private var mostplayed: PopularBrandAdapter<Data>? = null//
    private var AllPopular: PopularBrandAdapter<Data>? = null//
    private var TrendingGames: GamezopTrendingGameAdapter<Data>? = null//
    private var NewlyAddedGames: NewlyAddedGamesAdapter<Data>? = null//
    private var gamesbanner1: List<Data>? = null
    private var InTheSpotLight: List<Data>? = null
    private var recentPlayGame: RecentGameViewModel? = null

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
        bindingGamesForyou = FragmentGamesForYouBinding.inflate(inflater, container, false)
        viewmodeldata()
        setRecyclerBrand()
        popularGames()
        trendingGames()
        recentPlayedList()
        NewlyAddedGames()
        mostPlayed()
        GetGames()
        AllPopular()
        getRecentGameplayList()
        AllClickEvent()
        recentPlayGame = ViewModelProvider(this).get(RecentGameViewModel::class.java)

        return bindingGamesForyou?.root

    }

    private fun AllClickEvent() {
        bindingGamesForyou!!.seeAlltranding.setOnClickListener {
            openContainer("TrandingGames", "TrendingGames", false, "data.name")
        }
  bindingGamesForyou!!.seeAllPopular.setOnClickListener {
            openContainer("PopularGames", "PopularGames", false, "data.name")
        }
        bindingGamesForyou!!.seeAllNewlyAddedGames.setOnClickListener {
            openContainer("NewlyAdded", "NewlyAdded", false, "data.name")
        }
        bindingGamesForyou!!.AllMostPlayed.setOnClickListener {
            openContainer("MostPlayed", "MostPlayed", false, "data.name")
        }
    }

    private fun getRecentGameplay() {
        recentPlayGame?.getGameRecentPlayList(getUserId())
    }


    private fun getRecentGameplayList() {
        getSharedPreference().getUserId().let {
            viewModel.getRecentDataList( getUserId(), this, object : ApiListener<GameRecentPlayList, Any?> {
                    override fun onSuccess(t: GameRecentPlayList?, mess: String?) {
                        t!!.let {
                            if (it.data.isNullOrEmpty()) {
                                bindingGamesForyou!!.recentlist.visibility = View.GONE
                            } else {
                                it.data.let {
                                    bindingGamesForyou!!.recentlist.visibility = View.VISIBLE
                                 //   RecentGameListAdapter!!.addAllItem(it)
                                    //visible only newly 12 items
                                    RecentGameListAdapter!!.addAllItem(
                                        if (it.size > 10) {
                                            it.subList(0, 10)

                                    } else {
                                        it
                                    })
                                }
                            }
                        }
                    }

                })
        }


    }

    private fun viewmodeldata() {
        viewModel.getGamezopCategoryData(
            AppSharedPreference.getInstance(requireContext()).getUserId(), this, this
        )

    }

    private fun GetGames() {
        getSharedPreference().getUserId().let {
            viewModel.getAllGames(getUserId(), this, object : ApiListener<Games, Any?> {
                override fun onSuccess(t: Games?, mess: String?) {
                    val listData = t!!.data.size
                    t.let { Games ->
                        //Popular
                        Games!!.data.forEach {
                            if (it.popular_game.contains("1")) {
                                populargames!!.addItem(it)
                                setIntheSpotLight(Games.data)
                            }
                            if (it.trending_game.contains("1")) {
                                TrendingGames!! addItem (it)
                            }
                        }
                        //Newly Added
                        for (i in 6 downTo 1) {
                            NewlyAddedGames!!.addItem(Games.data[listData - i])
                        }

                        //All Popular
                        val myRandomValues = /*this is for showing how much data u want*/
                            List(51) { Random.nextInt(0, listData) }
                        myRandomValues.forEach {
                            AllPopular!!.addItem(Games.data[it])
                        }
                        //BannerList
                        setGamesBanner1(Games.data)

                        //mostplayed

                        for (i in 36 downTo 31) {
                            mostplayed!!.addItem(Games.data[listData - i])
                        }

                    }
                }
            })
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            GamesForYouFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun mostPlayed() {
        mostplayed = PopularBrandAdapter(this)
        bindingGamesForyou!!.mostplayed.apply {
            //   layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context, 3)
            adapter = mostplayed
        }
    }

    private fun trendingGames() {
        TrendingGames = GamezopTrendingGameAdapter(this)
        bindingGamesForyou!!.TrandingGames.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = TrendingGames
        }
        bindingGamesForyou!!.TrandingGames.setNestedScrollingEnabled(false);
    }

    private fun recentPlayedList() {
        RecentGameListAdapter = GetRecentlyPlayedGameAdapterList(this)
        bindingGamesForyou!!.recntlyPlayed.apply {
            layoutManager = GridLayoutManager(context, 5)
            //      layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecentGameListAdapter


        }
    }

    private fun popularGames() {
        populargames = PopularBrandAdapter(this)
        bindingGamesForyou!!.populargames.apply {
            //   layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context, 3)
            adapter = populargames
        }
    }

    private fun AllPopular() {
        AllPopular = PopularBrandAdapter(this)
        bindingGamesForyou!!.popularAll.apply {
            //   layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context, 3)
            adapter = AllPopular
        }
    }

    private fun NewlyAddedGames() {
        NewlyAddedGames = NewlyAddedGamesAdapter(this)
        bindingGamesForyou!!.NewlyAddedGames.apply {
            // layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context, 2)

            adapter = NewlyAddedGames
        }
    }

    private fun setRecyclerBrand() {
        gamecategory = GamezopCategoriAdapter(this)
        bindingGamesForyou!!.gamesCategories.apply {
            layoutManager = GridLayoutManager(
                context,
                5
            )
            adapter = gamecategory
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        Log.d("Data", "type:$type" + " pos:$pos" + " data:${data}")
        if (type == "GameCategory" /*&& data is Data*/) {
            openContainer("GameCategory", data, false, "data.name")
        } else {

            GameWebViewActivity.openWebView(context, data.toString(), "", "", "", "")
            //WebViewActivity.openWebView(context, data.toString(), "", "", "", "", "")
        }

    }

    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    override fun onSuccess(t: GamezopCategoryData?, mess: String?) {
        t.let {
            it?.data.let {
                gamecategory?.addAllItem(it!!)
            }
        }
    }

    private fun openWebView(data: MiniApp) {
        WebViewActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            ""
        )
    }

    private fun setIntheSpotLight(data: List<Data>) {
        val imageList = ArrayList<SlideModel>()
        InTheSpotLight = data
        for (x in data) {
            imageList.add(SlideModel(x.assets.wall, ScaleTypes.CENTER_CROP))
        }
        bindingGamesForyou!!.spotlight.setImageList(imageList)
        bindingGamesForyou!!.spotlight.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                GameWebViewActivity.openWebView(
                    context,
                    data[position].url,
                    data[position].id,
                    data[position].name,
                    data[position].assets.square,
                    ""
                )
                //WebViewActivity.openWebView(context, data[position].url, "", "", "", "", "" )
            }

        })
    }


    private fun setGamesBanner1(data: List<Data>) {

        val imageList = ArrayList<SliderModelMain>()
        gamesbanner1 = data
        for (x in data) {
            imageList.add(SliderModelMain("", x.url,x.id,"","",""))
        }

        bindingGamesForyou!!.Gamebanner1.adapter = NewBannerDataAdapter(
            imageList,
            bindingGamesForyou!!.Gamebanner1,
            object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data1: Any?, type: String) {
                    data.forEach {
                        if (data1==it.url){
                            GameWebViewActivity.openWebView(context,it.url,it.id,it.name,it.assets.square,"")
                 //           GameWebViewActivity.openWebView(context, data1.toString(), "", "", "", "")
                        }
                    }

                }

            })
        bindingGamesForyou!!.Gamebanner1.clipToPadding = false
        bindingGamesForyou!!.Gamebanner1.clipChildren = false
        bindingGamesForyou!!.Gamebanner1.offscreenPageLimit = 3
        bindingGamesForyou!!.Gamebanner1.getChildAt(0).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        /*
        //slider look start

        val compositPagerTransformation = CompositePageTransformer()
        compositPagerTransformation.addTransformer(MarginPageTransformer(30))
        compositPagerTransformation.addTransformer { page, position ->
            val slide = 1 - Math.abs(position)
            page.scaleY = 0.85f + slide + 0.25f
        }
        bindingGamesForyou!!.Gamebanner1.setPageTransformer(compositPagerTransformation)

        //slider look end

         */
        bindingGamesForyou!!.Gamebanner1.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bindingGamesForyou!!.Gamebanner1.removeCallbacks(sliderRunnable)
                bindingGamesForyou!!.Gamebanner1.postDelayed(sliderRunnable, 4000)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable,4000)
    }
    private val sliderRunnable= Runnable {
        try {
            bindingGamesForyou!!.Gamebanner1.currentItem=bindingGamesForyou!!.Gamebanner1.currentItem+1
        }catch (e:Exception){
            Log.d("DataException","$e")
        }

    }
    override fun onPause() {
        super.onPause()
        sliderHandler.postDelayed(sliderRunnable,4000)
    }
}
