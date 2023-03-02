package app.tapho.ui.tcash.DirectPaytmTransaction

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import app.tapho.R
import app.tapho.databinding.FragmentStatusPreviewScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import com.bumptech.glide.Glide


class StatusPreviewScreenFragment : BaseFragment<FragmentStatusPreviewScreenBinding>() {

    var status = ""
    var errorcode = ""
    var txn_Id =""
    var statusType =""

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
        _binding = FragmentStatusPreviewScreenBinding.inflate(inflater,container,false)

        status = activity?.intent?.getStringExtra("status").toString()
        errorcode = activity?.intent?.getStringExtra("errorcode").toString()
        txn_Id = activity?.intent?.getStringExtra("txn_Id").toString()
        statusType = activity?.intent?.getStringExtra("statusType").toString()
        val pspnamedata = activity?.intent?.getStringExtra("pspnamedata")
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback)

        getTCashdashboard(txn_Id.toString())

        setData(status,errorcode,txn_Id,statusType)
//        Looper.myLooper()?.let {
//            Handler(it).postDelayed ({
//                kotlin.runCatching {
//                    ContainerActivity.openContainerforPaymentStatus(requireContext(),"AddMoneyTransactionStatus",errorcode,txn_Id!!,status,"wallet",pspnamedata,"")
//                    activity?.finish()
//                }
//
//            }, 2000)
//        }

        return _binding?.root
    }

    private fun getTCashdashboard(txn_Id:String) {
        viewModel.getTCashDashboard(getUserId(),TimePeriodDialog.getCurrentDate(),TimePeriodDialog.getCurrentDate(),"2",this,object : ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                t!!.let {
                    it.txn.forEach {
                        if (it.txn_id.equals(txn_Id)){
                            ContainerActivity.openContainerforPaymentStatus(requireContext(),"AddMoneyTransactionStatus",errorcode,"",status,"wallet","","",it)
                            activity?.finish()
                        }
                    }
                }
            }
        })
    }


    private fun setData(status: String?, errorcode: String?, txnId: String?,statusType : String?) {

        when (statusType){
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)

                } else {
                    statusBarColor(R.color.red)
                    Glide.with(requireContext()).asGif().load(R.drawable.new_failed_icon).into( _binding!!.animationView)
                    statusBarTextBlack()
                }
            }
            "Paytm" -> {
                if (errorcode.toString().equals("01")) {
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)

                } else {
                    statusBarColor(R.color.red)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.new_failed_icon).into( _binding!!.animationView)
                }
            }
        }

    }



    companion object {

        @JvmStatic
        fun newInstance() =
            StatusPreviewScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}