package app.tapho.ui.merchants

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.ActivityActiveCashbackForWebBinding
import app.tapho.databinding.FragmentSelectedOffersDetailBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showCustomeSnack
import app.tapho.showLong
import app.tapho.showShortSnack
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.OfferRedeemWebViewActivity
import app.tapho.ui.merchants.adapter.MerchantDealOfferAdapter
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.utils.DATA
import app.tapho.utils.onlyDatewithMonth2
import app.tapho.utils.parseDate3
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.w3c.dom.Text
import java.net.URLDecoder


class SelectedOffersDetailFragment :BaseFragment<FragmentSelectedOffersDetailBinding>(),ApiListener<OfferDetailRes,Any?>{

    private var mAdapter: MerchantDealOfferAdapter? = null
    var voucherID = ""
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
        _binding = FragmentSelectedOffersDetailBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
        getDataFromVoucher()
        setLayoutForRV()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        return _binding?.root
    }

    private fun setLayoutForRV() {
        mAdapter = MerchantDealOfferAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is OfferData) {
                    ContainerActivity.openContainer(requireContext(),"SelectedOffersDetailFragment",data,false,"")
                }
            }

        })
        binding.morestores.apply {
//            layoutManager=GridLayoutManager(context,2)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = mAdapter
        }
    }

    private fun getDataFromVoucher() {
        activity?.intent?.getStringExtra(DATA).let {
             Gson().fromJson(it, OfferData::class.java).let {
                 voucherID = it.id
                 kotlin.runCatching {
                     if (it.type=="3"){
                         _binding!!.couponsCodelay.visibility = View.VISIBLE
                     }else{
                         _binding!!.couponsCodelay.visibility = View.GONE
                     }

                     _binding!!.termsandcondition.setOnClickListener {click->
                         bottomSheet(it.description)
                     }
                     _binding!!.claimBtn.setOnClickListener {click->
                         ActiveCashbackForWebActivity.openWebView(requireContext(),it.url,it.mini_app.get(0).id,it.mini_app.get(0).image,"0",it.is_favourite,it.cashback,"",it.url_type,it.name)

                     }
                     _binding!!.cashback.text = "+ "+URLDecoder.decode(it.cashback, "UTF-8")
                     _binding!!.claimBtn.text = if (it.type=="2") "CLAIM OFFER" else "REDEEM COUPOUN"


                     _binding!!.couponscode.text = it.code
                     _binding!!.copybtn.setOnClickListener {
                         ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)?.apply {
                             setPrimaryClip(ClipData.newPlainText( _binding!!.couponscode.text, _binding!!.couponscode.text))
                             val customSnackView: View = getLayoutInflater().inflate(R.layout.support_toast_layout, null)
                             customSnackView.findViewById<TextView>(R.id.toastTv).text = "Copied"
                             it.showCustomeSnack(customSnackView)
                         }
                     }
                     Glide.with(requireContext()).load(it.mini_app.get(0).logo).into(_binding!!.logo)
                     _binding!!.label.text = it.label!!.replace("_","₹").replace("Rs.","₹").replace("Rs","₹")
                         .replace("RS","₹")
                     _binding!!.expiredate.text = onlyDatewithMonth2(it.end_date)
                     _binding!!.leftdays.text = if (it.end.toInt()<=9) "Hurry up! "+it.end.toString()+" days left" else ""

                     _binding!!.descriptionTv.text = it.name.replace("_","₹").replace("Rs.","₹").replace("Rs","₹")
                         .replace("RS","₹")

                 }
                 viewModel.getOfferDetail(getUserId(),"2",it.mini_app.get(0).id,"0","0",this,this)
             }

        }

    }



    private fun bottomSheet(discription : String) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.terms_and_condition_vouchers, null)

        val terms = view.findViewById<TextView>(R.id.term1)
        terms.text = discription

        dialog.setContentView(view)
        dialog.show()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SelectedOffersDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: OfferDetailRes?, mess: String?) {
        val appdata: ArrayList<OfferData> = ArrayList()

        t?.let {
            it.app_data?.let {
                it.forEach {
                    if (it.id != voucherID) {
                        if (it.end_date.isNullOrEmpty().not()) {
                            if (it.end.toInt() == 0) {
                                if (TimePeriodDialog.getCurrentDate().equals(it.end_date)) {
//                            mAdapter!!.addItem(it)
                                    appdata.add(it)
                                }
                            } else {
                                appdata.add(it)
                            }
                        }
                    }
                }
                appdata.let { it1 -> mAdapter?.addAllItem(it1) }
                _binding!!.mainLayout.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
            }
        }

    }

}