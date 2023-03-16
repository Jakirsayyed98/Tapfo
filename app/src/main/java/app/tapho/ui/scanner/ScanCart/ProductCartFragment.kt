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
import app.tapho.ui.scanner.model.Data
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductCartFragment : BaseFragment<FragmentProductCartBinding>() {

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
        _binding = FragmentProductCartBinding.inflate(inflater,container,false)
        statusBarColor(R.color.black)
        statusBarTextBlack()

        _binding!!.addMore.setOnClickListener {
            startActivity(Intent(requireContext(),BarcodeScannerForProductActivity::class.java))
            activity?.finish()
//            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.backbtn.setOnClickListener {
            startActivity(Intent(requireContext(),BarcodeScannerForProductActivity::class.java))
            activity?.finish()
//            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.clear.setOnClickListener {
            GlobalScope.launch {
                getDatabase(requireContext()).appDao().DeleteAllProduct()
            }
        }

//        var data = activity?.intent?.getStringExtra(DATA)
//        if (data.isNullOrEmpty().not()){
//            Gson().fromJson(data,ProductData::class.java).let {
                SaveToCart()
//                data =null
//            }
//        }
//        else{
//            SaveToCart(null)
//        }

        return _binding?.root
    }



    @OptIn(DelicateCoroutinesApi::class)
    private fun SaveToCart() {


        getDatabase(requireContext()).appDao().getAllProductSet().observe(viewLifecycleOwner){
            setLayout(it)
        }



//        GlobalScope.launch {
//            val Product = getDatabase(requireContext()).appDao().ProductByBarcodeISExist(ProductData!!.QrCode)
//            if (Product){
//                GlobalScope.launch {
//                    getDatabase(requireContext()).appDao().getProductByBarcode(ProductData.QrCode).let {
//                        GlobalScope.launch {
//                            getDatabase(requireContext()).appDao().UpdateProductToCart(it.Qty+1,it.QrCode)
//                        }
//                    }
//                }
//            }else{
//                GlobalScope.launch {
//                    getDatabase(requireContext()).appDao().AddPRoductToCart(ProductData)
//                }
//            }
//        }



    }

    private fun setLayout(it: List<Data>?) {
        val tapfoCartAdapter  = TapfoCartAdapter<Data>(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })

        _binding!!.rrvData.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = tapfoCartAdapter
        }

        tapfoCartAdapter.addAllItem(it!!)

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