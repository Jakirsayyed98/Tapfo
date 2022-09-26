package app.tapho.ui.tcash.AddMoneyPopup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTopUpBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.home.adapter.customeCardDetailsAdapter
import app.tapho.ui.tcash.AddMoneyPopup.Adapter.CustomeAddMoneySuggetionAdapter
import app.tapho.ui.tcash.AddMoneyPopup.Model.CustomeAddMoneyModel
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.withSuffixAmount


class TopUpFragment : BaseFragment<FragmentTopUpBinding>() {

    var totalAmount = ""
    var Amount = ""
    var CouponApply = ""
    var cashback = ""
    var isSelected = false
    var availablebalance = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopUpBinding.inflate(inflater, container, false)
        Amount = activity?.intent?.getStringExtra("Amount").toString()
        CouponApply = activity?.intent?.getStringExtra("CouponApply").toString()
        cashback = activity?.intent?.getStringExtra("cashback").toString()
        _binding!!.mainLayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
        if (CouponApply.isEmpty().not()) {
            _binding!!.cashbacklayout.visibility = View.VISIBLE
            _binding!!.offerText.text ="Best Offer Applied: "+ withSuffixAmount(cashback)+" Cashback"

        } else {
            _binding!!.cashbacklayout.visibility = View.GONE
        }

        _binding!!.Amount.setText(Amount)
        statusBarTextWhite()
        setAndEnableClick()
        Addmoneysuggetion()
        getTcashdata()


        return _binding?.root
    }

    private fun getTcashdata() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(), this, object : ApiListener<TCashDasboardRes, Any?> {
                override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                    t?.let {
                        availablebalance = it.cash_available.toString()
                        _binding!!.cashavailable.text = withSuffixAmount(it.cash_available)
                        _binding!!.mainLayout.visibility = View.VISIBLE
                        _binding!!.progress.visibility = View.GONE
                    }
                }

            })
    }

    private fun setAndEnableClick() {
        _binding!!.btnVerify.isSelected = true
        _binding!!.Amount.addTextChangedListener {
            if (_binding!!.Amount.text!!.length >= 1) {
                _binding!!.btnVerify.isClickable = true
                _binding!!.btnVerify.isSelected = true

            } else {
                _binding!!.btnVerify.isClickable = false
                _binding!!.btnVerify.isSelected = false
            }
        }

        _binding!!.btnVerify.setOnClickListener {
            if (_binding!!.Amount.text.toString().toDouble() >= 1.00) {
                _binding!!.lessthanAmount.visibility = View.GONE
                _binding!!.MaxthanAmount.visibility = View.VISIBLE
                if (_binding!!.Amount.text.toString().toDouble() <= 20000.00) {

                    if (availablebalance.toDouble() + _binding!!.Amount.text.toString().toDouble() >= 20000) {
                        requireView().showShortSnack("Your Maximum wallet limit is ₹20000. T&C apply")
                    } else {
                        // Intializing Payment without wallet Amount
                        getSharedPreference().saveString("AddWalletAmount", _binding!!.Amount.text.toString())
                        ContainerActivity.openContainer(requireContext(), "AddMoneyToWalletWithPaytm", "")
                        activity?.finish()
                        requireView().showShortSnack("Allow To Update Your Wallet")
                    }
                } else {
                    requireView().showShortSnack("Amount Should be greter than 10 or less than 20000 ")
                }
            } else {
                _binding!!.lessthanAmount.visibility = View.VISIBLE
                _binding!!.MaxthanAmount.visibility = View.GONE
                requireView().showShortSnack("Amount Should be greter than 10 or less than 20000 ")
            }
        }

        _binding!!.Amount.addTextChangedListener {
            if (_binding!!.Amount.text.isNullOrEmpty()) {
                totalAmount = "0"
                Addmoneysuggetion()
            }
        }
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun Addmoneysuggetion() {
        val detailsAdapter = CustomeAddMoneySuggetionAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                _binding!!.Amount.setText(data.toString())
                /*
                when (data) {
                    "500" -> {
                        _binding!!.Amount.text!!.clear()
                        totalAmount = data.toString()
                        _binding!!.Amount.setText(totalAmount.toString())

                    }
                    "1000" -> {
                        _binding!!.Amount.text!!.clear()
                        totalAmount = data.toString()
                        _binding!!.Amount.setText(totalAmount.toString())

                    }

                    "2000" -> {
                        _binding!!.Amount.text!!.clear()
                        totalAmount = data.toString()
                        _binding!!.Amount.setText(totalAmount.toString())

                    }
                    "5000" -> {
                        _binding!!.Amount.text!!.clear()
                        totalAmount = data.toString()
                        _binding!!.Amount.setText(totalAmount.toString())

                    }

                }

                 */
            }

        }).apply {

            addItem(CustomeAddMoneyModel(500, "500", false))
            addItem(CustomeAddMoneyModel(1000, "1000", false))
            addItem(CustomeAddMoneyModel(2000, "2000", false))
            addItem(CustomeAddMoneyModel(5000, "5000", false))
        }
        _binding!!.AmountRV.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = detailsAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TopUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}