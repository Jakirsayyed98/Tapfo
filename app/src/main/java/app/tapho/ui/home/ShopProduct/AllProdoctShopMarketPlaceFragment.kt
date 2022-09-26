package app.tapho.ui.home.ShopProduct

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
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAllProdoctShopMarketPlaceBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.CustomeCategorySection.Adapter.ShopCustomeCategoryAdapter
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.ui.home.ShopProduct.ShopAdapter.ShopProducatAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import java.util.*
import kotlin.collections.ArrayList


class AllProdoctShopMarketPlaceFragment : BaseFragment<FragmentAllProdoctShopMarketPlaceBinding>(), RecyclerClickListener, ApiListener<ShopProductData, Any?>{


    private var shopProducatAdapter: ShopProducatAdapter<Data>? = null
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
        _binding = FragmentAllProdoctShopMarketPlaceBinding.inflate(inflater,container,false)
        statusBarColor(R.color.lightpurple3)
        setClickEvent()
        getShopAllProductViewModel()
       // customShopCategory()
        getShopProductViewModel()
        setShopProductData()
        setShopProductLayout()
        initdata()
        return _binding?.root
    }

    private fun setClickEvent() {

        _binding!!.backbutton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun getShopAllProductViewModel() {
        viewModel.getShopProduct(getUserId(),"", "", "", this, this)
    }
/*
    private fun customShopCategory() {
        val customShopCategory = CustomeShopCategoryAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "AllCategories" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "", "", "", "All Product")
                    }
                    "Mobiles" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "1", "3", "", "Mobiles")
                    }
                    "Fashion" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "3", "", "", "Fashion")
                    }
                    "Beauty" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "2", "", "", "Beauty")
                    }
                    "Laptop" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "1", "4", "", "Laptop")
                    }
                    "Television" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "1", "6", "", "Television")
                    }
                    "Audio" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "1", "2", "", "Audio")
                    }
                    "Refrigerators" -> {
                        Toast.makeText(requireContext(), "Refrigerators", Toast.LENGTH_SHORT).show()
                    }
                    "Washing Machines" -> {
                        Toast.makeText(requireContext(), "Washing Machines", Toast.LENGTH_SHORT).show()
                    }
                    "Kitchen Appliances " -> {
                        Toast.makeText(requireContext(), "Kitchen Appliances ", Toast.LENGTH_SHORT).show()
                    }
                    "Wearables" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "1", "1", "", "Wearables")
                    }
                    "Camera" -> {
                        ContainerActivity.openCustomeCategory(requireContext(), "CustomeShopCategory", "1", "8", "", "Camera")
                    }
                }
            }

        }).apply {


            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "All Categories", "AllCategories"))
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Mobiles", "Mobiles"))
            addItem(CustomeShopCategoryModel(R.drawable.fashion_new_icon, "Fashion", "Fashion"))
            addItem(CustomeShopCategoryModel(R.drawable.beauty_new_icon, "Beauty", "Beauty"))
            addItem(CustomeShopCategoryModel(R.drawable.laptop_new_icon2, "Laptop", "Laptop"))
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Television", "Television"))
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Audio", "Audio"))
//            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Refrigerators", "Refrigerators"))
//            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Washing\nMachines", "Washing Machines"))
//            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Kitchen\nAppliances ", "Kitchen Appliances "))
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Wearables", "Wearables"))
            addItem(CustomeShopCategoryModel(R.drawable.mobile_new_icon, "Camera", "Camera"))

        }
        _binding!!.ShopProductCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = GridLayoutManager(context, 5)
            adapter = customShopCategory
        }
    }
*/
    private fun initdata() {
        _binding!!.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.e("SearchData","$p0")
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
            ShopProductAdapter!!.updatelist(tempList)

        }

    }


    private fun setShopProductLayout() {
        ShopProductAdapter = ShopCustomeCategoryAdapter(this)
//        _binding!!.AllSearch.layoutManager = GridLayoutManager(requireContext(), 2)

        _binding!!.AllSearch.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        _binding!!.AllSearch.adapter = ShopProductAdapter
    }
    private fun getShopProductViewModel() {
        getSharedPreference().getUserId().let {
            viewModel.getShopProduct(getUserId(), "", "", "", this,
                object : ApiListener<ShopProductData, Any?> {
                    override fun onSuccess(t: ShopProductData?, mess: String?) {
                        t!!.data.let {
                            Log.d("SHopData", "$it")
                            shopProducatAdapter!!.addAllItem(if (it.size > 8) it.shuffled().subList(0, 8) else it
                            )

                        }
                    }
                })
        }
    }

    private fun setShopProductData() {
        shopProducatAdapter = ShopProducatAdapter(this)
//        _binding!!.ShopProduct.apply {
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//            adapter = shopProducatAdapter
//        }
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


    companion object {

        @JvmStatic
        fun newInstance() =
            AllProdoctShopMarketPlaceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: ShopProductData?, mess: String?) {
        var ProductList : ArrayList<Data> = ArrayList()
        t.let {
            itemList = it!!.data
            it.data.let {
                it.forEach {
                    if (it.sale_price.isNullOrEmpty().not() && it.mrp_price.isNullOrEmpty().not()){
                        ProductList.add(it)
                    }
                }
//                ShopProductAdapter!!.addAllItem(it.shuffled())

            }
            ShopProductAdapter!!.addAllItem(ProductList)
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        setOnCustomeCrome(data.toString(),"")
    }
}