package app.tapho.ui.recharge.model

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tapho.interfaces.ApiListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactFetchModel : ViewModel() {
    fun getAllContacts(context: Context?, listener: ApiListener<ArrayList<ContactsModel>, Any?>) {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    viewModelScope.launch {
                        withContext(coroutineContext) {
                            listener.onSuccess(getContactList(context), "")
                        }
                    }
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun getContactList(context: Context?): ArrayList<ContactsModel> {
        val list = ArrayList<ContactsModel>()
        val cr: ContentResolver? = context?.contentResolver
        val cur = cr?.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null)
        if (cur?.count ?: 0 > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME))

                val stared = cur.getString(cur.getColumnIndex(
                    ContactsContract.Contacts.STARRED))

                val image = getContactImage(cur, context, id)

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0
                ) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null)
                    if (pCur != null)
                        while (pCur.moveToNext()) {
                            val phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                            list.add(ContactsModel(name,
                                PhoneNumberUtils.formatNumber(phoneNo, "IN"),
                                stared == "1",
                                image))
//                        appLog("Name: $name")
//                        appLog("Phone Number: $phoneNo")
                        }
                    pCur?.close()
                }
            }
        }
        cur?.close()
        return list
    }

    private fun getContactImage(cursor: Cursor, context: Context?, contactId: String): Bitmap? {

        /* val my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
             java.lang.String.valueOf(contactId))
         val photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(
             context?.contentResolver,
             my_contact_Uri)
         val buf = BufferedInputStream(photo_stream)
         val my_btmp = BitmapFactory.decodeStream(buf)*/

        try {
            if (cursor.isNull(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI)).not()
            ) {
                val image_uri: String = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                return MediaStore.Images.Media
                    .getBitmap(context?.contentResolver,
                        Uri.parse(image_uri))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}