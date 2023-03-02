package app.tapho.ui.RechargeService.MobileRechcrge

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentSelectRechargeCircleBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.MobileRechcrge.adapter.RechargeCircleAdapter
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.Data
import app.tapho.ui.RechargeService.ModelData.RechargeCircle.RechargeCircle
import java.util.*
import kotlin.collections.ArrayList


class SelectRechargeCircleFragment :BaseFragment<FragmentSelectRechargeCircleBinding>(),ApiListener<RechargeCircle,Any?>,RecyclerClickListener{

    private var CircleAdapter: RechargeCircleAdapter<Data>? = null
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
        _binding = FragmentSelectRechargeCircleBinding.inflate(inflater,container,false)
        _binding!!.mainlayout.visibility = View.GONE
        _binding!!.progress.visibility = View.VISIBLE
        statusBarTextBlack()
        statusBarColor(R.color.black)
        getViewmodelData()
        setCircleLayout()
        _binding!!.backbtn.setOnClickListener {
            ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
            activity?.finish()
        }

       backpressedbtn()

       initdata()


        return _binding?.root
    }

    private fun backpressedbtn() {
        val  OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
                activity?.finish()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), OnBackPressedCallback)
    }

    private fun setCircleLayout() {
        CircleAdapter = RechargeCircleAdapter(this)
        _binding!!.circle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CircleAdapter
        }
    }

    private fun getViewmodelData() {
        viewModel.getRechargeCircle(getUserId(),this,this)
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
        CircleAdapter!!.updatelist(tempList)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            SelectRechargeCircleFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onSuccess(t: RechargeCircle?, mess: String?) {
        t.let {
            if (it!!.errorCode.equals("0")){
                if (it.data.isNullOrEmpty().not()){
                    itemList=it.data
                    CircleAdapter!!.addAllItem(it.data)
                    _binding!!.mainlayout.visibility = View.VISIBLE
                    _binding!!.progress.visibility = View.GONE
                }
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

        if (data is Data){
            getSharedPreference().saveString("Circle_Code", data.code)
            getSharedPreference().saveString("circle_name",data.name)
            getSharedPreference().saveString("circle_id", data.id)
            ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")
            activity?.finish()
        }


    }
}