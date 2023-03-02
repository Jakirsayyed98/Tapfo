package app.tapho.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentBuyGiftCardBinding
import app.tapho.ui.BaseFragment


class BuyGiftCardFragment : BaseFragment<FragmentBuyGiftCardBinding>(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buygiftcardbinding = FragmentBuyGiftCardBinding.inflate(inflater, container, false)
        return buygiftcardbinding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BuyGiftCardFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}