package app.tapho.ui.tcash

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.tapho.R
import app.tapho.databinding.FragmentFoPointsScreenBinding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.TCashDashboardData
import app.tapho.utils.TITLE


class FoPointsScreenFragment : BaseFragment<FragmentFoPointsScreenBinding>(),ApiListener<TCashDasboardRes,Any?> {
    var points =0.0
    var pendingpoints =0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoPointsScreenBinding.inflate(inflater,container,false)
        statusBarColor(R.color.lightpurple)

        tcashviewmodeldata()
        setAlltext()
        setClicks()


        return _binding?.root
    }

    private fun setClicks() {
        _binding!!.back.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.coinHistory.setOnClickListener {
            ContainerActivity.openContainer(context, "AvailableBalance", "")
        }
    }

    private fun setAlltext() {

    }

    private fun tcashviewmodeldata() {
        viewModel.getTCashDashboard(getUserId(), TimePeriodDialog.getDate(1, -12),
            TimePeriodDialog.getCurrentDate(),this,this)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            FoPointsScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
        t.let {
            it!!.data!!.forEach {
                if (it.status!!.uppercase().equals("PENDING")){
                    pendingpoints = pendingpoints + it.sale_amount!!.toDouble()
                }
            }
            it.data!!.forEach {
                if (it.status!!.uppercase().equals("VERIFIED")){
                    points = points + it.sale_amount!!.toDouble()
                }else  if (it.status!!.uppercase().equals("VALIDATED")){
                    points = points + it.sale_amount!!.toDouble()
                }

            }

            _binding!!.totalPoints.text = points.toString().dropLast(2)
            _binding!!.pendingpoints.text =getString(R.string.you_have_total,pendingpoints.toString().dropLast(2))
        }
    }


}