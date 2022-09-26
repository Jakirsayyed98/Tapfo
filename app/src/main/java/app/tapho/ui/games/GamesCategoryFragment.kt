package app.tapho.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.databinding.FragmentGamesCategoryBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.GamesMostPlayedAdapter

class GamesCategoryFragment : BaseFragment<FragmentGamesCategoryBinding>(), RecyclerClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGamesCategoryBinding.inflate(layoutInflater, container, false)
        init()
        return _binding?.root
    }

    private fun init() {
        binding.backIv.setOnClickListener { activity?.onBackPressed() }

        setRecycler()
    }

    private fun setRecycler() {
        binding.recyclerMostPlayed.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = GamesMostPlayedAdapter(this@GamesCategoryFragment)
        }
        binding.recyclerNewlyAdded.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = GamesMostPlayedAdapter(this@GamesCategoryFragment)
        }


    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }

}