package app.tapho.ui.home.CompareCategories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentInsuranceBinding
import app.tapho.databinding.FragmentOlaBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.adapter.AllminiAppStoresScreenAdapter
import app.tapho.ui.home.adapter.CustomeShopCategoryModel


class InsuranceFragment  : BaseFragment<FragmentInsuranceBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        InsuranceBinding = FragmentInsuranceBinding.inflate(inflater, container, false)
        statusBarColor(R.color.orange1)
        onClickListener()
        staticLayout()
        return InsuranceBinding?.root
    }
    private fun onClickListener() {
//        InsuranceBinding!!.backIV.setOnClickListener {
//            activity?.onBackPressed()
//        }
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
        InsuranceBinding!!.staticLayout.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = customShopCategory
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            InsuranceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}