package app.tapho.ui.scanner.model.AllProducts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Data(
    @PrimaryKey
    val id: String,
    val business_id: String,
    val business_user_category_id: String,
    val business_user_sub_category_id: String,
    val created_at: String,
    val description: String?=null,
    val ean: String,
    val food_type: String?=null,
    val image: String?=null,
    val mrp: String,
    val name: String,
    val price: String?=null,
    val qty: String,
    val status: String,
    val user_id: String,

)