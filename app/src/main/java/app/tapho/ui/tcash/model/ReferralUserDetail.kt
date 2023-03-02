package app.tapho.ui.tcash.model

data class ReferralUserDetail(
    val cash_available: Double,
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
    val referral_code: String,
    val status: String,
    val unique_user_id: String,
    val wallet_amount: Double
)