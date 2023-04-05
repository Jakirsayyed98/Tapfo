package app.tapho.ui.scanner.ScanCart

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentTapMartOrderConformationBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.scanner.adapter.TapfoCartAdapter3
import app.tapho.ui.scanner.model.BusinessDetail.searchBusinessRes
import app.tapho.ui.scanner.model.SearchCurrentOrder.Data
import app.tapho.ui.scanner.model.SearchCurrentOrder.Item
import app.tapho.utils.DATA
import app.tapho.utils.parseDate
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.gson.Gson

class TapMartOrderConformationFragment : BaseFragment<FragmentTapMartOrderConformationBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTapMartOrderConformationBinding.inflate(inflater,container,false)
        val data = activity?.intent?.getStringExtra(DATA)
        if (!data.isNullOrEmpty()){
            Gson().fromJson(data, Data::class.java).let {
                _binding!!.billID.text = it.code
                _binding!!.Date.text = parseDate(it.created_at)

                setData(it)
                setLayout(it.items)
                getStoreDetail(it)
            }
        }

        _binding!!.done.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        return _binding?.root

    }

    private fun getStoreDetail(data: Data?) {
        var totalCount = 0
        data!!.items.forEach {
            totalCount+=it.qty.toInt()
        }
        viewModel.searchBusiness(getUserId(), data.business_id,this,object : ApiListener<searchBusinessRes,Any?>{
            override fun onSuccess(t: searchBusinessRes?, mess: String?) {
             t!!.let {
                 it.data.get(0).let {
                    _binding!!.apply {
                        nameTV.text = it.business_name
                        tankyouStorename.text = getString(R.string.thank_you_for_shopping_with_bon_bon_supermart,it.business_name)
                        yourbill.text = getString(R.string.your_bill_amount_1529_00_a, withSuffixAmount(data.total_amount)!!.dropLast(3),it.business_name,totalCount.toString())
                    }
                 }
             }
            }
        })
    }


    private fun setData(data: Data) {
        when(data.status){
            "0"->{ //Pending
                statusBarColor(R.color.orange)
                statusBarTextBlack()
                _binding!!.orderstatus.text = "Order in Pending"
                _binding!!.mainlayout.setBackgroundColor(Color.parseColor("#EE8300"))
            }
            "1"->{  //Paid
                statusBarColor(R.color.green_dark)
                statusBarTextBlack()
                _binding!!.orderstatus.text = "Order Confirmed"
                _binding!!.mainlayout.setBackgroundColor(Color.parseColor("#008D3A"))
            }
            else->{  //Cancelled
                statusBarColor(R.color.red)
                statusBarTextBlack()
                _binding!!.orderstatus.text = "Order Rejected"
                _binding!!.mainlayout.setBackgroundColor(Color.parseColor("#EF5350"))
            }
        }
    }


    private fun setLayout(it: List<Item>?) {
        var totalCount = 0
        it!!.forEach {
            totalCount+=it.qty.toInt()
        }

        _binding!!.cartcount.text = totalCount.toString()+" Items"
        val tapfoCartAdapter  = TapfoCartAdapter3<Item>(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })

        _binding!!.rrvData.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = tapfoCartAdapter
        }

        tapfoCartAdapter.addAllItem(it)

    }



    companion object {
        @JvmStatic
        fun newInstance() =
            TapMartOrderConformationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}