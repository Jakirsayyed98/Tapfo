package app.tapho.ui.tcash.model

data class Txn(
    val recharge_detail: List<RechargeDetail>,
    val amount: String,
    val cashback: String,
    val created_at: String,
    val id: String,
    val pay_option: String,
    val payment_amount: String,
    val payment_plus_minus: String,
    val txn_id: String,
    val recharge_id: String,
    val type: String,
    val user_id: String
)

