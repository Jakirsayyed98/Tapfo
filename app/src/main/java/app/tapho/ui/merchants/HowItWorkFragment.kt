package app.tapho.ui.merchants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentHowItWorkBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.model.WebTCashRes


class HowItWorkFragment :BaseFragment<FragmentHowItWorkBinding>() {

    var miniAppID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHowItWorkBinding.inflate(inflater,container,false)

        miniAppID = activity?.intent?.getStringExtra("miniapp_id").toString()

        miniAppViewodel()

        return _binding?.root
    }

    private fun miniAppViewodel() {
        viewModel.getMiniAppTCash(getUserId(),miniAppID,this,object : ApiListener<WebTCashRes,Any?>{
            override fun onSuccess(t: WebTCashRes?, mess: String?) {
                t!!.let {
                   setTextInData(it)
                }
            }
        })
    }

    private fun setTextInData(it: WebTCashRes) {
        it.let {
            _binding!!.apply {
                storename.text =it.mini_app!!.get(0).name
                description.text =it.mini_app!!.get(0).description
                termsandcondition.text =it.mini_app!!.get(0).terms
                if (it.mini_app!!.get(0).terms.isNullOrEmpty()){
                    termsandconditionlayout.visibility=View.GONE
                }else{
                    termsandcondition.text =it.mini_app!!.get(0).terms
                }
                policy.text = it.mini_app!!.get(0).name
                activeted.text = getString(R.string.activated_by_hammer,it.mini_app!!.get(0).name)
                howtoearn.text = getString(R.string.how_to_earn_extra_cashback_from_kapiva,it.mini_app!!.get(0).name)
                policy1.text = getString(R.string.hammer_will_validates,it.mini_app!!.get(0).name)
                policy2.text = getString(R.string.hammer_takes_45_60_days_to,it.mini_app!!.get(0).name,it.data!!.get(0).report)
                backBtn.setOnClickListener {
                    activity?.onBackPressed()
                }
                _binding?.cashback?.text = it.data.get(0).cashback.toString()

                _binding?.term3?.text = getString(R.string.after_return_refund, it.mini_app?.get(0)?.name)
                _binding?.term1?.text = getString(R.string.open_hammer_link_, it.mini_app?.get(0)?.name, it.data.get(0).cashback.toString())

                _binding?.term2?.text = getString(R.string.hammer_takes_15_mins_to_validate_, it.mini_app?.get(0)?.name,it.data.get(0).report)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HowItWorkFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}