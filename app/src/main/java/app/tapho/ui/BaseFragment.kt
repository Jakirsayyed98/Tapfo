package app.tapho.ui

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import app.instagst.ui.interfaces.LoaderListener
import app.tapho.R
import app.tapho.network.RequestViewModel
import app.tapho.utils.AppSharedPreference


abstract class BaseFragment<t : ViewBinding> : Fragment(), LoaderListener {
    lateinit var viewModel: RequestViewModel



    var _binding: t? = null
    var leadershipbinding: t? = null
    var bindingCard: t? = null
    var _binding1: t? = null
    var offerbinding: t? = null
    var travelbinding: t? = null
    var AllMiniApps: t? = null
    var merchantbinding: t? = null
    var donationBinding: t? = null
    var recivemoneybinding: t? = null
    var CustomeShopBinding: t? = null
    var tcashpointbinding: t? = null
    var sendMoneyBinding: t? = null
    var billsbinding: t? = null
    var headlinesbinding: t? = null
    var cabsBinding: t? = null
    var UberBinding: t? = null
    var OlaBinding: t? = null
    var Busnessbinding: t? = null
    var ExpertSupportBinding: t? = null
    var ExpertSupportListBinding: t? = null
    var InsuranceBinding: t? = null
    var buygiftcardbinding: t? = null
    var bindingNewCat: t? = null
    var orderBinding: t? = null
    var shopCategories: t? = null
    var NewOrderBinding: t? = null
    var binding1: t? = null
    var recommendedbinding: t? = null
    var bindingnewHome: t? = null
    var bindingwallet: t? = null
    var favBinding:t?=null
    var bindingForYou:t?=null
    var shopBinding:t?=null
    var NewOfferBinding:t?=null
    var CategoryMerchantsbinding:t?=null
    var CategoryCashbackMerchantsbinding:t?=null

    var bindingForGames:t?=null
    var ActiveCasbackBinding:t?=null
    var bindingGamesForyou:t?=null
    var gamebinding:t?=null
    var bindingSeeAll:t?=null
    var bindingGamesFavList:t?=null
    var bindingpopulargames:t?=null
    var bindingMobileRecharge:t?=null
    var AllplansBinding:t?=null

    var bindingGameCategory:t?=null
    var headlinesBinding:t?=null
    var shortsbinding:t?=null
    var bindingSearchAndCompaire:t?=null
    var blackFragmentbinding:t?=null
    var Tpaybinding:t?=null
    var ministorebinding:t?=null

    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequestViewModel::class.java)
    }

    fun statusBarColor(color: Int) {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), color)
    }

//    fun statusBarTextWhite() {
////        activity?.theme=ContextCompat.get
//        activity?.theme?.applyStyle(R.style.Theme_Tapfo_BlackStatusBar, true)
//    }

    fun statusBarTextBlack() {
//        activity?.theme=ContextCompat.get
        activity?.theme?.applyStyle(R.style.TextViewBold, true)
    }



    fun statusBarTextWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun showLoader() {
        kotlin.runCatching {
            LoaderFragment.showLoader(childFragmentManager)
        }
    }

    override fun dismissLoader() {
        kotlin.runCatching {
            LoaderFragment.dismissLoader(childFragmentManager)
        }
    }

    override fun showMess(mess: String?) {
//       context?.toast(mess)
    }


    fun getSharedPreference(): AppSharedPreference {
        return AppSharedPreference.getInstance(requireContext())
    }

    fun getUserId(): String {
        getSharedPreference().getLoginData()?.let {
            it.id?.let { it1 ->
                return it1
            }
        }
        return ""
    }
    fun getRefreelCode(): String {
        getSharedPreference().getLoginData()?.let {
            it.unique_user_id?.let { it1 ->
                return it1
            }
        }
        return ""
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}