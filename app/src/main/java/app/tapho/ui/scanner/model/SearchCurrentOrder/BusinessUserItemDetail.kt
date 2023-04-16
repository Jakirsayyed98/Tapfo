package app.tapho.ui.scanner.model.SearchCurrentOrder

data class BusinessUserItemDetail(
    val business_id: String,
    val business_user_category_id: String,
    val business_user_sub_category_id: String,
    val created_at: String,
    val description: String,
    val ean: String,
    val food_type: String,
    val id: String,
    val image: String,
    val mrp: String,
    val name: String,
    val price: String,
    val qty: String,
    val status: String,
    val user_id: String
)