package app.tapho.ui.RechargeService.MobileRechcrge

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Activity.TELEPHONY_SERVICE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tapho.R
import app.tapho.databinding.FragmentMobileRecharge2Binding
import app.tapho.interfaces.ApiListener
import app.tapho.interfaces.RecyclerClickListener
import app.tapho.showShort
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.ModelData.DeviceContact.ContactModel
import app.tapho.ui.RechargeService.ModelData.DeviceContact.DeviceContactModelAdapter
import app.tapho.ui.RechargeService.ModelData.OperatorFatchModel.OpreatorfetchRes
import app.tapho.ui.RechargeService.ModelData.RechargeServices.RechargeServiceRes
import app.tapho.ui.RechargeService.ModelData.RechargeServices.ServiceBanner
import app.tapho.ui.home.adapter.NewBannerDataAdapter1
import app.tapho.ui.home.adapter.SliderModelMain
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.HomeRes
import app.tapho.ui.tcash.TimePeriodDialog
import app.tapho.ui.tcash.model.TCashDasboardRes
import app.tapho.ui.tcash.model.Txn
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator


class MobileRechargeFragment : BaseFragment<FragmentMobileRecharge2Binding>() {
    var isSelected=false
    private val CONTACT_PERMISSION_CODE = 1
    private val CONTACT_PICK_CODE = 2
    //var serviceTypeID = 1
    var serviceType = "Prepaid-Mobile"
    var currentOperater = ""
    var CircleOperater = ""
    var DeviceContactModelAdapter : DeviceContactModelAdapter?=null
    companion object {
        var mobileNumber = ""
        var Operator_Code = ""
        var Operator_ID = ""
        var Circle_Code = ""
        var Circle_Id = ""
        var operator_icon = ""

        var operator_id = ""
        var circle_id = ""
        var number = ""

        @JvmStatic
        fun newInstance() =
            MobileRechargeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingMobileRecharge = FragmentMobileRecharge2Binding.inflate(inflater, container, false)

        statusBarTextBlack()
        statusBarColor(R.color.black)
        setAllDataToEmpty()
        getHomeData()
        setLayoutForRecentRecharge()
        getTcashdetail()

//        bindingMobileRecharge!!.remove.setOnClickListener {
//            setAllDataToEmpty()
//            checkProgress()
//        }


//        bindingMobileRecharge!!.proceedTorecharge.visibility = View.GONE
        bindingMobileRecharge!!.progress.visibility = View.GONE
        bindingMobileRecharge!!.proceedTorecharge.isSelected=false
        bindingMobileRecharge!!.proceedTorecharge.isClickable=false
        bindingMobileRecharge!!.AllOperater.setOnClickListener {
            if (bindingMobileRecharge!!.number.text!!.length==10){
                ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
            }else{
                Snackbar.make(requireView(), "Please Enter valid mobile number", Snackbar.LENGTH_SHORT).show()
            }

        }
        bindingMobileRecharge!!.backbtn.setOnClickListener {
            activity?. onBackPressedDispatcher?.onBackPressed()
        }
        bindingMobileRecharge!!.number.addTextChangedListener{
            //    bindingMobileRecharge!!.layout.isSelected=true
            if (bindingMobileRecharge!!.number.text!!.length == 10) {
                bindingMobileRecharge!!.progress.visibility = View.VISIBLE
                bindingMobileRecharge!!.proceedTorecharge.isSelected = true
                bindingMobileRecharge!!.proceedTorecharge.isClickable = true
                val MobileNumber = bindingMobileRecharge!!.number.text.toString()
                OperatorFatchAPIModel(MobileNumber)
            }else{
                bindingMobileRecharge!!.proceedTorecharge.isSelected = false
                bindingMobileRecharge!!.proceedTorecharge.isClickable = false
                bindingMobileRecharge!!.operatorView.visibility = View.GONE
            }
        }

        bindingMobileRecharge!!.mobilenumbers.setOnClickListener {
            val MobileNumber = if (bindingMobileRecharge!!.mobilenumber.text.toString().contains("+91")){
                bindingMobileRecharge!!.mobilenumber.text.toString().replace("+91","")
            }else if (bindingMobileRecharge!!.mobilenumber.text.toString().length>10){
                bindingMobileRecharge!!.mobilenumber.text.toString().drop(2)
            }else{
                bindingMobileRecharge!!.mobilenumber.text.toString()
            }

            bindingMobileRecharge!!.number.setText(MobileNumber)
            OperatorFatchAPIModel(MobileNumber)
        }

        bindingMobileRecharge!!.proceedTorecharge.setOnClickListener {
            ProceedRecharge()
        }

        bindingMobileRecharge!!.contact.setOnClickListener {
            if (checkContactpermission()) {
                pickContact()
            } else {
                requestContactPermission()
            }
        }
        GetNumberd()

        return bindingMobileRecharge?.root
    }



