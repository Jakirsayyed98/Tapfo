package app.tapho.ui.login.model

class LoginData(
    var cash_available: String?,
    var lifetime_earning: String?,
    var country_code: String?,
    var created_at: String?,
    var dob: String?,
    var email: String?,
    var email_verify: String?,
    var gender: String?,
    var id: String?,
    var image: String?,
    var mobile_no: String?,
    var name: String?,
    var otp: String?,
    var status: String?,
    var unique_user_id: String?
) {
    constructor(mobile_no: String) : this("","", "91", "", "", "", "", "",
        "", "", mobile_no, "", "", "", "")


}