package app.tapho.NavSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentFavoriteNavBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.favourite.FavouriteDialogFragment
import app.tapho.ui.games.adapter.GamesFavListAdapter
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.home.SearchDialogFragment
import app.tapho.ui.home.adapter.*
import app.tapho.ui.model.*
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.OPEN_WEB_VIEW
import app.tapho.viewmodels.FavouriteViewModel

// TODO: Rename parameter arguments, choose names that match

class Fragment_favorite_nav : BaseFragment<FragmentFavoriteNavBinding>(), ApiListener<HomeRes, Any?>,
    RecyclerClickListener {
    private var favViewModel: FavouriteViewModel? = null
    private var appCategoryList: ArrayList<AppCategory>? = null
    private var itemTypeAdapter: ItemTypeAdapter? = null
    private var partnerCashbackAdapter: cashback_fav_adapter<NewCashback>? = null
    private var popularList: ArrayList<Popular>? = null
    private var cashbackMerchantFragmentAdapter: PagerFragmentAdapter? = null
    private var GameFavList: GamesFavListAdapter? = null

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
        favBinding= FragmentFavoriteNavBinding.inflate(inflater, container, false)
        favViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        getData()
        setFavourites()
        setRecyclerCashbackPartner()
        statusBarColor(R.color.white)
        statusBarTextWhite()
        favBinding!!.store.isSelected = true
        favBinding!!.game.isSelected = false

        favBinding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        favBinding!!.newCashbackPartnerMoreIv.setOnClickListener {
            openContainer(
                getString(R.string.cashback_merchants),
                null, false, getString(R.string.cashback_merchants)
            )
        }

        favBinding!!.search.setOnClickListener {
            openAllPopularCashbackMerchants("1")
        }
        favBinding!!.store.setOnClickListener {
            favBinding!!.store.isSelected = true
            favBinding!!.game.isSelected = false
            favBinding!!.storePage.visibility = View.VISIBLE
            favBinding!!.gamepage.visibility = View.GONE
        }
        favBinding!!.game.setOnClickListener {
            favBinding!!.store.isSelected = false
            favBinding!!.game.isSelected = true
            favBinding!!.storePage.visibility = View.GONE
            favBinding!!.gamepage.visibility = View.VISIBLE
        }

        FavouritesGames()
        setGameFavList()
        favBinding?.addMoreTv?.visibility = View.GONE
        favBinding?.addMoreTv?.setOnClickListener {
            if (appCategoryList.isNullOrEmpty().not()){
                FavouriteDialogFragment.newInstance(appCategoryList).show(childFragmentManager, null)
            }
        }
        return favBinding?.root
    }

    private fun setGameFavList() {
        GameFavList = GamesFavListAdapter(this)
        favBinding!!.gamesrecyclerFavourites.apply {
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = GameFavList
        }
    }

    private fun FavouritesGames() {
        viewModel.getGameFavList(getUserId(),this,object :ApiListener<getGameFavList,Any?>{
            override fun onSuccess(t: getGameFavList?, mess: String?) {
                t.let {
                    it!!.data.let {
                        GameFavList!!.addAllItem(it)
                    }
                }

            }

        })
    }

    private fun getData() {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(), this, this)
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            Fragment_favorite_nav()

    }

    private fun openAllPopularCashbackMerchants(type: String) {
        val list = ArrayList<Popular>()
        popularList?.let {
            list.addAll(it)
        }
        if (type == "1")
            SearchDialogFragment.showSearch(childFragmentManager, list)
        else
            openContainer(
                getString(R.string.popular_merchants),
                list,
                true,
                getString(R.string.popular_merchants)
            )
    }

    private fun setRecyclerCashbackPartner() {
        partnerCashbackAdapter = cashback_fav_adapter(this)
        favBinding!!.recyclerCashbackpartner.apply {
            layoutManager = GridLayoutManager(context, 5)
            favBinding!!.recyclerCashbackpartner.stopScroll()
//            layoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
            adapter = partnerCashbackAdapter
        }
    }
    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    override fun onSuccess(t: HomeRes?, mess: String?) {
        t?.let {

            it.new_cashback?.let { newCashBack ->
                partnerCashbackAdapter?.addAllItem(newCashBack)
            }
            appCategoryList = it.app_category
            favBinding?.addMoreTv?.visibility = View.VISIBLE
            setAppCategory(getString(R.string.more))

        }
    }

    private fun setAppCategory(moreText: String) {
        itemTypeAdapter?.clear()
        appCategoryList?.let { appCategory ->
            if (appCategory.size > 8 && moreText == getString(R.string.more))
                itemTypeAdapter?.addAllItem(
                    appCategory.subList(0, 7).toList()
                )
            else
                itemTypeAdapter?.addAllItem(appCategory)
            itemTypeAdapter?.addItem(
                AppCategory(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    moreText,
                    "",
                    false, null
                )
            )
        }
    }


    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "AppCategory" && data is AppCategory) {
            when (data.name) {
                getString(R.string.more) -> {
                    setAppCategory(getString(R.string.less))
                }
                getString(R.string.less) -> {
                    setAppCategory(getString(R.string.more))
                }
                else -> openContainer("MiniAppFragment", data, false, data.name)
            }
        } else if (type == "MiniAppFragment") {
            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
        } else if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp)
                openWebView(data)
        }
    }

    private fun openWebView(data: MiniApp) {
        ActiveCashbackForWebActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id,
            data.url_type,data.name
        )
    }

    private fun setFavourites() {
       val mAdapter = FavouriteHomeAdapter(this)

        favBinding!!.recyclerFavourites.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        favViewModel?.getData()?.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS) {
                mAdapter.clear()
                it.data?.let { it1 -> mAdapter.addAllItem(it1) }
            }
        })
        getFevouriteData()
    }

    private fun getFevouriteData() {
        favViewModel?.getFavouriteList(getUserId())
    }

    fun refreshFavData() {
        getFevouriteData()
    }
}
