package app.tapho.ui.home

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.databinding.FragmentGameSearchBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.games.adapter.SearchGames
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.customToast
import java.util.*
import kotlin.collections.ArrayList


class GameSearchFragment : BaseFragment<FragmentGameSearchBinding>(), ApiListener<Games, Any?>,
    RecyclerClickListener {
    private var AllGames: SearchGames<Data>? = null//
    private lateinit var itemList:List<Data>
    private val REQUEST_CODE_SPEECH_INPUT = 1

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
        bindingForGames!!.mainLayout.visibility = View.GONE
        bindingForGames!!.progress.visibility = View.VISIBLE
        viewmodeldata()
        AllGamesAdapter()
        initdata()
        bindingForGames!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        bindingForGames!!.mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast.makeText(context, " " + e.message, Toast.LENGTH_SHORT).show()
            }
        }


        return bindingForGames?.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                when(result!!.get(0)){
                    "mobile recharge"->{
                        getSharedPreference().saveString("servicetype","1")
                        ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
                    }
                    "recharge"->{
                        getSharedPreference().saveString("servicetype","1")
                        ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
                    }
                    "games"->{
                        ContainerActivity.openContainer(requireContext(), "Games", "")
                    }
                    else->{
                        bindingForGames!!.searchEt.setText(result.get(0).toString())
                        filterListData(result.get(0).toString())
                    }
                }
//                binding!!.searchEt.setText(Objects.requireNonNull(result)!!.get(0))
            }
        }
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

        val tempList: ArrayList<Data> = ArrayList()
        for (x in itemList){
            if (searchName.lowercase(Locale.getDefault()) in x.name.toString().lowercase(Locale.getDefault())){
                tempList.add(x)
            }
        }
        AllGames!!.updatelist(tempList)
    }


    private fun viewmodeldata() {
        viewModel.getAllGames(AppSharedPreference.getInstance(requireContext()).getUserId(),"","","", this, this )
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

        t.let {

            if (it!!.errorCode.equals("0")){
                itemList= it.data
                AllGames!!.addAllItem(it.data)
//            it.data.forEach {
//                AllGames!! addItem (it)
//            }
                bindingForGames!!.mainLayout.visibility = View.VISIBLE
                bindingForGames!!.progress.visibility = View.GONE
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