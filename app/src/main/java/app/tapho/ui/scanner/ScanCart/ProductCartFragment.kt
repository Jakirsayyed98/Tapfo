package app.tapho.ui.scanner.ScanCart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentProductCartBinding
import app.tapho.ui.BaseFragment
import app.tapho.RoomDB.getDatabase
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.scanner.adapter.TapfoCartAdapter
import app.tapho.ui.scanner.model.AllProducts.Data
import app.tapho.ui.scanner.model.CartData.Cart
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.utils.CART_ID
import app.tapho.utils.getCartIdRandom
import app.tapho.utils.getUniqueCode
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProductCartFragment : BaseFragment<FragmentProductCartBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductCartBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        _binding!!.addMore.setOnClickListener {
            startActivity(Intent(requireContext(),BarcodeScannerForProductActivity::class.java))
            activity?.finish()
        }
        _binding!!.backbtn.setOnClickListener {
            startActivity(Intent(requireContext(),BarcodeScannerForProductActivity::class.java))
            activity?.finish()
        }

        binding.PaymentModes.setOnClickListener {
            ContainerForProductActivity.openContainer(requireContext(),"SelectPaymentmodeFragment","",false,"")
        }

        setTextData()

        _binding!!.clear.setOnClickListener {
            GlobalScope.launch {
                getDatabase(requireContext()).appDao().DeleteAllProduct()
            }
        }

                SaveToCart()

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


    private fun SaveToCart() {
        getDatabase(requireContext()).appDao().getAllProductSet().observe(viewLifecycleOwner){
            setLayout(it)
        }
    }

    private fun setLayout(it: List<Cart>) {
        val tapfoCartAdapter  = TapfoCartAdapter<Cart>(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })

        _binding!!.rrvData.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = tapfoCartAdapter
        }

        tapfoCartAdapter.addAllItem(it)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ProductCartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}