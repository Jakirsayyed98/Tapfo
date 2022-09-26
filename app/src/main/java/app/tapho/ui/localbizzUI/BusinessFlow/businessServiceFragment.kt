package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import app.tapho.R
import app.tapho.databinding.FragmentBusinessServiceBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.BusinessSubCategory
import app.tapho.ui.localbizzUI.Model.BusinessSubCategory.Data

class businessServiceFragment : BaseFragment<FragmentBusinessServiceBinding>() {
    var businessserviceDatadata : ArrayList<String> = ArrayList()
    var businessservicename=""
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
            activity?.onBackPressed()
        }
        _binding!!.dis.text = getString(R.string.select_sub_category_to_describe_bn,getSharedPreference().getString("BusinessName"))
        _binding!!.btnVerify.setOnClickListener {
            activity?.onBackPressed()
        }

        callAllMethods()


        return _binding!!.root
    }


    private fun callAllMethods() {
        viewModel.getBusinessSubCategory(getUserId(),
            getSharedPreference().getString("category_id"),
            this, object : ApiListener<BusinessSubCategory, Any?> {
                override fun onSuccess(t: BusinessSubCategory?, mess: String?) {
                    t!!.data.let {
                        setSpinnerlayout(it)
                    }
                }
            })
    }

    private fun setSpinnerlayout(it: List<Data>) {

        it.forEach {
            businessserviceDatadata.add(it.name)
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, businessserviceDatadata)
        _binding!!.businessType.adapter = adapter
        _binding!!.businessType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                _binding!!.btnVerify.isSelected = true
                businessservicename = it.get(position).name
                getSharedPreference().saveString("service_name",businessservicename)
                getSharedPreference().saveString("service_id",it.get(position).id)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                _binding!!.btnVerify.isSelected = false
            }

        }
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