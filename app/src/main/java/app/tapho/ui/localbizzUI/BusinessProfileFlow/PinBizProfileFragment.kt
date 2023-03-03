package app.tapho.ui.localbizzUI.BusinessProfileFlow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.CamelCaseValue
import app.tapho.R
import app.tapho.databinding.FragmentPinBizProfileBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PrivacyPolicyActivity
import app.tapho.ui.localbizzUI.LocalbizContainerActivity
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.Data
import app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson.getBusinessListResForBusinessPerson
import app.tapho.ui.model.ProfileOptionsModel
import app.tapho.ui.profile.AboutAppDialogFragment
import app.tapho.ui.profile.adapter.ProfileOptionsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class PinBizProfileFragment : BaseFragment<FragmentPinBizProfileBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPinBizProfileBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE

        _binding!!.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.manage.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(), "BusinessProfile")
        }
        getBusinessList()
        setProfileLayout()
        getUserData()
        return _binding?.root
    }

    private fun getBusinessList() {
        val data: ArrayList<Data> = ArrayList()
        viewModel.getbusinessListForBusinessPerson(getUserId(), this, object :
            ApiListener<getBusinessListResForBusinessPerson, Any?> {
            override fun onSuccess(t: getBusinessListResForBusinessPerson?, mess: String?) {
                t!!.let {
                    it.data.forEach {
                        if (it.status.equals("1")) {
                            data.add(it)
                        }
                    }
                    _binding!!.activatedBiz.text = data.size.toString() + " Business Active"
                    _binding!!.mainLayout.visibility = View.VISIBLE
                    _binding!!.progress.visibility = View.GONE
                }
            }
        })
    }

    private fun setProfileLayout() {
        val mAdapter = ProfileOptionsAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "My Business" -> {
                        LocalbizContainerActivity.openContainer(requireContext(), "BusinessProfile")

                    }
                    "Add new Business" -> {
                        LocalbizContainerActivity.openContainer(requireContext(), "AddInformation")
                    }
                    "Refer & Earn" -> {
                        ContainerActivity.openContainer(requireContext(), "referandearnscreen", null)
                    }
                    "Ads & Campaign" -> {

                    }
                    "Pinbiz Partner" -> {
                        ContainerActivity.openContainer(
                            requireContext(),
                            "BecomeAPartnerFragment",
                            ""
                        )
                    }
                    "About Pinbiz" -> {
                        startActivity(Intent(context, PrivacyPolicyActivity::class.java)
                            .apply {
                                putExtra("TYPE", "1")
                            })
                    }
                    "Help & Support" -> {
                        ContainerActivity.openContainer(requireContext(), "OpenServiceList", "")
                    }

                }
            }
        }).apply {
            addItem(ProfileOptionsModel(R.drawable.online_fav_icon, "My Business", "", "", "My Business","0"))
            addItem(ProfileOptionsModel(R.drawable.refer_and_earn, "Add new Business", "", "", "Add new Business","0"))
            addItem(ProfileOptionsModel(R.drawable.refer_and_earn, "Refer & Earn", "", "", "Refer & Earn","0"))
            addItem(ProfileOptionsModel(R.drawable.how_doseit_works, "Ads & Campaign", "", "", "Ads & Campaign","0"))
            addItem(ProfileOptionsModel(R.drawable.pratnearwithus_icon, "Pinbiz Partner", "", "", "Pinbiz Partner","0"))
            addItem(ProfileOptionsModel(R.drawable.aboutus, "About Pinbiz", "", "", "About Pinbiz","0"))
            addItem(ProfileOptionsModel(R.drawable.helpandsupport, "Help & Support", "", "", "Help & Support","0"))

        }
        _binding!!.ProfileMenus.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

    }

    private fun getUserData() {
        val logindata = getSharedPreference().getLoginData()

        _binding!!.name.text = requireContext().CamelCaseValue(logindata!!.name.toString())
        if (logindata.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = logindata.name!!.replaceAfter(" ", "")
        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(logindata.image)
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon))
                .into(_binding!!.profileIv)
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PinBizProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}