    private fun ProceedRecharge() {
        if (bindingMobileRecharge!!.number.text!!.length < 10) {
            bindingMobileRecharge!!.number.setError("Please enter correct mobile number")
        } else {
            if (getSharedPreference().getString("operator_name")!!.isNullOrEmpty().not()){
                if (getSharedPreference().getString("circle_name").isNullOrEmpty().not()){
                    getSharedPreference().saveString("number",bindingMobileRecharge!!.number.text.toString())
                    ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")
                }else{
                    getSharedPreference().saveString("number",bindingMobileRecharge!!.number.text.toString())
                    ContainerActivity.openContainer(requireContext(),"SelectRechargeCircle","")
                }
            }else{
                getSharedPreference().saveString("number",bindingMobileRecharge!!.number.text.toString())
                ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
            }
        }
    }

    private fun checkProgress() {
//        if (getSharedPreference().getString("Operator_Code").isNullOrEmpty().not()){
////            bindingMobileRecharge!!.rechargeProgress.visibility = View.VISIBLE
//            getSharedPreference().let {
//                Glide.with(requireContext()).load(it.getString("operator_icon")).into(bindingMobileRecharge!!.OpIcon)
//                bindingMobileRecharge!!.mobileNumber.text = it.getString("number")
//                bindingMobileRecharge!!.OP.text = it.getString("operator_name")+" | "+it.getString("circle_name")
//                bindingMobileRecharge!!.continueRecharge.setOnClickListener {
//                    if (getSharedPreference().getString("operator_name")!!.isNullOrEmpty().not()){
//                        if (getSharedPreference().getString("circle_name").isNullOrEmpty().not()){
//                            getSharedPreference().saveString("number",bindingMobileRecharge!!.mobileNumber.text.toString())
//                            ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")
//                        }else{
//                            getSharedPreference().saveString("number",bindingMobileRecharge!!.mobileNumber.text.toString())
//                            ContainerActivity.openContainer(requireContext(),"SelectRechargeCircle","")
//                        }
//                    }else{
//                        getSharedPreference().saveString("number",bindingMobileRecharge!!.mobileNumber.text.toString())
//                        ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
//                    }
//
//                }
//            }
//
//        }else{
//            bindingMobileRecharge!!.rechargeProgress.visibility = View.GONE
//        }
    }

