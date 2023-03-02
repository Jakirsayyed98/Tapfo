package app.tapho.ui.tcash.model

data class Txn(

    val referral_user_detail: List<ReferralUserDetail>,
    val referral_user_id: String,
//    val voucher_detail: VoucherDetail,
    val voucher_id: String,
    val recharge_detail: List<RechargeDetail>,
    val amount: String,
    val cashback: String,
    val created_at: String,
    val id: String,
    val pay_option: String,
    val payment_amount: String,
    val payment_plus_minus: String,
    val txn_id: String,
    val status: String,
    val recharge_id: String,
    val type: String,
    val user_id: String
)



