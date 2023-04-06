package app.tapho.ui.tcash.model

data class Item(
    val business_order_id: String,
    val business_user_item_detail: BusinessUserItemDetail,
    val business_user_item_id: String,
    val created_at: String,
    val id: String,
    val price: String,
    val qty: String,
    val total_price: String
)