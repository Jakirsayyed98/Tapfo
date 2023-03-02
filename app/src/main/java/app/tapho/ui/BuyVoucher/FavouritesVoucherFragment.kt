package app.tapho.ui.BuyVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentFavouritesVoucherBinding
import app.tapho.ui.BaseFragment


class FavouritesVoucherFragment : BaseFragment<FragmentFavouritesVoucherBinding>() {

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
        _binding = FragmentFavouritesVoucherBinding.inflate(inflater,container,false)
        statusBarColor(R.color.white)
        statusBarTextWhite()

        return _binding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            FavouritesVoucherFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}