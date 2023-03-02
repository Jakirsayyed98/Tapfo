package app.tapho.ui.home.GamesContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSeeAllTrandingGamesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.NewlyAddedGamesAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.utils.DATA

class SeeAllTrandingGamesFragment : BaseFragment<FragmentSeeAllTrandingGamesBinding>(),
    ApiListener< Games,Any?>,RecyclerClickListener{
    private var TrendingGames: NewlyAddedGamesAdapter<Data>? = null

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
        bindingSeeAll = FragmentSeeAllTrandingGamesBinding.inflate(inflater, container, false)

        statusBarColor(R.color.white)
        statusBarTextWhite()
        viewModeldata()
        trendingGames()
        bindingSeeAll!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return bindingSeeAll?.root
    }

    private fun viewModeldata() {
        getSharedPreference().getUserId().let {
            viewModel.getAllGames(getUserId(),"","","", this, this)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SeeAllTrandingGamesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: Games?, mess: String?) {

        t.let { Games ->
            //Popular
            Games!!.data.forEach {
                if (it.trending_game.contains("1")) {
                    TrendingGames!! addItem (it)
                }
            }
            bindingSeeAll!!.progress.visibility = View.GONE
        }
    }
    private fun trendingGames() {
        TrendingGames = NewlyAddedGamesAdapter(this)
        bindingSeeAll!!.AllTrandingGames.apply {
            layoutManager=GridLayoutManager(activity,3)
            adapter = TrendingGames
        }
        bindingSeeAll!!.AllTrandingGames.setNestedScrollingEnabled(false);
    }
    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }


}