package app.tapho.ui.home.GamesContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSeeAllNewlyaddedBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.NewlyAddedGamesAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games


class SeeAllNewlyaddedFragment : BaseFragment<FragmentSeeAllNewlyaddedBinding>(),
    ApiListener<Games, Any?>, RecyclerClickListener {
    private var Newlyadded: NewlyAddedGamesAdapter<Data>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingSeeAll = FragmentSeeAllNewlyaddedBinding.inflate(inflater, container, false)
        viewModeldata()
        statusBarColor(R.color.white)
        statusBarTextWhite()
        NewlyAdded()
        bindingSeeAll!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        return bindingSeeAll?.root
    }

    private fun viewModeldata() {
        getSharedPreference().getUserId().let {  viewModel.getAllGames(getUserId(),"","","", this, this)  }
    }

    private fun NewlyAdded() {
        Newlyadded = NewlyAddedGamesAdapter(this)
        bindingSeeAll!!.recyclerNewlyAdded.apply {
            layoutManager= GridLayoutManager(activity,3)
            adapter = Newlyadded
        }
        bindingSeeAll!!.recyclerNewlyAdded.setNestedScrollingEnabled(false);
    }



    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SeeAllNewlyaddedFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: Games?, mess: String?) {
        val listData = t!!.data.size
        t.let { Games ->
            //Popular
                for (i in 50 downTo  1) {
                    Newlyadded!!.addItem(Games.data[listData - i])
                }

            bindingSeeAll!!.progress.visibility = View.GONE
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}