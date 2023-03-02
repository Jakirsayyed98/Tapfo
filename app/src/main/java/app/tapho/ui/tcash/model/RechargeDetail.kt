package app.tapho.ui.tcash.model

import app.tapho.ui.RechargeService.ModelData.RechargeOpretor.Data

data class RechargeDetail(
    val amount: String,
    val api_response: String,
    val created_at: String,
    val id: String,
    val number: String,
    val recharge_type: String,
    val status: String,
    val user_id: String,
    val usertx: String,
    val user_commission: String,
    val user_refund_status: String,
    val operator_detail : Data,
    val circle_detail : app.tapho.ui.RechargeService.ModelData.RechargeCircle.Data,
    val user_commission_amount: String

)