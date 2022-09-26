package app.tapho.GetContactList

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object ContactPermissionTraking {

    fun hasCOntactPermissions(context: Context):Boolean =
        EasyPermissions.hasPermissions(
            context,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
        )
}