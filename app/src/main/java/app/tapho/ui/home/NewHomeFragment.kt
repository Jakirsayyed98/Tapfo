package app.tapho.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import app.tapho.*
import app.tapho.databinding.FragmentNewHomeBinding
import app.tapho.ui.*
import app.tapho.ui.home.adapter.*
import app.tapho.ui.model.*
import app.tapho.ui.scanner.NewScannerActivity
import app.tapho.ui.scanner.scanner
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*
import javax.crypto.*

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

        _binding!!.favouritesBtn.setOnClickListener {
            ContainerActivity.openContainer(requireContext(),"favouritesBtn", "", false, "")
        }

        _binding!!.reProfile.setOnClickListener {
            ContainerActivity.openContainer(context, "ProfileDetailFragment", null)
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
     //   mAdapter.addFragment(HomeTabFragment.newInstance(),"")
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