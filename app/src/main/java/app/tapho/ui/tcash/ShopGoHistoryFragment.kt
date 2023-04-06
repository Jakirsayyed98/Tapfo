package app.tapho.ui.tcash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentShopGoHistoryBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.scanner.ScanCart.ContainerForProductActivity
import app.tapho.ui.tcash.adapter.OrderListAdapter
import app.tapho.ui.tcash.model.BusinessOrderDetail
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.DATA
import app.tapho.utils.TITLE
import com.google.gson.Gson


class ShopGoHistoryFragment : BaseFragment<FragmentShopGoHistoryBinding>() {
    var Transaction_type=""
    var OrderListAdapter:OrderListAdapter<Txn>?=null
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
        _binding = FragmentShopGoHistoryBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        Transaction_type = activity?.intent?.getStringExtra("Tcash_type").toString()
        _binding!!.textView26.text  = activity?.intent?.getStringExtra(TITLE).toString()

        val data = activity?.intent?.getStringExtra(DATA)
        if (data != null){
            Gson().fromJson(data,TCashDasboardRes::class.java).let {
                getTCashdata(it)
            }
        }

        return _binding?.root
    }

    private fun getTCashdata(tcash: TCashDasboardRes?) {
        val txnData : ArrayList<Txn> = ArrayList()
        tcash.let {
            it!!.txn.forEach {
                if (it.type.equals("8")) {
                    txnData.add(it)
                }
            }
            _binding!!.progress.visibility = View.GONE
            setRVLayout(txnData)
        }
    }

    private fun setRVLayout(txnData: ArrayList<Txn>) {
      OrderListAdapter = OrderListAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is BusinessOrderDetail){
                    ContainerForProductActivity.openContainer(requireContext(), "TapMartOrderConformationFragment", data, false, "")
                }
            }
        })

        _binding!!.transaction.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = OrderListAdapter
        }

        OrderListAdapter!!.addAllItem(txnData)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ShopGoHistoryFragment().apply {
                arguments = Bundle().apply {
                 }
            }
    }
}