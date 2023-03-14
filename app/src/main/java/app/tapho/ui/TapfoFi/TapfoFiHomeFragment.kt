package app.tapho.ui.TapfoFi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTapfoFiHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.TapfoFi.Adapter.TapfoFiCategoryAdapter
import app.tapho.ui.TapfoFi.Model.TapfoFiCategories.Data
import app.tapho.ui.TapfoFi.Model.TapfoFiCategories.TapfoFiCategories_Res


class TapfoFiHomeFragment : BaseFragment<FragmentTapfoFiHomeBinding>() {

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
        _binding = FragmentTapfoFiHomeBinding.inflate(inflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()
        ProgressVisible()
        getCategoriesData()
        return _binding?.root
    }

    private fun getCategoriesData() {
        viewModel.TapfoFiCategories(getUserId(),this,object :ApiListener<TapfoFiCategories_Res,Any?>{
            override fun onSuccess(t: TapfoFiCategories_Res?, mess: String?) {
                t!!.data.let {
                    homeSccreenVisible()
                    setDataForcategory(it)


                }
            }
        })
    }

    private fun homeSccreenVisible() {
        _binding!!.homeScreen.visibility = View.VISIBLE
        _binding!!.progress.visibility = View.GONE
    }
    private fun ProgressVisible() {
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.homeScreen.visibility = View.GONE
    }

    private fun setDataForcategory(it: ArrayList<Data>) {
       val itemTypeAdapter = TapfoFiCategoryAdapter<Data>(object :RecyclerClickListener{
           override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
               if (data is Data){
                   ContainerActivity.openContainer(requireContext(),"TapfoFicategoryMiniAppsFragment",data,false,"")
               }
           }
       })
        _binding!!.recyclerAppCategory.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = itemTypeAdapter
        }

        itemTypeAdapter.addAllItem(it)

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            TapfoFiHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}