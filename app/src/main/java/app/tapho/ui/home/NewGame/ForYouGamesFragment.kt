package app.tapho.ui.home.NewGame

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentForYouGamesBinding
import app.tapho.databinding.FragmentGameHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.adapter.*
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.NewGame.Adapter.TopBannerGameAdapter
import app.tapho.ui.home.NewGame.Model.GamebannerModel
import app.tapho.utils.AppSharedPreference
import kotlin.random.Random


class ForYouGamesFragment : BaseFragment<FragmentForYouGamesBinding>(),
    ApiListener<GamezopCategoryData, Games>,
    RecyclerClickListener {

    private var gamecategory: GamezopCategoriAdapter<app.tapho.ui.games.models.GamezopCategory.Data>? =
        null
    private var RecentGameListAdapter: GetRecentlyPlayedGameAdapterList? = null
    private var GamesBanner: ArrayList<Data>? = null
    private var TrendingGames: GamezopTrendingGameAdapter<Data>? = null//
//    private var NewlyAddedGames: NewlyAddedGamesAdapter<Data>? = null//
    private var NewlyAddedGames: PopularBrandAdapter<Data>? = null//
    private var AllPopular: PopularBrandAdapter<Data>? = null//


    private var actionGame: PopularBrandAdapter<Data>? = null//
    private var Adventure: PopularBrandAdapter<Data>? = null//
    private var Puzzle: PopularBrandAdapter<Data>? = null//
    private var Logic: PopularBrandAdapter<Data>? = null//
    private var Sports: PopularBrandAdapter<Data>? = null//
    private var Racing: PopularBrandAdapter<Data>? = null//
    private var Battle: PopularBrandAdapter<Data>? = null//
    private var Cricket: PopularBrandAdapter<Data>? = null//
    private var Cards: PopularBrandAdapter<Data>? = null//
    private var Zombies: PopularBrandAdapter<Data>? = null//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gamebinding = FragmentForYouGamesBinding.inflate(inflater, container, false)
        statusBarColor(R.color.black)
        statusBarTextBlack()
        GetGames()
        GetGameCategory()
        getRecentGameplayList()
        recentPlayedList()
        trendingGames()
        newlyAddedGames()
        AllPopulardata()
        ClickEventForAll()


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
        // Inflate the layout for this fragment
        return gamebinding?.root
    }

    private fun ClickEventForAll() {
        gamebinding!!.viewAllTv.setOnClickListener {
            ContainerActivity.openContainer(context, "MostPlayed", "MostPlayed", false, "")
        }
        gamebinding!!.morenewlyAddedGames.setOnClickListener {
            ContainerActivity.openContainer(context, "NewlyAdded", "NewlyAdded", false, "")
        }
        gamebinding!!.TrandingGames.setOnClickListener {
            ContainerActivity.openContainer(context, "TrandingGames", "TrandingGames", false, "")
        }

    }

    private fun AllPopulardata() {
        AllPopular = PopularBrandAdapter(this)
        gamebinding!!.recyclerPopularGames.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = AllPopular
        }
    }

    private fun newlyAddedGames() {
        NewlyAddedGames = PopularBrandAdapter(this)
        gamebinding!!.recyclerNewlyAdded.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            layoutManager = GridLayoutManager(context, 2)

            adapter = NewlyAddedGames
        }
    }

    private fun getRecentGameplayList() {
        getSharedPreference().getUserId().let {
            viewModel.getRecentDataList(getUserId(), this, object :
                ApiListener<GameRecentPlayList, Any?> {
                override fun onSuccess(t: GameRecentPlayList?, mess: String?) {
                    t!!.let {


                        if (it.data.isNullOrEmpty()) {
                            gamebinding!!.recentlist.visibility = View.GONE
                        } else {
                            it.data.let {
                                gamebinding!!.recentlist.visibility = View.VISIBLE
                                RecentGameListAdapter!!.addAllItem(
                                    if (it.size > 10) {
                                        it.subList(0, 10)

                                    } else {
                                        it
                                    }
                                )
                            }
                        }
                    }
                }

            })
        }
    }

    private fun recentPlayedList() {
        RecentGameListAdapter = GetRecentlyPlayedGameAdapterList(this)
        gamebinding!!.recyclerPlayAgain.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecentGameListAdapter
        }
    }


    private fun GetGameCategory() {
        viewModel.getGamezopCategoryData(
            AppSharedPreference.getInstance(requireContext()).getUserId(), this, this
        )

    }

    private fun GetGames() {
        getSharedPreference().getUserId().let {
            viewModel.getAllGames(getUserId(), this, object : ApiListener<Games, Any?> {
                override fun onSuccess(t: Games?, mess: String?) {
                    var listData = t!!.data.size
                    var List: ArrayList<Data> = ArrayList()

                    var ActionGame: ArrayList<Data> = ArrayList()
                    var AdventureGame: ArrayList<Data> = ArrayList()
                    var PuzzleGame: ArrayList<Data> = ArrayList()
                    var LogicGame: ArrayList<Data> = ArrayList()
                    var SportsGame: ArrayList<Data> = ArrayList()
                    var RacingGame: ArrayList<Data> = ArrayList()
                    var BattleGame: ArrayList<Data> = ArrayList()
                    var CricketGame: ArrayList<Data> = ArrayList()
                    var CardsGame: ArrayList<Data> = ArrayList()
                    var ZombiesGame: ArrayList<Data> = ArrayList()

                    t!!.data.shuffled().forEach {
                        if (it.trending_game.contains("1")) {
                            TrendingGames!!.addItem(it)

                        }
                    }
                    t.let {

                        val myRandomValues = /*this is for showing how much data u want*/
                            List(6) { Random.nextInt(0, listData) }
                        myRandomValues.forEach { pos ->
                            AllPopular!!.addItem(it.data[pos])
                        }

                        for (i in 6 downTo 1) {
                            NewlyAddedGames!!.addItem(it.data[listData - i])
                        }
                        it!!.data.forEach {
                            List.add(it)
                        }
                    }

                    t.let {
                        it.data.forEach { data ->
                            data.categories.forEach {

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
                                }
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
                    actionGame!!.addAllItem(
                        if (ActionGame.size > 12) ActionGame.subList(
                            0,
                            12
                        ) else ActionGame
                    )
                    Adventure!!.addAllItem(
                        if (AdventureGame.size > 12) AdventureGame.subList(
                            0,
                            12
                        ) else AdventureGame
                    )
                    Puzzle!!.addAllItem(
                        if (PuzzleGame.size > 12) PuzzleGame.subList(
                            0,
                            12
                        ) else PuzzleGame
                    )
                    Logic!!.addAllItem(
                        if (LogicGame.size > 12) LogicGame.subList(
                            0,
                            12
                        ) else LogicGame
                    )
                    Sports!!.addAllItem(
                        if (SportsGame.size > 12) SportsGame.subList(
                            0,
                            12
                        ) else SportsGame
                    )
                    Racing!!.addAllItem(
                        if (RacingGame.size > 12) RacingGame.subList(
                            0,
                            12
                        ) else RacingGame
                    )
                    Battle!!.addAllItem(
                        if (BattleGame.size > 12) BattleGame.subList(
                            0,
                            12
                        ) else BattleGame
                    )
                    Cricket!!.addAllItem(
                        if (CricketGame.size > 12) CricketGame.subList(
                            0,
                            12
                        ) else CricketGame
                    )
                    Cards!!.addAllItem(
                        if (CardsGame.size > 12) CardsGame.subList(
                            0,
                            12
                        ) else CardsGame
                    )
                    Zombies!!.addAllItem(
                        if (ZombiesGame.size > 12) ZombiesGame.subList(
                            0,
                            12
                        ) else ZombiesGame
                    )

                }
            })
        }
    }

    private fun actionGame() {
        actionGame = PopularBrandAdapter(this)
        gamebinding!!.ActionGame.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = actionGame
        }
    }

    private fun AdventureGame() {
        Adventure = PopularBrandAdapter(this)
        gamebinding!!.Adventure.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Adventure
        }
    }

    private fun PuzzleGame() {
        Puzzle = PopularBrandAdapter(this)
        gamebinding!!.Puzzle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Puzzle
        }
    }

    private fun LogicGame() {
        Logic = PopularBrandAdapter(this)
        gamebinding!!.Logic.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Logic
        }
    }

    private fun SportsGame() {
        Sports = PopularBrandAdapter(this)
        gamebinding!!.Sports.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Sports
        }
    }

    private fun RacingGame() {
        Racing = PopularBrandAdapter(this)
        gamebinding!!.Racing.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Racing
        }
    }

    private fun BattleGame() {
        Battle = PopularBrandAdapter(this)
        gamebinding!!.Battle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Battle
        }
    }

    private fun CricketGame() {
        Cricket = PopularBrandAdapter(this)
        gamebinding!!.Cricket.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Cricket
        }
    }

    private fun CardsGame() {
        Cards = PopularBrandAdapter(this)
        gamebinding!!.Cards.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Cards
        }
    }

    private fun ZombiesGame() {
        Zombies = PopularBrandAdapter(this)
        gamebinding!!.Zombies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = Zombies
        }
    }

    private fun trendingGames() {
        TrendingGames = GamezopTrendingGameAdapter(this)
        gamebinding!!.recyclerTrendingGames.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = TrendingGames
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ForYouGamesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: GamezopCategoryData?, mess: String?) {

    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}