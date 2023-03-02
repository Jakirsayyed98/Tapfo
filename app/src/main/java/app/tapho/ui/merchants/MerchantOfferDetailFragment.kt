package app.tapho.ui.merchants

import android.R.attr.data
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMerchantOfferDetailBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.News.NewsActivity.Companion.position
import app.tapho.ui.home.OfferRedeemWebViewActivity
import app.tapho.ui.merchants.adapter.MerchantDealOfferAdapter
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.ui.tcash.TimePeriodDialog.Companion.getCurrentDate
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import java.net.URLDecoder
import java.util.*
import kotlin.collections.ArrayList


class MerchantOfferDetailFragment : BaseFragment<FragmentMerchantOfferDetailBinding>(),
    RecyclerClickListener, ApiListener<OfferDetailRes, Any?>, TabLayout.OnTabSelectedListener {
    private var mAdapter: MerchantDealOfferAdapter? = null
    private var app_data: List<OfferData>? = null
    private var storeDeals: StoreDeals? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMerchantOfferDetailBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        init()
        return _binding?.root
    }

    private fun init() {
        binding.shopNowTv.setOnClickListener {
            ActiveCashbackForWebActivity.openWebView(context, storeDeals?.url, storeDeals?.id, storeDeals?.image, storeDeals?.tcash, storeDeals?.is_favourite,storeDeals?.cashback,storeDeals?.app_category_id,storeDeals!!.url_type,storeDeals!!.name)
        }

        binding.tabLayout.addTab(binding.tabLayout.newTab().apply {
            text = "All Deals"
        })
        binding.tabLayout.addTab(binding.tabLayout.newTab().apply {
            text = "Offers"
        })
        binding.tabLayout.addTab(binding.tabLayout.newTab().apply {
            text = "Coupons"
        })

        binding.tabLayout.addOnTabSelectedListener(this)

        mAdapter = MerchantDealOfferAdapter(this)
        binding.recyclerDeals.apply {
//            layoutManager=GridLayoutManager(context,2)
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = mAdapter
        }
        getData()
    }
    private fun getData() {
        activity?.intent?.extras?.let { bundle->
            val id = bundle.getString("ID")
            val type = bundle.getString("TYPE")

            storeDeals = Gson().fromJson(bundle.getString("DATA"), StoreDeals::class.java)

            kotlin.runCatching {
                Glide.with(binding.merchantImage).load(storeDeals?.image).into(binding.merchantImage)
                binding.merchantNameTv.text = storeDeals?.name
//            (storeDeals?.getDeals() + " Offers").also { binding.offerTv.text = it }

                binding.shopNowTv.visibility = if (storeDeals?.url.isNullOrEmpty()) View.GONE else View.VISIBLE

                if (storeDeals?.merchant_payout != null) {
                    binding.extraCashbackTv.text = URLDecoder.decode(storeDeals?.miniapp?.cashback, "UTF-8")
                } else binding.extraCashbackTv.visibility = View.GONE



                viewModel.getOfferDetail(getUserId(), type, id, "0", "0", this, this)


            }



        }

        /*Gson().fromJson(activity?.intent?.getStringExtra(DATA), Bundle::class.java)?.let { bundle ->

        }*/
    }

    companion object {
        var merchantName:String?=null
        @JvmStatic
        fun newInstance() =
            MerchantOfferDetailFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is OfferData) {

            ContainerActivity.openContainer(requireContext(),"SelectedOffersDetailFragment",data,false,"")
        }
    }

    private fun openDialogredem(data: OfferData) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.offerbottom_sheet, null)
        val partnerIv:ImageView=view.findViewById(R.id.partnerIV);
        val name:TextView=view.findViewById(R.id.name);

        val couponsAndOffer:TextView=view.findViewById(R.id.couponsTv);
        val ExpireDateTV:TextView=view.findViewById(R.id.ExpireDateTV);
        val grabTV:TextView=view.findViewById(R.id.grabTV);
        val grabAdditional:TextView=view.findViewById(R.id.grabAdditional);
        val coupons:TextView=view.findViewById(R.id.coupons);
        val copybtn:TextView=view.findViewById(R.id.copybtn);
        val peopleUsed:TextView=view.findViewById(R.id.peopleUsed);
        val validitydaysleft:TextView=view.findViewById(R.id.validitydaysleft);
        val details:TextView=view.findViewById(R.id.details);
        val redeem:AppCompatButton=view.findViewById(R.id.redeem);
        val couponsCode:CardView=view.findViewById(R.id.couponsCode);
        val hintCoupn:TextView=view.findViewById(R.id.hintCoupn);

        if (data.type == "2") {
            couponsCode.visibility=View.GONE
            hintCoupn.visibility=View.GONE
            couponsAndOffer.text = "Offers"
        } else {
            couponsCode.visibility=View.VISIBLE
            hintCoupn.visibility=View.VISIBLE
            couponsAndOffer.text = "Coupon"
        }

        Glide.with(requireContext()).load(data.mini_app.get(0).image).circleCrop()
            .into(partnerIv)
        ExpireDateTV.text=data.end_date
        grabTV.text="Grab! "+data.label+ " "+ data.name
        grabAdditional.text="+ Get "+URLDecoder.decode(data.cashback, "UTF-8")+"*"
        merchantName=data.mini_app.get(0).name
        name.text=data.mini_app.get(0).name

        coupons.text=data.code
        copybtn.setOnClickListener {
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)?.apply {
                setPrimaryClip(
                    ClipData.newPlainText(coupons.text,
                    coupons.text))
                copybtn.text="Coppied"
//                context?.customToast("code coppied", false)
            }
        }
        peopleUsed.text=data.visited+" people used this"
        validitydaysleft.text=data.end+" days left"
        details.text=data.description
        redeem.setOnClickListener {
            OfferRedeemWebViewActivity.openWebView(context, data.url, data.mini_app[0].id, data.mini_app[0].image, data.tcash, data.is_favourite, data.cashback)
        }
        dialog.setContentView(view)
        dialog.show()
    }


    override fun onSuccess(t: OfferDetailRes?, mess: String?) {
        val appdata :ArrayList<OfferData> = ArrayList()
        t?.let {
            it.app_data!!.forEach {
                if (it.end_date.isNullOrEmpty().not()){
                    if (it.end.toInt()==0){
                        if (getCurrentDate().equals(it.end_date)){
//                            mAdapter!!.addItem(it)
                            appdata.add(it)
                        }
                    }else{
                        appdata.add(it)
                    }
                }
            }


            app_data = appdata
            appdata.size.toString().let { binding.offerTv.text = it+" Offers" }

            appdata.let { it1 ->
                it1.sortBy { it.end }
                mAdapter?.addAllItem(it1)
            }
            _binding!!.mainLayout.visibility = View.VISIBLE
            _binding!!.progress.visibility = View.GONE
            setEmptyView()
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        mAdapter?.let {
            it.clear()
            if (tab?.position == 0)
                app_data?.let { it1 -> it.addAllItem(it1) }
            else {
                app_data?.let { it1 ->
                    it1.forEach { data ->
                        if (tab?.position == 1 && data.type == "2")
                            mAdapter?.addItem(data)
                        else if (tab?.position == 2 && data.type == "3")
                            mAdapter?.addItem(data)
                    }
                }
            }
        }
        setEmptyView()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    private fun setEmptyView() {
        if (mAdapter?.itemCount != 0) {
            binding.emptyLi.visibility = View.GONE
            return
        } else {
            binding.emptyLi.visibility = View.VISIBLE
        }
        when (binding.tabLayout.selectedTabPosition) {
            0 -> {
                binding.titleEmptyTv.text = getString(R.string.empty_title_offer, "deals")
                binding.messEmptyTv.text = getString(R.string.empty_offer_mess, "deals")
            }
            1 -> {
                binding.titleEmptyTv.text = getString(R.string.empty_title_offer, "offers")
                binding.messEmptyTv.text = getString(R.string.empty_offer_mess, "offers")
            }
            2 -> {
                binding.titleEmptyTv.text = getString(R.string.empty_title_offer, "coupons")
                binding.messEmptyTv.text = getString(R.string.empty_offer_mess, "coupons")
            }
        }
    }
}