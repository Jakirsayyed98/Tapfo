package app.tapho.ui.home.OfferAndCoupon

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentNewOfferAndCouponBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.OfferAndCoupon.Adapter.AllOfferCardAdapter
import app.tapho.ui.home.OfferAndCoupon.Adapter.OfferCardOfferForYou
import app.tapho.ui.home.OfferAndCoupon.Adapter.OfferCategoriesAdapter
import app.tapho.ui.home.ShopProduct.NewWebViewActivity
import app.tapho.ui.home.adapter.NewBannerDataAdapter
import app.tapho.ui.home.adapter.SliderModelMain
import app.tapho.ui.merchants.model.MerchantOfferRes
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import java.util.*
import kotlin.collections.ArrayList


class NewOfferAndCouponFragment : BaseFragment<FragmentNewOfferAndCouponBinding>() {

    var banner : ArrayList<BannerList> = ArrayList()
    var offerCategoriesCategoryadapter : OfferCategoriesAdapter<AppCategory>? = null

    var AllOffersAdapter : AllOfferCardAdapter<OfferData>? = null

    var offerForYou : OfferCardOfferForYou<OfferData>? = null
    var tempList : ArrayList<OfferData> = ArrayList()

    private lateinit var itemList:List<OfferData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       offerbinding = FragmentNewOfferAndCouponBinding.inflate(inflater,container,false)
        statusBarColor(R.color.new_grey)
        getOfferData()
        setLayout()
        setAllOfferDataInRV()
        getOfferCategoryData()
        setOfferForYouLayout()
        getAllCategryForOffer()
        getSearch()
        offerbinding!!.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        return offerbinding?.root
    }

    private fun getSearch() {
        offerbinding!!.searchTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterListData(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.e("Offer Search","$p0")
//                filterListData(p0.toString())
            }

        })
    }
    private fun filterListData(searchName: String) {

        if (itemList.isNullOrEmpty().not()){
            var tempList: ArrayList<OfferData> = ArrayList()
            for (x in itemList){
                if (searchName.lowercase(Locale.getDefault()) in x.name.toString().lowercase(Locale.getDefault())){
                    tempList.add(x)
                }
            }
            AllOffersAdapter!!.updatelist(tempList)
        }

    }


    //offer For You Start

    private fun getAllCategryForOffer() {
        viewModel.getOffers(getUserId(),"1",this,object  : ApiListener<MerchantOfferRes,Any?>{
            override fun onSuccess(t: MerchantOfferRes?, mess: String?) {
                t.let {
                    it.let {
                        it!!.category!!.forEach {
                            getAllCategoryOfferList(it.id.toString())
                        }

                    }

                }
            }

        })

    }

    private fun getAllCategoryOfferList(app_categoryID:String) {
        viewModel.getOfferDetail(getUserId(),"1",app_categoryID,"0","0",this,object : ApiListener<OfferDetailRes,Any?>{
            override fun onSuccess(t: OfferDetailRes?, mess: String?) {

                t.let {
                    itemList = it!!.app_data!!
                    tempList.addAll(it.app_data!!)
                    tempList.shuffled().let {
                        offerForYou!!.addAllItem( if (it.size > 3) it.subList(0,4) else it )
                    }
                }
            }

        })
    }

    //Offer For you end

    internal fun getOfferList(app_categoryID: String) {
        viewModel.getOfferDetail(getUserId(),"1",app_categoryID,"0","0",this,object : ApiListener<OfferDetailRes,Any?>{
            override fun onSuccess(t: OfferDetailRes?, mess: String?) {
                t.let {
                    it!!.app_data.let {
                        AllOffersAdapter!!.addAllItem(it!!)
                    }
                }
            }

        })
    }
    private fun getOfferCategoryData() {
        viewModel.getOffers(getUserId(),"1",this,object  : ApiListener<MerchantOfferRes,Any?>{
            override fun onSuccess(t: MerchantOfferRes?, mess: String?) {
                t.let {
                    it.let {
                        it!!.category!!.forEach {
                            getOfferList(it.id.toString())
                        }

                    }
                }
            }

        })
    }

    private fun getOfferData() {
        viewModel.getOffers(getUserId(),"1",this,object  : ApiListener<MerchantOfferRes,Any?>{
            override fun onSuccess(t: MerchantOfferRes?, mess: String?) {
                t.let {
                    it.let {
                        offerCategoriesCategoryadapter!!.addItem(AppCategory("","","","","","","0","","For you","",true,null))
                        it!!.category.let {
                            offerCategoriesCategoryadapter!!.addAllItem(it!!)
                        }
                    }
                    it!!.banner_list.let {
                        setBannerdata4(it!!)
                    }
                }
            }

        })
    }

    private fun setLayout() {
        offerCategoriesCategoryadapter= OfferCategoriesAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                AllOffersAdapter!!.clear()
                AllOffersAdapter!!.notifyDataSetChanged()
                if (data.toString().equals("0")){
                    getOfferCategoryData()
                }else{
                    getOfferList(data.toString())
                }

            }
        })
        offerbinding!!.Categories.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerCategoriesCategoryadapter
        }
    }

    private fun setAllOfferDataInRV() {
        AllOffersAdapter= AllOfferCardAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    setOnCustomeCrome(data.toString(),type.toString())
            }
        })
        offerbinding!!.AllCoupons.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = AllOffersAdapter
        }
    }

    private fun setOnCustomeCrome(url: String,color: String) {
        val customIntent = CustomTabsIntent.Builder()
        customIntent.setToolbarColor(ContextCompat.getColor(requireContext(),R.color.purple_200))
        //customIntent.setShowTitle(true);
        openCustomTab(requireContext(), customIntent.build(), Uri.parse(url))
    }

    private fun openCustomTab(requireContext: Context, customTabsIntent : CustomTabsIntent, Url: Uri) {
        val packageName = "com.android.chrome"

        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(requireContext, Url)


        } else {
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Url))
        }
    }


    private fun setOfferForYouLayout() {
        offerForYou= OfferCardOfferForYou(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        offerbinding!!.offerForYou.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerForYou
        }
    }



    private fun setBannerdata4(bannerList4: ArrayList<BannerList>) {
        if (bannerList4.isNullOrEmpty()) {
            offerbinding!!.banner.visibility = View.GONE
        } else {
            offerbinding!!.banner.visibility = View.VISIBLE
        }

        val imageList = ArrayList<SliderModelMain>()
        banner = bannerList4

        for (x in bannerList4) {
            imageList.add(SliderModelMain(x.image, x.url, x.id, x.type, "", ""))
        }
        offerbinding!!.banner.adapter =
            NewBannerDataAdapter(imageList, offerbinding!!.banner, object : RecyclerClickListener {
                override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                    if (data.toString().contains("https://")) {
                        //   WebViewActivity.openWebView(context, data.toString(), "", "", "", "", "", "" )
                        NewWebViewActivity.openWebView(
                            context,
                            data.toString(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        )

                    } else if (data.toString().contains("http://")) {
                        setOnCustomeCrome(data.toString(),"#000")
                    }
                }

            })


        //   TabLayoutMediator(_binding1!!.tabLayout1, _binding1!!.banner1) { _, _ -> }.attach()

        offerbinding!!.banner.clipToPadding = false
        offerbinding!!.banner.clipChildren = false
        offerbinding!!.banner.offscreenPageLimit = 3
        offerbinding!!.banner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        //   _binding1!!.banner3.setPadding(30, 0, 30, 0)

        //Handler Start

        Handler().apply {
            val runnable = object : Runnable {
                override fun run() {
                    var i = offerbinding!!.banner.currentItem

                    if (i == bannerList4.size - 1) {
                        i = -1//0
                        offerbinding!!.banner.currentItem = i
                    }
                    i++
                    offerbinding!!.banner.setCurrentItem(i, true)
                    postDelayed(this, 4000)
                }
            }
            postDelayed(runnable, 4000)
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            NewOfferAndCouponFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}