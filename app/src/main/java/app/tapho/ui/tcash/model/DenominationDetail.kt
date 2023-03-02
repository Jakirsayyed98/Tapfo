package app.tapho.ui.tcash.model

data class DenominationDetail(
    val created_at: String,
    val denomination_id: String,
    val denomination_key: String,
    val end_date: String,
    val id: String,
    val price: String,
    val qty: String,
    val response_json: ResponseJson,
    val users_voucher_id: String,
    val voucher_id: String
)