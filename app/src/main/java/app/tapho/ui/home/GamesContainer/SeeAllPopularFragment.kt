package app.tapho.ui.home.GamesContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.databinding.FragmentSeeAllPopularBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.NewlyAddedGamesAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games


class SeeAllPopularFragment :  BaseFragment<FragmentSeeAllPopularBinding>(),
    ApiListener<Games, Any?>, RecyclerClickListener {
    private var popularGames: NewlyAddedGamesAdapter<Data>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingSeeAll = FragmentSeeAllPopularBinding.inflate(inflater, container, false)
        viewModeldata()
        Popular()
        return bindingSeeAll?.root
    }

    private fun viewModeldata() {
        getSharedPreference().getUserId().let {  viewModel.getAllGames(getUserId(),"","","", this, this)  }
    }

    private fun Popular() {
        popularGames = NewlyAddedGamesAdapter(this)
        bindingSeeAll!!.popularGames.apply {
            layoutManager= GridLayoutManager(activity,2)
            adapter = popularGames
        }
        bindingSeeAll!!.popularGames.setNestedScrollingEnabled(false);
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SeeAllPopularFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: Games?, mess: String?) {
        t.let { Games ->
            //Popular
            Games!!.data.forEach {
                if (it.popular_game.contains("1")) {
                    popularGames!!.addItem (it)
                }
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}