package app.tapho.ui.home.NewGame

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentGameHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.adapter.GamezopCategoriAdapter
import app.tapho.ui.games.adapter.GamezopTrendingGameAdapter
import app.tapho.ui.games.adapter.NewlyAddedGamesAdapter
import app.tapho.ui.games.adapter.PopularBrandAdapter
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.NewGame.Adapter.TopBannerGameAdapter
import app.tapho.ui.home.NewGame.Model.GamebannerModel
import app.tapho.utils.AppSharedPreference
import kotlin.random.Random


class GameHomeFragment : BaseFragment<FragmentGameHomeBinding>(),
    ApiListener<GamezopCategoryData, Games>,
    RecyclerClickListener {

    private var gamecategory: GamezopCategoriAdapter<app.tapho.ui.games.models.GamezopCategory.Data>? = null

    private var GamesBanner: ArrayList<Data>? = null

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity?.window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        activity?.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        statusBarTextBlack()
        GetGames()
        GetGameCategory()
        setCategorydata()
        addFragment(ForYouGamesFragment.newInstance(), "")

        gamebinding!!.favouritesBtn.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "favouritesBtn", "favouritesBtn")
        }

        gamebinding!!.backIv.setOnClickListener {
            activity?.onBackPressed()
        }

        gamebinding!!.search.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "GameSearch", "GameSearch")
        }

        gamebinding!!.notificationRe.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "GameNotification", "GameNotification")
        }

        // Inflate the layout for this fragment
        return gamebinding?.root
    }


    private fun setCategorydata() {
        gamecategory = GamezopCategoriAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                val bundle = Bundle()
                bundle.putString("CategoryID", data.toString())
                if (type.toString().equals("0")) {
                    addFragment(ForYouGamesFragment.newInstance(), "")
                } else {
                    CategoryID = data.toString()
                    addFragment(GameCategoyFragment.newInstance(), data.toString())
                }
            }

        })
        gamebinding!!.recyclerCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = gamecategory
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
                    var tempList: ArrayList<Data> = ArrayList()
                    var bannerdata: ArrayList<Data> = ArrayList()
                    t.let {
                        var i = 0
                        tempList.addAll(it!!.data)
                        tempList.shuffled()
                        tempList.shuffled().forEach {
                            if (i <= 5) {
                                bannerdata.add(it)
                                i++
                            }
                        }
                        setGamesBanner1(bannerdata)
                    }

                }
            })
        }
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
        tempList.forEach {
            Log.e("DATATRY", "${it.name}")
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


    private fun addFragment(fragment: Fragment, Data: String) {
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.Gamecontainer, fragment, Data).commit()
    }


    companion object {

        var CategoryID = ""

        @JvmStatic
        fun newInstance() =
            GameHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: GamezopCategoryData?, mess: String?) {
        var tempList: ArrayList<Data> = ArrayList()
        gamecategory!!.apply {
            addItem(
                app.tapho.ui.games.models.GamezopCategory.Data(
                    tempList,
                    "",
                    "0",
                    "",
                    "For You",
                    "",
                    true
                )
            )
            addAllItem(t!!.data)
        }
//        gamecategory!!.addAllItem(t!!.data)

    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}