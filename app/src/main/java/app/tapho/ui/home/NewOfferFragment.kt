package app.tapho.ui.home

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentNewOfferBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.CustomeOfferListAdapter
import app.tapho.ui.home.adapter.HomePageOfferListAdapter
import app.tapho.ui.home.adapter.RecommendedAppCategoryAdapter
import app.tapho.ui.merchants.MerchantOfferDetailFragment
import app.tapho.ui.merchants.model.OfferData
import app.tapho.ui.merchants.model.OfferDetailRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.HomeRes
import app.tapho.utils.AppSharedPreference
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.URLDecoder

class NewOfferFragment : BaseFragment<FragmentNewOfferBinding>() {

    private var itemTypeAdapter: RecommendedAppCategoryAdapter? = null
    private var appCategoryList: ArrayList<AppCategory>? = null

    private var mAdapter: CustomeOfferListAdapter? = null

    var categoryID="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        NewOfferBinding=FragmentNewOfferBinding.inflate(inflater, container, false)

       statusBarColor(R.color.orange1)
        getOffrsViewmodel1(categoryID)
        getCategoriesDataViewModel()
        setRecyclerBrand()
        offerforYou()

        return NewOfferBinding?.root
    }

    private fun getCategoriesDataViewModel() {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(),this,
            object : ApiListener<HomeRes, Any?> {
                override fun onSuccess(t: HomeRes?, mess: String?) {
                    t?.app_category.let {
                        appCategoryList?.let {

                        }
                        categoryID=it!!.get(0).id.toString()
                        getOffrsViewmodel1(categoryID)
                        itemTypeAdapter?.addAllItem(it!!)
                    }
                }
            })
    }
    private fun setRecyclerBrand() {
        itemTypeAdapter = RecommendedAppCategoryAdapter(object :RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                getOffrsViewmodel1(data.toString())
            }

        })
        NewOfferBinding!!.recyclerAppCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            //    layoutManager = GridLayoutManager(context, 5)
            adapter = itemTypeAdapter
        }
    }

    private fun getOffrsViewmodel1(id: String?) {
        viewModel.getOfferDetail(getUserId(), "1", id, "0", "0", this,
            object : ApiListener<OfferDetailRes, Any?> {
                override fun onSuccess(t: OfferDetailRes?, mess: String?) {
                    t!!.app_data.let {
                        mAdapter?.clear()
                        mAdapter?.notifyDataSetChanged()
                        mAdapter?.addAllItem(it!!)
                    }

                }

            })
    }
    private fun offerforYou() {
        mAdapter = CustomeOfferListAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is OfferData)
                    openDialogredem(data)
            }

        })
        NewOfferBinding!!.offers.apply {
          //  layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
              layoutManager= GridLayoutManager(context,2)
              // layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            NewOfferFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    @SuppressLint("InflateParams")
    private fun openDialogredem(data: OfferData) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.offerbottom_sheet, null)
        val partnerIv: ImageView = view.findViewById(R.id.partnerIV);
        val name: TextView = view.findViewById(R.id.name);

        val couponsAndOffer: TextView = view.findViewById(R.id.couponsTv);
        val ExpireDateTV: TextView = view.findViewById(R.id.ExpireDateTV);
        val grabTV: TextView = view.findViewById(R.id.grabTV);
        val grabAdditional: TextView = view.findViewById(R.id.grabAdditional);
        val coupons: TextView = view.findViewById(R.id.coupons);
        val copybtn: TextView = view.findViewById(R.id.copybtn);
        val peopleUsed: TextView = view.findViewById(R.id.peopleUsed);
        val validitydaysleft: TextView = view.findViewById(R.id.validitydaysleft);
        val details: TextView = view.findViewById(R.id.details);
        val redeem: AppCompatButton = view.findViewById(R.id.redeem);
        val couponsCode: CardView = view.findViewById(R.id.couponsCode);
        val hintCoupn: TextView = view.findViewById(R.id.hintCoupn);

        if (data.type == "2") {
            couponsCode.visibility = View.GONE
            hintCoupn.visibility = View.GONE
            couponsAndOffer.text = "Offers"
        } else {
            couponsCode.visibility = View.VISIBLE
            hintCoupn.visibility = View.VISIBLE
            couponsAndOffer.text = "Coupon"
        }

        Glide.with(requireContext()).load(data.mini_app.get(0).image).circleCrop()
            .into(partnerIv)
        ExpireDateTV.text = data.end_date
        grabTV.text = "Grab! " + data.label + " " + data.name
        grabAdditional.text = "+ Get " + URLDecoder.decode(data.cashback, "UTF-8") + "*"
        MerchantOfferDetailFragment.merchantName = data.mini_app.get(0).name
        name.text = data.mini_app.get(0).name

        coupons.text = data.code
        copybtn.setOnClickListener {
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)?.apply {
                setPrimaryClip(
                    ClipData.newPlainText(
                        coupons.text,
                        coupons.text
                    )
                )
                copybtn.text = "Coppied"
//                context?.customToast("code coppied", false)
            }
        }
        peopleUsed.text = data.visited + " people used this"
        validitydaysleft.text = data.end + " days left"
        details.text = data.description
        redeem.setOnClickListener {
            OfferRedeemWebViewActivity.openWebView(
                context,
                data.url,
                data.mini_app[0].id,
                data.mini_app[0].image,
                data.tcash,
                data.is_favourite,
                data.cashback
            )
        }
        dialog.setContentView(view)
        dialog.show()
    }
}