package app.tapho.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentEndToEndEncriptionBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel1
import app.tapho.ui.home.adapter.CustomeEndToEndModel
import app.tapho.ui.home.adapter.EndtoEndEncriptionPointsAdapter
import kotlinx.android.synthetic.main.activity_news.view.*

class EndToEndEncriptionFragment :BaseFragment<FragmentEndToEndEncriptionBinding>() {


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
        _binding  = FragmentEndToEndEncriptionBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        setRVLayout()
        setRVLayout2()
        return _binding!!.root
    }

    private fun setRVLayout() {
        val RvAdApter = EndtoEndEncriptionPointsAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        }).apply {
            addItem(CustomeEndToEndModel(R.drawable.appin_icon,"TAPFO 2FA PIN authentication & biometric","We protect your wallet by offering Tapfo Passcode, mobile security system biometric authentication. They help prevent unauthorized access to your account.",""))
            addItem(CustomeEndToEndModel(R.drawable.fraud_prevention_icon,"Fraud prevention","Your account and transaction are protected 24/7 by a fraud detection engine.",""))
            addItem(CustomeEndToEndModel(R.drawable.online_transcations_icon,"Secured online transcations","Every online  transaction is 3D Secure enabled and authenticated with an OTP bypass security from mobile linked bank.",""))
            addItem(CustomeEndToEndModel(R.drawable.instant_payment_alert,"Instant Payment alerts","Push notification are sent out after every payment from bank & Tapfo keeping you aware of all your transaction.",""))
            addItem(CustomeEndToEndModel(R.drawable.data_encription_icon,"Data encryption ","Account details and transactions are end-to end encrypted. Tapfo doesn’t store or share any personal information with any other entity.",""))
            addItem(CustomeEndToEndModel(R.drawable.data_security_icon,"Data security ","Tapfo stands with the security of end-to-end encryption layer",""))
            addItem(CustomeEndToEndModel(R.drawable.passcode_icon,"Tapfo Passcode","Tapfopay is compliant with Passcode, which is the highest standard of security for account.",""))
        }
        _binding!!.encriptionPointsRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = RvAdApter
        }
    }

    private fun setRVLayout2() {
        val RvAdApter = EndtoEndEncriptionPointsAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        }).apply {
            addItem(CustomeEndToEndModel(R.drawable.seprated_security_icon,"Separated for your security","Your Tapfo balance is held in a dedicated bank account’s transaction logs to secured from end to end data cloud.",""))
            addItem(CustomeEndToEndModel(R.drawable.secure_from_the_start,"Secure from the start","Every transaction on Tapfo needs your fingerprint/face ID, UPI PIN and password for authentication.",""))
            addItem(CustomeEndToEndModel(R.drawable.risk_assessment_icon,"Risk assessment","Our security teams monitor all transactions in real-time to block any suspicious activity. We also report fraud complaints & block fraudulent users from accessing the Tapfo platform",""))
            addItem(CustomeEndToEndModel(R.drawable.payment_privacy,"Payment Privacy","Only you and the person you are transacting with will be able to see the details of your payment.",""))
        }
        _binding!!.encriptionPointsRV2.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = RvAdApter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            EndToEndEncriptionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}