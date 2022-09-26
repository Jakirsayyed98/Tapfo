package app.tapho.ui.PaytmPaymentGateway

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentPaymentStatusScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.PaytmPaymentGateway.Adapter.PopularPartnerAdapter
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.home.NewHomeFragment
import app.tapho.ui.localbizzUI.UserFlow.HomeScreenFragment
import app.tapho.ui.model.HomeRes
import app.tapho.ui.model.Popular
import app.tapho.ui.recharge.model.ServiceOperatorRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.RechargeDetail
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.parseDateWithFullFormate
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide


class paymentStatusScreenFragment : BaseFragment<FragmentPaymentStatusScreenBinding>() {

    var paymentStatus = ""

    var PopularPartnerAdapter: PopularPartnerAdapter<Popular>? = null

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
        _binding = FragmentPaymentStatusScreenBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.mainBackground.visibility = View.VISIBLE
        val status = activity?.intent?.getStringExtra("status")
        val errorcode = activity?.intent?.getStringExtra("errorcode")
        val txn_Id = activity?.intent?.getStringExtra("txn_Id")
        val statusType = activity?.intent?.getStringExtra("statusType")
        val pspnamedata = activity?.intent?.getStringExtra("pspnamedata")
        _binding!!.PaymentMode.text = pspnamedata
        setData(status, errorcode, txn_Id, statusType)



        onBackPressedMethod()
        VisiblityLayout()

