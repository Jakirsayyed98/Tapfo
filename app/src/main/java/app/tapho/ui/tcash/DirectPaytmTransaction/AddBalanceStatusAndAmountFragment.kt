package app.tapho.ui.tcash.DirectPaytmTransaction

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import app.tapho.R
import app.tapho.databinding.FragmentAddBalanceStatusAndAmountBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.PaytmPaymentGateway.Adapter.PopularPartnerAdapter
import app.tapho.ui.home.HomeActivity
import app.tapho.ui.model.Popular
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.parseDateWithFullFormate
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide


class AddBalanceStatusAndAmountFragment : BaseFragment<FragmentAddBalanceStatusAndAmountBinding>() {


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
        _binding = FragmentAddBalanceStatusAndAmountBinding.inflate(layoutInflater,container,false)
        _binding!!.mainBackground.visibility = View.VISIBLE
        val status = activity?.intent?.getStringExtra("status")
        val errorcode = activity?.intent?.getStringExtra("errorcode")
        val txn_Id = activity?.intent?.getStringExtra("txn_Id")
        val statusType = activity?.intent?.getStringExtra("statusType")
        val pspnamedata = activity?.intent?.getStringExtra("pspnamedata")
        _binding!!.paymentOption.text = pspnamedata
        setData(status,errorcode,txn_Id,statusType)

        onBackPressedMethod()


        getTcashdata(txn_Id, statusType,errorcode)
        Looper.myLooper()?.let {
            Handler(it).postDelayed ({
                _binding!!.mainBackground.visibility = View.GONE
                _binding!!.paymentdetail.visibility = View.VISIBLE
                _binding!!.paymentlayout.visibility = View.VISIBLE

            }, 3000)
        }

        VisiblityLayout()

        _binding!!.sucessDone.setOnClickListener {
            startActivity(Intent(requireContext(),HomeActivity::class.java))
            activity?.finish()
        }

        _binding!!.retrybtn.setOnClickListener {
            activity?.onBackPressed()
            activity?.finish()
        }
        _binding!!.donbtnAfterFailed.setOnClickListener {
            SendToHomeScreen()
          //  startActivity(Intent(requireContext(),HomeActivity::class.java))
            activity?.finish()
        }

        return _binding?.root
    }

    private fun onBackPressedMethod() {
        _binding!!.back.setOnClickListener {
            SendToHomeScreen()
            activity?.finish()
        }
        _binding!!.back1.setOnClickListener {
            SendToHomeScreen()
            activity?.finish()
        }
    }

    private fun SendToHomeScreen() {
//        startActivity(Intent(requireContext(),HomeActivity::class.java))
        activity?.onBackPressed()
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

    }
    private fun getTcashdata(txnId: String?, statusType : String?,errorcode:String?) {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),this,object : ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
             t.let {
                 it!!.txn.forEach {

                     if (it.txn_id.equals(txnId)){
                         setAllData(it,statusType,errorcode)
                     }
                 }
             }
            }
        })
    }

    private fun setAllData(it: Txn,statusType : String?,errorcode:String?) {
        _binding!!.WalletOrderId.text = it.txn_id
        _binding!!.txnid.text ="TXN ID "+ it.txn_id

        _binding!!.Cashback.text = withSuffixAmount(it.cashback)
        val pm = requireContext().getPackageManager()
        Glide.with(requireContext()).load(pm.getApplicationIcon("app.tapho")).circleCrop().into(_binding!!.icon)
        if (it.cashback.equals("0")){
            _binding!!.cashbacklayout.visibility = View.GONE
        }else{
            _binding!!.cashbacklayout.visibility = View.VISIBLE
        }
        _binding!!.amount.text = withSuffixAmount(it.amount)

        _binding!!.idCreatedAt.text = parseDateWithFullFormate(it.created_at)



        when (statusType) {
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
                    _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(it.amount)," Sucessful")
                } else {
                    _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(it.amount)," Failed")
                }
            }
            "Paytm" -> {
                if (errorcode.toString().equals("01")) {
                    _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(it.amount)," Sucessful")
                } else {
                    _binding!!.paymentForTitle.text = getString(R.string.payment_of_299_sucessful, withSuffixAmount(it.amount)," Failed")
                }
            }
        }

    }


    private fun setData(status: String?, errorcode: String?, txnId: String?,statusType : String?) {

        when (statusType){
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
              //      _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.sucessbtnlayout.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.failedbtnlayout.visibility = View.GONE
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)

                } else {
             //       _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.red))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                    _binding!!.SuccessScreen.visibility = View.GONE
                    _binding!!.sucessbtnlayout.visibility = View.GONE
                    _binding!!.failedScreen.visibility = View.VISIBLE
                    _binding!!.failedbtnlayout.visibility = View.VISIBLE
                    statusBarColor(R.color.red)
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
                    statusBarTextBlack()
                }
            }
            "Paytm" -> {
                if (errorcode.toString().equals("01")) {
             //       _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.GreenWalletBackgroundDark))
                    _binding!!.SuccessScreen.visibility = View.VISIBLE
                    _binding!!.sucessbtnlayout.visibility = View.VISIBLE
                    _binding!!.failedScreen.visibility = View.GONE
                    _binding!!.failedbtnlayout.visibility = View.GONE
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)

                } else {
                 //   _binding!!.mainBackground.setBackgroundColor(resources.getColor(R.color.red))
                    _binding!!.paymentdetail.setBackgroundColor(resources.getColor(R.color.red))
                    statusBarColor(R.color.red)
                    _binding!!.SuccessScreen.visibility = View.GONE
                    _binding!!.sucessbtnlayout.visibility = View.GONE
                    _binding!!.failedScreen.visibility = View.VISIBLE
                    _binding!!.failedbtnlayout.visibility = View.VISIBLE
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
                }
            }
        }

    }



    companion object {

        @JvmStatic
        fun newInstance() =
            AddBalanceStatusAndAmountFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}