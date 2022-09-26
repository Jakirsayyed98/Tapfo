package app.tapho.ui.profile

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.BuildConfig
import app.tapho.CamelCaseValue
import app.tapho.R
import app.tapho.databinding.FragmentProfileDetailBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PrivacyPolicyActivity
import app.tapho.ui.help.SupportServiceActivity
import app.tapho.ui.login.LoginActivity
import app.tapho.ui.model.ProfileOptionsModel
import app.tapho.ui.profile.adapter.ProfileOptionsAdapter
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory


class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(), RecyclerClickListener {

    lateinit var manager: ReviewManager

//    val manager: FakeReviewManager = ReviewManagerFactory.create(requireContext()) as FakeReviewManager

    private val launch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                activity?.finish()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        statusBarTextWhite()
        statusBarColor(R.color.grey_v_light)
        getAllViewmodelAndData()
        return _binding?.root
    }

    private fun getAllViewmodelAndData() {
        getTCashDashBoardDatViewmodel()
        init()
    }

    override fun onStart() {
        super.onStart()
        getAllViewmodelAndData()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }
    private fun getTCashDashBoardDatViewmodel() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),this,object : ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                t.let {
                    _binding!!.apply {
                        lifetimeEarning.text = getString(R.string._0_00_cashback_earned, withSuffixAmount(it!!.lifetime_earning))
                        availableBalance.text = withSuffixAmount(it.cash_available)
                    }
                }

            }

        })
    }


/*
    private fun AboutApp() {
        val AboutApp = ProfileOptionsAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Rate us" -> {
                        rateApp()
                        Toast.makeText(requireContext(), "Rate us", Toast.LENGTH_SHORT).show()
                    }
                    "Feedback" -> {
                        Toast.makeText(requireContext(), "Feedback", Toast.LENGTH_SHORT)
                            .show()
                    }
                    "Support" -> {
                        startActivity(Intent(context, SupportServiceActivity::class.java))
                        Toast.makeText(requireContext(), "Support", Toast.LENGTH_SHORT).show()
                    }
                    "Setting" -> {
                        Toast.makeText(requireContext(), "Setting", Toast.LENGTH_SHORT).show()
                    }
                    "Share Feedback" -> {
                        Toast.makeText(requireContext(), "Share Feedback", Toast.LENGTH_SHORT)
                            .show()
                    }
               "Privacy policy" -> {
                   startActivity(Intent(context, PrivacyPolicyActivity::class.java)
                       .apply {
                           flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                           putExtra("TYPE", "2")
                       })
                   Toast.makeText(requireContext(), "Privacy policy", Toast.LENGTH_SHORT).show()
                    }
               "New Update" -> {
                   AboutAppDialogFragment.show(childFragmentManager)
                        Toast.makeText(requireContext(), "New Update", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }).apply {
            addItem(ProfileOptionsModel(R.drawable.ic_earnings, "Rate us", "Rate us"))
            addItem(ProfileOptionsModel(R.drawable.ic_earnings, "Feedback", "Feedback"))
            addItem(ProfileOptionsModel(R.drawable.ic_earnings, "Support", "Support"))
            addItem(ProfileOptionsModel(R.drawable.ic_earnings, "Setting", "Setting"))
            addItem(ProfileOptionsModel(R.drawable.ic_earnings, "Share Feedback", "Share Feedback"))
            addItem(ProfileOptionsModel(R.drawable.ic_earnings, "Privacy policy", "Privacy policy"))
            addItem((ProfileOptionsModel(R.drawable.ic_earnings, "New Update", "New Update")))
        }
        binding.AboutApp.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AboutApp
        }

    }

  */


    private fun setData() {
        val loginData = getSharedPreference().getLoginData()
        Glide.with(this).load(loginData?.image).apply(
            RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
        ).into(_binding!!.profileIv)
        _binding!!.name.text =requireContext().CamelCaseValue( loginData?.name!!)
        _binding!!.mobile.text = "+91 " + loginData?.mobile_no
        _binding!!.email.text = loginData?.email

        if (loginData?.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = loginData?.name

        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(loginData?.image).apply(
                RequestOptions().circleCrop().placeholder(R.drawable.ic_profile_holder)
            ).into(_binding!!.profileIv)
        }
    }

    private fun init() {
        binding.backbutton.setOnClickListener { activity?.onBackPressed() }
        binding.editProfile.setOnClickListener {
            editProfile()
        }
        binding.backbutton.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.merchantTransaction.setOnClickListener {
            ContainerActivity.openContainer(context, "transactionHistory", "")
//            launch.launch(Intent(context, ContainerActivity::class.java).apply {
//                putExtra(CONTAINER_TYPE_KEY, ContainerType.MERCHANT_REPORTS.toString())
//            })
        }

        val mAdapter = ProfileOptionsAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data){
                    "Setting" -> {

                    }
                    "Rate Us" -> {
                       // ratesInApp()
                        rateApp()
                    }
                    "Share The App" -> {
                        shareApp()
                    }
                    "Feedback" -> {
                        startActivity(Intent(context, SupportServiceActivity::class.java))
                    }
                    "Update App" -> {

                    }
                    "Business & Partnership" -> {

                    }
                    "About Us" -> {
                        startActivity(Intent(context, PrivacyPolicyActivity::class.java)
                            .apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                putExtra("TYPE", "2")
                            })
                    }
                    "App Version" -> {
                      //  AboutAppDialogFragment.show(childFragmentManager)
                    }
                  "Help & Support" -> {

                      ContainerActivity.openContainer(context,"helpandsupport","" )

                    }
                    "Log Out" -> {
                        logout()
                    }
                }
            }
        }).apply {
//            addItem(ProfileOptionsModel(R.drawable.setting_icon, "Setting", "Notifications, Passcode and more","","Setting"))
            addItem(ProfileOptionsModel(R.drawable.rate_us_icon, "Rate Us", "Spread Love, drop ratings on play store","","Rate Us"))
            addItem(ProfileOptionsModel(R.drawable.share_icon, "Share The App", "Share with friends to save more","","Share The App"))
//            addItem(ProfileOptionsModel(R.drawable.feedback_icon, "Feedback", "share your feedback, our team will create.","","Feedback"))
//            addItem(ProfileOptionsModel(R.drawable.update_icon, "Update App", "Already Updated!","Update","Update App"))
//            addItem(ProfileOptionsModel(R.drawable.business_icon, "Business & Partnership","Add more value to your business.", "","Business & Partnership"))
            addItem(ProfileOptionsModel(R.drawable.help_end_support_icon, "Help & Support", "FAQ's ","","Help & Support"))
            addItem(ProfileOptionsModel(R.drawable.about_us_icon, "About Us", "Privacy Policy, Terms & more","","About Us"))
            addItem(ProfileOptionsModel(R.drawable.app_version, "App Version", BuildConfig.VERSION_NAME,"","App Version"))
            addItem(ProfileOptionsModel(R.drawable.log_out_icon, "Log Out", "","","Log Out"))

        }
        binding.ProfileMenus.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

