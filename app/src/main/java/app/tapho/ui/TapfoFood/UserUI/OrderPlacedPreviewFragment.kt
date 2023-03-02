package app.tapho.ui.TapfoFood.UserUI

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentOrderPlacedPreviewBinding
import app.tapho.showLong
import app.tapho.ui.BaseFragment
import app.tapho.utils.customToast

class OrderPlacedPreviewFragment : BaseFragment<FragmentOrderPlacedPreviewBinding>() {

    val handler = Handler(Looper.getMainLooper())

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
        _binding = FragmentOrderPlacedPreviewBinding.inflate(inflater,container,false)

        val runable = object : Runnable{
            override fun run() {
               handler.postDelayed(object : Runnable{
                   override fun run() {
                       requireContext().customToast("Order Placed SucessFully",false)
                   }
               },5000)
            }

        }
        val thread = Thread(runable)
        thread.start()

        return _binding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrderPlacedPreviewFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}