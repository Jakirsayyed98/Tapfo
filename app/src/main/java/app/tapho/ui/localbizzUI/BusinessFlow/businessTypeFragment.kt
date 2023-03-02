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
import app.tapho.databinding.FragmentBusinessServiceBinding
import app.tapho.databinding.FragmentBusinessTypeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.BusinessType.Data
import app.tapho.ui.localbizzUI.Model.BusinessType.business_types

class businessTypeFragment : BaseFragment<FragmentBusinessTypeBinding>() {
    var businessTypesdata : ArrayList<String> = ArrayList()
    var businessTypesname=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessTypeBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        statusBarColor(R.color.white)
        _binding!!.toolbar.tvTitle.text="Listing Business"

        _binding!!.toolbar.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.btnVerify.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        callAllMethods()

        return _binding!!.root
    }

    private fun callAllMethods() {
        getBusinessTypes()

    }

    private fun getBusinessTypes() {
        viewModel.getBusinessType(getUserId(),this,object : ApiListener<business_types, Any?> {
            override fun onSuccess(t: business_types?, mess: String?) {
                t.let {
                    it!!.data.let {
                        setDataInSpinner(it)
                    }
                }
            }

        })
    }

    private fun setDataInSpinner(typelist: List<Data>) {

        typelist.forEach {
            businessTypesdata.add(it.name)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            businessTypesdata
        )
        _binding!!.businessType.adapter = adapter
        _binding!!.businessType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    _binding!!.btnVerify.isSelected = true
                    businessTypesname = typelist.get(position).name
                    var businessTypesid = typelist.get(position).id
                    getSharedPreference().saveString("bussinessType", businessTypesname)
                    getSharedPreference().saveString("bussinessTypeid", businessTypesid)
                    Toast.makeText(
                        requireContext(),
                        typelist.get(position).name,
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    _binding!!.btnVerify.isSelected = false
                }
            }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            businessTypeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}