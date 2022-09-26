package app.tapho.ui.favourite

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentFavouriteBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.home.adapter.TopIconAdapter
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.AppSharedPreference
import app.tapho.utils.IS_FAVOURITE
import app.tapho.utils.MINI_APP_ID
import app.tapho.utils.OPEN_WEB_VIEW

class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(), ApiListener<WebTCashRes, Any?>,
    RecyclerClickListener {
    private var mAdapter: UniversalAdapter<MiniApp>? = null
    private var partnerCashbackAdapter: TopIconAdapter<NewCashback>? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent?.getBooleanExtra(IS_FAVOURITE, false) == false)
                    intent.getStringExtra(MINI_APP_ID)?.let { mAdapter?.removeItem(it) }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        statusBarColor(R.color.greenstatus)
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        setRecycler()
        topicon()

        return _binding?.root
    }

    private fun topicon() {
        partnerCashbackAdapter = TopIconAdapter(this)
        binding.recyclerTop.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = partnerCashbackAdapter
        }
    }

    private fun setRecycler() {
        if (activity is HomeActivity)
            binding.backIv.visibility = View.GONE

        binding.backIv.setOnClickListener { activity?.onBackPressed() }

        mAdapter = UniversalAdapter(this@FavouriteFragment)
        mAdapter!!.setShowRupee(true)

        binding.recyclerFavourite.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
        getData()
    }

    private fun getData() {
        viewModel.getFavMiniApp(
            AppSharedPreference.getInstance(requireContext()).getUserId(),
            this,
            this
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavouriteFragment()
    }

    override fun onSuccess(t: WebTCashRes?, mess: String?) {
        t?.let {
            it.cashback?.let { cashback ->
                partnerCashbackAdapter?.addAllItem(if (cashback.size > 7) cashback.subList(0, 4) else cashback)
            }

            mAdapter?.addAllItem(t.data)
            _binding?.emptyLi?.visibility = if (it.data.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW) {
            if (data is MiniApp) WebViewActivity.openWebView2(
                startForResult,
                context,
                data.url,
                data.id,
                data.image,
                data.tcash,
                data.is_favourite, data.cashback
            )
        }
    }
}