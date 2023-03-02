package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentBusinessServiceBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Adapter.SelectBusinessCategoryAdapter
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.BusinessSubCategory
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.Data

class businessServiceFragment : BaseFragment<FragmentBusinessServiceBinding>() {
    var businessserviceDatadata : ArrayList<String> = ArrayList()
    var businessservicename=""
    var SelectBusinessCategoryAdapter : SelectBusinessCategoryAdapter<Data>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessServiceBinding.inflate(inflater, container, false)
        statusBarTextWhite()
        statusBarColor(R.color.white)
        _binding!!.toolbar.tvTitle.text = "Listing Business"
        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.dis.text = getString(R.string.select_sub_category_to_describe_bn,getSharedPreference().getString("BusinessName"))
        _binding!!.btnVerify.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }


        setLayoutForSubCategory()
        callAllMethods()
        return _binding!!.root
    }

    private fun setLayoutForSubCategory() {
        SelectBusinessCategoryAdapter = SelectBusinessCategoryAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data){
                    _binding!!.btnVerify.isSelected = true
                    businessservicename = data.name
                    getSharedPreference().saveString("service_name",businessservicename)
                    getSharedPreference().saveString("service_id",data.id)

                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed(object : Runnable{
                        override fun run() {
                            activity?.onBackPressedDispatcher?.onBackPressed()
                        }
                    },1000)

                }
            }
        })
        _binding!!.subCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = SelectBusinessCategoryAdapter
        }
    }


    private fun callAllMethods() {
        viewModel.getBusinessSubCategory(getUserId(),
            getSharedPreference().getString("category_id"),
            this, object : ApiListener<BusinessSubCategory, Any?> {
                override fun onSuccess(t: BusinessSubCategory?, mess: String?) {
                    t!!.data.let {
                        SelectBusinessCategoryAdapter!!.addAllItem(it)

                    }
                }
            })
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            businessServiceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}