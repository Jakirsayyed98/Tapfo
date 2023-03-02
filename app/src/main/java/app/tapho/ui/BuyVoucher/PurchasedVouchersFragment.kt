package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentPurchasedVouchersBinding
import app.tapho.getExprieOrNot
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.AllVoucher_data
import app.tapho.ui.BuyVoucher.BuyVoucherApimodel.PurchasedVoucherTabModel
import app.tapho.ui.BuyVoucher.adapter.PurchasedVouchersTabAdapter
import app.tapho.ui.BuyVoucher.adapter.purchased_vouchers_list_adapter
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.*
import app.tapho.utils.parseDateExpire
import kotlin.collections.ArrayList

class PurchasedVouchersFragment : BaseFragment<FragmentPurchasedVouchersBinding>() {
    private var categoryTabAdapter: PurchasedVouchersTabAdapter<PurchasedVoucherTabModel>? = null

    var AllvouchersAdapter : purchased_vouchers_list_adapter<AllVoucher_data>?= null
    var listdata : ArrayList<AllVoucher_data> = ArrayList()
    var txndata : ArrayList<Txn> = ArrayList()
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
        _binding = FragmentPurchasedVouchersBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        setTabLayoutdata()
        setPurchasedVoucherLayout()
        getTcashData()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        setAllVoucherdata("All")
        return _binding?.root
    }

    private fun setAllVoucherdata(moretext : String) {
        if (moretext=="All"){
            AllvouchersAdapter!!.clear()
            AllvouchersAdapter!!.addAllItem(listdata)

        }else if (moretext == "Active"){
            listdata.forEach {
                requireContext().getExprieOrNot(parseDateExpire(it.validity)!!)
            }
        }else{

        }

    }

    private fun setPurchasedVoucherLayout() {
        AllvouchersAdapter = purchased_vouchers_list_adapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })
        _binding!!.recyclerVouchers.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = AllvouchersAdapter
        }
    }

    private fun getTcashData() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),"2",
            this, object : ApiListener<TCashDasboardRes, Any?> {
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t.let {
                        it!!.txn.forEach {
                            if (it.type.equals("5")) {
                                txndata.add(it)
                            }
                        }
                        if (txndata.isNullOrEmpty().not()){
                            getVouchersTxnData(txndata)
                        }
                    }
                }
            })
    }

    private fun getVouchersTxnData(txndata: ArrayList<Txn>) {
        txndata.forEach {
           // getVouchersDetails(it.voucher_detail.response_json.vPullVouchersResult)

        }
    }

    private fun getVouchersDetails(vPullVouchersResult: VPullVouchersResult) {
        if (vPullVouchersResult.PullVouchers.isNullOrEmpty().not()){
            vPullVouchersResult.PullVouchers.forEach {
                setVouchersData(it)
            }
        }
    }

    private fun setVouchersData(pullvoucher: PullVoucher) {
        pullvoucher.Vouchers.forEach {

//            listdata.add(AllVoucher_data("",pullvoucher.ProductName,it.VoucherNo,it.EndDate,it.Voucherpin,it.Value,it.VoucherGCcode))
            AllvouchersAdapter!!.addItem(AllVoucher_data("",pullvoucher.ProductName,it.VoucherNo,it.EndDate,it.Voucherpin,it.Value,it.VoucherGCcode))
        }
    }


    private fun setTabLayoutdata() {
        categoryTabAdapter = PurchasedVouchersTabAdapter<PurchasedVoucherTabModel>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                if (data is PurchasedVoucherTabModel){
                    when(data.name){
                        "All"->{
                            setAllVoucherdata("All")
                        }
                        "Active Vouchers"->{
                            setAllVoucherdata("Active")
                        }
                        "Expired Vouchers"->{
                            setAllVoucherdata("All")
                        }

                    }
                }
            }
        }).apply {
                addItem(PurchasedVoucherTabModel("1","All",true))
                addItem(PurchasedVoucherTabModel("2","Active Vouchers",false))
                addItem(PurchasedVoucherTabModel("3","Expired Vouchers",false))
        }
        _binding!!.vouchersCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryTabAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PurchasedVouchersFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}