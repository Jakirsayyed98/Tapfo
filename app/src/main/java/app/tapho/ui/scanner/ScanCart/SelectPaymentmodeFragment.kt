package app.tapho.ui.scanner.ScanCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.FragmentSelectPaymentmodeBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.scanner.adapter.TapfoCartAdapter2
import app.tapho.ui.scanner.adapter.TapfoPaymentModeAdapter
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart
import app.tapho.ui.scanner.model.customePaymentMode
import app.tapho.utils.CART_ID
import app.tapho.utils.getCartIdRandom
import app.tapho.utils.withSuffixAmount
import java.text.SimpleDateFormat
import java.util.*


class SelectPaymentmodeFragment : BaseFragment<FragmentSelectPaymentmodeBinding>() {


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
        _binding = FragmentSelectPaymentmodeBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        getcartItems()
        setPaymentModeLayout()
        setTextData()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.viewall.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.PaymentModes.setOnClickListener {
            ContainerForProductActivity.openContainer(requireContext(),"TapMartCheckOutFragment","",false,"")
        }

        return _binding?.root
    }

    private fun setTextData() {
        getSharedPreference().getBusinessData().let {
            _binding!!.apply {
                storename.text = it!!.business_name
                storeaddress.text = it.address
                cartid.text = "CART ID: "+ getSharedPreference().getString(CART_ID).toString()
                time.text = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
            }
        }
    }


    private fun getcartItems() {
        getDatabase(requireContext()).appDao().getAllProductSet().observe(viewLifecycleOwner){
            setLayout(it)

        }
    }

    private fun setLayout(it: List<Cart>?) {
        var total = 0.0
        it!!.forEach {
            for (i  in 1..it.qty.toInt()){
                total+=it.price.toDouble()
            }
        }
        _binding!!.paybleAmount.text = withSuffixAmount(total.toString())
        val tapfoCartAdapter  = TapfoCartAdapter2<Cart>(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })

        _binding!!.rrvData.apply {
            layoutManager =GridLayoutManager(requireContext(),4)
            adapter = tapfoCartAdapter
        }

        tapfoCartAdapter.addAllItem(if (it.size>=4) it.subList(0,4) else it)

    }

    private fun setPaymentModeLayout() {
        val paymentModeadapter  = TapfoPaymentModeAdapter<customePaymentMode>(object :RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is customePaymentMode){
                    _binding!!.PaymentModes.visibility = View.VISIBLE
                    _binding!!.PaymentModesText.text = "Proceed to "+data.name
                }
            }
        }).apply {
            addItem(customePaymentMode("1","Pay at Counter ","Skip the line & pay at bill counter","","2",false))
            addItem(customePaymentMode("2","Online Payment","Pay Via UPI, Netbanking, Card or more","","2",false))
        }

        _binding!!.paymentmodes.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = paymentModeadapter
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SelectPaymentmodeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}