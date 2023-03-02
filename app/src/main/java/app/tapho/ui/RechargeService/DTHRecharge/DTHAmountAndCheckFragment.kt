package app.tapho.ui.RechargeService.DTHRecharge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentDTHAmountAndCheckBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.AddMoneyPopup.Adapter.CustomeAddMoneySuggetionAdapter
import app.tapho.ui.tcash.AddMoneyPopup.Model.CustomeAddMoneyModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class DTHAmountAndCheckFragment : BaseFragment<FragmentDTHAmountAndCheckBinding>() {
    var ServiceTypeID = ""
    var Name = ""
    var icon = ""
    var CustomerIDType = ""
    var discription = ""
    var AmountRange = ""
    var CustomerID = ""
    var totalAmount = 0.0
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
        _binding = FragmentDTHAmountAndCheckBinding.inflate(inflater, container, false)
        statusBarTextBlack()
        statusBarColor(R.color.black)

        ServiceTypeID = getSharedPreference().getString("servicetype").toString()
        Name = getSharedPreference().getString("operator_nameDTH").toString()
        icon = getSharedPreference().getString("operator_iconDTH").toString()
        CustomerIDType = getSharedPreference().getString("account_displayDTH").toString()
        discription = getSharedPreference().getString("discriptionDTH").toString()
        AmountRange = getSharedPreference().getString("amount_rangeDTH").toString()
        CustomerID = getSharedPreference().getString("numberDTH").toString()

        Addmoneysuggetion()
        setAllText()

        return _binding?.root
    }

    private fun setAllText() {
        _binding!!.name.text =  Name
        _binding!!.clientID.text = CustomerID
        Glide.with(requireContext()).load(icon).circleCrop().into(_binding!!.icon)
        _binding!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }


        val startRange = AmountRange.replaceAfter("-", "")
        val endRange = AmountRange.removeRange(0, 4)
        _binding!!.btnVerify.setOnClickListener {
            if (_binding!!.Amount.text.toString().isNullOrEmpty().not()) {
                if (_binding!!.Amount.text.toString().toDouble() >= startRange.replace("-", "").toString().toDouble()) {
                    if (_binding!!.Amount.text.toString().toDouble() < endRange.replace("-", "").toString().toDouble()) {
                        getSharedPreference().saveString("Amount", _binding!!.Amount.text.toString())
                        ContainerActivity.openContainerForDTHRecharge(requireContext(), "DTHRechargeFinalScreenFragment")

                    } else {

                        Snackbar.make(requireView(), "Please enter amount within $startRange to $endRange ", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(requireView(), "Please enter amount within $startRange to $endRange ", Snackbar.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun Addmoneysuggetion() {
        val detailsAdapter = CustomeAddMoneySuggetionAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                when (data) {
                    "500" -> {
                        _binding!!.Amount.text!!.clear()
//                        totalAmount = totalAmount + data.toString().toDouble()
//                        _binding!!.Amount.setText(totalAmount.toString())
                        _binding!!.Amount.setText("500")

                    }
                    "1000" -> {
                        _binding!!.Amount.text!!.clear()
//                        totalAmount = totalAmount + data.toString().toDouble()
//                        _binding!!.Amount.setText(totalAmount.toString())
                        _binding!!.Amount.setText("1000")

                    }
                    "1500" -> {
                        _binding!!.Amount.text!!.clear()
//                        totalAmount = totalAmount + data.toString().toDouble()
//                        _binding!!.Amount.setText(totalAmount.toString())
                        _binding!!.Amount.setText("1500")
                    }
                    "3000" -> {
                        _binding!!.Amount.text!!.clear()
 //                        totalAmount = totalAmount + data.toString().toDouble()
//                        _binding!!.Amount.setText(totalAmount.toString())
                        _binding!!.Amount.setText("3000")

                    }

                }
            }

        }).apply {

            addItem(CustomeAddMoneyModel(500, "500", false))
            addItem(CustomeAddMoneyModel(1000, "1000", false))
            addItem(CustomeAddMoneyModel(1500, "1500", false))
            addItem(CustomeAddMoneyModel(3000, "3000", false))
        }
        _binding!!.AmountRV.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = detailsAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DTHAmountAndCheckFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}