package app.tapho.ui.RechargeService.DTHRecharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentDTHBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.DTHRecharge.Adapter.DTHRechargeOpratorAdapter
import app.tapho.ui.RechargeService.MobileRechcrge.MobileRechargeFragment
import app.tapho.ui.RechargeService.MobileRechcrge.adapter.RechargeOrporaterAdapter
import app.tapho.ui.recharge.model.ServiceOperatorData
import app.tapho.ui.recharge.model.ServiceOperatorRes


class DTHFragment : BaseFragment<FragmentDTHBinding>() {


    private var orporaterAdapter: DTHRechargeOpratorAdapter<ServiceOperatorData>? = null

    var serviceTypeID = ""

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
        _binding = FragmentDTHBinding.inflate(inflater, container, false)
        statusBarTextWhite()
        statusBarColor(R.color.white)
        _binding!!.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        serviceTypeID= getSharedPreference().getString("servicetype").toString()
        getDTHOpratorViewModel()
        SetOperatorData()
        return _binding?.root
    }

    private fun getDTHOpratorViewModel() {
        viewModel.getRechargeServiceOperator(
            getUserId(),
            serviceTypeID,
            this,
            object : ApiListener<ServiceOperatorRes, Any?> {
                override fun onSuccess(t: ServiceOperatorRes?, mess: String?) {
                    t.let {
                        it!!.data.let {
                            orporaterAdapter!!.addAllItem(it!!)
                        }
                    }
                }
            })
    }

    private fun SetOperatorData() {

        orporaterAdapter = DTHRechargeOpratorAdapter(object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

                if (data is ServiceOperatorData) {
                    if (data.image.isNullOrEmpty().not()){
                        getSharedPreference().saveString("operator_icon", "https://tapfo.in/delta/public/storage/all_images/" + data.image!!)
                    }else{
                        getSharedPreference().saveString("operator_icon", "")
                    }
                    getSharedPreference().saveString("operator_id", data.id!!)
                    getSharedPreference().saveString("operator_name", data.name!!)
                    getSharedPreference().saveString("account_display", data.account_display!!)
                    getSharedPreference().saveString("amount_range", data.amount_range!!)
                    getSharedPreference().saveString("discription", "Enter 10 digit Customer Id Starting with 3. To Locate the Customer ID, press the MENU Button on your remote.")
                    ContainerActivity.openContainerForDTHRecharge(
                        requireContext(),
                        "ProceedForDTHRecharge"
                    )
                }

            }

        })
        _binding!!.DTHOpraterName.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = orporaterAdapter
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DTHFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}