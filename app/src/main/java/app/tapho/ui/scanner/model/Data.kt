package app.tapho.ui.scanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Data(
    @PrimaryKey
    val id: String,
    val buyingQty:Int,
    val barcode: String,
    val created_at: String,
    val description: String,
    val image: String,
    val mrp_price: String,
    val name: String,
    val qty: String,
    val sale_price: String,
    val sku: String,
    val status: String,
    val user_id: String
)