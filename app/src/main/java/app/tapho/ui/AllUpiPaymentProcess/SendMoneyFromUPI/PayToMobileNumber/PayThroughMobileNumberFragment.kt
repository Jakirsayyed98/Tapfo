package app.tapho.ui.AllUpiPaymentProcess.SendMoneyFromUPI.PayToMobileNumber

import android.R
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import app.tapho.GetContactList.ContactModel
import app.tapho.GetContactList.ContactPermissionTraking
import app.tapho.GetContactList.FeatchContactListAdapter
import app.tapho.databinding.FragmentPayThroughMobileNumberBinding
import app.tapho.ui.AllUpiPaymentProcess.UPIContainerActivity
import app.tapho.ui.BaseFragment
import com.vmadalin.easypermissions.EasyPermissions


class PayThroughMobileNumberFragment : BaseFragment<FragmentPayThroughMobileNumberBinding>()/*, EasyPermissions.PermissionCallbacks */{


    var arrayList : ArrayList<ContactModel> = arrayListOf()
    var FeatchContactListAdapter : FeatchContactListAdapter = FeatchContactListAdapter(arrayList)

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
        _binding = FragmentPayThroughMobileNumberBinding.inflate(inflater,container,false)

    //    getContactList()

        getAllTextAndData()

        if (checkContactFachingPermission()){

            _binding!!.apply {
                AllContacts1.apply {
                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    adapter = FeatchContactListAdapter(arrayList)
                }
            }
            getAllContactData()
        }
        checkContactFachingPermission()

        return _binding?.root
    }

    private fun getAllTextAndData() {
        _binding!!.number.addTextChangedListener {
            if (_binding!!.number.text.toString().length==10){
                _binding!!.proceedBtn.isClickable=true
                _binding!!.proceedBtn.isSelected=true
            }else{
                _binding!!.proceedBtn.isClickable=false
                _binding!!.proceedBtn.isSelected=false
            }
        }

        _binding!!.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding!!.proceedBtn.setOnClickListener {
            if(_binding!!.number.text!!.contains("0000000000")){
                UPIContainerActivity.openContainer(requireContext(),"NumberNotAvailInUPIID","",false,"",_binding!!.number.text.toString())
            }else{
                UPIContainerActivity.openContainer(requireContext(),"NumberAvailInUPIIDSearchPage","",false,"",_binding!!.number.text.toString())
            }
        }

    }

    private fun getContactList() {


        // create cursor and query the data
        // create cursor and query the data
       val cursor:Cursor = requireActivity().contentResolver.query(
           ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
           null,
           null,
           null,
           null
       )!!
        requireActivity().startManagingCursor(cursor)

        // data is a array of String type which is
        // used to store Number ,Names and id.

        // data is a array of String type which is
        // used to store Number ,Names and id.
        val data = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone._ID
        )
        val to = intArrayOf(R.id.text1, R.id.text2)

        // creation of adapter using SimpleCursorAdapter class

        // creation of adapter using SimpleCursorAdapter class
        val adapter = SimpleCursorAdapter(requireContext(), R.layout.simple_list_item_2, cursor, data, to)

        // Calling setAdaptor() method to set created adapter

        // Calling setAdaptor() method to set created adapter

        _binding!!.AllContacts.setAdapter(adapter)
        _binding!!.AllContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
    }


    @SuppressLint("Range")
    private fun getAllContactData() {
        arrayList.clear()
        val cursor = requireActivity().contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        while (cursor!!.moveToNext()){
            val cursorName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val cursorNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel(cursorName,cursorNumber)
            arrayList.add(contactModel)
        }
        Log.d("ContactData","${arrayList.size}")
        FeatchContactListAdapter.notifyDataSetChanged()
        cursor.close()

    }


    private fun checkContactFachingPermission(): Boolean {
        if (ContactPermissionTraking.hasCOntactPermissions(requireContext())){
            return true
        }else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            EasyPermissions.requestPermissions(requireActivity(),"You will need to accept the permmision in order to run the Application",100
            ,android.Manifest.permission.READ_CONTACTS,android.Manifest.permission.WRITE_CONTACTS)
            return true
        }else{
            return false
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            PayThroughMobileNumberFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    /*
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)){
            AppSettingsDialog.Builder(requireActivity())
        }
    }
    */

}