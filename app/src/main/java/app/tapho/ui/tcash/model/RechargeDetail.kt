package app.tapho.ui.tcash.model

data class RechargeDetail(
    val amount: String,
    val api_response: String,
    val circle_id: String,
    val created_at: String,
    val id: String,
    val number: String,
    val operator_id: String,
    val recharge_type: String,
    val status: String,
    val user_id: String,
    val usertx: String
)