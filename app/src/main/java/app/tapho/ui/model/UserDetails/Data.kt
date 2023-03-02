package app.tapho.ui.model.UserDetails

data class Data(
    val cash_available: String,
    val lifetime_earning: String,
    val country_code: String,
    val created_at: String,
    val dob: String,
    val email: String,
    val email_verify: String,
    val gender: String,
    val id: String,
    val image: String,
    val mobile_no: String,
    val name: String,
    val notification_count: Int,
    val otp: String,
    val recharge_request_pending: Int,
    val referral_code: String,
    val status: String,
    val unique_user_id: String,
    val wallet_amount: String
)