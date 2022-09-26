package app.tapho.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMerchantDealBinding
import app.tapho.databinding.FragmentMiniAppBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.DealsAndOfferAdapter
import app.tapho.ui.home.adapter.HomePageOfferListAdapter
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.MiniApp
import app.tapho.utils.TITLE
import com.bumptech.glide.Glide

class MerchantDealFragment : BaseFragment<FragmentMerchantDealBinding>() {
    private var mAdapter: DealsAndOfferAdapter<OfferData>? = null
    var MiniAppId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        merchantbinding = FragmentMerchantDealBinding.inflate(inflater, container, false)
        val app_category_id=activity?.intent?.getStringExtra("app_category_id").toString()
         MiniAppId=activity?.intent?.getStringExtra("miniapp_id").toString()
        val MerchantName=activity?.intent?.getStringExtra(TITLE).toString()
        val logo=activity?.intent?.getStringExtra("logo").toString()
        merchantbinding!!.productNameTv.text=MerchantName
        Glide.with(this).load(logo).into(merchantbinding!!.logo)


        merchantbinding!!.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        getOffrsViewmodel1(app_category_id)
        offerforYou()
        return merchantbinding?.root
    }

    private fun getOffrsViewmodel1(id: String?) {
        viewModel.getOfferDetail(getUserId(), "1", id, "0", "0", this,
            object : ApiListener<OfferDetailRes, Any?> {
                override fun onSuccess(t: OfferDetailRes?, mess: String?) {
                    var tempList: ArrayList<OfferData> = ArrayList()
                    t!!.app_data.let {
                        if (it.isNullOrEmpty()){
                            merchantbinding!!.icRupee1.visibility=View.VISIBLE
                        }else{
                            merchantbinding!!.icRupee1.visibility=View.GONE
                        }
                        it!!.forEach {
                            if (it.mini_app.get(0).id==MiniAppId){
                                tempList.add(it)
                            }
                        }
                        mAdapter!!.addAllItem(tempList)
                    }
                }
            })
    }

    private fun offerforYou() {
        mAdapter = DealsAndOfferAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
             //   if (data is OfferData)
                 //   openDialogredem(data)
                setOnCustomeCrome(data.toString(),type.toString())
            }

        })
        merchantbinding!!.deals.apply {
//              layoutManager= GridLayoutManager(context,2)
              layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = mAdapter
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



    companion object {

        @JvmStatic
        fun newInstance() =
            MerchantDealFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}