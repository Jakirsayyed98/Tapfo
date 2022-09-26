package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import app.tapho.R
import app.tapho.databinding.FragmentAddcategoryBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.BusinessCategories.BusinessCategory
import app.tapho.ui.localbizzUI.Model.BusinessCategories.Data

class AddcategoryFragment :BaseFragment<FragmentAddcategoryBinding>(){
    var businesscategorydata : ArrayList<String> = ArrayList()
    var businesscategoryname=""

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
            activity?.onBackPressed()
        }
        _binding!!.btnVerify.setOnClickListener {
            activity?.onBackPressed()
        }

        _binding!!.dis.text = getString(R.string.what_is_the_best_category_to_describe_bn,getSharedPreference().getString("BusinessName"))

        callAllMethods()

        return _binding!!.root
    }

    private fun callAllMethods() {
        getBuisinessCategory()
    }

    private fun getBuisinessCategory() {
        viewModel.getBusinesscategory(getUserId(),this, object : ApiListener<BusinessCategory, Any?>{
            override fun onSuccess(t: BusinessCategory?, mess: String?) {
                t.let {
                    it!!.data.let {
                        setSpinnerlayout(it)
                    }
                }
            }

        })
    }

    private fun setSpinnerlayout(it: List<Data>) {

        it.forEach {
            businesscategorydata.add(it.name)
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, businesscategorydata)
        _binding!!.businessType.adapter = adapter
        _binding!!.businessType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                _binding!!.btnVerify.isSelected = true
                businesscategoryname = it.get(position).name
                getSharedPreference().saveString("category_name",businesscategoryname)
                getSharedPreference().saveString("category_id",it.get(position).id)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                _binding!!.btnVerify.isSelected = false
            }

        }
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