package app.tapho.ui.recharge

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.cursoradapter.widget.SimpleCursorAdapter
import app.tapho.R
import app.tapho.databinding.FragmentContactsBinding
import app.tapho.ui.BaseFragment


class ContactsFragment : BaseFragment<FragmentContactsBinding>(), OnItemClickListener {
  private  val cursorColumns = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        search()
        return _binding?.root
    }

    private fun search() {
        val contacts: Cursor? = getListOfContactNames("")

        val viewIds = intArrayOf(R.id.tv_name)

        val simpleCursorAdapter = SimpleCursorAdapter(
            context,
            R.layout.contact_item_layout,
            contacts,
            cursorColumns,
            viewIds,
            0)

        binding.contactListView.adapter = simpleCursorAdapter
        binding.contactListView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l -> //get contact details and display
                val tv = view.findViewById<TextView>(R.id.tv_name)
                Toast.makeText(context,
                    "Selected Contact " + tv.text,
                    Toast.LENGTH_LONG).show()
            }
    }

    private fun search2() {
//        val contacts = getListOfContactNames(newText)
//        val cursorAdapter = ContactsAdapter(this@ContactsSearchActivity, contacts, searchView)
//        searchView.setSuggestionsAdapter(cursorAdapter)
    }

     fun getListOfContactNames(searchText: String): Cursor? {
        var cur: Cursor? = null
        val cr: ContentResolver? = context?.contentResolver
        val mProjection = arrayOf(ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        val uri: Uri = ContactsContract.Contacts.CONTENT_URI
        val selection = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?"
        val selectionArgs = arrayOf("%$searchText%")
        cur = cr?.query(uri, mProjection, selection, selectionArgs, null)
        return cur
    }

    override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

    }

    companion object {

    }

}