    private fun setLayoutForRecentRecharge() {
        DeviceContactModelAdapter = DeviceContactModelAdapter(object : RecyclerClickListener{
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {
                if (data is Txn){
                   data.recharge_detail.get(0).let {

                       val OPname = it.operator_detail.name
                       val OPcode = it.operator_detail.operator_code
                       val finalicon=  it.operator_detail.image


                       val circleName = it.circle_detail.name
                       val circleCode = it.circle_detail.code

//                       getSharedPreference().saveString("Operator_Code",OPcode) // OP Code is required
//                       getSharedPreference().saveString("Circle_Code",circleCode)
//                       getSharedPreference().saveString("operator_name",OPname)
//                       getSharedPreference().saveString("circle_name",circleName)
//                       getSharedPreference().saveString("operator_icon",finalicon)
//                       getSharedPreference().saveString("circle_id",it.circle_detail.id)
//                       getSharedPreference().saveString("operator_id",it.operator_detail.id)
//                       getSharedPreference().saveString("min_recharge",it.operator_detail.min_recharge.toString())
//                       getSharedPreference().saveString("number",it.number)
//                       ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")

                   }
                }
            }
        })
        bindingMobileRecharge!!.recentRecharge.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = DeviceContactModelAdapter
        }
//        DeviceContactModelAdapter.addAllItem()
    }

    private fun getTcashdetail() {
        viewModel.getTCashDashboard(getUserId(),TimePeriodDialog.getDate(1,-12),TimePeriodDialog.getCurrentDate(),"1",this,object : ApiListener<TCashDasboardRes,Any?>{
            override fun onSuccess(t: TCashDasboardRes?, mess: String?) {
                val MobileRecarge : ArrayList<Txn> = ArrayList()
                bindingMobileRecharge!!.history.visibility = View.VISIBLE
                bindingMobileRecharge!!.history.setOnClickListener {
                    ContainerActivity.openContainerForTransaction(requireContext(),"AllTransactionFragment","Recharge",t!!,false,"Recharge & bill payments")
                }

                t!!.txn.let {
                   it.forEach {
                       if (it.type.equals("3")){
                           if (it.recharge_detail.isNullOrEmpty().not()){
                               if (!MobileRecarge.toString().contains(it.recharge_detail.get(0).number)){
                                   MobileRecarge.add(it)
                               }
                           }
                       }
                   }

                    if (MobileRecarge.isNullOrEmpty().not()){
                        DeviceContactModelAdapter!!.addAllItem(MobileRecarge)
                        bindingMobileRecharge!!.recentLayout.visibility = View.VISIBLE
                    }else{
                        bindingMobileRecharge!!.recentLayout.visibility = View.GONE
                    }




                }

            }

        })
    }

    private fun setAllDataToEmpty() {
        getSharedPreference().saveString("Operator_Code","")
        getSharedPreference().saveString("Circle_Code","")
        getSharedPreference().saveString("number","")
     //   getSharedPreference().saveString("servicetype","")
        getSharedPreference().saveString("discription","")
        getSharedPreference().saveString("Amount","")
        getSharedPreference().saveString("operator_name","")
        getSharedPreference().saveString("circle_name","")
        getSharedPreference().saveString("operator_icon","")
        getSharedPreference().saveString("circle_id","")
        getSharedPreference().saveString("operator_id","")
        getSharedPreference().saveString("min_recharge","")
        getSharedPreference().saveString("user_commission","")
    }

    fun GetNumberd() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_NUMBERS) ==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val telephonyManager = requireContext().getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val phoneNumber = telephonyManager.line1Number
            val phoneNumberOP =telephonyManager.networkOperatorName
            bindingMobileRecharge!!.MNumberop.text = phoneNumberOP
            bindingMobileRecharge!!.mobilenumber.text = phoneNumber
            return
        } else {
            // Ask for permission
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.READ_PHONE_STATE
                ), 100
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                val telephonyManager = requireContext().getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                val phoneNumber = telephonyManager.line1Number
                val phoneNumberOP =telephonyManager.networkOperatorName
                bindingMobileRecharge!!.MNumberop.text = phoneNumberOP
                bindingMobileRecharge!!.mobilenumber.text = phoneNumber
            }
            else -> //                requireContext().showShort(phoneNumber)
            {
                throw IllegalStateException("Unexpected value: $requestCode")
            }
        }
    }

    private fun OperatorFatchAPIModel(mobileNumber: String) {
        viewModel.OperatorFatchAPI(AppSharedPreference.getInstance(requireContext()).getUserId(), mobileNumber, this,
            object : ApiListener<OpreatorfetchRes, Any?> {
                override fun onSuccess(t: OpreatorfetchRes?, mess: String?) {
                    getSharedPreference().saveString("number",bindingMobileRecharge!!.number.text.toString())


                    currentOperater ="null" // t?.data!!.OperatorCode.toString()
                    CircleOperater ="null" // t.data.CircleCode.toString()
                    if (currentOperater.equals("null")){
                        getSharedPreference().saveString("number",bindingMobileRecharge!!.number.text.toString())
                        ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
                     //   activity?.finish()
                        bindingMobileRecharge!!.proceedTorecharge.visibility = View.VISIBLE
                        bindingMobileRecharge!!.AllOperater.text="Select Operater"
                        bindingMobileRecharge!!.operatorView.visibility = View.VISIBLE
                        bindingMobileRecharge!!.progress.visibility = View.GONE
                    }else{
                        bindingMobileRecharge!!.AllOperater.text=currentOperater
                        bindingMobileRecharge!!.proceedTorecharge.visibility = View.VISIBLE
                        bindingMobileRecharge!!.operatorView.visibility = View.VISIBLE
                        bindingMobileRecharge!!.progress.visibility = View.GONE

                    }
                    if (CircleOperater.equals("null")){
                        bindingMobileRecharge!!.AllCircle.text="Select Circle"
                    }else{
                        bindingMobileRecharge!!.AllCircle.text=CircleOperater
                    }

                }

            })
    }

    private fun enableCLickForCircle(currentOperaters: String) {
        bindingMobileRecharge!!.AllCircle.setOnClickListener {
            if (getSharedPreference().getString("operator_name")!!.isEmpty()){
                Snackbar.make(requireView(), "Please select Operator First", Snackbar.LENGTH_SHORT).show()
            }else if (currentOperaters.equals("null")){
                Snackbar.make(requireView(), "Please select Operator First", Snackbar.LENGTH_SHORT).show()
            }else{
                ContainerActivity.openContainer(requireContext(),"SelectRechargeCircle","")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkProgress()
        setTextAnddata()
    }

    private fun setTextAnddata() {
        if (getSharedPreference().getString("operator_name")!!.isEmpty().not()){
            val icon = getSharedPreference().getString("operator_icon").toString()
            bindingMobileRecharge!!.AllOperater.text = getSharedPreference().getString("operator_name").toString()
            Glide.with(requireContext()).load(icon).circleCrop().into(bindingMobileRecharge!!.icon)
            bindingMobileRecharge!!.progress.visibility = View.GONE
        }else{
            bindingMobileRecharge!!.AllOperater.text="Select Operater"
        }
        if (getSharedPreference().getString("circle_name")!!.isEmpty().not()){
            bindingMobileRecharge!!.AllCircle.text = getSharedPreference().getString("circle_name").toString()
            bindingMobileRecharge!!.progress.visibility = View.GONE
        }else{
            bindingMobileRecharge!!.AllCircle.text="Select Circle"
        }

        enableCLickForCircle(currentOperater)

    }

    private fun checkContactpermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactPermission() {
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(requireActivity(), permission, CONTACT_PERMISSION_CODE)
    }

    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICK_CODE)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CONTACT_PICK_CODE) {

                val cursor1: Cursor
                val cursor2: Cursor?

                val uri = data!!.data
                cursor1 = requireActivity().contentResolver.query(uri!!, null, null, null, null)!!
                if (cursor1.moveToFirst()) {
                    val Contactid =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
                    val ContactName =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val ContactThubnail =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    val Contactresult =
                        cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val idResultHold = Contactresult.toInt()

                    if (idResultHold == 1) {
                        cursor2 = requireActivity().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contactid,
                            null,
                            null
                        )
                        while (cursor2!!.moveToNext()) {
                            val ContactNumber =
                                cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val Number = ContactNumber.toString()
                            if (Number.contains("+91")) {
                                val data = Number.drop(3)
                                bindingMobileRecharge!!.number.text =
                                    Editable.Factory.getInstance().newEditable(data)
                            } else {
                                bindingMobileRecharge!!.number.text =
                                    Editable.Factory.getInstance().newEditable(Number)
                            }
                        }
                        cursor2.close()
                    }
                    cursor1.close()
                }
            }

        } else {
            Toast.makeText(context, "Cancelled ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getHomeData() {
        viewModel.getRechargeService(getUserId(),this,object : ApiListener<RechargeServiceRes,Any?>{
            override fun onSuccess(t: RechargeServiceRes?, mess: String?) {
                t.let {
                    it!!.data.get(0).service_banner.let {
                        setBannerAuto(it)
                    }
                }
            }

        })
    }

    private fun setBannerAuto(banners:List<ServiceBanner>) {

        if (banners.isNullOrEmpty()) {

            bindingMobileRecharge!!.bannerlay.visibility = View.GONE
        } else {
            bindingMobileRecharge!!.bannerlay.visibility = View.VISIBLE
        }
        val imageList = ArrayList<SliderModelMain>()

        for (x in banners) {
            if (x.image.isNullOrEmpty().not()){
                imageList.add(SliderModelMain(x.image, x.url, x.id,x.type, "", ""))
            }

        }

        bindingMobileRecharge!!.banner.adapter = NewBannerDataAdapter1(imageList, bindingMobileRecharge!!.banner, object : RecyclerClickListener {
            override fun onRecyclerItemClick(pos: Int, data: Any?, type: String) {

            }
        })
        bindingMobileRecharge!!.banner.clipToPadding = false
        bindingMobileRecharge!!.banner.clipChildren = false
        bindingMobileRecharge!!.banner.offscreenPageLimit = 3
        bindingMobileRecharge!!.banner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        bindingMobileRecharge!!.banner.setPadding(20, 0, 20, 0)

        //Handler Start
        Handler(Looper.getMainLooper()).apply {
            val runnable = object : Runnable {
                override fun run() {
                    kotlin.runCatching {
                        var i = bindingMobileRecharge!!.banner.currentItem

                        if (i == imageList.size - 1) {
                            i = -1//0
                            bindingMobileRecharge!!.banner.currentItem = i
                        }
                        i++
                        bindingMobileRecharge!!.banner.setCurrentItem(i, true)
                        postDelayed(this, 2000)
                    }
                }
            }
            postDelayed(runnable, 2000)
        }

        TabLayoutMediator(bindingMobileRecharge!!.tablayout,bindingMobileRecharge!!.banner,false) { _,_ -> }.attach()
    }


}