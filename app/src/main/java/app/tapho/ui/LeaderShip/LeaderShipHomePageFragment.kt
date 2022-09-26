package app.tapho.ui.LeaderShip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentLeaderShipHomePageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.LeaderShip.Adapter.leadershipAdapter
import app.tapho.ui.LeaderShip.Model.Data
import app.tapho.ui.LeaderShip.Model.leaderboardRes
import com.google.android.gms.common.api.Api


class LeaderShipHomePageFragment : BaseFragment<FragmentLeaderShipHomePageBinding>(){


    var leaderboardadapter : leadershipAdapter<Data>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        leadershipbinding =  FragmentLeaderShipHomePageBinding.inflate(inflater,container,false)
        getviewModeldata()
        setDataInLayout()
        leadershipbinding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
        return leadershipbinding?.root
    }



    private fun getviewModeldata() {
        viewModel.getLeaderBoardData(getUserId(),this,object : ApiListener<leaderboardRes,Any?>{
            override fun onSuccess(t: leaderboardRes?, mess: String?) {
                var templist : ArrayList<Data> = ArrayList()
                var finalList : ArrayList<Data> = ArrayList()

                t.let {
                    it!!.data.let {
                        it.forEach {
                            if (it.name.isNullOrEmpty().not() ){
                                templist.add(it)
                            }
                        }
                    }
                }
                templist.let {
                    leaderboardadapter!!.addAllItem( if (it.size > 50) it.subList(0,50) else it )
                }

            }

        })
    }


    private fun setDataInLayout() {
        leaderboardadapter= leadershipAdapter()
        leadershipbinding!!.leaderBoardData.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = leaderboardadapter
        }


    }

    companion object {

        @JvmStatic
        fun newInstance() =
            LeaderShipHomePageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}