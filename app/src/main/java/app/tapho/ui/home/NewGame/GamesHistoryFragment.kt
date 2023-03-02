package app.tapho.ui.home.NewGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentGamesHistoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.GetRecentlyPlayedGameAdapterList
import app.tapho.ui.games.models.GameAddToRecent.RecentPlayList.GameRecentPlayList


class GamesHistoryFragment :BaseFragment<FragmentGamesHistoryBinding>() {

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
        _binding = FragmentGamesHistoryBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        getRecentGameplayList()

        return _binding?.root
    }


    //Recent Games
    private fun getRecentGameplayList() {
        viewModel.getRecentDataList().observe(viewLifecycleOwner, {
            when (it.status){
                Status.SUCCESS->{it!!.data.let { t->
                    t!!.let {
                        recentPlayedList(it)
                    }
                }
                }
                Status.ERROR->{

                }
                Status.LOADING->{

                }

            }
        })
        getFevouriteData()
    }

    private fun getFevouriteData() {
        viewModel.getRecentDataList(getUserId())
    }

    fun refreshGameRecentData() {
        getFevouriteData()
    }

    private fun recentPlayedList(it : GameRecentPlayList) {
        val RecentGameListAdapter= GetRecentlyPlayedGameAdapterList(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })

        _binding!!.recyclerPlayAgain.apply {
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = RecentGameListAdapter
        }
        if (it.data.isNullOrEmpty()) {
            _binding!!.nodatafound.visibility = View.VISIBLE
            _binding!!.progress.visibility = View.GONE

        } else {
            it.data.let {
                RecentGameListAdapter.addAllItem(it)
                _binding!!.progress.visibility = View.GONE
                _binding!!.nodatafound.visibility = View.GONE
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            GamesHistoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}