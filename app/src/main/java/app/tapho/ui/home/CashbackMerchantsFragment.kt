package app.tapho.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentCashbackMerchantsBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.ActiveCashbackForWebActivity
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.CashbackMerchantAdapter
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.MiniApp
import app.tapho.utils.DATA
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CashbackMerchantsFragment : BaseFragment<FragmentCashbackMerchantsBinding>(),
    RecyclerClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCashbackMerchantsBinding.inflate(inflater, container, false)
        getData()
        binding.moreBtn.setOnClickListener { openContainer(getString(R.string.cashback_merchants)) }
        return _binding?.root
    }

    private fun openContainer(type: String) {
        ContainerActivity.openContainer(context, type, "", false, getString(R.string.cashback_merchants))
    }

    private fun getData() {
        var stringList = arguments?.getString(DATA)
        if (stringList.isNullOrEmpty()) stringList = activity?.intent?.getStringExtra(DATA)

        Gson().fromJson<List<CashbackMerchant>>(stringList,
            object : TypeToken<List<CashbackMerchant>>() {}.type)?.let {
            setRecycler(it)
        }
    }

    private fun setRecycler(list: List<CashbackMerchant>) {
        val big = arguments?.getBoolean("SHOW_BIG") == true
        val mAdapter = CashbackMerchantAdapter(big, this)
        binding.recyclerCashbackMerchants.apply {
            if (big)
                layoutManager = LinearLayoutManager(context)
            else
                layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        mAdapter.addAllItem(list)
        binding.moreBtn.visibility =
            if (arguments?.getBoolean("MORE") == true) View.VISIBLE else View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(list: List<CashbackMerchant>?, isMore: Boolean, showBigView: Boolean) =
            CashbackMerchantsFragment().apply {
                arguments = Bundle().apply {
                    if (list.isNullOrEmpty().not())
                        putString(DATA, Gson().toJson(list))
                    putBoolean("MORE", isMore)
                    putBoolean("SHOW_BIG", showBigView)
                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is MiniApp) {
            ActiveCashbackForWebActivity.openWebView(context,
                data.url,
                data.id,
                data.image,
                data.tcash,
                data.is_favourite,
                data.cashback,
                data.app_category_id,
                data.url_type,data.name
            )
        }
    }
}