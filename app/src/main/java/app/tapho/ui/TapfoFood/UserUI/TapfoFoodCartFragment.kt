package app.tapho.ui.TapfoFood.UserUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentTapfoFoodCartBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.TapfoFood.UserUI.Adapter.AllAddressListAdapter
import app.tapho.ui.TapfoFood.UserUI.Adapter.AllItemofCartAdapter
import app.tapho.ui.TapfoFood.UserUI.model.FoodCustomeSuperCategoryModel
import app.tapho.ui.TapfoFood.UserUI.model.addresslistModel
import app.tapho.utils.DATA
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson


class TapfoFoodCartFragment : BaseFragment<FragmentTapfoFoodCartBinding>(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTapfoFoodCartBinding.inflate(inflater,container,false)

        allClickTovisibleData()
        getStoreData()
        setCartData()

        return _binding!!.root
    }

    private fun allClickTovisibleData() {
        _binding!!.backbtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        _binding!!.addmoreitems.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        _binding!!.delivery.isSelected = true
        _binding!!.verifypickup.visibility = View.GONE
        _binding!!.verifydelivery.visibility = View.VISIBLE
        _binding!!.contactlessdelivery.visibility = View.VISIBLE
        _binding!!.contactlessdeliveryView.visibility = View.VISIBLE
        _binding!!.delivery.setOnClickListener {
            _binding!!.delivery.isSelected = true
            _binding!!.pickup.isSelected = false
            _binding!!.verifypickup.visibility = View.GONE
            _binding!!.verifydelivery.visibility = View.VISIBLE
            _binding!!.deliveryaddress.visibility = View.VISIBLE
            _binding!!.contactlessdelivery.visibility = View.VISIBLE
            _binding!!.contactlessdeliveryView.visibility = View.VISIBLE
        }
        _binding!!.pickup.setOnClickListener {
            _binding!!.pickup.isSelected = true
            _binding!!.delivery.isSelected = false
            _binding!!.verifypickup.visibility = View.VISIBLE
            _binding!!.verifydelivery.visibility = View.GONE
            _binding!!.deliveryaddress.visibility = View.GONE
            _binding!!.contactlessdelivery.visibility = View.GONE
            _binding!!.contactlessdeliveryView.visibility = View.GONE
        }


        _binding!!.arrow.setOnClickListener {
            if (_binding!!.visiblelayout.isVisible) {
                _binding!!.arrow.setImageResource(R.drawable.arrow_up)
                _binding!!.visiblelayout.visibility = View.GONE
            } else {
                _binding!!.arrow.setImageResource(R.drawable.arrow_down)
                _binding!!.visiblelayout.visibility = View.VISIBLE
            }
        }
        _binding!!.summary.setOnClickListener {
            if (_binding!!.visiblelayout.isVisible) {
                _binding!!.arrow.setImageResource(R.drawable.arrow_up)
                _binding!!.visiblelayout.visibility = View.GONE
            } else {
                _binding!!.arrow.setImageResource(R.drawable.arrow_down)
                _binding!!.visiblelayout.visibility = View.VISIBLE
            }
        }

        _binding!!.placeOrder.setOnClickListener {
            TapfoFoodContainerActivity.openContainer(requireContext(),"OrderPlacedPreviewFragment","123456789")
        }

        _binding!!.addAddress.setOnClickListener {
            openPopupForDialog()
        }


    }

    private fun openPopupForDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.select_addressfor_tapfofood,null)
        val AddressRV : RecyclerView = view.findViewById(R.id.addresslist)
        val addnewAddress : TextView = view.findViewById(R.id.addnewAddress)

        addnewAddress.setOnClickListener {
            dialog.dismiss()
            openPopupForGEtAddress()
        }

        val AllAddressListAdapter = AllAddressListAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                dialog.dismiss()
            }
        }).apply {
                addItem(addresslistModel(1,"507 KP Aurum","Marol maroshi Road","Marol Naka", "Office"))
                addItem(addresslistModel(3,"96 5/9 Yamuna society ","Sagar nagar Park site","Vikhroli west", "Home"))
                addItem(addresslistModel(2,"705 Evrest Park ","Near sakinaka Metro Station","Sakinaka Naka", "PG"))
                addItem(addresslistModel(4,"97 behind Bata Store GB Road","Ghatkopar Metro Station","Ghatkopar", "GYM"))
        }
        AddressRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter =AllAddressListAdapter
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun openPopupForGEtAddress() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.getaddressdata,null)
        val addnewAddress : TextView = view.findViewById(R.id.myaddress)



        dialog.setContentView(view)
        dialog.show()
    }

    private fun setCartData() {
        val cartAdapter = AllItemofCartAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {



            }
        }).apply {
            val image1="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxHD5S5z5zGhjDKj-K95kmBpOUNFtpAVrp-4Yqd77v&s"
            val image2="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQF0LjrsgkVi79EIFUmXABwDTKKS0go6rn-S1xDIU7Tmo_UecYTu81-v6nfNHFrwbZ0YcA&usqp=CAU"
            val image3="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT7s_BYGWp21LC_mYwiW1FVCAKcqcPV-Rao73lj2izFwAuw80XzAOAPBTi2V7H-qnJgtbU&usqp=CAU"


            addItem(FoodCustomeSuperCategoryModel(image1,"Monster Pizza","1",""))
            addItem(FoodCustomeSuperCategoryModel(image2,"Cafe Coffe Day","1",""))
            addItem(FoodCustomeSuperCategoryModel(image3,"Capitol Cafe","1",""))

        }
        _binding!!.couponse.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = cartAdapter
        }
    }

    private fun getStoreData() {
        activity?.intent?.getStringExtra(DATA).let {
            val data = Gson().fromJson(it, FoodCustomeSuperCategoryModel::class.java)
            _binding!!.storename.text = data.name
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TapfoFoodCartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}