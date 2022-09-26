package app.tapho.ui.RechargeService.MobileRechcrge

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import app.tapho.R
import app.tapho.databinding.FragmentMobileRecharge2Binding
import app.tapho.interfaces.ApiListener
import app.tapho.ui.BaseFragment
import app.tapho.ui.ContainerActivity
import app.tapho.ui.RechargeService.ModelData.OperatorFatchModel.getRechargeoperatorFeatch
import app.tapho.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_enter_bank_detail.*


class MobileRechargeFragment : BaseFragment<FragmentMobileRecharge2Binding>() {
    var isSelected=false
    private val CONTACT_PERMISSION_CODE = 1
    private val CONTACT_PICK_CODE = 2
    //var serviceTypeID = 1
    var serviceType = "Prepaid-Mobile"
    var currentOperater = ""
    var CircleOperater = ""


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

        statusBarTextWhite()
        statusBarColor(R.color.white)

        featchMyNumber()

        bindingMobileRecharge!!.proceedTorecharge.visibility = View.GONE
        bindingMobileRecharge!!.progress.visibility = View.GONE
        bindingMobileRecharge!!.proceedTorecharge.isSelected=false
        bindingMobileRecharge!!.proceedTorecharge.isClickable=false
        bindingMobileRecharge!!.AllOperater.setOnClickListener {
            ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
            activity?.finish()
        }
        bindingMobileRecharge!!.backBtn.setOnClickListener {
          activity?.onBackPressed()
        }
        bindingMobileRecharge!!.number.addTextChangedListener{
            bindingMobileRecharge!!.layout.isSelected=true
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

        bindingMobileRecharge!!.proceedTorecharge.setOnClickListener {
            if (bindingMobileRecharge!!.number.text!!.length < 10) {
                bindingMobileRecharge!!.number.setError("Please enter correct mobile number")
            } else {
                getSharedPreference().saveString("number",bindingMobileRecharge!!.number.text.toString())
                ContainerActivity.openContainerForRecharge(context, "RechargeOrporaterAllPlans","",false,"Mobile Prepaid")
                activity?.finish()
            }

        }

        bindingMobileRecharge!!.contact.setOnClickListener {
            if (checkContactpermission()) {
                pickContact()
            } else {
                requestContactPermission()
            }
        }


        return bindingMobileRecharge?.root
    }

    private fun featchMyNumber() {
        if (ActivityCompat.checkSelfPermission(requireContext(), READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), READ_PHONE_NUMBERS) ==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val tMgr = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val mPhoneNumber = tMgr.line1Number+" \n"+tMgr.simOperator+" \n"+tMgr.deviceSoftwareVersion+" \n"+tMgr.networkOperatorName

            bindingMobileRecharge!!.textNumber.text = mPhoneNumber
            return
        } else {
            CheckPermission()
        }
    }


    private fun CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE),100)
        }
    }

    private fun OperatorFatchAPIModel(mobileNumber: String) {
        viewModel.OperatorFatchAPI(AppSharedPreference.getInstance(requireContext()).getUserId(), mobileNumber, this,
            object : ApiListener<getRechargeoperatorFeatch, Any?> {
                override fun onSuccess(t: getRechargeoperatorFeatch?, mess: String?) {

                    currentOperater = t?.OperatorCode.toString()
                    CircleOperater = t?.CircleCode.toString()

                    if (currentOperater.equals("null")){
                        getSharedPreference().saveString("number",bindingMobileRecharge!!.number.text.toString())
                        ContainerActivity.openContainer(requireContext(),"SelectRechargeOpretor","")
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContact()
            } else {
                Toast.makeText(context, "permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
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

}