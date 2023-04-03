package app.tapho.ui.scanner.ScanCart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProductCartFragment : BaseFragment<FragmentProductCartBinding>() {

    private val mRequestCode = 100
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
            val intent = Intent(requireContext(), BarcodeScannerForProductActivity::class.java)
            startActivityForResult(intent, mRequestCode)
//            startActivity(Intent(requireContext(),BarcodeScannerForProductActivity::class.java))
//            activity?.finish()
        }
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.PaymentModes.setOnClickListener {
            ContainerForProductActivity.openContainer(requireContext(),"SelectPaymentmodeFragment","",false,"")
            activity?.finish()
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
        var Amount = 0.0
        it.forEach {
            Amount+=it.totalPrice
        }

        _binding!!.totalCartValue.text = withSuffixAmount(Amount.toString())


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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mRequestCode) {
                if (data != null){
                    val eanCode = data.getStringExtra("ENCodeData")
                    chechProductAvailableOrNot(eanCode)
                }

            }
        }
    }

    private fun chechProductAvailableOrNot(textData: String?) {
        if (textData != null) {
            GlobalScope.launch {
                getDatabase(requireContext()).appDao().ProductByEANisExist(textData).let {
                    if(it){
                        GlobalScope.launch {
                            getDatabase(requireContext()).appDao().searchProduct(textData).let {
                                requireActivity().runOnUiThread(Runnable {
                                    OpenCartBottomSheet(it)
                                })
                            }
                        }
                    }else{
                        requireActivity().runOnUiThread(Runnable {
                            Toast.makeText(requireContext(),"Product Not Fond "+textData,Toast.LENGTH_SHORT).show()
                        })

                    }
                }
            }
        }
    }


    private fun OpenCartBottomSheet(it: Data) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.tapfomart_cart_layout,null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        val image = view.findViewById<ImageView>(R.id.image)
        val name = view.findViewById<TextView>(R.id.name)
        val price = view.findViewById<TextView>(R.id.price)
        val disprice = view.findViewById<TextView>(R.id.disprice)
        val QtyData = view.findViewById<TextView>(R.id.QtyData)
        val add = view.findViewById<ImageView>(R.id.add)
        val less = view.findViewById<ImageView>(R.id.less)
        val addToCheckOut = view.findViewById<AppCompatButton>(R.id.addToCheckOut)

        Glide.with(this).load(it.image).placeholder(R.drawable.loading_progress).into(image)
        name.text = it.name
        price.text = withSuffixAmount(it.mrp)
        disprice.text = withSuffixAmount(it.price)
        var qtyd = 1
        GlobalScope.launch {
            val Product = getDatabase(requireContext()).appDao().ProductByBarcodeISExist(it.ean)
            if (Product){
                GlobalScope.launch {
                    getDatabase(requireContext()).appDao().getProductByBarcode(it.ean).let {
                        QtyData.text = it.qty.toString()
                        qtyd = it.qty
                    }
                }
            }else{
                qtyd =1
            }
        }


        QtyData.text = qtyd.toString()
        add.setOnClickListener{
            qtyd += 1
            QtyData.text = qtyd.toString()
        }

        less.setOnClickListener{
            qtyd -= 1
            QtyData.text = qtyd.toString()
        }

        addToCheckOut.setOnClickListener{click->
            addToCart(qtyd,it)
            dialog.dismiss()
        }

    }

    private fun addToCart(qtyd: Int, data : Data) {
        data.let {
            GlobalScope.launch {
                val Product = getDatabase(requireContext()).appDao().ProductByBarcodeISExist(it.ean)
                if (Product){
                    GlobalScope.launch {
                        getDatabase(requireContext()).appDao().getProductByBarcode(it.ean).let {
                            GlobalScope.launch {
                                getDatabase(requireContext()).appDao().UpdateProductToCart(qtyd,it.ean,(qtyd * it.price.toDouble()))
                            }
                        }
                    }
                }else{
                    GlobalScope.launch {
                        getDatabase(requireContext()).appDao().AddPRoductToCart(
                            Cart(it.id,it.business_id,it.business_user_category_id,it.business_user_sub_category_id,it.created_at,it.description,
                                it.ean,it.food_type,it.image,it.mrp,it.name,it.price,it.qty,it.status,it.user_id,qtyd,
                                (qtyd * it.price.toDouble()))
                        )
                    }
                }
            }
        }
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