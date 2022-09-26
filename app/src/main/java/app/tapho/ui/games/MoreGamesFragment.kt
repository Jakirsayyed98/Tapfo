package app.tapho.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMoreGamesBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.GamesCategoryTabAdapter
import app.tapho.ui.games.adapter.GamesMostPlayedAdapter
import app.tapho.ui.games.models.GamesCategoryData
import app.tapho.viewmodels.GamesViewModel

class MoreGamesFragment : BaseFragment<FragmentMoreGamesBinding>(), RecyclerClickListener {
    private var categoryAdapter: GamesCategoryTabAdapter? = null
    private val gamesViewModel: GamesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoreGamesBinding.inflate(inflater, container, false)
        setGamesCategory()
        setGamesRecycler()
        return _binding?.root
    }

    private fun setGamesCategory() {
        categoryAdapter = GamesCategoryTabAdapter(this)
        binding.recyclerGamesCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        getCategories()
    }

    private fun setGamesRecycler() {
        binding.recyclerGames.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = GamesMostPlayedAdapter(this@MoreGamesFragment)
        }
    }

    private fun getCategories() {
        binding.liDummyTabs.visibility = View.GONE
        if (gamesViewModel.getCategory().value?.status != Status.SUCCESS) {
            gamesViewModel.getCategory().observe(viewLifecycleOwner, {
                if (it.status == Status.LOADING) {
                    binding.liDummyTabs.visibility = View.VISIBLE
                } else {
                    binding.liDummyTabs.visibility = View.GONE
                    if (it.status == Status.SUCCESS)
                        setCategoryData()
                }
            })
            gamesViewModel.getGamesCategories(getUserId())
        }else
        setCategoryData()
    }

    private fun setCategoryData() {
        categoryAdapter?.addItem(GamesCategoryData("", "", "", "All", "", true))
        gamesViewModel.getCategory().value?.data?.let { categoryAdapter?.addAllItem(it) }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == "play")
            findNavController().navigate(R.id.action_moreGamesFragment_to_playNowFragment)
    }

}