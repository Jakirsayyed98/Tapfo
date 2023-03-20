package app.tapho.ui.scanner.ScanCart

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import app.tapho.ui.scanner.adapter.TapfoCartAdapter
import app.tapho.ui.scanner.adapter.TapfoCartAdapter2
import app.tapho.ui.scanner.adapter.TapfoPaymentModeAdapter
import app.tapho.ui.scanner.model.Data
import app.tapho.ui.scanner.model.customePaymentMode
import app.tapho.utils.withSuffixAmount


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

    private fun getcartItems() {
        getDatabase(requireContext()).appDao().getAllProductSet().observe(viewLifecycleOwner){
            setLayout(it)

        }
    }

    private fun setLayout(it: List<Data>?) {
        var total = 0.0
        it!!.forEach {
            for (i  in 1..it.buyingQty.toInt()){
                total+=it.sale_price.toDouble()
            }
        }
        _binding!!.paybleAmount.text = withSuffixAmount(total.toString())
        val tapfoCartAdapter  = TapfoCartAdapter2<Data>(object : RecyclerClickListener{
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