package app.tapho.ui.tcash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.databinding.FragmentWalletTabBinding
import app.tapho.ui.BaseFragment

class WalletTabFragment : BaseFragment<FragmentWalletTabBinding>() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletTabBinding.inflate(inflater,container,false)


        return _binding?.root
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            WalletTabFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}