package app.tapho.ui.home.SearchAndComare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSearchAndCompareBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.News.NewsFragment.NewsHeadlineFragment
import app.tapho.ui.home.*
import app.tapho.ui.home.NewAdapter.SuperLinkAdapter
import app.tapho.ui.home.NewGame.GameHomeFragment
import app.tapho.ui.home.SearchAndComare.Fragments.*
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.tcash.TCashDashboardFragment


class SearchAndCompareFragment :BaseFragment<FragmentSearchAndCompareBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentSearchAndCompareBinding.inflate(inflater,container,false)
        tabClicked("amazon")
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
        createCategory()
        _binding!!.searchbtn.setOnClickListener {
            tabClicked("amazon")
            dataQuery = _binding!!.search.text.toString()
        }

        return _binding?.root
    }

    private fun createCategory() {
        val Cadapter = ComareCategoryAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data){
                    "amazon"->{
                        tabClicked("amazon")
                    }
                    "Flipkart"->{
                        tabClicked("Flipkart")
                    }
                    "Tatacliq"->{
                        tabClicked("Tatacliq")
                    }
                    "SnapDeal"->{
                        tabClicked("SnapDeal")
                    }
                    "Shopclues"->{
                        tabClicked("Shopclues")
                    }

                }
            }

        }).apply {
            addItem(SearchCategoryModel(R.drawable.amazon_logo,"amazon","amazon",true))
            addItem(SearchCategoryModel(R.drawable.flipkart_logo,"Flipkart","Flipkart",false))
            addItem(SearchCategoryModel(R.drawable.tatacliq_icon,"Tatacliq","Tatacliq",false))
            addItem(SearchCategoryModel(R.drawable.snapdeal,"SnapDeal","SnapDeal",false))
            addItem(SearchCategoryModel(R.drawable.shopcluse_icon,"Shopclues","Shopclues",false))
        }
        _binding!!.category.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter =Cadapter
        }

    }

    fun tabClicked(view: String) {

        when (view) {
            "amazon"-> addFragment(AmazonFragment.newInstance())
            "Flipkart"-> addFragment(FlipkartFragment.newInstance())
            "Tatacliq"-> addFragment(TatacliqFragment.newInstance())
            "SnapDeal"-> addFragment(SnapDealFragment.newInstance())
            "Shopclues"-> addFragment(ShopCluseFragment.newInstance())
        }
    }

    private fun addFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.containerID,fragment)?.commit()
//        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragment)?.addToBackStack("BACK")?.commit()
    }


    companion object {
        var dataQuery:String=""
        @JvmStatic
        fun newInstance() =
            SearchAndCompareFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}