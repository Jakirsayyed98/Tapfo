package app.tapho.ui.home.GamesContainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMostplayedBinding
import app.tapho.databinding.FragmentSeeAllNewlyaddedBinding
import app.tapho.databinding.FragmentSeeAllPopularBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.NewlyAddedGamesAdapter
import app.tapho.ui.games.adapter.PopularBrandAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games

class MostplayedFragment : BaseFragment<FragmentMostplayedBinding>(),
    ApiListener<Games, Any?>, RecyclerClickListener {
    private var MostPlayed: NewlyAddedGamesAdapter<Data>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingSeeAll = FragmentMostplayedBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        viewModeldata()
        MostPlayed()
        bindingSeeAll!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return bindingSeeAll?.root
    }

    private fun viewModeldata() {
        getSharedPreference().getUserId().let {  viewModel.getAllGames(getUserId(),"","","", this, this)  }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MostplayedFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: Games?, mess: String?) {
        t.let {
            it!!.data.let {
                it.shuffled().let {
                    MostPlayed!!.addAllItem(
                        if (it.size > 50) {
                            it.subList(0, 50)
                        } else {
                            it
                        })
                }

                bindingSeeAll!!.progress.visibility = View.GONE
            }
        }
    }

    private fun MostPlayed() {
        MostPlayed = NewlyAddedGamesAdapter(this)
        bindingSeeAll!!.mostplayedrGames.apply {
            layoutManager= GridLayoutManager(activity,3)
            adapter = MostPlayed
        }
        bindingSeeAll!!.mostplayedrGames.setNestedScrollingEnabled(false);
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }

}