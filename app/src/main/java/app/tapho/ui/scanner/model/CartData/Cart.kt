package app.tapho.ui.scanner.model.CartData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity//(tableName = "AllProduct")
data class Cart(
    @PrimaryKey
    val id: String,
    val business_id: String,
    val business_user_category_id: String,
    val business_user_sub_category_id: String,
    val created_at: String,
    val description: String?=null,
    val ean: String,
    val food_type: String,
    val image: String?=null,
    val mrp: String,
    val name: String,
    val price: String,
    val AvailQty: String,
    val status: String,
    val user_id: String,

    val qty:Int,
    val totalPrice:Double,
    val totalActualPrice:Double,

    //    [{"business_user_item_id":"1","qty":"2","price":"100","total_price":"200"}]
)