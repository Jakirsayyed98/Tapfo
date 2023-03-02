package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAddcategoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Adapter.SelectBusinessCategoryAdapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.BusinessCategories.BusinessCategory
import app.tapho.ui.localbizzUI.Model.BusinessCategories.Data
import com.google.android.material.snackbar.Snackbar

class AddcategoryFragment :BaseFragment<FragmentAddcategoryBinding>(){
    var businesscategorydata : ArrayList<String> = ArrayList()
    var businesscategoryname=""
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
        // Inflate the layout for this fragment
        _binding = FragmentAddcategoryBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        statusBarColor(R.color.white)


        _binding!!.toolbar.tvTitle.text="Add Business Info"

        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.btnVerify.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.dis.text = getString(R.string.what_is_the_best_category_to_describe_bn,getSharedPreference().getString("BusinessName"))

        SetCategoryLayout()
        callAllMethods()

        return _binding!!.root
    }

    private fun SetCategoryLayout() {
        SelectBusinessCategoryAdapter = SelectBusinessCategoryAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Data){
                    _binding!!.btnVerify.isSelected = true
                    businesscategoryname = data.name
                    getSharedPreference().saveString("category_name",businesscategoryname)
                    getSharedPreference().saveString("category_id",data.id)
                    Snackbar.make(requireView(),"Category SucessFully Selected",Snackbar.LENGTH_SHORT).show()

                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed(object : Runnable{
                        override fun run() {

                            activity?.onBackPressedDispatcher?.onBackPressed()

                        }

                    },1000)

                }

            }
        })
        _binding!!.category.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = SelectBusinessCategoryAdapter
        }
    }

    private fun callAllMethods() {
        getBuisinessCategory()
    }

    private fun getBuisinessCategory() {
        viewModel.getBusinesscategory(getUserId(),this, object : ApiListener<BusinessCategory, Any?>{
            override fun onSuccess(t: BusinessCategory?, mess: String?) {
                t.let {
                    it!!.data.let {
                        SelectBusinessCategoryAdapter!!.addAllItem(it)
                        //setSpinnerlayout(it)
                    }
                }
            }

        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddcategoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}