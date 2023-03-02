package app.tapho.ui.localbizzUI.BusinessProfileFlow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentBusinessProfilePageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.localbizzUI.Adapter.AllProfilesShowAdapter
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.Data
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.getBusinessListResForBusinessPerson


class BusinessProfilePageFragment : BaseFragment<FragmentBusinessProfilePageBinding>() {

    var AllProfilesShowAdapter: AllProfilesShowAdapter<Data>? =null
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
        _binding = FragmentBusinessProfilePageBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        _binding!!.tvTitle.text = "My Businesses"
        _binding!!.addnewbusiness.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(),"AddInformation")
        }
        _binding!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        statusBarColor(R.color.white)
        getAllData()
        setAllLayoutdata()
        return _binding!!.root
    }

    private fun setAllLayoutdata() {
        AllProfilesShowAdapter = AllProfilesShowAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        _binding!!.allprofile.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = AllProfilesShowAdapter
        }
    }

    private fun getAllData() {
        viewModel.getbusinessListForBusinessPerson(getUserId(),this,object : ApiListener<getBusinessListResForBusinessPerson,Any?>{
            override fun onSuccess(t: getBusinessListResForBusinessPerson?, mess: String?) {
                t.let {
                    it!!.data.let {
                        AllProfilesShowAdapter!!.addAllItem(it)
                    }
                }
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BusinessProfilePageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}