/*
        val mAdapter = ProfileOptionsAdapter(this).apply {
            addItem(ProfileOptionsModel(R.drawable.ic_earnings, "Earnings", "1"))
            addItem(ProfileOptionsModel(R.drawable.ic_merchants_reports, "Merchant Reports", "2"))
            addItem(ProfileOptionsModel(R.drawable.ic_my_favourites, "My Favorites", "3"))
            addItem(ProfileOptionsModel(R.drawable.ic_rate_us, "Rate us", "4"))
            addItem(ProfileOptionsModel(R.drawable.ic_share_app, "Share App", "5"))
            addItem(ProfileOptionsModel(R.drawable.ic_term_policy, "Terms & Policies", "6"))
            addItem(ProfileOptionsModel(R.drawable.ic_help_support, "Help & Support", "7"))
            addItem(ProfileOptionsModel(R.drawable.ic_about_app, "About App", "8"))
        }
        binding.ProfileMenus.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        */

    }

    private fun editProfile() {
        if (parentFragment is NavHostFragment)
            (parentFragment as NavHostFragment).navController
                .navigate(R.id.action_profileDetailFragment_to_editProfileFragment)
    }

    private fun logout() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Do you want to logout?")
            setTitle(R.string.logout)
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                getSharedPreference().clear()
                startActivity(Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
            setNegativeButton(getString(R.string.cancel), null)
        }.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileDetailFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

        when (data) {
            "1" -> {
                launch.launch(Intent(context, ContainerActivity::class.java).apply {
                    putExtra(CONTAINER_TYPE_KEY, ContainerType.EARNINGS.toString())  })
            }
            "2" -> {
                launch.launch(Intent(context, ContainerActivity::class.java).apply {
                    putExtra(CONTAINER_TYPE_KEY, ContainerType.MERCHANT_REPORTS.toString())
                })
            }
            "3" -> {
                ContainerActivity.openContainer(context, ContainerType.FAVOURITE.toString(), "")
            }
            "4" -> {
             //   rateApp()
                ratesInApp()
            }
            "5" -> {
                shareApp()
            }
            "6" -> {
                startActivity(Intent(context, PrivacyPolicyActivity::class.java)
                    .apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra("TYPE", "2")
                    })
            }
            "7" -> {
                startActivity(Intent(context, SupportServiceActivity::class.java))
            }
            "8" -> {
                AboutAppDialogFragment.show(childFragmentManager)
            }
        }


    }

    fun ratesInApp() {
        manager = ReviewManagerFactory.create(requireContext())
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                startActivity(requireContext(), reviewInfo)
            }
//            else {
//                // There was some problem, log or handle the error code.
//                @ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
//            }
        }
    }

    private fun startActivity(requireContext: Context, reviewInfo: ReviewInfo) {
        val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
        flow.addOnCompleteListener { _ ->
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
        }
    }

    private fun rateApp() {
        val uri: Uri = Uri.parse("https://play.google.com/store/apps/details?id=" + activity?.packageName.toString() + "")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            context?.toast("Unable to find market app")
        }
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + activity?.packageName.toString() + "")
            type = "text/plain"
        }
        try {
            startActivity(Intent.createChooser(sendIntent, null))
        } catch (e: ActivityNotFoundException) {
            context?.toast("Unable to find market app")
        }
    }

}