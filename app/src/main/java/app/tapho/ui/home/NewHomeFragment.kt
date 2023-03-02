package app.tapho.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.*
import app.tapho.Connection.ConnectionReceiver
import app.tapho.Connection.ConnectivityListener
import app.tapho.databinding.FragmentNewHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.BaseRes
import app.tapho.network.Status
import app.tapho.ui.*
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.MiniCash.Adapter.MerchantsDataAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes
import app.tapho.ui.RechargeService.ModelData.RechargeStatus.checkRechargeStatusRes
import app.tapho.ui.Stories.Adapter.StoriesAdapter
import app.tapho.ui.Stories.Model.Data
import app.tapho.ui.Stories.Model.StoriesResFile
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.favourite.FavouriteDialogFragment
import app.tapho.ui.games.GameWebViewActivity
import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import app.tapho.ui.home.adapter.*
import app.tapho.ui.localbizzUI.LocalBizSplashActivity
import app.tapho.ui.merchants.MerchantOfferFragment
import app.tapho.ui.merchants.adapter.CategoryTabAdapter
import app.tapho.ui.model.*
import app.tapho.ui.model.AllNotification.AllNotificationRes
import app.tapho.ui.model.mini_app_data.MiniAppsDataRes
import app.tapho.ui.scanner.NewScannerActivity
import app.tapho.ui.scanner.scanner
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.*
import app.tapho.viewmodels.FavouriteViewModel
import app.tapho.viewmodels.MerchantsAllListViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.JsonObject
import com.kakyiretechnologies.appreview.reviewApp
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec




class NewHomeFragment : BaseFragment<FragmentNewHomeBinding>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewHomeBinding.inflate(inflater, container, false)
        statusBarColor(R.color.black)
        statusBarTextBlack()
        init()
        createShortCuts()
        _binding!!.Greeting.text ="Hey, "+ getGreetingMessage()
//        _binding!!.notificationRe.setOnClickListener {
//            ContainerActivity.openContainer(context, "AllNotification", "")
//        }
//        _binding!!.cashbackCard.setOnClickListener {
//            ContainerActivity.openContainer(context, "cashbackcard", "")
//        }
        _binding!!.favouritesBtn.setOnClickListener {
            ContainerActivity.openContainer(requireContext(),"favouritesBtn", "", false, "")
        }

        _binding!!.reProfile.setOnClickListener {
            ContainerActivity.openContainer(context, ContainerType.PROFILE_EDIT.name, "")
        }

        _binding!!.scanner.setOnClickListener {
//            startActivity(Intent(requireContext(), scanner::class.java))
            permissionTaking()
        }
        return _binding?.root
    }
    private fun permissionTaking() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 100)
        } else {
//            ScanAndPayContainerActivity.openContainer(requireContext(),"EnterAmountForSend","textData","","")
            startActivity(Intent(requireContext(), NewScannerActivity::class.java))
        }
    }

    private fun init() {
        val mAdapter = PagerFragmentAdapter(this)
        mAdapter.addFragment(HomeTabFragment.newInstance(),"")
//        mAdapter.addFragment(BuyGiftCardFragment.newInstance(), getString(R.string.UPI))
        binding.viewPager.adapter = mAdapter
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
//            tab.customView = getCustomTab2(context, mAdapter.getTitle(pos))
//        }.attach()

    }
    private fun createShortCuts() {
        val intent : Intent = Intent(requireContext(),scanner::class.java)
        intent.action = Intent.ACTION_VIEW
        val shortcut = ShortcutInfoCompat.Builder(requireContext(), "id1")
            .setShortLabel("Scanner")
            .setLongLabel("Open Scanner")
            .setIcon(IconCompat.createWithResource(requireContext(), R.drawable.ic_qr_code_scanner))
            //   .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mysite.example.com/")))
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(requireContext(), shortcut)
    }


    fun progressVISIBLE() {
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.VISIBLE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }

    fun lowConnection() {
        _binding!!.lowconnection.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
    }

    fun showHome() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.noconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.VISIBLE



    }

    fun noInternetConnection() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.noconnection.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        val loginData = getSharedPreference().getLoginData()
//        HomeFragment.UserId = loginData?.unique_user_id.toString()
        if (loginData?.image.isNullOrEmpty()) {
            _binding!!.profileName.visibility = View.VISIBLE
            _binding!!.profileIv.visibility = View.GONE
            _binding!!.profileName.text = loginData!!.name!!.replaceAfter(" ", "")
        } else {
            _binding!!.profileName.visibility = View.GONE
            Glide.with(this).load(loginData?.image).apply(RequestOptions().circleCrop().placeholder(R.drawable.loding_app_icon)).into(_binding!!.profileIv)
        }

        _binding!!.name.text =requireContext().CamelCaseValue(loginData?.name.toString()).replaceAfter(" ","")
    }

    companion object {
        var UserId = ""
        @JvmStatic
        fun newInstance() =
            NewHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}