package app.tapho.ui.tcash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentHomeCardToTcashPageBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.WebViewActivity
import app.tapho.ui.home.adapter.*
import app.tapho.ui.model.*
import app.tapho.ui.tcash.adapter.TcashbackOnlyWalletTransaction_Adapter
import app.tapho.ui.tcash.adapter.TcashbackWallet_Adapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.ui.tcashback_detail_Activity
import app.tapho.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson


class HomeCardToTcashPageFragment : BaseFragment<FragmentHomeCardToTcashPageBinding>(),
    RecyclerClickListener {


    private var walletAdapter: TcashbackOnlyWalletTransaction_Adapter? = null

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
        _binding = FragmentHomeCardToTcashPageBinding.inflate(inflater, container, false)
        statusBarColor(R.color.walletscreen)
        statusBarTextBlack()
//        statusBarTextWhite()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
            details()
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }


        _binding!!.transactionLog.setOnClickListener {
            ContainerActivity.openContainer(context, "AvailableBalance", "")
        }

        _binding!!.AddMoneyToWallet.setOnClickListener {
            ContainerActivity.openContainerForVoucher(context, "addtopup", "200","","")
        }

        tcashhistoryDataViewModel1()
        setRecycler()
        return _binding?.root
    }

    private fun addGiftCardPopup() {

        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.giftcard_adding_popup, null)

        dialog.setCancelable(true)
        val giftcardnumber: TextInputEditText = view.findViewById(R.id.giftcard_number)
        val Gift_Card_Pin: TextInputEditText = view.findViewById(R.id.Gift_Card_Pin)
        val btnSetProfile: AppCompatButton = view.findViewById(R.id.btnSetProfile)
//        var isSelected = false

         if (giftcardnumber.text!!.isBlank()) {
            btnSetProfile.isSelected=false
        } else  if (Gift_Card_Pin.text!!.isBlank()) {
            btnSetProfile.isSelected=false
        } else{
            btnSetProfile.isSelected=true
        }


        dialog.setContentView(view)
        dialog.show()
    }


    private fun details() {
        val detailsAdapter = customeCardDetailsAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "Add balance" -> {
                        ContainerActivity.openContainer(context, "addtopup", "")
                    }
                     "Buy Voucher" -> {
                        startActivity(Intent(requireContext(),VouchersActivity::class.java))
                    }
                    "Card Offer" -> {
                        ContainerActivity.openContainer(requireContext(), "AddMoneyCardOffers", "")
                    }
                    "Piggy Bank" -> {
                        ContainerActivity.openContainer(requireContext(), "BuyGiftCard", "")
                    }
                    "Transfer to Bank" -> {
                        ContainerActivity.openContainer(requireContext(), "BuyGiftCard", "")
                    }
                    "Send Card Balance" -> {
                        ContainerActivity.openContainer(requireContext(), "BuyGiftCard", "")
//                        ContainerActivity.openContainer(requireContext(), "Mini Apps", "")
                    }
                    "Card Vouchers" -> {
                        ContainerActivity.openContainer(requireContext(), "BuyGiftCard", "")
//                        addGiftCardPopup()
                    }

                }
            }

        }).apply {

            addItem(CustomeShopCategoryModel(R.drawable.wallet_offer_icon, "Card Offer", "Card Offer"))
            addItem(CustomeShopCategoryModel(R.drawable.wallet_offer_icon, "Buy Voucher", "Buy Voucher"))
//            addItem(CustomeShopCategoryModel(R.drawable.gullak_icon, "Piggy Bank", "Piggy Bank"))
//            addItem(CustomeShopCategoryModel(R.drawable.topup_card_icon, "Add balance", "Add balance"))
//            addItem(CustomeShopCategoryModel(R.drawable.transfer_to_bank_icon, "Transfer to Bank", "Transfer to Bank"))
//            addItem(CustomeShopCategoryModel(R.drawable.send_to_card_balance, "Send Card Balance", "Send Card Balance"))
//            addItem(CustomeShopCategoryModel(R.drawable.card_voucher_icon, "Card Vouchers", "Card Vouchers"))




        }
        _binding!!.allfeature.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = detailsAdapter
        }
    }


    private fun tcashhistoryDataViewModel1() {
        getSharedPreference().getUserId().let {
            viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),
                this, object : ApiListener<TCashDasboardRes, Any?> {
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                        t.let {
                            it!!.txn.let {
                                Log.d("MyTxnData",it.toString())
                                walletAdapter!!.addAllItem(it)
                            }
                            _binding!!.availbalance.text = withSuffixAmount(it.cash_available)
                            if (it.cash_available.toString().toInt()<100){
                                _binding!!.lowbalance.visibility = View.VISIBLE
                            }else{
                                _binding!!.lowbalance.visibility = View.GONE
                            }
                            if (it.txn.isNullOrEmpty().not()){
                                _binding!!.icRupee1.visibility = View.GONE
                            }else{
                                _binding!!.icRupee1.visibility = View.VISIBLE
                            }

                        }
                    }
                })
        }
    }

    private fun setRecycler() {
        walletAdapter = TcashbackOnlyWalletTransaction_Adapter(this)
        _binding!!.transaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = walletAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeCardToTcashPageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
//        if (type == "AppCategory" && data is AppCategory) {
//               openContainer("MiniAppFragment", data, false, data.name)
//        } else if (type == "MiniAppFragment") {
//            openContainer("MiniAppFragment", data, true, getString(R.string.service_more))
//        } else if (type == OPEN_WEB_VIEW) {
//            if (data is MiniApp)
//                openWebView(data)
//        }else  if (type == "detail" && data is TCashDashboardData) {
//            // startActivity(Intent(context, TCashbackDetailActivity::class.java).apply {
//            startActivity(Intent(context, tcashback_detail_Activity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                putExtra(DATA, Gson().toJson(data))
//            })
//        }else{
//            startActivity(Intent(context, TCashbackDetailActivity::class.java))
//        }
    }
    private fun openContainer(type: String, data: Any?, isOldData: Boolean, title: String?) {
        ContainerActivity.openContainer(context, type, data, isOldData, title)
    }

    private fun openWebView(data: MiniApp) {
        //    ActiveCashbackActivity.openWebView(context, data.url, data.id, data.image, data.tcash, data.is_favourite, data.cashback, data.app_category_id)
        WebViewActivity.openWebView(
            context,
            data.url,
            data.id,
            data.image,
            data.tcash,
            data.is_favourite,
            data.cashback,
            data.app_category_id
        )
    }
}