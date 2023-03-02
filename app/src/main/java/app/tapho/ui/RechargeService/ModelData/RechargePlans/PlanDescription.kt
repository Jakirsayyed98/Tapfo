package app.tapho.ui.RechargeService.ModelData.RechargePlans

data class PlanDescription(
    val circle_id: String,
    val id: String,
    val operator_id: String,
    val recharge_amount: String,
    val recharge_long_desc: String,
    val recharge_short_desc: String,
    val recharge_talktime: String,
    val recharge_type: String,
    val recharge_validity: String
)