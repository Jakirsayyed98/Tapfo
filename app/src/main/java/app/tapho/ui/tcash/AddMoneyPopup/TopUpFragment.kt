package app.tapho.ui.tcash.AddMoneyPopup

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.databinding.FragmentTopUpBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.AddMoneyPopup.Adapter.CustomeAddMoneySuggetionAdapter
import app.tapho.ui.tcash.AddMoneyPopup.Model.CustomeAddMoneyModel
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.AddMoneyVoucers.AddWalletVoucherRes
import app.tapho.ui.tcash.model.AddMoneyVoucers.Data
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.utils.DATA
import app.tapho.utils.withSuffixAmount
import com.google.gson.Gson


class TopUpFragment : BaseFragment<FragmentTopUpBinding>() {
    var tcashdashboard: TCashDasboardRes? = null
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
        if (CouponApply.isNullOrEmpty().not()) {
            _binding!!.cashbacklayout.visibility = View.VISIBLE
            _binding!!.offerText.text =
                "Cashback Applied: " + withSuffixAmount(cashback) + " Cashback"
        } else {
            _binding!!.cashbacklayout.visibility = View.GONE
        }
        _binding!!.Amount.setText(Amount)



        statusBarTextWhite()
        Addmoneysuggetion()
        getTcashdata()
        setAndEnableClick()
        return _binding?.root
    }

    private fun getTcashdata() {

        val data = activity?.intent?.getStringExtra(DATA)
        if (data.isNullOrEmpty().not()) {
            val tcash = Gson().fromJson(data, TCashDasboardRes::class.java)
            tcash.let {
                availablebalance = it.cash_available.toString()
                _binding!!.cashavailable.text = withSuffixAmount(it.cash_available.toString())
                _binding!!.mainLayout.visibility = View.VISIBLE
                _binding!!.progress.visibility = View.GONE
            }
        } else {
            viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(), "2", this, object : ApiListener<TCashDasboardRes, Any?> {
                    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                        t?.let {
                            availablebalance = it.cash_available.toString()
                            _binding!!.cashavailable.text =
                                withSuffixAmount(it.cash_available.toString())
                            _binding!!.mainLayout.visibility = View.VISIBLE
                            _binding!!.progress.visibility = View.GONE
                        }
                    }
                })


        }
    }

    private fun setAndEnableClick() {
        _binding!!.btnVerify.isSelected = true
        _binding!!.Amount.addTextChangedListener {

            _binding!!.btnVerify.isClickable = true
            _binding!!.btnVerify.isSelected = true
            if (_binding!!.Amount.text!!.length >= 2) {
                if (_binding!!.Amount.text.toString().toDouble() <= 10000) {
                    _binding!!.btnVerify.isClickable = true
                    _binding!!.btnVerify.isSelected = true
                } else {
                    _binding!!.btnVerify.isClickable = false
                    _binding!!.btnVerify.isSelected = false
                }
            } else {
                _binding!!.btnVerify.isClickable = false
                _binding!!.btnVerify.isSelected = false
            }
        }

        _binding!!.btnVerify.setOnClickListener {
            if (_binding!!.Amount.text.toString().toDouble() >= 10.00) {
                _binding!!.lessthanAmount.visibility = View.GONE
                _binding!!.MaxthanAmount.visibility = View.VISIBLE
                if (_binding!!.Amount.text.toString().toDouble() <= 10000.00) {

                    if (availablebalance.toDouble() + _binding!!.Amount.text.toString()
                            .toDouble() >= 10000
                    ) {
                        requireView().showShortSnack("Your Maximum wallet limit is ₹20000. T&C apply")
                    } else {
                        // Intializing Payment without wallet Amount
                        getSharedPreference().saveString(
                            "AddWalletAmount",
                            _binding!!.Amount.text.toString()
                        )
                        ContainerActivity.openContainer(
                            requireContext(),
                            "AddMoneyToWalletWithPaytm",
                            ""
                        )
                        activity?.finish()
                        requireView().showShortSnack("Allow To Update Your Wallet")
                    }
                } else {
                    requireView().showShortSnack("Amount Should be greater than 10 or less than 10000 ")
                }
            } else {
                _binding!!.lessthanAmount.visibility = View.VISIBLE
                _binding!!.MaxthanAmount.visibility = View.GONE
                requireView().showShortSnack("Amount Should be greater than 10 or less than 10000 ")
            }
        }
        _binding!!.Amount.addTextChangedListener {
            _binding!!.cashbacklayout.visibility = View.GONE
            if (_binding!!.Amount.text.isNullOrEmpty()) {
                _binding!!.outlinedTextField.boxStrokeColor = Color.RED
                _binding!!.outlinedTextField.hintTextColor = ColorStateList.valueOf(Color.RED)
                _binding!!.outlinedTextField.hint = "Enter Amount Here"
                totalAmount = "0"
                Addmoneysuggetion()
            } else {

                if (_binding!!.Amount.text.toString().toDouble() <= 10000) {
                    if (_binding!!.Amount.text.toString().toDouble() >= 10.00) {
                        _binding!!.MaxthanAmount.setTextColor(Color.parseColor("#008D3A"))
                        _binding!!.outlinedTextField.boxStrokeColor = Color.parseColor("#008D3A")
                        _binding!!.outlinedTextField.hint = "Enter Amount Here"
                        _binding!!.outlinedTextField.hintTextColor =
                            ColorStateList.valueOf(Color.parseColor("#008D3A"))
                        _binding!!.btnVerify.isClickable = true
                        _binding!!.btnVerify.isSelected = true
                    } else {
                        _binding!!.MaxthanAmount.setTextColor(Color.RED)
                        _binding!!.outlinedTextField.boxStrokeColor = Color.RED
                        _binding!!.outlinedTextField.hintTextColor =
                            ColorStateList.valueOf(Color.RED)
                        _binding!!.outlinedTextField.hint = "Amount should be greater than 10"
                        _binding!!.btnVerify.isClickable = false
                        _binding!!.btnVerify.isSelected = false
                    }
                } else {
                    _binding!!.MaxthanAmount.setTextColor(Color.RED)
                    _binding!!.outlinedTextField.boxStrokeColor = Color.RED
                    _binding!!.outlinedTextField.hintTextColor = ColorStateList.valueOf(Color.RED)
                    _binding!!.outlinedTextField.hint = "Amount Should be less than 10000"
                    _binding!!.btnVerify.isClickable = false
                    _binding!!.btnVerify.isSelected = false
                }
            }
        }
        _binding!!.back.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun Addmoneysuggetion() {
        val detailsAdapter = CustomeAddMoneySuggetionAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                _binding!!.Amount.setText(data.toString())

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