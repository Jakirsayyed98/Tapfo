package app.tapho.ui.electro.FragmentsScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentElectroBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.CustomeCategorySection.Adapter.ShopCustomeCategoryAdapter
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.ShopProductData
import app.tapho.ui.home.ShopProduct.ShopAdapter.ShopProducatAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import java.util.*
import kotlin.collections.ArrayList


class ElectroFragment : BaseFragment<FragmentElectroBinding>(), RecyclerClickListener,
    ApiListener<ShopProductData, Any?> {

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
        // Inflate the layout for this fragment
        _binding = FragmentElectroBinding.inflate(inflater,container,false)
        statusBarColor(R.color.lightpurple3)
        staticLayout()
        getShopAllProductViewModel()
        setShopProductLayout()
        _binding!!.backbutton.setOnClickListener {
            activity?.onBackPressed()
        }
        initdata()
        return _binding!!.root
    }


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



    private fun staticLayout() {
//        val customShopCategory = StaticHomelayoutAdapter(object : RecyclerClickListener {
//            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//                when (data) {
//
//                }
//            }
//
//        }).apply {
//            addItem(CustomeShopCategoryModel(R.drawable.staricon, "Popular", "Stores"))
//            addItem(CustomeShopCategoryModel(R.drawable.intro_categories_icon, "9M+", "Products"))
//            addItem(CustomeShopCategoryModel(R.drawable.earning_icon, "Cashback", "products"))
//        }
//        _binding!!.staticLayout.apply {
//            layoutManager = GridLayoutManager(context, 3)
//            adapter = customShopCategory
//        }
    }


    private fun getShopAllProductViewModel() {
        viewModel.getShopProduct(getUserId(),"1", "", "", this, this)
    }

    override fun onSuccess(t: ShopProductData?, mess: String?) {
        t.let {
            itemList = it!!.data
            it.data.let {
                ShopProductAdapter!!.addAllItem(it.shuffled())
            }

        }
    }
    private fun setShopProductLayout() {
        ShopProductAdapter = ShopCustomeCategoryAdapter(this)
        _binding!!.AllSearch.layoutManager = GridLayoutManager(requireContext(), 2)
        _binding!!.AllSearch.adapter = ShopProductAdapter
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


    companion object {

        @JvmStatic
        fun newInstance() =
            ElectroFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}