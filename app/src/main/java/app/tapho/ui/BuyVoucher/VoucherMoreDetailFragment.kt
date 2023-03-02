package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import app.tapho.R
import app.tapho.databinding.FragmentVoucherMoreDetailBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VoucherDetails.Data
import app.tapho.utils.DATA
import com.google.gson.Gson

class VoucherMoreDetailFragment : BaseFragment<FragmentVoucherMoreDetailBinding>() {


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
        _binding = FragmentVoucherMoreDetailBinding.inflate(inflater, container, false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        setLayout()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        return _binding?.root
    }

    private fun setLayout() {
        activity?.intent?.getStringExtra(DATA).let {
            val data = Gson().fromJson(it, Data::class.java)
            settextData(data)
        }
    }

    private fun settextData(data: Data?) {
        _binding!!.title.text ="About "+data!!.name+" Voucher"

        _binding!!.thingsTonotes.text =  data.things_note.toString().parseAsHtml()
        _binding!!.howtoredeem.text =  data.redeem.toString().parseAsHtml()
        _binding!!.termsandcondition.text = data.terms_condition.toString().parseAsHtml()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            VoucherMoreDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}