package app.tapho.ui.home

import android.content.ClipData
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentGameBinding
import app.tapho.databinding.FragmentGameSearchBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.games.adapter.SearchGames
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.utils.AppSharedPreference
import java.util.*
import kotlin.collections.ArrayList


class GameSearchFragment : BaseFragment<FragmentGameSearchBinding>(), ApiListener<Games, Any?>,
    RecyclerClickListener {
    private var AllGames: SearchGames<Data>? = null//
    private lateinit var itemList:List<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingForGames = FragmentGameSearchBinding.inflate(inflater, container, false)
        statusBarTextWhite()
        viewmodeldata()
        AllGamesAdapter()
        initdata()
        bindingForGames!!.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
        return bindingForGames?.root
    }

    private fun initdata() {
        bindingForGames!!.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterListData(p0.toString())
            }
        })


    }

    private fun filterListData(searchName: String) {

        var tempList: ArrayList<Data> = ArrayList()
        for (x in itemList){
            if (searchName.lowercase(Locale.getDefault()) in x.name.toString().lowercase(Locale.getDefault())){
                tempList.add(x)
            }
        }
        AllGames!!.updatelist(tempList)
    }


    private fun viewmodeldata() {
        viewModel.getAllGames(AppSharedPreference.getInstance(requireContext()).getUserId(), this, this )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            GameSearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: Games?, mess: String?) {
        Log.d("AllGames","$t")
        t.let {
            itemList=it!!.data
            it.data.forEach {
                AllGames!! addItem (it)
            }
        }
    }
    private fun AllGamesAdapter() {
        AllGames = SearchGames(this)
        bindingForGames!!.AllGames.apply {
            layoutManager= GridLayoutManager(activity,3)
            adapter = AllGames
        }
        bindingForGames!!.AllGames.setNestedScrollingEnabled(false);
    }
    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
    }

}