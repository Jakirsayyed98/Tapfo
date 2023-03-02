package app.tapho.ui.profile

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import app.tapho.ui.intro.IntroActivity
import app.tapho.ui.login.LoginActivity
import app.tapho.ui.login.VerifyOtpActivity
import app.tapho.ui.login.model.LoginData
import app.tapho.ui.login.model.LoginRes
import app.tapho.ui.model.ProfileOptionsModel
import app.tapho.ui.model.UserDetails.getUserDetailRes
import app.tapho.ui.profile.adapter.ProfileOptionsAdapter
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile_detail.*


class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(), RecyclerClickListener {

    private val launch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
        statusBarColor(R.color.white)
        progressVisible()

        val runnable = object : Runnable{
            override fun run() {
                synchronized(this@ProfileDetailFragment) {
                    kotlin.runCatching {

                    }
                }
            }
        }
        val thread = Thread(runnable)
        thread.start()

        _binding!!.vname.text = "V"+BuildConfig.VERSION_NAME
        getAllViewmodelAndData()
        return _binding?.root
    }

    private fun progressVisible() {
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
    }



    private fun getAllViewmodelAndData() {
        getTCashDashBoardDatViewmodel()
        init()

        kotlin.runCatching {
            val handler  = Handler(Looper.getMainLooper())
            handler.postDelayed(object  : Runnable{
                override fun run() {
                    kotlin.runCatching {
                        if (getSharedPreference().getString("startProfile").equals("0")){
                            handler.removeCallbacksAndMessages(null)
                            getTCashDashBoardDatViewmodel()
                        }else{
                            handler.removeCallbacksAndMessages(null)
                        }
                    }
                }
            },10000)


        }


    }



    override fun onResume() {
        super.onResume()
        setData()
    }


    private fun getTCashDashBoardDatViewmodel() {
        getSharedPreference().saveString("startProfile","0")

        val data = activity?.intent?.getStringExtra(DATA)

        if (data.isNullOrEmpty().not()){
           val tcash =  Gson().fromJson(data,TCashDasboardRes::class.java)
            tcash.let {
                _binding!!.apply {
                    walletbalance.text = withSuffixAmount(it!!.cash_available.toString())
                    getSharedPreference().saveString("startProfile","1")
                    getmainLayoutvisible()
                    merchantTransaction.setOnClickListener {click->
                        ContainerActivity.openContainer(context, "cashbackcard", it)
                    }
                }
            }
        }else{
            viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),"2",this,object : ApiListener<TCashDasboardRes,Any?>{
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t.let {
                        _binding!!.apply {
                            walletbalance.text = withSuffixAmount(it!!.cash_available.toString())
                            getSharedPreference().saveString("startProfile","1")
                            getmainLayoutvisible()
                            merchantTransaction.setOnClickListener {click->
                                ContainerActivity.openContainer(context, "cashbackcard", it)
                            }
                        }
                    }

                }

            })
        }
    }

    private fun getmainLayoutvisible() {
        _binding!!.mainLayout.visibility = View.VISIBLE
        _binding!!.progress.visibility = View.GONE

    }


    private fun setData() {
        val loginData = getSharedPreference().getLoginData()
        Glide.with(requireContext()).load(loginData?.image).apply(
            RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon)
        ).into(_binding!!.profileIv)
        _binding!!.name.text =requireContext().CamelCaseValue( loginData?.name!!)
        _binding!!.mobile.text = "+91 " + loginData?.mobile_no

        if (loginData.email.isNullOrEmpty()){
            _binding!!.email.visibility = View.GONE
        }else{
            _binding!!.email.text = loginData.email
        }

        if (loginData.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = loginData.name

        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(loginData.image).apply(
                RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon)
            ).into(_binding!!.profileIv)
        }
    }

    private fun init() {
        binding.backIv.setOnClickListener { activity?. onBackPressedDispatcher?.onBackPressed() }
        binding.editProfile.setOnClickListener {
            editProfile()
        }


        val mAdapter = ProfileOptionsAdapter(this).apply {
            addItem(ProfileOptionsModel(R.drawable.online_fav_icon, "My Favourites", "","","My Favourites","0"))
            addItem(ProfileOptionsModel(R.drawable.refer_and_earn, "Refer & Earn", "Refer & Earn","","Share The App","0"))
            addItem(ProfileOptionsModel(R.drawable.how_doseit_works, "How it works", "How it work","","How it work","0"))
            addItem(ProfileOptionsModel(R.drawable.pratnearwithus_icon, "Become Partner with us", "Become Partner with us","","Become Partner with us","0"))
            addItem(ProfileOptionsModel(R.drawable.ret_us_onplay, "Rate Us on the Play Store", "Spread Love, drop ratings on play store","","Rate Us","0"))
            addItem(ProfileOptionsModel(R.drawable.helpandsupport, "Help & Support", "FAQ's ","","Help & Support","0"))
            addItem(ProfileOptionsModel(R.drawable.security_device_icon, "Screen Lock", "","","","1"))
            addItem(ProfileOptionsModel(R.drawable.aboutus, "Terms & Privacy", "Privacy Policy, Terms & more","","About Us","0"))


//            addItem(ProfileOptionsModel(R.drawable.log_out_iconnew, "Log  Out", "","","Log Out","0"))

        }
        binding.ProfileMenus.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

    }

    private fun logouttab() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.logout_sheet, null)

        val cancel : TextView = view.findViewById(R.id.cancel)
        val logout : TextView = view.findViewById(R.id.logout)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        logout.setOnClickListener {
            dialog.dismiss()
            getSharedPreference().clear()
            startActivity(Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK xor Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun editProfile() {

        ContainerActivity.openContainer(requireContext(),"EditProfileFragment","")


        if (parentFragment is NavHostFragment)
            (parentFragment as NavHostFragment).navController
                .navigate(R.id.action_profileDetailFragment_to_editProfileFragment)
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileDetailFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        when (data){
            "My Favourites" -> {
                ContainerActivity.openContainer(context, "favouritesBtn", "", false, "")
            }
            "Rate Us" -> {
                rateApp()
            }
            "Share The App" -> {
                shareApp()
            }
            "Feedback" -> {
                ContainerActivity.openContainer(requireContext(),"OpenServiceList","")
            }
            "How it work" -> {
                ContainerActivity.openContainer(context, "CashbackHowItsWorksFragment", "", false, "")
            }

            "About Us" -> {
                startActivity(Intent(context, PrivacyPolicyActivity::class.java)
                    .apply {
//                                putExtra("TYPE", "1")
                        putExtra("TYPE", "1")
                    })
            }
            "App Version" -> {
                AboutAppDialogFragment.show(childFragmentManager)
            }

            "Help & Support" -> {
                ContainerActivity.openContainer(requireContext(),"OpenServiceList","")
            }
            "Become Partner with us" -> {
                ContainerActivity.openContainer(requireContext(),"BecomeAPartnerFragment","")
            }
            "Log Out" -> {
//                        logout()
                logouttab()
            }
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
        ContainerActivity.openContainer(requireContext(), "referandearnscreen", "")
        /*
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


         */
    }

}