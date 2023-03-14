package app.tapho.ui.TapfoFi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTapfoFicategoryMiniAppsBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.TapfoFi.Adapter.TapfoFiMiniAppsAdapter
import app.tapho.ui.TapfoFi.Model.TapfoFiCategories.Data
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.FiCategoriesMiniAppsRes
import app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp.FinMiniApp
import app.tapho.utils.DATA
import app.tapho.utils.setOnCustomeCrome
import com.google.gson.Gson


class TapfoFicategoryMiniAppsFragment : BaseFragment<FragmentTapfoFicategoryMiniAppsBinding>(){

    var title = ""
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
        _binding = FragmentTapfoFicategoryMiniAppsBinding.inflate(inflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()
        ProgressVisible()


        val data = activity?.intent?.getStringExtra(DATA)
        if (data.isNullOrEmpty().not()){
            Gson().fromJson(data, Data::class.java).let {
                _binding!!.searchEt.setHint("Search "+it.name)
                _binding!!.title.text = it.name
                title = it.name
                callViewModelForList(it)
            }
        }


        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        return _binding?.root
    }

    private fun callViewModelForList(it: Data?) {
        viewModel.TapfoFiCategoriesMiniapps(getUserId(),it!!.id,this,object :ApiListener<FiCategoriesMiniAppsRes,Any?>{
            override fun onSuccess(t: FiCategoriesMiniAppsRes?, mess: String?) {
                t!!.fin_mini_app.let {
                    mainlayout()
                    setLayoutData(it)
                }
            }

        })


    }

    private fun setLayoutData(it: List<FinMiniApp>) {
        val TapfoFiMiniAppsAdapter = TapfoFiMiniAppsAdapter<FinMiniApp>(object :RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is FinMiniApp){

                    ContainerActivity.openContainer(requireContext(),"TapfoFiMiniAppDetailFragment",data,false,title)
//                    requireContext().setOnCustomeCrome(data.url)
                }
            }
        })
        _binding!!.miniapps.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = TapfoFiMiniAppsAdapter
        }

        TapfoFiMiniAppsAdapter.addAllItem(it)
    }

    private fun mainlayout() {
        _binding!!.mainlayout.visibility = View.VISIBLE
        _binding!!.progress.visibility = View.GONE
    }
    private fun ProgressVisible() {
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.mainlayout.visibility = View.GONE
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TapfoFicategoryMiniAppsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}