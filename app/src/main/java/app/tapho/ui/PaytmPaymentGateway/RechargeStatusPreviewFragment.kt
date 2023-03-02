package app.tapho.ui.PaytmPaymentGateway

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentRechargeStatusPreviewBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import com.bumptech.glide.Glide


class RechargeStatusPreviewFragment : BaseFragment<FragmentRechargeStatusPreviewBinding>() {


    var status =""
    var errorcode =""
    var txn_Id=""
    var statusType=""
    var result =""
    var pspnamedata =""

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
        _binding = FragmentRechargeStatusPreviewBinding.inflate(inflater,container,false)

        status = activity?.intent?.getStringExtra("status").toString()
        errorcode = activity?.intent?.getStringExtra("errorcode").toString()
        txn_Id = activity?.intent?.getStringExtra("txn_Id").toString()
        statusType = activity?.intent?.getStringExtra("statusType").toString()
        pspnamedata = activity?.intent?.getStringExtra("pspnamedata").toString()
        result = activity?.intent?.getStringExtra("result").toString()
//        val AlarmMusic = MediaPlayer.create(requireContext(), R.raw.error_1)
//        AlarmMusic.isLooping = false
//        AlarmMusic.start()
        setData(status, errorcode, txn_Id, statusType)
        getTCashdahboardAPI(txn_Id.toString())
//        Looper.myLooper()?.let {
//            Handler(it).postDelayed({
//                kotlin.runCatching {
//                    ContainerActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatus",errorcode,txn_Id!!,status,statusType,pspnamedata,result)
//                    activity?.finish()
//                }
//            }, 3000)
//        }
        return _binding?.root
    }

    private fun getTCashdahboardAPI(txn_Id:String) {
        viewModel.getTCashDashboard(getUserId(),TimePeriodDialog.getCurrentDate(),TimePeriodDialog.getCurrentDate(),"2",this,object : ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {

                t!!.txn.forEach {
                    if (it.id.equals(txn_Id)) {
                        ContainerActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatus",errorcode,"",status,statusType,pspnamedata,result,it)
                        activity?.finish()
                    }
                }
            }
        })
    }


    private fun setData(status: String?, errorcode: String?, txnId: String?, statusType: String?) {

        when (statusType) {
            "wallet" -> {
                if (errorcode.toString().equals("0")) {
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)

                } else {
                    statusBarColor(R.color.red)
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
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
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
                }
            }
            "Recharge" -> {
                if (status.toString().equals("Success")) {
                    statusBarColor(R.color.GreenWalletBackgroundDark)
                    statusBarTextBlack()
                    Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into( _binding!!.animationView)


                } else {
                    statusBarColor(R.color.red)
                    Glide.with(requireContext()).asGif().load(R.drawable.failed_icon).into( _binding!!.animationView)
                    statusBarTextBlack()
                }
            }


        }

    }



    companion object {

        @JvmStatic
        fun newInstance() =
            RechargeStatusPreviewFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}