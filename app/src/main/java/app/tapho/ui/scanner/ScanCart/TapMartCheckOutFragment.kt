package app.tapho.ui.scanner.ScanCart

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R

import app.tapho.databinding.FragmentTapMartCheckOutBinding
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.network.Status
import app.tapho.showShortSnack
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.intro.IntroActivity
import app.tapho.ui.login.VerifyOtpActivity
import app.tapho.ui.scanner.adapter.TapfoCartAdapter3
import app.tapho.ui.scanner.model.PlaceOrder.Data
import app.tapho.ui.scanner.model.PlaceOrder.ScanPlaceOrderRes
import app.tapho.ui.scanner.model.SearchCurrentOrder.Item
import app.tapho.utils.DATA
import app.tapho.utils.REACHED_HOME
import app.tapho.utils.setBusinessQR
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.util.*


class TapMartCheckOutFragment : BaseFragment<FragmentTapMartCheckOutBinding>() {

    private var myBitmap: Bitmap? = null

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
        _binding= FragmentTapMartCheckOutBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()
        _binding!!.MainLayout.visibility = View.GONE
        _binding!!.Progress.visibility = View.VISIBLE
        setTextData()
        _binding!!.backbtn.setOnClickListener {
           activity?.onBackPressedDispatcher?.onBackPressed()
        }

        val data = activity?.intent?.getStringExtra(DATA)
        if (!data.isNullOrEmpty()){
            Gson().fromJson(data,ScanPlaceOrderRes::class.java).let {
                it.data.let {
                    setLayoutData(it)
                }
            }
        }

        return _binding?.root
    }


    private fun setLayoutData(it: Data) {

        viewModel.getsearchBusinessOrders().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    _binding!!.MainLayout.visibility = View.VISIBLE
                    _binding!!.Progress.visibility = View.GONE
                    setData(it.data!!.data.get(0))
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        }

        callVmData(it.code)

    }

    private fun callVmData(code:String) {
        viewModel.getsearchBusinessOrders(getUserId(), code)
    }

    private fun setData(data: app.tapho.ui.scanner.model.SearchCurrentOrder.Data) {
      data.let {
          Glide.with(requireContext()).load(setBusinessQR(it.qr_code)).into(_binding!!.qrcode)
          _binding!!.paybleAmount.text = withSuffixAmount(it.total_amount.toString())
          _binding!!.cartcount.text = "Cart summary : "+it!!.items.size+" items"
          _binding!!.qrcodedata.text = it.code
          setLayout(it.items)

          when(it.status){
              "0"->{
                    InPendingMethod(it)
              }
              "1"->{
                  requireView().showShortSnack("Your Order Has been verified")
              }
              else->{
                  requireView().showShortSnack("Your Order Has been Rejected")
              }


          }

      }
    }

    private fun InPendingMethod(it: app.tapho.ui.scanner.model.SearchCurrentOrder.Data) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable{
            override fun run() {
                synchronized(this@TapMartCheckOutFragment) {
                    kotlin.runCatching {
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                kotlin.runCatching {
                                    callVmData(it.code)
                                }
                            }
                        }, 10000)
                    }
                }
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }


    private fun setTextData() {
        getSharedPreference().getBusinessData().let {
            _binding!!.apply {
                storename.text = it!!.business_name
                storeaddress.text = it.address
            }
        }
    }


    private fun setLayout(it: List<Item>?) {
        val tapfoCartAdapter  = TapfoCartAdapter3<Item>(object : RecyclerClickListener {
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
            TapMartCheckOutFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}