package app.tapho.ui.merchants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.databinding.FragmentMerchantOfferBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.merchants.adapter.MerchantOfferAdapter
import app.tapho.ui.merchants.model.MerchantOfferRes
import app.tapho.ui.merchants.model.StoreDeals
import app.tapho.utils.ContainerType
import com.google.gson.Gson

class MerchantOfferFragment : BaseFragment<FragmentMerchantOfferBinding>(), RecyclerClickListener,
    ApiListener<MerchantOfferRes, Any?> {

    private var mAdapter: MerchantOfferAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMerchantOfferBinding.inflate(inflater, container, false)

        init()
        return _binding?.root
    }

    private fun init() {
        mAdapter = MerchantOfferAdapter(this)
        if (activity is HomeActivity)
            binding.reBack.visibility = View.GONE
        binding.backIv.setOnClickListener { activity?. onBackPressedDispatcher?.onBackPressed() }
        binding.recyclerMerchantsOffer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        binding.searchEt.doAfterTextChanged {
            it?.also { mAdapter?.search(it.toString()) }
        }
        getData()
    }

    private fun getData() {
        viewModel.getOffers(getSharedPreference().getUserId(), "2", this, this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MerchantOfferFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is StoreDeals) ContainerActivity.openContainer(context, ContainerType.MERCHANT_DETAIL.name, Bundle().apply {
                    putString("ID", data.id)
                    putString("TYPE", "2")
                    putString("DATA", Gson().toJson(data))
                })
    }

    override fun onSuccess(t: MerchantOfferRes?, mess: String?) {
        t?.let {
            t.store_deals?.let { deals ->
                mAdapter?.addAllItem(deals)
            }
        }
    }
}