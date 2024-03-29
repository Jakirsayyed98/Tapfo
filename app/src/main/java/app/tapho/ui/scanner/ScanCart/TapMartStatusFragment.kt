package app.tapho.ui.scanner.ScanCart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentTapMartStatusBinding
import app.tapho.ui.BaseFragment
import app.tapho.ui.scanner.model.SearchCurrentOrder.Data
import app.tapho.utils.DATA
import com.bumptech.glide.Glide
import com.google.gson.Gson


class TapMartStatusFragment : BaseFragment<FragmentTapMartStatusBinding>() {

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
        _binding = FragmentTapMartStatusBinding.inflate(inflater, container, false)

        val data = activity?.intent?.getStringExtra(DATA)
        if (!data.isNullOrEmpty()) {
            Gson().fromJson(data, Data::class.java).let {
                startForNew(it)
                when (it.status) {
                    "0" -> { //Pending
                        Glide.with(requireContext()).asGif().load(R.drawable.tapcart_success)
                            .into(_binding!!.status)
                        _binding!!.orderstatus.text = "Order in pending"
                    }
                    "1" -> {  //Paid
                        Glide.with(requireContext()).asGif().load(R.drawable.tapcart_success)
                            .into(_binding!!.status)
                        _binding!!.orderstatus.text = "Order Confirmed"
                    }
                    else -> {  //Cancelled
                        Glide.with(requireContext()).asGif().load(R.drawable.rejectedok)
                            .into(_binding!!.status)
                        _binding!!.orderstatus.text = "Order Cancelled by Store"
                    }
                }
            }
        }

        return _binding?.root
    }


    private fun startForNew(data: Data) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                synchronized(this@TapMartStatusFragment) {
                    kotlin.runCatching {
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                kotlin.runCatching {
                                    ContainerForProductActivity.openContainer(
                                        requireContext(),
                                        "TapMartOrderConformationFragment",
                                        data,
                                        false,
                                        ""
                                    )
                                    activity?.finish()
                                }
                            }
                        }, 2000)
                    }
                }
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            TapMartStatusFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}