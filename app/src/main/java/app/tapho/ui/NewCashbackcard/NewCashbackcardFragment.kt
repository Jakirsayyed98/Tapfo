package app.tapho.ui.NewCashbackcard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentNewCashbackcardBinding
import app.tapho.databinding.FragmentNewsHeadlineBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.home.ShopProduct.Model.ShopProduct.Data
import app.tapho.ui.home.ShopProduct.ShopAdapter.ShopProducatAdapter
import app.tapho.ui.home.adapter.CardAdapter
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import com.google.android.gms.common.api.Api


class NewCashbackcardFragment :BaseFragment<FragmentNewCashbackcardBinding>(),RecyclerClickListener {
    private var card: CardAdapter<TCashDashboardData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingCard = FragmentNewCashbackcardBinding.inflate(inflater, container,false)
        statusBarColor(R.color.lightGreen)
        tCashViewmodel()
        setCardRecyclerData()

        return bindingCard?.root
    }

    private fun tCashViewmodel() {
        viewModel.getTCashDashboard(getUserId(),TimePeriodDialog.getDate(1, -12), TimePeriodDialog.getCurrentDate(),this, object : ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                setCardData(t)
            }

        })
    }

    private fun setCardData(t: TCashDasboardRes?) {
        t.let {
            card!!.addAllItem(it!!.data!!)
        }
    }


    private fun setCardRecyclerData() {
        card = CardAdapter(this)
        bindingCard!!.cards.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = card
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            NewCashbackcardFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

    }
}