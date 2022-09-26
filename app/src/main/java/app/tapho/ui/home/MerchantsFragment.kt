package app.tapho.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.databinding.FragmentMerchantsBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.Popular
import app.tapho.utils.OPEN_WEB_VIEW


/**
 * A simple [Fragment] subclass.
 * Use the [MerchantsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MerchantsFragment : BaseFragment<FragmentMerchantsBinding>(), RecyclerClickListener {
    private val list = ArrayList<Popular>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMerchantsBinding.inflate(inflater, container, false)
        setAdapter()
        return _binding?.root
    }

    private fun setAdapter() {
        val mAdapter = UniversalAdapter<Popular>(this)
        mAdapter.addAllItem(list)
        binding.recyclerMerchants.apply {
            layoutManager = GridLayoutManager(
                context,
                4
            )
            adapter = mAdapter
        }
    }

    fun setList(list: List<Popular>) {
        this.list.addAll(list)
    }

    fun getList():ArrayList<Popular>{
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MerchantsFragment()
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
         if (type == OPEN_WEB_VIEW) {
            if(data is MiniApp)
                WebViewActivity.openWebView(context,data.url,data.id,data.image,data.tcash,data.is_favourite,data.cashback,data.app_category_id)
        }
    }
}