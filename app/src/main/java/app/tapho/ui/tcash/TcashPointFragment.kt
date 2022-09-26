package app.tapho.ui.tcash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentTcashPointBinding
import app.tapho.ui.BaseFragment


class TcashPointFragment : BaseFragment<FragmentTcashPointBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tcashpointbinding = FragmentTcashPointBinding.inflate(inflater, container, false)
        statusBarColor(R.color.orange1)
        // Inflate the layout for this fragment
        return tcashpointbinding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TcashPointFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}