package app.tapho.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentGamesFavouriteBinding
import app.tapho.databinding.FragmentGamesForYouBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.GamesFavListAdapter
import app.tapho.ui.games.adapter.GamezopCategoriAdapter
import app.tapho.ui.games.models.GameFavandUnFav.GameFavList.getGameFavList
import app.tapho.ui.games.models.GamezopCategory.GamezopCategoryData
import app.tapho.ui.games.models.getGames.Games
import app.tapho.utils.AppSharedPreference


class GamesFavouriteFragment : BaseFragment<FragmentGamesFavouriteBinding>(),
    ApiListener<getGameFavList, Any?>,
    RecyclerClickListener {
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
        bindingGamesFavList = FragmentGamesFavouriteBinding.inflate(inflater, container, false)
        viewmodeldata()
        setRecyclerBrand()
        return bindingGamesFavList?.root

    }



    companion object {

        @JvmStatic
        fun newInstance() =
            GamesFavouriteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun viewmodeldata() {
        viewModel.getGameFavList(AppSharedPreference.getInstance(requireContext()).getUserId(), this, this )

    }

    override fun onSuccess(t: getGameFavList?, mess: String?) {
        t.let {
            it?.data.let {
                GameFavList?.addAllItem(it!!)
            }
        }
    }
    private fun setRecyclerBrand() {
        GameFavList = GamesFavListAdapter(this)
        bindingGamesFavList!!.gamesCategories.apply { layoutManager = GridLayoutManager(context, 5 )
            adapter = GameFavList
            adapter!!.notifyDataSetChanged()
        }
    }
    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }


}