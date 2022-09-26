package app.tapho.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentMiniAppsearchfragmentBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.UniversalAdapter
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.WebTCashRes
import app.tapho.utils.OPEN_WEB_VIEW


class MiniAppsearchfragmentFragment : BaseFragment<FragmentMiniAppsearchfragmentBinding>(),ApiListener<WebTCashRes,Any?>,RecyclerClickListener {

    private var serviceAdapter: UniversalAdapter<MiniApp>? = null
    private var popularList: ArrayList<MiniApp> = ArrayList()
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
        _binding = FragmentMiniAppsearchfragmentBinding.inflate(inflater,container,false)
        _binding!!.searchEt.doAfterTextChanged {
            serachViewModel(it.toString())
        }
        setLayoutForRV()
        return _binding?.root
    }

    private fun setLayoutForRV() {
        dismissLoader()
        _binding?.backIv?.setOnClickListener {
            activity?.onBackPressed()
        }
        serviceAdapter = UniversalAdapter(this)
        _binding?.recycler?.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = serviceAdapter
        }
    }

    private fun serachViewModel(query: String) {
        viewModel.searchMiniApp(getUserId(),query,this,this)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MiniAppsearchfragmentFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    override fun showLoader() {
        _binding!!.progress.visibility = View.VISIBLE
    }

    override fun dismissLoader() {
        _binding?.progress?.visibility = View.GONE
    }


    override fun onSuccess(t: WebTCashRes?, mess: String?) {
            t.let {
                it!!.data.let {
                    serviceAdapter!!.addAllItem(it)
                }
            }

    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (type == OPEN_WEB_VIEW)
            if (data is MiniApp)
                WebViewActivity.openWebView(context,
                    data.url,
                    data.id,
                    data.image,
                    data.tcash,
                    data.is_favourite,data.cashback,data.app_category_id)
    }
}