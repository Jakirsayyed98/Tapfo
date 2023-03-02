package app.tapho.ui.tcash.model

data class VoucherDetail(
    val created_at: String,
    val denomination_detail: List<DenominationDetail>,
    val id: String,
    val reponse_status: String,
    val response_json: ResponseJsonX,
    val total_price: String,
    val user_id: String
)