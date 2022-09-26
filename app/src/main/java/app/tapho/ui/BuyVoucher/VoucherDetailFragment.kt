package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import app.tapho.R
import app.tapho.TapfoApplication
import app.tapho.databinding.FragmentVoucherDetail2Binding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.RequestViewModel
import app.tapho.showLong
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.RoomDatabase.*
import app.tapho.ui.BuyVoucher.VoucherDetails.CustomeVoucherlistDenomination
import app.tapho.ui.BuyVoucher.VoucherDetails.voucherdatailRes
import app.tapho.ui.BuyVoucher.VoucherListViewModel.Data
import app.tapho.ui.BuyVoucher.adapter.voucher_denomination_list_adapter
import app.tapho.ui.ContainerActivity
import app.tapho.utils.CONTAINER_TYPE_KEY
import app.tapho.utils.DATA
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.paytm.pgsdk.Log
import kotlinx.android.synthetic.main.fragment_d_t_h_recharge_final_screen.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VoucherDetailFragment : BaseFragment<FragmentVoucherDetail2Binding>(),
    ApiListener<voucherdatailRes, Any?>, RecyclerClickListener {

    var voucher_denomination_list_adapter: voucher_denomination_list_adapter<CustomeVoucherlistDenomination>? = null
    lateinit var database: VouchersDatabase

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVoucherDetail2Binding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        activity?.intent?.getStringExtra(DATA).let {
            val data = Gson().fromJson(it, Data::class.java)
            setDataLayout(data)
        }
        setdenominationLayout()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.viewcart.setOnClickListener {
            ContainerActivity.openContainer(requireContext(),"OpenVoucherCart","")
        }
        // database = Room.databaseBuilder(requireContext(),VouchersDatabase::class.java,"voucherdata_db").build()
        database = VouchersDatabase.getDatabase(requireContext())




        return _binding?.root
    }


    private fun setDataLayout(data: Data?) {
        getViewmodelData(data!!.id)
    }

    private fun getViewmodelData(id: String) {
        viewModel.getVoucherDetail(getUserId(), id, this, this)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            VoucherDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: voucherdatailRes?, mess: String?) {
        t.let {
            setTextData(it)
        }
    }

    private fun setTextData(it: voucherdatailRes?) {
        val userDiscount = it!!.data.get(0).user_discount
        _binding!!.storename.text = it.data.get(0).name
        _binding!!.userDiscount.text = it.data.get(0).user_discount
        Glide.with(requireContext()).load(it.data.get(0).logo).centerCrop().into(_binding!!.icon)
        Glide.with(requireContext()).load(it.data.get(0).banner).centerCrop()
            .into(_binding!!.banner)
        Glide.with(requireContext()).load(it.data.get(0).logo).centerCrop().into(_binding!!.logo)
        val voucherdenominationsize = it.voucherlist_denomination.size
        _binding!!.denominationCount.text = voucherdenominationsize.toString() + " Denomination"
        _binding!!.denominationValue.text = "From " + withSuffixAmount(it.voucherlist_denomination.get(0).price) + " - " + withSuffixAmount(
                it.voucherlist_denomination.get(voucherdenominationsize - 1).price
            )

        it.voucherlist_denomination.forEach { itt ->
            voucher_denomination_list_adapter!!.addItem(
                CustomeVoucherlistDenomination(
                    it.data.get(0).name,
                    it.data.get(0).logo,
                    itt.denomination_key,
                    itt.id,
                    itt.popular,
                    itt.price,
                    itt.status,
                    itt.voucherlist_id,
                    userDiscount
                )
            )
        }


    }

    private fun setdenominationLayout() {
        voucher_denomination_list_adapter = voucher_denomination_list_adapter(this)

        _binding!!.denominationlist.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = voucher_denomination_list_adapter
        }
    }


    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is CustomeVoucherlistDenomination) {

            when (type) {
                "add" -> {
                    addDatatoDB(data, pos.toString())
                }
                "delete" -> {
                    deleteDatafromDB(data, pos.toString())
                }
            }

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deleteDatafromDB(data: CustomeVoucherlistDenomination, qty: String) {
        val setData = VouchersData(0, data.Mini_icon, data.Mininame, data.price, "10", qty, data.voucherlist_id, data.id)
        GlobalScope.launch {
            database.getVouchersDatabase().delete(setData)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addDatatoDB(data: CustomeVoucherlistDenomination, Qty: String) {
        val setData = VouchersData(0, data.Mini_icon, data.Mininame, data.price, "10", Qty, data.voucherlist_id, data.id)


                GlobalScope.launch {
                    database.getVouchersDatabase().insert(setData)
                }





    }

}