package app.tapho.ui.scanner.model.SearchCurrentOrder

data class Data(
    val business_id: String,
    val business_staff_id: String,
    val code: String,
    val created_at: String,
    val id: String,
    val items: List<Item>,
    val qr_code: String,
    val status: String,
    val total_amount: String,
    val user_id: String
)