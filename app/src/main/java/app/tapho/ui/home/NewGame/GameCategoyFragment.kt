package app.tapho.ui.home.NewGame

import android.content.Intent
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
import app.tapho.ui.games.adapter.GamezopCategoriAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.DATA
import app.tapho.utils.GAME_ID
import app.tapho.utils.TITLE
import com.google.gson.Gson


class GameCategoyFragment : BaseFragment<FragmentGameCategoyBinding>(),
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
        statusBarColor(R.color.white)
        statusBarTextWhite()

        bindingGameCategory!!.title.text = activity?.intent?.getStringExtra(TITLE)+" Games"
        bindingGameCategory!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }


        viewmodeldata()
        return bindingGameCategory?.root
    }

    private fun viewmodeldata() {
        val categoryID = activity?.intent?.getStringExtra(DATA)!!.drop(1).dropLast(1)

        viewModel.getAllGames(AppSharedPreference.getInstance(requireContext()).getUserId(),categoryID,"","",
            this, object: ApiListener<Games,Any?>{
            override fun onSuccess(t: Games?, mess: String?) {
                t.let {
                    initdata(it!!.data)
                }
            }
        })
    }

    private fun initdata(data : ArrayList<Data>) {

        gamecategories = GameCategoryContainerAdapter(this)
        bindingGameCategory!!.GamesOfCategories.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = gamecategories
        }

        gamecategories!!.addAllItem(data)
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