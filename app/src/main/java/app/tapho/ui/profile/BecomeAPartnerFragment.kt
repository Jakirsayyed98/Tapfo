package app.tapho.ui.profile

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import app.tapho.R
import app.tapho.databinding.FragmentBecomeAPartnerBinding
import app.tapho.ui.BaseFragment
import com.bumptech.glide.Glide


class BecomeAPartnerFragment : BaseFragment<FragmentBecomeAPartnerBinding>() {

    val url = "https://docs.google.com/forms/d/e/1FAIpQLSeyRlao16fkZ_cxtx7n-tZbOocck7VyPSWAbjBTpex_IEusOA/viewform?usp=sf_link"
    val image = "https://tapfo.in/delta/public/storage/all_images/2022120215500616699764063IOrOZmvhr.png"

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
        _binding = FragmentBecomeAPartnerBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.howwork.isSelected = true
//        Glide.with(requireContext()).load(R.drawable.partnerwithus).into(_binding!!.icon)
        Glide.with(requireContext()).load(image).into(_binding!!.icon)
        _binding!!.backIv.setOnClickListener{
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.howwork.setOnClickListener{
            setOnCustomeCrome(url)
        }

        return _binding?.root
    }


    private fun setOnCustomeCrome(url: String) {
        val customIntent = CustomTabsIntent.Builder()
        customIntent.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.white))
        val backbtn = BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_back_black_24dp)
        customIntent.setCloseButtonIcon(backbtn)
        customIntent.setDefaultShareMenuItemEnabled(false)
        customIntent.build()
        openCustomTab(requireContext(), customIntent.build(), Uri.parse(url))
    }

    private fun openCustomTab(context: Context, customTabsIntent: CustomTabsIntent, Url: Uri) {
        val packageName = "com.android.chrome"
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.launchUrl(context, Url)

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            BecomeAPartnerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}