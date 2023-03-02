package app.tapho.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentCategoriesBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.adapter.CategoryOfferAdapter
import app.tapho.ui.merchants.model.MerchantOfferRes
import app.tapho.ui.model.AppCategory
import app.tapho.utils.ContainerType
import com.google.gson.Gson


/**
 * A simple [Fragment] subclass.
 * Use the [CategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>(), RecyclerClickListener,
    ApiListener<MerchantOfferRes, Any?> {
    private var mAdapter: CategoryOfferAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        init()
        return _binding?.root
    }

    private fun init() {
        mAdapter = CategoryOfferAdapter(this)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
//            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = mAdapter
        }

        getData()
    }

    private fun getData() {
        viewModel.getOffers(getSharedPreference().getUserId(), "1", this, this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CategoriesFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is AppCategory)
            ContainerActivity.openContainer(context,
                ContainerType.MERCHANT_DETAIL.name,
                Bundle().apply { putString("ID", data.id)
                    putString("TYPE", "1")
                    putString("DATA",Gson().toJson(data))
                })
    }

    override fun onSuccess(t: MerchantOfferRes?, mess: String?) {
        t?.let { it ->
            it.banner_list?.let { banners ->

                if (parentFragment is OffersFragment) {
                    (parentFragment as OffersFragment).setBannerData(banners)
                }
            }

            it.category?.let { cates ->
                it.data

                mAdapter?.addAllItem(cates)
            }
        }
    }
}