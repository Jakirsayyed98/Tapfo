package app.tapho.ui.model.NewHomeRes

data class ProfileDetail(
    val cash_available: String,
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
    val otp: String,
    val referral_code: String,
    val status: String,
    val unique_user_id: String,
    val wallet_amount: Int
)