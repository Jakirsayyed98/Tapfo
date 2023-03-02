package app.tapho.ui.BuyVoucher

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentVoucherDetail2Binding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.RoomDatabase.*
import app.tapho.ui.BuyVoucher.VoucherDetails.CustomeVoucherlistDenomination
import app.tapho.ui.BuyVoucher.VoucherDetails.voucherdatailRes
import app.tapho.ui.BuyVoucher.VoucherListViewModel.CustomeVoucherList
import app.tapho.ui.BuyVoucher.VoucherListViewModel.Data
import app.tapho.ui.BuyVoucher.VoucherPayments.VoucherPaymentBaseActivity
import app.tapho.ui.BuyVoucher.adapter.voucher_denomination_list_adapter
import app.tapho.ui.ContainerActivity
import app.tapho.utils.DATA
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_d_t_h_recharge_final_screen.*
import kotlinx.android.synthetic.main.fragment_voucher_detail2.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VoucherDetailFragment : BaseFragment<FragmentVoucherDetail2Binding>(),
    ApiListener<voucherdatailRes, Any?>, RecyclerClickListener {

    var voucher_denomination_list_adapter: voucher_denomination_list_adapter<CustomeVoucherlistDenomination>? = null
    lateinit var database: VouchersDatabase
    var voucherdata: ArrayList<VouchersData> = ArrayList()
    val handler =Handler(Looper.getMainLooper())
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

        CallAllApis()
        _binding!!.retryinternetButton.setOnClickListener {
            CallAllApis()
        }




        return _binding?.root
    }

    private fun CallAllApis() {
        progressVISIBLE()
        activity?.intent?.getStringExtra(DATA).let {
            val data = Gson().fromJson(it, CustomeVoucherList::class.java)
            setDataLayout(data)

            kotlin.runCatching {
                when(data.redeemOption){
                    "1"->{

                        Glide.with(requireContext()).load(R.drawable.redeem_online_icon).placeholder(R.drawable.loding_app_icon).into(_binding!!.redeemicon)
                        _binding!!.redeemtext.text = "Redeemable\nonline"

                    }
                    "2"->{
                        Glide.with(requireContext()).load(R.drawable.instore_redeem_icon).placeholder(R.drawable.loding_app_icon).into(_binding!!.redeemicon)
                        _binding!!.redeemtext.text = "Redeemable\nIn-Store"
                    }
                    "3"->{
                        Glide.with(requireContext()).load(R.drawable.redeem_online_and_offline).placeholder(R.drawable.loding_app_icon).into(_binding!!.redeemicon)
                        _binding!!.redeemtext.text = "Redeemable\nonline & In-store"
                    }else->{
                    Glide.with(requireContext()).load(R.drawable.redeem_online_and_offline).placeholder(R.drawable.loding_app_icon).into(_binding!!.redeemicon)
                    _binding!!.redeemtext.text = "Redeemable\nonline & In-store"
                }
                }

            }
        }
        setdenominationLayout()
        _binding!!.backbtn.setOnClickListener {
            handler.removeMessages(0)
            activity?.onBackPressedDispatcher?.onBackPressed()

        }

        database = VouchersDatabase.getDatabase(requireContext())
        getDataFromDB()


    }



    private fun getDataFromDB() {
        voucherdata.clear()
        database.getVouchersDatabase().getAllVouchersData().observe(requireActivity(), Observer {
            voucherdata.addAll(it)
            if (it.isNullOrEmpty().not()){
                _binding!!.ProceddToPurchaseVoucher.visibility = View.VISIBLE
            }else{

            }
        })


        _binding!!.ProceddToPurchaseVoucher.setOnClickListener {
            activity?.intent?.getStringExtra(DATA).let {
                val data = Gson().fromJson(it, Data::class.java)
                VoucherPaymentBaseActivity.openContainer(requireContext(),"InitiatePaymentForVoucher",data)
               activity?.finish()
            }

        }
    }


    private fun setDataLayout(data: CustomeVoucherList?) {
        getViewmodelData(data!!.id)
    }

    private fun getViewmodelData(id: String) {
        getSharedPreference().saveString("StartLoadingVouchersdata","0")
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
            getSharedPreference().saveString("StartLoadingVouchersdata","1")
            showHome()
            setTextData(it)
            showHome()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTextData(it: voucherdatailRes?) {
        val userDiscount = it!!.data.get(0).user_discount
        _binding!!.name.text = it.data.get(0).name+" E-Gift Vouchers"
        _binding!!.voucher.text ="Buy "+ it.data.get(0).name+"  e-Vouchers"
        _binding!!.discount.text ="Flat "+ it.data.get(0).user_discount+"% Discount"
        Glide.with(requireContext()).load(it.data.get(0).logo).circleCrop().into(_binding!!.icon)
        if (it.data.get(0).banner.isNullOrEmpty()){
            _binding!!.banner.visibility = View.GONE
        }else{
            Glide.with(requireContext()).load(it.data.get(0).banner).centerCrop().into(_binding!!.banner)
        }

        val voucherdenominationsize = it.voucherlist_denomination.size

        _binding!!.details.setOnClickListener { click->
            ContainerActivity.openContainer(requireContext(),"OpenVoucherDetails",it.data.get(0))
        }

//        _binding!!.denominationValue.text = "From " + withSuffixAmount(it.voucherlist_denomination.get(0).price) + " - " + withSuffixAmount(
//                it.voucherlist_denomination.get(voucherdenominationsize - 1).price
//            )

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
                    userDiscount,
                    0
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


    @OptIn(DelicateCoroutinesApi::class)
    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is VouchersData) {
            when (type) {
                "add" -> {
                    if (voucherdata.toString().contains(data.denominationKey)){
                        GlobalScope.launch  {
                            database.getVouchersDatabase().update(pos.toString(),data.PaybleAmount,data.denominationKey)

                        }
                    }else{
                        GlobalScope.launch  {
                            database.getVouchersDatabase().insert(data)

                        }
                    }


                }
                "delete" -> {
                    if (voucherdata.toString().contains(data.denominationKey)){
                        voucherdata.forEach {
                            if (data.denominationKey==it.denominationKey){
                                GlobalScope.launch  {
                                    database.getVouchersDatabase().delete(VouchersData(it.id,data.MiniApp_icon,data.name,data.ActualAmount,data.PaybleAmount,pos.toString(),data.voucherID,data.denominationKey,data.denominationId))

                                }
                            }
                        }

                    }

                }
            }

        }
    }



    fun progressVISIBLE() {
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.VISIBLE
        _binding!!.lowconnection.visibility = View.GONE

    }
    fun lowConnection() {
        _binding!!.lowconnection.visibility = View.VISIBLE
        _binding!!.homeScreenLayout.visibility = View.GONE
        _binding!!.shimmerViewContainer.visibility = View.GONE

    }

    fun showHome() {
        _binding!!.shimmerViewContainer.visibility = View.GONE
        _binding!!.lowconnection.visibility = View.GONE
        _binding!!.homeScreenLayout.visibility = View.VISIBLE

    }


}