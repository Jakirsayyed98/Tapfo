package app.tapho.ui.scanner.ScanCart

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RoomDB.getDatabase
import app.tapho.databinding.FragmentTapMartCheckOutBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.scanner.adapter.TapfoCartAdapter3
import app.tapho.ui.scanner.model.PlaceOrder.Data
import app.tapho.ui.scanner.model.PlaceOrder.ScanPlaceOrderRes
import app.tapho.ui.scanner.model.SearchCurrentOrder.Item
import app.tapho.ui.scanner.model.SearchCurrentOrder.SearchBusinessOrdersRes
import app.tapho.utils.DATA
import app.tapho.utils.setBusinessQR
import app.tapho.utils.withSuffixAmount
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TapMartCheckOutFragment : BaseFragment<FragmentTapMartCheckOutBinding>() {
    val millisUntilFinished = 1800000
    lateinit var coundown: CountDownTimer
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
        statusBarColor(R.color.GreenWalletBackgroundDark)
        statusBarTextBlack()
        _binding!!.MainLayout.visibility = View.GONE
        _binding!!.Progress.visibility = View.VISIBLE
        setTextData()
        _binding!!.backbtn.setOnClickListener {
            coundown.cancel()
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
        viewModel.getsearchBusinessOrders(getUserId(),it.code,this,object :ApiListener<SearchBusinessOrdersRes,Any?>{
            override fun onSuccess(t: SearchBusinessOrdersRes?, mess: String?) {
                _binding!!.MainLayout.visibility = View.VISIBLE
                _binding!!.Progress.visibility = View.GONE
                setData(t!!.data.get(0))
            }
        })
    }



    private fun setData(data: app.tapho.ui.scanner.model.SearchCurrentOrder.Data) {
      data.let {
          Glide.with(requireContext()).load(setBusinessQR(it.qr_code)).into(_binding!!.qrcode)
          _binding!!.paybleAmount.text = withSuffixAmount(it.total_amount.toString())!!.dropLast(3)
          _binding!!.paybleAmount1.text = withSuffixAmount(it.total_amount.toString())!!.dropLast(3)

          _binding!!.qrcodedata.text ="CHECKOUT CODE : "+ it.code
          val count = it.items.size

          _binding!!.cartcount.text =  if (count<=1) "Cart summary : "+count+" item" else "Cart summary : "+count+" items"

          setLayout(it.items)
          callVmData(it)
          setTimer()
      }
    }

    private fun setTimer() {
        coundown = object : CountDownTimer(millisUntilFinished.toLong(), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                _binding!!.timercount.text = minute.toString()+" : "+seconds.toString()
            }
            override fun onFinish() {

            }

        }.start()
    }

    private fun callVmData(data: app.tapho.ui.scanner.model.SearchCurrentOrder.Data) {
        viewModel.getsearchBusinessOrders(getUserId(),data.code,this,object :ApiListener<SearchBusinessOrdersRes,Any?>{
            override fun onSuccess(t: SearchBusinessOrdersRes?, mess: String?) {
                t!!.data.get(0).let {
                    when(it.status){
                        "0"->{
                            InPendingMethod(it)
                        }
                        else->{
                            GlobalScope.launch {
                                getDatabase(requireContext()).appDao().DeleteAllProduct()
                            }
                            coundown.cancel()
                            ContainerForProductActivity.openContainer(requireContext(),"TapMartStatusFragment",it,false,"")
                            activity?.finish()
                        }
                    }
                }
            }
        })
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
                                    callVmData(it)
                                }
                            }
                        }, 1000)
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