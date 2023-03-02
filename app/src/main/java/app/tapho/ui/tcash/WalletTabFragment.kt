package app.tapho.ui.tcash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.RechargeServiceActivity
import app.tapho.databinding.FragmentWalletTabBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.BuyVoucher.VouchersActivity
import app.tapho.ui.ContainerActivity
import app.tapho.ui.TapfoFood.TapfoFoodContainerActivity
import app.tapho.ui.home.adapter.CustomeShopCategoryModel
import app.tapho.ui.home.adapter.CustomeSuperCategoryModel
import app.tapho.ui.localbizzUI.LocalBizSplashActivity
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.HomeRes
import app.tapho.ui.tcash.adapter.SuperLinkAdapterFotTcash
import app.tapho.ui.tcash.adapter.TcashBannerAdapter
import app.tapho.ui.tcash.adapter.customeCardDetailsAdapter
import app.tapho.ui.tcash.adapter.quickrecharegAdapter
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.custome_quickrechargemodel
import app.tapho.utils.withSuffixAmount
import kotlinx.android.synthetic.main.dishes_layout.*
import java.util.ArrayList


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