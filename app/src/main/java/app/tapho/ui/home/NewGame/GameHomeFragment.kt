package app.tapho.ui.home.NewGame

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentGameHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.adapter.*
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.NewGame.Adapter.*
import app.tapho.ui.home.NewGame.Model.GameCustomeSuperCategoryModel
import app.tapho.ui.home.NewGame.Model.GamebannerModel
import app.tapho.ui.home.adapter.HomeFavAdapter
import kotlin.random.Random


class GameHomeFragment : BaseFragment<FragmentGameHomeBinding>(),
    RecyclerClickListener {
    private var gamecategory: GamezopCategoriAdapter<app.tapho.ui.games.models.GamezopCategory.Data>? = null
    private var GamesBanner: ArrayList<Data>? = null
    var customGamesCategory : GamesSuperLinkAdapter?=null
    private var TrendingGames: GamezopTrendingGameAdapter<Data>? = null//
    private var MyTrendingGames: PopularBrandAdapter<Data>? = null//
    private var AllPopular: PopularBrandAdapter<Data>? = null//
    private var mypopularGames: ActionGameCategoryAdapter<Data>? = null//
    private var NewlyAddedGames: newgames_Adapter<Data>? = null//
    private var GameFavList: MyGamesFavListAdapter? = null



    private var actionGame: ActionGameCategoryAdapter<Data>? = null//
    private var Adventure: AdventureGameCategoryAdapter<Data>? = null//
    private var Puzzle: PuzzleGameCategoryAdapter<Data>? = null//
    private var Logic: LogicGameCategoryAdapter<Data>? = null//
    private var Logic1: LogicGameCategoryAdapter<Data>? = null//
    private var Sports: SportsGameCategoryAdapter<Data>? = null//
    private var Racing: RacingGameCategoryAdapter<Data>? = null//
    private var Battle: BattleGameCategoryAdapter<Data>? = null//
    private var Cricket: CricketGameCategoryAdapter<Data>? = null//
    private var Cards: CardsGameCategoryAdapter<Data>? = null//
    private var Zombies: ZombiesGameCategoryAdapter<Data>? = null//
    private var Strategy: StrategyGameCategoryAdapter<Data>? = null//
    private var Arcade: ArcadeGameCategoryAdapter<Data>? = null//
    private var Sports_Racing: SportsRacingGameCategoryAdapter<Data>? = null//
    private var Puzzle_Logic: Puzzle_logicGameCategoryAdapter<Data>? = null//
    private var Featured: FeaturedGameCategoryAdapter<Data>? = null//

    var moreText="Show more"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gamebinding = FragmentGameHomeBinding.inflate(inflater, container, false)
        statusBarColor(R.color.grey_dark)
        activity?.window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity?.window!!.setFlags( WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS )
        statusBarTextBlack()



        progressStart()

        gamebinding!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        gamebinding!!.search.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "GameSearch", "GameSearch")
        }

        gamebinding!!.notificationRe.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "GameNotification", "GameNotification")
        }

        gamebinding!!.TrandingGames.setOnClickListener {
            ContainerActivity.openContainer(context, "TrandingGames", "TrandingGames", false, "")
        }

        gamebinding!!.refresh.setOnClickListener {
            startmodelclass()
        }
        startmodelclass()
        return gamebinding?.root
    }

    private fun progressStart() {
        gamebinding!!.progress.visibility = View.VISIBLE
        gamebinding!!.mainLayout.visibility = View.GONE
        gamebinding!!.lowconnection.visibility = View.GONE
    }

    private fun startmodelclass() {
        superlinks()
        setMoreTextSuperLinks("Show less")
        trendingGames()
        MytrendingGames()
        AllPopulardata()
        mypopularGamesdata()
        newlyAddedGames()
        getRecentGameplayList()
        GetGameCategory()
        setCategorydata()
        getFavListLayout()
        actionGame()
        AdventureGame()
        PuzzleGame()
        LogicGame()
        SportsGame()
        RacingGame()
        BattleGame()
        CricketGame()
        CardsGame()
        ZombiesGame()
        StrategysGame()
        ArcadesGame()
        Sports_RacingsGame()
        Puzzle_LogicsGame()
        FeaturedsGame()

        GetGames()
        getMyfav()
        val handler  = Handler(Looper.getMainLooper())
        handler.postDelayed(object  : Runnable{
            override fun run() {
                kotlin.runCatching {
                    if (getSharedPreference().getString("StartGameLoading").equals("0")){
                        handler.removeCallbacksAndMessages(null)
                        lowConnection()
                    }else{
                        handler.removeCallbacksAndMessages(null)
                    }
                }
            }
        },10000)
    }

    //Fav Games
    private fun getMyfav() {
        viewModel.getGameFavList().observe(viewLifecycleOwner, {
            when (it.status){
                Status.SUCCESS->{it!!.data.let {t->
                    t.let {
                        if (it!!.data.isNullOrEmpty()){
                            gamebinding!!.myfavRVList.visibility = View.GONE
                        }
                        else {
                            GameFavList!!.clear()
                            GameFavList!!.addAllItem(if (it.data.size>12) it.data.subList(0,12) else it.data)
                            gamebinding!!.myfavRVList.visibility = View.VISIBLE
                        }
                    }
                }
                }
                Status.ERROR->{

                }
                Status.LOADING->{

                }

            }
        })
        getrecentGameData()
    }

    private fun getFavGameData() {
        viewModel.getGameFavList(getUserId())
    }

    fun refreshGameFavData() {
        getFavGameData()
    }



    private fun getFavListLayout() {
         GameFavList= MyGamesFavListAdapter(this)
        gamebinding!!.myfavRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = GameFavList
        }
    }

    private fun GetGames() {
        getSharedPreference().saveString("StartGameLoading","0")
        getSharedPreference().getUserId().let {
            viewModel.getAllGames(getUserId(),"","","", this, object : ApiListener<Games, Any?> {
                override fun onSuccess(t: Games?, mess: String?) {
                    val listData = t!!.data.size
                    val tempList: ArrayList<Data> = ArrayList()
                    val bannerdata: ArrayList<Data> = ArrayList()

                    val ActionGame: ArrayList<Data> = ArrayList()
                    val AdventureGame: ArrayList<Data> = ArrayList()
                    val PuzzleGame: ArrayList<Data> = ArrayList()
                    val LogicGame: ArrayList<Data> = ArrayList()
                    val SportsGame: ArrayList<Data> = ArrayList()
                    val RacingGame: ArrayList<Data> = ArrayList()
                    val BattleGame: ArrayList<Data> = ArrayList()
                    val CricketGame: ArrayList<Data> = ArrayList()
                    val CardsGame: ArrayList<Data> = ArrayList()
                    val ZombiesGame: ArrayList<Data> = ArrayList()
                    val StrategyGame: ArrayList<Data> = ArrayList()
                    val ArcadeGame: ArrayList<Data> = ArrayList()
                    val Sports_RacingGame: ArrayList<Data> = ArrayList()
                    val Puzzle_LogicGame: ArrayList<Data> = ArrayList()
                    val FeaturedGame: ArrayList<Data> = ArrayList()


                    t.let {
                        var i = 0
                        tempList.addAll(it.data)
                        tempList.shuffled()
                        tempList.shuffled().forEach {
                            if (i <= 5) {
                                bannerdata.add(it)
                                i++
                            }
                        }
                        setGamesBanner1(bannerdata)
                        it.data.shuffled().forEach {
                            if (it.trending_game.contains("1")) {
                                TrendingGames!!.addItem(it)

                            }
                        }
                        it.data.shuffled().forEach {
                            if (it.trending_game.contains("1")) {
                                MyTrendingGames!!.addItem(it)

                            }
                        }

                        val myRandomValues = /*this is for showing how much data u want*/
                            List(12) { Random.nextInt(0, listData) }
                        myRandomValues.forEach { pos ->
                            AllPopular!!.addItem(it.data[pos])
                        }
                        val myRandomValues1 = /*this is for showing how much data u want*/
                            List(12) { Random.nextInt(0, listData) }
                        myRandomValues1.forEach { pos ->
                            mypopularGames!!.addItem(it.data[pos])
                        }

                        for (i in 8 downTo 1) {
                            NewlyAddedGames!!.addItem(it.data[listData - i])
                        }

                    }
                    t.let {
                        it.data.forEach { data ->
                            data.categories.forEach {

                                when(it.id){
                                    "1"->{
                                        ActionGame.add(data)
                                    }
                                    "2"->{
                                        AdventureGame.add(data)
                                    }
                                    "3"->{
                                        PuzzleGame.add(data)
                                    }
                                    "4"->{
                                        LogicGame.add(data)
                                    }
                                    "5"->{
                                        SportsGame.add(data)
                                    }
                                    "6"->{
                                        RacingGame.add(data)
                                    }
                                    "7"->{
                                        BattleGame.add(data)
                                    }
                                    "10"->{
                                        CricketGame.add(data)
                                    }
                                    "11"->{
                                        CardsGame.add(data)
                                    }
                                    "12"->{
                                        ZombiesGame.add(data)
                                    }
                                    "18"->{
                                        StrategyGame.add(data)
                                    }
                                    "19"->{
                                        ArcadeGame.add(data)
                                    }
                                    "20"->{
                                        Sports_RacingGame.add(data)
                                    }
                                    "21"->{
                                        Puzzle_LogicGame.add(data)
                                    }
                                    "22"->{
                                        FeaturedGame.add(data)
                                    }
                                }

                                /*

                                if (it.id.toString().equals("1")) {
                                    ActionGame.add(data)
                                } else if (it.id.toString().equals("2")) {
                                    AdventureGame.add(data)
                                } else if (it.id.toString().equals("3")) {
                                    PuzzleGame.add(data)
                                } else if (it.id.toString().equals("4")) {
                                    LogicGame.add(data)
                                } else if (it.id.toString().equals("5")) {
                                    SportsGame.add(data)
                                } else if (it.id.toString().equals("6")) {
                                    RacingGame.add(data)
                                } else if (it.id.toString().equals("7")) {
                                    BattleGame.add(data)
                                } else if (it.id.toString().equals("10")) {
                                    CricketGame.add(data)
                                } else if (it.id.toString().equals("11")) {
                                    CardsGame.add(data)
                                } else if (it.id.toString().equals("12")) {
                                    ZombiesGame.add(data)
                                }else if (it.id.toString().equals("18")) {
                                    StrategyGame.add(data)
                                }else if (it.id.toString().equals("19")) {
                                    ArcadeGame.add(data)
                                }else if (it.id.toString().equals("20")) {
                                    Sports_RacingGame.add(data)
                                }else if (it.id.toString().equals("21")) {
                                    Puzzle_LogicGame.add(data)
                                }else if (it.id.toString().equals("22")) {
                                    FeaturedGame.add(data)
                                }


                                 */
                            }
                        }
                    }

                    if (ActionGame.isNullOrEmpty()) {
                        gamebinding!!.Action.visibility = View.GONE
                    }
                    if (AdventureGame.isNullOrEmpty()) {
                        gamebinding!!.AdventureLayout.visibility = View.GONE
                    }
                    if (PuzzleGame.isNullOrEmpty()) {
                        gamebinding!!.PuzzleLayout.visibility = View.GONE
                    }
                    if (LogicGame.isNullOrEmpty()) {
                        gamebinding!!.LogicLayout.visibility = View.GONE
                    }
                    if (SportsGame.isNullOrEmpty()) {
                        gamebinding!!.SportsLayout.visibility = View.GONE
                    }
                    if (RacingGame.isNullOrEmpty()) {
                        gamebinding!!.RacingLayout.visibility = View.GONE
                    }
                    if (BattleGame.isNullOrEmpty()) {
                        gamebinding!!.BattleLayout.visibility = View.GONE
                    }
                    if (CricketGame.isNullOrEmpty()) {
                        gamebinding!!.CricketLayout.visibility = View.GONE
                    }
                    if (CardsGame.isNullOrEmpty()) {
                        gamebinding!!.CardsLayout.visibility = View.GONE
                    }
                    if (ZombiesGame.isNullOrEmpty()) {
                        gamebinding!!.ZombiesLayout.visibility = View.GONE
                    }
                    if (StrategyGame.isNullOrEmpty()) {
                        gamebinding!!.StrategyLayout.visibility = View.GONE
                    }
                    if (ArcadeGame.isNullOrEmpty()) {
                        gamebinding!!.ArcadeLayout.visibility = View.GONE
                    }
                    if (Sports_RacingGame.isNullOrEmpty()) {
                        gamebinding!!.SportsRacingLayout.visibility = View.GONE
                    }
                    if (Puzzle_LogicGame.isNullOrEmpty()) {
                        gamebinding!!.PuzzleLogicLayout.visibility = View.GONE
                    }
                    if (FeaturedGame.isNullOrEmpty()) {
                        gamebinding!!.FeaturedLayout.visibility = View.GONE
                    }


                    actionGame!!.addAllItem(if (ActionGame.size > 12) ActionGame.subList(0, 12) else ActionGame)
                    Adventure!!.addAllItem(if (AdventureGame.size > 12) AdventureGame.subList(0, 12) else AdventureGame)
                    Puzzle!!.addAllItem(if (PuzzleGame.size > 12) PuzzleGame.subList(0, 12) else PuzzleGame)
                    Logic!!.addAllItem(if (LogicGame.size > 12) LogicGame.subList(0, 12) else LogicGame)
                    Sports!!.addAllItem(if (SportsGame.size > 12) SportsGame.subList(0, 12) else SportsGame)
                    Racing!!.addAllItem(if (RacingGame.size > 12) RacingGame.subList(0, 12) else RacingGame)
                    Battle!!.addAllItem(if (BattleGame.size > 12) BattleGame.subList(0, 12) else BattleGame)
                    Cricket!!.addAllItem(if (CricketGame.size > 12) CricketGame.subList(0, 12) else CricketGame)
                    Cards!!.addAllItem(if (CardsGame.size > 12) CardsGame.subList(0, 12) else CardsGame)
                    Zombies!!.addAllItem(if (ZombiesGame.size > 12) ZombiesGame.subList(0, 12) else ZombiesGame)
                    Strategy!!.addAllItem(if (StrategyGame.size > 12) StrategyGame.subList(0, 12) else StrategyGame)
                    Arcade!!.addAllItem(if (ArcadeGame.size > 12) ArcadeGame.subList(0, 12) else ArcadeGame)
                    Sports_Racing!!.addAllItem(if (Sports_RacingGame.size > 12) Sports_RacingGame.subList(0, 12) else Sports_RacingGame)
                    Puzzle_Logic!!.addAllItem(if (Puzzle_LogicGame.size > 12) Puzzle_LogicGame.subList(0, 12) else Puzzle_LogicGame)
                    Featured!!.addAllItem(if (FeaturedGame.size > 12) FeaturedGame.subList(0, 12) else FeaturedGame)
                    getSharedPreference().saveString("StartGameLoading","1")

                    homeVisible()


                }
            })
        }
    }

    private fun homeVisible() {
        gamebinding!!.progress.visibility = View.GONE
        gamebinding!!.lowconnection.visibility = View.GONE
        gamebinding!!.mainLayout.visibility = View.VISIBLE
    }

    private fun setGamesBanner1(tempList: ArrayList<Data>) {
        if (tempList.isNullOrEmpty()) {
            gamebinding!!.bannerTop.visibility = View.GONE
        } else {
            gamebinding!!.bannerTop.visibility = View.VISIBLE
        }

        val imageList = ArrayList<GamebannerModel>()
        GamesBanner = tempList
        for (x in tempList) {
            imageList.add(
                GamebannerModel(
                    x.assets.brick,
                    x.url,
                    x.id,
                    x.name,
                    x.gamePlays,
                    x.description
                )
            )
        }
        gamebinding!!.bannerTop.adapter = TopBannerGameAdapter(imageList, gamebinding!!.bannerTop,
            object : RecyclerClickListener { override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data.toString().contains("https://")) {
                    tempList.forEach {
                        if (it.url == data) {
                            GameWebViewActivity.openWebView(context, it.url, it.id, it.name, it.assets.square, "")
                        }
                    }

                } else if (data.toString().contains("http://")) {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(data.toString()))
                    startActivity(browserIntent)
                }
            }

            })
        gamebinding!!.bannerTop.clipToPadding = false
        gamebinding!!.bannerTop.clipChildren = false
        gamebinding!!.bannerTop.offscreenPageLimit = 3

        gamebinding!!.bannerTop.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        //  _binding1!!.GamesBanner.setPadding(40, 0, 40, 0)

        //Handler Start
        Handler().apply {
            val runnable = object : Runnable {
                override fun run() {
                    var i = gamebinding!!.bannerTop.currentItem

                    if (i == tempList.size - 1) {
                        i = -1// 0
                        gamebinding!!.bannerTop.currentItem = i
                    }
                    i++
                    gamebinding!!.bannerTop.setCurrentItem(i, true)
                    postDelayed(this, 4000)
                }
            }
            postDelayed(runnable, 4000)
        }


    }

    //Super links
    private fun setMoreTextSuperLinks(moreTex: String) {
        val CustomeGamesCategory: ArrayList<GameCustomeSuperCategoryModel> = ArrayList()
        CustomeGamesCategory.add(GameCustomeSuperCategoryModel(R.drawable.online_fav_icon, "My Favourites", "My Favourites",""))
        CustomeGamesCategory.add(GameCustomeSuperCategoryModel(R.drawable.trending_games, "Trending Games", "Trending Games",""))
        CustomeGamesCategory.add(GameCustomeSuperCategoryModel(R.drawable.offer_and_coupons_icon, "New Games", "New Games",""))
        CustomeGamesCategory.add(GameCustomeSuperCategoryModel(R.drawable.lastplayed, "Last Played", "Last Played",""))
        CustomeGamesCategory.add(GameCustomeSuperCategoryModel(R.drawable.popular_terding_games, "Popular Games", "Popular Games",""))
  //      CustomeGamesCategory.add(GameCustomeSuperCategoryModel(R.drawable.online_store_categories, "Categories", "Categories",""))
        customGamesCategory!!.addAllItem(CustomeGamesCategory)


/*
        if (moreTex == "Show more") {
            customShopCategory!!.clear()
            customShopCategory!!.addAllItem(CustomeShopCategory)

            customShopCategory!!.addItem(GameCustomeSuperCategoryModel(R.drawable.superlinks_upicon, "Less", "Show less",""))
            moreText ="Show less"
        }else{
            customShopCategory!!.clear()
            customShopCategory!!.addAllItem(if (CustomeShopCategory.size>=6) CustomeShopCategory.subList(0,5) else CustomeShopCategory)
            customShopCategory!!.addItem(GameCustomeSuperCategoryModel(R.drawable.down_2_icon, "More", "Show more",""))
            moreText ="Show more"
        }
  */

    }

    private fun superlinks() {
        customGamesCategory = GamesSuperLinkAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "My Favourites" -> {
                        ContainerActivity.openContainer(requireContext(), "GamesFavouriteFragment", "")
                    }
                    "Trending Games" -> {
                        ContainerActivity.openContainer(context, "TrandingGames", "TrandingGames", false, "")
                    }
                    "New Games" -> {
                        ContainerActivity.openContainer(context, "NewlyAdded", "NewlyAdded", false, "")
                    }
                    "Last Played" -> {
                        ContainerActivity.openContainer(context, "GamesHistory", "GamesHistory", false, "")
                    }
                    "Popular Games" -> {
                        ContainerActivity.openContainer(context, "MostPlayed", "MostPlayed", false, "")
                    }
                }
            }

        })

        gamebinding!!.gameSuperCategories.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = customGamesCategory
        }
    }

    //Recent Games
    private fun getRecentGameplayList() {
        viewModel.getRecentDataList().observe(viewLifecycleOwner, {
                when (it.status){
                    Status.SUCCESS->{it!!.data.let {t->
                        t!!.let {
                            recentPlayedList(it)
                        }
                      }
                    }
                    Status.ERROR->{

                    }
                    Status.LOADING->{

                    }

                }
        })
        getrecentGameData()
    }

    private fun getrecentGameData() {
        viewModel.getRecentDataList(getUserId())
    }

    fun refreshGameRecentData() {
        getrecentGameData()
    }


    override fun onResume() {
        super.onResume()
        refreshGameRecentData()
        refreshGameFavData()
    }

    private fun recentPlayedList(it : GameRecentPlayList) {
        val RecentGameListAdapter=GetRecentlyPlayedGameAdapterList(this)

        gamebinding!!.recyclerPlayAgain.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecentGameListAdapter
        }
        if (it.data.isNullOrEmpty()) {
            gamebinding!!.recentlist.visibility = View.GONE
        } else {
            it.data.let {
                gamebinding!!.recentlist.visibility = View.VISIBLE
                RecentGameListAdapter.addAllItem(
                    if (it.size > 10) {
                        it.subList(0, 10)
                    } else {
                        it
                    }
                )
            }
        }
    }

    //categories
    private fun setCategorydata() {
        gamecategory = GamezopCategoriAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is app.tapho.ui.games.models.GamezopCategory.Data){

                    ContainerActivity.openContainer(requireContext(),"OpenGamesCategory",data.id,false,data.name)
                }

            }
        })
        gamebinding!!.recyclerCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = gamecategory
        }
    }

    private fun GetGameCategory() {
        viewModel.getGamezopCategoryData(getUserId(), this, object : ApiListener<GamezopCategoryData,Games>{
            override fun onSuccess(t: GamezopCategoryData?, mess: String?) {
                gamecategory!!.addAllItem(t!!.data)
            }

        })

    }

    //trending
    private fun trendingGames() {
        TrendingGames = GamezopTrendingGameAdapter(this)
        gamebinding!!.recyclerTrendingGames.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = TrendingGames
        }
    }
  //trending
    private fun MytrendingGames() {
         MyTrendingGames = PopularBrandAdapter(this)
        gamebinding!!.mytrendingGames.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = MyTrendingGames
        }
    }

    // Popular
    private fun AllPopulardata() {
        AllPopular = PopularBrandAdapter(this)
        gamebinding!!.recyclerPopularGames.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = AllPopular
        }
    }
  // mypopularGames
    private fun mypopularGamesdata() {
      mypopularGames = ActionGameCategoryAdapter(this)
      gamebinding!!.mypopularGames.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mypopularGames
        }
    }

    //newly Added
    private fun newlyAddedGames() {
        NewlyAddedGames = newgames_Adapter(this)
        gamebinding!!.recyclerNewlyAdded.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NewlyAddedGames
        }
    }

    private fun actionGame() {
        actionGame = ActionGameCategoryAdapter(this)
        gamebinding!!.ActionGame.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = actionGame
        }
    }

    private fun AdventureGame() {
        Adventure = AdventureGameCategoryAdapter(this)
        gamebinding!!.Adventure.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Adventure
        }
    }

    private fun PuzzleGame() {
        Puzzle = PuzzleGameCategoryAdapter(this)
        gamebinding!!.Puzzle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Puzzle
        }
    }

    private fun LogicGame() {
        Logic = LogicGameCategoryAdapter(this)
        gamebinding!!.Logic.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Logic
        }
    }

    private fun SportsGame() {
        Sports = SportsGameCategoryAdapter(this)
        gamebinding!!.Sports.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Sports
        }
    }

    private fun RacingGame() {
        Racing = RacingGameCategoryAdapter(this)
        gamebinding!!.Racing.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Racing
        }
    }

    private fun BattleGame() {
        Battle = BattleGameCategoryAdapter(this)
        gamebinding!!.Battle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Battle
        }
    }

    private fun CricketGame() {
        Cricket = CricketGameCategoryAdapter(this)
        gamebinding!!.Cricket.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Cricket
        }
    }

    private fun CardsGame() {
        Cards = CardsGameCategoryAdapter(this)
        gamebinding!!.Cards.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Cards
        }
    }

    private fun ZombiesGame() {
        Zombies = ZombiesGameCategoryAdapter(this)
        gamebinding!!.Zombies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Zombies
        }
    }

    private fun StrategysGame() {
        Strategy = StrategyGameCategoryAdapter(this)
        gamebinding!!.Strategy.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Strategy
        }
    }

    private fun ArcadesGame() {
        Arcade = ArcadeGameCategoryAdapter(this)
        gamebinding!!.Arcade.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Arcade
        }
    }

    private fun Sports_RacingsGame() {
        Sports_Racing = SportsRacingGameCategoryAdapter(this)
        gamebinding!!.SportsRacing.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Sports_Racing
        }
    }

    private fun Puzzle_LogicsGame() {
        Puzzle_Logic = Puzzle_logicGameCategoryAdapter(this)
        gamebinding!!.PuzzleLogic.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Puzzle_Logic
        }
    }

    private fun FeaturedsGame() {
        Featured = FeaturedGameCategoryAdapter(this)
        gamebinding!!.Featured.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Featured
        }
    }


    fun lowConnection() {
        gamebinding!!.lowconnection.visibility = View.VISIBLE
        gamebinding!!.mainLayout.visibility = View.GONE
        gamebinding!!.progress.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {


    }
}