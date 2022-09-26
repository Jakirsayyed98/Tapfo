package app.tapho.ui.BuyVoucher

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentVoucherCartBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersData
import app.tapho.ui.BuyVoucher.RoomDatabase.VouchersDatabase
import app.tapho.ui.BuyVoucher.adapter.voucher_cart_adapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VoucherCartFragment : BaseFragment<FragmentVoucherCartBinding>() {

    var voucherCartAdapter: voucher_cart_adapter<VouchersData>?=null
    lateinit var database: VouchersDatabase

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
        _binding = FragmentVoucherCartBinding.inflate(inflater,container,false)
        database = VouchersDatabase.getDatabase(requireContext())
        statusBarColor(R.color.white)
        statusBarTextWhite()
        setLayoutData()
        getDataforVoucherCart()

        return _binding?.root
    }

    private fun getDataforVoucherCart() {
        voucherCartAdapter!!.clear()
        database.getVouchersDatabase().getAllVouchersData().observe(requireActivity(), Observer {
            voucherCartAdapter!!.addAllItem(it)
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setLayoutData() {
        voucherCartAdapter = voucher_cart_adapter(object : RecyclerClickListener{
            @SuppressLint("FragmentLiveDataObserve")
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                voucherCartAdapter!!.clear()
                Log.d("Datas",data.toString())
                if (data is VouchersData){
                    GlobalScope.launch {
                        if (type.equals("Update")){
                            Log.d("MyDaya",data.toString())
                            database.getVouchersDatabase().update(data)
                        }else if(type.equals("delete")){
                            database.getVouchersDatabase().delete(data)
                        }

                    }
                }
            }

        })
        _binding!!.cartItem.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = voucherCartAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            VoucherCartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}