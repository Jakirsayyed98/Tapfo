package app.tapho.ui.tcash.model

data class BusinessOrderDetail(
    val bill_receipt: String,
    val business_id: String,
    val business_staff_id: String,
    val code: String,
    val created_at: String,
    val id: String,
    val invoice_url: String,
    val items: List<Item>,
    val qr_code: String,
    val staff_detail: List<StaffDetail>,
    val status: String,
    val total_amount: String,
    val user_id: String
)