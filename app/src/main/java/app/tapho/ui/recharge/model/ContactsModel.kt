package app.tapho.ui.recharge.model

import android.graphics.Bitmap

data class ContactsModel(
    val name: String,
    val phone: String,
    val isStared: Boolean,
    val image: Bitmap?,
)