        getTcashdata(txn_Id, statusType, errorcode)
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                _binding!!.mainBackground.visibility = View.GONE
                _binding!!.paymentdetail.visibility = View.VISIBLE
                _binding!!.paymentlayout.visibility = View.VISIBLE
            }, 3000)
        }


        getPopularMiniApps()
        setRecyclerViewData()

        return _binding?.root
    }

    private fun VisiblityLayout() {
        _binding!!.walletDetails.setOnClickListener {
            if (_binding!!.walletDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.imagedown1)
                _binding!!.walletDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.imagedown1)
                _binding!!.walletDetailsform.visibility = View.VISIBLE
            }
        }

        _binding!!.sucessbtn.setOnClickListener {
            activity?.onBackPressed()
            activity?.finish()
        }
        _binding!!.retrybtn.setOnClickListener {
            activity?.onBackPressed()
            activity?.finish()
        }
        _binding!!.donbtnAfterFailed.setOnClickListener {
            activity?.onBackPressed()
//            startActivity(Intent(requireContext(),HomeActivity::class.java))
            activity?.finish()
        }

        _binding!!.RechargeDetails.setOnClickListener {
            if (_binding!!.RechargeDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.imagedown1)
                _binding!!.RechargeDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.imagedown1)
                _binding!!.RechargeDetailsform.visibility = View.VISIBLE
            }
        }
        _binding!!.PaymentMethodDetails.setOnClickListener {
            if (_binding!!.PaymentDetailsform.visibility == View.VISIBLE) {
                Glide.with(requireContext()).load(R.drawable.arrow_down).into(_binding!!.imagedown1)
                _binding!!.PaymentDetailsform.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(R.drawable.arrow_up).into(_binding!!.imagedown1)
                _binding!!.PaymentDetailsform.visibility = View.VISIBLE
            }
        }
    }

    private fun setRecyclerViewData() {
        PopularPartnerAdapter = PopularPartnerAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }

        })
        _binding!!.popularMiniApps.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = PopularPartnerAdapter
        }
    }

    private fun getPopularMiniApps() {
        val popularList: ArrayList<Popular> = ArrayList()
        viewModel.getHomeData("home", getUserId(), this, object : ApiListener<HomeRes, Any?> {
            override fun onSuccess(t: HomeRes?, mess: String?) {
                t.let {
                    it!!.popular.let {
                        popularList.addAll(it!!)
                    }
                    popularList.shuffle()
                    PopularPartnerAdapter!!.addAllItem(if (popularList.size > 3) popularList.subList(0, 3) else popularList)
                }
            }
        })

    }

    private fun onBackPressedMethod() {
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.back1.setOnClickListener {
            SendToHomeScreen()
        }

//        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                //BackPressAction()
//              //  SendToHomeScreen()
//
//            }
//        }
//        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

    private fun SendToHomeScreen() {
        activity?.onBackPressed()
    }

    private fun getTcashdata(txnId: String?, statusType: String?, errorcode: String?) {
        viewModel.getTCashDashboard(
            getUserId(),
            TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),
            this,
            object :
                ApiListener<TCashDasboardRes, Any?> {
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t.let {
                        it!!.txn.forEach {
                            if (it.txn_id.equals(txnId)) {
                                setAllData(it, statusType, errorcode)
                            }
                        }
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setAllData(it: Txn, statusType: String?, errorcode: String?) {
        _binding!!.WalletOrderId.text = it.txn_id
        _binding!!.txnid.text = "TXN ID " + it.txn_id
        _binding!!.paymentDebitAmount.text = "-" + withSuffixAmount(it.amount)
        _binding!!.paybleAmount.text = "-" + withSuffixAmount(it.payment_amount)
        val pm = requireContext().getPackageManager()
        if (it.cashback.equals("0")) {
            _binding!!.cashbacklayout.visibility = View.GONE
        } else {
            _binding!!.cashbacklayout.visibility = View.VISIBLE
        }

        _binding!!.idCreatedAt.text = parseDateWithFullFormate(it.created_at)
        _binding!!.RechargeOrderId.text = it.recharge_detail.get(0).usertx
        _binding!!.PaymentOrderId.text = it.txn_id

        _binding!!.Rechargeamount.text = withSuffixAmount(it.recharge_detail.get(0).amount)
        _binding!!.paymentOption1.text = it.pay_option
        getDataFromRecharge(it.recharge_detail)
        when (statusType) {
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
                    _binding!!.paymentForTitle.text = getString(
                        R.string.payment_of_299_sucessful,
                        withSuffixAmount(it.amount),
                        " Sucessfull"
                    )
                } else {
                    _binding!!.paymentForTitle.text = getString(
                        R.string.payment_of_299_sucessful,
                        withSuffixAmount(it.amount),
                        " Failed"
                    )
                }
            }
            "Paytm" -> {
                if (errorcode.toString().equals("01")) {
                    _binding!!.paymentForTitle.text = getString(
                        R.string.payment_of_299_sucessful,
                        withSuffixAmount(it.amount),
                        " Sucessfull"
                    )
                } else {
                    _binding!!.paymentForTitle.text = getString(
                        R.string.payment_of_299_sucessful,
                        withSuffixAmount(it.amount),
                        " Failed"
                    )
                }
            }
        }

    }

    private fun getDataFromRecharge(rechargeDetail: List<RechargeDetail>) {
        val OpretorId = rechargeDetail.get(0).operator_id
        viewModel.getRechargeServiceOperator(
            getUserId(), "1", this, object : ApiListener<ServiceOperatorRes, Any?> {
                override fun onSuccess(t: ServiceOperatorRes?, mess: String?) {
                    t.let {
                        it!!.data!!.forEach {
                            if (it.id.toString().toInt() == OpretorId.toInt()) {
                                _binding!!.opretorName.text =
                                    getString(R.string.recharge_for, it.name)
                                _binding!!.mobileNumber.text = rechargeDetail.get(0).number
                                Glide.with(requireContext()).load(it.image).centerCrop()
                                    .circleCrop().into(_binding!!.opretorIcon)
                            }
                        }
                    }
                }
            })
    }


    private fun setData(status: String?, errorcode: String?, txnId: String?, statusType: String?) {

        when (statusType) {
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
        //            _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.failedbtnlayout.visibility = View.GONE
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)

                } else {
                    //          _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.red))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                    _binding!!.SuccessScreen.visibility = View.GONE
                    _binding!!.failedScreen.visibility = View.VISIBLE
                    _binding!!.failedbtnlayout.visibility = View.VISIBLE
                    statusBarColor(R.color.red)
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
                    statusBarTextBlack()
                }
            }
            "Paytm" -> {
                if (errorcode.toString().equals("01")) {
                    //            _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.failedbtnlayout.visibility = View.GONE
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)

                } else {
                    //          _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.red))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                    statusBarColor(R.color.red)
                    _binding!!.SuccessScreen.visibility = View.GONE
                    _binding!!.failedScreen.visibility = View.VISIBLE
                    _binding!!.failedbtnlayout.visibility = View.VISIBLE
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
                }
            }
            "Recharge" -> {
                if (status.toString().equals("Success")) {
                    //          _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.failedbtnlayout.visibility = View.GONE
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)
                } else {
                    //        _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.red))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                    statusBarColor(R.color.red)
                    _binding!!.SuccessScreen.visibility = View.GONE
                    _binding!!.failedScreen.visibility = View.VISIBLE
                    _binding!!.failedbtnlayout.visibility = View.VISIBLE
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
                    statusBarTextBlack()
                }
            }


        }

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            paymentStatusScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}