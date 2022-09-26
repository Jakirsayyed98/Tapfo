package app.tapho.ui.RechargeService.MobileRechcrge

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSelectRechargeOpretorBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.MobileRechcrge.adapter.RechargeOrporaterAdapter
import app.tapho.ui.games.models.getGames.Data
import app.tapho.ui.recharge.model.ServiceOperatorData
import app.tapho.ui.recharge.model.ServiceOperatorRes
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList


class SelectRechargeOpretorFragment : BaseFragment<FragmentSelectRechargeOpretorBinding>(),ApiListener<ServiceOperatorRes,Any?>,RecyclerClickListener {

    private var orporaterAdapter: RechargeOrporaterAdapter<ServiceOperatorData>? = null

    private lateinit var itemList:List<ServiceOperatorData>

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
        _binding = FragmentSelectRechargeOpretorBinding.inflate(inflater,container,false)
        statusBarTextWhite()
        getOpretorData()
        setLayoutData()
        _binding!!.backIv.setOnClickListener {
            ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
            activity?.finish()
        }
        initdata()
        backpressedbtn()
        return _binding?.root
    }

    private fun getOpretorData() {
        viewModel.getRechargeServiceOperator(getUserId(),getSharedPreference().getString("servicetype").toString(),this,this)
    }

    private fun backpressedbtn() {
        val  OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                ContainerActivity.openContainer(requireContext(), "mobile_prepaid", "")
                activity?.finish()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), OnBackPressedCallback)
    }


    private fun setLayoutData() {
        orporaterAdapter = RechargeOrporaterAdapter(this)
        _binding!!.orperator.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = orporaterAdapter
        }
    }

    private fun initdata() {
        _binding!!.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterListData(p0.toString())
            }
        })
    }

    private fun filterListData(searchName: String) {
        var tempList: ArrayList<ServiceOperatorData> = ArrayList()
        for (x in itemList){
            if (searchName.lowercase(Locale.getDefault()) in x.name.toString().lowercase(Locale.getDefault())){
                tempList.add(x)
            }
        }
        orporaterAdapter!!.updatelist(tempList)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            SelectRechargeOpretorFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: ServiceOperatorRes?, mess: String?) {
        t.let {
            itemList=it!!.data!!
            orporaterAdapter!!.addAllItem(it.data!!)
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is ServiceOperatorData){
            getSharedPreference().saveString("Operator_Code", data.operator_code.toString())
            getSharedPreference().saveString("operator_name",data.name.toString())
            getSharedPreference().saveString("operator_icon", "https://tapfo.in/delta/public/storage/all_images/"+data.image.toString())
            getSharedPreference().saveString("operator_id", data.id.toString())
            ContainerActivity.openContainer(requireContext(),"SelectRechargeCircle","")
            activity?.finish()
        }
    }
}