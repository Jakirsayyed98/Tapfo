package app.tapho.ui.RechargeService.MobileRechcrge

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.R
import app.tapho.databinding.FragmentAllPlansBinding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.MobileRechcrge.adapter.AllPlansAdapters
import app.tapho.ui.RechargeService.ModelData.RechargePlans.PlanDescription
import app.tapho.ui.RechargeService.ModelData.RechargePlans.getRechargePlans
import app.tapho.utils.AppSharedPreference
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList


class AllPlansFragment : BaseFragment<FragmentAllPlansBinding>() ,ApiListener<getRechargePlans,Any?>,RecyclerClickListener{
    var Operator_Code = ""
    var Circle_Code = ""
    var mobileNumber = ""
    var opretorName = ""
    var operator_icon = ""
    var circle_name = ""


    private lateinit var itemList:List<PlanDescription>


    private var AllPlansAdapter : AllPlansAdapters<PlanDescription>? = null//
    val categoryList: ArrayList<String> = ArrayList()
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
        AllplansBinding= FragmentAllPlansBinding.inflate(inflater, container, false)
        statusBarTextBlack()
        statusBarColor(R.color.black)
        Operator_Code = getSharedPreference().getString("Operator_Code").toString()
        Circle_Code = getSharedPreference().getString("Circle_Code").toString()
        mobileNumber = getSharedPreference().getString("number").toString()
        opretorName = getSharedPreference().getString("operator_name").toString()
        operator_icon =getSharedPreference().getString("operator_icon").toString()
        circle_name =getSharedPreference().getString("circle_name").toString()

        AllplansBinding!!.mainLayout.visibility = View.GONE
        AllplansBinding!!.progress.visibility = View.VISIBLE

        AllplansBinding!!.opretorName.text = opretorName +" - "+circle_name
        AllplansBinding!!.number.text =  mobileNumber
        Glide.with(requireContext()).load(operator_icon).circleCrop().centerCrop().into(AllplansBinding!!.opretorIcon)

        AllplansBinding!!.backIv.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        viewmodeldata()
        setAllPlansInData()
        initdata()
        return AllplansBinding?.root
    }


    private fun viewmodeldata() {

        viewModel.getAllRechargePlans(AppSharedPreference.getInstance(requireContext()).getUserId(),mobileNumber,Operator_Code, Circle_Code,this, this)
    }

    override fun onSuccess(t: getRechargePlans?, mess: String?) {
        if (t!!.errorCode.equals("0")){
            t.data.PlanDescription.let {
                itemList=it
                it.forEach {
                    getListData(it)
                }
                initTabLayout()
                AllPlansAdapter!!.addAllItem(it)

                AllplansBinding!!.mainLayout.visibility = View.VISIBLE
                AllplansBinding!!.progress.visibility = View.GONE
            }
        }
    }


    private fun initdata() {
        AllplansBinding!!.searchEt.addTextChangedListener(object : TextWatcher {
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
        val tempList: ArrayList<PlanDescription> = ArrayList()
        for (x in itemList){
            if (searchName.lowercase(Locale.getDefault()) in x.recharge_amount.toString().lowercase(Locale.getDefault())){
                tempList.add(x)
            }
        }
        AllPlansAdapter!!.updatelist(tempList)
    }

    private fun initTabLayout() {
        categoryList.forEach {
            AllplansBinding!!.tabLayout.addTab(AllplansBinding!!.tabLayout.newTab().setText(it))
        }
        initMediator()
    }

    private fun initMediator() {
        TabbedListMediator(
            AllplansBinding!!.RechargeType,
            AllplansBinding!!.tabLayout,
            categoryList.indices.toList()
        ).attach()
    }

    private fun getListData(it: PlanDescription) {
        if (categoryList.contains(it.recharge_type)){

        }else{
            categoryList.add(it.recharge_type)
        }

    }

    private fun setAllPlansInData() {
        AllPlansAdapter = AllPlansAdapters(object :RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                getSharedPreference().saveString("discription",type.toString())
                getSharedPreference().saveString("Amount",data.toString())
                ContainerActivity.openContainerForFinalRecharge(requireContext(),"FinalCheckForRecharge",data.toString(),false,"")
                activity?.finish()
            }
        })
        AllplansBinding!!.RechargeType.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = AllPlansAdapter

        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            AllPlansFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }



    override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

        //
    }
}