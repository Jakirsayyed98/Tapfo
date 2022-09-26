package app.tapho.ui.home.NewGame

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentGameCategoyBinding
import app.tapho.databinding.FragmentGameContainerBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.GameContainerFragment
import app.tapho.ui.games.adapter.GameCategoryContainerAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.GAME_ID
import com.google.gson.Gson


class GameCategoyFragment : BaseFragment<FragmentGameCategoyBinding>(),
    ApiListener<Games, Any?>,
    RecyclerClickListener {
    private var gamecategories: GameCategoryContainerAdapter<Data>? = null//

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
        bindingGameCategory = FragmentGameCategoyBinding.inflate(inflater, container, false)
        initdata()
        viewmodeldata()
        statusBarColor(R.color.white)
        return bindingGameCategory?.root
    }

    private fun viewmodeldata() {
        viewModel.getAllGames(AppSharedPreference.getInstance(requireContext()).getUserId(), this, this)
    }

    private fun initdata() {
        gamecategories = GameCategoryContainerAdapter(this)
        bindingGameCategory!!.GamesOfCategories.apply {
            //  layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 3)
            adapter = gamecategories

        }
    }



    override fun onSuccess(t: Games?, mess: String?) {
        val Categoryid =GameHomeFragment.CategoryID

        t!!.data.forEach { data ->

            data.categories.forEach { categories ->
                if (Categoryid.toString().equals(categories.id) && categories.status.contains("1")) {
                    gamecategories!!.addItem(data)
                }
            }
        }
    }



    companion object {

        @JvmStatic
        fun newInstance() =
            GameCategoyFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}