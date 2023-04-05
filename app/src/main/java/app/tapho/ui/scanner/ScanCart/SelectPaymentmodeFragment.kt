package app.tapho.ui.scanner.ScanCart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.FragmentSelectPaymentmodeBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.scanner.adapter.TapfoCartAdapter2
import app.tapho.ui.scanner.adapter.TapfoPaymentModeAdapter
import app.tapho.ui.scanner.model.CartData.Cart
import app.tapho.ui.scanner.model.PlaceOrder.ScanPlaceOrderRes
import app.tapho.ui.scanner.model.customePaymentMode
import app.tapho.utils.CART_ID
import app.tapho.utils.withSuffixAmount
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class SelectPaymentmodeFragment : BaseFragment<FragmentSelectPaymentmodeBinding>() {

    val Orderitems = JSONArray()
    var TotalAmount = 0.0
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
        backpressedbtn()
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        var obj: JSONObject? = null


        getDatabase(requireContext()).appDao().getAllProductSet().observe(viewLifecycleOwner){
            it.forEach {
                it.let {
                    TotalAmount += it.totalPrice.toDouble()
                    obj = JSONObject()
                    try {
                        obj!!.put("business_user_item_id",it.id)
                        obj!!.put("qty",it.qty)
                        obj!!.put("price",if (it.price.isNullOrEmpty()) it.mrp else it.price)
                        obj!!.put("total_price",it.totalPrice)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Orderitems.put(obj)
                }


            }
        }

        _binding!!.viewall.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.PaymentModes.setOnClickListener {
            OpenExitBottomSheet()
        }

        return _binding?.root
    }

    private fun backpressedbtn() {
        val  OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                ContainerForProductActivity.openContainer(requireContext(),"ProductCartFragment","",false,"")
                activity?.finish()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), OnBackPressedCallback)
    }

    private fun PlaceOrder() {
        viewModel.businessPlaceOrder(getUserId(),getSharedPreference().getBusinessData()!!.id,TotalAmount.toString(),Orderitems.toString(),this,object : ApiListener<ScanPlaceOrderRes,Any?>{
            override fun onSuccess(t: ScanPlaceOrderRes?, mess: String?) {
                t!!.let {
                    ContainerForProductActivity.openContainer(requireContext(),"TapMartCheckOutFragment",it,false,"")
                    activity?.finish()
                }
            }

            override fun onError(mess: String?) {
                super.onError(mess)

            }
        })

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
        _binding!!.paybleAmount.text = withSuffixAmount(total.toString())!!.dropLast(3)
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
            addItem(customePaymentMode("2","Online Payment","Pay Via UPI, Netbanking, Card or more","","1",false))
        }

        _binding!!.paymentmodes.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = paymentModeadapter
        }

    }


    private fun OpenExitBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.store_checkout_bottomsheet, null)

        dialog.setCancelable(true)
        val exit: TextView = view.findViewById(R.id.exit)

        val continuebtn: AppCompatButton = view.findViewById(R.id.continuebtn)

        exit.setOnClickListener {
            ContainerForProductActivity.openContainer(requireContext(),"ProductCartFragment","",false,"")
            activity?.finish()
            dialog.dismiss()
        }


        continuebtn.setOnClickListener {
            PlaceOrder()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()


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