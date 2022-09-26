package app.tapho.ui.CustomeCategorySection

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentCustomeShopCategoryBinding

import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.CustomeCategorySection.Adapter.ShopCustomeCategoryAdapter
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.utils.TITLE
import java.util.*
import kotlin.collections.ArrayList


class CustomeShopCategoryFragment : BaseFragment<FragmentCustomeShopCategoryBinding>(),
    RecyclerClickListener, ApiListener<ShopProductData, Any?> {
    private var ShopProductAdapter: ShopCustomeCategoryAdapter<Data>? = null
    private lateinit var itemList: List<Data>

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
        CustomeShopBinding = FragmentCustomeShopCategoryBinding.inflate(inflater, container, false)

    //    ShowLoading()
        var category_id = activity?.intent!!.getStringExtra("category_id")
        var sub_category_id = activity?.intent!!.getStringExtra("sub_category_id")
        var child_category_id = activity?.intent!!.getStringExtra("child_category_id")

        getProductData(category_id, sub_category_id, child_category_id)
        setShopProductLayout()

        initdata()
        CustomeShopBinding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
        CustomeShopBinding!!.title.text =ContainerActivity.TitileText.toString()
        statusBarColor(R.color.white)

        return CustomeShopBinding?.root
    }

//    private fun ShowLoading() {
//        CustomeShopBinding!!.progress.visibility = View.VISIBLE
//    }
//    private fun dissmissLoading() {
//        CustomeShopBinding!!.progress.visibility = View.GONE
//    }

    private fun initdata() {
        CustomeShopBinding!!.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterListData(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
//                filterListData(p0.toString())
            }
        })
    }

    private fun filterListData(searchName: String) {
        if (!itemList.isNullOrEmpty()){

            var tempList: ArrayList<Data> = ArrayList()
            for (x in itemList) {
                if (searchName.lowercase(Locale.getDefault()) in x.name.toString()
                        .lowercase(Locale.getDefault())
                ) {
                    tempList.add(x)
                }
            }

            CustomeShopBinding!!.producatQty.text= tempList.size.toString() +" Items"
            ShopProductAdapter!!.updatelist(tempList)

        }

    }

    private fun setShopProductLayout() {
        ShopProductAdapter = ShopCustomeCategoryAdapter(this)
        CustomeShopBinding!!.AllSearch.layoutManager = GridLayoutManager(requireContext(), 2)
        CustomeShopBinding!!.AllSearch.adapter = ShopProductAdapter
    }

    private fun getProductData(categoryId: String?, subCategoryId: String?, childCategoryId: String?) {
        viewModel.getShopProduct(getUserId(), categoryId.toString(), subCategoryId.toString(), childCategoryId.toString(), this, this)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CustomeShopCategoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        setOnCustomeCrome(data.toString(),"")
    }


    private fun setOnCustomeCrome(url: String,color: String) {
        val customIntent = CustomTabsIntent.Builder()
        customIntent.setToolbarColor(ContextCompat.getColor(requireContext(),R.color.purple_200))
        customIntent.setShowTitle(true);
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


    override fun onSuccess(t: ShopProductData?, mess: String?) {
        t.let {
            itemList = it!!.data
            it.data.let {
                ShopProductAdapter!!.addAllItem(it.shuffled())
                CustomeShopBinding!!.producatQty.text= it.size.toString() +" Items"
       //        dissmissLoading()
            }

        }
    }
}