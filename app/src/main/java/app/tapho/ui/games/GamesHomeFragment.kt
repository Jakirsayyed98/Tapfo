package app.tapho.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentGamesHomeBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.GamesCategoryAdapter
import app.tapho.ui.games.adapter.GamesMostPlayedAdapter
import app.tapho.ui.games.adapter.TrendingGamesAdapter

class GamesHomeFragment : BaseFragment<FragmentGamesHomeBinding>(), RecyclerClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGamesHomeBinding.inflate(inflater, container, false)
        setCategoryRecycler()
        init()
        return _binding?.root
    }

    private fun init(){
        binding.viewAllTv.setOnClickListener {
            findNavController().navigate(R.id.action_gamesHomeFragment_to_gamesCategoryFragment)
        }
    }

    private fun setCategoryRecycler() {
        binding.recyclerCategory.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = GamesCategoryAdapter(this@GamesHomeFragment)
        }

        binding.recyclerFeaturedGames.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = GamesCategoryAdapter(this@GamesHomeFragment)
        }

        binding.recyclerPlayAgain.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = GamesMostPlayedAdapter(this@GamesHomeFragment)
        }

        binding.recyclerTrendingGames.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = TrendingGamesAdapter()
        }

        binding.recyclerNewlyAdded.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = GamesMostPlayedAdapter(this@GamesHomeFragment)
        }

        binding.recyclerPopularGames.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = TrendingGamesAdapter()
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        findNavController().navigate(R.id.action_gamesHomeFragment_to_moreGamesFragment)
    }

}