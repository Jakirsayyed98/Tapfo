package app.tapho.ui.BuyVoucher.VoucherPayments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentVoucherStatusPreviewBinding
import app.tapho.ui.BaseFragment
import com.bumptech.glide.Glide


class VoucherStatusPreviewFragment : BaseFragment<FragmentVoucherStatusPreviewBinding>() {


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
        _binding = FragmentVoucherStatusPreviewBinding.inflate(inflater,container,false)


        val status = activity?.intent?.getStringExtra("status")
        val errorcode = activity?.intent?.getStringExtra("errorcode")
        val txn_Id = activity?.intent?.getStringExtra("txn_Id")
        val statusType = activity?.intent?.getStringExtra("statusType")
        val pspnamedata = activity?.intent?.getStringExtra("pspnamedata")
        setData(status, errorcode, txn_Id, statusType)
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                VoucherPaymentBaseActivity.openContainerforPaymentStatus(requireContext(),"TransactionStatusForVoucher",errorcode,txn_Id!!,status,statusType,pspnamedata)
                activity?.finish()
            }, 3000)
        }

        return _binding?.root
    }

    private fun setData(status: String?, errorcode: String?, txnId: String?, statusType: String?) {
        if (errorcode.toString().equals("0")) {
            statusBarColor(R.color.GreenWalletBackgroundDark)
            statusBarTextBlack()
            Glide.with(requireContext()).asGif().load(R.drawable.sucess_icon).into(_binding!!.animationView)
        } else {
            statusBarColor(R.color.red)
            Glide.with(requireContext()).asGif().load(R.drawable.new_failed_icon).into(_binding!!.animationView)
            statusBarTextBlack()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            VoucherStatusPreviewFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}