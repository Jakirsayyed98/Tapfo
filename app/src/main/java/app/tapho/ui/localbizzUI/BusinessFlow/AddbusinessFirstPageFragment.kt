package app.tapho.ui.localbizzUI.BusinessFlow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.FragmentAddbusinessFirstPageBinding
import app.tapho.databinding.FragmentHomeScreenBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.home.adapter.SuperLinksSubCategories
import app.tapho.ui.localbizzUI.LocalbizContainerActivity


class AddbusinessFirstPageFragment  : BaseFragment<FragmentAddbusinessFirstPageBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddbusinessFirstPageBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        onClickItems()
        AllFuncationAndMethods()
        return _binding!!.root
    }

    private fun AllFuncationAndMethods() {
        superlinksSubCategories()
    }


    private fun onClickItems() {
        _binding!!.addYourBusiness.setOnClickListener {
            LocalbizContainerActivity.openContainer(requireContext(),"AddInformation")
        }
    }



    private fun superlinksSubCategories() {
        val customShopCategory = SuperLinksSubCategories(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        }).apply {

            addItem(
                CustomeShopCategoryModel(
                    R.drawable.new_gift_card_icon,
                    "Gift Vouchers",
                    "Gift Voucher"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.new_book_cabs_icon,
                    "Book Cabs",
                    "Book Cabs"
                )
            )

            addItem(CustomeShopCategoryModel(R.drawable.live_score, "Live Scores", "Sports"))
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.entertainment_icon,
                    "Entertainment",
                    "mx player"
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.new_book_cabs_icon,
                    "Book Cabs",
                    "Book Cabs"
                )
            )

            addItem(CustomeShopCategoryModel(R.drawable.live_score, "Live Scores", "Sports"))


        }
        _binding!!.SuperLinksSUBRV.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = customShopCategory
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            AddbusinessFirstPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}