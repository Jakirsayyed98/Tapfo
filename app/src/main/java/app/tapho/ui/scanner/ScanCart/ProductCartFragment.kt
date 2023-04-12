package app.tapho.ui.scanner.ScanCart

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import app.tapho.utils.*
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
        _binding = FragmentProductCartBinding.inflate(inflater, container, false)
        statusBarColor(R.color.GreenWalletBackgroundDark)
        statusBarTextBlack()

        _binding!!.addMore.setOnClickListener {
            val intent = Intent(requireContext(), BarcodeScannerForProductActivity::class.java)
            startActivityForResult(intent, mRequestCode)
        }
        _binding!!.backbtn.setOnClickListener {
            OpenExitBottomSheet()
        }

        backpressedbtn()

        binding.PaymentModes.setOnClickListener {
            ContainerForProductActivity.openContainer(
                requireContext(),
                "SelectPaymentmodeFragment",
                "",
                false,
                ""
            )
            activity?.finish()
        }

        setTextData()

        _binding!!.clear.setOnClickListener {
            OpenClearCartBottomSheet()

        }


        SaveToCart()

        return _binding?.root
    }

    private fun backpressedbtn() {
        val OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                OpenExitBottomSheet()
            }
        }
        requireActivity().getOnBackPressedDispatcher()
            .addCallback(requireActivity(), OnBackPressedCallback)
    }

    private fun OpenExitBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.store_exit_bottomsheet, null)

        dialog.setCancelable(true)
        val exit: AppCompatButton = view.findViewById(R.id.exit)

        val continuebtn: AppCompatButton = view.findViewById(R.id.continuebtn)

        exit.setOnClickListener {
            GlobalScope.launch {
                getDatabase(requireContext()).appDao().DeleteAllProduct()
            }
            activity?.finish()
            dialog.dismiss()
        }


        continuebtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun OpenClearCartBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.store_clearcart_bottomsheet, null)

        dialog.setCancelable(true)
        val exit: AppCompatButton = view.findViewById(R.id.exit)

        val continuebtn: AppCompatButton = view.findViewById(R.id.continuebtn)

        exit.setOnClickListener {
            GlobalScope.launch {
                getDatabase(requireContext()).appDao().DeleteAllProduct()
            }
            dialog.dismiss()
        }


        continuebtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()


    }

    private fun setTextData() {
        getSharedPreference().getBusinessData().let {
            _binding!!.apply {
                storename.text = it!!.business_name
                storeaddress.text = it.area
                cartid.text = "CART ID: " + getSharedPreference().getString(CART_ID).toString()
                time.text = SimpleDateFormat(
                    "dd-MMM-yyyy",
                    Locale.ENGLISH
                ).format(Calendar.getInstance().time)
            }
        }
    }


    private fun SaveToCart() {
        getDatabase(requireContext()).appDao().getAllProductSet().observe(viewLifecycleOwner) {
            _binding!!.cartEmpty.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
            _binding!!.clear.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
            _binding!!.PaymentModes.isSelected = if (it.isNullOrEmpty()) false else true
            _binding!!.PaymentModes.isClickable = if (it.isNullOrEmpty()) false else true

            setLayout(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setLayout(it: List<Cart>) {
        var Amount = 0.0
        var MRP = 0.0
        var disprice = 0.0


        _binding!!.cartCount.text =
            if (it.size == 1) it.size.toString() + " Item" else it.size.toString() + " Items"

        it.forEach {
            Amount += it.totalPrice
        }

        it.forEach {
            MRP += (it.qty.toDouble() * it.mrp.toDouble())
        }
        it.forEach {
            disprice += (it.qty.toDouble() * it.price.toDouble())
        }

        val Amt = MRP - disprice

        _binding!!.savingAmount.text = withSuffixAmount(Amt.toString())!!.dropLast(3)
        _binding!!.savinglayout.visibility = if (Amt > 1) View.VISIBLE else View.GONE

        _binding!!.totalCartValue.text = withSuffixAmount(Amount.toString())!!.dropLast(3)


        val tapfoCartAdapter = TapfoCartAdapter<Cart>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })

        _binding!!.rrvData.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = tapfoCartAdapter
        }

        tapfoCartAdapter.addAllItem(it)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mRequestCode) {
                if (data != null) {
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
                    if (it) {
                        GlobalScope.launch {
                            getDatabase(requireContext()).appDao().searchProduct(textData).let {
                                requireActivity().runOnUiThread(Runnable {
                                    if (it.qty.toInt() >= 1) {
                                        OpenCartBottomSheet(it)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Product is not available",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                })
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread(Runnable {
                            Toast.makeText(
                                requireContext(),
                                "Product Not Fond " + textData,
                                Toast.LENGTH_SHORT
                            ).show()
                        })

                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun OpenCartBottomSheet(it: Data) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.tapfomart_cart_layout, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()
        val image = view.findViewById<ImageView>(R.id.image)
        val name = view.findViewById<TextView>(R.id.name)
        val eanNumber = view.findViewById<TextView>(R.id.eanNumber)
        val price = view.findViewById<TextView>(R.id.price)
        val disprice = view.findViewById<TextView>(R.id.disprice)
        val QtyData = view.findViewById<TextView>(R.id.QtyData)
        val savepercent = view.findViewById<TextView>(R.id.savepercent)
        val add = view.findViewById<ImageView>(R.id.add)
        val less = view.findViewById<ImageView>(R.id.less)
        val addToCheckOut = view.findViewById<AppCompatButton>(R.id.addToCheckOut)


        savepercent.text =
            roundOffAmount((((it.mrp.toDouble() - it.price!!.toDouble()) / it.mrp.toDouble()) * 100).toString()).dropLast(
                3
            ) + "% OFF"

        Glide.with(this).load(it.business_item.image).placeholder(R.drawable.loding_app_icon).into(image)
        name.text = it.name
        eanNumber.text = "EAN : " + it.ean
        price.text = withSuffixAmount(it.mrp)!!.dropLast(3)
        disprice.text = withSuffixAmount(it.price)!!.dropLast(3)
        var qtyd = 1
        GlobalScope.launch {
            val Product = getDatabase(requireContext()).appDao().ProductByBarcodeISExist(it.ean)
            if (Product) {
                GlobalScope.launch {
                    getDatabase(requireContext()).appDao().getProductByBarcode(it.ean).let {
                        QtyData.text = it.qty.toString()
                        qtyd = it.qty
                    }
                }
            } else {
                qtyd = 1
            }
        }


        QtyData.text = qtyd.toString()
        add.setOnClickListener { click ->
            if (qtyd < it.qty.toInt()) {
                qtyd += 1
                QtyData.text = qtyd.toString()
            }

        }

        less.setOnClickListener {
            if (qtyd > 1) {
                qtyd -= 1
                QtyData.text = qtyd.toString()
            }

        }

        addToCheckOut.setOnClickListener { click ->
            addToCart(qtyd, it)
            dialog.dismiss()
        }

    }

    private fun addToCart(qtyd: Int, data: Data) {
        data.let {
            GlobalScope.launch {
                val Product = getDatabase(requireContext()).appDao().ProductByBarcodeISExist(it.ean)
                if (Product) {
                    GlobalScope.launch {
                        getDatabase(requireContext()).appDao().getProductByBarcode(it.ean).let {
                            GlobalScope.launch {
                                getDatabase(requireContext()).appDao()
                                    .UpdateProductToCart(qtyd, it.ean, (qtyd * it.price.toDouble()))
                            }
                        }
                    }
                } else {
                    GlobalScope.launch {
                        getDatabase(requireContext()).appDao().AddPRoductToCart(
                            Cart(
                                it.id,
                                it.business_id,
                                it.business_user_category_id,
                                it.business_user_sub_category_id,
                                it.created_at,
                                it.description,
                                it.ean,
                                it.food_type!!,
                                it.business_item.image,
                                it.mrp,
                                it.name,
                                it.price!!,
                                it.qty,
                                it.status,
                                it.user_id,
                                qtyd,
                                (qtyd * it.price.toDouble()),
                                (qtyd * it.mrp.toDouble())
                            )
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