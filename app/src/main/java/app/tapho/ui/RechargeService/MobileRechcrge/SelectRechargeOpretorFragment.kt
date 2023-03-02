package app.tapho.ui.RechargeService.MobileRechcrge

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.Data
import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.ServiceOperatorRes

import java.util.*
import kotlin.collections.ArrayList


class SelectRechargeOpretorFragment : BaseFragment<FragmentSelectRechargeOpretorBinding>(),ApiListener<ServiceOperatorRes,Any?>,RecyclerClickListener {

    private var orporaterAdapter: RechargeOrporaterAdapter<Data>? = null

    private lateinit var itemList:List<Data>

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
        statusBarTextBlack()
        statusBarColor(R.color.black)
        getOpretorData()

        _binding!!.mainlayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE

        _binding!!.backbtn.setOnClickListener {
          activity?.onBackPressedDispatcher?.onBackPressed()
        }
        setLayoutData()
//        if (itemList.isNullOrEmpty().not()){
            initdata()
//        }

        return _binding?.root
    }

    private fun getOpretorData() {
        viewModel.getRechargeServiceOperator(getUserId(),getSharedPreference().getString("servicetype").toString(),this,this)
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
        val tempList: ArrayList<Data> = ArrayList()
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
            if (it!!.errorCode.equals("0")){
                if (it.data.isNullOrEmpty().not()){
                    itemList= it.data
                    orporaterAdapter!!.addAllItem(it.data)
                    _binding!!.mainlayout.visibility = View.VISIBLE
                    _binding!!.progress.visibility = View.GONE

                }
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
        if (data is Data){
            getSharedPreference().saveString("Operator_Code", data.operator_code.toString())
            getSharedPreference().saveString("operator_name",data.name.toString())
            getSharedPreference().saveString("operator_icon",data.image.toString())
            getSharedPreference().saveString("operator_id", data.id.toString())

            getSharedPreference().saveString("min_recharge",data.min_recharge.toString())
            getSharedPreference().saveString("user_commission",data.user_commission)

            ContainerActivity.openContainer(requireContext(),"SelectRechargeCircle","")
            activity?.finish()
        }
    }
}