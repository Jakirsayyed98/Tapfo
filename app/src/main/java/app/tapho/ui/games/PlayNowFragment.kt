package app.tapho.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.databinding.FragmentPlayNowBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.GamesMostPlayedAdapter

class PlayNowFragment : BaseFragment<FragmentPlayNowBinding>(), RecyclerClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlayNowBinding.inflate(inflater, container, false)
        init()
        return _binding?.root
    }

    private fun init() {
        binding.recyclerSimilarGames.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter= GamesMostPlayedAdapter(this@PlayNowFragment)
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }

}