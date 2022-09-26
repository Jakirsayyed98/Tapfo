package app.tapho.ui.home.FinanceCategories

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentDonationBinding
import app.tapho.databinding.FragmentNewHomeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.WebViewActivityForOffer
import app.tapho.ui.home.adapter.AllminiAppStoresScreenAdapter
import app.tapho.ui.home.adapter.CustomeFinanceCategoryAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.WebTCashRes
import app.tapho.ui.scanner.scanner
import app.tapho.utils.AppSharedPreference


class DonationFragment : BaseFragment<FragmentDonationBinding>() {

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
        donationBinding = FragmentDonationBinding.inflate(inflater, container, false)
        statusBarColor(R.color.orange1)
        onClickListener()
        staticLayout()
        CustomeDonationCategories()
        return donationBinding?.root
    }

    private fun onClickListener() {
        donationBinding!!.backIV.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    private fun staticLayout() {
        val customShopCategory = AllminiAppStoresScreenAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {

                }
            }

        }).apply {

            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img1,
                    "Save battery,\nspace & memory",
                    ""
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img2,
                    "No need to\nDownload it",
                    ""
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img3,
                    "Plus earn extra\ncashback",
                    ""
                )
            )
            addItem(
                CustomeShopCategoryModel(
                    R.drawable.save_img4,
                    "Enjoy 90,000+\nBrands & Stores",
                    ""
                )
            )
        }
        donationBinding!!.staticLayout.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = customShopCategory
        }
    }

    private fun CustomeDonationCategories() {
        val customFinaceCategory = CustomeFinanceCategoryAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Donate Meals" -> {
                        getWebUrlData("17")
                    }
                    "Wild Life" -> {
                        getWebUrlData("18")
                    }
                    "Plant Tree" -> {
                        getWebUrlData("19")
                    }
                    "Donates Books" -> {
                        getWebUrlData("23")
                    }
                    "Bharat Ke veer" -> {
                        getWebUrlData("20")
                    }
                    "Helpage india" -> {
                        getWebUrlData("21")
                    }
                    "Rescue Children" -> {
                        getWebUrlData("22")
                    }

                }
            }

        }).apply {

            addItem(CustomeShopCategoryModel(R.drawable.donate_meals_icon, "Donate\nMeals", "Donate Meals"))
            addItem(CustomeShopCategoryModel(R.drawable.save_animals_icon, "Save Wild\nLife", "Wild Life"))
            addItem(CustomeShopCategoryModel(R.drawable.plant_green_icon, "Plant Tree", "Plant Tree"))
            addItem(CustomeShopCategoryModel(R.drawable.donate_books, "Donate\nbooks", "Donates Books"))
            addItem(CustomeShopCategoryModel(R.drawable.bharat_ke_veer_icon, "Bharat Ke veer", "Bharat Ke veer"))
            addItem(CustomeShopCategoryModel(R.drawable.helpage_india_icon, "Helpage\nindia", "Helpage india"))
            addItem(CustomeShopCategoryModel(R.drawable.save_child_icon, "Rescue Children", "Rescue Children"))
//            addItem(CustomeShopCategoryModel(R.drawable.scanner_new_icon, "", ""))


        }
        donationBinding!!.donationCategory.apply {

            layoutManager = GridLayoutManager(context, 4)
            //layoutManager = LinearLayoutManager(context)
            adapter = customFinaceCategory
        }
    }

    private fun getWebUrlData(s: String) {
        viewModel.getHomeData("home", AppSharedPreference.getInstance(requireContext()).getUserId(),
            this, object : ApiListener<HomeRes, Any?> {
                override fun onSuccess(t: HomeRes?, mess: String?) {
                    t!!.services.let {
                        it!!.forEach {
                            if (it.id == s) {
                                WebViewActivityForOffer.openWebView(context, it.url, id.toString(), "", "", "", "", "")
                            }
                        }
                    }
                }

            })
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DonationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}